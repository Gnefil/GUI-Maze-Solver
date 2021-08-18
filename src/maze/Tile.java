package maze;

import java.io.Serializable;

/**
 * Class representing each tile of the maze, also nested classes: Type
 */
public class Tile implements Serializable{
    
    private Type type;


    private Tile(Type typeIn) {

        type = typeIn;

    }

    /**
     * Creates a correspoding type of tile according the given character
     * @param charIn: character representation of the type of tile; # for wall, . for corridor, e for entrance, e for exit
     * @return Returns a tile of corresponding type
     * @throws InvalidMazeException Thrown there is a invalid character for the maze
     */
    protected static Tile fromChar(char charIn) throws InvalidMazeException{

        switch(charIn) {
            case '.': return new Tile(Type.CORRIDOR);
            case 'e': return new Tile(Type.ENTRANCE);
            case 'x': return new Tile(Type.EXIT);
            case '#': return new Tile(Type.WALL);
            default: throw new InvalidMazeException("Invalid character detected in the maze text file");
        }
        
    }

    /**
     * Gets the type of the tile
     * @return Return the type of the tile (WALL, CORRIDOR, ENTRANCE, EXIT)
     */
    public Type getType() {

        return type;

    }

    /**
     * Determines if a tile is navigable (no for WALL, yes for the rest)
     * @return Returns false if the type is WALL, and true for the rest
     */
    public boolean isNavigable() {

        switch(type) {
            case WALL: return false;
            default: return true;
        }

    }

    /**
     * String representation of the tile, as it would be in character
     * @return Returns a String with character representation of the tile
     */
    public String toString() {

        switch(type) {
            case CORRIDOR: return ".";
            case ENTRANCE: return "e";
            case EXIT: return "x";
            default: return "#";
        }

    }




// Nested class Type
/**
 * Inner class Type, enum to keep constant tile type values: CORRIDOR, ENTRANCE, EXIT, WALL
 */
public enum Type {
    CORRIDOR, ENTRANCE, EXIT, WALL
}

}






