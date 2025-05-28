import { useState } from "react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Separator } from "@/components/ui/separator";
import {
  Play,
  Pause,
  KeyRound,
  ShieldCheck,
  Lightbulb,
  Network,
} from "lucide-react";

// Helper type for bases
type Basis = "↕" | "↗"; // Rectilinear or Diagonal

interface SimulationStep {
  index: number;
  aliceBit?: number;
  aliceBase?: Basis;
  bobBase?: Basis;
  eveBase?: Basis; // Eve's chosen basis for interception
  eveAction?: string; // Description of Eve's action
  qubitSentToBob?: { bit: number; base: Basis }; // Qubit Eve sends to Bob
  bobMeasurementOutcome?: number;
  basesMatch?: boolean;
  bitKept?: boolean;
  eveInterferenceDetected?: boolean;
}

export default function QkdCard() {
  const [n, setN] = useState<number>(10); // Number of qubits

  // Alice's state
  const [aliceBits, setAliceBits] = useState<number[]>([]);
  const [aliceBases, setAliceBases] = useState<Basis[]>([]);

  // Bob's state
  const [bobBases, setBobBases] = useState<Basis[]>([]);
  const [bobMeasuredBits, setBobMeasuredBits] = useState<number[]>([]);

  // Eve's state
  const [evePresent, setEvePresent] = useState<boolean>(false);
  const [eveBases, setEveBases] = useState<Basis[]>([]);
  const [eveInterceptionResults, setEveInterceptionResults] = useState<
    Array<{
      originalBit: number;
      measuredByEve: number;
      resentToBobAs: number;
      eveBasis: Basis;
    }>
  >([]);

  // Shared key and process
  const [siftedKeyAlice, setSiftedKeyAlice] = useState<number[]>([]);
  const [siftedKeyBob, setSiftedKeyBob] = useState<number[]>([]);
  const [simulationLog, setSimulationLog] = useState<SimulationStep[]>([]);
  const [errorRate, setErrorRate] = useState<number>(0);

  const generateRandomBit = (): number => (Math.random() < 0.5 ? 0 : 1);
  const generateRandomBase = (): Basis => (Math.random() < 0.5 ? "↕" : "↗");

  const runSimulation = () => {
    const newAliceBits: number[] = [];
    const newAliceBases: Basis[] = [];
    const newBobBases: Basis[] = [];
    const newBobMeasuredBits: number[] = [];
    const newEveBases: Basis[] = [];
    const newEveInterceptionResultsLocal: typeof eveInterceptionResults = []; // Renamed to avoid conflict
    const newSimulationLog: SimulationStep[] = [];

    for (let i = 0; i < n; i++) {
      const bitA = generateRandomBit();
      const basisA = generateRandomBase();
      newAliceBits.push(bitA);
      newAliceBases.push(basisA);

      const basisB = generateRandomBase();
      newBobBases.push(basisB);

      let bitSentToBob = bitA;
      let basisSentToBob = basisA;
      let logEntry: SimulationStep = {
        index: i,
        aliceBit: bitA,
        aliceBase: basisA,
        bobBase: basisB,
      };

      if (evePresent) {
        const basisE = generateRandomBase();
        newEveBases.push(basisE);
        logEntry.eveBase = basisE;

        let measuredBitE: number;
        // Eve measures Alice's qubit
        if (basisA === basisE) {
          measuredBitE = bitA; // Eve's basis matches Alice's, measurement is correct
          logEntry.eveAction = `Eve midio ${bitA} (base ${basisE} coincidio con ${basisA})`;
        } else {
          measuredBitE = generateRandomBit(); // Eve's basis differs, measurement is random
          logEntry.eveAction = `Eve midio aleatoriamente ${measuredBitE} (base ${basisE} no coincidio con ${basisA})`;
        }

        // Eve resends a qubit in her measurement basis
        bitSentToBob = measuredBitE;
        basisSentToBob = basisE; // Eve sends using her basis
        logEntry.qubitSentToBob = { bit: measuredBitE, base: basisE };
        newEveInterceptionResultsLocal.push({
          originalBit: bitA,
          measuredByEve: measuredBitE,
          resentToBobAs: measuredBitE,
          eveBasis: basisE,
        });
      }

      // Bob measures the qubit (either directly from Alice or from Eve)
      let measuredBitB: number;
      if (basisSentToBob === basisB) {
        measuredBitB = bitSentToBob; // Bob's basis matches the incoming qubit's basis
      } else {
        measuredBitB = generateRandomBit(); // Bob's basis differs, measurement is random
      }
      newBobMeasuredBits.push(measuredBitB);
      logEntry.bobMeasurementOutcome = measuredBitB;
      newSimulationLog.push(logEntry);
    }

    setAliceBits(newAliceBits);
    setAliceBases(newAliceBases);
    setBobBases(newBobBases);
    setBobMeasuredBits(newBobMeasuredBits);
    if (evePresent) {
      setEveBases(newEveBases);
      setEveInterceptionResults(newEveInterceptionResultsLocal);
    } else {
      setEveBases([]);
      setEveInterceptionResults([]);
    }

    // Sifting process
    const finalAliceKey: number[] = [];
    const finalBobKey: number[] = [];
    let mismatches = 0;
    let agreedBitsCount = 0;

    newSimulationLog.forEach((log, i) => {
      if (newAliceBases[i] === newBobBases[i]) {
        log.basesMatch = true;
        finalAliceKey.push(newAliceBits[i]);
        finalBobKey.push(newBobMeasuredBits[i]);
        log.bitKept = true;

        if (newAliceBits[i] !== newBobMeasuredBits[i]) {
          mismatches++;
          log.eveInterferenceDetected = true;
        }
        agreedBitsCount++;
      } else {
        log.basesMatch = false;
        log.bitKept = false;
      }
    });

    setSiftedKeyAlice(finalAliceKey);
    setSiftedKeyBob(finalBobKey);
    setSimulationLog(newSimulationLog); // Update simulation log with sifting info
    setErrorRate(
      agreedBitsCount > 0 ? (mismatches / agreedBitsCount) * 100 : 0
    );
  };

  return (
    <Card className="w-full max-w-4xl mx-auto">
      <CardHeader>
        <CardTitle>Simulación del Protocolo BB84 QKD</CardTitle>
        <div className="flex items-center justify-center space-x-3 pt-2 text-blue-600 dark:text-blue-400">
          <KeyRound size={20} />
          <ShieldCheck size={20} />
          <Lightbulb size={20} />
          <Network size={20} />
        </div>
        <p className="pt-3 text-sm text-zinc-300 text-center">
          Este programa te permite jugar con una simulación de criptografía
          cuántica, específicamente el protocolo BB84. Imagina que Alice quiere
          enviar un mensaje secreto (una clave) a Bob. Usan partículas de luz
          (fotones) y dos 'idiomas' (bases) para codificar '0's y '1's. Si un
          espía, Eve, intenta escuchar, inevitablemente perturbará las
          partículas, ¡alertando a Alice y Bob! Esta simulación te muestra cómo
          generan una clave segura, cómo Eve puede intentar interceptarla y cómo
          se dan cuenta si alguien está espiando. ¡Es como un juego de espías
          con física cuántica para proteger secretos!
        </p>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="flex flex-wrap items-center gap-4 mb-4">
          <div className="flex items-center space-x-2">
            <Label htmlFor="n_qubits">Número de Qubits (n):</Label>
            <Input
              id="n_qubits"
              type="number"
              value={n}
              onChange={(e) => setN(Math.max(1, parseInt(e.target.value) || 1))}
              className="w-24"
              min="1"
            />
          </div>
          <div className="flex items-center space-x-2">
            <input
              type="checkbox"
              id="eve_present_checkbox"
              checked={evePresent}
              onChange={(e) => {
                setEvePresent(e.target.checked);
                // Optionally, clear previous Eve data if she's removed
                if (!e.target.checked) {
                  setEveBases([]);
                  setEveInterceptionResults([]);
                }
              }}
              className="h-4 w-4 text-blue-600 border-zinc-300 rounded focus:ring-blue-500"
            />
            <Label htmlFor="eve_present_checkbox">
              Incluir a Eve (Interceptora)
            </Label>
          </div>
          <Button onClick={runSimulation} variant="outline">
            <Play className="w-4 h-4" />
            Correr Simulación
          </Button>
        </div>

        {simulationLog.length > 0 && (
          <div className="space-y-4">
            <h3 className="text-xl font-semibold mb-2">
              Proceso de transmisión y medición
            </h3>
            <div className="overflow-x-auto shadow rounded-lg">
              <table className="min-w-full divide-y divide-zinc-200 dark:divide-zinc-700">
                <thead className="bg-zinc-50 dark:bg-zinc-700">
                  <tr>
                    <th className="px-4 py-3 text-left text-xs font-medium text-zinc-500 dark:text-zinc-300 uppercase tracking-wider">
                      #
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-zinc-500 dark:text-zinc-300 uppercase tracking-wider">
                      Bit Alice
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-zinc-500 dark:text-zinc-300 uppercase tracking-wider">
                      Base Alice
                    </th>
                    {evePresent && (
                      <th className="px-4 py-3 text-left text-xs font-medium text-zinc-500 dark:text-zinc-300 uppercase tracking-wider">
                        Base Eve
                      </th>
                    )}
                    {evePresent && (
                      <th className="px-4 py-3 text-left text-xs font-medium text-zinc-500 dark:text-zinc-300 uppercase tracking-wider min-w-[200px]">
                        Acción Eve
                      </th>
                    )}
                    <th className="px-4 py-3 text-left text-xs font-medium text-zinc-500 dark:text-zinc-300 uppercase tracking-wider">
                      Base Bob
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-zinc-500 dark:text-zinc-300 uppercase tracking-wider">
                      Bit Bob (Medido)
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-zinc-500 dark:text-zinc-300 uppercase tracking-wider">
                      Bases Coinciden? (A-B)
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-zinc-500 dark:text-zinc-300 uppercase tracking-wider">
                      Bit Conservado?
                    </th>
                    {evePresent && (
                      <th className="px-4 py-3 text-left text-xs font-medium text-red-500 dark:text-red-400 uppercase tracking-wider">
                        Error?
                      </th>
                    )}
                  </tr>
                </thead>
                <tbody className="bg-white dark:bg-zinc-800 divide-y divide-zinc-200 dark:divide-zinc-700">
                  {simulationLog.map((log) => (
                    <tr
                      key={log.index}
                      className={`${
                        log.eveInterferenceDetected
                          ? "bg-red-100 dark:bg-red-900"
                          : ""
                      } ${!log.basesMatch ? "opacity-60" : ""}`}
                    >
                      <td className="px-4 py-3 whitespace-nowrap text-sm text-zinc-900 dark:text-zinc-100">
                        {log.index + 1}
                      </td>
                      <td className="px-4 py-3 whitespace-nowrap text-sm text-zinc-900 dark:text-zinc-100">
                        {log.aliceBit}
                      </td>
                      <td className="px-4 py-3 whitespace-nowrap text-sm text-zinc-900 dark:text-zinc-100">
                        {log.aliceBase}
                      </td>
                      {evePresent && (
                        <td className="px-4 py-3 whitespace-nowrap text-sm text-zinc-900 dark:text-zinc-100">
                          {log.eveBase ?? "-"}
                        </td>
                      )}
                      {evePresent && (
                        <td className="px-4 py-3 text-sm text-zinc-700 dark:text-zinc-300 min-w-[200px]">
                          {log.eveAction ?? "-"}
                        </td>
                      )}
                      <td className="px-4 py-3 whitespace-nowrap text-sm text-zinc-900 dark:text-zinc-100">
                        {log.bobBase}
                      </td>
                      <td className="px-4 py-3 whitespace-nowrap text-sm text-zinc-900 dark:text-zinc-100">
                        {log.bobMeasurementOutcome}
                      </td>
                      <td className="px-4 py-3 whitespace-nowrap text-sm text-zinc-900 dark:text-zinc-100">
                        {log.basesMatch ? "Sí" : "No"}
                      </td>
                      <td className="px-4 py-3 whitespace-nowrap text-sm font-medium text-zinc-900 dark:text-zinc-100">
                        {log.bitKept ? `Sí (${log.aliceBit})` : "No"}
                      </td>
                      {evePresent && (
                        <td
                          className={`px-4 py-3 whitespace-nowrap text-sm font-medium ${
                            log.eveInterferenceDetected
                              ? "text-red-600 dark:text-red-400"
                              : "text-green-600 dark:text-green-400"
                          }`}
                        >
                          {log.eveInterferenceDetected ? "Sí" : "No"}
                        </td>
                      )}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <h3 className="text-xl font-semibold mt-6 mb-4">
              Sumario y clave sincronizada
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
              <div className="p-3 bg-zinc-50 dark:bg-zinc-700 rounded shadow">
                <strong>Bits Originales de Alice:</strong>{" "}
                <span className="font-mono">{aliceBits.join(" ")}</span>
              </div>
              <div className="p-3 bg-zinc-50 dark:bg-zinc-700 rounded shadow">
                <strong>Bases de Alice (A):</strong>{" "}
                <span className="font-mono">{aliceBases.join(" ")}</span>
              </div>
              <div className="p-3 bg-zinc-50 dark:bg-zinc-700 rounded shadow">
                <strong>Bases de Bob (B):</strong>{" "}
                <span className="font-mono">{bobBases.join(" ")}</span>
              </div>
              {evePresent && (
                <div className="p-3 bg-orange-50 dark:bg-orange-800 rounded shadow">
                  <strong>Bases de Eve (E):</strong>{" "}
                  <span className="font-mono">{eveBases.join(" ")}</span>
                </div>
              )}
              <div className="p-3 bg-zinc-50 dark:bg-zinc-700 rounded shadow">
                <strong>Bits Medidos por Bob:</strong>{" "}
                <span className="font-mono">{bobMeasuredBits.join(" ")}</span>
              </div>
            </div>

            <div
              className={`mt-4 p-4 rounded shadow ${
                siftedKeyAlice.join("") === siftedKeyBob.join("")
                  ? "dark:bg-green-800"
                  : "dark:bg-red-800"
              }`}
            >
              <p className="text-lg">
                <strong>Clave Sincronizada de Alice:</strong>{" "}
                <span className="font-mono text-green-700 dark:text-green-300">
                  {siftedKeyAlice.join("") || "(ninguna)"}
                </span>
              </p>
              <p className="text-lg">
                <strong>Clave Sincronizada de Bob:</strong>{" "}
                <span className="font-mono text-green-700 dark:text-green-300">
                  {siftedKeyBob.join("") || "(ninguna)"}
                </span>
              </p>
              <Separator className="my-4 bg-white/75" />
              {siftedKeyAlice.length > 0 && (
                <p
                  className={`text-lg font-semibold ${
                    siftedKeyAlice.join("") === siftedKeyBob.join("")
                      ? "text-green-700 dark:text-green-300"
                      : "text-red-700 dark:text-red-100"
                  }`}
                >
                  <strong>Coincidencia de Claves (Alice vs Bob):</strong>{" "}
                  {siftedKeyAlice.join("") === siftedKeyBob.join("")
                    ? "¡Perfecta!"
                    : "¡Discrepancia!"}
                </p>
              )}
              {evePresent && siftedKeyAlice.length > 0 && (
                <p className="text-lg font-bold text-red-600 dark:text-red-100 mt-2">
                  Tasa de Error Cuántico de Bits (QBER): {errorRate.toFixed(2)}%
                  {errorRate > 0 && " (¡Eve ha sido detectada!)"}
                </p>
              )}
              {!evePresent &&
                siftedKeyAlice.length > 0 &&
                siftedKeyAlice.join("") !== siftedKeyBob.join("") && (
                  <p className="font-bold text-orange-600 dark:text-orange-400 mt-2">
                    Nota: Se detectó una discrepancia en las claves sin Eve. En
                    una simulación ideal de BB84, esto no debería ocurrir si las
                    bases de Alice y Bob coincidieron.
                  </p>
                )}
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
