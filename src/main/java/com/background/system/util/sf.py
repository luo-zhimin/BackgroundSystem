from PIL import Image
def changeDpiPic (url, path) :
    pic = Image.open(url)
    pic.save(path + "/changed.jpg", dpi=(300,300))
