"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import {
    buscarPreguntas,
    listarCompetencias,
    listarFactores,
    FiltrosPregunta,
} from "@/services/preguntaService";
import { PreguntaDTO } from "@/types/pregunta";
import PopupError from "@/components/ui/PopupError";

const FILTROS_VACIOS: FiltrosPregunta = { competencia: "", factor: "", pregunta: "" };

const CLASE_SELECT =
    "w-full rounded border border-gray-300 bg-white px-3 py-2 text-sm text-gray-600";

export default function GestionPreguntasPage() {
    const [popup, setPopup] = useState<string | null>(null);
    const router = useRouter();
    const [filtros, setFiltros] = useState<FiltrosPregunta>(FILTROS_VACIOS);
    const [competencias, setCompetencias] = useState<string[]>([]);
    const [factores, setFactores] = useState<string[]>([]);
    const [resultados, setResultados] = useState<PreguntaDTO[] | null>(null);
    const [seleccion, setSeleccion] = useState<PreguntaDTO | null>(null);
    const [aviso, setAviso] = useState("");

    useEffect(() => {
        listarCompetencias().then(setCompetencias);
        listarFactores().then(setFactores);
    }, []);

    const buscar = async () => {
        const hayFiltro = Object.values(filtros).some((v) => v.trim() !== "");
        if (!hayFiltro) {
            setAviso("Ingresá al menos un criterio de búsqueda.");
            return;
        }
        setAviso("");
        setResultados(await buscarPreguntas(filtros));
        setSeleccion(null);
    };

    const limpiar = () => {
        setFiltros(FILTROS_VACIOS);
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
                                    onChange={(e) => setFiltros({ ...filtros, competencia: e.target.value })}
                                    className={`${CLASE_SELECT} mt-1`}
                                >
                                    <option value="" className="bg-white text-black">-</option>
                                    {competencias.map((c) => (
                                        <option key={c} value={c} className="bg-white text-black">{c}</option>
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
                                    {factores.map((f) => (
                                        <option key={f} value={f} className="bg-white text-black">{f}</option>
                                    ))}
                                </select>
                            </label>

                            <label className="text-xs font-semibold text-gray-600">
                                Pregunta
                                <input
                                    placeholder="-"
                                    value={filtros.pregunta}
                                    onChange={(e) => setFiltros({ ...filtros, pregunta: e.target.value })}
                                    className={`${CLASE_SELECT} mt-1 text-black`}
                                />
                            </label>
                        </div>

                        <div className="mt-4 flex gap-2">
                            <button onClick={buscar} className="rounded bg-[#1565c0] px-5 py-2 text-sm font-bold text-white">
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
                                                onClick={() => setPopup("Aún en desarrollo")}
                                                className="rounded-lg bg-[#4472c4] px-6 py-2 text-sm font-bold text-white shadow-md"
                                            >
                                                Modificar
                                            </button>
                                            <button
                                                onClick={() => setPopup("Aún en desarrollo")}
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
            {popup && <PopupError mensaje={popup} onCerrar={() => setPopup(null)} />}
        </main>
    );
}