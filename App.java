import javafx.application.Application;
import javafx.geometry.Insets;
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
        Text flashcardText = new Text();

        HBox deckDescriptionBox = new HBox();
        deckDescriptionBox.setAlignment(Pos.CENTER);

        Text deckTitleText = new Text("(deck title)");
        Text seperatorText = new Text("    |    ");
        Text deckProgressText = new Text("0/0");
        deckTitleText.setStyle("-fx-font-size: 13");
        seperatorText.setStyle("-fx-font-size: 13");
        deckProgressText.setStyle("-fx-font-size: 13");

        HBox controlsBox = new HBox(5);
        controlsBox.setAlignment(Pos.CENTER);

        Button correctBtn = new Button("✓");
        Text correctText = new Text("0");
        HBox.setMargin(correctText, new Insets(0, 60, 0, 0));
    
        Button incorrectBtn = new Button("X");
        Text incorrectText = new Text("0");
        HBox.setMargin(incorrectText, new Insets(0, 0, 0, 60));

        Button previousBtn = new Button("<");
        Button flipBtn = new Button("Flip");
        Button nextBtn = new Button(">");

        Label menuTitleLabel = new Label("Menu");
        menuTitleLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");

        Button resetScoreBtn = new Button("Reset Score");
        Button uploadBtn = new Button("Upload Deck");

        Label premadeDecksLabel = new Label("Premade decks:");
        VBox.setMargin(premadeDecksLabel, new Insets(20, 0, 0, 0));

        Button timesTablesDeckBtn = new Button("Times Tables");
        Button triviaDeckBtn = new Button("Trivia");

        VBox messageBox = new VBox();
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setStyle("-fx-padding: 10");
        Text messageText = new Text("No flashcards open.");


        // Organize components in 
        mainBox.setLeft(contentBox);
        mainBox.setRight(menuBox);
        mainBox.setBottom(messageBox);
        flashcardBox.getChildren().add(flashcardText);
        messageBox.getChildren().add(messageText);
        deckDescriptionBox.getChildren().addAll(deckTitleText, seperatorText, deckProgressText);
        contentBox.getChildren().addAll(deckDescriptionBox, flashcardBox, controlsBox);
        controlsBox.getChildren().addAll(correctBtn, correctText, previousBtn, flipBtn, nextBtn, incorrectText, incorrectBtn);
        menuBox.getChildren().addAll(menuTitleLabel, resetScoreBtn, uploadBtn, premadeDecksLabel, timesTablesDeckBtn, triviaDeckBtn);


        // Button reactions


        // Set up and display window
        Scene scene = new Scene(mainBox);
        stage.setTitle("Flashcards App");
        stage.setScene(scene);
        stage.show();
    }
}