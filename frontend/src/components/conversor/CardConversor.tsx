import { useState } from "react";
import { SeletorDeFormato } from "./SeletorDeFormato";
import { EstadoOcioso } from "./EstadoOcioso";
import { EstadoConvertendo } from "./EstadoConvertendo";
import { EstadoSucesso } from "./EstadoSucesso";
import { converterAudio } from "@/services/audioConversionService";
import type { FormatoDeAudio, StatusDaConversao } from "@/types/conversao";

export function CardConversor() {
  const [formatoDeOrigem, setFormatoDeOrigem] = useState<FormatoDeAudio>("MP3");
  const [formatoDeDestino, setFormatoDeDestino] =
    useState<FormatoDeAudio>("WAV");
  const [statusDaConversao, setStatusDaConversao] =
    useState<StatusDaConversao>("ocioso");
  const [nomeDoArquivoSelecionado, setNomeDoArquivoSelecionado] =
    useState<string>("");
  const [arquivoConvertido, setArquivoConvertido] = useState<Blob | null>(null);

  function trocarFormatos() {
    setFormatoDeOrigem(formatoDeDestino);
    setFormatoDeDestino(formatoDeOrigem);
  }

  async function iniciarConversao(arquivoSelecionado: File) {
    try {
      setNomeDoArquivoSelecionado(arquivoSelecionado.name);
      setStatusDaConversao("convertendo");
      setArquivoConvertido(null);

      const blob = await converterAudio(
        arquivoSelecionado,
        formatoDeOrigem,
        formatoDeDestino,
      );

      setArquivoConvertido(blob);
      setStatusDaConversao("sucesso");
    } catch (error) {
      console.error(error);

      const mensagem =
        error instanceof Error
          ? error.message
          : "Erro inesperado ao converter áudio.";

      alert(mensagem);
      setStatusDaConversao("ocioso");
    }
  }

  function reiniciarFluxoDeConversao() {
    setStatusDaConversao("ocioso");
    setNomeDoArquivoSelecionado("");
    setArquivoConvertido(null);
  }

  function baixarArquivoConvertido() {
    if (!arquivoConvertido) return;

    const nomeSemExtensao = nomeDoArquivoSelecionado.replace(/\.[^/.]+$/, "");
    const nomeDoArquivoConvertido = `${nomeSemExtensao}-converted.${formatoDeDestino.toLowerCase()}`;

    const url = URL.createObjectURL(arquivoConvertido);
    const link = document.createElement("a");

    link.href = url;
    link.download = nomeDoArquivoConvertido;

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    URL.revokeObjectURL(url);
  }

  return (
    <div className="relative lg:mt-12">
      <div className="absolute -inset-16 rounded-full bg-primary/5 blur-[120px]" />

      <div className="relative space-y-10 rounded-3xl border border-white/5 bg-card p-8 shadow-2xl md:p-12">
        <SeletorDeFormato
          formatoDeOrigem={formatoDeOrigem}
          formatoDeDestino={formatoDeDestino}
          aoTrocarFormatos={trocarFormatos}
        />

        {statusDaConversao === "ocioso" && (
          <EstadoOcioso aoSelecionarArquivo={iniciarConversao} />
        )}

        {statusDaConversao === "convertendo" && (
          <EstadoConvertendo nomeDoArquivo={nomeDoArquivoSelecionado} />
        )}

        {statusDaConversao === "sucesso" && (
          <EstadoSucesso
            nomeDoArquivoOriginal={nomeDoArquivoSelecionado}
            formatoDeDestino={formatoDeDestino}
            aoBaixarArquivo={baixarArquivoConvertido}
            aoConverterNovamente={reiniciarFluxoDeConversao}
          />
        )}
      </div>
    </div>
  );
}
