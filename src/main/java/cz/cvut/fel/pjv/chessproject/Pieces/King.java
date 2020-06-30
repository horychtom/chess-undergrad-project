package cz.cvut.fel.pjv.chessproject.Pieces;

import cz.cvut.fel.pjv.chessproject.Model.Board;
import cz.cvut.fel.pjv.chessproject.Enums.PieceTypes;
import java.util.ArrayList;

/**
 * King class containing its method for possible moves generation and couple
 * utility methods for check clarifying.
 */
public class King extends Piece {

    private boolean Checked = false;

    public King(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        this.type = PieceTypes.KING;
        this.Checked = false;
        imageName = isWhite ? "KingW.png" : "King.png";
    }

    public void setChecked(boolean Checked) {
        this.Checked = Checked;
    }

    public boolean isChecked() {
        return Checked;
    }

    @Override
    public ArrayList<int[]> getPossibleMoves(Board board) {
        ArrayList<int[]> moves = new ArrayList<>();
        int[][] vectors = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        //going in all directions direction
        for (int[] vec : vectors) {
            int xTmp = x; //tmp so i can change it below
            int yTmp = y;

            //not going out of board
            while (!board.isOutOfBounds(xTmp + vec[0], yTmp + vec[1])) {
                //this is simple check that we move only 1 box
                if (Math.abs(xTmp + vec[0] - x) < 2 && Math.abs(yTmp + vec[1] - y) < 2) {

                    //no piece no problem
                    if (board.getBox(xTmp + vec[0], yTmp + vec[1]).getCurrentPiece() == null) {
                        int[] move = {xTmp + vec[0], yTmp + vec[1]};
                        moves.add(move);
                        xTmp = xTmp + vec[0];
                        yTmp = yTmp + vec[1];
                    } //this is finding my piece so this direction is busted
                    else if (board.getBox(xTmp + vec[0], yTmp + vec[1]).getCurrentPiece().IsWhite() == this.IsWhite()) {
                        break;
                    } //this is finding opponents piece and capturing it
                    else {
                        int[] move = {xTmp + vec[0], yTmp + vec[1]};
                        moves.add(move);
                        break;
                    }
                } //too far! im "just" a king
                else {
                    break;
                }

            }
        }

        int[][] castlingVecs = {{1, 0}, {-1, 0}};
        //castling
        if (this.didNotMove() && !this.Checked) {
            for (int[] vec : castlingVecs) {
                int xTmp = x;
                while (!board.isOutOfBounds(xTmp + vec[0], y)) {
                    Piece tmp = board.getBox(xTmp + vec[0], y).getCurrentPiece();
                    //if its free place check for CHECK rule and go on
                    if (tmp == null) {
                        if (wouldBeChecked(board, xTmp + vec[0], y)) {
                            break;
                        }
                        xTmp = xTmp + vec[0];
                        continue;
                    }
                    //we hit piece - if its rook and did not move we are done
                    if (tmp.getType() == PieceTypes.ROOK && tmp.didNotMove) {
                        //simple check in which direction we go
                        if (vec[0] > 0) {
                            int[] move = {x + 2, y};
                            moves.add(move);
                        } else {
                            int[] move = {x - 2, y};
                            moves.add(move);
                        }
                        break;
                    } //we hit piece, its something else - leave it be
                    else {
                        break;
                    }

                }

            }
        }
        return moves;
    }

    //simple util for castling check check
    private boolean wouldBeChecked(Board board, int tryX, int tryY) {
        boolean isChecked = false;

        //store it to undo the move
        Piece tmp = this;
        int realX = x;
        int realY = y;

        //pretend the situation
        board.getBox(tryX, tryY).setCurrentPiece(tmp);
        this.setX(tryX);
        this.setY(tryY);

        //verify result
        if (this.isThreatened(board)) {
            isChecked = true;
        }

        //undo move
        this.setX(realX);
        this.setY(realY);
        board.getBox(tryX, tryY).setCurrentPiece(null);

        return isChecked;

    }

    /**
     * Iterating over the board and for each enemy piece gets list of their
     * possible moves. If this.King is found on any of these lists he is in
     * danger and true is returned
     *
     * @param board instance to iterate over
     * @return boolean state if is in danger or not
     */
    @Override
    public boolean isThreatened(Board board) {
        //iterating over whole board
        for (int iY = 0; iY < 8; iY++) {
            for (int iX = 0; iX < 8; iX++) {
                if (board.getBox(iX, iY).getCurrentPiece() != null) {
                    if (board.getBox(iX, iY).getCurrentPiece().IsWhite() != this.IsWhite()) {
                        ArrayList<int[]> moves = board.getBox(iX, iY).getCurrentPiece().getPossibleMoves(board);
                        if (isInList(moves)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    //util for finding out if is in list
    private boolean isInList(ArrayList<int[]> moves) {

        for (int[] m : moves) {
            if ((x == m[0]) && (y == m[1])) {
                return true;
            }
        }

        return false;
    }

}
