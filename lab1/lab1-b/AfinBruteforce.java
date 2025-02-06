import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class AfinBruteforce {
  // Valores válidos de 'a' (coprimos con 27)
  private static final int[] VALID_A = {1, 2, 4, 5, 7, 8, 10, 11, 13, 14, 16, 17, 19, 20, 22, 23, 25, 26};

  public static void main(String[] args) {
    try {
      // Leer el archivo cifrado
      String ciphertext = new String(Files.readAllBytes(Paths.get("Cifrados/afines.txt")));

      // Obtener las k mejores claves (top 5)
      int k = 5;
      List<KeyMetric> bestKeys = findBestKeys(ciphertext, k);

      // Mostrar resultados
      System.out.println("Top " + k + " posibles combinaciones de claves para el texto cifrado Afin:");
      for (int i = 0; i < bestKeys.size(); i++) {
        KeyMetric keyMetric = bestKeys.get(i);
        Afin afin = new Afin(keyMetric.a, keyMetric.b);
        String decrypted = afin.decrypt(ciphertext);

        System.out.printf("\n%d. Claves: a=%d, b=%d (Distancia: %.4f)\n",
                i + 1, keyMetric.a, keyMetric.b, keyMetric.distance);
        System.out.println("Primeros 100 caracteres del texto descifrado:");
        System.out.println(decrypted.substring(0, Math.min(100, decrypted.length())));
        System.out.println("-".repeat(80));
      }

    } catch (IOException e) {
      System.err.println("Error al leer el archivo: " + e.getMessage());
    }
  }

  static class KeyMetric implements Comparable<KeyMetric> {
    int a;
    int b;
    double distance;

    KeyMetric(int a, int b, double distance) {
      this.a = a;
      this.b = b;
      this.distance = distance;
    }

    @Override
    public int compareTo(KeyMetric other) {
      return Double.compare(this.distance, other.distance);
    }
  }

  private static List<KeyMetric> findBestKeys(String ciphertext, int k) {
    // Frecuencias esperadas del español
    Map<Character, Double> expectedFreq = new HashMap<>();
    expectedFreq.put('A', 12.53); expectedFreq.put('B', 1.42);
    expectedFreq.put('C', 4.68);  expectedFreq.put('D', 5.86);
    expectedFreq.put('E', 13.68); expectedFreq.put('F', 0.69);
    expectedFreq.put('G', 1.01);  expectedFreq.put('H', 0.70);
    expectedFreq.put('I', 6.25);  expectedFreq.put('J', 0.44);
    expectedFreq.put('K', 0.02);  expectedFreq.put('L', 4.97);
    expectedFreq.put('M', 3.15);  expectedFreq.put('N', 6.71);
    expectedFreq.put('Ñ', 0.31);  expectedFreq.put('O', 8.68);
    expectedFreq.put('P', 2.51);  expectedFreq.put('Q', 0.88);
    expectedFreq.put('R', 6.87);  expectedFreq.put('S', 7.98);
    expectedFreq.put('T', 4.63);  expectedFreq.put('U', 3.93);
    expectedFreq.put('V', 0.90);  expectedFreq.put('W', 0.01);
    expectedFreq.put('X', 0.22);  expectedFreq.put('Y', 0.90);
    expectedFreq.put('Z', 0.52);

    List<KeyMetric> metrics = new ArrayList<>();

    // Probar todas las combinaciones válidas de a y b
    for (int a : VALID_A) {
      for (int b = 0; b < Utility.M; b++) {
        try {
          Afin afin = new Afin(a, b);
          String decrypted = afin.decrypt(ciphertext);

          // Analizar frecuencias del texto descifrado
          Frequency freq = new Frequency();
          freq.analyzeText(decrypted);
          Map<Character, Double> actualFreq = freq.getFrequencies();

          // Calcular distancia euclidiana entre frecuencias
          double distance = calculateDistance(expectedFreq, actualFreq);
          metrics.add(new KeyMetric(a, b, distance));
        } catch (IllegalArgumentException e) {
          // Ignorar combinaciones inválidas de claves
          continue;
        }
      }
    }

    // Ordenar por distancia y obtener las k mejores
    Collections.sort(metrics);
    return metrics.subList(0, Math.min(k, metrics.size()));
  }

  private static double calculateDistance(Map<Character, Double> expected,
                                          Map<Character, Double> actual) {
    double sum = 0.0;
    for (char c : Utility.ALPHABET.toCharArray()) {
      double expectedPerc = expected.get(c);
      double actualPerc = actual.get(c) * 100; // Convertir a porcentaje
      double diff = expectedPerc - actualPerc;
      sum += diff * diff;
    }
    return Math.sqrt(sum);
  }
}