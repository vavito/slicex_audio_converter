import { IconeTrocarFormato } from "./IconeTrocarFormato";
import type { FormatoDeAudio } from "@/types/conversao";

type SeletorDeFormatoProps = {
  formatoDeOrigem: FormatoDeAudio;
  formatoDeDestino: FormatoDeAudio;
  aoTrocarFormatos: () => void;
};

export function SeletorDeFormato({
  formatoDeOrigem,
  formatoDeDestino,
  aoTrocarFormatos,
}: SeletorDeFormatoProps) {
  return (
    <div className="flex items-center justify-between rounded-2xl border border-white/10 bg-background/50 px-6 py-8">
      <div className="flex-1 text-center">
        <span className="mb-2 block text-[10px] font-mono uppercase tracking-widest text-muted-foreground">
          De
        </span>
        <span className="font-mono text-2xl text-foreground">
          {formatoDeOrigem}
        </span>
      </div>

      <button
        type="button"
        onClick={aoTrocarFormatos}
        className="group flex size-14 items-center justify-center rounded-full border border-primary/20 bg-primary/5 text-primary shadow-lg transition-all hover:bg-primary hover:text-primary-foreground active:scale-90"
        aria-label="Trocar formato de origem e destino"
      >
        <IconeTrocarFormato className="text-2xl transition-transform duration-500 group-hover:rotate-180" />
      </button>

      <div className="flex-1 text-center">
        <span className="mb-2 block text-[10px] font-mono uppercase tracking-widest text-muted-foreground">
          Para
        </span>
        <span className="font-mono text-2xl text-foreground">
          {formatoDeDestino}
        </span>
      </div>
    </div>
  );
}
