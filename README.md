# GUI-Maze-Solver
A simple maze solver using javafx as graphical user interface. Allows customised mazes.

Execution script provided.
In Linux use
```
$ ./javac.sh src/MazeApplication.java  
$ ./java.sh MazeApplication
```
In Windows use
```
$ javac src/MazeApplication.java  
$ java MazeApplication
```
to execute the application.

# Customise your maze
### Bearing in mind that the maze must be rectangular

|Character|`Function`|*Limit*             |
|:-------:|:--------:|:------------------:|
|e        |entrance  |only 1              |
|x        |exit      |only 1              |
|*        |wall      |as many as necessary|
|.        |path      |as many as necessary|

Store in txt file, and remember its path to use it in the application


