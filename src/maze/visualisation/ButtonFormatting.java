package maze.visualisation;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Helper class to format a button given a button and several parameters, this saves code lines
 */
public class ButtonFormatting {

    private Button button;

    /**
     * Main method to format the button with parameters given, automatically set's the TextAlignment into CENTER
     * @param button: The button want to be formatted
     * @param fontSize: The font size wanted for the text inside the button
     * @param colorOfTextFillHex: String of hexadecimal representation of the font color willing to set 
     * @param padding: Space in pixels between the text inside button and the border of the button 
     * @param width: Width of the button if willing to change, null will do noting
     */
    public static void format(Button button, double fontSize, String colorOfTextFillHex, int padding, Integer width) {

        button.setTextAlignment(TextAlignment.CENTER);
        button.setFont(new Font("Cambria", fontSize));
        button.setPadding(new Insets(padding));
        button.setTextFill(Color.valueOf(colorOfTextFillHex));
        if (width != null) {
            button.setMaxWidth(width);
        }
    }
}
















