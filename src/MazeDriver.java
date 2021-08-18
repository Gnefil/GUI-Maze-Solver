import maze.*;
import maze.routing.*;
import maze.visualisation.*;

/**
 * Temporal class that no longer needs to be implemented as MazeApplication is working fine
 */
public class MazeDriver {
    public static void main(String args[]) {

        // Maze maze1 = Maze.fromTxt("resources/mazes/maze1.txt");
        // Maze.Coordinate coordinate1 = maze1.new Coordinate(0, 0);
        // Tile tile1 = maze1.getTiles().get(0).get(0);
        // Tile tile2 = maze1.getTileAtLocation(coordinate1);
        // RouteFinder routeFinder1 = new RouteFinder(maze1);

        
        // System.out.println(maze1.toString());
        // System.out.println(maze1.getTileLocation(tile2));
        // System.out.println(maze1.getAdjacentTile(tile2, Maze.Direction.EAST));

        // for (int i=0; i<4; i++) {
        //     for (int j=0; j<6; j++) {
        //         routeFinder1.step();
        //     }
        //     System.out.println(routeFinder1.toString());
        // }

        // routeFinder1.save("resources/routes/route1.obj");

        System.out.println(RouteFinder.load("resources/routes/route1.obj"));
        System.out.println(RouteFinder.load("resources/mazes/maze1.txt"));
        System.out.println(RouteFinder.load("resources/mazes/maze1.obj"));
        System.out.println(RouteFinder.load("Hydrogen.obj"));

    }
}











