"use client";

import { useEffect, useState } from "react";
import PopupError from "@/components/ui/PopupError";
import { LIMITES_PREGUNTA } from "@/constants/preguntas";
import { ApiError } from "@/lib/apiClient";
import { listarOpcionesRespuesta } from "@/services/catalogoService";
import { modificarPregunta, PreguntaDuplicadaError } from "@/services/preguntaService";
import { OpcionRespuestaDTO } from "@/types/opcionRespuesta";
import { PreguntaDTO } from "@/types/pregunta";

interface Props {
    pregunta: PreguntaDTO;
    onGuardada: () => void;
    onCancelar: () => void;
}

interface Aviso {
    mensaje: string;
    detalles?: string[];
}

const CLASE_CAMPO = "w-full rounded border border-gray-300 bg-white px-3 py-2 text-sm text-black";
const CLASE_ETIQUETA = "block text-xs font-semibold text-gray-600";

export default function ModalEditarPregunta({ pregunta, onGuardada, onCancelar }: Props) {
    const [opciones, setOpciones] = useState<OpcionRespuestaDTO[]>([]);
    const [nombre, setNombre] = useState(pregunta.nombre);
    const [textoPregunta, setTextoPregunta] = useState(pregunta.textoPregunta);
    const [descripcion, setDescripcion] = useState(pregunta.descripcion ?? "");
    const [idOpcion, setIdOpcion] = useState(String(pregunta.idOpcionRespuesta));
    const [ponderaciones, setPonderaciones] = useState<string[]>(() =>
        [...pregunta.opcionesPregunta]
            .sort((a, b) => a.orden - b.orden)
            .map((o) => String(o.ponderacion))
    );
    const [guardando, setGuardando] = useState(false);
    const [aviso, setAviso] = useState<Aviso | null>(null);

    useEffect(() => {
        listarOpcionesRespuesta()
            .then(setOpciones)
            .catch((e) => {
                setAviso({
                    mensaje:
                        e instanceof ApiError
                            ? e.message
                            : "No se pudieron cargar las opciones de respuesta.",
                });
            });
    }, []);

    const opcionElegida = opciones.find((o) => o.id === Number(idOpcion));
    const items = opcionElegida ? [...opcionElegida.items].sort((a, b) => a.orden - b.orden) : [];
    const valores = ponderaciones.map((p) => (p.trim() === "" ? 0 : Number(p)));
    const suma = valores.reduce((a, b) => a + b, 0);

    const elegirOpcion = (id: string) => {
        setIdOpcion(id);
        const opcion = opciones.find((o) => o.id === Number(id));
        const itemsOrdenados = opcion ? [...opcion.items].sort((a, b) => a.orden - b.orden) : [];
        if (Number(id) === pregunta.idOpcionRespuesta) {
            setPonderaciones(
                itemsOrdenados.map((item) => {
                    const existente = pregunta.opcionesPregunta.find((op) => op.orden === item.orden);
                    return existente ? String(existente.ponderacion) : "";
                })
            );
        } else {
            setPonderaciones(itemsOrdenados.map(() => ""));
        }
    };

    const cambiarPonderacion = (indice: number, valor: string) => {
        setPonderaciones(ponderaciones.map((p, i) => (i === indice ? valor : p)));
    };

    const guardar = async () => {
        const n = nombre.trim();
        const t = textoPregunta.trim();
        const d = descripcion.trim();

        const faltan: string[] = [];
        if (!n) faltan.push("Nombre de pregunta");
        if (!t) faltan.push("Pregunta");
        if (!idOpcion) faltan.push("Opciones de respuesta");
        if (faltan.length > 0) {
            setAviso({
                mensaje:
                    "NO SE INGRESARON LOS DATOS EN TODAS LAS CASILLAS OBLIGATORIAS (marcadas con *). Faltan ingresar:",
                detalles: faltan,
            });
            return;
        }

        const excedidos: string[] = [];
        if (n.length > LIMITES_PREGUNTA.nombre)
            excedidos.push(`Nombre de pregunta (máximo ${LIMITES_PREGUNTA.nombre} caracteres)`);
        if (t.length > LIMITES_PREGUNTA.textoPregunta)
            excedidos.push(`Pregunta (máximo ${LIMITES_PREGUNTA.textoPregunta} caracteres)`);
        if (d.length > LIMITES_PREGUNTA.descripcion)
            excedidos.push(`Descripción (máximo ${LIMITES_PREGUNTA.descripcion} caracteres)`);
        if (excedidos.length > 0) {
            setAviso({
                mensaje: "ALGUNOS DE LOS CAMPOS EXCEDEN SU LONGITUD MÁXIMA. Campos implicados:",
                detalles: excedidos,
            });
            return;
        }

        const invalidas = valores.some((v) => Number.isNaN(v) || v < 0);
        if (items.length === 0 || invalidas || Math.abs(suma - 10) > 0.001) {
            setAviso({
                mensaje: "LAS PONDERACIONES DEBEN SER 0 O POSITIVAS Y SUMAR 10 PUNTOS.",
                detalles: [`Suma actual: ${Number.isNaN(suma) ? "inválida" : suma}`],
            });
            return;
        }

        setGuardando(true);
        try {
            await modificarPregunta(pregunta.idPregunta, {
                nombre: n,
                textoPregunta: t,
                descripcion: d === "" ? null : d,
                idOpcionRespuesta: Number(idOpcion),
                opcionesPregunta: items.map((item, i) => ({
                    orden: item.orden,
                    ponderacion: valores[i],
                })),
            });
            onGuardada();
        } catch (e) {
            if (e instanceof PreguntaDuplicadaError) {
                setAviso({
                    mensaje: `LA PREGUNTA: "${n}" ya existe para el factor y la competencia seleccionadas.`,
                    detalles: [
                        e.campo === "nombre"
                            ? "Ya existe una pregunta con ese nombre"
                            : "Ya existe una pregunta con ese texto",
                    ],
                });
            } else {
                setAviso({
                    mensaje:
                        e instanceof ApiError
                            ? e.message
                            : "No se pudo modificar la pregunta. Intente nuevamente.",
                });
            }
        } finally {
            setGuardando(false);
        }
    };

    return (
        <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/40 p-4">
            <div className="max-h-[90vh] w-full max-w-4xl overflow-y-auto bg-[#e5e5e5] shadow-lg">
                <header className="flex items-center justify-between bg-[#0078d4] px-4 py-2 text-white">
                    <h2 className="font-bold">Modificar pregunta</h2>
                    <button onClick={onCancelar} aria-label="Cerrar" className="text-xl leading-none">
                        ✕
                    </button>
                </header>

                <div className="p-6">
                    <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                        <div className="space-y-4">
                            <label className={CLASE_ETIQUETA}>
                                Competencia
                                <input
                                    disabled
                                    value={pregunta.nombreCompetencia}
                                    className={`${CLASE_CAMPO} mt-1 bg-gray-100 text-gray-600`}
                                />
                            </label>

                            <label className={CLASE_ETIQUETA}>
                                Factor
                                <input
                                    disabled
                                    value={pregunta.nombreFactor}
                                    className={`${CLASE_CAMPO} mt-1 bg-gray-100 text-gray-600`}
                                />
                            </label>

                            <label className={CLASE_ETIQUETA}>
                                Nombre de pregunta *
                                <input
                                    value={nombre}
                                    onChange={(e) => setNombre(e.target.value.toUpperCase())}
                                    className={`${CLASE_CAMPO} mt-1`}
                                />
                            </label>

                            <label className={CLASE_ETIQUETA}>
                                Pregunta *
                                <textarea
                                    value={textoPregunta}
                                    onChange={(e) => setTextoPregunta(e.target.value.toUpperCase())}
                                    className={`${CLASE_CAMPO} mt-1 h-24 resize-none`}
                                />
                            </label>

                            <label className={CLASE_ETIQUETA}>
                                Descripción
                                <textarea
                                    value={descripcion}
                                    onChange={(e) => setDescripcion(e.target.value.toUpperCase())}
                                    className={`${CLASE_CAMPO} mt-1 h-24 resize-none`}
                                />
                            </label>
                        </div>

                        <div>
                            <label className={CLASE_ETIQUETA}>
                                Opciones de respuesta *
                                <select
                                    value={idOpcion}
                                    onChange={(e) => elegirOpcion(e.target.value)}
                                    className={`${CLASE_CAMPO} mt-1`}
                                >
                                    <option value="" className="bg-white text-black">
                                        Agregar opción de respuesta...
                                    </option>
                                    {opciones.map((o) => (
                                        <option key={o.id} value={o.id} className="bg-white text-black">
                                            {o.nombre}
                                        </option>
                                    ))}
                                </select>
                            </label>

                            {items.length > 0 && (
                                <div className="mt-3 rounded bg-white p-2">
                                    <table className="w-full text-sm">
                                        <thead className="bg-[#4472c4] text-white">
                                            <tr>
                                                <th className="p-1">Orden</th>
                                                <th className="p-1">Descripción</th>
                                                <th className="p-1">Ponderación</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {items.map((item, i) => (
                                                <tr key={item.orden} className="border-b border-gray-200 text-center">
                                                    <td className="p-1">{item.orden}</td>
                                                    <td className="p-1">{item.descripcion}</td>
                                                    <td className="p-1">
                                                        <input
                                                            type="number"
                                                            min={0}
                                                            step="any"
                                                            value={ponderaciones[i] ?? ""}
                                                            onChange={(e) => cambiarPonderacion(i, e.target.value)}
                                                            className="w-20 rounded border border-gray-300 px-2 py-1 text-black"
                                                        />
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                    <p className="mt-2 text-right text-xs text-gray-600">Suma: {suma} / 10</p>
                                </div>
                            )}
                        </div>
                    </div>

                    <div className="mt-6 flex justify-end gap-2">
                        <button
                            disabled={guardando}
                            onClick={guardar}
                            className="rounded bg-[#1565c0] px-6 py-2 text-sm font-bold text-white disabled:opacity-50"
                        >
                            {guardando ? "Guardando..." : "Aceptar"}
                        </button>
                        <button
                            disabled={guardando}
                            onClick={onCancelar}
                            className="rounded border border-gray-300 bg-white px-6 py-2 text-sm"
                        >
                            Cancelar
                        </button>
                    </div>
                </div>
            </div>

            {aviso && (
                <PopupError
                    mensaje={aviso.mensaje}
                    detalles={aviso.detalles}
                    onCerrar={() => setAviso(null)}
                />
            )}
        </div>
    );
}
