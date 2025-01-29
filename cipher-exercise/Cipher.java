import java.util.Base64;
import java.security.SecureRandom;

public class Cipher {

    // ASCII a Binario
    public static String asciiToBinary(String asciiText) {
        StringBuilder binary = new StringBuilder();
        for (char character : asciiText.toCharArray()) {
            String binValue = String.format("%8s", Integer.toBinaryString(character))
                    .replace(' ', '0');
            binary.append(binValue).append(" ");
        }
        return binary.toString().trim();
    }

    // Base64 a Binario
    public static String base64ToBinary(String base64Text) {
        // Primero decodificamos Base64 a bytes
        byte[] decodedBytes = Base64.getDecoder().decode(base64Text);
        StringBuilder binary = new StringBuilder();

        // Convertimos cada byte a su representación binaria
        for (byte b : decodedBytes) {
            String binValue = String.format("%8s", Integer.toBinaryString(b & 0xFF))
                    .replace(' ', '0');
            binary.append(binValue).append(" ");
        }
        return binary.toString().trim();
    }

    // Binario a Base64
    public static String binaryToBase64(String binaryText) {
        // Removemos espacios y dividimos en grupos de 8 bits
        binaryText = binaryText.replaceAll("\\s+", "");
        byte[] bytes = new byte[binaryText.length() / 8];

        // Convertimos cada grupo de 8 bits a un byte
        for (int i = 0; i < bytes.length; i++) {
            String byteStr = binaryText.substring(i * 8, (i + 1) * 8);
            bytes[i] = (byte) Integer.parseInt(byteStr, 2);
        }

        // Codificamos los bytes a Base64
        return Base64.getEncoder().encodeToString(bytes);
    }

    // Binario a ASCII
    public static String binaryToAscii(String binaryText) {
        // Removemos espacios y dividimos en grupos de 8 bits
        binaryText = binaryText.replaceAll("\\s+", "");
        StringBuilder ascii = new StringBuilder();

        // Convertimos cada grupo de 8 bits a un carácter ASCII
        for (int i = 0; i < binaryText.length(); i += 8) {
            String byteStr = binaryText.substring(i, i + 8);
            int charCode = Integer.parseInt(byteStr, 2);
            ascii.append((char) charCode);
        }
        return ascii.toString();
    }

    // Base64 a ASCII (pasando por binario)
    public static String base64ToAscii(String base64Text) {
        // Convertimos Base64 a binario y luego a ASCII
        String binary = base64ToBinary(base64Text);
        return binaryToAscii(binary);
    }

    // Aplicar XOR a texto binario
    public static String applyXOR(String binaryText, String binaryKey) {
        // Removemos espacios
        binaryText = binaryText.replaceAll("\\s+", "");
        binaryKey = binaryKey.replaceAll("\\s+", "");

        StringBuilder result = new StringBuilder();

        // Aplicamos XOR bit a bit
        for (int i = 0; i < binaryText.length(); i++) {
            boolean bit1 = binaryText.charAt(i) == '1';
            boolean bit2 = binaryKey.charAt(i % binaryKey.length()) == '1';
            result.append(bit1 ^ bit2 ? "1" : "0");

            // Añadimos espacio cada 8 bits para legibilidad
            if ((i + 1) % 8 == 0 && i < binaryText.length() - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }

    // Generar llave dinámica en ASCII
    public static String generateDynamicKey(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder key = new StringBuilder();

        // Generamos caracteres ASCII imprimibles (32-126)
        for (int i = 0; i < length; i++) {
            int randomAscii = random.nextInt(95) + 32; // Rango de ASCII imprimibles
            key.append((char) randomAscii);
        }

        return key.toString();
    }

    // Cifrar texto con llave de tamaño fijo
    public static String encryptWithFixedKey(String text, String key) {
        // Convertimos texto y llave a binario
        String binaryText = asciiToBinary(text);
        String binaryKey = asciiToBinary(key);

        // Aplicamos XOR
        String encryptedBinary = applyXOR(binaryText, binaryKey);

        // Convertimos resultado a ASCII
        return binaryToAscii(encryptedBinary);
    }

    // Cifrar texto con llave de tamaño dinámico
    public static String encryptWithDynamicKey(String text) {
        // Generamos una llave del mismo tamaño que el texto
        String key = generateDynamicKey(text.length());

        // Usamos el método de cifrado con llave fija
        String encrypted = encryptWithFixedKey(text, key);

        // Retornamos tanto el texto cifrado como la llave utilizada
        return String.format("Encrypted: %s\nKey: %s", encrypted, key);
    }

    // Método para descifrar (útil para verificar el cifrado)
    public static String decrypt(String encryptedText, String key) {
        // El XOR es reversible, por lo que usamos el mismo método
        return encryptWithFixedKey(encryptedText, key);
    }
}