package cz.cvut.fel.pjv.chessproject.Model;

import cz.cvut.fel.pjv.chessproject.Enums.GameStatus;
import cz.cvut.fel.pjv.chessproject.Enums.PieceTypes;

/**
 * Class which holds necessary information about specific move.
 */
public class Move {

    private int from_x;
    private int from_y;
    private int to_x;
    private int to_y;
    private PieceTypes toPromoteTo = null;
    private GameStatus resultIn = GameStatus.IS_RUNNING;

    /**
     * Couple of similar constructors are made for easier setup in edge cases.
     */
    public Move(int from_x, int from_y, int to_x, int to_y) {
        this.from_x = from_x;
        this.from_y = from_y;
        this.to_x = to_x;
        this.to_y = to_y;

    }

    public Move(int from_x, int from_y) {
        this.from_x = from_x;
        this.from_y = from_y;
    }

    public Move() {
    }

    public GameStatus getResultIn() {
        return resultIn;
    }

    public void setResultIn(GameStatus resultIn) {
        this.resultIn = resultIn;
    }

    public PieceTypes getToPromoteTo() {
        return toPromoteTo;
    }

    public void setToPromoteTo(PieceTypes toPromoteTo) {
        this.toPromoteTo = toPromoteTo;
    }

    public int getFrom_x() {
        return from_x;
    }

    public int getFrom_y() {
        return from_y;
    }

    public int getTo_x() {
        return to_x;
    }

    public int getTo_y() {
        return to_y;
    }

    public void setFrom(int from_x, int from_y) {
        this.from_x = from_x;
        this.from_y = from_y;
    }

    public void setTo(int to_x, int to_y) {
        this.to_x = to_x;
        this.to_y = to_y;
    }

}
