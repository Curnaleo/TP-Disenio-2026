"use client";

// CU026 - Completar Cuestionario (actor: Candidato)

import { useEffect, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import PopupConfirmacion from "@/components/ui/PopupConfirmacion";
import PopupError from "@/components/ui/PopupError";
import {
    CuestionarioCerradoError,
    guardarBloque,
    iniciarCuestionario,
    obtenerCuestionarioActual,
    recuperarBloque,
} from "@/services/cuestionarioService";
import { BloqueCuestionario, CuestionarioActual, MotivoCierre } from "@/types/cuestionario";

type Pantalla =
    | { tipo: "cargando" }
    | { tipo: "instrucciones" }
    | { tipo: "bloque"; bloque: BloqueCuestionario }
    | { tipo: "cerrado"; motivo: MotivoCierre }
    | { tipo: "completado" };

const MENSAJES_CIERRE: Record<MotivoCierre, { titulo: string; detalle: string }> = {
    // 1.B
    SIN_CONTESTAR: {
        titulo: "Se ha superado el tiempo máximo para poder responder el cuestionario.",
        detalle: 'El cuestionario quedará registrado como "Sin Contestar".',
    },
    // 1.A
    INCOMPLETO: {
        titulo: "Se ha superado el tiempo máximo para poder responder el cuestionario.",
        detalle: 'El cuestionario quedará registrado como "Incompleto".',
    },
    // Precondición: cantidad máxima de accesos
    ACCESOS_SUPERADOS: {
        titulo: "Se ha superado la cantidad máxima de accesos permitidos.",
        detalle: 'El cuestionario quedará registrado como "Incompleto".',
    },
};

const CLASE_BOTON_PRIMARIO =
    "rounded bg-[#1565c0] px-6 py-2 text-sm font-bold text-white disabled:opacity-50";
const CLASE_BOTON_SECUNDARIO =
    "rounded border border-gray-400 bg-gray-100 px-6 py-2 text-sm text-gray-700 disabled:opacity-50";

async function emitirAlerta() {
    if (!window.AudioContext || !navigator.userActivation?.hasBeenActive) return;

    const audio = new AudioContext();
    try {
        await audio.resume();
        const tono = audio.createOscillator();
        const volumen = audio.createGain();
        tono.frequency.value = 440;
        volumen.gain.value = 0.05;
        tono.connect(volumen).connect(audio.destination);
        await new Promise<void>((resolver) => {
            tono.onended = () => resolver();
            tono.start();
            tono.stop(audio.currentTime + 0.15);
        });
    } finally {
        await audio.close();
    }
}

function alertar() {
    void emitirAlerta().catch(() => {});
}

export default function CompletarCuestionarioPage() {
    const router = useRouter();

    const [cuestionario, setCuestionario] = useState<CuestionarioActual | null>(null);
    const [pantalla, setPantalla] = useState<Pantalla>({ tipo: "cargando" });
    const [respuestas, setRespuestas] = useState<Record<number, number>>({});
    const [faltanResponder, setFaltanResponder] = useState(false);
    const [procesando, setProcesando] = useState(false);
    const [confirmarSalida, setConfirmarSalida] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // En desarrollo React ejecuta los efectos dos veces: evita iniciar el cuestionario dos veces
    const inicializado = useRef(false);

    const volverAlMenu = () => router.push("/");

    const mostrarError = (err: unknown, porDefecto: string) =>
        setError(err instanceof Error ? err.message : porDefecto);

    const cerrarCuestionario = (motivo: MotivoCierre) => {
        setPantalla({ tipo: "cerrado", motivo });
        alertar();
    };

    const cargarBloque = async (nroBloque: number) => {
        const bloque = await recuperarBloque(nroBloque);
        setRespuestas({});
        setFaltanResponder(false);
        setPantalla({ tipo: "bloque", bloque });
        window.scrollTo({ top: 0 });
    };

    // Pasos 3 y 4 (o 1.C si ya estaba en proceso)
    const comenzar = async () => {
        setProcesando(true);
        try {
            const nroBloque = await iniciarCuestionario();
            await cargarBloque(nroBloque);
        } catch (err) {
            if (err instanceof CuestionarioCerradoError) {
                cerrarCuestionario(err.motivo);
            } else {
                mostrarError(err, "No se pudo iniciar el cuestionario.");
            }
        } finally {
            setProcesando(false);
        }
    };

    useEffect(() => {
        if (inicializado.current) return;
        inicializado.current = true;

        (async () => {
            try {
                const actual = await obtenerCuestionarioActual();
                setCuestionario(actual);

                switch (actual.estado) {
                    case "ACTIVO":
                        setPantalla({ tipo: "instrucciones" }); // Paso 1
                        break;
                    case "EN_PROCESO":
                        await comenzar(); // 1.C: sigue en el bloque pendiente
                        break;
                    case "COMPLETO":
                        setPantalla({ tipo: "completado" });
                        break;
                    case "INCOMPLETO":
                    case "SIN_CONTESTAR":
                        cerrarCuestionario(actual.estado);
                        break;
                }
            } catch (err) {
                mostrarError(err, "No se pudo recuperar el cuestionario.");
            }
        })();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    // Pasos 5 a 8
    const avanzar = async () => {
        if (!cuestionario || pantalla.tipo !== "bloque") return;
        const { bloque } = pantalla;

        // 6.A: tienen que estar todas respondidas; se conservan las ya marcadas
        const sinResponder = bloque.preguntas.filter((p) => respuestas[p.idPregCuest] === undefined);
        if (sinResponder.length > 0) {
            setFaltanResponder(true);
            alertar();
            document
                .getElementById(`pregunta-${sinResponder[0].idPregCuest}`)
                ?.scrollIntoView({ behavior: "smooth", block: "center" });
            return;
        }

        setProcesando(true);
        try {
            // Paso 7
            const resultado = await guardarBloque(
                bloque.nroBloque,
                bloque.preguntas.map((p) => ({
                    idPregCuest: p.idPregCuest,
                    idOpcionPregunta: respuestas[p.idPregCuest],
                }))
            );

            if (resultado.estado === "COMPLETO") {
                setPantalla({ tipo: "completado" });
            } else if (resultado.siguienteBloque !== undefined) {
                await cargarBloque(resultado.siguienteBloque);
            } else {
                throw new Error("El servidor no informó el siguiente bloque.");
            }
        } catch (err) {
            if (err instanceof CuestionarioCerradoError) {
                cerrarCuestionario(err.motivo);
            } else {
                mostrarError(err, "No se pudo guardar el bloque.");
            }
        } finally {
            setProcesando(false);
        }
    };

    const responder = (idPregCuest: number, idOpcionPregunta: number) =>
        setRespuestas((prev) => ({ ...prev, [idPregCuest]: idOpcionPregunta }));

    // ------------------------------------------------------------ vistas

    const renderInstrucciones = () => (
        <div className="space-y-4">
            <h2 className="text-sm font-bold text-gray-700">Instrucciones</h2>
            <div className="max-h-80 overflow-y-auto whitespace-pre-line rounded bg-white p-4 text-sm leading-relaxed text-gray-700">
                {cuestionario?.instrucciones}
            </div>
            <div className="flex justify-end gap-2">
                {/* 2.A */}
                <button onClick={volverAlMenu} disabled={procesando} className={CLASE_BOTON_SECUNDARIO}>
                    Cancelar
                </button>
                {/* Paso 2 */}
                <button
                    onClick={comenzar}
                    disabled={procesando}
                    className={CLASE_BOTON_PRIMARIO}
                >
                    {procesando ? "Preparando..." : "Aceptar"}
                </button>
            </div>
        </div>
    );

    const renderBloque = (bloque: BloqueCuestionario) => {
        const esUltimo = bloque.nroBloque >= bloque.totalBloques;
        const respondidas = bloque.preguntas.filter((p) => respuestas[p.idPregCuest] !== undefined).length;

        return (
            <div className="space-y-4">
                <div className="flex items-center justify-between text-xs text-gray-600">
                    <span className="font-semibold">
                        Bloque {bloque.nroBloque} de {bloque.totalBloques}
                    </span>
                    <span>
                        {respondidas} de {bloque.preguntas.length} respondidas
                    </span>
                </div>
                <div className="h-1.5 w-full overflow-hidden rounded bg-gray-300">
                    <div
                        className="h-full bg-[#1565c0] transition-all"
                        style={{ width: `${((bloque.nroBloque - 1) / bloque.totalBloques) * 100}%` }}
                    />
                </div>

                {faltanResponder ? (
                    <p role="alert" className="rounded border border-red-300 bg-red-50 px-4 py-2 text-center text-xs font-semibold text-red-700">
                        ⚠ Quedaron preguntas sin responder
                    </p>
                ) : (
                    <p className="rounded border border-yellow-300 bg-yellow-50 px-4 py-2 text-center text-xs font-semibold text-yellow-800">
                        ⚠ Una vez que avance no podrá modificar las respuestas de este bloque
                    </p>
                )}

                <ol className="space-y-3">
                    {bloque.preguntas.map((p) => {
                        const marcada = respuestas[p.idPregCuest];
                        const pendiente = faltanResponder && marcada === undefined;
                        return (
                            <li
                                key={p.idPregCuest}
                                id={`pregunta-${p.idPregCuest}`}
                                className={`rounded border bg-white p-4 ${pendiente ? "border-red-400" : "border-transparent"}`}
                            >
                                <fieldset>
                                    <legend className="text-sm font-semibold text-gray-800">
                                        {p.nroOrden}. {p.textoPregunta}
                                    </legend>
                                    {p.descripcion && (
                                        <p className="mt-1 text-xs text-gray-500">{p.descripcion}</p>
                                    )}
                                    <div className="mt-3 space-y-1">
                                        {[...p.opciones]
                                            .sort((a, b) => a.orden - b.orden)
                                            .map((o) => (
                                                <label
                                                    key={o.idOpcionPregunta}
                                                    className="flex cursor-pointer items-center gap-2 rounded px-2 py-1 text-sm text-gray-700 hover:bg-blue-50"
                                                >
                                                    <input
                                                        type="radio"
                                                        name={`pregunta-${p.idPregCuest}`}
                                                        checked={marcada === o.idOpcionPregunta}
                                                        onChange={() => responder(p.idPregCuest, o.idOpcionPregunta)}
                                                        disabled={procesando}
                                                        className="accent-[#1565c0]"
                                                    />
                                                    {o.descripcion}
                                                </label>
                                            ))}
                                    </div>
                                    {pendiente && (
                                        <p className="mt-2 text-xs text-red-600">Esta pregunta no fue respondida.</p>
                                    )}
                                </fieldset>
                            </li>
                        );
                    })}
                </ol>

                <div className="flex justify-between">
                    <button
                        onClick={() => setConfirmarSalida(true)}
                        disabled={procesando}
                        className={CLASE_BOTON_SECUNDARIO}
                    >
                        Salir
                    </button>
                    <button onClick={avanzar} disabled={procesando} className={CLASE_BOTON_PRIMARIO}>
                        {procesando ? "Guardando..." : esUltimo ? "Finalizar" : "Siguiente ▶"}
                    </button>
                </div>
            </div>
        );
    };

    const renderMensaje = (titulo: string, detalle: string, exito: boolean) => (
        <div className="flex flex-col items-center gap-5 py-8 text-center">
            <p
                className={`rounded border px-6 py-3 text-sm font-bold ${
                    exito
                        ? "border-green-300 bg-green-100 text-green-800"
                        : "border-orange-300 bg-orange-50 text-orange-800"
                }`}
            >
                {titulo}
            </p>
            <p className="max-w-md text-sm text-gray-600">{detalle}</p>
            <button onClick={volverAlMenu} className={CLASE_BOTON_PRIMARIO}>
                Aceptar
            </button>
        </div>
    );

    const renderContenido = () => {
        switch (pantalla.tipo) {
            case "cargando":
                return <p className="py-10 text-center text-sm text-gray-600">Cargando cuestionario...</p>;
            case "instrucciones":
                return renderInstrucciones();
            case "bloque":
                return renderBloque(pantalla.bloque);
            case "cerrado": {
                const m = MENSAJES_CIERRE[pantalla.motivo];
                return renderMensaje(m.titulo, m.detalle, false);
            }
            case "completado":
                return renderMensaje(
                    "El cuestionario ha sido completado con éxito",
                    'Los resultados han sido guardados y el estado del cuestionario cambió a "Completo".',
                    true
                );
        }
    };

    const enWizard = pantalla.tipo === "bloque";

    return (
        <main className="min-h-screen bg-white p-6 text-black">
            <div className="mx-auto max-w-3xl bg-[#e5e5e5] shadow">
                <header className="flex items-center justify-between bg-[#0078d4] px-4 py-2 text-white">
                    <h1 className="font-bold">Cuestionario</h1>
                    {cuestionario && <span className="text-xs">{cuestionario.nombreCandidato}</span>}
                </header>

                <div className="p-4">{renderContenido()}</div>
            </div>

            {confirmarSalida && enWizard && (
                <PopupConfirmacion
                    titulo="Salir del cuestionario"
                    mensaje="Las respuestas de este bloque no se guardarán. Podrás continuar más tarde desde este bloque. ¿Desea salir?"
                    onSi={volverAlMenu}
                    onNo={() => setConfirmarSalida(false)}
                />
            )}

            {error && <PopupError mensaje={error} onCerrar={() => setError(null)} />}
        </main>
    );
}
