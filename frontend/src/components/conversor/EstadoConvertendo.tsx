type EstadoConvertendoProps = {
  nomeDoArquivo: string;
};

export function EstadoConvertendo({ nomeDoArquivo }: EstadoConvertendoProps) {
  return (
    <div className="space-y-6 rounded-2xl border border-white/10 bg-background/50 px-6 py-10 text-center">
      <div
        className="spinner-conversor mx-auto size-12 rounded-full border-2 border-primary/20 border-t-primary"
        role="status"
        aria-label="Convertendo arquivo"
      />
      <p className="font-mono text-sm text-muted-foreground">
        Convertendo {nomeDoArquivo}...
      </p>
    </div>
  );
}
