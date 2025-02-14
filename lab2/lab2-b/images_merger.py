from PIL import Image

def load_images(path: str, key: str):
  # cargar imagenes, conviertiendolas al formato de color RGB para el XOR
  original = Image.open(path).convert('RGB')
  key = Image.open(key).convert('RGB')

  key = key.resize(original.size, Image.Resampling.LANCZOS)

  return original, key

def merge_images(img1: Image, img2: Image):
  # realizar el XOR entre los bytes de ambas imagenes
  if img1.size != img2.size:
    raise ValueError("Las imagenes no son del mismo tamano!")

  img_bytes_1 = img1.tobytes()
  img_bytes_2 = img2.tobytes()
  xor_image_bytes = bytes(a ^ b for a, b in zip(img_bytes_1, img_bytes_2))

  return Image.frombytes(mode="RGB", size=img1.size, data=xor_image_bytes)

try:
  original, key = load_images(
    "lambo.jpg",
    "corvette.jpg"
  )
  result = merge_images(original, key)

  result.save("merged_images.png")

except Exception as e:
  print(f"Error: {e}")
