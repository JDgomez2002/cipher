# Cifrado de Flujo con Keystream Pseudoaleatorio
#### José Daniel Gómez Cabrera 21429
Este laboratorio implementa un cifrado de flujo utilizando un keystream pseudoaleatorio generado a partir de una semilla (seed). Las funcionalidades están implementadas en Python y permiten cifrar y descifrar mensajes utilizando la operación XOR con un keystream generado.

## Funcionalidades
1. **Generación de Keystream**: Genera un keystream pseudoaleatorio basado en una semilla (seed) y una longitud específica.
2. **Cifrado**: Cifra un mensaje en texto plano utilizando la operación XOR con el keystream generado.
3. **Descifrado**: Descifra un mensaje cifrado utilizando la misma operación XOR con el keystream.

## Cómo Ejecutar el Código
1. Clona o descarga el repositorio.
2. Abre el archivo `stream_cipher.ipynb` en un entorno de Jupyter Notebook.
3. Ejecuta las celdas en orden para generar el keystream, cifrar y descifrar mensajes.

### Ejemplos de Uso
```python
# Generar keystream
seed = 12345
longitud_mensaje = 10
keystream = generar_keystream(seed, longitud_mensaje)
print("Keystream:", keystream)

# Cifrar mensaje
mensaje = b"Stream cipher!!!"
mensaje_cifrado = cifrar_xor(mensaje, keystream)
print("Mensaje cifrado:", mensaje_cifrado)

# Descifrar mensaje
mensaje_descifrado = descifrar_xor(mensaje_cifrado, keystream)
print("Mensaje descifrado:", mensaje_descifrado.decode())
```

### Preguntas y Respuestas
1. ¿Qué sucede cuando cambias la clave utilizada para generar el keystream?

   - Al cambiar la clave, se genera un nuevo keystream, lo que resulta en un mensaje cifrado diferente.

2. ¿Qué riesgos de seguridad existen si reutilizas el mismo keystream para cifrar dos mensajes diferentes?

   - Reutilizar el keystream puede permitir que un atacante deduzca información sobre los mensajes originales mediante un ataque de XOR.

3. ¿Cómo afecta la longitud del keystream a la seguridad del cifrado?

    - La longitud del keystream debe ser al menos igual a la del mensaje para garantizar que cada byte del mensaje esté cifrado de manera única.

4. ¿Qué consideraciones debes tener al generar un keystream en un entorno real?

    - Es crucial utilizar generadores de números pseudoaleatorios criptográficamente seguros y proteger la clave utilizada para inicializar el PRNG.

### Contrbuciones

¡Las contribuciones son bienvenidas! Si tienes ideas para mejorar este laboratorio o agregar nuevas funcionalidades, no dudes en enviar un pull request.

### Licencia

Este laboratorio es de uso libre para fines educativos y personales. Por favor, da el crédito correspondiente si utilizas este código en tus proyectos u ejercicios.
