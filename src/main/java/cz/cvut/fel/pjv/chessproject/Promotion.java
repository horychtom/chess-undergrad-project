package cz.cvut.fel.pjv.chessproject;

import cz.cvut.fel.pjv.chessproject.Model.Game;
import cz.cvut.fel.pjv.chessproject.Pieces.*;
import cz.cvut.fel.pjv.chessproject.View.BoardView;
import java.util.ArrayList;
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
 * This class provides a window with captured pieces for player to choose from.
 */
public class Promotion {

    /**
     * pop-up window with pieces drawn
     */
    public static Stage promWindow;
    private Canvas canvas = new Canvas(400, 100);
    private final List<Piece> pieces;

    public Promotion(boolean white) {
        promWindow = new Stage();
        this.pieces = new ArrayList<>();
        pieces.add(new Queen(white, 0, 0));
        pieces.add(new Bishop(white, 0, 0));
        pieces.add(new Rook(white, 0, 0));
        pieces.add(new Knight(white, 0, 0));
    }

    /**
     * initialization of drawing window
     */
    public void showWindow() {
        promWindow.initModality(Modality.APPLICATION_MODAL);
        promWindow.setTitle("Choose wisely!");
        promWindow.setWidth(400);
        promWindow.setHeight(100);

        Pane pane = new Pane(canvas);
        Scene scene = new Scene(pane);
        promWindow.setScene(scene);
        promWindow.centerOnScreen();
        promWindow.setResizable(false);
        promWindow.show();
        drawPieces();
    }

    private void drawPieces() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int i = 0; i < 4; i++) {
            gc.drawImage(new Image(pieces.get(i).getImageName()), BoardView.TILE_DIM * i + 20, 0);
        }

    }

    /**
     * Displays the window with pieces for player to choose from them.
     *
     * @param game instance on which promotion is to be made.
     * @param bV board view to be refreshed
     */
    public void promote(Game game, BoardView bV) {
        //AI told me that it always wants queen (what a smart AI (no, it's really not))
        if (game.getCurrentPlayer().isAI()) {
            Piece toRevive = choose(0);
            game.getBoard().promote(game.getPromotingPiece(), toRevive, game.getCurrentPlayer());
            bV.reDraw();
            game.swapTurns();
            return;
        }
        showWindow();
        promWindow.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent t) -> {
            Piece toRevive = choose((int) t.getSceneX() / BoardView.TILE_DIM);
            game.getBoard().promote(game.getPromotingPiece(), toRevive, game.getCurrentPlayer());
            bV.reDraw();
            game.swapTurns();
        });

    }

    /**
     * Gets the instance of chosen piece from pieces list and closes the window
     * with them.
     *
     * @param x one coordinate is sufficient.
     * @return the piece to be promoted to.
     */
    public Piece choose(int x) {
        Piece toReturn = pieces.get(x);
        promWindow.close();
        return toReturn;
    }
}
