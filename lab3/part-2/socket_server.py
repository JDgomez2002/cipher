import socket

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(("0.0.0.0", 8080))
server.listen(1)
print(f"Listening on port 8080")

while True:
    conn, addr = server.accept()
    print(f"Connection from {addr}")
    while True:
        data = conn.recv(1024)
        if not data:
            break
        print(f"Received: {data.hex()}")
    conn.close()