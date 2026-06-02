import { Navbar } from "@/components/Navbar";
import { Footer } from "@/components/Footer";

export default function Contato() {
  return (
    <div className="flex min-h-screen flex-col bg-background text-foreground">
      <Navbar />

      <main className="mx-auto w-full max-w-6xl flex-1 px-6 py-12 md:py-24">
        <section className="animate-fade-up space-y-20 pt-8">
          <div className="max-w-2xl space-y-6">
            <span className="block text-[10px] font-mono uppercase tracking-[0.4em] text-primary">
              Fale Conosco
            </span>
            <h1 className="font-serif text-5xl leading-tight tracking-tighter md:text-6xl">
              Contato
            </h1>
            <p className="text-lg leading-relaxed text-muted-foreground">
              Dúvidas? Problemas? Estamos aqui para garantir que sua experiência
              com a Slicex seja sempre a melhor possível.
            </p>
          </div>

          <form
            className="grid grid-cols-1 gap-x-12 gap-y-12 md:grid-cols-2"
            onSubmit={(e) => e.preventDefault()}
          >
            <div className="space-y-4">
              <label className="block text-[10px] font-mono uppercase tracking-widest text-muted-foreground">
                Nome Completo
              </label>
              <input
                type="text"
                placeholder="Seu nome"
                className="w-full border-b border-white/10 bg-transparent py-4 text-lg text-foreground transition-colors placeholder:text-muted-foreground/50 focus:border-primary focus:outline-none"
              />
            </div>

            <div className="space-y-4">
              <label className="block text-[10px] font-mono uppercase tracking-widest text-muted-foreground">
                Endereço de Email
              </label>
              <input
                type="email"
                placeholder="voce@exemplo.com"
                className="w-full border-b border-white/10 bg-transparent py-4 text-lg text-foreground transition-colors placeholder:text-muted-foreground/50 focus:border-primary focus:outline-none"
              />
            </div>

            <div className="space-y-4 md:col-span-2">
              <label className="block text-[10px] font-mono uppercase tracking-widest text-muted-foreground">
                Sua Mensagem
              </label>
              <textarea
                rows={3}
                placeholder="Como podemos ajudar?"
                className="w-full resize-none border-b border-white/10 bg-transparent py-4 text-lg text-foreground transition-colors placeholder:text-muted-foreground/50 focus:border-primary focus:outline-none"
              />
            </div>

            <div className="pt-4">
              <button
                type="submit"
                className="group inline-flex items-center gap-4 rounded-full bg-primary px-8 py-4 text-primary-foreground transition-all hover:bg-primary/90 hover:shadow-lg hover:shadow-primary/30"
              >
                <span className="text-[10px] font-mono uppercase tracking-[0.3em] font-bold">
                  Enviar Mensagem
                </span>
                <div className="h-px w-8 bg-primary-foreground transition-all group-hover:w-14" />
              </button>
            </div>
          </form>
        </section>
      </main>

      <Footer />
    </div>
  );
}
