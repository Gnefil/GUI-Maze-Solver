package maze;

/**
 * Exception class that extends InvalidMazeException, thrown when there is no exits in the maze
 */
public class NoExitException extends InvalidMazeException{

    /**
    * Constructor that throws NoExitException with a message
    * @throws NoExitException Thrown when there is a maze not having any exits
    */
    public NoExitException() {
        super("A NoExitException occurred, there is no exit!");
    }

}
