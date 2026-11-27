"use client";

import { useEffect, useRef, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import {
    buscarPreguntas,
    eliminarPregunta,
    FiltrosPregunta,
} from "@/services/preguntaService";
import {
    listarCompetenciasCatalogo,
    listarFactoresCatalogo,
} from "@/services/catalogoService";
import { ApiError } from "@/lib/apiClient";
import { CompetenciaDTO } from "@/types/competencia";
import { FactorDTO } from "@/types/factor";
import { PreguntaDTO } from "@/types/pregunta";
import ModalEditarPregunta from "@/features/preguntas/ModalEditarPregunta";
import PopupConfirmacion from "@/components/ui/PopupConfirmacion";
import PopupError from "@/components/ui/PopupError";

const FILTROS_VACIOS: FiltrosPregunta = { competencia: "", factor: "", pregunta: "" };

const CLASE_SELECT =
    "w-full rounded border border-gray-300 bg-white px-3 py-2 text-sm text-gray-600";

export default function GestionPreguntasPage() {
    const [popup, setPopup] = useState<string | null>(null);
    const router = useRouter();
    const [filtros, setFiltros] = useState<FiltrosPregunta>(FILTROS_VACIOS);
    const [competencias, setCompetencias] = useState<CompetenciaDTO[]>([]);
    const [factores, setFactores] = useState<FactorDTO[]>([]);
    const [resultados, setResultados] = useState<PreguntaDTO[] | null>(null);
    const [seleccion, setSeleccion] = useState<PreguntaDTO | null>(null);
    const [editando, setEditando] = useState<PreguntaDTO | null>(null);
    const [confirmarBaja, setConfirmarBaja] = useState<PreguntaDTO | null>(null);
    const [ultimosFiltros, setUltimosFiltros] = useState<FiltrosPregunta | null>(null);
    const [mensajeExito, setMensajeExito] = useState<string | null>(null);
    const [aviso, setAviso] = useState("");
    const versionBusqueda = useRef(0);

    useEffect(() => {
        Promise.all([listarCompetenciasCatalogo(), listarFactoresCatalogo()])
            .then(([comps, facts]) => {
                setCompetencias(comps);
                setFactores(facts);
            })
            .catch((e) => {
                setPopup(
                    e instanceof ApiError
                        ? e.message
                        : "No se pudieron cargar los filtros de búsqueda."
                );
            });
    }, []);

    const factoresVisibles = filtros.competencia
        ? factores.filter((f) => f.nombreCompetencia === filtros.competencia)
        : factores;

    useEffect(() => {
        if (!mensajeExito) return;
        const alPresionarTecla = () => setMensajeExito(null);
        window.addEventListener("keydown", alPresionarTecla);
        return () => window.removeEventListener("keydown", alPresionarTecla);
    }, [mensajeExito]);

    const buscar = async (criterios: FiltrosPregunta = filtros, confirmacion?: string) => {
        const hayFiltro = Object.values(criterios).some((v) => v.trim() !== "");
        if (!hayFiltro) {
            setAviso("Ingresá al menos un criterio de búsqueda.");
            return;
        }
        setAviso("");
        const version = ++versionBusqueda.current;
        try {
            const coincidencias = await buscarPreguntas(criterios);
            if (version !== versionBusqueda.current) return;
            setUltimosFiltros(criterios);
            setResultados(coincidencias);
            setSeleccion(null);
            if (confirmacion) {
                setMensajeExito(confirmacion);
            }
        } catch (e) {
            if (version !== versionBusqueda.current) return;
            if (confirmacion) {
                setResultados(null);
                setPopup("El cambio se guardó. No se pudo actualizar la grilla. Volvé a buscar.");
                return;
            }
            setPopup(e instanceof ApiError ? e.message : "Error al buscar preguntas.");
        }
    };

    const confirmarEliminar = async () => {
        if (!confirmarBaja) return;
        const { idPregunta, nombre } = confirmarBaja;
        setConfirmarBaja(null);
        try {
            await eliminarPregunta(idPregunta);
            setSeleccion(null);
            await buscar(
                ultimosFiltros ?? filtros,
                `Los datos de la pregunta ${nombre} han sido eliminados del sistema.`
            );
        } catch (e) {
            setPopup(e instanceof ApiError ? e.message : "No se pudo eliminar la pregunta.");
        }
    };

    const limpiar = () => {
        versionBusqueda.current++;
        setFiltros(FILTROS_VACIOS);
        setUltimosFiltros(null);
        setResultados(null);
        setSeleccion(null);
        setAviso("");
    };

    const salir = () => router.push("/");

    return (
        <main className="min-h-screen bg-white p-6 text-black">
            <div className="mx-auto max-w-4xl bg-[#e5e5e5] shadow">
                {/* Barra de título con cruz de cerrar */}
                <header className="flex items-center justify-between bg-[#0078d4] px-4 py-2 text-white">
                    <h1 className="font-bold">Gestión de Preguntas</h1>
                    <button onClick={salir} aria-label="Cerrar" className="text-xl leading-none">
                        ✕
                    </button>
                </header>

                <div className="p-4">
                    {/* Filtros */}
                    <p className="mb-2 text-sm font-semibold text-gray-700">🔍 Filtros de búsqueda</p>
                    <section className="rounded bg-white p-4">
                        <div className="grid grid-cols-3 gap-6">
                            <label className="text-xs font-semibold text-gray-600">
                                Competencia
                                <select
                                    value={filtros.competencia}
                                    onChange={(e) =>
                                        setFiltros({ ...filtros, competencia: e.target.value, factor: "" })
                                    }
                                    className={`${CLASE_SELECT} mt-1`}
                                >
                                    <option value="" className="bg-white text-black">-</option>
                                    {competencias.map((c) => (
                                        <option key={c.codigo} value={c.nombre} className="bg-white text-black">
                                            {c.nombre}
                                        </option>
                                    ))}
                                </select>
                            </label>

                            <label className="text-xs font-semibold text-gray-600">
                                Factor
                                <select
                                    value={filtros.factor}
                                    onChange={(e) => setFiltros({ ...filtros, factor: e.target.value })}
                                    className={`${CLASE_SELECT} mt-1`}
                                >
                                    <option value="" className="bg-white text-black">-</option>
                                    {factoresVisibles.map((f) => (
                                        <option key={f.codigo} value={f.nombre} className="bg-white text-black">
                                            {f.nombre}
                                        </option>
                                    ))}
                                </select>
                            </label>

                            <label className="text-xs font-semibold text-gray-600">
                                Pregunta
                                <input
                                    placeholder="-"
                                    value={filtros.pregunta}
                                    onChange={(e) => setFiltros({ ...filtros, pregunta: e.target.value.toUpperCase() })}
                                    className={`${CLASE_SELECT} mt-1 text-black`}
                                />
                            </label>
                        </div>

                        <div className="mt-4 flex gap-2">
                            <button onClick={() => void buscar()} className="rounded bg-[#1565c0] px-5 py-2 text-sm font-bold text-white">
                                Buscar
                            </button>
                            <button onClick={limpiar} className="rounded border border-gray-400 bg-white px-3 py-2 text-xs text-gray-600">
                                Limpiar filtros
                            </button>
                        </div>
                        {aviso && <p className="mt-2 text-sm text-red-600">{aviso}</p>}
                    </section>

                    {/* Crear pregunta */}
                    <div className="mt-4 flex justify-end">
                        <Link
                            href="/consultores/preguntas/nueva"
                            className="rounded-lg bg-[#4472c4] px-6 py-3 text-sm font-bold text-white shadow-md"
                        >
                            Crear pregunta
                        </Link>
                    </div>

                    {/* Resultados: el bloque blanco siempre está visible */}
                    <h2 className="mt-2 text-sm font-bold">Resultados de Búsqueda</h2>
                    <section className="mt-2 min-h-[220px] rounded bg-white p-4">
                        {resultados && (
                            <>
                                <p className="text-xs text-gray-600">
                                    Se encontraron {resultados.length} resultado(s)
                                </p>
                                <table className="mt-2 w-full border-collapse text-sm">
                                    <thead className="bg-[#4472c4] text-white">
                                    <tr>
                                        <th className="p-2">Texto Pregunta</th>
                                        <th className="p-2">Competencia</th>
                                        <th className="p-2">Factor</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {resultados.map((p) => (
                                        <tr
                                            key={p.idPregunta}
                                            onClick={() => setSeleccion(p)}
                                            className={`cursor-pointer border-b border-gray-200 text-center ${
                                                seleccion?.idPregunta === p.idPregunta
                                                    ? "bg-[#d6e4f0] font-semibold text-[#1a3a6b]"
                                                    : ""
                                            }`}
                                        >
                                            <td className="p-2 text-left">{p.textoPregunta}</td>
                                            <td className="p-2">{p.nombreCompetencia}</td>
                                            <td className="p-2">{p.nombreFactor}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>

                                {seleccion && (
                                    <div className="mt-6 border-t border-gray-300 pt-2">
                                        <p className="text-xs italic text-blue-700">
                                            pregunta seleccionada: {seleccion.textoPregunta}
                                        </p>
                                        <div className="mt-2 flex items-center gap-3">
                                            <span className="text-sm font-semibold">Acciones:</span>
                                            <button
                                                onClick={() => setEditando(seleccion)}
                                                className="rounded-lg bg-[#4472c4] px-6 py-2 text-sm font-bold text-white shadow-md"
                                            >
                                                Modificar
                                            </button>
                                            <button
                                                onClick={() => setConfirmarBaja(seleccion)}
                                                className="rounded-lg bg-[#c0392b] px-6 py-2 text-sm font-bold text-white shadow-md"
                                            >
                                                Eliminar
                                            </button>
                                        </div>
                                    </div>
                                )}
                            </>
                        )}
                    </section>

                    {/* Salir abajo a la derecha */}
                    <div className="mt-4 flex justify-end">
                        <button
                            onClick={salir}
                            className="rounded-lg bg-[#7f8c8d] px-8 py-3 text-sm font-bold text-white shadow-md"
                        >
                            Salir
                        </button>
                    </div>
                </div>
            </div>
            {editando && (
                <ModalEditarPregunta
                    pregunta={editando}
                    onCancelar={() => setEditando(null)}
                    onGuardada={() => {
                        setEditando(null);
                        setSeleccion(null);
                        void buscar(ultimosFiltros ?? filtros, "La operación ha culminado con éxito");
                    }}
                />
            )}
            {confirmarBaja && (
                <PopupConfirmacion
                    titulo="Confirmar eliminación"
                    mensaje={`Los datos de la pregunta ${confirmarBaja.nombre} serán eliminados del sistema`}
                    textoSi="ELIMINAR"
                    textoNo="CANCELAR"
                    onSi={() => void confirmarEliminar()}
                    onNo={() => setConfirmarBaja(null)}
                />
            )}
            {mensajeExito && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
                    <div
                        role="dialog"
                        aria-modal="true"
                        aria-label="Operación exitosa"
                        className="w-full max-w-md overflow-hidden rounded-xl border border-neutral-700 bg-[#121212] shadow-2xl"
                    >
                        <div className="bg-[#0078d4] px-5 py-3 text-sm font-bold text-white">Información</div>
                        <p className="px-8 py-8 text-center text-base text-neutral-300">{mensajeExito}</p>
                        <div className="flex justify-center pb-6">
                            <button
                                autoFocus
                                onClick={() => setMensajeExito(null)}
                                className="rounded-lg border border-neutral-700 bg-[#1e1e1e] px-8 py-2 text-sm text-neutral-200"
                            >
                                Aceptar
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {popup && <PopupError mensaje={popup} onCerrar={() => setPopup(null)} />}
        </main>
    );
}
