from libs.graphics import GraphWin
from libs.graphics import Point as oldPoint
from libs.graphics import Polygon
from libs.graphics import Rectangle
from libs.graphics import color_rgb
from libs.graphics import Image
from libs.graphics import GraphicsError

import math

import json


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

    def __floordiv__(self, other):
        return Point(self.getX() // other, self.getY() // other)

    def __mod__(self, other):
        return Point(self.getX() % other, self.getY() % other)

    def distance(self, other):
        return math.sqrt(math.pow(self.getX() - other.getX(), 2) + math.pow(self.getY() - other.getY(), 2))

    def length(self):
        return self.distance(Point(0, 0))

    def unitary(self):
        return self / self.length()

    def __getitem__(self, key):
        if key == 0:
            return self.x
        if key == 1:
            return self.y
        raise IndexError()

class AdaboostProblem:
    def __init__(self):
        # problem parameters
        try:
            with open("params.txt", "r") as param_file:
                self.params = json.load(param_file)
        except (json.decoder.JSONDecodeError, FileNotFoundError):
            self.params = dict()
            self.params["dimensions"] = 2
            self.params["class_colors"] = ["red", "blue", "green"]
            self.params["class_names"] = ["circle", "cross", "triangle"]
            self.params["img_files"] = ["circle.gif", "cross.gif", "triangle.gif"]
        # problem data
        try:
            with open("data.txt", "r") as data_file:
                self.data = json.load(data_file)
        except (json.decoder.JSONDecodeError, FileNotFoundError):
            self.data = dict()
        # clasificador
        try:
            with open("adaboost.txt", "r") as adaboost_file:
                self.adaboost =  json.load(adaboost_file)
        except (json.decoder.JSONDecodeError, FileNotFoundError):
            self.adaboost = None

    def data_to_string(self):
        return json.dumps(self.data)

    def params_to_string(self):
        return json.dumps(self.params)

def clasificar(clasificador, punto):
    resultado = 0.0
    for c in range(len(clasificador["clasificadores"])):
        clas = clasificador["clasificadores"][c]
        resultado += clasificador["confianzas"][c]*(1 if clas["direccion"] ^ (0 < punto[clas["dimension"]]-clas["valor"]) else -1)
    return resultado

class Window:
    def __init__(self, problem):
        # Save the problem data
        self.problem = problem

        # Create the window
        self.win = GraphWin(width=582, height=552, title="Adaboost 2d point editor")

        # Create the graph
        self.block_size = 2                                                 # scale of the graph

        self.graph_start = Point(20, 20)                                    # window point where the graph starts
        self.graph_size = Point(self.block_size, self.block_size) * 256     # graph size (0-255 * scale)

        self.graph = Rectangle(self.graph_start, self.graph_start + self.graph_size)    # creates the graph space
        self.graph.setFill(color_rgb(255, 255, 255))                                    # background color is white
        self.graph.setOutline(color_rgb(0, 0, 0))                                       # border color is black

        self.graph.draw(self.win)                                                       # draws the graph

        # Create the buttons
        self.images_start = Point(552, 20)  # window point where the class selection buttons start
        self.image_size = 9                 # button image size
        self.image_border = 5               # space between buttons

        self.class_images = []
        for class_i in range(len(self.problem.params["class_names"])):
            self.class_images.append(
                Image(
                    self.images_start + Point(0, (self.image_size + self.image_border) * class_i),  # button position
                    self.problem.params["img_files"][class_i]                                       # button image
                )
            )
            self.class_images[class_i].draw(self.win)                                               # draws the button

        # Problem editing data
        self.actual_class = 0   # selected class
        self.border_size = 4    # non-interactive border of the graph
        # Draws all loaded points
        self.img_dict = {}
        for real_pos in self.problem.data:
            adapted_pos = Point(float(real_pos.split(" ")[0]), float(real_pos.split(" ")[1]))
            adapted_pos = adapted_pos*2 + Point(20, 20)

            img = Image(adapted_pos, self.problem.params["img_files"][self.problem.data[real_pos]])
            self.img_dict[real_pos] = img
            img.draw(self.win)

        # Draw the adaboost classifiers
        if self.problem.adaboost is not None:
            #change this to increase precision
            adaboost_block_multiplier = 4;

            puntos_clasificados = []
            lineas_poligono = []
            for classifier in self.problem.adaboost["clasificadores"]:
                puntos_clasificados.append({})
                lineas_poligono.append([])


            for i in range(256 // adaboost_block_multiplier):
                for j in range(256 // adaboost_block_multiplier):
                    punto = Point(i, j)*adaboost_block_multiplier
                    for c in range(len(self.problem.adaboost["clasificadores"])):
                        # str(punto.getX()) + " " + str(punto.getY())
                        puntos_clasificados[c][str(punto)] = clasificar(self.problem.adaboost["clasificadores"][c], punto)

            for i in range(256 // adaboost_block_multiplier):
                for j in range(256 // adaboost_block_multiplier):
                    punto = Point(i, j)*adaboost_block_multiplier
                    for c in range(len(self.problem.adaboost["clasificadores"])):
                        if 0 < puntos_clasificados[c][str(punto)]:
                            superior = punto - Point(adaboost_block_multiplier, 0)
                            inferior = punto + Point(adaboost_block_multiplier, 0)
                            izquierdo = punto - Point(0, adaboost_block_multiplier)
                            derecho = punto + Point(0, adaboost_block_multiplier)
                            inferior_derecho = punto + Point(adaboost_block_multiplier, adaboost_block_multiplier)
                            # borde superior:
                            if superior.getX() < 0 or puntos_clasificados[c][str(superior)] <= 0:
                                lineas_poligono[c].append([self.graph_start + punto * self.block_size, self.graph_start + derecho * self.block_size])
                            # borde inferior:
                            if 255 < inferior.getX() or puntos_clasificados[c][str(inferior)] <= 0:
                                lineas_poligono[c].append([self.graph_start + inferior * self.block_size, self.graph_start + inferior_derecho * self.block_size])
                            # borde izquierdo:
                            if izquierdo.getY() < 0 or puntos_clasificados[c][str(izquierdo)] <= 0:
                                lineas_poligono[c].append([self.graph_start + punto * self.block_size, self.graph_start + inferior * self.block_size])
                            # borde derecho:
                            if 255 < derecho.getY() or puntos_clasificados[c][str(derecho)] <= 0:
                                lineas_poligono[c].append([self.graph_start + derecho * self.block_size, self.graph_start + inferior_derecho * self.block_size])

            self.polygons = []

            for c in range(len(self.problem.adaboost["clasificadores"])):
                self.polygons.append([])
                puntos = []
                punto_actual = lineas_poligono[c][0][1]
                puntos.append(lineas_poligono[c][0][0])
                del lineas_poligono[c][0]
                while len(lineas_poligono[c]) != 0:
                    for linea in range(len(lineas_poligono[c])):
                        ind = -1
                        if str(punto_actual) == str(lineas_poligono[c][linea][0]):
                            ind = 1
                        if str(punto_actual) == str(lineas_poligono[c][linea][1]):
                            ind = 0
                        if ind != -1:
                            puntos.append(punto_actual)
                            punto_actual = lineas_poligono[c][linea][ind]
                            del lineas_poligono[c][linea]
                            break
                poli = Polygon(puntos)
                poli.setOutline(self.problem.params["class_colors"][c])
                poli.setFill(self.problem.params["class_colors"][c])
                self.polygons[c].append(poli)
                if c == 0:
                    poli.draw(self.win)

    def get_mouse(self):
        return self.win.getMouse()

    def pos_in_graph(self, pos):
        lower_x_bound = self.graph_start.getX() + self.border_size
        higher_x_bound = (self.graph_start + self.graph_size).getX() - self.border_size
        in_x = lower_x_bound < pos.getX() < higher_x_bound
        lower_y_bound = self.graph_start.getY() + self.border_size
        higher_y_bound = (self.graph_start + self.graph_size).getY() - self.border_size
        in_y = lower_y_bound < pos.getY() < higher_y_bound
        return in_x and in_y

    def update_graph(self, pos):
        # If the position is in the graph
        if self.pos_in_graph(pos):
            # Discretizes the point
            adapted_pos = Point(pos.getX(), pos.getY())
            adapted_pos = adapted_pos - (adapted_pos % 5)
            real_pos = (adapted_pos - Point(20, 20))//2
            real_pos = str(real_pos.getX())+" "+str(real_pos.getY())
            # If there's already a symbol there, it is deleted
            if real_pos in self.problem.data:
                self.img_dict[real_pos].undraw()
                del self.img_dict[real_pos]
                del self.problem.data[real_pos]
            # If there's no symbol, a new one is created
            else:
                img = Image(adapted_pos, self.problem.params["img_files"][self.actual_class])
                self.problem.data[real_pos] = self.actual_class
                self.img_dict[real_pos] = img
                img.draw(self.win)

    def pos_in_buttons_x(self, pos):
        lower_x_bound = self.images_start.getX() - self.image_border / 2
        higher_x_bound = self.images_start.getX() + self.image_size + self.image_border / 2
        return lower_x_bound < pos.getX() < higher_x_bound

    def pos_in_button_y(self, pos, button_i):
        lower_y_bound = self.images_start.getY() + (self.image_size + self.image_border) * button_i
        lower_y_bound = lower_y_bound - self.image_border / 2       # makes the button click zone bigger than the button
        higher_y_bound = self.images_start.getY() + (self.image_size + self.image_border) * button_i + self.image_size
        higher_y_bound = higher_y_bound + self.image_border / 2     # makes the button click zone bigger than the button
        return lower_y_bound < pos.getY() < higher_y_bound

    def update_buttons(self, pos):
        # If the position is in the buttons zone
        if self.pos_in_buttons_x(pos):
            # Checks each button
            for class_i in range(len(self.problem.params["class_names"])):
                for poli in self.polygons[class_i]:
                    poli.undraw()
                if self.pos_in_button_y(pos, class_i):
                    self.actual_class = class_i
                    for poli in self.polygons[class_i]:
                        poli.draw(self.win)


def main():
    problem = AdaboostProblem()
    window = Window(problem)

    try:
        while True:
            pos = window.get_mouse()
            window.update_graph(pos)
            window.update_buttons(pos)
    except GraphicsError:
        param_file = open("params.txt", "w")
        param_file.write(window.problem.params_to_string())
        param_file.close()
        data_file = open("data.txt", "w")
        data_file.write(window.problem.data_to_string())
        data_file.close()

main()
