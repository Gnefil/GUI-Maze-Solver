package maze;

/**
 * Exception class that extends RuntimeException, thrown normally when there is an invalid maze
 */
public class InvalidMazeException extends RuntimeException{
    
    /**
     * Constructor that throws invalid maze exception with a message
     * @throws InvalidMazeException Thrown when there is a maze that does not comply with the requirement
     */
    public InvalidMazeException() {
        super("An InvalidMazeException Occurred, please have a look!");
    }

    /**
     * Constructor that throws invalid maze exception given a message
     * @param msg: String of message wanted to send
     * @throws InvalidMazeException Thrown when there is a maze that does not comply with the requirement
     */
    public InvalidMazeException(String msg) {
        super(msg);
    }

    
}
