def isTriangle(sides):
    smallest,medium,biggest = sorted(sides)
    return smallest+medium>=biggest and all(s>0 for s in sides)