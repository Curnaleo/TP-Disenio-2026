"use client";

import { useEffect, useRef } from "react";

interface PopupErrorProps {
    mensaje: string;
    detalles?: string[];
    titulo?: string;
    textoBoton?: string;
    // lateral: panel a la derecha, sin fondo oscuro y sin tapar el formulario
    lateral?: boolean;
    onCerrar: () => void;
}

async function emitirAlertaSonora() {
    if (typeof window === "undefined" || !window.AudioContext || !navigator.userActivation?.hasBeenActive) return;

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

export default function PopupError({
                                       mensaje,
                                       detalles,
                                       titulo = "ERROR",
                                       textoBoton = "Volver",
                                       lateral = false,
                                       onCerrar,
                                   }: PopupErrorProps) {
    const botonRef = useRef<HTMLButtonElement>(null);

    useEffect(() => {
        botonRef.current?.focus();
        void emitirAlertaSonora().catch(() => {});
    }, []);

    useEffect(() => {
        const alPresionar = (e: KeyboardEvent) => {
            if (e.key === "Escape") onCerrar();
        };
        window.addEventListener("keydown", alPresionar);
        return () => window.removeEventListener("keydown", alPresionar);
    }, [onCerrar]);

    const panel = (
        <div
            role="alertdialog"
            aria-modal={!lateral}
            aria-label={titulo}
            className="w-full max-w-md overflow-hidden rounded-xl border border-neutral-700 bg-[#121212] shadow-2xl"
        >
            <div className="bg-[#ffa8a8] px-5 py-3 text-sm font-bold text-black">{titulo}</div>

            <div className="flex items-center gap-4 px-6 py-6">
                <svg width="48" height="48" viewBox="0 0 48 48" aria-hidden="true" className="shrink-0">
                    <polygon points="24,4 46,42 2,42" fill="#9a5208" />
                    <rect x="22" y="16" width="4" height="13" rx="1" fill="#121212" />
                    <circle cx="24" cy="35" r="2.5" fill="#121212" />
                </svg>

                <div className="text-sm text-neutral-300">
                    <p>{mensaje}</p>
                    {detalles && detalles.length > 0 && (
                        <ul className="mt-3 list-disc pl-5 text-sm">
                            {detalles.map((d) => (
                                <li key={d}>{d}</li>
                            ))}
                        </ul>
                    )}
                </div>
            </div>

            <div className="mx-3 border-t border-neutral-700" />

            <div className="flex justify-end p-3">
                <button
                    ref={botonRef}
                    onClick={onCerrar}
                    className="rounded-lg border border-neutral-700 bg-[#1e1e1e] px-6 py-2 text-sm text-neutral-200"
                >
                    {textoBoton}
                </button>
            </div>
        </div>
    );

    if (lateral) {
        // El contenedor padre tiene que ser "relative"
        return <div className="absolute right-6 top-28 z-50 w-80">{panel}</div>;
    }

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
            {panel}
        </div>
    );
}