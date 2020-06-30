package cz.cvut.fel.pjv.chessproject.View;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import cz.cvut.fel.pjv.chessproject.Pieces.Piece;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Class for rendering chess board.
 */
public class BoardView {

    private final Stage stage;
    private Canvas canvas;
    private final Board board;

    /**
     * Dimension of tile on the board.
     */
    public static final int TILE_DIM = 100;

    public BoardView(Stage stage, Board board) {
        this.stage = stage;
        this.board = board;
    }

    public BoardView(Stage stage) {
        this.stage = stage;
        this.board = null;
    }

    /**
     * Prepares stage for drawing the board.
     *
     * @param Bpane input to put created layout onto.
     */
    public void initDrawingSurface(BorderPane Bpane) {
        canvas = new Canvas(800, 800);
        Pane boardPane = new Pane(canvas);
        Bpane.setCenter(boardPane);
        stage.setResizable(false);
        stage.show();
        reDraw();
    }

    /**
     * Redraws current state of board.
     */
    public void reDraw() {
        drawTable();
        drawPieces();
    }

    /**
     * Draws the board according to standard chess layout.
     */
    public void drawTable() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image light = new Image("backgroundWhite.png");
        Image dark = new Image("pic.png");

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((y % 2) == 0) {
                    if ((x % 2) == 0) {
                        gc.drawImage(light, x * TILE_DIM, y * TILE_DIM);
                    } else {
                        gc.drawImage(dark, x * TILE_DIM, y * TILE_DIM);
                    }
                } else {
                    if ((x % 2) == 0) {
                        gc.drawImage(dark, x * TILE_DIM, y * TILE_DIM);
                    } else {
                        gc.drawImage(light, x * TILE_DIM, y * TILE_DIM);
                    }
                }

            }
        }
    }

    /**
     * Fills [x,y] spot on board in red or green color, depending on if the move
     * is correct
     *
     * @param isCorrect boolean variable storing information if the move is
     * correct or not
     */
    public void drawMove(int x, int y, boolean isCorrect) {
        if (board.isOutOfBounds(x, y)) {
            return;
        }
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image sign = new Image(isCorrect ? "green.png" : "red.png");
        Piece piece = board.getBox(x, y).getCurrentPiece();
        gc.drawImage(sign, x * TILE_DIM, y * TILE_DIM);
        if (piece != null) {
            gc.drawImage(new Image(piece.getImageName()), x * TILE_DIM + 20, y * TILE_DIM + 20);
        }
    }

    /**
     * Draws green square on [x,y] positions of possible moves
     *
     * @param positions is a list of possible moves to clicked piece ( generated
     * in @Game class)
     */
    public void drawPossibilities(List<int[]> positions) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int[] pos : positions) {
            gc.drawImage(new Image("green3.png"), pos[0] * TILE_DIM + 10, pos[1] * TILE_DIM + 10);
            Piece piece = board.getBox(pos[0], pos[1]).getCurrentPiece();
            if (piece != null) {
                gc.drawImage(new Image(piece.getImageName()), pos[0] * TILE_DIM + 20, pos[1] * TILE_DIM + 20);
            }
        }

    }

    /**
     * Iterates through the board and draw each piece.
     */
    public void drawPieces() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = board.getBox(x, y).getCurrentPiece();
                if (piece != null) {
                    gc.drawImage(new Image(piece.getImageName()), x * TILE_DIM + 20, y * TILE_DIM + 20);
                }

            }
        }

    }

    /**
     * Draws text onto the center of the screen according to the winners color
     *
     * @param white boolean to check if winners color of pieces is white or
     * black
     */
    public void drawWinner(boolean white) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (white) {
            gc.drawImage(new Image("whiteWins.png"), TILE_DIM, 3.7 * TILE_DIM);
        } else {
            gc.drawImage(new Image("blackWins.png"), TILE_DIM, 3.7 * TILE_DIM);
        }
    }

    /**
     * Draw Stalemate result text onto the center of the screen.
     */
    public void drawStaleMate() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(new Image("stalemate.png"), TILE_DIM, 3.7 * TILE_DIM);
    }

    /**
     * Draw unplayable result text onto the center of the screen.
     */
    public void drawUnplayable() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(new Image("unplayable.png"), TILE_DIM, 3.7 * TILE_DIM);
    }
}
