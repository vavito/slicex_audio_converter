export function Footer() {
  return (
    <footer className="mt-40 border-t border-white/5 py-20">
      <div className="mx-auto flex max-w-6xl flex-col items-start justify-between gap-12 px-6 md:flex-row md:items-center">
        <div className="space-y-4">
          <span className="block font-serif text-3xl italic">Slicex</span>
          <p className="text-[10px] font-mono uppercase tracking-widest text-muted-foreground">
            &copy; 2026 SLICEX AUDIO LABS. TODOS OS DIREITOS RESERVADOS.
          </p>
        </div>

        <div className="flex flex-wrap gap-x-16 gap-y-4 text-[10px] font-mono uppercase tracking-widest text-muted-foreground">
          <div className="flex flex-col gap-3">
            <span className="mb-2 text-foreground">Legal</span>
            <a href="/" className="transition-colors hover:text-primary">
              Termos
            </a>
            <a href="/" className="transition-colors hover:text-primary">
              Privacidade
            </a>
          </div>
          <div className="flex flex-col gap-3">
            <span className="mb-2 text-foreground">Social</span>
            <a href="/" className="transition-colors hover:text-primary">
              Instagram
            </a>
            <a href="/" className="transition-colors hover:text-primary">
              Twitter
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
}
