# Threshold-based AdaBoost
### *Visualization of bidimensional example*

<br>

Implementation of AdaBoost as explained in the `assignment.pdf` document. Threshold-based refers to the fact that all hyperplanes (weak learners) are parallel to every dimension's axis except for the one they are perpendicular to. Final objective is to be able to classify handwritten digits.

This stage of the project consists on a bidimensional example which can be easily represented. This allows for a friendlier debugging environment. Each dimension ranges from 0 to 255. There are three classes: circle (red), cross (blue) and triangle (green). The main program is written in Java 8 and inside a _NetBeans 11.0_ project, and a support program written in Python 3.6 is used to visualize the strong learners and edit the data set.

Both programs are written in an awful mix of English and Spanish, the code is messy (especially the Python one) and most parameters must be set through code changes. Consider this as a **warning**

<br>

## `2dPoints.py` 

`2dPoints` offers an interface to edit the data set and visualize the strong learners. Click on the left buttons to change the type of point you create or to change the strong learner displayed. Click on the graph to add a point, or click on an already existint point to delete it. New data set is saved on window closing. To delete all points at once, close the program and delete the `data.txt`.

It might take a while to load because of the strong learner representation, you can change the resolution of the grid in which the strong learner is displayed by passing a number argument to `2dPoints.py`. The default block size value is 4, a smaller number will provide more resolution, and a bigger number will work faster (and probably look nicer).

The interface is kind of wonky, but it works. It uses [John Zelle's graphics library](https://mcsp.wartburg.edu/zelle/python/graphics/graphics.pdf), a modified copy of which is located in the `2dpoints\libs`. The modification consists on a change in the Polygon.draw function, so that it is filled with a dotted pattern, allowing the points to be seen.

Do not be afraid about trying to add a new class, it shouldn't be difficult: just modify the `params.txt`.

<br>

## `Main.java`

The main program of the NetBeans project can be run without any parameter: it will train an AdaBoost instance, show its accuracy, and save it to the `2dpoints\adaboost.txt` file. To alter the default parameters, the code must be changed. Thankfully all parameters are encapsulated behind the *Parametros* class, and all of them are set on the first lines of the main function.

The *dimensiones* and *rangoValores* refer to the data set's shape, and should not be changed. To change the train-test division percentage, modify the *porcentajeTrain* parameter. The way in which the sets are generated is decided by *modoGenerador*, which at the moment allows for two values: *"fijo"* (not randomized, always the same sets) and *"aleatorio"* (randomized). Finally *intentosAleatorios* represents the number of weak learners generated for each weak learner added to a strong learner (*A* in the `assignment.pdf`), and *numeroClasificadores* represents the number of weak learners to be added to each strong learner (*T* in the `assignment.pdf`).
