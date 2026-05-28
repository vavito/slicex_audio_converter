export function CabecalhoHero() {
  return (
    <div className="space-y-10">
      <h1 className="font-serif text-5xl leading-[0.9] tracking-tighter text-balance md:text-7xl lg:text-8xl">
        Converta seu <span className="italic text-primary">áudio</span> com
        precisão.
      </h1>

      <p className="max-w-md text-lg leading-relaxed text-balance text-muted-foreground">
        A Slicex oferece conversão de áudio com foco em simplicidade e
        integridade. Feito para engenheiros, desenhado para criadores.
      </p>

      <div className="flex items-center gap-4 text-[10px] font-mono uppercase tracking-[0.2em] text-muted-foreground">
        <span>Seguro</span>
        <span className="text-white/10">/</span>
        <span>Confiável</span>
        <span className="text-white/10">/</span>
        <span>Ultra rápido</span>
      </div>
    </div>
  );
}
