import { useState } from "react";

export function Navbar() {
  const [mobileOpen, setMobileOpen] = useState(false);

  return (
    <nav className="sticky top-0 z-50 border-b border-white/5 bg-background/80 backdrop-blur-md">
      <div className="mx-auto flex h-20 max-w-6xl items-center justify-between px-6">
        <div className="flex items-center gap-12">
          <a
            href="/"
            className="font-serif text-2xl italic tracking-tight text-foreground"
          >
            Slicex
          </a>
          <div className="hidden items-center gap-8 text-[11px] font-mono uppercase tracking-widest text-muted-foreground md:flex">
            <a
              href="/"
              className="text-foreground transition-colors hover:text-foreground"
            >
              Conversor
            </a>
            <a
              href="/contato"
              className="transition-colors hover:text-foreground"
            >
              Contato
            </a>
          </div>
        </div>

        {/* Botão mobile */}
        <button
          onClick={() => setMobileOpen(!mobileOpen)}
          className="flex size-10 flex-col items-center justify-center gap-1.5 md:hidden"
          aria-label="Menu"
        >
          <span
            className={`h-0.5 w-6 bg-foreground transition-transform ${mobileOpen ? "translate-y-2 rotate-45" : ""}`}
          />
          <span
            className={`h-0.5 w-6 bg-foreground transition-opacity ${mobileOpen ? "opacity-0" : ""}`}
          />
          <span
            className={`h-0.5 w-6 bg-foreground transition-transform ${mobileOpen ? "-translate-y-2 -rotate-45" : ""}`}
          />
        </button>
      </div>

      {/* Menu mobile */}
      {mobileOpen && (
        <div className="border-t border-white/5 bg-background/95 px-6 py-6 backdrop-blur-md md:hidden">
          <div className="flex flex-col gap-6 text-sm font-mono uppercase tracking-widest text-muted-foreground">
            <a
              href="/"
              onClick={() => setMobileOpen(false)}
              className="text-foreground"
            >
              Conversor
            </a>
            <a
              href="/contato"
              onClick={() => setMobileOpen(false)}
              className="hover:text-foreground"
            >
              Contato
            </a>
          </div>
        </div>
      )}
    </nav>
  );
}
