import { Navbar } from "@/components/Navbar";
import { Footer } from "@/components/Footer";
import { CabecalhoHero } from "@/components/conversor/CabecalhoHero";
import { CardConversor } from "@/components/conversor/CardConversor";

export default function App() {
  return (
    <div className="flex min-h-screen flex-col bg-background text-foreground">
      <Navbar />

      <main className="flex-1">
        <section className="mx-auto grid max-w-7xl gap-16 px-6 py-16 lg:grid-cols-2 lg:gap-28 lg:py-24">
          <CabecalhoHero />
          <CardConversor />
        </section>
      </main>

      <Footer />
    </div>
  );
}
