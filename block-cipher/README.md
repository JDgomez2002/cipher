# Ejercicio Block Cipher
## Cifrado de Información
### José Daniel Gómez Cabrera 21429
#### Universidad del Valle de Guatemala
#### Ingenierio Ludwing Cano

### Preguntas a Responder

1. ¿Qué tamaño de clave se está usando para DES, 3DES y AES?
   - DES: 8 bytes (64 bits, aunque efectivamente 56 bits)
   - 3DES: 24 bytes (192 bits, aunque efectivamente 168 bits)
   - AES: 32 bytes (256 bits)


2. ¿Qué modo de operación está implementado?
   - DES: Electronic Codebook (ECB)
   - 3DES: Cipher Block Chaining (CBC)
   - AES: Ambos, ECB y CBC


3. ¿Por qué no debemos usar ECB en datos sensibles?
   - ECB cifra bloques idénticos de texto plano en bloques idénticos de texto cifrado.
   - Esto puede revelar patrones en los datos, comprometiendo la seguridad.
   - Es especialmente problemático en imágenes, donde los patrones pueden ser visibles.


4. ¿Cual es la diferencia entre ECB vs CBC, se puede notar directamente en una imagen?
   - ECB: cada bloque se cifra independientemente.
   - CBC: cada bloque se combina (XOR) con el bloque cifrado anterior antes de cifrarse.
   - En imágenes, ECB puede revelar patrones mientras que CBC no.


5. ¿Que es el IV?
   - IV (Vector de Inicialización): valor aleatorio usado para asegurar que textos planos idénticos cifrados con la misma clave produzcan textos cifrados diferentes.
   - Debe ser aleatorio y único para cada operación de cifrado.


6. ¿Que es el PADDING?
   - Técnica para completar el último bloque de datos al tamaño requerido por el algoritmo.
   - PKCS#7 añade bytes con el valor igual al número de bytes añadidos.


7. ¿En qué situaciones se recomienda cada modo de operación?
   - ECB: Solo para datos pequeños y aleatorios sin patrones.
   - CBC: Datos sensibles donde la seguridad es crítica.
   - CTR: Cifrado de stream para datos grandes o en tiempo real.
   - GCM: Cuando se necesita autenticación además de confidencialidad.


8. ¿Cómo elegir un modo seguro en cada lenguaje de programación?
   - Usar bibliotecas criptográficas actualizadas y mantenidas.
   - Preferir modos que incluyan IV (CBC, CTR, GCM) sobre ECB.
   - Seguir las recomendaciones de NIST y otros estándares de seguridad.
   - En Python, pycryptodome ofrece implementaciones seguras de varios modos.