import socket
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad
import os

key = b'0123456789abcdef'

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(("172.20.10.2", 8080))

try:
    file_path = input("Ingrese la ruta del archivo a enviar: ")
    with open(file_path, 'rb') as f:
        file_data = f.read()

    iv = os.urandom(16)  # IV aleatorio
    cipher = AES.new(key, AES.MODE_CBC, iv)
    encrypted_data = cipher.encrypt(pad(file_data, AES.block_size))

    # Enviar el IV y el archivo cifrado
    sock.sendall(iv + encrypted_data)
finally:
    sock.close()