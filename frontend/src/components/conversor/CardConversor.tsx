// src/components/conversor/CardConversor.tsx
import { useState } from "react";
import { SeletorDeFormato } from "./SeletorDeFormato";
import { EstadoOcioso } from "./EstadoOcioso";
import { EstadoConvertendo } from "./EstadoConvertendo";
import { EstadoSucesso } from "./EstadoSucesso";
import { ModalDeErro } from "@/components/ModalDeErro";
import {
  converterAudio,
  ErroDeConversao,
  type CodigoDeErroDeConversao,
} from "@/services/audioConversionService";
import type { FormatoDeAudio, StatusDaConversao } from "@/types/conversao";

type EstadoDoModal = {
  aberto: boolean;
  titulo: string;
  mensagem: string;
};

const MODAL_FECHADO: EstadoDoModal = {
  aberto: false,
  titulo: "",
  mensagem: "",
};

export function CardConversor() {
  const [formatoDeOrigem, setFormatoDeOrigem] = useState<FormatoDeAudio>("MP3");
  const [formatoDeDestino, setFormatoDeDestino] =
    useState<FormatoDeAudio>("WAV");
  const [statusDaConversao, setStatusDaConversao] =
    useState<StatusDaConversao>("ocioso");
  const [nomeDoArquivoSelecionado, setNomeDoArquivoSelecionado] = useState("");
  const [arquivoConvertido, setArquivoConvertido] = useState<Blob | null>(null);
  const [modal, setModal] = useState<EstadoDoModal>(MODAL_FECHADO);

  function trocarFormatos() {
    setFormatoDeOrigem(formatoDeDestino);
    setFormatoDeDestino(formatoDeOrigem);
  }

  function exibirErro(
    codigo: CodigoDeErroDeConversao,
    mensagemDoServidor: string,
  ) {
    const dados = mapearErroParaConteudoDoModal(codigo, mensagemDoServidor);
    setModal({ aberto: true, ...dados });
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

      if (error instanceof ErroDeConversao) {
        exibirErro(error.codigo, error.message);
      } else {
        exibirErro(
          "FALHA_NO_SERVIDOR",
          "Erro inesperado ao converter o áudio.",
        );
      }

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
    <>
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

      <ModalDeErro
        aberto={modal.aberto}
        titulo={modal.titulo}
        mensagem={modal.mensagem}
        aoFechar={() => setModal(MODAL_FECHADO)}
      />
    </>
  );
}

function mapearErroParaConteudoDoModal(
  codigo: CodigoDeErroDeConversao,
  mensagemDoServidor: string,
): { titulo: string; mensagem: string } {
  switch (codigo) {
    case "ARQUIVO_MUITO_GRANDE":
      return {
        titulo: "Arquivo muito grande",
        mensagem:
          "O arquivo selecionado ultrapassa o limite de 50MB. Escolha um arquivo menor e tente novamente.",
      };
    case "FORMATO_INCOMPATIVEL":
      return {
        titulo: "Formato incompatível",
        mensagem:
          "O formato declarado não corresponde ao conteúdo do arquivo enviado. Verifique se o arquivo é realmente um MP3 ou WAV e selecione o formato correto antes de enviar.",
      };
    case "TIMEOUT":
      return {
        titulo: "Tempo esgotado",
        mensagem:
          "O servidor demorou mais do que o esperado para concluir a conversão. Tente novamente em alguns instantes.",
      };
    case "ERRO_DE_REDE":
      return {
        titulo: "Falha de conexão",
        mensagem:
          "Não foi possível se comunicar com o servidor. Verifique sua conexão e tente novamente.",
      };
    case "FALHA_NO_SERVIDOR":
    default:
      return {
        titulo: "Não foi possível converter",
        mensagem: mensagemDoServidor || "Tente novamente em alguns instantes.",
      };
  }
}
