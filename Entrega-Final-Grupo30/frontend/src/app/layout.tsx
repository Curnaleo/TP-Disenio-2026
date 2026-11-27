import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Capital Humano - Evaluación de Candidatos",
  description: "Sistema de gestión de evaluaciones de candidatos para puestos de trabajo",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="es">
      <body className="antialiased">
        {children}
      </body>
    </html>
  );
}
