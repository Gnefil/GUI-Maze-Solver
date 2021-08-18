package maze.visualisation;

import javax.swing.GroupLayout.Alignment;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.TextAlignment;


/**
 * Visualisation class that constructs the blocks that forms the maze, called by the MazeApplication 
 */
public class SquareBlock {

    /**
     * Main static class that returns a StackPane once builden from the character, width and height entered from the mazeGrid
     * @param characterIn: The character representation of a tile in current maze state: 'e' for entrance, 'x' for exit, '.' for corridor, '*' for route followed, '-' for tried tiles, '#' for walls
     * @param width: The width given by the GridPane, mazeGrid containing this block
     * @param height: The height given by the GridPane, mazeGrid containing this block
     * @return Returns a StackPane with a visual representation for the given character in a suitable width and height
     */
    public static StackPane fromChar(char characterIn, double width, double height) {

        // Create a pane to store the rectangle and anything above it
        StackPane blockToReturn = new StackPane();
        Rectangle background = new Rectangle(width, height);
        blockToReturn.getChildren().addAll(background);

        String color;

        // Taking the smaller one of the dimensions as the length of the sides of the star. This used for the construction of some figures
        double length = Math.min(width, height);

        switch (characterIn) {
            case 'e': color = "#b97e6d";
                // Aim to place a red circle
                Circle circle = new Circle(length/2, Color.RED);

                // Add the created figure into the stackPane
                blockToReturn.getChildren().addAll(circle);
                break;
            case 'x': color = "#FFA17F";

                // Aim to draw a green star using canvas and strokePolygon

                Polygon star = new Polygon(new double[]{
                    0, 0.4*length,
                    length, 0.4*length,
                    0.2*length, length,
                    0.5*length, 0,
                    0.8*length, length,
                    0, 0.4*length
                });
                star.setFill(Color.valueOf("7cf480"));

                blockToReturn.getChildren().addAll(star);
                break;
            case '.': color = "#fff1cf";
                break;
            case '*': color = "#fff1cf";
                // Aim to drow a diamond to indicate the route following
                Polygon diamond = new Polygon(new double[]{
                    0, length/2,
                    length/2, 0,
                    length, length/2,
                    length/2, length
                });
                diamond.setFill(Color.valueOf("#EF916F"));
                blockToReturn.getChildren().addAll(diamond);
                break;
            case '-': color = "#2f3a4a";
                break;
            default: color = "#0c2842";
        }

        background.setFill(Color.valueOf(color));



        return blockToReturn;
    }
}




























