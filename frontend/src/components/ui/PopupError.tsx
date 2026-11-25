"use client";

import { useEffect, useRef } from "react";

interface PopupErrorProps {
    mensaje: string;
    detalles?: string[];
    titulo?: string;
    textoBoton?: string;
    onCerrar: () => void;
}

export default function PopupError({
                                       mensaje,
                                       detalles,
                                       titulo = "ERROR",
                                       textoBoton = "Volver",
                                       onCerrar,
                                   }: PopupErrorProps) {
    const botonRef = useRef<HTMLButtonElement>(null);

    // Al abrirse, el foco queda en el botón: Enter lo activa igual que el clic
    useEffect(() => {
        botonRef.current?.focus();
    }, []);

    // Escape también cierra el popup
    useEffect(() => {
        const alPresionar = (e: KeyboardEvent) => {
            if (e.key === "Escape") onCerrar();
        };
        window.addEventListener("keydown", alPresionar);
        return () => window.removeEventListener("keydown", alPresionar);
    }, [onCerrar]);

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
            <div
                role="alertdialog"
                aria-modal="true"
                aria-label={titulo}
                className="w-full max-w-md overflow-hidden rounded-xl border border-neutral-700 bg-[#121212] shadow-2xl"
            >
                <div className="bg-[#ffa8a8] px-5 py-3 text-sm font-bold text-black">{titulo}</div>

                <div className="flex items-center gap-6 px-8 py-8">
                    <svg width="48" height="48" viewBox="0 0 48 48" aria-hidden="true" className="shrink-0">
                        <polygon points="24,4 46,42 2,42" fill="#9a5208" />
                        <rect x="22" y="16" width="4" height="13" rx="1" fill="#121212" />
                        <circle cx="24" cy="35" r="2.5" fill="#121212" />
                    </svg>

                    <div className="text-base text-neutral-300">
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
        </div>
    );
}