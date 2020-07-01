package cz.cvut.fel.pjv.chessproject;

import cz.cvut.fel.pjv.chessproject.Networking.Sender;
import cz.cvut.fel.pjv.chessproject.Networking.Reciever;
import com.google.gson.Gson;
import cz.cvut.fel.pjv.chessproject.Data.DataParser;
import cz.cvut.fel.pjv.chessproject.Model.Board;
import cz.cvut.fel.pjv.chessproject.Model.Game;
import cz.cvut.fel.pjv.chessproject.Data.GameData;
import cz.cvut.fel.pjv.chessproject.Enums.GameStatus;
import cz.cvut.fel.pjv.chessproject.View.BoardView;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Main controller class providing methods of GUI interaction with the model and
 * view. 
 */
public class App extends Application {

    private Game game;
    private BoardView boardView;
    private StatusWrapper status = new StatusWrapper();
    private Board customBoard = new Board(true);
    private Time time = new Time(30);
    private boolean customization = false;
    private boolean moveFrom = false;
    private boolean multiplayer = true;

    private boolean sender;

    private static final Logger lgr = Logger.getLogger(Game.class.getName());

    Stage window;

    @Override
    public void start(Stage primaryStage) {
        //JavaFX stuff
        window = primaryStage;
        window.setTitle("CHESS!");
        window.setWidth(1000);
        window.setHeight(825);
        window.centerOnScreen();
        BorderPane Bpane = new BorderPane();
        Scene mainScene = new Scene(Bpane);
        window.setScene(mainScene);
        Menu s = new Menu();
        Bpane.setRight(Menu.pane);

        //create basic game on start
        createNewGame(Bpane, false, new Board());

        //_______BUTTON_HANDLING_________________________________________________
        Menu.newGameReal.setOnAction(e -> createNewGame(Bpane, false, new Board()));
        Menu.newGameAI.setOnAction(e -> createNewGame(Bpane, true, new Board()));
        Menu.customGame.setOnAction(e -> createNewGame(Bpane, false, customBoard));
        Menu.customizeBoard.setOnAction(e -> initCustomCreation(Bpane));
        Menu.saveNormal.setOnAction(e -> game.saveGame());
        Menu.loadNormal.setOnAction(e -> loadSaved(Bpane));
        Menu.setTime.setOnAction(e -> setTime());
        Menu.black.setOnAction(e -> initBlack());
        Menu.white.setOnAction(e -> initWhite());

        //depending on current state of action process game move or some board placing
        window.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent t) -> {
            boardView.reDraw();
            int x = (int) t.getSceneX() / (BoardView.TILE_DIM);
            int y = (int) t.getSceneY() / (BoardView.TILE_DIM);

            if (game.IsRunning()) {
                processMove(x, y);
            } else if (customization) {
                processSet(x, y);
            }
        });
    }

    //set in multiplayer receiver mode
    private void initBlack() {
        multiplayer = true;
        sender = false;
        Menu.black.setDisable(true);
        Menu.white.setDisable(true);
        game.setIsRunning(false);
        receive();
    }

    //set in multiplayer sender mode
    private void initWhite() {
        multiplayer = true;
        Menu.black.setDisable(true);
        Menu.white.setDisable(true);
        sender = true;
        setTime();
    }

    private void send() {
        Thread t = new Thread(new Sender(game, sender));
        game.setIsRunning(false);
        t.start();
        time.setIsOn(false);
        receive();
    }

    private void receive() {
        Thread t = new Thread(new Reciever(game, status, sender, boardView, time));
        t.start();
    }

    //creates new game
    private void createNewGame(BorderPane Bpane, boolean AIgame, Board board) {
        Menu.setTime.setDisable(false);
        Menu.seconds.setDisable(false);
        time.reset();
        time.setIsOn(false);
        customization = false;
        moveFrom = false;
        multiplayer = false;
        game = new Game(AIgame, board);
        boardView = new BoardView(window, game.getBoard());
        boardView.initDrawingSurface(Bpane);
        status.setGs(game.getCurrentStatus());
        //is here for custom boards (so it excludes weird ideas like game without king
        checkStatus();
    }

    private void initCustomCreation(BorderPane Bpane) {
        customization = true;
        game.setIsRunning(false);
        customBoard = new Board(true);
        boardView = new BoardView(window, customBoard);
        boardView.initDrawingSurface(Bpane);
    }

    private void setTime() {
        //gets the input from textfield
        try {
            int n = Integer.parseInt(Menu.seconds.getText());
            time.setSeconds(n);
            Menu.seconds.setStyle("-fx-text-fill: green");
            Menu.terminal.setText(">_");
        } catch (NumberFormatException e) {
            Menu.terminal.setText(">_invalid time");
            return;
        }
        //starts counting from the input
        time.setIsOn(true);
        Thread clock = new Clock(time);
        clock.start();
        Menu.setTime.setDisable(true);
        Menu.seconds.setDisable(true);
    }

    private void processSet(int x, int y) {
        CustomBoard cs = new CustomBoard(boardView);
        cs.set(customBoard, x, y);
    }

    private void processMove(int x, int y) {

        //ran out of time
        if (time.getSeconds() < 1) {
            game.setIsRunning(false);
            boardView.drawWinner(!game.getCurrentPlayer().isWhite());
            return;
        }
        //if AI 
        if (game.getCurrentPlayer().isAI()) {
            if (game.createAImove()) {
                status.setGs(game.performGameCycle());
                boardView.reDraw();
                checkStatus();

            }
        }
        //moveFrom is not created -> should be created
        if (!moveFrom) {
            if (game.createMoveFrom(x, y)) {
                boardView.drawMove(x, y, true);
                boardView.drawPossibilities(game.getPossibleMoves());
                moveFrom = true;
            } else {
                boardView.drawMove(x, y, false);
            }
            /* moveFrom is created, you are free to choose destination*/
        } else {
            if (game.createMoveTo(x, y)) {
                status.setGs(game.performGameCycle());
                boardView.reDraw();
                checkStatus();
                if (multiplayer) {
                    send();
                }

                //if AI then it moves here silently
                if (game.getCurrentPlayer().isAI()) {
                    if (game.createAImove()) {
                        status.setGs(game.performGameCycle());
                        boardView.reDraw();
                        checkStatus();

                    }
                }

                //each move resets clock
                time.reset();

            } else {
                boardView.drawMove(x, y, false);
            }
            //this returns you back to "create FROM" phase
            moveFrom = false;
        }

    }

    //handles game status 
    private void checkStatus() {
        Menu.terminal.setText(">_");
        if (status.getGs() == GameStatus.IS_RUNNING || status.getGs() == GameStatus.CHECK) {
            return;
        }
        if (status.getGs() == GameStatus.PROMOTING) {
            game.swapTurns();
            Promotion prom = new Promotion(game.getCurrentPlayer().isWhite());
            prom.promote(game, boardView);
            return;
        }
        //results that end the game
        time.reset();
        time.setIsOn(false);
        game.setIsRunning(false);
        if (status.getGs() == GameStatus.CHECKMATE) {
            boardView.drawWinner(!game.getCurrentPlayer().isWhite());

            return;
        }
        if (status.getGs() == GameStatus.STALEMATE) {
            boardView.drawStaleMate();
            return;
        }
        if (status.getGs() == GameStatus.UNPLAYABLE) {
            boardView.drawUnplayable();
            Menu.terminal.setText(">_UNPLAYBLE");
        }

    }

    //loads data class and transform it into playable situation (game)
    private void loadSaved(BorderPane Bpane) {
        Gson gson = new Gson();
        GameData gd = new GameData();
        try {
            gd = gson.fromJson(new FileReader("src/main/resources/data/saved_game.json"), GameData.class);
        } catch (FileNotFoundException ex) {
            lgr.log(Level.SEVERE, "Cannot load the game.", ex.getMessage());
            Menu.terminal.setText(">_no such file");
            return;
        }
        DataParser dp = new DataParser(gd);
        Board board = dp.setBoard(new Board(true));
        createNewGame(Bpane, gd.isIsAIGame(), board);

        //sets correct turn
        if (!gd.isCurrentPLayerIsWhite()) {
            game.swapTurns();
        }
        status.setGs(gd.getStatus());
        //checkStatus();

    }

    public static void main(String[] args) {
        launch();
    }
}
