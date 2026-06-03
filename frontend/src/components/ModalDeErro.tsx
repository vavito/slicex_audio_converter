// src/components/ModalDeErro.tsx
import { useEffect } from "react";

type ModalDeErroProps = {
  aberto: boolean;
  titulo: string;
  mensagem: string;
  aoFechar: () => void;
};

export function ModalDeErro({
  aberto,
  titulo,
  mensagem,
  aoFechar,
}: ModalDeErroProps) {
  useEffect(() => {
    if (!aberto) return;
    function aoPressionarTecla(evento: KeyboardEvent) {
      if (evento.key === "Escape") aoFechar();
    }
    window.addEventListener("keydown", aoPressionarTecla);
    return () => window.removeEventListener("keydown", aoPressionarTecla);
  }, [aberto, aoFechar]);

  if (!aberto) return null;

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/70 px-6 backdrop-blur-sm"
      role="dialog"
      aria-modal="true"
      aria-labelledby="modal-de-erro-titulo"
      onClick={aoFechar}
    >
      <div
        className="w-full max-w-md space-y-6 rounded-3xl border border-white/10 bg-card p-8 shadow-2xl animate-fade-up"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="space-y-3">
          <span className="block text-[10px] font-mono uppercase tracking-[0.3em] text-primary">
            Atenção
          </span>
          <h2
            id="modal-de-erro-titulo"
            className="font-serif text-3xl tracking-tight"
          >
            {titulo}
          </h2>
          <p className="text-sm leading-relaxed text-muted-foreground">
            {mensagem}
          </p>
        </div>

        <button
          type="button"
          onClick={aoFechar}
          className="w-full rounded-2xl bg-primary px-6 py-4 text-sm font-bold uppercase tracking-[0.2em] text-primary-foreground transition-all hover:brightness-110 active:scale-[0.98]"
        >
          Entendi
        </button>
      </div>
    </div>
  );
}
