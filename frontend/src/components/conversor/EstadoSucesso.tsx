import type { FormatoDeAudio } from "@/types/conversao";

type EstadoSucessoProps = {
  nomeDoArquivoOriginal: string;
  formatoDeDestino: FormatoDeAudio;
  aoBaixarArquivo: () => void;
  aoConverterNovamente: () => void;
};

export function EstadoSucesso({
  nomeDoArquivoOriginal,
  formatoDeDestino,
  aoBaixarArquivo,
  aoConverterNovamente,
}: EstadoSucessoProps) {
  const nomeSemExtensao = nomeDoArquivoOriginal.replace(/\.[^/.]+$/, "");
  const nomeDoArquivoConvertido = `${nomeSemExtensao}-converted.${formatoDeDestino.toLowerCase()}`;

  return (
    <div className="space-y-6 rounded-2xl border border-primary/20 bg-primary/5 p-6 text-center">
      <div className="space-y-2">
        <div className="mx-auto mb-4 flex size-12 items-center justify-center rounded-full bg-primary/10">
          <span className="text-xl text-primary" aria-hidden="true">
            ✓
          </span>
        </div>
        <h3 className="font-serif text-2xl">Conversão concluída</h3>
        <p className="font-mono text-sm tracking-tight text-muted-foreground">
          {nomeDoArquivoConvertido}
        </p>
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <button
          type="button"
          onClick={aoBaixarArquivo}
          className="rounded-2xl bg-foreground px-8 py-5 font-bold text-background transition-colors hover:bg-primary hover:text-primary-foreground"
        >
          Baixar Arquivo
        </button>
        <button
          type="button"
          onClick={aoConverterNovamente}
          className="rounded-2xl border border-white/10 px-8 py-5 font-medium text-foreground transition-colors hover:bg-white/5"
        >
          Converter novamente
        </button>
      </div>
    </div>
  );
}
