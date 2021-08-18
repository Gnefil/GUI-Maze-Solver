import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import maze.Maze;
import maze.routing.RouteFinder;
import maze.visualisation.*;

/**
 * Main class of the GUI of the maze. Consist of 4 scenes stored in each of the 4 private methods, Start method and main method. 
 * Each of the scene methods rebuilds the scene each time when called, and always ends with a VBox containing all the elements which is itself contained in the corresponding scene, the last instruction of them is to set the stage to the one called. When there is a need to refresh elements in the same scene, the method itself can call itself recursively to refresh.
 * All attributes declared because their use is to be shared for the all scenes when they are involved in some lambda expressions, and therefore needs to be global. Some will require initial values, others not.
 */
public class MazeApplication extends Application{


    // These are attributes need to be used in eventhandlers, therefore needs to be declared as attributes. Some require initial values, other's don't
    private Scene startScene, inputScene, mazeScene, saveScene;
    private Maze maze;
    private RouteFinder routeFinder;
    private String successInput = "File read successfully! Loading maze......\nEnter return key again to start. ", resultColor = "7cf480", successSave = "File saved successfully!";
    private Label inputLabel = new Label("Enter relative path and filename to load the maze: "), resultLabel;
    private TextField inputTextField = new TextField();
    private boolean isNewMaze = true;


    private void callingSaveScene(Stage stage) {

        // Save scene -------------------------------------------------------------------------------------------------------

        // Set result label not visible until told to do so
        if (resultLabel != null) {
            resultLabel.setVisible(false);
        }
        // Back button, to start scene
        Button backButton = new Button("BACK");
        ButtonFormatting.format(backButton, 30, "#8b6761", 0, null);
        backButton.setOnMouseClicked(e -> {
            callingMazeScene(stage);
        });

        VBox backButtonBox = new VBox();
        backButtonBox.getChildren().addAll(backButton);
        backButtonBox.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(backButtonBox, new Insets(50, 50, 0, 50));


        // Text for instruction
        
        Label saveLabel = new Label("Enter relative path and filename.obj to save the current maze: ");
        saveLabel.setFont(Font.font("Cambria", 50));
        saveLabel.setTextAlignment(TextAlignment.CENTER);
        saveLabel.setTextFill(Color.valueOf("#FFA17F"));
        saveLabel.setWrapText(true);

        VBox saveLabelBox = new VBox();
        saveLabelBox.getChildren().addAll(saveLabel);
        VBox.setMargin(saveLabelBox, new Insets(50, 50, 0, 50));



        // Text Field to input, needs distinguish
        TextField saveTextField = new TextField("resources/routes/route.obj");
        saveTextField.setFont(Font.font("Cambria", 20));
        saveTextField.setStyle("-fx-text-inner-color: #af796b");
        saveTextField.setOnAction(e -> {
            String path = saveTextField.getText();
            // Try to save the maze into path
            try {
                routeFinder.save(path);
                // Output successful message
                successSave = "File saved successfully!";
                resultColor = "#7cf480";
                resultLabel.setText(successSave);
                resultLabel.setTextFill(Color.valueOf(resultColor));
                resultLabel.setVisible(true);

            } catch (Exception exception) {

                // Output failed message
                if (isNewMaze) {
                successSave = exception.getMessage() + " Please reintroduce a valid path and filename.";
                resultColor = "#f47e7c";
                resultLabel.setText(successSave);
                resultLabel.setTextFill(Color.valueOf(resultColor));
                resultLabel.setVisible(true);
                }
            }

        });

        VBox saveTextFieldBox = new VBox();
        saveTextFieldBox.getChildren().addAll(saveTextField);
        VBox.setMargin(saveTextFieldBox, new Insets(50, 50, 0, 50));


        // The result of the text field
        resultLabel = new Label(successSave);
        resultLabel.setVisible(false);
        resultLabel.setFont(Font.font("Cambria", 30));
        resultLabel.setTextFill(Color.valueOf(resultColor));
        resultLabel.setWrapText(true);

        VBox resultLabelBox = new VBox();
        resultLabelBox.getChildren().addAll(resultLabel);
        VBox.setMargin(resultLabelBox, new Insets(20, 50, 0, 50));



        // Closing, container
        VBox saveSceneContainer = new VBox();
        saveSceneContainer.getChildren().addAll(backButtonBox, saveLabelBox, saveTextFieldBox, resultLabelBox);
        saveSceneContainer.setBackground(new Background(new BackgroundFill(Color.valueOf("#0c2842"), null, null)));



        // Create start scene for new maze and load route
        saveScene = new Scene(saveSceneContainer, 800, 700);    
        stage.setScene(saveScene);
        
    }



    private void callingMazeScene(Stage stage) {
        
        // Maze scene -------------------------------------------------------------------------------------------------------

        // Menu button
        Button menuButton = new Button("MENU");
        ButtonFormatting.format(menuButton, 30, "#8b6761", 2, null);
        menuButton.setOnMouseClicked(e -> {
            callingStartScene(stage);
        });

        // Step button
        Button stepButton = new Button("STEP");
        ButtonFormatting.format(stepButton, 30, "#8b6761", 2, null);
        stepButton.setOnMouseClicked(e -> {
            routeFinder.step();
            callingMazeScene(stage);
        });

        // Save button
        Button saveButton = new Button("SAVE");
        ButtonFormatting.format(saveButton, 30, "#8b6761", 2, null);
        saveButton.setOnMouseClicked(e -> {
            callingSaveScene(stage);
        });


        // HBox for menu and save button
        HBox menuStepSaveButtonsBox = new HBox(200);
        menuStepSaveButtonsBox.getChildren().addAll(menuButton, saveButton, stepButton);
        menuStepSaveButtonsBox.setAlignment(Pos.CENTER);
        VBox.setMargin(menuStepSaveButtonsBox, new Insets(50, 50, 0, 50));
        
        // Grid pane to cointain the body of visual maze
        GridPane mazeGrid = new GridPane();


        // Create a string to store the representation of the maze
        String[] mazeStrings = routeFinder.toString().split("\n");

        // Calculate dimentions of each cell in the grid
        double eachCellHeight = 550/mazeStrings.length;
        double eachCellWidth = 550/mazeStrings[0].toCharArray().length;

        // Read out everycharacter and form a respective square
        for (int row = 0; row < mazeStrings.length; row++) {
            for (int column = 0; column < mazeStrings[row].toCharArray().length; column++) {
                // For each character, apply the fromChar method from SquareBlock to build a new shape and add it to the grid
                mazeGrid.add(SquareBlock.fromChar(mazeStrings[row].charAt(column), eachCellWidth, eachCellHeight), column, row);
            }
        }
        // Make the size of mazeGrid fit to its parent
        GridPane.setFillHeight(mazeGrid, true);
        GridPane.setFillWidth(mazeGrid, true);
        mazeGrid.setAlignment(Pos.CENTER);

        VBox mazeGridBox = new VBox();
        mazeGridBox.getChildren().addAll(mazeGrid);
        mazeGridBox.setMinHeight(mazeGrid.getHeight());
        mazeGridBox.setAlignment(Pos.CENTER);
        VBox.setMargin(mazeGridBox, new Insets(30, 50, 0, 50));


        // Closing, container
        VBox mazeSceneContainer = new VBox();
        mazeSceneContainer.getChildren().addAll(menuStepSaveButtonsBox, mazeGridBox);
        mazeSceneContainer.setBackground(new Background(new BackgroundFill(Color.valueOf("#ffa17f"), null, null)));



        // Create start scene for new maze and load route
        mazeScene = new Scene(mazeSceneContainer, 800, 700);    
        stage.setScene(mazeScene);


    }

    private void callingInputScene(Stage stage) {

        // Input Scene --------------------------------------------------------------------------------------------------------------

        // Let the result label not visible until it's called to do so
        if (resultLabel != null) {
            resultLabel.setVisible(false);
        }

        // Back button, to start scene
        Button backButton = new Button("BACK");
        ButtonFormatting.format(backButton, 30, "#8b6761", 0, null);
        backButton.setOnMouseClicked(e -> {
            callingStartScene(stage);

        });

        VBox backButtonBox = new VBox();
        backButtonBox.getChildren().addAll(backButton);
        backButtonBox.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(backButtonBox, new Insets(50, 50, 0, 50));


        // Text for instruction, needs distinguish
        
        inputLabel.setFont(Font.font("Cambria", 50));
        inputLabel.setTextAlignment(TextAlignment.CENTER);
        inputLabel.setTextFill(Color.valueOf("#FFA17F"));
        inputLabel.setWrapText(true);

        VBox inputLabelBox = new VBox();
        inputLabelBox.getChildren().addAll(inputLabel);
        VBox.setMargin(inputLabelBox, new Insets(50, 50, 0, 50));



        // Text Field to input, needs distinguish
        inputTextField.setFont(Font.font("Cambria", 20));
        inputTextField.setStyle("-fx-text-inner-color: #af796b");
        inputTextField.setOnAction(e -> {
            String path = inputTextField.getText();
            // Depending on loading or creating new, try different methods
            try {
                if (isNewMaze) {
                    Maze maze = Maze.fromTxt(path);
                    routeFinder = new RouteFinder(maze);
                } else {
                    routeFinder = RouteFinder.load(path);
                }

                // This is not part of algorithm but to give more response to the user, ensure it's read correctly by letting the user click 2 times enter
                if (resultLabel.isVisible() && resultColor.equals("#7cf480")){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    callingMazeScene(stage);
                }

                // Output successful message

                successInput = "File read successfully! Loading maze......\nEnter return key again to start.";
                resultColor = "#7cf480";
                resultLabel.setText(successInput);
                resultLabel.setTextFill(Color.valueOf(resultColor));
                resultLabel.setVisible(true);


            } catch (Exception exception) {

                // Output fail message
                if (isNewMaze) {
                    successInput = exception.getMessage() + " Please reintroduce a valid maze text file to be load.";
                } else {
                    successInput = exception.getMessage() + " Please reintroduce a valid RouteFinder file to be load.";
                }
                resultColor = "#f47e7c";
                resultLabel.setText(successInput);
                resultLabel.setTextFill(Color.valueOf(resultColor));
                resultLabel.setVisible(true);
            }

        });

        VBox inputTextFieldBox = new VBox();
        inputTextFieldBox.getChildren().addAll(inputTextField);
        VBox.setMargin(inputTextFieldBox, new Insets(50, 50, 0, 50));


        // The result of the text field
        resultLabel = new Label(successInput);
        resultLabel.setVisible(false);
        resultLabel.setFont(Font.font("Cambria", 30));
        resultLabel.setTextFill(Color.valueOf(resultColor));
        resultLabel.setWrapText(true);

        VBox resultLabelBox = new VBox();
        resultLabelBox.getChildren().addAll(resultLabel);
        VBox.setMargin(resultLabelBox, new Insets(20, 50, 0, 50));



        // Closing, container
        VBox inputSceneContainer = new VBox();
        inputSceneContainer.getChildren().addAll(backButtonBox, inputLabelBox, inputTextFieldBox, resultLabelBox);
        inputSceneContainer.setBackground(new Background(new BackgroundFill(Color.valueOf("#0c2842"), null, null)));

        // Create start scene for new maze and load route
        inputScene = new Scene(inputSceneContainer, 800, 700);
        stage.setScene(inputScene);


    }


    private void callingStartScene(Stage stage) {

        // Start scene --------------------------------------------------------------------------------------------------

        // Maze solver title
        Label theMazeSolverTitle = new Label("The Maze Solver");
        theMazeSolverTitle.setFont(Font.font("Cambria", FontWeight.BOLD, 70));
        theMazeSolverTitle.setPadding(new Insets(50));
        theMazeSolverTitle.setTextAlignment(TextAlignment.CENTER);
        theMazeSolverTitle.setTextFill(Color.valueOf("#00223E"));

        VBox theMazeSolverTitleBox = new VBox();
        theMazeSolverTitleBox.setAlignment(Pos.CENTER);
        theMazeSolverTitleBox.getChildren().addAll(theMazeSolverTitle);
        theMazeSolverTitleBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#FFA17F"), null, null)));


        // Start buttons
        Button newMazeButton = new Button("NEW MAZE");
        ButtonFormatting.format(newMazeButton, 50, "#bc806e", 20, 350);
        newMazeButton.setOnMouseClicked(e -> {
            isNewMaze = true;
            inputTextField.setText("resources/mazes/maze2.txt");
            inputLabel.setText("Enter relative path and filename.txt to create new maze: ");
            callingInputScene(stage);
        });

        Button loadRouteButton = new Button("LOAD MAZE");
        ButtonFormatting.format(loadRouteButton, 50, "#bc806e", 20, 350);
        loadRouteButton.setOnMouseClicked(e-> {
            isNewMaze = false;
            inputTextField.setText("resources/routes/route1.obj");
            inputLabel.setText("Enter relative path and filename.obj to load solving maze: ");
            callingInputScene(stage);
        });


        VBox startButtonsBox = new VBox(50);
        startButtonsBox.setAlignment(Pos.CENTER);
        startButtonsBox.getChildren().addAll(newMazeButton, loadRouteButton);
        startButtonsBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#0c2842"), null, null)));
        startButtonsBox.setFillWidth(true);
        VBox.setVgrow(startButtonsBox, Priority.ALWAYS);


        // Closing
        VBox startSceneContainer = new VBox();
        startSceneContainer.getChildren().addAll(theMazeSolverTitleBox, startButtonsBox);
        startSceneContainer.setBackground(new Background(new BackgroundFill(Color.valueOf("#000000"), null, null)));

        // Create start scene for new maze and load route
        Scene startScene = new Scene(startSceneContainer, 800, 700);
        stage.setScene(startScene);


    }

    /**
     * Start method which is called when javafx is called to run the program. In this case, this start method starts a stage named "Coursework II: The Maze Solver", and sets the startScene as the first scene to be set, by calling method callingStartScene. After startScene being called, it will contain buttons that calls further scenes.
     * @param stage: The stage, window to show the GUI
     */
    @Override
    public void start(Stage stage) {

        // Start the stage --------------------------------------------------------------------------------------------------
        stage.setTitle("Coursework II: The Maze Solver");
        callingStartScene(stage);
        stage.show();
    }


    /**
     * The main method to start the program.
     * @param args: Parameter to help the JVM find the starter method
     */
    public static void main(String[] args) {
        launch(args);
    }
}




















