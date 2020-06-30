package cz.cvut.fel.pjv.chessproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * class for encapsulating all JavaFX stuff of side menu panel and separating
 * them from App class
 */
public class Menu {

    /**
     * side menu panel JavaFX stuff
     */
    public static Pane pane = new Pane();
    public static VBox panel = new VBox();
    public static Button newGameAI = new Button("Human vs Computer");
    public static Button newGameReal = new Button("Human vs Human");
    public static Button customGame = new Button("Custom Game");
    public static Button customizeBoard = new Button("Create Custom Board");
    public static Button saveNormal = new Button("Save game");
    public static Button loadNormal = new Button("Load game");
    public volatile static TextField seconds = new TextField("30");
    public static Button setTime = new Button("Set time");
    public volatile static TextField terminal = new TextField("</");
    public static VBox multiplayer = new VBox();
    public static HBox mpbuttons = new HBox();
    public static Button black = new Button("BLACK");
    public static Button white = new Button("WHITE");
    public static Label mpLabel = new Label("MULTIPLAYER");

    public Menu() {
        //all clickable buttons
        newGameAI.setMaxWidth(160);
        newGameReal.setMaxWidth(160);
        customGame.setMaxWidth(160);
        customizeBoard.setMaxWidth(160);
        saveNormal.setMaxWidth(160);
        loadNormal.setMaxWidth(160);
        setTime.setMaxWidth(160);

        //TextFields
        seconds.setAlignment(Pos.CENTER);
        seconds.setMaxWidth(160);
        seconds.setMinHeight(60);
        seconds.setFont(Font.font(20));
        terminal.setMaxWidth(160);
        terminal.setMinHeight(100);
        terminal.setEditable(false);
        terminal.setOpacity(0.5);
        terminal.setAlignment(Pos.TOP_LEFT);
        seconds.setOpacity(0.5);

        //multiplayer section
        mpLabel.setAlignment(Pos.CENTER);
        mpLabel.setOpacity(0.7);
        multiplayer.setAlignment(Pos.CENTER);
        mpbuttons.setSpacing(30);
        mpbuttons.getChildren().addAll(white, black);
        multiplayer.setSpacing(15);
        multiplayer.getChildren().addAll(mpLabel, mpbuttons);
        black.setOpacity(0.7);
        white.setOpacity(0.7);

        mpLabel.setStyle("-fx-text-fill: black");
        terminal.setStyle("-fx-background-color:black;-fx-text-fill: white");
        newGameAI.setStyle("-fx-background-color: #C3D7DF;");
        newGameReal.setStyle("-fx-background-color: #C3D7DF;");
        customGame.setStyle("-fx-background-color: #C3D7DF;");
        setTime.setStyle("-fx-background-color: #C3D7DF;");
        customizeBoard.setStyle("-fx-background-color: #C3D7DF;");
        saveNormal.setStyle("-fx-background-color: #C3D7DF;");
        loadNormal.setStyle("-fx-background-color: #C3D7DF;");
        black.setStyle("-fx-background-color: black; -fx-text-fill: #C3D7DF");
        white.setStyle("-fx-background-color: #C3D7DF;");

        panel.setSpacing(40);
        panel.setPadding(new Insets(20, 20, 20, 20));
        panel.getChildren().addAll(newGameReal, newGameAI, customGame, customizeBoard,
                saveNormal, loadNormal, multiplayer, seconds, setTime, terminal
        );
        pane.setMinWidth(200);
        pane.getChildren().add(panel);
        pane.setStyle("-fx-background-color: #83B4B6;");

    }

}
