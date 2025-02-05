public class Afin {
    private final int a;
    private final int b;

    public Afin(int a, int b) {
        // Asegurarse de que a sea positivo y menor que M
        a = Utility.mod(a, Utility.M);

        // Verificar que a y M sean coprimos
        if (a == 0 || gcd(a, Utility.M) != 1) {
            throw new IllegalArgumentException("'a' debe ser coprimo con " + Utility.M +
                    " (los valores válidos son: 1,2,4,5,7,8,10,11,13,14,16,17,19,20,22,23,25,26)");
        }
        this.a = a;
        this.b = Utility.mod(b, Utility.M);
    }

    private int gcd(int a, int b) {
        if (b == 0) return Math.abs(a);
        return gcd(b, a % b);
    }

    public String encrypt(String text) {
        text = Utility.cleanText(text);
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            int pos = Utility.ALPHABET.indexOf(c);
            int newPos = Utility.mod(a * pos + b, Utility.M);
            result.append(Utility.ALPHABET.charAt(newPos));
        }

        return result.toString();
    }

    public String decrypt(String text) {
        text = Utility.cleanText(text);
        StringBuilder result = new StringBuilder();
        int aInverse = Utility.modInverse(a, Utility.M);

        for (char c : text.toCharArray()) {
            int pos = Utility.ALPHABET.indexOf(c);
            int newPos = Utility.mod(aInverse * (pos - b), Utility.M);
            result.append(Utility.ALPHABET.charAt(newPos));
        }

        return result.toString();
    }
}