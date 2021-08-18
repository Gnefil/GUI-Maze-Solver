package maze;

import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

/**
 * Main class to hold maze objects and its methods, also nested classes: Coordinate and Direction
 */

public class Maze implements Serializable{

    
/**
 * Inner class Coordinate, to build coordinate feature of each tile
 */

public class Coordinate {
    
    private int x;
    private int y;


    /**
     * Constructor initialises x and y of Coordinate object
     * @param xIn: the x-axis index (from left to right)
     * @param yIn: the y-axis index (from bottom to top)
     */
    public Coordinate(int xIn, int yIn) {

        x = xIn;
        y = yIn;

    }

    /**
     * Gets x coordinate
     * @return Returns the x attribute of Coordinate
     */
    public int getX() {

        return x;

    }

    /**
     * Gets y coordinate
     * @return Returns the y attribute of Coordinate
     */
    public int getY() {

        return y;

    }

    /**
     * String representation of Coordiante as (x, y)
     * @return Returns the string representation of Coordinate as (x, y)
     */
    public String toString() {

        // return String.format("(%d, %d)", x, y);
        return "(" + x + ", " + y +")";
    }
}

    private Tile entrance;
    private Tile exit;
    private List<List<Tile>> tiles =  new ArrayList<List<Tile>> ();



    private Maze() {

    }


    // Extra method to read the files and turn them into String
    private static String stringOfFile(String relativePathToMazeFile) throws RaggedMazeException, FileNotFoundException, IOException {

        try(FileReader mazeFile = new FileReader(relativePathToMazeFile);
            BufferedReader mazeStream = new BufferedReader(mazeFile)){

            // Added a previous start point for mazeString, otherwise an extra \n is introduced to the String
            String mazeString = mazeStream.readLine();
            int size = mazeString.length();
            String temporal; 
            while((temporal = mazeStream.readLine()) != null) {
                // If size is not always the same then throw RaggedMazeException
                if (temporal.length() != size) {throw new RaggedMazeException();}
                // \n because the readLine method deletes the last return character
                mazeString = mazeString + "\n" + temporal;
            }

            return mazeString;

        } catch (FileNotFoundException readStringOFileNotFoundException) {

            throw new FileNotFoundException("Could not find the entered file!");
            

        } catch (IOException readStringOfFileIOException) {

            throw new IOException("An IOException occurred when trying to read the file, please retry");
 
        } 


    }

    /**
     * Initialises a maze object given a relative path name to a text file with required format, note that this method also throws other exceptions
     * @param userPathIn: String that represents the relative path to the text file
     * @return Returns a maze object given the structure of characters in the text file
     * @throws FileNotFoundException Thrown when the target file String does not match with any file found
     * @throws IOException Thrown when an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
     * @throws RaggedMazeException Thrown when the maze is irregular with unequal column/rows number
     * @throws NoEntranceException Thrown when the maze has no entrance
     * @throws NoEntranceException Thrown when the maze has no exit
     * @throws NoExitException Thrown when the maze has no exit
     * @throws MultipleEntranceException Thrown when the maze has more than one entrance
     * @throws MultipleExitException Thrown when the maze has more than one exit
     * @throws IllegalArgumentException Thrown when trying to set entrance or exit with a tile not belongs tiles attribute.
     * @throws InvalidMazeException Thrown when trying to use an unexpected character from txt file into the maze.
     */
    public static Maze fromTxt(String userPathIn) throws InvalidMazeException, IllegalArgumentException, MultipleExitException, MultipleEntranceException, NoExitException, NoEntranceException, RaggedMazeException, FileNotFoundException, IOException{

        // Split into an array of Strings
        String[] mazeStrings = stringOfFile(userPathIn).split("\n");
        Maze maze = new Maze();
        // Temporal 1D arraylist added to tile

        int[] entran = new int[2];
        entran[0] = -1;
        
        int[] ex = new int[2];
        ex[0] = -1;


        for (int i = 0; i < mazeStrings.length; i++) {

            // Know issue with .clear() method (took me hours to figure out). When clear the sublist which previously have being added with list.add(sublist), the list will get empty too.
            List<Tile> temp = new ArrayList<Tile>();
            for (int j = 0; j < mazeStrings[i].length(); j++) {

                // This takes the whole row and add them into temp


                Tile tile = Tile.fromChar(mazeStrings[i].charAt(j));

                // Test if there is multi entrance or exit
                temp.add(tile);
                if(mazeStrings[i].charAt(j) == 'e') { 
                    entran[0] = i;
                    entran[1] = j;
                } else if(mazeStrings[i].charAt(j) == 'x') {
                    ex[0] = i;
                    ex[1] = j;
                }

            }
            
            // This adds the temp (one row) to the whole of 2D arraylist tiles
            maze.tiles.add(temp);

        }


        if(entran[0] == -1) {throw new NoEntranceException();} else {maze.setEntrance(maze.tiles.get(entran[0]).get(entran[1]));
        }
        if(ex[0] == -1) {throw new NoExitException();} else {maze.setExit(maze.tiles.get(ex[0]).get(ex[1]));
        }

        return maze;

    }

    /**
     * Gets the corresponding tile given a tile and an orientation to the adjacent tile wanted
     * @param currentTileIn: the current tile willing to look from
     * @param directionIn: the orientation from the current tile, can be NORTH, SOUTH, EAST, WEST
     * @return Returns the corresponding tile after moved from current tile and direction
     * @throws IndexOutOfBoundsException Thrown to indicate no tile will be available in that direction, out of tiles range
     */
    public Tile getAdjacentTile(Tile currentTileIn, Direction directionIn) throws IndexOutOfBoundsException{

        int x = 0;
        int y = 0;

        switch (directionIn) {
            case NORTH: y = 1; break;
            case SOUTH: y = -1; break;
            case EAST: x = 1; break;
            case WEST: x = -1; break;
        }

        Coordinate newCoordinate = new Coordinate(getTileLocation(currentTileIn).getX() + x, getTileLocation(currentTileIn).getY() + y);

        return getTileAtLocation(newCoordinate);

    }

    /**
     * Gets the entrance tile of the maze
     * @return Return the entrance tile of the maze
     */
    public Tile getEntrance() {

        return entrance;

    }

    /**
     * Gets the exit tile of the maze
     * @return Return the exit tile of the maze
     */
    public Tile getExit() {
        return exit;

    }

    /**
     * Gets the tile of a given location
     * @param coordinateIn: Coordinate looking for
     * @return Returns a tile in the given coordinate
     * @throws IndexOutOfBoundsException Thrown if the coordinate is out of tiles range
     */
    public Tile getTileAtLocation(Coordinate coordinateIn) throws IndexOutOfBoundsException{

        // To get the coordinate upside down of coordinate y
        int distanceToLimit = (tiles.size()-1) - coordinateIn.getY();

        return tiles.get(distanceToLimit).get(coordinateIn.getX());


    }

    /**
     * Gets the location of a given tile
     * @param tileIn: Tile willing look for coordinate
     * @return Returns the coordinate of the tile
     */
    public Coordinate getTileLocation(Tile tileIn) {

        int inverseI;

        for (int i = 0; i < tiles.size(); i++) {
            
             // For the content of coordinate
            inverseI = (tiles.size()-1) - i;
            for (int j = 0; j < tiles.get(0).size(); j++) {

                // Return the coordinate object if found
                if(tiles.get(i).get(j) == tileIn) {
                    Coordinate coordinate = new Coordinate(j, inverseI);
                    return coordinate;
                } 
            }
        }
        return null;

    }

    /**
     * Get tiles of the maze
     * @return Returns tiles which contains the tiles of the maze
     */
    public List<List<Tile>> getTiles() {

        return tiles;

    }

    private void setEntrance(Tile tileIn) throws MultipleEntranceException, IllegalArgumentException{

        // Make sure the tile is from tiles
        for (List<Tile> subList: tiles) {
            if (subList.contains(tileIn)) {
                if (entrance == null) {
                    entrance = tileIn;
                    return;
                } else {
                    throw new MultipleEntranceException();
                }
            }
        }

        throw new IllegalArgumentException("An IllegalArgumentException occurred when trying to set a not-in-maze tile as entrance!");
    }

    private void setExit(Tile tileIn) throws MultipleEntranceException, IllegalArgumentException{

        // Make sure the tile is from tiles
        for (List<Tile> subList: tiles) {
            if (subList.contains(tileIn)) {
                if (exit == null) {
                    exit = tileIn;
                    return;
                } else {
                    throw new MultipleExitException();
                }
            }
        }

        throw new IllegalArgumentException("An IllegalArgumentException occurred when trying to set a not-in-maze tile as exit!");
    }

    /**
     * Gives the String representation of the maze similar as it would be in text file
     * @return Returns the visual string representation of the maze
     */
    public String toString() {
        
        String output = "";

        for (List<Tile> subList: tiles) {
            for (Tile each: subList) {
                output += each.toString();
            }
            output += "\n";
        }
        return output;

    }


// Nested class Direction
/**
 * Inner class Direction, enum which contains for directions to keep them constant: NORTH, SOUTH, EAST, WEST
 */
public enum Direction {
    NORTH, SOUTH, EAST, WEST
}
}



































