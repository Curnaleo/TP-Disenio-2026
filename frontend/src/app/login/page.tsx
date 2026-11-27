"use client";

// CU001 - Autenticar usuario.
// Pantalla APARTADA: el login no es uno de los 5 CU pedidos, se agregó solo para no depender de Postman.
// Para quitarla: borrar la carpeta app/login y el enlace "Iniciar sesión" de app/page.tsx.

import { useState } from "react";
import { useRouter } from "next/navigation";
import PopupError from "@/components/ui/PopupError";
import { ApiError } from "@/lib/apiClient";
import { loginCandidato, loginConsultor } from "@/services/authService";
import { TipoDocumento } from "@/types/auth";

type TipoAcceso = "consultor" | "candidato";

const TIPOS_DOCUMENTO: TipoDocumento[] = ["DNI", "LE", "LC", "PP"];

const CLASE_INPUT =
    "mt-1 w-full rounded border border-gray-300 bg-white px-3 py-2 text-sm text-black";
const CLASE_BOTON_PRIMARIO =
    "rounded bg-[#1565c0] px-6 py-2 text-sm font-bold text-white disabled:opacity-50";
const CLASE_BOTON_SECUNDARIO =
    "rounded border border-gray-400 bg-white px-4 py-2 text-sm text-gray-700";
const CLASE_OPCION =
    "block w-full rounded-lg border border-gray-300 bg-white p-4 text-left shadow-sm hover:border-[#0078d4] hover:bg-blue-50";

export default function LoginPage() {
    const router = useRouter();

    const [tipoAcceso, setTipoAcceso] = useState<TipoAcceso | null>(null);

    const [usuario, setUsuario] = useState("");
    const [contrasena, setContrasena] = useState("");

    const [tipoDocumento, setTipoDocumento] = useState<TipoDocumento>("DNI");
    const [nroDocumento, setNroDocumento] = useState("");
    const [clave, setClave] = useState("");

    const [procesando, setProcesando] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const blanquearCampos = () => {
        setUsuario("");
        setContrasena("");
        setTipoDocumento("DNI");
        setNroDocumento("");
        setClave("");
    };

    // Observación del CU: en cualquier momento se puede volver a elegir el tipo de acceso
    const cambiarTipoAcceso = (tipo: TipoAcceso | null) => {
        blanquearCampos();
        setTipoAcceso(tipo);
    };

    const ingresar = async (e: React.FormEvent) => {
        e.preventDefault();
        setProcesando(true);
        try {
            if (tipoAcceso === "consultor") {
                // Pasos 4 a 6
                await loginConsultor({ usuario, contrasena });
                router.push("/");
            } else {
                // 3.A.1 a 3.A.3
                await loginCandidato({ tipoDocumento, nroDocumento: nroDocumento.trim(), clave });
                router.push("/cuestionario");
            }
        } catch (err) {
            // 5.A.1 / 3.A.2.2: el back ya devuelve el mensaje del CU
            setError(
                err instanceof ApiError
                    ? err.message
                    : "No se pudo conectar con el servidor. Verifique que el back esté levantado."
            );
            setProcesando(false);
        }
    };

    // 5.A.3 / 3.A.2.4: al cerrar el error se blanquean los campos
    const cerrarError = () => {
        setError(null);
        blanquearCampos();
    };

    const renderSeleccion = () => (
        <div className="space-y-3">
            <p className="text-sm text-gray-700">Seleccione el tipo de acceso:</p>
            <button onClick={() => cambiarTipoAcceso("consultor")} className={CLASE_OPCION}>
                <p className="font-bold text-[#1565c0]">Ingresar como usuario consultor</p>
            </button>
            <button onClick={() => cambiarTipoAcceso("candidato")} className={CLASE_OPCION}>
                <p className="font-bold text-[#1565c0]">Realizar cuestionario</p>
            </button>
        </div>
    );

    const renderFormulario = () => (
        <form onSubmit={ingresar} className="space-y-4">
            <h2 className="text-sm font-bold text-gray-700">
                {tipoAcceso === "consultor" ? "Ingresar como usuario consultor" : "Realizar cuestionario"}
            </h2>

            <div className="space-y-3 rounded bg-white p-4">
                {tipoAcceso === "consultor" ? (
                    <>
                        <label className="block text-xs font-semibold text-gray-600">
                            Usuario
                            <input
                                autoFocus
                                required
                                maxLength={15}
                                autoComplete="username"
                                value={usuario}
                                // Siempre en mayúsculas, sin importar Bloq Mayús
                                onChange={(e) => setUsuario(e.target.value.toUpperCase())}
                                className={CLASE_INPUT}
                            />
                        </label>
                        <label className="block text-xs font-semibold text-gray-600">
                            Contraseña
                            <input
                                required
                                type="password"
                                maxLength={20}
                                autoComplete="current-password"
                                value={contrasena}
                                onChange={(e) => setContrasena(e.target.value)}
                                className={CLASE_INPUT}
                            />
                        </label>
                    </>
                ) : (
                    <>
                        <div className="grid grid-cols-3 gap-3">
                            <label className="block text-xs font-semibold text-gray-600">
                                Tipo
                                <select
                                    autoFocus
                                    value={tipoDocumento}
                                    onChange={(e) => setTipoDocumento(e.target.value as TipoDocumento)}
                                    className={CLASE_INPUT}
                                >
                                    {TIPOS_DOCUMENTO.map((t) => (
                                        <option key={t} value={t}>
                                            {t}
                                        </option>
                                    ))}
                                </select>
                            </label>
                            <label className="col-span-2 block text-xs font-semibold text-gray-600">
                                Número de documento
                                <input
                                    required
                                    value={nroDocumento}
                                    onChange={(e) => setNroDocumento(e.target.value)}
                                    className={CLASE_INPUT}
                                />
                            </label>
                        </div>
                        <label className="block text-xs font-semibold text-gray-600">
                            Clave
                            <input
                                required
                                type="password"
                                autoComplete="off"
                                value={clave}
                                onChange={(e) => setClave(e.target.value)}
                                className={CLASE_INPUT}
                            />
                        </label>
                    </>
                )}
            </div>

            <div className="flex justify-between">
                <button type="button" onClick={() => cambiarTipoAcceso(null)} className={CLASE_BOTON_SECUNDARIO}>
                    ◀ Cambiar tipo de acceso
                </button>
                <button type="submit" disabled={procesando} className={CLASE_BOTON_PRIMARIO}>
                    {procesando ? "Ingresando..." : "Ingresar"}
                </button>
            </div>
        </form>
    );

    return (
        <main className="min-h-screen bg-white p-6 text-black">
            <div className="mx-auto max-w-md bg-[#e5e5e5] shadow">
                <header className="flex items-center justify-between bg-[#0078d4] px-4 py-2 text-white">
                    <h1 className="font-bold">Autenticar usuario</h1>
                    {/* Salir: disponible en cualquier flujo */}
                    <button onClick={() => router.push("/")} aria-label="Salir" className="text-xl leading-none">
                        ✕
                    </button>
                </header>

                <div className="p-4">{tipoAcceso ? renderFormulario() : renderSeleccion()}</div>
            </div>

            {error && <PopupError mensaje={error} onCerrar={cerrarError} />}
        </main>
    );
}
