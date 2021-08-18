package maze.routing;
import maze.*;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;


/**
 * Main class to contain the route calculated from a to be solved maze
 */
public class RouteFinder implements Serializable{
    
    private Maze maze;
    private Stack<Tile> route = new Stack<Tile>();
    private boolean finished = false;
    private ArrayList<Tile> triedTiles = new ArrayList<Tile>();

    /**
     * Constructor initialises the maze to be solved attribute
     * @param mazeIn: Maze object to be solved by the RouteFinder
     */
    public RouteFinder(Maze mazeIn) {

        maze = mazeIn;

    }

    /**
     * Gets the maze attribute
     * @return Returns the maze to be solved
     */
    public Maze getMaze() {

        return maze;

    }

    /**
     * Gets the route solved so far
     * @return Return the current route
     */
    public List<Tile> getRoute() {

        return route;

    } 

    /**
     * Gets the isFinished attribute, which is true when the route solved the entire maze
     * @return Returns isFinished attribute to determine if finished or not
     */
    public boolean isFinished() {

        return finished;

    }

    /**
     * Initialises a RouteFinder by a given string that represents relative path to a RouteFinder object
     * @param stringIn: String representing the relative path
     * @return Returns a RouteFinder object given the RouteFinder object file already exist
     * @throws FileNotFoundException Thrown if serialized object could not be find
     * @throws ClassNotFoundException Thrown if the serialized object is not a RouteFinder object
     * @throws EOFException Thrown when end of file or end of stream has been reached unexpectedly during input
     * @throws IOException Thrown if there is an IO operation failed, in this case may due to try to read a file which is not an serialized object such as txt file
     */
    public static RouteFinder load(String stringIn) throws FileNotFoundException, ClassNotFoundException, EOFException, IOException{

        try(FileInputStream routeFinder = new FileInputStream(stringIn);
            ObjectInputStream routeFinderStream = new ObjectInputStream(routeFinder)) {

            RouteFinder objectRouteFinder = (RouteFinder) routeFinderStream.readObject();
            return objectRouteFinder;

        } catch (FileNotFoundException fileNotFoundExceptionLoad) {

            throw new FileNotFoundException("A FileNotFoundException occurred, the filename or path entered does not exist!");

        } catch (ClassNotFoundException classNotFoundExceptionLoad) {
            
            throw new ClassNotFoundException("A ClassNotFoundException occurred, the object read is not a RouteFinder object!");

        } catch (EOFException eofExceptionLoad) {

            throw new EOFException("An EOFException occurred, end of file has been reached unexpectedly, may be empty file.");

        } catch (IOException iOExceptionLoad) {

            throw new IOException("An IOException occurred, the object fail to be read in, may due to it's not a serielized object but a text file!");
            
        }

    }

    /**
     * Saves the current RouteFinder object into a given string representing relative path with file name to store it
     * @param stringIn: String representing the relative path and file name to store current RouteFinder object
     * @throws FileNotFoundException Thrown when the path is a directory rather than a regular file or cannot be created for some reason
     * @throws IOException Thrown if an I/O error occurs while writing stream header
     */
    public void save(String stringIn) throws FileNotFoundException, IOException {
        try(FileOutputStream routeFinder = new FileOutputStream(stringIn);
            ObjectOutputStream routeFinderStream = new ObjectOutputStream(routeFinder)) {

                routeFinderStream.writeObject(this);

        } catch (FileNotFoundException fileNotFoundExceptionSave) {

            throw new FileNotFoundException("A FileNotFoundException occurred, this is a directory rather than a file or cannot create file!");

        } catch (IOException iOExceptionSave) {

            throw new IOException("A IOException occurred when trying to save the file, please retry!");
            
        }


    }

    /**
     * Advances one step in the route, either going forward or backward if not solved yet. Also returns a boolean to declare if the maze is solved or not
     * @return Returns true if the maze is solved by route, false otherwise
     * @throws NoRouteFoundException Thrown when tried tiles covered the entire maze, leaving no direction to follow by
     */
    public boolean step() throws NoRouteFoundException{

        // Don't change anything if it's finished
        if (isFinished()) {return isFinished();}

        // Add entrance to the route stack if it's empty and, no tried tiles are set (start of the solving) otherwise it means it have been through the whole maze
        if (route.isEmpty()) { 
            if ((triedTiles.isEmpty())) {
                route.push(maze.getEntrance());
                return false;
            } else {
                throw new NoRouteFoundException();
            }
        }

        // No previous direction if it's the second step
        ArrayList<Maze.Direction> availableDirections = getNavigableDirections(route.peek());

        // Test the tile in each direction is not in triedTiles, as it's not possible to change the arraylist when iterating through it, use the iterator class
        Iterator<Maze.Direction> iterator = availableDirections.iterator();
        while (iterator.hasNext()) {
            Maze.Direction direction = iterator.next();
            if (triedTiles.contains(maze.getAdjacentTile(route.peek(), direction))) {
                iterator.remove();
            }
        }



        // Act differently for different availableDirections number, look for target tile
        Tile targetTile = null;
        switch(availableDirections.size()) {
            case 4: 
            case 3: 
            case 2: 
                // In case they are more options which already been through, let it be the last options
                ArrayList<Maze.Direction> alreadyWalked = new ArrayList<Maze.Direction>();
                for (Maze.Direction direction: availableDirections) {

                    // If gets the previous stacked tile, and post it to the last option, as it's better to explore unknown option rather than go back from where it came
                    if(route.contains(maze.getAdjacentTile(route.peek(), direction))){
                        alreadyWalked.add(direction); 
                        continue;
                    }
                    targetTile = maze.getAdjacentTile(route.peek(), direction);
                    break;
                }

                // Take any of the already walked option, in this case we will take the first one
                if (targetTile == null) {
                    targetTile = maze.getAdjacentTile(route.peek(), alreadyWalked.get(0)); 

                }
                break;

            case 1: targetTile = maze.getAdjacentTile(route.peek(), availableDirections.get(0));
                break;

            //This is just in case entrance is surrounded by 4 walls
            case 0: throw new NoRouteFoundException();

        }

        // To see if targetTile is the previous stack element
        // Push or remove the targetTile

        if ( (route.size() >= 2) && (route.get(route.size()-2) == targetTile)) {
            triedTiles.add(route.peek());
            route.pop();
        } else {
            route.push(targetTile);

            // if the tile just popped is exit, then the maze is finished
            if (targetTile == maze.getExit()) {
                finished = true;
            }
        }

        return isFinished();

    }

    // This gives navigable directions given a tile, without taking triedTiles into account
    private ArrayList<Maze.Direction> getNavigableDirections(Tile tileIn) {

        // Build an empty navigable direction arraylist to fill
        ArrayList<Maze.Direction> navigable = new ArrayList<Maze.Direction>();

        // All directions options stored in this array
        Maze.Direction[] directions = {
            Maze.Direction.EAST,
            Maze.Direction.NORTH,
            Maze.Direction.WEST,
            Maze.Direction.SOUTH
        };

        // For each of the directions, try to get its adjacentile block; if it's navigable, then add it to navigable, otherwise no. Catch in case of IndexOutOfBoundsException and continue, just not adding it to the navigable
        for (Maze.Direction direction: directions){
            try{
                if (maze.getAdjacentTile(tileIn, direction).isNavigable()) {navigable.add(direction);}
            } catch(IndexOutOfBoundsException error){}
        }

        return navigable;

    }

    /**
     * Gives the String representation of the maze and the route with "*", including tried tiles with "-"
     * @return Returns a visual String representation of the maze with route ("*"") and tried tiles ("-")
     */
    public String toString() {

        String output = "";

        // Place the base model of maze
        output = maze.toString();

        // Create a character array version of the output to facilitate changes
        char[] outputArray = output.toCharArray();

        // Place the tried tiles as -
        if (!triedTiles.isEmpty()) {
            for (Tile each: triedTiles) {
                // Get the coordinates and process them as number of characters, the inverse of getY() is the real number of rows starting from top
                int column = maze.getTileLocation(each).getX();
                int rows = (maze.getTiles().size()-1) - maze.getTileLocation(each).getY();

                // Row size + 1 for each column as newline character is also in the string
                int ordinalNumberToTheCharacter = rows*(maze.getTiles().get(0).size() + 1) + column;

                outputArray[ordinalNumberToTheCharacter] = '-';  
            }
        }


        // Place the route as *
        if (!route.isEmpty()) {
            for (Tile each: route) {
            // Get the coordinates and process them as number of characters, the inverse of getY() is the real number of rows starting from top
            int column = maze.getTileLocation(each).getX();
            int rows = (maze.getTiles().size()-1) - maze.getTileLocation(each).getY();

            // Row size + 1 for each column as newline character is also in the string
            int ordinalNumberToTheCharacter = rows*(maze.getTiles().get(0).size() + 1) + column;

            outputArray[ordinalNumberToTheCharacter] = '*';  
            }
        }

        // Turn into string again
        output = "";
        for(char symbol: outputArray) {
            output += symbol;
        }

        return output;

    }


}










