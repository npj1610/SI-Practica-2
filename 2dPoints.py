from graphics import GraphWin
from graphics import Point as oldPoint
from graphics import Rectangle
from graphics import color_rgb
from graphics import Image

import math
# Estoy usando puntos como vectores, me siento sucia
class Point(oldPoint):
    def __add__(self, other):
        return Point(self.getX() + other.getX(), self.getY() + other.getY())

    def __sub__(self, other):
        return Point(self.getX() - other.getX(), self.getY() - other.getY())

    def __mul__(self, other):
        return Point(self.getX() * other, self.getY() * other)

    def __truediv__(self, other):
        return Point(self.getX() / other, self.getY() / other)

    def __mod__(self, other):
        return Point(self.getX() % other, self.getY() % other)

    def distance(self, other):
        return math.sqrt(math.pow(self.getX() - other.getX(), 2) + math.pow(self.getY() - other.getY(), 2))

    def length(self):
        return self.distance(Point(0, 0))

    def unitary(self):
        return self / self.length()

def main():
    win = GraphWin(width=582, height=552, title="Adaboost 2d point editor")  # create a window
    point_dict = {}
    block_size = 2
    border_size = 4
    class_names = ["circle.gif", "cross.gif", "triangle.gif"]
    actual_class = 0
    images_start = Point(552, 20)
    image_size = Point(0, 9)
    image_border = Point(0, 5)
    class_images = []
    for class_i in range(len(class_names)):
        class_images.append(Image(images_start+(image_size+image_border)*class_i, class_names[class_i]))
        class_images[class_i].draw(win)

    graph_start = Point(20, 20)
    graph_size = Point(256*block_size, 256*block_size)
    graphBG_color = color_rgb(255, 255, 255)
    graphOL_color = color_rgb(0, 0, 0)
    graph = Rectangle(graph_start, graph_start + graph_size)
    graph.setFill(graphBG_color)
    graph.setOutline(graphOL_color)

    win.getMouse()

    graph.draw(win)


    while True:
        pos = win.getMouse()
        if graph_start.getX()+border_size < pos.getX() < (graph_start+graph_size).getX()-border_size:
            if graph_start.getY()+border_size < pos.getY() < (graph_start+graph_size).getY()-border_size:
                adaptedPos = Point(pos.getX(), pos.getY())
                adaptedPos = adaptedPos - (adaptedPos % 5)
                if str(adaptedPos) in point_dict:
                    point_dict[str(adaptedPos)][0].undraw()
                    del point_dict[str(adaptedPos)]
                else:
                    img = Image(adaptedPos, class_names[actual_class])
                    point_dict[str(adaptedPos)] = [img, actual_class]
                    img.draw(win)
        else:
            if images_start.getX() - image_border.length()/2 < pos.getX() < (images_start).getX() + image_size.length() + image_border.length()/2:
                for class_i in range(len(class_names)):
                    if (images_start + (image_size + image_border) * class_i).getY() - image_border.length()/2< pos.getY():
                        if pos.getY() < (images_start + (image_size + image_border) * class_i).getY() + image_size.length() + image_border.length()/2:
                            actual_class = class_i
main()
