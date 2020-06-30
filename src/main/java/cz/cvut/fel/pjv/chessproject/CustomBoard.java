package cz.cvut.fel.pjv.chessproject;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import cz.cvut.fel.pjv.chessproject.Pieces.Piece;
import cz.cvut.fel.pjv.chessproject.Pieces.PiecesList;
import cz.cvut.fel.pjv.chessproject.View.BoardView;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class provides drawing of all possible pieces and lets player to choose
 * from them.
 */
public class CustomBoard {

    /**
     * window with pieces
     */
    public static Stage pieceWindow;
    private List<Piece> pieces;
    private Canvas canvas;
    private BoardView bW;

    public CustomBoard(BoardView bW) {
        this.bW = bW;
        pieceWindow = new Stage();
        PiecesList pl = new PiecesList();
        pieces = pl.getPieces();
        pieceWindow.initModality(Modality.APPLICATION_MODAL);
        pieceWindow.setTitle("YOUR PIECES");
        pieceWindow.setWidth(800);
        pieceWindow.setHeight(420);
        canvas = new Canvas(800, 420);
        Pane pane = new Pane(canvas);
        Scene scene = new Scene(pane);
        pieceWindow.setScene(scene);
        pieceWindow.centerOnScreen();
        pieceWindow.setResizable(false);
    }

    /**
     * provides the pop-up window with pieces drawn on it
     */
    public void showWindow() {
        pieceWindow.show();
        drawPieces();
    }

    private void drawPieces() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = pieces.get(y * 8 + x);
                if (piece != null) {
                    gc.drawImage(new Image(piece.getImageName()), x * BoardView.TILE_DIM + 20, y * BoardView.TILE_DIM + 20);
                }

            }
        }

    }

    /**
     * Displays the window with pieces for player to choose from them.
     *
     * @param customBoard board instance to be modified.
     * @param x coordinates of place on board
     * @param y
     */
    public void set(Board customBoard, int x, int y) {
        showWindow();
        pieceWindow.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent g) -> {
            Piece toBeSet = choose((int) g.getSceneX() / BoardView.TILE_DIM, (int) g.getSceneY() / BoardView.TILE_DIM);
            toBeSet.setDidNotMove(false);
            customBoard.setPiece(toBeSet, x, y);
            bW.reDraw();
        });

    }

    /**
     * gets the instance of chosen piece from pieces list and closes the window
     * with them.
     *
     */
    public Piece choose(int x, int y) {
        Piece toReturn = pieces.get(y * 8 + x);
        pieces.remove(y * 8 + x);
        pieceWindow.close();
        return toReturn;
    }
}
