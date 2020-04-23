package Lab2.WinniePooh;

import java.util.Random;

public class Bear implements Runnable {
    private Thread thread;
    private Random random;
    private volatile Object[][] forest;
    private final int sizeOfForest;
    private volatile boolean isRunning = true;

    // not null
    Bear(Object[][] forest) {
        this.forest = forest;
        sizeOfForest = forest.length;
        random = new Random();
        thread = new Thread(this, "Bear");
        thread.start();
    }

    public void run() {
        while (isRunning) {
            hide();
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("\nThe bear has finished its work");
                }
            }
        }
    }

    private void hide() {
        System.out.println("\nThe bear is hiding");
        forest[random.nextInt(sizeOfForest)][random.nextInt(sizeOfForest)] = this;
    }

    void stop() {
        isRunning = false;
        thread.interrupt();
    }
}
