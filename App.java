import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;



public class App extends Application {
    int correctAnswers = 0;
    int incorrectAnswers = 0;
    int progress = 0;
    String currentDeck;
    String[] questions;
    String[] answers;
    ArrayList<String> redoQuestions = new ArrayList<>();
    ArrayList<String> redoAnswers = new ArrayList<>();

    Text correctText;
    Text incorrectText;
    Text deckTitleText;
    Text progressText;
    Text messageText;
    Text flashcardText;
    File userDeckFile;
    File userStatsFile;
    
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
        flashcardText = new Text();

        HBox deckDescriptionBox = new HBox();
        deckDescriptionBox.setAlignment(Pos.CENTER);

        deckTitleText = new Text("(deck title)");
        Text seperatorText = new Text("    |    ");
        progressText = new Text("0/0");
        deckTitleText.setStyle("-fx-font-size: 13");
        seperatorText.setStyle("-fx-font-size: 13");
        progressText.setStyle("-fx-font-size: 13");

        HBox controlsBox = new HBox(5);
        controlsBox.setAlignment(Pos.CENTER);

        Button correctBtn = new Button("✓");
        correctText = new Text("0");
        HBox.setMargin(correctText, new Insets(0, 50, 0, 0));
    
        Button incorrectBtn = new Button("X");
        incorrectText = new Text("0");
        HBox.setMargin(incorrectText, new Insets(0, 0, 0, 50));

        Button previousBtn = new Button("<");
        Button flipBtn = new Button("Flip");
        Button nextBtn = new Button(">");

        Label menuTitleLabel = new Label("Menu");
        menuTitleLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");

        Button resetScoreBtn = new Button("Reset Score");
        Button uploadBtn = new Button("Upload Deck");
        Button redoIncorrectBtn = new Button("Redo Incorrect Answers");
        Button saveStatsBtn = new Button("Save Stats");

        Label premadeDecksLabel = new Label("Premade decks:");
        VBox.setMargin(premadeDecksLabel, new Insets(20, 0, 0, 0));

        Button timesTablesDeckBtn = new Button("Times Tables");
        Button triviaDeckBtn = new Button("Trivia");

        VBox messageBox = new VBox();
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setStyle("-fx-padding: 10");
        messageText = new Text("Welcome to Flashcards App! Any errors will appear here.");


        // Organize components in 
        mainBox.setLeft(contentBox);
        mainBox.setRight(menuBox);
        mainBox.setBottom(messageBox);
        flashcardBox.getChildren().add(flashcardText);
        messageBox.getChildren().add(messageText);
        deckDescriptionBox.getChildren().addAll(deckTitleText, seperatorText, progressText);
        contentBox.getChildren().addAll(deckDescriptionBox, flashcardBox, controlsBox);
        controlsBox.getChildren().addAll(correctBtn, correctText, previousBtn, flipBtn, nextBtn, incorrectText, incorrectBtn);
        menuBox.getChildren().addAll(menuTitleLabel, resetScoreBtn, uploadBtn, redoIncorrectBtn, saveStatsBtn, premadeDecksLabel, timesTablesDeckBtn, triviaDeckBtn);


        // Button reactions
        correctBtn.setOnAction(event -> updateCorrectAnswers());
        incorrectBtn.setOnAction(event -> {
            try {
                updateIncorrectAnswers(questions[progress-1], answers[progress-1]);
            } catch (NullPointerException npe) {
                messageText.setText("No deck has been selected.");
            }
        });
        resetScoreBtn.setOnAction(event -> resetScore());
        timesTablesDeckBtn.setOnAction(event -> setDeck("Times Tables Deck.txt"));
        triviaDeckBtn.setOnAction(event -> setDeck("Trivia Deck.txt"));
        flipBtn.setOnAction(event -> flipCard());
        nextBtn.setOnAction(event -> nextCard());
        previousBtn.setOnAction(event -> previousCard());
        uploadBtn.setOnAction(event -> uploadDeck());
        redoIncorrectBtn.setOnAction(event -> redoIncorrectAnswers());
        saveStatsBtn.setOnAction(event -> saveStats());


        // Set up and display window
        Scene scene = new Scene(mainBox);
        stage.setTitle("Flashcards App");
        stage.setScene(scene);
        stage.show();
    }


    void updateCorrectAnswers() {
        if (questions == null) {
            messageText.setText("No deck has been selected.");
            return;
            
        } else {
            correctAnswers++;
            correctText.setText(Integer.toString(correctAnswers));
        }
    }


    void updateIncorrectAnswers(String question, String answer) {
        incorrectAnswers++;
        incorrectText.setText(Integer.toString(incorrectAnswers));

        if (!redoQuestions.contains(question)) {
            redoQuestions.add(question);
        }
        if (!redoAnswers.contains(answer)) {
            redoAnswers.add(answer);
        }
    }


    void resetScore() {
        correctAnswers = 0;
        incorrectAnswers = 0;
        correctText.setText("0");
        incorrectText.setText("0");
        redoQuestions.clear();
        redoAnswers.clear();
    }


    void setDeck(String deckFileName) {
        File deckFile = new File(deckFileName);

        try {
            Scanner sc = new Scanner(deckFile);
            deckTitleText.setText(sc.nextLine());

            questions = sc.nextLine().split(",");
            answers = sc.nextLine().split(",");
            progress = 1;

            sc.close();

            progressText.setText("1/" + questions.length);
            flashcardText.setText(questions[0]);


        } catch (FileNotFoundException fnfe) {
            messageText.setText("File not found.");
            return;
        }
    }


    void flipCard() {
        try {
            if (flashcardText.getText().equals(questions[progress-1])) {
                flashcardText.setText(answers[progress-1]);
            } else {
                flashcardText.setText(questions[progress-1]);
            }

        } catch (NullPointerException npe) {
            messageText.setText("No deck has been selected.");
            return;
        }
    }


    void nextCard() {
        try {
            if (progress == questions.length) {
                progress = 1;
            } else {
                progress++;
            }
            progressText.setText(Integer.toString(progress) + "/" + questions.length);
            flashcardText.setText(questions[progress-1]);

        } catch (NullPointerException npe) {
            messageText.setText("No deck has been selected.");
            return;
        }
    }


    void previousCard() {
        try {
            if (progress == 1) {
                progress = questions.length;
            } else {
                progress--;
            }
            progressText.setText(Integer.toString(progress) + "/" + questions.length);
            flashcardText.setText(questions[progress-1]);

        } catch (NullPointerException npe) {
            messageText.setText("No deck has been selected.");
            return;
        }
    }


    void uploadDeck() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("DECK FILE FORMAT: Line 1 - deck title, Line 2 - questions seperated by commas, Line 3 - answers seperated by commas");
        userDeckFile = fileChooser.showOpenDialog(null);

        if (userDeckFile != null) {
            if (checkDeck(userDeckFile)) {
                setDeck(userDeckFile.getName());
            }
        }
    }

    
    Boolean checkDeck(File deckFile) {
        try {
            Scanner sc = new Scanner(deckFile);
            sc.nextLine();
            String[] checkQuestions = sc.nextLine().split(",");
            String [] checkAnswers = sc.nextLine().split(",");
            sc.close();

            Boolean valid = true;

            if (checkQuestions.length != checkAnswers.length) {
                messageText.setText("Uneven amount of questions and answers.");
                valid = false;
            }
            return valid;

        } catch (FileNotFoundException fnfe) {
            messageText.setText("File not found.");
            return false;

        } catch (NoSuchElementException nsee) {
            messageText.setText("Invalid file format: not enough lines.");
            return false;
        }
    }


    void redoIncorrectAnswers() {
        if (redoQuestions.size() == 0) {
            messageText.setText("You don't have any incorrect answers.");
            return;

        } else {
            progress = 1;
            questions = new String[redoQuestions.size()];
            questions = redoQuestions.toArray(questions);
            answers = new String[redoAnswers.size()];
            answers = redoAnswers.toArray(answers);
            deckTitleText.setText("Redoing Incorrect Answers");
            progressText.setText("1/" + questions.length);
            flashcardText.setText(questions[0]);
        }
    }


    void saveStats() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Tune File");
            userStatsFile = fileChooser.showSaveDialog(null);
            PrintWriter fileWriter = new PrintWriter(userStatsFile);

            fileWriter.println("Correct answers: " + correctAnswers);
            fileWriter.println("Incorrect answers: " + incorrectAnswers);
            fileWriter.println("\nIncorrect questions:");

            for (int i = 0; i < redoQuestions.size(); i++) {
                fileWriter.println("   " + redoQuestions.get(i));
            }

            fileWriter.close();

        } catch (FileNotFoundException fnfe) {
            messageText.setText("File not found.");
            return;
        }
    }
}