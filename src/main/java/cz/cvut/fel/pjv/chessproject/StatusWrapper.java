package cz.cvut.fel.pjv.chessproject;

import cz.cvut.fel.pjv.chessproject.Enums.GameStatus;

/**
 * Simple wrapper for easier variable passing between threads.
 */
public class StatusWrapper {

    private GameStatus gs;

    public StatusWrapper() {
        this.gs = GameStatus.IS_RUNNING;
    }

    public synchronized GameStatus getGs() {
        return gs;
    }

    public synchronized void setGs(GameStatus gs) {
        this.gs = gs;
    }

}
