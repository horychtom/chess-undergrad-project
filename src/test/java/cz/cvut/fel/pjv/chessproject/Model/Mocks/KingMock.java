/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.chessproject.Model.Mocks;

import cz.cvut.fel.pjv.chessproject.Enums.PieceTypes;
import cz.cvut.fel.pjv.chessproject.Model.Board;
import cz.cvut.fel.pjv.chessproject.Pieces.Piece;

/**
 *
 * @author tomas
 */
public class KingMock extends Piece {

    private boolean Checked;
    private final boolean inDanger;

    public KingMock(boolean isWhite, int x, int y, boolean inDanger) {
        super(isWhite, x, y);
        this.type = PieceTypes.KING;
        this.Checked = false;
        this.inDanger = inDanger;
    }

    @Override
    public boolean isThreatened(Board board) {
        return inDanger;
    }

}
