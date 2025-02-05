public class Main {
    public static void main(String[] args) {
        // cifrado caesar
        Caesar caesar = new Caesar(3);
        String textoCesar = "HOLA MUNDO";
        String cifradoCesar = caesar.encrypt(textoCesar);
        String descifradoCesar = caesar.decrypt(cifradoCesar);
        System.out.println("César - Original: " + textoCesar);
        System.out.println("César - Cifrado: " + cifradoCesar);
        System.out.println("César - Descifrado: " + descifradoCesar);

        // cifrado afin
        try {
            Afin afin = new Afin(5, 8);
            String textoAfin = "ATACAR AL AMACECER";
            String cifradoAfin = afin.encrypt(textoAfin);
            String descifradoAfin = afin.decrypt(cifradoAfin);
            System.out.println("\nAfín - Original: " + textoAfin);
            System.out.println("Afín - Cifrado: " + cifradoAfin);
            System.out.println("Afín - Descifrado: " + descifradoAfin);
        } catch (IllegalArgumentException e) {
            System.out.println("\nError en cifrado Afín: " + e.getMessage());
        }

        // cifrado vigenere
        Vigenere vigenere = new Vigenere("CLAVE");
        String textoVigenere = "MENSAJE SECRETO";
        String cifradoVigenere = vigenere.encrypt(textoVigenere);
        String descifradoVigenere = vigenere.decrypt(cifradoVigenere);
        System.out.println("\nVigenère - Original: " + textoVigenere);
        System.out.println("Vigenère - Cifrado: " + cifradoVigenere);
        System.out.println("Vigenère - Descifrado: " + descifradoVigenere);

//        // analisis de frecuencia
//        String textoParaAnalizar = "EL QUIJOTE DE LA MANCHA ES UNA OBRA LITERARIA DE MIGUEL DE CERVANTES";
//        System.out.println("\nTexto original para análisis:");
//        System.out.println(textoParaAnalizar);
//
//        Frequency freq = new Frequency();
//        freq.analyzeText(textoParaAnalizar);
//        freq.printAnalysis();
//        freq.printHistogram();
//
//        Caesar caesar2 = new Caesar(3);
//        String cifrado = caesar2.encrypt(textoParaAnalizar);
//        System.out.println("\nAnálisis del texto cifrado:");
//        System.out.println("Texto cifrado: " + cifrado);
//
//        Frequency freqCifrado = new Frequency();
//        freqCifrado.analyzeText(cifrado);
//        freqCifrado.printAnalysis();
//        freqCifrado.printHistogram();

        // comparacion de analisis de frecuencia

        // Texto de ejemplo
        String texto = "EL QUIJOTE DE LA MANCHA ES UNA OBRA LITERARIA DE MIGUEL DE CERVANTES";

        // Análisis de frecuencia
        Frequency freq = new Frequency();
        freq.analyzeText(texto);
        freq.printAnalysis();

        // Comparación con distribución teórica
        Graph graph = new Graph();
        graph.compareDistributions(freq.getFrequencies());

        // Ejemplo con texto cifrado
        Caesar caesar3 = new Caesar(3);
        String textoCifrado = caesar3.encrypt(texto);

        Frequency freqCifrado = new Frequency();
        freqCifrado.analyzeText(textoCifrado);
        System.out.println("\n=== Análisis del texto cifrado ===");
        freqCifrado.printAnalysis();
        graph.compareDistributions(freqCifrado.getFrequencies());

    }
}