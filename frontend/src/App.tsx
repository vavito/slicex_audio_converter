import { CabecalhoHero } from "./components/conversor/CabecalhoHero";
import { CardConversor } from "./components/conversor/CardConversor";

function App() {
  return (
    <main className="mx-auto max-w-6xl px-6 py-12 md:py-24">
      <section className="animate-fade-up grid items-start gap-16 lg:grid-cols-2">
        <CabecalhoHero />
        <CardConversor />
      </section>
    </main>
  );
}

export default App;
