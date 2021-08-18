package maze;

/**
 * Exception class that extends InvalidMazeException, thrown when trying to set multiple exits
 */
public class MultipleExitException extends InvalidMazeException{

    /**
    * Constructor that throws MultipleExitException with a message
    * @throws MultipleEntranceException Thrown when there is a maze is trying to have multiple exits
    */
    public MultipleExitException() {
        super("A MultiExitException occurred, there are more than one exit!");
    }

    
}
