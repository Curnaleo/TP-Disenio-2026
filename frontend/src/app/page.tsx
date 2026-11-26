"use client";

import { useState } from "react";
import Link from "next/link";
import PopupError from "@/components/ui/PopupError";

interface OpcionMenu {
  titulo: string;
  descripcion: string;
  href?: string; // sin href = todavía en desarrollo
}

const OPCIONES_CONSULTOR: OpcionMenu[] = [
  {
    titulo: "Preguntas",
    descripcion: "Buscar, crear, modificar y eliminar preguntas (CU020)",
    href: "/consultores/preguntas",
  },
  {
    titulo: "Evaluar candidatos",
    descripcion: "Generar evaluaciones y cuestionarios (CU025)",
  },
  {
    titulo: "Orden de mérito",
    descripcion: "Emitir el orden de mérito de un puesto (CU028)",
  },
];

const OPCIONES_CANDIDATO: OpcionMenu[] = [
  {
    titulo: "Completar cuestionario",
    descripcion: "Responder el cuestionario asignado (CU026)",
  },
];

const CLASE_TARJETA =
    "block w-full rounded-lg border border-gray-300 bg-white p-4 text-left shadow-sm hover:border-[#0078d4] hover:bg-blue-50";

export default function MenuInicioPage() {
  const [popup, setPopup] = useState<string | null>(null);

  const renderOpcion = (o: OpcionMenu) => {
    const contenido = (
        <>
          <p className="font-bold text-[#1565c0]">{o.titulo}</p>
          <p className="mt-1 text-sm text-gray-600">{o.descripcion}</p>
        </>
    );

    if (o.href) {
      return (
          <Link key={o.titulo} href={o.href} className={CLASE_TARJETA}>
            {contenido}
          </Link>
      );
    }

    return (
        <button
            key={o.titulo}
            onClick={() => setPopup("Aún en desarrollo")}
            className={CLASE_TARJETA}
        >
          {contenido}
        </button>
    );
  };

  return (
      <main className="min-h-screen bg-white p-6 text-black">
        <div className="mx-auto max-w-3xl bg-[#e5e5e5] shadow">
          <header className="bg-[#0078d4] px-4 py-2 font-bold text-white">
            Capital Humano - Evaluación de candidatos
          </header>

          <div className="space-y-6 p-6">
            <section>
              <h2 className="mb-3 text-sm font-bold text-gray-700">Acceso consultor</h2>
              <div className="grid gap-3 md:grid-cols-2">
                {OPCIONES_CONSULTOR.map(renderOpcion)}
              </div>
            </section>

            <section>
              <h2 className="mb-3 text-sm font-bold text-gray-700">Acceso candidato</h2>
              <div className="grid gap-3 md:grid-cols-2">
                {OPCIONES_CANDIDATO.map(renderOpcion)}
              </div>
            </section>
          </div>
        </div>

        {popup && <PopupError mensaje={popup} onCerrar={() => setPopup(null)} />}
      </main>
  );
}