import socket
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad
import os

key = b'0123456789abcdef'  # Clave de 16 bytes

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(("127.0.0.1", 8080))

try:
    while True:
        mensaje = input("Ingrese el mensaje a enviar (o 'exit' para salir): ").encode()
        if mensaje == b'exit':
            break

        iv = os.urandom(16)  # IV aleatorio
        cipher = AES.new(key, AES.MODE_CBC, iv)
        mensaje_cifrado = cipher.encrypt(pad(mensaje, AES.block_size))

        # Enviar el IV y el mensaje cifrado por TCP
        sock.sendall(iv + mensaje_cifrado)
finally:
    sock.close()