public class Caesar {
    private final int shift;

    public Caesar(int shift) {
        this.shift = shift;
    }

    public String encrypt(String text) {
        text = Utility.cleanText(text);
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            int pos = Utility.ALPHABET.indexOf(c);
            int newPos = Utility.mod(pos + shift, Utility.M);
            result.append(Utility.ALPHABET.charAt(newPos));
        }

        return result.toString();
    }

    public String decrypt(String text) {
        text = Utility.cleanText(text);
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            int pos = Utility.ALPHABET.indexOf(c);
            int newPos = Utility.mod(pos - shift, Utility.M);
            result.append(Utility.ALPHABET.charAt(newPos));
        }

        return result.toString();
    }
}
