import { useRef } from "react";

type EstadoOciosoProps = {
  aoSelecionarArquivo: (arquivo: File) => void;
};

export function EstadoOcioso({ aoSelecionarArquivo }: EstadoOciosoProps) {
  const referenciaDoInputDeArquivo = useRef<HTMLInputElement>(null);

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

  return (
    <div className="space-y-4">
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
