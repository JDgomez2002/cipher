import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class VigenereBruteforce {
  private static final int MAX_KEY_LENGTH = 15; // Aumentamos el máximo
  private static final int MAX_RESULTS = 5;

  public static void main(String[] args) {
    try {
      String ciphertext = new String(Files.readAllBytes(Paths.get("Cifrados/vigenere.txt")));

      // Encontrar la longitud más probable de la clave usando Kasiski
      List<Integer> probableKeyLengths = findKeyLengthKasiski(ciphertext);
      System.out.println("Longitudes de clave más probables: " + probableKeyLengths);

      List<KeyMetric> bestKeys = new ArrayList<>();
      for (int keyLength : probableKeyLengths) {
        List<KeyMetric> keysForLength = findKeysForLength(ciphertext, keyLength);
        bestKeys.addAll(keysForLength);
      }

      // Ordenar y mostrar los mejores resultados
      Collections.sort(bestKeys);
      int numResults = Math.min(MAX_RESULTS, bestKeys.size());
      for (int i = 0; i < numResults; i++) {
        KeyMetric keyMetric = bestKeys.get(i);
        Vigenere vigenere = new Vigenere(keyMetric.key);
        String decrypted = vigenere.decrypt(ciphertext);

        System.out.printf("\n%d. Clave: %s (Distancia: %.4f)\n",
                i + 1, keyMetric.key, keyMetric.distance);
        System.out.println("Primeros 200 caracteres del texto descifrado:");
        System.out.println(decrypted.substring(0, Math.min(200, decrypted.length())));
        System.out.println("-".repeat(80));
      }

    } catch (IOException e) {
      System.err.println("Error al leer el archivo: " + e.getMessage());
    }
  }

  static class KeyMetric implements Comparable<KeyMetric> {
    String key;
    double distance;

    KeyMetric(String key, double distance) {
      this.key = key;
      this.distance = distance;
    }

    @Override
    public int compareTo(KeyMetric other) {
      return Double.compare(this.distance, other.distance);
    }
  }

  private static List<Integer> findKeyLengthKasiski(String text) {
    Map<String, List<Integer>> positions = new HashMap<>();
    List<Integer> distances = new ArrayList<>();

    // Buscar secuencias repetidas de 3 caracteres
    for (int i = 0; i < text.length() - 3; i++) {
      String trigram = text.substring(i, i + 3);
      positions.computeIfAbsent(trigram, k -> new ArrayList<>()).add(i);
    }

    // Calcular distancias entre repeticiones
    for (List<Integer> pos : positions.values()) {
      if (pos.size() > 1) {
        for (int i = 0; i < pos.size() - 1; i++) {
          distances.add(pos.get(i + 1) - pos.get(i));
        }
      }
    }

    // Encontrar factores comunes
    Map<Integer, Integer> factorCount = new HashMap<>();
    for (int distance : distances) {
      for (int i = 2; i <= MAX_KEY_LENGTH; i++) {
        if (distance % i == 0) {
          factorCount.merge(i, 1, Integer::sum);
        }
      }
    }

    return factorCount.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(3)
            .map(Map.Entry::getKey)
            .toList();
  }

  private static List<KeyMetric> findKeysForLength(String ciphertext, int keyLength) {
    List<String> columns = new ArrayList<>();
    // Dividir el texto en columnas
    for (int i = 0; i < keyLength; i++) {
      StringBuilder column = new StringBuilder();
      for (int j = i; j < ciphertext.length(); j += keyLength) {
        column.append(ciphertext.charAt(j));
      }
      columns.add(column.toString());
    }

    // Para cada columna, encontrar la mejor letra usando análisis de frecuencia
    char[] keyChars = new char[keyLength];
    for (int i = 0; i < keyLength; i++) {
      String column = columns.get(i);
      keyChars[i] = findBestChar(column);
    }

    // Crear y evaluar la clave
    String key = new String(keyChars);
    Vigenere vigenere = new Vigenere(key);
    String decrypted = vigenere.decrypt(ciphertext);

    // Evaluar la calidad del descifrado
    double quality = evaluateText(decrypted);

    return List.of(new KeyMetric(key, quality));
  }

  private static char findBestChar(String text) {
    Map<Character, Double> expectedFreq = getExpectedFrequencies();
    char bestChar = 'A';
    double bestScore = Double.MAX_VALUE;

    for (char shift : Utility.ALPHABET.toCharArray()) {
      Caesar caesar = new Caesar(Utility.ALPHABET.indexOf(shift));
      String decrypted = caesar.decrypt(text);

      Frequency freq = new Frequency();
      freq.analyzeText(decrypted);
      double score = calculateFrequencyScore(expectedFreq, freq.getFrequencies());

      if (score < bestScore) {
        bestScore = score;
        bestChar = shift;
      }
    }

    return bestChar;
  }

  private static double calculateFrequencyScore(Map<Character, Double> expected,
                                                Map<Character, Double> actual) {
    double score = 0.0;
    for (char c : Utility.ALPHABET.toCharArray()) {
      double expectedPerc = expected.get(c);
      double actualPerc = actual.get(c) * 100;
      score += Math.pow(expectedPerc - actualPerc, 2);
    }
    return Math.sqrt(score);
  }

  private static double evaluateText(String text) {
    // Evaluar la calidad del texto usando n-gramas comunes en español
    String[] commonBigrams = {"ES", "EN", "EL", "LA", "DE", "QU", "AR"};
    String[] commonTrigrams = {"QUE", "EST", "CON", "ENT", "IEN"};

    double bigramScore = 0;
    double trigramScore = 0;

    // Contar bigramas
    for (String bigram : commonBigrams) {
      int count = countOccurrences(text, bigram);
      bigramScore += count;
    }

    // Contar trigramas
    for (String trigram : commonTrigrams) {
      int count = countOccurrences(text, trigram);
      trigramScore += count * 2; // Dar más peso a los trigramas
    }

    // Combinar con análisis de frecuencia
    Frequency freq = new Frequency();
    freq.analyzeText(text);
    double freqScore = calculateFrequencyScore(getExpectedFrequencies(), freq.getFrequencies());

    return freqScore / (bigramScore + trigramScore + 1);
  }

  private static int countOccurrences(String text, String pattern) {
    int count = 0;
    int pos = 0;
    while ((pos = text.indexOf(pattern, pos)) != -1) {
      count++;
      pos++;
    }
    return count;
  }

  private static Map<Character, Double> getExpectedFrequencies() {
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
    return expectedFreq;
  }
}