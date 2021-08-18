package maze.routing;

import maze.InvalidMazeException;

/**
 * Exception class that extends InvalidMazeException, thrown when the maze can't be solved
 */
public class NoRouteFoundException extends InvalidMazeException{
    
    /**
    * Constructor that throws NoRouteFoundException with a message
    * @throws NoRouteFoundException Thrown when there is no solution for the maze
    */
    public NoRouteFoundException () {
        super("A NoRouteFoundException occurred, this maze can't be solved!");
    }

}
