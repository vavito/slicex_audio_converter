import { useRef, useState, type DragEvent } from "react";

type EstadoOciosoProps = {
  aoSelecionarArquivo: (arquivo: File) => void;
};

export function EstadoOcioso({ aoSelecionarArquivo }: EstadoOciosoProps) {
  const referenciaDoInputDeArquivo = useRef<HTMLInputElement>(null);
  const [estaArrastando, setEstaArrastando] = useState(false);

  function abrirSeletorDeArquivo() {
    referenciaDoInputDeArquivo.current?.click();
  }

  function quandoArquivoForEscolhido(
    evento: React.ChangeEvent<HTMLInputElement>,
  ) {
    const arquivoEscolhido = evento.target.files?.[0];
    if (!arquivoEscolhido) return;
    aoSelecionarArquivo(arquivoEscolhido);
  }

  function aoArrastarSobre(evento: DragEvent<HTMLDivElement>) {
    evento.preventDefault();
    setEstaArrastando(true);
  }

  function aoSairDoArrasto(evento: DragEvent<HTMLDivElement>) {
    evento.preventDefault();
    setEstaArrastando(false);
  }

  function aoSoltarArquivo(evento: DragEvent<HTMLDivElement>) {
    evento.preventDefault();
    setEstaArrastando(false);
    const arquivoSolto = evento.dataTransfer.files?.[0];
    if (!arquivoSolto) return;
    aoSelecionarArquivo(arquivoSolto);
  }

  return (
    <div className="space-y-4">
      <div
        onDragOver={aoArrastarSobre}
        onDragEnter={aoArrastarSobre}
        onDragLeave={aoSairDoArrasto}
        onDrop={aoSoltarArquivo}
        onClick={abrirSeletorDeArquivo}
        role="button"
        tabIndex={0}
        onKeyDown={(evento) => {
          if (evento.key === "Enter" || evento.key === " ") {
            evento.preventDefault();
            abrirSeletorDeArquivo();
          }
        }}
        className={`flex cursor-pointer flex-col items-center justify-center gap-2 rounded-2xl border-2 border-dashed px-6 py-10 text-center transition-all ${
          estaArrastando
            ? "border-primary bg-primary/10"
            : "border-white/15 bg-background/40 hover:border-primary/40 hover:bg-background/60"
        }`}
      >
        <span className="text-sm font-medium text-foreground">
          Arraste e solte seu arquivo aqui
        </span>
        <span className="text-[10px] uppercase tracking-[0.2em] text-muted-foreground">
          ou clique para selecionar
        </span>
      </div>

      <button
        type="button"
        onClick={abrirSeletorDeArquivo}
        className="flex w-full items-center justify-center gap-3 rounded-2xl bg-primary px-8 py-6 text-lg font-semibold text-primary-foreground transition-all hover:brightness-110 active:scale-[0.98]"
      >
        Selecionar Arquivo
      </button>

      <input
        ref={referenciaDoInputDeArquivo}
        type="file"
        accept=".mp3,.wav"
        className="hidden"
        onChange={quandoArquivoForEscolhido}
      />

      <p className="text-center text-[10px] uppercase tracking-[0.2em] text-muted-foreground">
        Tamanho máximo: 50MB por arquivo
      </p>
    </div>
  );
}
