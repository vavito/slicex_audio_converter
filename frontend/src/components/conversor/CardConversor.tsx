import { useState } from "react";
import { SeletorDeFormato } from "./SeletorDeFormato";
import { EstadoOcioso } from "./EstadoOcioso";
import { EstadoConvertendo } from "./EstadoConvertendo";
import { EstadoSucesso } from "./EstadoSucesso";
import type { FormatoDeAudio, StatusDaConversao } from "@/types/conversao";

export function CardConversor() {
  const [formatoDeOrigem, setFormatoDeOrigem] = useState<FormatoDeAudio>("MP3");
  const [formatoDeDestino, setFormatoDeDestino] =
    useState<FormatoDeAudio>("WAV");
  const [statusDaConversao, setStatusDaConversao] =
    useState<StatusDaConversao>("ocioso");
  const [nomeDoArquivoSelecionado, setNomeDoArquivoSelecionado] =
    useState<string>("");

  function trocarFormatos() {
    setFormatoDeOrigem(formatoDeDestino);
    setFormatoDeDestino(formatoDeOrigem);
  }

  function iniciarConversao(arquivoSelecionado: File) {
    setNomeDoArquivoSelecionado(arquivoSelecionado.name);
    setStatusDaConversao("convertendo");

    // Simulação do processamento — substituir pela chamada real
    // ao backend (Spring Boot) quando a integração for feita.
    setTimeout(() => {
      setStatusDaConversao("sucesso");
    }, 3000);
  }

  function reiniciarFluxoDeConversao() {
    setStatusDaConversao("ocioso");
    setNomeDoArquivoSelecionado("");
  }

  function baixarArquivoConvertido() {
    const nomeSemExtensao = nomeDoArquivoSelecionado.replace(/\.[^/.]+$/, "");
    alert(
      `Baixando: ${nomeSemExtensao}-converted.${formatoDeDestino.toLowerCase()}`,
    );
  }

  return (
    <div className="relative lg:mt-12">
      {/* Brilho decorativo atrás do card */}
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
