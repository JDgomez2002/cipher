
def decrypt_xor_image(path: str, key: bytes, output: str):
  with open(path, 'rb') as f:
    encrypted = f.read()

  decrypted = bytearray()
  for i, byte in enumerate(encrypted):
    # la llave debe de ser menor o igual tamano
    decrypted.append(byte ^ key[i % len(key)])

  with open(output, 'wb') as f:
    f.write(decrypted)

decrypt_xor_image(
  'imagen_xor.png',
  b'cifrados_2025',
  'original_image.png'
)
