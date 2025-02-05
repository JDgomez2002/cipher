import java.util.*;

public class Graph {
    // Frecuencias teóricas del español
    private static final Map<Character, Double> SPANISH_FREQUENCIES = new HashMap<>() {{
        put('A', 12.53); put('B', 1.42);  put('C', 4.68);  put('D', 5.86);  put('E', 13.68);
        put('F', 0.69);  put('G', 1.01);  put('H', 0.70);  put('I', 6.25);  put('J', 0.44);
        put('K', 0.02);  put('L', 4.97);  put('M', 3.15);  put('N', 6.71);  put('Ñ', 0.31);
        put('O', 8.68);  put('P', 2.51);  put('Q', 0.88);  put('R', 6.87);  put('S', 7.98);
        put('T', 4.63);  put('U', 3.93);  put('V', 0.90);  put('W', 0.01);  put('X', 0.22);
        put('Y', 0.90);  put('Z', 0.52);
    }};

    public void compareDistributions(Map<Character, Double> observedFreq) {
        System.out.println("\nComparación de Distribuciones de Frecuencia:");
        System.out.println("============================================");
        System.out.printf("%-6s%-15s%-15s%-15s%n", "Letra", "Observada", "Teórica", "Diferencia");
        System.out.println("----------------------------------------------------");

        // Calcular diferencias y ordenar por magnitud de diferencia
        List<ComparisonEntry> comparisons = new ArrayList<>();

        for (char c : Utility.ALPHABET.toCharArray()) {
            double observed = observedFreq.getOrDefault(c, 0.0) * 100; // Convertir a porcentaje
            double theoretical = SPANISH_FREQUENCIES.getOrDefault(c, 0.0);
            double difference = Math.abs(observed - theoretical);

            comparisons.add(new ComparisonEntry(c, observed, theoretical, difference));
        }

        // Ordenar por diferencia descendente
        comparisons.sort((a, b) -> Double.compare(b.difference, a.difference));

        // Imprimir resultados
        for (ComparisonEntry entry : comparisons) {
            System.out.printf("%-6c%-15.2f%-15.2f%-15.2f%n",
                    entry.letter, entry.observed, entry.theoretical, entry.difference);
        }

        // Calcular y mostrar estadísticas
        printStatistics(comparisons);

        // Mostrar gráfico de comparación
        printComparisonGraph(comparisons);
    }

    private void printStatistics(List<ComparisonEntry> comparisons) {
        double totalDiff = comparisons.stream()
                .mapToDouble(e -> e.difference)
                .sum();
        double maxDiff = comparisons.stream()
                .mapToDouble(e -> e.difference)
                .max()
                .orElse(0.0);
        double avgDiff = totalDiff / comparisons.size();

        System.out.println("\nEstadísticas de la comparación:");
        System.out.printf("Diferencia total: %.2f%%\n", totalDiff);
        System.out.printf("Diferencia máxima: %.2f%%\n", maxDiff);
        System.out.printf("Diferencia promedio: %.2f%%\n", avgDiff);
    }

    private void printComparisonGraph(List<ComparisonEntry> comparisons) {
        System.out.println("\nGráfico de Comparación:");
        System.out.println("(O: Observada, T: Teórica)");
        System.out.println("0%      5%       10%      15%");
        System.out.println("|--------|--------|--------|");

        // Ordenar alfabéticamente para el gráfico
        comparisons.sort((a, b) -> Character.compare(a.letter, b.letter));

        for (ComparisonEntry entry : comparisons) {
            System.out.printf("%c ", entry.letter);

            // Calcular posiciones para los marcadores
            int obsPos = (int) (entry.observed * 2);  // *2 para escalar el gráfico
            int theoPos = (int) (entry.theoretical * 2);

            // Construir la línea del gráfico
            StringBuilder line = new StringBuilder(".".repeat(30));
            try {
                line.setCharAt(obsPos, 'O');
                line.setCharAt(theoPos, 'T');
            } catch (StringIndexOutOfBoundsException e) {
                // Manejar casos donde los valores exceden el tamaño del gráfico
                if (obsPos >= 30) line.setCharAt(29, 'O');
                if (theoPos >= 30) line.setCharAt(29, 'T');
            }

            System.out.println(line.toString());
        }
    }

    private static class ComparisonEntry {
        char letter;
        double observed;
        double theoretical;
        double difference;

        ComparisonEntry(char letter, double observed, double theoretical, double difference) {
            this.letter = letter;
            this.observed = observed;
            this.theoretical = theoretical;
            this.difference = difference;
        }
    }
}