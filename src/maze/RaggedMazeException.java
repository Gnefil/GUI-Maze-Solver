package maze;

/**
 * Exception class that extends InvalidMazeException, thrown when the maze is not rectangular
 */
public class RaggedMazeException extends InvalidMazeException{

    /**
    * Constructor that throws RaggedMazeException with a message
    * @throws RaggedMazeException Thrown when there is a irregular-shaped maze not being rectangular
    */
    public RaggedMazeException() {
        super("A RaggedMazeException occurred, the maze is not regular rectangle!");
    }

    
}
