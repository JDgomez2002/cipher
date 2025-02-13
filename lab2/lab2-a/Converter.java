public class Converter {
  /**
   * Convierte una cadena de texto a su representación en bits
   * @param text La cadena de texto a convertir
   * @return String con la representación en bits
   */
  public static String charToBits(String text) {
    if (text == null || text.isEmpty()) {
      return "";
    }

    StringBuilder bits = new StringBuilder();

    for (char character : text.toCharArray()) {
      // Convertir cada carácter a su representación en 8 bits
      String binaryChar = String.format("%8s",
                      Integer.toBinaryString(character))
              .replace(' ', '0');
      bits.append(binaryChar);
    }

    return bits.toString();
  }

  /**
   * Convierte una cadena de bits a texto
   * @param bits La cadena de bits a convertir (debe ser múltiplo de 8)
   * @return String con el texto correspondiente
   */
  public static String bitsToChar(String bits) {
    if (bits == null || bits.isEmpty() || bits.length() % 8 != 0) {
      return "";
    }

    StringBuilder text = new StringBuilder();

    // Procesar los bits en grupos de 8
    for (int i = 0; i < bits.length(); i += 8) {
      // Obtener el grupo de 8 bits
      String byte_str = bits.substring(i, i + 8);
      // Convertir los 8 bits a su valor decimal y luego a carácter
      char c = (char) Integer.parseInt(byte_str, 2);
      text.append(c);
    }

    return text.toString();
  }

  private static final String BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  private static final char PADDING = '=';

  public static String get_base64_chars() {
    return BASE64_CHARS;
  }

  /**
   * Convierte una cadena de caracteres a Base64, utilizando la conversión manual (texto a binario, binario a codigo UNICODE).
   * @param chars La cadena de caracteres.
   * @return String en Base64
   */
  public static String charsToBase64(String chars) {

    if (chars == null || chars.isEmpty()) {
      return "";
    }

    // Paso 1: Convertir la cadena a bits
    String binary = charToBits(chars);

    // Paso 2: Asegurar que la longitud del binario sea múltiplo de 6
    // añadiendo ceros al final si es necesario
    while (binary.length() % 6 != 0) {
      binary += "0";
    }

    StringBuilder base64 = new StringBuilder();

    // Paso 3: Convertir grupos de 6 bits a caracteres Base64
    for (int i = 0; i < binary.length(); i += 6) {
      String sixBits = binary.substring(i, i + 6);
      // Convertir los 6 bits a su valor decimal
      int index = Integer.parseInt(sixBits, 2);
      // Obtener el carácter Base64 correspondiente
      base64.append(BASE64_CHARS.charAt(index));
    }

    // Paso 4: Añadir padding si es necesario
    while (base64.length() % 4 != 0) {
      base64.append(PADDING);
    }

    return base64.toString();
  }

  /**
   * Convierte una cadena Base64 a texto
   * @param base64 La cadena en Base64 a convertir
   * @return String con el texto original
   */
  public static String base64ToChars(String base64) {
    if (base64 == null || base64.isEmpty()) {
      return "";
    }

    // Paso 1: Remover el padding
    base64 = base64.replaceAll("=", "");

    StringBuilder binary = new StringBuilder();

    // Paso 2: Convertir cada carácter Base64 a 6 bits
    for (char c : base64.toCharArray()) {
      int value = BASE64_CHARS.indexOf(c);
      if (value == -1) {
        continue; // Ignorar caracteres que no son Base64
      }
      // Convertir el valor a 6 bits
      String bits = String.format("%6s",
                      Integer.toBinaryString(value))
              .replace(' ', '0');
      binary.append(bits);
    }

    // Paso 3: Ajustar la longitud del binario para que sea múltiplo de 8
    while (binary.length() % 8 != 0) {
      binary.setLength(binary.length() - 1);
    }

    // Paso 4: Convertir los bits a caracteres
    return bitsToChar(binary.toString());
  }

  /**
   * Realiza la operación XOR bit a bit entre dos cadenas de texto
   * @param texto Cadena de texto principal
   * @param llave Llave para realizar XOR (se repetirá si es necesario)
   * @return Resultado de la operación XOR como texto
   */
  public static String xorStrings(String texto, String llave) {
    if (texto == null || texto.isEmpty() || llave == null || llave.isEmpty()) {
      return "";
    }

    // Paso 1: Convertir ambas cadenas a bits
    String bitsTexto = charToBits(texto);
    String bitsLlave = charToBits(llave);

    // Paso 2: Si la llave es más corta, repetirla hasta tener la longitud necesaria
    StringBuilder llaveCompleta = new StringBuilder(bitsLlave);
    while (llaveCompleta.length() < bitsTexto.length()) {
      llaveCompleta.append(bitsLlave);
    }

    // Truncar si es más larga que el texto
    if (llaveCompleta.length() > bitsTexto.length()) {
      llaveCompleta.setLength(bitsTexto.length());
    }

    // Paso 3: Realizar operación XOR bit a bit
    StringBuilder resultado = new StringBuilder();
    for (int i = 0; i < bitsTexto.length(); i++) {
      // XOR: 1 si los bits son diferentes, 0 si son iguales
      char bitResultado = (bitsTexto.charAt(i) == llaveCompleta.charAt(i)) ? '0' : '1';
      resultado.append(bitResultado);
    }

    // Paso 4: Convertir el resultado en bits de vuelta a texto
    return bitsToChar(resultado.toString());
  }
}
