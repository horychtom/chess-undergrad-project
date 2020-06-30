package cz.cvut.fel.pjv.chessproject.Players;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import cz.cvut.fel.pjv.chessproject.Model.Move;
import cz.cvut.fel.pjv.chessproject.Pieces.Piece;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for storing information about player and decision making method for AI
 */
public class Player {

    private final boolean isWhite;
    private boolean isAI;
    private Piece myKing;
    public List<Piece> myPieces = new ArrayList<>();

    public Player(boolean isWhite, boolean isAI) {
        this.isWhite = isWhite;
        this.isAI = isAI;
    }

    public void setMyKing(Piece myKing) {
        this.myKing = myKing;
    }

    public boolean isAI() {
        return isAI;
    }

    public Piece getMyKing() {
        return myKing;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public List<Piece> getMyPieces() {
        return myPieces;
    }

    /**
     * Method for AI player to generate valid moves
     *
     * @param currentMove to store information about its move
     * @param board to check validity
     * @return boolean if possible move was found
     */
    public boolean getRandomMove(Move currentMove, Board board) {
        //as this method takes first possible moves, pieces are shuffled to
        //generate some randomness
        Collections.shuffle(this.myPieces);

        for (Piece piece : this.myPieces) {

            //skip dead pieces
            if (!piece.isAlive()) {
                continue;
            }

            //procedure of generate move and exclude check-resulting moves
            ArrayList<int[]> moves = piece.getPossibleMoves(board);
            ArrayList<int[]> checks = new ArrayList<>();
            for (int[] move : moves) {
                Move testMove = new Move(piece.getX(), piece.getY(), move[0], move[1]);
                if (board.willBeChecked(testMove, this)) {
                    checks.add(move);
                }
            }

            //removing these that result in check
            if (!checks.isEmpty()) {
                moves.removeAll(checks);
            }

            //NOT empty means there exist non-check resulting move
            if (!moves.isEmpty()) {
                int[] toMove = moves.get(0);
                currentMove.setFrom(piece.getX(), piece.getY());
                currentMove.setTo(toMove[0], toMove[1]);
                return true;
            }
        }
        //no move found -> will be check or stale MATE
        return false;
    }

}
