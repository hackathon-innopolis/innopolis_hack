from PIL import Image
# from smth import coordinate


def main(coordinate):
    img = Image.open('full_map.png')
    area = (coordinate[0] - 350, coordinate[1] - 350, coordinate[0] + 350, coordinate[1] + 350)
    new_img = img.crop(area)
    return new_img


coordinate = (4500, 5350)
main(coordinate).show()
