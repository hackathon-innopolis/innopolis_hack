from PIL import Image


# from smth import coordinate


def convert(coordniate):
    coordinate[0] = int(((coordniate[0] - 56.8) / 0.1 * 8506) % 1)
    coordinate[1] = int(((coordniate[1] - 60.5) / 0.2 * 9322) % 1)
    return coordniate


def main(coordinate):
    coordinate = convert(coordinate)
    img = Image.open('full_map.png')
    k = 350
    area = (coordinate[0] - k, coordinate[1] - k, coordinate[0] + k, coordinate[1] + k)
    new_img = img.crop(area)
    return new_img


coordinate = [4500, 5350]
main(coordinate).show()
