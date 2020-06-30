package cz.cvut.fel.pjv.chessproject;

import cz.cvut.fel.pjv.chessproject.Networking.Reciever;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 * Thread class for standard clock. Has time which it does modify.
 */
public class Clock extends Thread {

    private Time t;

    private static final Logger lgr = Logger.getLogger(Reciever.class.getName());

    public Clock(Time time) {
        this.t = time;
    }

    /**
     * Gets the value of seconds and after one seconds decrements it. Also
     * writes out the value onto the GUI in a thread safe way.
     */
    @Override
    public void run() {
        while (true) {
            try {
                if (t.getSeconds() >= 1 && t.isIsOn()) {
                    Thread.sleep(1000);
                    t.decrement();
                    Platform.runLater(new Runnable() {
                        public void run() {
                            Menu.seconds.setText(String.valueOf(t.getSeconds()));
                        }
                    });
                } else {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            Menu.seconds.setText(String.valueOf(t.getBase()));
                        }
                    });

                    break;
                }
            } catch (InterruptedException ex) {
                lgr.log(Level.SEVERE, "ERROR: interupted, please restart the game", ex.getMessage());
            }
        }

    }

}
