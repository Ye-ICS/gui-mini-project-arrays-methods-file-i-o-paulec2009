import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) {

        // Create components and containers
        BorderPane mainBox = new BorderPane();

        VBox contentBox = new VBox(5);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle("-fx-padding: 10");

        VBox menuBox = new VBox(5);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle("-fx-padding: 10");

        VBox flashcardBox = new VBox();
        flashcardBox.setAlignment(Pos.CENTER);
        flashcardBox.setStyle("-fx-border-color: black");
        flashcardBox.setMinWidth(300);
        flashcardBox.setMinHeight(200);
        Text flashcardText = new Text("No flashcards open.");

        HBox controlsBox = new HBox(5);
        controlsBox.setAlignment(Pos.CENTER);

        Button previousBtn = new Button("<");
        Button flipBtn = new Button("Flip");
        Button nextBtn = new Button(">");

        Label menuTitleLabel = new Label("Menu");
        menuTitleLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");

        Button menuBtn = new Button("Option 1");


        // Organize components in 
        mainBox.setLeft(contentBox);
        mainBox.setRight(menuBox);
        contentBox.getChildren().addAll(flashcardBox, controlsBox);
        flashcardBox.getChildren().add(flashcardText);
        controlsBox.getChildren().addAll(previousBtn, flipBtn, nextBtn);
        menuBox.getChildren().addAll(menuTitleLabel, menuBtn);


        // Button reactions


        // Set up and display window
        Scene scene = new Scene(mainBox);
        stage.setScene(scene);
        stage.setTitle("Flashcards");
        stage.show();
    }
}