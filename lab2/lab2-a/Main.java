public class Main {
  public static void main(String[] args) {
    // Ejemplo 1: XOR con llave más corta
    String texto1 = "Hello";
    String llave1 = "Key";
    String xor1 = Converter.xorStrings(texto1, llave1);

    System.out.println("Ejemplo 1: XOR con llave más corta");
    System.out.println("Texto: " + texto1);
    System.out.println("Llave: " + llave1);
    System.out.println("Resultado XOR: " + xor1);

    // Mostrar representación en bits
    System.out.println("Bits del texto: " + Converter.charToBits(texto1));
    String llaveRepetida = llave1;
    while (llaveRepetida.length() < texto1.length()) {
      llaveRepetida += llave1;
    }
    llaveRepetida = llaveRepetida.substring(0, texto1.length());
    System.out.println("Bits de la llave (repetida): " + Converter.charToBits(llaveRepetida));
    System.out.println("Bits del resultado: " + Converter.charToBits(xor1));

    System.out.println("\n-------------------\n");

    // Ejemplo 2: XOR y verificación (desencriptación)
    String texto2 = "Confidential";
    String llave2 = "Secret";
    String xor2 = Converter.xorStrings(texto2, llave2);

    System.out.println("Ejemplo 2: XOR y verificación");
    System.out.println("Texto original: " + texto2);
    System.out.println("Llave: " + llave2);
    System.out.println("Texto cifrado (XOR): " + xor2);

    // Verificar que se puede recuperar el texto original
    String recuperado = Converter.xorStrings(xor2, llave2);
    System.out.println("Texto recuperado: " + recuperado);

    // Verificar que coincida con el original
    System.out.println("¿Coincide con el original? " + texto2.equals(recuperado));
  }
}