// src/services/audioConversionService.ts

const API_URL = import.meta.env.VITE_API_URL ?? "http://localhost:8080";

// 50 MB — alinhado ao limite mostrado na UI.
export const TAMANHO_MAXIMO_EM_BYTES = 50 * 1024 * 1024;

// Tempo máximo total que o frontend espera pela conversão (fila + processamento).
const TIMEOUT_TOTAL_MS = 2 * 60 * 1000; // 2 minutos
const INTERVALO_DE_POLLING_MS = 1500;

export type CodigoDeErroDeConversao =
  | "ARQUIVO_MUITO_GRANDE"
  | "FORMATO_INCOMPATIVEL"
  | "TIMEOUT"
  | "FALHA_NO_SERVIDOR"
  | "ERRO_DE_REDE";

export class ErroDeConversao extends Error {
  readonly codigo: CodigoDeErroDeConversao;

  constructor(codigo: CodigoDeErroDeConversao, mensagem: string) {
    super(mensagem);
    this.name = "ErroDeConversao";
    this.codigo = codigo;
  }
}

type RespostaDeJob = {
  id: string;
  status: "PENDENTE" | "EM_PROCESSAMENTO" | "CONCLUIDO" | "FALHOU";
  message?: string | null;
};

export async function converterAudio(
  arquivo: File,
  formatoOrigem: string,
  formatoDestino: string,
): Promise<Blob> {
  // 1) Validação local — evita ida ao servidor e o "Failed to fetch".
  if (arquivo.size > TAMANHO_MAXIMO_EM_BYTES) {
    throw new ErroDeConversao(
      "ARQUIVO_MUITO_GRANDE",
      "O arquivo enviado ultrapassa o limite de 50MB.",
    );
  }

  // 2) Enfileira a conversão no backend.
  const formData = new FormData();
  formData.append("file", arquivo);
  formData.append("nomeDoArquivoOriginal", arquivo.name);
  formData.append("formatoPreConversao", formatoOrigem);
  formData.append("formatoPosConversao", formatoDestino);

  const respostaDeEnfileiramento = await chamarApi(
    `${API_URL}/api/audio/conversions`,
    { method: "POST", body: formData },
  );

  if (!respostaDeEnfileiramento.ok) {
    throw await traduzirErroDeResposta(respostaDeEnfileiramento);
  }

  const job = (await respostaDeEnfileiramento.json()) as RespostaDeJob;

  // 3) Polling do status até concluir, falhar ou estourar o timeout.
  const limite = Date.now() + TIMEOUT_TOTAL_MS;

  while (Date.now() < limite) {
    await esperar(INTERVALO_DE_POLLING_MS);

    const respostaDeStatus = await chamarApi(
      `${API_URL}/api/audio/conversions/${job.id}`,
      { method: "GET" },
    );

    if (!respostaDeStatus.ok) {
      throw await traduzirErroDeResposta(respostaDeStatus);
    }

    const statusAtual = (await respostaDeStatus.json()) as RespostaDeJob;

    if (statusAtual.status === "CONCLUIDO") {
      const respostaDeDownload = await chamarApi(
        `${API_URL}/api/audio/conversions/${job.id}/download`,
        { method: "GET" },
      );

      if (!respostaDeDownload.ok) {
        throw await traduzirErroDeResposta(respostaDeDownload);
      }

      return respostaDeDownload.blob();
    }

    if (statusAtual.status === "FALHOU") {
      throw new ErroDeConversao(
        "FALHA_NO_SERVIDOR",
        statusAtual.message ?? "A conversão falhou no servidor.",
      );
    }
  }

  throw new ErroDeConversao(
    "TIMEOUT",
    "O servidor demorou demais para concluir a conversão.",
  );
}

async function chamarApi(url: string, init: RequestInit): Promise<Response> {
  try {
    return await fetch(url, init);
  } catch {
    throw new ErroDeConversao(
      "ERRO_DE_REDE",
      "Não foi possível conectar ao servidor.",
    );
  }
}

async function traduzirErroDeResposta(resposta: Response): Promise<ErroDeConversao> {
  // 413 = Payload Too Large (Spring / proxy podem retornar antes do handler).
  if (resposta.status === 413) {
    return new ErroDeConversao(
      "ARQUIVO_MUITO_GRANDE",
      "O arquivo enviado ultrapassa o limite de 50MB.",
    );
  }

  let mensagem = "Erro ao converter o áudio.";
  try {
    const corpo = await resposta.json();
    mensagem = corpo?.message ?? mensagem;
  } catch {
    /* corpo não-JSON, mantém mensagem padrão */
  }

  // Heurística: identificação do erro de formato incompatível vindo do backend.
  if (/formato.*não corresponde/i.test(mensagem) || /formato.*incompat/i.test(mensagem)) {
    return new ErroDeConversao("FORMATO_INCOMPATIVEL", mensagem);
  }

  if (/excede.*tamanho/i.test(mensagem)) {
    return new ErroDeConversao("ARQUIVO_MUITO_GRANDE", mensagem);
  }

  return new ErroDeConversao("FALHA_NO_SERVIDOR", mensagem);
}

function esperar(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}
