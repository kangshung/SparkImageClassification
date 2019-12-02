from PIL import Image

v = list()
pic = Image.open("/photos/too_dark/h.jpg").convert("HSV")
for i in range(pic.size[0]):
    for j in range(pic.size[1]):
        v.append(pic.getpixel((i, j))[2])

output = sum(v) / (pic.size[0] * pic.size[1])
