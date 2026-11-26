"use client";

import { useEffect, useRef } from "react";

interface PopupConfirmacionProps {
    titulo?: string;
    mensaje: string;
    textoSi?: string;
    textoNo?: string;
    onSi: () => void;
    onNo: () => void;
}

export default function PopupConfirmacion({
                                              titulo = "Confirmación",
                                              mensaje,
                                              textoSi = "Sí",
                                              textoNo = "No",
                                              onSi,
                                              onNo,
                                          }: PopupConfirmacionProps) {
    const botonSiRef = useRef<HTMLButtonElement>(null);

    useEffect(() => {
        botonSiRef.current?.focus();
    }, []);

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
            <div
                role="dialog"
                aria-modal="true"
                aria-label={titulo}
                className="w-full max-w-md overflow-hidden rounded-xl border border-neutral-700 bg-[#121212] shadow-2xl"
            >
                <div className="bg-[#0078d4] px-5 py-3 text-sm font-bold text-white">{titulo}</div>
                <p className="px-8 py-8 text-center text-base text-neutral-300">{mensaje}</p>
                <div className="flex justify-center gap-3 pb-6">
                    <button
                        ref={botonSiRef}
                        onClick={onSi}
                        className="rounded-lg border border-neutral-700 bg-[#1e1e1e] px-8 py-2 text-sm text-neutral-200"
                    >
                        {textoSi}
                    </button>
                    <button
                        onClick={onNo}
                        className="rounded-lg border border-neutral-700 bg-[#1e1e1e] px-8 py-2 text-sm text-neutral-200"
                    >
                        {textoNo}
                    </button>
                </div>
            </div>
        </div>
    );
}