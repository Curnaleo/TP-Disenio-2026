"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import PopupError from "@/components/ui/PopupError";
import PopupConfirmacion from "@/components/ui/PopupConfirmacion";
import { LIMITES_PREGUNTA } from "@/constants/preguntas";
import {
    listarCompetenciasCatalogo,
    listarFactoresDeCompetencia,
    listarOpcionesRespuesta,
} from "@/services/catalogoService";
import { CompetenciaDTO } from "@/types/competencia";
import { FactorDTO } from "@/types/factor";
import { OpcionRespuestaDTO } from "@/types/opcionRespuesta";
import { crearPregunta, PreguntaDuplicadaError } from "@/services/preguntaService";

interface Aviso {
    mensaje: string;
    detalles?: string[];
}

const CLASE_CAMPO = "w-full rounded border border-gray-300 bg-white px-3 py-2 text-sm text-black";
const CLASE_ETIQUETA = "block text-xs font-semibold text-gray-600";

export default function AltaPreguntaPage() {
    const router = useRouter();

    const [competencias, setCompetencias] = useState<CompetenciaDTO[]>([]);
    const [factores, setFactores] = useState<FactorDTO[]>([]);
    const [opciones, setOpciones] = useState<OpcionRespuestaDTO[]>([]);

    const [idCompetencia, setIdCompetencia] = useState("");
    const [idFactor, setIdFactor] = useState("");
    const [nombre, setNombre] = useState("");
    const [textoPregunta, setTextoPregunta] = useState("");
    const [descripcion, setDescripcion] = useState("");
    const [idOpcion, setIdOpcion] = useState("");
    const [ponderaciones, setPonderaciones] = useState<string[]>([]);

    const [aviso, setAviso] = useState<Aviso | null>(null);
    const [nombreCreada, setNombreCreada] = useState<string | null>(null);

    useEffect(() => {
        listarCompetenciasCatalogo().then(setCompetencias);
        listarOpcionesRespuesta().then(setOpciones);
    }, []);

    // Grilla de opciones ordenada por "orden" (CU021)
    const opcionElegida = opciones.find((o) => o.id === Number(idOpcion));
    const items = opcionElegida ? [...opcionElegida.items].sort((a, b) => a.orden - b.orden) : [];
    const valores = ponderaciones.map((p) => (p.trim() === "" ? 0 : Number(p)));
    const suma = valores.reduce((a, b) => a + b, 0);

    const elegirCompetencia = async (id: string) => {
        setIdCompetencia(id);
        setIdFactor("");
        setFactores(id ? await listarFactoresDeCompetencia(Number(id)) : []);
    };

    const elegirOpcion = (id: string) => {
        setIdOpcion(id);
        const opcion = opciones.find((o) => o.id === Number(id));
        setPonderaciones(opcion ? opcion.items.map(() => "") : []);
    };

    const cambiarPonderacion = (indice: number, valor: string) => {
        setPonderaciones(ponderaciones.map((p, i) => (i === indice ? valor : p)));
    };

    const limpiar = () => {
        setIdCompetencia("");
        setIdFactor("");
        setFactores([]);
        setNombre("");
        setTextoPregunta("");
        setDescripcion("");
        setIdOpcion("");
        setPonderaciones([]);
    };

    const cancelar = () => router.push("/consultores/preguntas");

    const aceptar = async () => {
        const n = nombre.trim();
        const t = textoPregunta.trim();
        const d = descripcion.trim();

        // 2.A: faltan datos obligatorios (se listan TODOS)
        const faltan: string[] = [];
        if (!idCompetencia) faltan.push("Competencia");
        if (!idFactor) faltan.push("Factor");
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

        // 2.B: campos que exceden su longitud máxima (se listan TODOS)
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

        // Ponderaciones: 0 o positivas y suman 10
        const invalidas = valores.some((v) => Number.isNaN(v) || v < 0);
        if (invalidas || Math.abs(suma - 10) > 0.001) {
            setAviso({
                mensaje: "LAS PONDERACIONES DEBEN SER 0 O POSITIVAS Y SUMAR 10 PUNTOS.",
                detalles: [`Suma actual: ${Number.isNaN(suma) ? "inválida" : suma}`],
            });
            return;
        }

        try {
            const creada = await crearPregunta({
                idCompetencia: Number(idCompetencia),
                idFactor: Number(idFactor),
                nombre: n,
                textoPregunta: t,
                descripcion: d === "" ? null : d,
                idOpcionRespuesta: Number(idOpcion),
                ponderaciones: valores,
            });
            setNombreCreada(creada.nombre);
        } catch (e) {
            // 2.C: la pregunta ya existe (aclara cuál validación no se superó)
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
                setAviso({ mensaje: "No se pudo registrar la pregunta. Intente nuevamente." });
            }
        }
    };

    return (
        <main className="min-h-screen bg-white p-6 text-black">
            <div className="relative mx-auto max-w-6xl bg-[#e5e5e5] shadow">
                <header className="flex items-center justify-between bg-[#0078d4] px-4 py-2 text-white">
                    <h1 className="font-bold">Crear pregunta</h1>
                    <button onClick={cancelar} aria-label="Cerrar" className="text-xl leading-none">
                        ✕
                    </button>
                </header>

                <div className="p-6">
                    <div className="flex justify-end">
                        <button
                            onClick={() => setAviso({ mensaje: "Aún en desarrollo" })}
                            className="rounded bg-[#0078d4] px-4 py-2 text-sm font-bold text-white"
                        >
                            ✨ Generar con IA
                        </button>
                    </div>

                    <div className="mt-2 grid grid-cols-1 gap-x-8 gap-y-4 md:grid-cols-3">
                        {/* Columna 1 */}
                        <div className="space-y-4">
                            <label className={CLASE_ETIQUETA}>
                                Competencia *
                                <select
                                    value={idCompetencia}
                                    onChange={(e) => elegirCompetencia(e.target.value)}
                                    className={`${CLASE_CAMPO} mt-1`}
                                >
                                    <option value="" className="bg-white text-black">Seleccionar competencia...</option>
                                    {competencias.map((c) => (
                                        <option key={c.id} value={c.id} className="bg-white text-black">
                                            {c.nombre}
                                        </option>
                                    ))}
                                </select>
                            </label>

                            <label className={CLASE_ETIQUETA}>
                                Factor *
                                <select
                                    value={idFactor}
                                    onChange={(e) => setIdFactor(e.target.value)}
                                    className={`${CLASE_CAMPO} mt-1`}
                                >
                                    <option value="" className="bg-white text-black">Seleccionar factor...</option>
                                    {factores.map((f) => (
                                        <option key={f.id} value={f.id} className="bg-white text-black">
                                            {f.nombre}
                                        </option>
                                    ))}
                                </select>
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
                                    className={`${CLASE_CAMPO} mt-1 h-28 resize-none`}
                                />
                            </label>

                            <label className={CLASE_ETIQUETA}>
                                Descripción
                                <textarea
                                    value={descripcion}
                                    onChange={(e) => setDescripcion(e.target.value.toUpperCase())}
                                    className={`${CLASE_CAMPO} mt-1 h-32 resize-none`}
                                />
                            </label>
                        </div>

                        {/* Columna 2: opciones de respuesta */}
                        <div className="md:pt-[7.5rem]">
                            <label className={CLASE_ETIQUETA}>
                                Opciones de respuesta *
                                <select
                                    value={idOpcion}
                                    onChange={(e) => elegirOpcion(e.target.value)}
                                    className={`${CLASE_CAMPO} mt-1`}
                                >
                                    <option value="" className="bg-white text-black">Agregar opción de respuesta...</option>
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

                        {/* Columna 3: reservada para el aviso lateral */}
                        <div />
                    </div>

                    <div className="mt-6 flex justify-end gap-2">
                        <button
                            onClick={aceptar}
                            className="rounded border border-gray-300 bg-white px-6 py-2 text-sm"
                        >
                            Aceptar
                        </button>
                        <button
                            onClick={cancelar}
                            className="rounded border border-gray-300 bg-white px-6 py-2 text-sm"
                        >
                            Cancelar
                        </button>
                    </div>
                </div>

                {aviso && (
                    <PopupError
                        lateral
                        mensaje={aviso.mensaje}
                        detalles={aviso.detalles}
                        onCerrar={() => setAviso(null)}
                    />
                )}
            </div>

            {nombreCreada && (
                <PopupConfirmacion
                    titulo="Confirmación de carga"
                    mensaje={`La Pregunta ${nombreCreada} se ha creado correctamente ¿Desea cargar otra?`}
                    textoSi="Sí"
                    textoNo="No"
                    onSi={() => {
                        setNombreCreada(null);
                        limpiar();
                    }}
                    onNo={cancelar}
                />
            )}
        </main>
    );
}