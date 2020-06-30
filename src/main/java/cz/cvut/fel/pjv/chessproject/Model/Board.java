package cz.cvut.fel.pjv.chessproject.Model;

import cz.cvut.fel.pjv.chessproject.Enums.PieceTypes;
import cz.cvut.fel.pjv.chessproject.Model.Move;
import cz.cvut.fel.pjv.chessproject.Pieces.Bishop;
import cz.cvut.fel.pjv.chessproject.Pieces.King;
import cz.cvut.fel.pjv.chessproject.Pieces.Knight;
import cz.cvut.fel.pjv.chessproject.Pieces.Pawn;
import cz.cvut.fel.pjv.chessproject.Pieces.Piece;
import cz.cvut.fel.pjv.chessproject.Pieces.Queen;
import cz.cvut.fel.pjv.chessproject.Pieces.Rook;
import cz.cvut.fel.pjv.chessproject.Players.Player;
import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<Move> historyOfMoves = new ArrayList<>();
    private final Box[][] board;

    public Board() {
        this.board = new Box[8][8];
        createNewBoard();
    }

    public Board(boolean empty) {
        this.board = new Box[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                board[y][x] = new Box(x, y);
            }
        }
    }

    /**
     * @return instance of box on [x,y] spot of the board
     */
    public Box getBox(int x, int y) {
        return board[y][x];
    }

    /**
     * @return the last element of moves history list
     */
    public Move getLastMove() {
        if (historyOfMoves.isEmpty()) {
            return null;
        }
        return historyOfMoves.get(historyOfMoves.size() - 1);
    }

    /**
     * @return list with all moves yet played
     */
    public List<Move> getHistoryOfMoves() {
        return historyOfMoves;
    }

    public void setHistoryOfMoves(List<Move> historyOfMoves) {
        this.historyOfMoves = historyOfMoves;
    }

    /**
     * checks if board dimensions are ok
     */
    public boolean isOutOfBounds(int x, int y) {
        return (x > 7 || y > 7 || x < 0 || y < 0);
    }

    /**
     * Places piece on board and sets it appropriately
     *
     * @param piece piece to be set
     */
    public void setPiece(Piece piece, int x, int y) {
        if (piece == null) {
            return;
        }
        if (isOutOfBounds(x, y)) {
            return;
        }
        piece.setX(x);
        piece.setY(y);
        board[y][x].setCurrentPiece(piece);
    }

    /**
     * sets up new board with basic standard layout
     */
    public void createNewBoard() {
        //creating all boxes == real places of board
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                board[y][x] = new Box(x, y);
            }
        }

        //creating black side (from my view)
        board[0][0].setCurrentPiece(new Rook(false, 0, 0));
        board[0][1].setCurrentPiece(new Knight(false, 1, 0));
        board[0][2].setCurrentPiece(new Bishop(false, 2, 0));
        board[0][3].setCurrentPiece(new Queen(false, 3, 0));
        board[0][4].setCurrentPiece(new King(false, 4, 0));
        board[0][5].setCurrentPiece(new Bishop(false, 5, 0));
        board[0][6].setCurrentPiece(new Knight(false, 6, 0));
        board[0][7].setCurrentPiece(new Rook(false, 7, 0));

        for (int i = 0; i < 8; i++) {
            board[1][i].setCurrentPiece(new Pawn(false, i, 1));
        }

        //same with white
        board[7][0].setCurrentPiece(new Rook(true, 0, 7));
        board[7][1].setCurrentPiece(new Knight(true, 1, 7));
        board[7][2].setCurrentPiece(new Bishop(true, 2, 7));
        board[7][3].setCurrentPiece(new Queen(true, 3, 7));
        board[7][4].setCurrentPiece(new King(true, 4, 7));
        board[7][5].setCurrentPiece(new Bishop(true, 5, 7));
        board[7][6].setCurrentPiece(new Knight(true, 6, 7));
        board[7][7].setCurrentPiece(new Rook(true, 7, 7));
        for (int i = 0; i < 8; i++) {
            board[6][i].setCurrentPiece(new Pawn(true, i, 6));
        }

    }

    /**
     * Makes an actual difference on board. Moving piece, and destination is
     * read from move input and is then processed to deliver it into destination
     * and possibly capture the piece on the destination
     *
     * @param move created by player
     */
    public void makeChange(Move move) {
        Piece movingPiece = board[move.getFrom_y()][move.getFrom_x()].getCurrentPiece();
        //checkmate with AI exception
        if (movingPiece == null) {
            return;
        }
        Piece capturedPiece = board[move.getTo_y()][move.getTo_x()].getCurrentPiece();
        movingPiece.setDidNotMove(false);

        //CASTLING check for kings
        if (movingPiece.getType() == PieceTypes.KING
                && Math.abs(move.getFrom_x() - move.getTo_x()) == 2) {
            //short castling
            if (move.getTo_x() > move.getFrom_x()) {
                Piece rookToCastle = board[move.getFrom_y()][7].getCurrentPiece();
                board[move.getFrom_y()][move.getFrom_x() + 1].setCurrentPiece(rookToCastle);
                rookToCastle.setX(move.getFrom_x() + 1);
                rookToCastle.setY(move.getFrom_y());
                board[move.getFrom_y()][7].setCurrentPiece(null);
                //long castling    
            } else {
                Piece rookToCastle = board[move.getFrom_y()][0].getCurrentPiece();
                board[move.getFrom_y()][move.getFrom_x() - 1].setCurrentPiece(rookToCastle);
                rookToCastle.setX(move.getFrom_x() - 1);
                rookToCastle.setY(move.getFrom_y());
                board[move.getFrom_y()][0].setCurrentPiece(null);
            }
            //the rest of the move is managed below as for usual move

        } //EN PASSANT check for pawns
        else if (movingPiece.getType() == PieceTypes.PAWN
                && Math.abs(move.getFrom_x() - move.getTo_x()) != 0
                && capturedPiece == null) {
            board[move.getFrom_y()][move.getTo_x()].getCurrentPiece().setIsAlive(false);
            board[move.getFrom_y()][move.getTo_x()].setCurrentPiece(null);
        }

        //capturing
        if (capturedPiece != null) {
            capturedPiece.setIsAlive(false);
        }

        board[move.getTo_y()][move.getTo_x()].setCurrentPiece(movingPiece);
        board[move.getFrom_y()][move.getFrom_x()].setCurrentPiece(null);
        //the piece i moved with it needs new coords
        movingPiece.setX(move.getTo_x());
        movingPiece.setY(move.getTo_y());
        historyOfMoves.add(move);
    }

    /**
     * Checks if move will result in check. Even if player is already checked,
     * this methods therefore clarifies if he is able to remove the check
     *
     * @param move we want to test for check
     * @return boolean state if will or will not be checked
     */
    public boolean willBeChecked(Move move, Player player) {
        boolean isChecked = false;

        //store it to undo move
        Piece tmp = getBox(move.getFrom_x(), move.getFrom_y()).getCurrentPiece();
        Piece tmp2 = getBox(move.getTo_x(), move.getTo_y()).getCurrentPiece();

        //make move + check the result of move - we dont have to set
        // our pieces new position because it will get back immediately and wont affect anything
        getBox(move.getTo_x(), move.getTo_y()).setCurrentPiece(tmp);
        tmp.setX(move.getTo_x());
        tmp.setY(move.getTo_y());
        getBox(move.getFrom_x(), move.getFrom_y()).setCurrentPiece(null);

        //main "checking" part
        if (player.getMyKing().isThreatened(this)) {
            isChecked = true;
        }

        //undo move
        getBox(move.getFrom_x(), move.getFrom_y()).setCurrentPiece(tmp);
        tmp.setX(move.getFrom_x());
        tmp.setY(move.getFrom_y());
        getBox(move.getTo_x(), move.getTo_y()).setCurrentPiece(tmp2);

        return isChecked;
    }

    /**
     * Scans the board and when comes to piece add it to its owners piece list
     *
     * @param playerW with white pieces
     * @param playerB with black pieces
     */
    public void setPlayerPieces(Player playerW, Player playerB) {

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board[y][x].getCurrentPiece() != null) {
                    Piece tmp = board[y][x].getCurrentPiece();
                    if (tmp.IsWhite() == playerW.isWhite()) {
                        playerW.myPieces.add(tmp);
                    } else {
                        playerB.myPieces.add(tmp);
                    }
                }
            }
        }

    }

    /**
     * Swaps actual piece on board with new piece
     *
     * @param piece1 pawn to be forgotten
     * @param piece2 piece chosen to be revived
     */
    public void promote(Piece piece1, Piece piece2, Player player) {
        if (piece2 == null) {
            return;
        }
        Move m = getLastMove();

        if (m != null) {
            m.setToPromoteTo(piece2.getType());
        }

        piece2.setIsAlive(true);
        player.myPieces.add(piece2);
        setPiece(piece2, piece1.getX(), piece1.getY());

        piece1.setIsAlive(false);
    }

}
