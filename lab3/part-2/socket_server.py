import socket
from Crypto.Cipher import AES
from Crypto.Util.Padding import unpad
import os


def receive_exactly(conn, n):
  """Recibe exactamente n bytes de la conexión."""
  data = b''
  while len(data) < n:
    packet = conn.recv(n - len(data))
    if not packet:
      return None
    data += packet
  return data


def save_file(file_name, data, output_dir="received_files"):
  """Guarda los datos en un archivo."""
  if not os.path.exists(output_dir):
    os.makedirs(output_dir)

  file_path = os.path.join(output_dir, file_name)
  with open(file_path, 'wb') as f:
    f.write(data)
  return file_path

key = b'0123456789abcdef'  # Clave de 16 bytes

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(("0.0.0.0", 8080))
server.listen(1)
print(f"Escuchando en puerto 8080")

while True:
    conn, addr = server.accept()
    print(f"Conexión desde {addr}")

    try:
      while True:
        # Recibir el tipo de datos (0: mensaje, 1: archivo)
        data_type = receive_exactly(conn, 1)
        if not data_type:
          print("Conexión cerrada por el cliente.")
          break

        if data_type == b'\x00':  # Mensaje de texto
          # Recibir IV (16 bytes)
          iv = receive_exactly(conn, 16)
          if not iv:
            break

          # Recibir mensaje cifrado
          encrypted_msg = receive_exactly(conn, 1024)  # Asumimos que el mensaje no excede 1024 bytes
          if not encrypted_msg:
            break

          # Descifrar mensaje
          cipher = AES.new(key, AES.MODE_CBC, iv)
          try:
            decrypted_msg = unpad(cipher.decrypt(encrypted_msg), AES.block_size)
            print(f"Mensaje recibido: {decrypted_msg.decode('utf-8')}")
          except Exception as e:
            print(f"Error al descifrar el mensaje: {e}")

        elif data_type == b'\x01':  # Archivo
          # Recibir IV (16 bytes)
          iv = receive_exactly(conn, 16)
          if not iv:
            break

          # Recibir longitud del nombre del archivo (4 bytes)
          name_length_bytes = receive_exactly(conn, 4)
          if not name_length_bytes:
            break
          name_length = int.from_bytes(name_length_bytes, byteorder='big')

          # Recibir nombre del archivo
          file_name_bytes = receive_exactly(conn, name_length)
          if not file_name_bytes:
            break
          file_name = file_name_bytes.decode('utf-8')

          # Recibir tamaño del contenido cifrado (8 bytes)
          content_size_bytes = receive_exactly(conn, 8)
          if not content_size_bytes:
            break
          content_size = int.from_bytes(content_size_bytes, byteorder='big')

          # Recibir contenido cifrado
          encrypted_content = receive_exactly(conn, content_size)
          if not encrypted_content:
            break

          # Descifrar contenido
          cipher = AES.new(key, AES.MODE_CBC, iv)
          try:
            decrypted_content = unpad(cipher.decrypt(encrypted_content), AES.block_size)

            # Guardar archivo
            saved_path = save_file(file_name, decrypted_content)
            print(f"Archivo recibido y guardado en: {saved_path}")
          except Exception as e:
            print(f"Error al descifrar el archivo: {e}")

        else:
          print(f"Tipo de datos desconocido: {data_type.hex()}")
    finally:
      conn.close()
      print(f"Conexión con {addr} cerrada.")
