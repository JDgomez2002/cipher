public class Main {
    public static void main(String[] args) {
        // En el main
        String text = "Hello World!";
        String key = "SecretKey";

        // Cifrado con llave fija
        String encrypted = Cipher.encryptWithFixedKey(text, key);
        System.out.println("Encrypted: " + encrypted);

        // Descifrado
        String decrypted = Cipher.decrypt(encrypted, key);
        System.out.println("Decrypted: " + decrypted);

        // Cifrado con llave dinámica
        String dynamicResult = Cipher.encryptWithDynamicKey(text);
        System.out.println(dynamicResult);
    }
}
