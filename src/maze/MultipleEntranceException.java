package maze;

/**
 * Exception class that extends InvalidMazeException, thrown when trying to set multiple entrances
 */
public class MultipleEntranceException extends InvalidMazeException{

    /**
    * Constructor that throws MultipleEntranceException with a message
    * @throws MultipleEntranceException Thrown when there is a maze is trying to have multiple entrances
    */
    public MultipleEntranceException () {
        super("A MultiEntranceException occurred, there are more than one entrance!");
    }
    
}
