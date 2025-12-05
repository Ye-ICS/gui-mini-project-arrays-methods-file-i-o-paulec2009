import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    ArrayList<String> userDeckQuestions = new ArrayList<>();
    ArrayList<String> userDeckAnswers = new ArrayList<>();

    Text correctText;
    Text incorrectText;
    Text deckTitleText;
    Text progressText;
    Text messageText;
    Text flashcardText;
    File userDeckFile;
    File userCreatedDeckFile;
    File userStatsFile;
    TextField questionField;
    TextField answerField;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) {

        // Create main boxes
        BorderPane mainBox = new BorderPane();

        VBox contentBox = new VBox(5);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle("-fx-padding: 10");

        VBox menuBox = new VBox(5);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle("-fx-padding: 10");

        VBox createDeckBox = new VBox(5);
        createDeckBox.setAlignment(Pos.CENTER);
        createDeckBox.setStyle("-fx-padding: 10");

        VBox messageBox = new VBox();
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setStyle("-fx-padding: 10");


        // Create sub-boxes for organizing elements inside the main boxes
        VBox flashcardBox = new VBox();
        flashcardBox.setAlignment(Pos.CENTER);
        flashcardBox.setStyle("-fx-border-color: black");
        flashcardBox.setMinWidth(300);
        flashcardBox.setMinHeight(250);
        
        HBox deckDescriptionBox = new HBox();
        deckDescriptionBox.setAlignment(Pos.CENTER);

        HBox controlsBox = new HBox(5);
        controlsBox.setAlignment(Pos.CENTER);

        HBox questionFieldBox = new HBox();
        questionFieldBox.setAlignment(Pos.CENTER);

        HBox answerFieldBox = new HBox();
        answerFieldBox.setAlignment(Pos.CENTER);

        HBox createDeckControlsBox = new HBox(10);
        createDeckControlsBox.setAlignment(Pos.CENTER);


        // Create elements to place inside boxes
        flashcardText = new Text();

        deckTitleText = new Text("(deck title)");
        Text seperatorText = new Text("    |    ");
        progressText = new Text("0/0");
        deckTitleText.setStyle("-fx-font-size: 13");
        seperatorText.setStyle("-fx-font-size: 13");
        progressText.setStyle("-fx-font-size: 13");

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

        Button resetBtn = new Button("Reset Progress");
        Button shuffleDeckBtn = new Button("Shuffle Deck");
        Button uploadBtn = new Button("Upload Deck");
        Button redoIncorrectBtn = new Button("Redo Incorrect Answers");
        Button saveStatsBtn = new Button("Save Stats");

        Label premadeDecksLabel = new Label("Premade decks:");
        VBox.setMargin(premadeDecksLabel, new Insets(15, 0, 0, 0));

        Button timesTablesDeckBtn = new Button("Times Tables");
        Button triviaDeckBtn = new Button("Trivia");
        
        messageText = new Text("Welcome to Flashcards App!");
        VBox.setMargin(messageBox, new Insets(0, 0, 15, 0));

        Label createDeckLabel = new Label("Create Your Own Deck");
        createDeckLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        VBox.setMargin(createDeckLabel, new Insets(15, 0, 5, 0));
        
        Label questionFieldLabel = new Label("Question: ");
        Label answerFieldLabel = new Label("Answer: ");

        questionField = new TextField();
        questionField.setMinWidth(400);
        questionField.setPromptText(" type a question to add to your deck here");
        
        answerField = new TextField();
        answerField.setMinWidth(410);
        answerField.setPromptText(" type the answer to that question here");

        Button addCardBtn = new Button("Add to Deck");
        Button clearDeckBtn = new Button("Clear Deck");
        Button setDeckBtn = new Button("Set Deck");
        Button saveDeckBtn = new Button("Save Deck to File");


        // Organize components in boxes
        mainBox.setLeft(contentBox);
        mainBox.setRight(menuBox);
        mainBox.setBottom(createDeckBox);
        mainBox.setTop(messageBox);
        flashcardBox.getChildren().add(flashcardText);
        messageBox.getChildren().add(messageText);
        questionFieldBox.getChildren().addAll(questionFieldLabel, questionField);
        answerFieldBox.getChildren().addAll(answerFieldLabel, answerField);
        createDeckControlsBox.getChildren().addAll(addCardBtn, setDeckBtn, clearDeckBtn, saveDeckBtn);
        deckDescriptionBox.getChildren().addAll(deckTitleText, seperatorText, progressText);
        contentBox.getChildren().addAll(deckDescriptionBox, flashcardBox, controlsBox);
        createDeckBox.getChildren().addAll(createDeckLabel, questionFieldBox, answerFieldBox, createDeckControlsBox);
        controlsBox.getChildren().addAll(correctBtn, correctText, previousBtn, flipBtn, nextBtn, incorrectText, incorrectBtn);
        menuBox.getChildren().addAll(menuTitleLabel, resetBtn, shuffleDeckBtn, uploadBtn, redoIncorrectBtn, saveStatsBtn, premadeDecksLabel, timesTablesDeckBtn, triviaDeckBtn);


        // Button reactions
        incorrectBtn.setOnAction(event -> {
            try {
                updateIncorrectAnswers(questions[progress-1], answers[progress-1]);
            } catch (NullPointerException npe) {
                messageText.setText("No deck has been selected.");
            }
        });
        correctBtn.setOnAction(event -> updateCorrectAnswers());
        resetBtn.setOnAction(event -> resetProgress());
        timesTablesDeckBtn.setOnAction(event -> setDeck("Times Tables Deck.txt"));
        triviaDeckBtn.setOnAction(event -> setDeck("Trivia Deck.txt"));
        flipBtn.setOnAction(event -> flipCard());
        nextBtn.setOnAction(event -> nextCard());
        previousBtn.setOnAction(event -> previousCard());
        uploadBtn.setOnAction(event -> uploadDeck());
        redoIncorrectBtn.setOnAction(event -> redoIncorrectAnswers());
        saveStatsBtn.setOnAction(event -> saveStats());
        shuffleDeckBtn.setOnAction(event -> shuffleDeck());
        addCardBtn.setOnAction(event -> addUserCard());
        clearDeckBtn.setOnAction(event -> clearUserDeck());
        setDeckBtn.setOnAction(event -> setUserDeck());
        saveDeckBtn.setOnAction(event -> saveUserDeck());


        // Set up and display window
        Scene scene = new Scene(mainBox);
        stage.setTitle("Flashcards App");
        stage.setScene(scene);
        stage.show();
    }



    /**
     * Adds 1 to the correct answers score and updates display.
     */
    void updateCorrectAnswers() {
        if (questions == null) {
            messageText.setText("No deck has been selected.");
            return;
            
        } else {
            correctAnswers++;
            correctText.setText(Integer.toString(correctAnswers));
        }
    }


    /**
     * Adds 1 to the incorrect answers score and updates display.
     * Also adds the incorrect question to the list of questions to redo.
     */
    void updateIncorrectAnswers(String question, String answer) {
        incorrectAnswers++;
        incorrectText.setText(Integer.toString(incorrectAnswers));

        if (!redoQuestions.contains(question)) {
            redoQuestions.add(question);
            redoAnswers.add(answer);
        }
    }


    /**
     * Resets score and questions to redo.
     */
    void resetProgress() {
        correctAnswers = 0;
        incorrectAnswers = 0;
        correctText.setText("0");
        incorrectText.setText("0");
        redoQuestions.clear();
        redoAnswers.clear();

        messageText.setText("Your score and incorrect answers have been reset.");
    }


    /**
     * Sets the deck display to a deck from a specified file.
     */
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

            messageText.setText("'" + deckTitleText.getText() + "' has been set to your current deck.");

        } catch (FileNotFoundException fnfe) {
            messageText.setText("File not found.");
            return;
        }
    }


    /**
     * Switches display between the current card's question and answer.
     */
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


    /**
     * Switches display to the next card in the deck.
     */
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


    /**
     * Switches display to the previous card in the deck.
     */
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


    /**
     * Uploads a deck from a file.
     * File must have the following format: Line 1 - deck title, Line 2 - questions seperated by commas, Line 3 - answers seperated by commas.
     */
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

    
    /**
     * Checks a deck for any errors before setting it to the display.
     */
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


    /**
     * Sets display to a deck of the questions the user has answered incorrectly during their current score.
     */
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
            deckTitleText.setText("Incorrect Answers");
            progressText.setText("1/" + questions.length);
            flashcardText.setText(questions[0]);

            messageText.setText("You are currently redoing your incorrect answers.");
        }
    }


    /**
     * Saves the user's current score and incorrect answers to a chosen file.
     */
    void saveStats() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file to save stats in");
            userStatsFile = fileChooser.showSaveDialog(null);
            PrintWriter fileWriter = new PrintWriter(userStatsFile);

            fileWriter.println("Correct answers: " + correctAnswers);
            fileWriter.println("Incorrect answers: " + incorrectAnswers);
            fileWriter.println("\nIncorrect answers:");

            for (int i = 0; i < redoQuestions.size(); i++) {
                fileWriter.println("   " + redoQuestions.get(i));
            }
            fileWriter.close();

            messageText.setText("Your stats have been saved to '" + userStatsFile.getName() + "'.");

        } catch (FileNotFoundException fnfe) {
            messageText.setText("File not found.");
            return;
        } catch (NullPointerException npe) {
            return;
        }
    }


    /**
     * Shuffles the order of the questions in the current deck.
     */
    void shuffleDeck() {
        try {
            int randomInt;
            String temp;

            for (int i = 0; i < questions.length-1; i++) {
                randomInt = ThreadLocalRandom.current().nextInt(i, questions.length-1);

                temp = questions[randomInt];
                questions[randomInt] = questions[i];
                questions[i] = temp;

                temp = answers[randomInt];
                answers[randomInt] = answers[i];
                answers[i] = temp;
            }
            flashcardText.setText(questions[progress-1]);

            messageText.setText("The current deck has been shuffled.");

        } catch (NullPointerException npe) {
            messageText.setText("No deck has been selected.");
            return;
        }
    }


    /**
     * Adds the provided question to the user's deck.
     */
    void addUserCard() {
        if (questionField.getText() == "" || answerField.getText() == "") {
            messageText.setText("Please type something in the question box and the answer box.");
            return;

        } else {
            userDeckQuestions.add(questionField.getText());
            questionField.clear();
            userDeckAnswers.add(answerField.getText());
            answerField.clear();
            messageText.setText("Card has been added to your deck.");
        }
    }


    /**
     * Clears all cards in the user's deck.
     */
    void clearUserDeck() {
        userDeckQuestions.clear();
        userDeckAnswers.clear();
        messageText.setText("Your deck has been cleared.");
    }


    /**
     * Sets the user deck to the display.
     */
    void setUserDeck() {
        if (userDeckQuestions.size() == 0) {
            messageText.setText("Your deck is empty.");
            return;

        } else {
            progress = 1;
            questions = new String[userDeckQuestions.size()];
            questions = userDeckQuestions.toArray(questions);
            answers = new String[userDeckAnswers.size()];
            answers = userDeckAnswers.toArray(answers);
            deckTitleText.setText("Your Deck");
            progressText.setText("1/" + questions.length);
            flashcardText.setText(questions[0]);

            messageText.setText("Your deck has been set to the current deck.");
        }
    }


    /**
     * Saves the user's deck to a chosen file.
     */
    void saveUserDeck() {
        if (userDeckQuestions.size() == 0) {
            messageText.setText("Your deck is empty.");
            return;

        } else {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select file to save deck in");
                userCreatedDeckFile = fileChooser.showSaveDialog(null);
                PrintWriter fileWriter = new PrintWriter(userCreatedDeckFile);

                fileWriter.println("Your Deck");

                for (int i = 0; i < userDeckQuestions.size()-1; i++) {
                    fileWriter.print(userDeckQuestions.get(i) + ",");
                }
                fileWriter.println(userDeckQuestions.get(userDeckQuestions.size()-1));

                for (int i = 0; i < userDeckAnswers.size()-1; i++) {
                    fileWriter.print(userDeckAnswers.get(i) + ",");
                }
                fileWriter.print(userDeckAnswers.get(userDeckAnswers.size()-1));

                fileWriter.close();

                messageText.setText("Your deck has been saved to the chosen file.");

            } catch (FileNotFoundException fnfe) {
                messageText.setText("File not found.");
                return;
            } catch (NullPointerException npe) {
                return;
            }
        }
    }
}