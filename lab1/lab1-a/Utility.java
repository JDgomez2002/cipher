public class Utility {
    public static final String ALPHABET = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    public static final int M = ALPHABET.length(); // 27

    public static String cleanText(String text) {
        return text.toUpperCase()
                .replaceAll("[^A-ZÑ]", "");
    }

    public static int mod(int a, int m) {
        int result = a % m;
        return result < 0 ? result + m : result;
    }

    // Encuentra el inverso multiplicativo modular
    public static int modInverse(int a, int m) {
        for (int x = 1; x < m; x++) {
            if (((a % m) * (x % m)) % m == 1) {
                return x;
            }
        }
        throw new RuntimeException("El inverso multiplicativo no existe.");
    }
}