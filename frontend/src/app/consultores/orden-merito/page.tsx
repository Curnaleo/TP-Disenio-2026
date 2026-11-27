"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import PopupError from "@/components/ui/PopupError";
import {
    buscarPuestos,
    FiltrosPuesto,
    generarOrdenMerito,
    listarEvaluaciones,
} from "@/services/reporteService";
import { EvaluacionDTO } from "@/types/evaluacion";
import { CandidatoOrdenMeritoDTO } from "@/types/ordenMerito";
import { PuestoDTO } from "@/types/puesto";

const FILTROS_VACIOS: FiltrosPuesto = { codigo: "", nombre: "", empresa: "" };

const CLASE_INPUT =
    "mt-1 w-full rounded border border-gray-300 bg-white px-3 py-2 text-sm text-black";

function formatearFechaHora(valor: string | null): string {
    if (!valor) return "-";
    const fecha = new Date(valor);
    if (Number.isNaN(fecha.getTime())) return valor;
    return fecha.toLocaleString("es-AR", {
        dateStyle: "short",
        timeStyle: "short",
    });
}

export default function OrdenMeritoPage() {
    const router = useRouter();

    const [filtros, setFiltros] = useState<FiltrosPuesto>(FILTROS_VACIOS);
    const [puestos, setPuestos] = useState<PuestoDTO[] | null>(null);
    const [puestoSeleccionado, setPuestoSeleccionado] = useState<PuestoDTO | null>(null);

    const [evaluaciones, setEvaluaciones] = useState<EvaluacionDTO[]>([]);
    const [evaluacionesIds, setEvaluacionesIds] = useState<number[]>([]);

    const [ordenMerito, setOrdenMerito] = useState<CandidatoOrdenMeritoDTO[] | null>(null);
    const [popup, setPopup] = useState<string | null>(null);

    const buscar = async () => {
        try {
            const lista = await buscarPuestos(filtros);
            setPuestos(lista);
            setPuestoSeleccionado(null);
            setEvaluaciones([]);
            setEvaluacionesIds([]);
            setOrdenMerito(null);
        } catch (err) {
            setPopup(err instanceof Error ? err.message : "Error al buscar puestos.");
        }
    };

    const limpiar = () => {
        setFiltros(FILTROS_VACIOS);
        setPuestos(null);
        setPuestoSeleccionado(null);
        setEvaluaciones([]);
        setEvaluacionesIds([]);
        setOrdenMerito(null);
    };

    const seleccionarPuesto = async (puesto: PuestoDTO) => {
        setPuestoSeleccionado(puesto);
        setOrdenMerito(null);
        setEvaluacionesIds([]);
        try {
            const lista = await listarEvaluaciones(puesto.puestoId);
            setEvaluaciones(lista);
        } catch (err) {
            setPopup(err instanceof Error ? err.message : "Error al cargar evaluaciones.");
        }
    };

    const toggleEvaluacion = (id: number) => {
        setEvaluacionesIds((prev) =>
            prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
        );
    };

    const emitirOrdenMerito = async () => {
        if (!puestoSeleccionado) {
            setPopup("Seleccioná un puesto para emitir el orden de mérito.");
            return;
        }
        try {
            const resultado = await generarOrdenMerito(
                puestoSeleccionado.puestoId,
                evaluacionesIds.length > 0 ? evaluacionesIds : undefined
            );
            setOrdenMerito(resultado);
        } catch (err) {
            setPopup(err instanceof Error ? err.message : "Error al generar el orden de mérito.");
        }
    };

    const salir = () => router.push("/");

    let posicionActual = 0;
    const evaluacionesIncluidas =
        evaluacionesIds.length > 0
            ? evaluaciones.filter((ev) => evaluacionesIds.includes(ev.idEvaluacion))
            : evaluaciones;

    return (
        <main className="min-h-screen bg-white p-6 text-black print:p-0">
            <div className="mx-auto max-w-5xl bg-[#e5e5e5] shadow print:max-w-none print:bg-white print:shadow-none">
                {/* Barra de título con cruz de cerrar */}
                <header className="flex items-center justify-between bg-[#0078d4] px-4 py-2 text-white print:hidden">
                    <h1 className="font-bold">Emitir Orden de Mérito</h1>
                    <button onClick={salir} aria-label="Cerrar" className="text-xl leading-none">
                        ✕
                    </button>
                </header>

                <div className="space-y-4 p-4 print:p-0">
                    {/* Paso 1: Filtros de búsqueda de Puesto */}
                    <div className="print:hidden">
                        <p className="mb-2 text-sm font-semibold text-gray-700">
                            🔍 Buscar Puesto
                        </p>
                        <section className="rounded bg-white p-4">
                            <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
                                <label className="text-xs font-semibold text-gray-600">
                                    Código
                                    <input
                                        placeholder="-"
                                        value={filtros.codigo}
                                        onChange={(e) =>
                                            setFiltros({ ...filtros, codigo: e.target.value.toUpperCase() })
                                        }
                                        className={CLASE_INPUT}
                                    />
                                </label>

                                <label className="text-xs font-semibold text-gray-600">
                                    Nombre de Puesto
                                    <input
                                        placeholder="-"
                                        value={filtros.nombre}
                                        onChange={(e) =>
                                            setFiltros({ ...filtros, nombre: e.target.value.toUpperCase() })
                                        }
                                        className={CLASE_INPUT}
                                    />
                                </label>

                                <label className="text-xs font-semibold text-gray-600">
                                    Empresa
                                    <input
                                        placeholder="-"
                                        value={filtros.empresa}
                                        onChange={(e) =>
                                            setFiltros({ ...filtros, empresa: e.target.value.toUpperCase() })
                                        }
                                        className={CLASE_INPUT}
                                    />
                                </label>
                            </div>

                            <div className="mt-4 flex gap-2">
                                <button
                                    onClick={buscar}
                                    className="rounded bg-[#1565c0] px-5 py-2 text-sm font-bold text-white"
                                >
                                    Buscar
                                </button>
                                <button
                                    onClick={limpiar}
                                    className="rounded border border-gray-400 bg-white px-3 py-2 text-xs text-gray-600"
                                >
                                    Limpiar filtros
                                </button>
                            </div>
                        </section>
                    </div>

                    {/* Resultados de Puestos */}
                    {puestos && (
                        <div className="print:hidden">
                            <h2 className="mb-2 text-sm font-bold">Puestos encontrados</h2>
                            <section className="rounded bg-white p-4">
                                <p className="text-xs text-gray-600">
                                    Se encontraron {puestos.length} puesto(s)
                                </p>
                                {puestos.length > 0 && (
                                    <table className="mt-2 w-full border-collapse text-sm">
                                        <thead className="bg-[#4472c4] text-white">
                                            <tr>
                                                <th className="p-2 text-left">Código</th>
                                                <th className="p-2 text-left">Puesto</th>
                                                <th className="p-2 text-left">Empresa</th>
                                                <th className="p-2 text-center">Candidatos</th>
                                                <th className="p-2 text-center">Completas</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {puestos.map((p) => (
                                                <tr
                                                    key={p.puestoId}
                                                    onClick={() => seleccionarPuesto(p)}
                                                    className={`cursor-pointer border-b border-gray-200 ${
                                                        puestoSeleccionado?.puestoId === p.puestoId
                                                            ? "bg-[#d6e4f0] font-semibold text-[#1a3a6b]"
                                                            : "hover:bg-gray-50"
                                                    }`}
                                                >
                                                    <td className="p-2">{p.codigo}</td>
                                                    <td className="p-2">{p.nombre}</td>
                                                    <td className="p-2">{p.empresa}</td>
                                                    <td className="p-2 text-center">
                                                        {p.cantidadCandidatos ?? 0}
                                                    </td>
                                                    <td className="p-2 text-center">
                                                        {p.evaluacionesCompletas ?? 0}
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                )}
                            </section>
                        </div>
                    )}

                    {/* Paso 2: Evaluaciones del puesto seleccionado */}
                    {puestoSeleccionado && (
                        <div className="print:hidden">
                            <h2 className="mb-2 text-sm font-bold">
                                Evaluaciones de {puestoSeleccionado.nombre}
                            </h2>
                            <section className="rounded bg-white p-4">
                                {evaluaciones.length === 0 ? (
                                    <p className="text-sm text-gray-600">
                                        Este puesto no tiene evaluaciones registradas.
                                    </p>
                                ) : (
                                    <>
                                        <p className="mb-2 text-xs text-gray-600">
                                            Seleccioná una o más evaluaciones para filtrar, o dejá todas sin marcar para incluir todas.
                                        </p>
                                        <table className="w-full border-collapse text-sm">
                                            <thead className="bg-[#4472c4] text-white">
                                                <tr>
                                                    <th className="w-10 p-2 text-center">Sel.</th>
                                                    <th className="p-2 text-left">Evaluación</th>
                                                    <th className="p-2 text-center">Fecha creación</th>
                                                    <th className="p-2 text-center">Candidatos</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {evaluaciones.map((ev) => {
                                                    const marcada = evaluacionesIds.includes(
                                                        ev.idEvaluacion
                                                    );
                                                    return (
                                                        <tr
                                                            key={ev.idEvaluacion}
                                                            onClick={() =>
                                                                toggleEvaluacion(ev.idEvaluacion)
                                                            }
                                                            className={`cursor-pointer border-b border-gray-200 ${
                                                                marcada ? "bg-[#d6e4f0]" : ""
                                                            }`}
                                                        >
                                                            <td className="p-2 text-center">
                                                                <input
                                                                    type="checkbox"
                                                                    checked={marcada}
                                                                    onChange={() =>
                                                                        toggleEvaluacion(
                                                                            ev.idEvaluacion
                                                                        )
                                                                    }
                                                                    onClick={(e) =>
                                                                        e.stopPropagation()
                                                                    }
                                                                />
                                                            </td>
                                                            <td className="p-2">
                                                                {ev.nombreEvaluacion}
                                                            </td>
                                                            <td className="p-2 text-center">
                                                                {ev.fechaCreacion}
                                                            </td>
                                                            <td className="p-2 text-center">
                                                                {ev.cantidadCandidatos}
                                                            </td>
                                                        </tr>
                                                    );
                                                })}
                                            </tbody>
                                        </table>
                                        <div className="mt-4 flex justify-end">
                                            <button
                                                onClick={emitirOrdenMerito}
                                                className="rounded-lg bg-[#4472c4] px-6 py-2 text-sm font-bold text-white shadow-md"
                                            >
                                                Emitir orden de mérito
                                            </button>
                                        </div>
                                    </>
                                )}
                            </section>
                        </div>
                    )}

                    {/* Paso 3: Resultado del Orden de Mérito */}
                    {ordenMerito && (
                        <div>
                            <div className="mb-2 flex items-center justify-between">
                                <h2 className="text-sm font-bold print:text-base">
                                    Orden de Mérito — {puestoSeleccionado?.nombre}
                                </h2>
                                {ordenMerito.length > 0 && (
                                    <button
                                        onClick={() => window.print()}
                                        className="rounded border border-gray-400 bg-white px-3 py-1 text-xs font-semibold text-gray-700 print:hidden"
                                    >
                                        Imprimir
                                    </button>
                                )}
                            </div>
                            {puestoSeleccionado && (
                                <div className="mb-3 hidden text-xs text-gray-700 print:block">
                                    <p>
                                        <strong>Puesto:</strong> {puestoSeleccionado.codigo} —{" "}
                                        {puestoSeleccionado.nombre} ({puestoSeleccionado.empresa})
                                    </p>
                                    {evaluacionesIncluidas.length > 0 && (
                                        <p>
                                            <strong>Evaluaciones:</strong>{" "}
                                            {evaluacionesIncluidas
                                                .map((ev) => `${ev.nombreEvaluacion} (${ev.fechaCreacion})`)
                                                .join(", ")}
                                        </p>
                                    )}
                                </div>
                            )}
                            <section className="overflow-x-auto rounded bg-white p-4 print:overflow-visible print:p-0">
                                {ordenMerito.length === 0 ? (
                                    <p className="text-sm text-gray-600">
                                        No se encontraron candidatos para las evaluaciones seleccionadas.
                                    </p>
                                ) : (
                                    <table className="w-full border-collapse text-sm">
                                        <thead className="bg-[#4472c4] text-white">
                                            <tr>
                                                <th className="p-2 text-center">Pos.</th>
                                                <th className="p-2 text-left">Documento</th>
                                                <th className="p-2 text-left">Apellido y Nombre</th>
                                                <th className="p-2 text-center">Estado</th>
                                                <th className="p-2 text-center">Puntaje</th>
                                                <th className="p-2 text-center">Cumple Mín.</th>
                                                <th className="p-2 text-center">Fecha Inicio</th>
                                                <th className="p-2 text-center">Fecha Fin</th>
                                                <th className="p-2 text-center">Último Ingreso</th>
                                                <th className="p-2 text-center">Accesos</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {ordenMerito.map((c, index) => {
                                                const pos =
                                                    c.cumplePonderacionMinima === true
                                                        ? ++posicionActual
                                                        : "-";
                                                const esCompleto = c.estadoCuestionario === "COMPLETO";
                                                return (
                                                    <tr
                                                        key={`${c.tipoDocumento}-${c.nroDocumento}-${index}`}
                                                        className="border-b border-gray-200"
                                                    >
                                                        <td className="p-2 text-center font-bold">
                                                            {pos}
                                                        </td>
                                                        <td className="p-2">
                                                            {c.tipoDocumento} {c.nroDocumento}
                                                        </td>
                                                        <td className="p-2">
                                                            {c.apellido}, {c.nombre}
                                                        </td>
                                                        <td className="p-2 text-center">
                                                            {c.estadoCuestionario}
                                                        </td>
                                                        <td className="p-2 text-center font-semibold">
                                                            {c.puntajeObtenido !== null
                                                                ? c.puntajeObtenido.toFixed(2)
                                                                : "-"}
                                                        </td>
                                                        <td className="p-2 text-center">
                                                            {c.cumplePonderacionMinima === null
                                                                ? "-"
                                                                : c.cumplePonderacionMinima
                                                                  ? "Sí"
                                                                  : "No"}
                                                        </td>
                                                        <td className="p-2 text-center">
                                                            {formatearFechaHora(c.fechaInicio)}
                                                        </td>
                                                        <td className="p-2 text-center">
                                                            {esCompleto
                                                                ? formatearFechaHora(c.fechaFin)
                                                                : "-"}
                                                        </td>
                                                        <td className="p-2 text-center">
                                                            {esCompleto
                                                                ? "-"
                                                                : formatearFechaHora(
                                                                      c.fechaHoraUltimoIngreso
                                                                  )}
                                                        </td>
                                                        <td className="p-2 text-center">
                                                            {c.cantidadAccesos ?? 0}
                                                        </td>
                                                    </tr>
                                                );
                                            })}
                                        </tbody>
                                    </table>
                                )}
                            </section>
                        </div>
                    )}

                    {/* Salir abajo a la derecha */}
                    <div className="mt-4 flex justify-end print:hidden">
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
