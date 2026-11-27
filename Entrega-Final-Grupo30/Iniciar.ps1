param([switch]$SinNavegador)

$ErrorActionPreference = 'Stop'
$dbContainer = 'grupo30-entrega-postgres'
$dbPort = 55430
$dbStarted = $false
$backendProcess = $null
$frontendProcess = $null
$logFolder = $null
$exitStatus = 0

function Assert-LocalPortAvailable([int]$Port) {
    $listener = [Net.Sockets.TcpListener]::new([Net.IPAddress]::Loopback, $Port)
    $listener.ExclusiveAddressUse = $true
    try {
        $listener.Start()
    } catch {
        throw "El puerto $Port no esta disponible. Cierra la instancia que lo usa y vuelve a ejecutar el lanzador."
    } finally {
        $listener.Stop()
    }
}

function Wait-Http([string]$Url, [Diagnostics.Process]$Process) {
    $deadline = (Get-Date).AddSeconds(75)
    do {
        if ($Process.HasExited) {
            throw "El proceso para $Url termino con codigo $($Process.ExitCode)."
        }
        try {
            $response = Invoke-WebRequest -UseBasicParsing -Uri $Url -TimeoutSec 3
            if ($response.StatusCode -eq 200) { return }
        } catch {
            Start-Sleep -Milliseconds 700
        }
    } while ((Get-Date) -lt $deadline)
    throw "El servicio en $Url no respondio a tiempo."
}

try {
    $java = (Get-Command java.exe -ErrorAction SilentlyContinue).Source
    $node = (Get-Command node.exe -ErrorAction SilentlyContinue).Source
    if (-not $java -or -not $node) {
        throw 'Se necesita Java 21 o superior y Node.js 24 disponibles en el PATH.'
    }

    $docker = (Get-Command docker.exe -ErrorAction SilentlyContinue).Source
    if (-not $docker) {
        $docker = @(
            "$env:ProgramFiles\Docker\Docker\resources\bin\docker.exe"
            "$env:ProgramFiles\Docker\Docker.staging\resources\bin\docker.exe"
        ) | Where-Object { Test-Path -LiteralPath $_ } | Select-Object -First 1
    }
    if (-not $docker) { throw 'No se encontro Docker Desktop.' }

    $jar = 'programa/backend.jar'
    $server = 'programa/frontend/server.js'
    foreach ($file in $jar, $server) {
        if (-not (Test-Path -LiteralPath (Join-Path $PSScriptRoot $file) -PathType Leaf)) {
            throw "Falta $file. Extrae el ZIP completo antes de ejecutar Iniciar.cmd."
        }
    }

    foreach ($port in 3000, 8080, $dbPort) { Assert-LocalPortAvailable $port }
    & $docker info --format '{{.ServerVersion}}' | Out-Null
    if ($LASTEXITCODE -ne 0) { throw 'Abre Docker Desktop y espera a que el motor este listo.' }

    $containers = @(& $docker ps -a --format '{{.Names}}')
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo consultar Docker.' }
    $labelName = 'ar.utn.grupo30.entrega'
    Write-Host 'Iniciando PostgreSQL de demostracion...'
    if ($dbContainer -in $containers) {
        $containerInfo = & $docker inspect $dbContainer
        if ($LASTEXITCODE -ne 0) { throw 'No se pudo inspeccionar el contenedor de demostracion.' }
        $dbInfo = @(($containerInfo -join [Environment]::NewLine) | ConvertFrom-Json)[0]
        if ($dbInfo.Config.Labels.$labelName -ne 'postgres') {
            throw "El nombre de contenedor $dbContainer esta en uso por otra instalacion."
        }
        if ($dbInfo.State.Running) { throw 'El contenedor de demostracion ya esta en ejecucion.' }
        & $docker start $dbContainer | Out-Null
    } else {
        $dockerRun = @(
            'run', '-d', '--name', $dbContainer, '--label', "$labelName=postgres"
            '-p', ('127.0.0.1:{0}:5432' -f $dbPort)
            '-e', 'POSTGRES_DB=capital_humano', '-e', 'POSTGRES_USER=postgres'
            '-e', 'POSTGRES_PASSWORD=postgres', 'postgres:17'
        )
        & $docker @dockerRun | Out-Null
    }
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo iniciar PostgreSQL.' }
    $dbStarted = $true

    $dbDeadline = (Get-Date).AddSeconds(60)
    do {
        & $docker exec $dbContainer pg_isready -h 127.0.0.1 -U postgres -d capital_humano | Out-Null
        if ($LASTEXITCODE -eq 0) { break }
        if ((Get-Date) -ge $dbDeadline) { throw 'PostgreSQL no estuvo listo a tiempo.' }
        Start-Sleep -Seconds 1
    } while ($true)

    $sessionName = [Guid]::NewGuid().ToString('N').Substring(0, 8)
    $logFolder = Join-Path $env:TEMP "Grupo30-entrega-$sessionName"
    New-Item -ItemType Directory -Path $logFolder | Out-Null
    $env:SPRING_DATASOURCE_URL = "jdbc:postgresql://127.0.0.1:$dbPort/capital_humano"
    $env:SPRING_DATASOURCE_USERNAME = 'postgres'
    $env:SPRING_DATASOURCE_PASSWORD = 'postgres'
    $env:SERVER_PORT = '8080'
    $env:APP_CORS_ALLOWED_ORIGINS = 'http://localhost:3000'
    $env:HOSTNAME = '127.0.0.1'
    $env:PORT = '3000'

    $backendArgs = @{
        FilePath = $java
        ArgumentList = '-jar', $jar, '--server.address=127.0.0.1'
        WorkingDirectory = $PSScriptRoot
        WindowStyle = 'Hidden'
        RedirectStandardOutput = "$logFolder/backend.log"
        RedirectStandardError = "$logFolder/backend-error.log"
        PassThru = $true
    }
    Write-Host 'Iniciando backend...'
    $backendProcess = Start-Process @backendArgs
    Wait-Http 'http://localhost:8080/v3/api-docs' $backendProcess

    $frontendArgs = @{
        FilePath = $node
        ArgumentList = $server
        WorkingDirectory = $PSScriptRoot
        WindowStyle = 'Hidden'
        RedirectStandardOutput = "$logFolder/frontend.log"
        RedirectStandardError = "$logFolder/frontend-error.log"
        PassThru = $true
    }
    Write-Host 'Iniciando frontend...'
    $frontendProcess = Start-Process @frontendArgs
    Wait-Http 'http://localhost:3000/' $frontendProcess

    Write-Host 'Sistema listo en http://localhost:3000/'
    Write-Host "Registros en $logFolder"
    if (-not $SinNavegador) {
        try { Start-Process 'http://localhost:3000/' } catch { Write-Host 'Abre http://localhost:3000/ en tu navegador.' }
    }
    $null = Read-Host 'Presiona Enter en esta ventana para detener el sistema'
} catch {
    Write-Host "ERROR: $($_.Exception.Message)" -ForegroundColor Red
    if ($logFolder) { Write-Host "Registros en $logFolder" }
    $exitStatus = 1
} finally {
    foreach ($childProcess in $frontendProcess, $backendProcess) {
        if ($childProcess -and -not $childProcess.HasExited) {
            Stop-Process -Id $childProcess.Id -ErrorAction SilentlyContinue
        }
    }
    if ($dbStarted) {
        & $docker stop $dbContainer | Out-Null
        if ($LASTEXITCODE -ne 0) { $exitStatus = 1 }
    }
}
exit $exitStatus
