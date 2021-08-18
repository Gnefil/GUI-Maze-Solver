package maze;

/**
 * Exception class that extends InvalidMazeException, thrown when there is no entrances in the maze
 */
public class NoEntranceException extends InvalidMazeException{

    /**
    * Constructor that throws NoEntranceException with a message
    * @throws NoEntranceException Thrown when there is a maze not having any entrances
    */
    public NoEntranceException() {
        super("A NoEntranceException occurred, there is no entrance!");
    }

    
}
