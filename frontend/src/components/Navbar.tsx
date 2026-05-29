export function Navbar() {
  return (
    <header className="border-b border-white/10 bg-background/80 backdrop-blur">
      <nav className="mx-auto flex max-w-7xl items-center justify-between px-6 py-5">
        <a href="/" className="font-serif text-2xl tracking-tight">
          Slicex
        </a>
        <ul className="hidden items-center gap-8 text-sm text-muted-foreground md:flex">
          <li>
            <a href="#" className="transition-colors hover:text-foreground">
              Conversor
            </a>
          </li>
          <li>
            <a href="#" className="transition-colors hover:text-foreground">
              Sobre
            </a>
          </li>
          <li>
            <a href="#" className="transition-colors hover:text-foreground">
              Contato
            </a>
          </li>
        </ul>
      </nav>
    </header>
  );
}
