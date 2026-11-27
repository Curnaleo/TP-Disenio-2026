@echo off
powershell.exe -NoLogo -NoProfile -ExecutionPolicy RemoteSigned -File "%~dp0Iniciar.ps1" %*
if errorlevel 1 (
    pause
    exit /b 1
)
