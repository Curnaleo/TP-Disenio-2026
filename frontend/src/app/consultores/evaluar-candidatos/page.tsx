"use client";

import { useState, type FormEvent } from "react";
import { useRouter } from "next/navigation";
import PopupError from "@/components/ui/PopupError";
import { ApiError } from "@/lib/apiClient";
import {
    buscarCandidatos,
    confirmarEvaluacion,
    exportarClavesExcel,
    prepararEvaluacion,
    validarCandidatos,
    validarPuesto,
} from "@/services/evaluacionService";
import type { CandidatoGestionDTO, VistaPrevia } from "@/services/evaluacionService";
import { buscarPuestos } from "@/services/reporteService";
import type { PuestoDTO } from "@/types/puesto";

const campo = "rounded border border-gray-300 bg-white px-3 py-2 text-sm";
const boton = "rounded border border-gray-400 bg-white px-4 py-2 text-sm hover:bg-gray-100 disabled:opacity-50";
const principal = "rounded bg-[#0078d4] px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700 disabled:opacity-50";
const titulos = ["Seleccionar candidatos", "Elegir puesto", "Revisar claves"];

interface GrillaProps {
    candidatos: CandidatoGestionDTO[];
    marcados?: number[];
    alMarcar?: (nro: number) => void;
    claves?: Map<number, string>;
    mensajeVacio?: string;
}

function GrillaCandidatos({ candidatos, marcados, alMarcar, claves, mensajeVacio }: GrillaProps) {
    if (candidatos.length === 0) return <p className="py-3 text-sm text-gray-600">{mensajeVacio}</p>;

    return (
        <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
                <thead className="bg-gray-100">
                    <tr>
                        {marcados && <th scope="col" className="p-2">Seleccionar</th>}
                        <th scope="col" className="p-2">N.º candidato</th>
                        <th scope="col" className="p-2">Apellido</th>
                        <th scope="col" className="p-2">Nombre</th>
                        <th scope="col" className="p-2">Documento</th>
                        {claves && <th scope="col" className="p-2">Clave de ingreso</th>}
                    </tr>
                </thead>
                <tbody>
                    {candidatos.map((candidato) => (
                        <tr key={candidato.nroCandidato} className="border-b border-gray-200">
                            {marcados && (
                                <td className="p-2">
                                    <input
                                        type="checkbox"
                                        aria-label={`Seleccionar a ${candidato.apellido}, ${candidato.nombre}`}
                                        checked={marcados.includes(candidato.nroCandidato)}
                                        onChange={() => alMarcar?.(candidato.nroCandidato)}
                                    />
                                </td>
                            )}
                            <td className="p-2">{candidato.nroCandidato}</td>
                            <td className="p-2">{candidato.apellido}</td>
                            <td className="p-2">{candidato.nombre}</td>
                            <td className="p-2">{candidato.tipoDocumento} {candidato.nroDocumento}</td>
                            {claves && <td className="p-2 font-mono">{claves.get(candidato.nroCandidato)}</td>}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

function alternar(marcados: number[], nro: number) {
    return marcados.includes(nro) ? marcados.filter((id) => id !== nro) : [...marcados, nro];
}

export default function EvaluarCandidatosPage() {
    const router = useRouter();
    const [paso, setPaso] = useState(0);
    const [resultados, setResultados] = useState<CandidatoGestionDTO[] | null>(null);
    const [seleccionados, setSeleccionados] = useState<CandidatoGestionDTO[]>([]);
    const [marcadosBusqueda, setMarcadosBusqueda] = useState<number[]>([]);
    const [marcadosSeleccion, setMarcadosSeleccion] = useState<number[]>([]);
    const [puestos, setPuestos] = useState<PuestoDTO[] | null>(null);
    const [puestoId, setPuestoId] = useState<number | null>(null);
    const [vistaPrevia, setVistaPrevia] = useState<VistaPrevia | null>(null);
    const [ocupado, setOcupado] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [puestoInvalido, setPuestoInvalido] = useState<string | null>(null);
    const [idEvaluacion, setIdEvaluacion] = useState<number | null>(null);
    const [archivoExcel, setArchivoExcel] = useState<Awaited<ReturnType<typeof exportarClavesExcel>> | null>(null);
    const [mensajeExportacion, setMensajeExportacion] = useState<string | null>(null);

    const finalizado = idEvaluacion !== null;
    const puesto = puestos?.find((item) => item.puestoId === puestoId);
    const disponibles = resultados?.filter((candidato) =>
        !seleccionados.some((item) => item.nroCandidato === candidato.nroCandidato),
    ) ?? [];

    async function ejecutar(accion: () => Promise<void>) {
        setOcupado(true);
        setError(null);
        try {
            await accion();
        } catch (causa) {
            setError(causa instanceof Error ? causa.message : "No se pudo completar la operación.");
        } finally {
            setOcupado(false);
        }
    }

    function buscar(evento: FormEvent<HTMLFormElement>) {
        evento.preventDefault();
        const datos = new FormData(evento.currentTarget);
        const filtros = new URLSearchParams();
        for (const [nombre, valor] of datos) {
            const limpio = String(valor).trim();
            if (limpio) filtros.set(nombre, limpio);
        }
        void ejecutar(async () => {
            setResultados(await buscarCandidatos(filtros));
            setMarcadosBusqueda([]);
        });
    }

    function agregar() {
        if (marcadosBusqueda.length === 0) {
            setError("Seleccioná al menos un candidato para agregar.");
            return;
        }
        setSeleccionados([...seleccionados, ...disponibles.filter((item) => marcadosBusqueda.includes(item.nroCandidato))]);
        setMarcadosBusqueda([]);
    }

    function quitar() {
        setSeleccionados(seleccionados.filter((item) => !marcadosSeleccion.includes(item.nroCandidato)));
        setMarcadosSeleccion([]);
    }

    function avanzarAPuesto() {
        if (seleccionados.length === 0) {
            setError("Agregá al menos un candidato a evaluar.");
            return;
        }
        void ejecutar(async () => {
            const bloqueados = await validarCandidatos(seleccionados);
            if (bloqueados.length > 0) {
                const nombres = bloqueados.map((item) => `${item.apellido}, ${item.nombre}`).join("; ");
                setError(`Estos candidatos ya tienen un cuestionario activo o en proceso: ${nombres}`);
                return;
            }
            setPaso(1);
        });
    }

    function buscarPuestosParaEvaluar(evento: FormEvent<HTMLFormElement>) {
        evento.preventDefault();
        const datos = new FormData(evento.currentTarget);
        const nombre = String(datos.get("nombrePuesto") ?? "").trim();
        const empresa = String(datos.get("empresa") ?? "").trim();
        if (!nombre && !empresa) {
            setError("Ingresá el nombre del puesto o de la empresa para buscar.");
            return;
        }
        void ejecutar(async () => {
            setPuestos(await buscarPuestos({ codigo: "", nombre, empresa }));
            setPuestoId(null);
        });
    }

    function elegirPuesto(valor: string) {
        setPuestoId(valor ? Number(valor) : null);
        if (!valor) return;

        void ejecutar(async () => {
            try {
                await validarPuesto(Number(valor));
            } catch (causa) {
                if (causa instanceof ApiError && causa.status === 400) {
                    setPuestoInvalido(causa.message);
                    return;
                }
                setPuestoId(null);
                throw causa;
            }
        });
    }

    function revisarClaves() {
        if (!puesto) {
            setError("Seleccioná un puesto para continuar.");
            return;
        }
        void ejecutar(async () => {
            setVistaPrevia(await prepararEvaluacion(puesto.puestoId, seleccionados));
            setPaso(2);
        });
    }

    function finalizar() {
        if (!puesto || !vistaPrevia) return;
        void ejecutar(async () => {
            try {
                const confirmacion = await confirmarEvaluacion(puesto.puestoId, vistaPrevia.id);
                setIdEvaluacion(confirmacion.idEvaluacion);
            } catch (causa) {
                setVistaPrevia(null);
                setPaso(1);
                throw causa;
            }
        });
    }

    function exportarExcel() {
        if (idEvaluacion === null) return;
        const navegador = window as Window & {
            showSaveFilePicker?: (opciones: { suggestedName: string }) => Promise<FileSystemFileHandle>;
        };
        setMensajeExportacion(null);

        void ejecutar(async () => {
            if (navegador.showSaveFilePicker && archivoExcel) {
                let destino: FileSystemFileHandle;
                try {
                    destino = await navegador.showSaveFilePicker({ suggestedName: archivoExcel.nombre });
                } catch (causa) {
                    if (causa instanceof DOMException && causa.name === "AbortError") {
                        setArchivoExcel(null);
                        return;
                    }
                    throw causa;
                }
                const salida = await destino.createWritable();
                await salida.write(archivoExcel.archivo);
                await salida.close();
                setArchivoExcel(null);
                setMensajeExportacion("Archivo Excel guardado.");
                return;
            }

            const excel = await exportarClavesExcel(idEvaluacion);
            if (navegador.showSaveFilePicker) {
                setArchivoExcel(excel);
                return;
            }

            const url = URL.createObjectURL(excel.archivo);
            const enlace = document.createElement("a");
            enlace.href = url;
            enlace.download = excel.nombre;
            enlace.click();
            window.setTimeout(() => URL.revokeObjectURL(url), 1000);
            setMensajeExportacion("Descarga de Excel iniciada.");
        });
    }

    const salir = () => router.push("/");

    return (
        <main className="min-h-screen bg-white p-4 text-black sm:p-6">
            <div className="mx-auto max-w-5xl border border-gray-300 bg-[#e5e5e5] shadow">
                <header className="bg-[#0078d4] px-4 py-2 text-white">
                    <h1 className="font-bold">Evaluar candidatos</h1>
                </header>
                <fieldset disabled={ocupado} aria-busy={ocupado} className="space-y-4 p-4">
                    {!finalizado && (
                        <ol className="flex flex-wrap gap-x-6 gap-y-2 text-sm text-gray-600" aria-label="Pasos de la evaluación">
                            {titulos.map((titulo, indice) => (
                                <li
                                    key={titulo}
                                    aria-current={paso === indice ? "step" : undefined}
                                    className={paso === indice ? "font-semibold text-[#1565c0]" : undefined}
                                >
                                    {indice + 1}. {titulo}
                                </li>
                            ))}
                        </ol>
                    )}

                    {paso === 0 && (
                        <>
                            <section className="space-y-3 rounded border border-gray-300 bg-white p-4">
                                <h2 className="font-semibold">Buscar candidatos</h2>
                                <form onSubmit={buscar} className="flex flex-wrap items-end gap-3">
                                    <label className="grid gap-1 text-sm">
                                        Apellido
                                        <input name="apellido" className={campo} />
                                    </label>
                                    <label className="grid gap-1 text-sm">
                                        Nombre
                                        <input name="nombre" className={campo} />
                                    </label>
                                    <label className="grid gap-1 text-sm">
                                        N.º de candidato
                                        <input name="nroCandidato" type="number" min="1" max="2147483647" className={campo} />
                                    </label>
                                    <button type="submit" className={principal}>Buscar</button>
                                </form>
                                {resultados && (
                                    <>
                                        <GrillaCandidatos
                                            candidatos={disponibles}
                                            marcados={marcadosBusqueda}
                                            alMarcar={(id) => setMarcadosBusqueda(alternar(marcadosBusqueda, id))}
                                            mensajeVacio="No hay candidatos disponibles en los resultados."
                                        />
                                        <button type="button" onClick={agregar} className={boton}>Agregar</button>
                                    </>
                                )}
                            </section>

                            <section className="space-y-3 rounded border border-gray-300 bg-white p-4">
                                <h2 className="font-semibold">Candidatos a evaluar</h2>
                                <GrillaCandidatos
                                    candidatos={seleccionados}
                                    marcados={marcadosSeleccion}
                                    alMarcar={(id) => setMarcadosSeleccion(alternar(marcadosSeleccion, id))}
                                    mensajeVacio="Todavía no agregaste candidatos."
                                />
                                <button type="button" onClick={quitar} disabled={!marcadosSeleccion.length} className={boton}>
                                    Quitar
                                </button>
                            </section>
                        </>
                    )}

                    {paso === 1 && (
                        <section className="space-y-4 rounded border border-gray-300 bg-white p-4">
                            <form onSubmit={buscarPuestosParaEvaluar} className="flex flex-wrap items-end gap-3">
                                <label className="grid gap-1 text-sm">
                                    Nombre del puesto
                                    <input name="nombrePuesto" className={campo} />
                                </label>
                                <label className="grid gap-1 text-sm">
                                    Empresa
                                    <input name="empresa" className={campo} />
                                </label>
                                <button type="submit" className={principal}>Buscar puestos</button>
                            </form>
                            {puestos?.length ? (
                                <label className="grid max-w-md gap-1 text-sm">
                                    Puesto y empresa
                                    <select className={campo} value={puestoId ?? ""} onChange={(evento) => elegirPuesto(evento.target.value)}>
                                        <option value="">Seleccioná un puesto</option>
                                        {puestos.map((item) => (
                                            <option key={item.puestoId} value={item.puestoId}>{item.nombre} · {item.empresa}</option>
                                        ))}
                                    </select>
                                </label>
                            ) : puestos && <p className="text-sm">No se encontraron puestos.</p>}
                            {puesto && (
                                <table className="w-full text-left text-sm">
                                    <thead className="bg-gray-100">
                                        <tr>
                                            <th scope="col" className="p-2">Competencia</th>
                                            <th scope="col" className="p-2">Ponderación requerida</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {puesto.caracteristicas.map((item) => (
                                            <tr key={item.codigoCompetencia} className="border-b border-gray-200">
                                                <td className="p-2">{item.nombreCompetencia}</td>
                                                <td className="p-2">{item.ponderacionMinima}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            )}
                        </section>
                    )}

                    {paso === 2 && vistaPrevia && (
                        <section className="space-y-3 rounded border border-gray-300 bg-white p-4">
                            <h2 className="font-semibold">
                                {finalizado ? "Evaluación generada" : `Candidatos para ${puesto?.nombre}`}
                            </h2>
                            <GrillaCandidatos
                                candidatos={vistaPrevia.candidatos.map((item) => item.candidato)}
                                claves={new Map(vistaPrevia.candidatos.map((item) => [item.candidato.nroCandidato, item.clave]))}
                            />
                        </section>
                    )}

                    {finalizado && (
                        <p role="status" className="text-sm text-gray-600">
                            {mensajeExportacion ?? (archivoExcel
                                ? "Elegí dónde guardar el archivo Excel."
                                : "¿Querés exportar la lista de candidatos y sus claves a Excel?")}
                        </p>
                    )}
                    {ocupado && <p role="status" className="text-sm text-gray-600">Procesando…</p>}
                    <div className="flex flex-wrap justify-end gap-2">
                        <button type="button" onClick={salir} className={boton}>{finalizado ? "Volver al inicio" : "Cancelar"}</button>
                        {!finalizado && paso > 0 && (
                            <button type="button" className={boton} onClick={() => {
                                setVistaPrevia(null);
                                setPaso(paso - 1);
                            }}>Anterior</button>
                        )}
                        {paso === 0 && <button type="button" onClick={avanzarAPuesto} className={principal}>Siguiente</button>}
                        {paso === 1 && <button type="button" onClick={revisarClaves} className={principal}>Siguiente</button>}
                        {paso === 2 && !finalizado && (
                            <button type="button" onClick={finalizar} className={principal}>Finalizar</button>
                        )}
                        {finalizado && (
                            <button type="button" onClick={exportarExcel} className={principal}>
                                {archivoExcel ? "Guardar como" : "Exportar Excel"}
                            </button>
                        )}
                    </div>
                </fieldset>
            </div>
            {error && <PopupError mensaje={error} onCerrar={() => setError(null)} />}
            {puestoInvalido && <PopupError mensaje={puestoInvalido} textoBoton="Aceptar" onCerrar={salir} />}
        </main>
    );
}
