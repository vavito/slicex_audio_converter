export async function converterAudio(
  arquivo: File,
  formatoOrigem: string,
  formatoDestino: string,
): Promise<Blob> {
  const formData = new FormData();

  formData.append("file", arquivo);
  formData.append("nomeDoArquivoOriginal", arquivo.name);
  formData.append("formatoPreConversao", formatoOrigem);
  formData.append("formatoPosConversao", formatoDestino);

  const response = await fetch("http://localhost:8080/api/audio/conversions", {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    let mensagemDeErro = "Erro ao converter áudio.";

    try {
      const erro = await response.json();
      mensagemDeErro = erro.message ?? mensagemDeErro;
    } catch {
      // Mantém mensagem padrão se a resposta não vier em JSON.
    }

    throw new Error(mensagemDeErro);
  }

  return response.blob();
}
