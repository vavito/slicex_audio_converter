import { createFileRoute } from "@tanstack/react-router";
import { CabecalhoHero } from "@/components/conversor/CabecalhoHero";
import { CardConversor } from "@/components/conversor/CardConversor";

export const Route = createFileRoute("/")({
  head: () => ({
    meta: [
      { title: "Slicex Audio Converter — MP3 e WAV" },
      {
        name: "description",
        content:
          "Converta áudio entre MP3 e WAV com alta fidelidade. Rápido, seguro e confiável.",
      },
      { property: "og:title", content: "Slicex Audio Converter — MP3 e WAV" },
      {
        property: "og:description",
        content: "Converta áudio entre MP3 e WAV com alta fidelidade.",
      },
    ],
  }),
  component: PaginaInicial,
});

function PaginaInicial() {
  return (
    <main className="mx-auto max-w-6xl px-6 py-12 md:py-24">
      <section className="animate-fade-up grid items-start gap-16 lg:grid-cols-2">
        <CabecalhoHero />
        <CardConversor />
      </section>
    </main>
  );
}
