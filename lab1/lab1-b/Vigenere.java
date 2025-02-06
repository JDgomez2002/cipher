public class Vigenere {
  private final String key;

  public Vigenere(String key) {
    this.key = Utility.cleanText(key);
    if (this.key.isEmpty()) {
      throw new IllegalArgumentException("La clave no puede estar vacía");
    }
  }

  public String encrypt(String text) {
    text = Utility.cleanText(text);
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {
      char textChar = text.charAt(i);
      char keyChar = key.charAt(i % key.length());

      int textPos = Utility.ALPHABET.indexOf(textChar);
      int keyPos = Utility.ALPHABET.indexOf(keyChar);
      int newPos = Utility.mod(textPos + keyPos, Utility.M);

      result.append(Utility.ALPHABET.charAt(newPos));
    }

    return result.toString();
  }

  public String decrypt(String text) {
    text = Utility.cleanText(text);
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {
      char textChar = text.charAt(i);
      char keyChar = key.charAt(i % key.length());

      int textPos = Utility.ALPHABET.indexOf(textChar);
      int keyPos = Utility.ALPHABET.indexOf(keyChar);
      int newPos = Utility.mod(textPos - keyPos, Utility.M);

      result.append(Utility.ALPHABET.charAt(newPos));
    }

    return result.toString();
  }
}