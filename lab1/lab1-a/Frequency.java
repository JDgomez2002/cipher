import java.util.*;

public class Frequency {
    private Map<Character, Double> frequencies;
    private int totalCharacters;

    public Frequency() {
        frequencies = new HashMap<>();
        // Inicializar todas las letras del alfabeto con frecuencia 0
        for (char c : Utility.ALPHABET.toCharArray()) {
            frequencies.put(c, 0.0);
        }
    }

    public void analyzeText(String text) {
        // Limpiar el texto y convertirlo a mayúsculas
        text = Utility.cleanText(text);
        totalCharacters = text.length();

        // Contar frecuencias
        for (char c : text.toCharArray()) {
            frequencies.put(c, frequencies.get(c) + 1);
        }

        // Convertir conteos a probabilidades
        for (char c : frequencies.keySet()) {
            double probability = frequencies.get(c) / totalCharacters;
            frequencies.put(c, probability);
        }
    }

    public Map<Character, Double> getFrequencies() {
        return new HashMap<>(frequencies);
    }

    public void printAnalysis() {
        System.out.println("\nAnálisis de Frecuencia:");
        System.out.println("Total de caracteres: " + totalCharacters);
        System.out.println("\nDistribución de frecuencias:");
        System.out.printf("%-10s%-15s%-15s%n", "Letra", "Frecuencia", "Porcentaje");
        System.out.println("----------------------------------------");

        // Ordenar las letras por frecuencia (de mayor a menor)
        List<Map.Entry<Character, Double>> sortedFreq = new ArrayList<>(frequencies.entrySet());
        sortedFreq.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (Map.Entry<Character, Double> entry : sortedFreq) {
            char letra = entry.getKey();
            double freq = entry.getValue();
            int count = (int) (freq * totalCharacters);
            System.out.printf("%-10c%-15d%.4f%%%n",
                    letra,
                    count,
                    freq * 100);
        }
    }

    // Método para generar un histograma ASCII simple
    public void printHistogram() {
        System.out.println("\nHistograma de frecuencias:");
        int maxBars = 50; // Máximo número de caracteres para la barra

        // Encontrar la frecuencia máxima para escalar el histograma
        double maxFreq = frequencies.values().stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0.0);

        // Ordenar por frecuencia descendente
        List<Map.Entry<Character, Double>> sortedFreq = new ArrayList<>(frequencies.entrySet());
        sortedFreq.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (Map.Entry<Character, Double> entry : sortedFreq) {
            char letra = entry.getKey();
            double freq = entry.getValue();

            // Calcular el número de caracteres para la barra
            int numBars = (int) ((freq / maxFreq) * maxBars);

            // Imprimir la letra y su barra
            System.out.printf("%c |%-50s| %.2f%%%n",
                    letra,
                    "=".repeat(numBars),
                    freq * 100);
        }
    }
}