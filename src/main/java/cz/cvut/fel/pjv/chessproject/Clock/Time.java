package cz.cvut.fel.pjv.chessproject;

/**
 * Class for storing (wrapping) information about seconds. with simple
 * decrement, reset methods.
 */
public class Time {

    //base is for storing intial value
    private volatile int base;
    private volatile int seconds;
    private volatile boolean isOn;

    public Time(int seconds) {
        this.seconds = seconds;
        this.base = seconds;
        this.isOn = true;
    }

    public synchronized int getSeconds() {
        return seconds;
    }

    public synchronized boolean isIsOn() {
        return isOn;
    }

    public synchronized void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }

    public synchronized int getBase() {
        return base;
    }

    public synchronized void setSeconds(int seconds) {
        this.seconds = seconds;
        base = seconds;
    }

    /**
     * decrements current seconds by 1 (as one second goes by)
     */
    public synchronized void decrement() {
        seconds = seconds - 1;
    }

    /**
     * restoring initial
     */
    public synchronized void reset() {
        seconds = base;
    }

}
