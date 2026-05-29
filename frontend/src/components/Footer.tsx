export function Footer() {
  return (
    <footer className="border-t border-white/10 bg-background">
      <div className="mx-auto flex max-w-7xl flex-col items-center justify-between gap-4 px-6 py-8 text-sm text-muted-foreground md:flex-row">
        <p className="font-mono text-xs uppercase tracking-[0.2em]">
          © {new Date().getFullYear()} Slicex Audio
        </p>
        <p className="font-mono text-xs uppercase tracking-[0.2em]">
          Feito com precisão
        </p>
      </div>
    </footer>
  );
}
