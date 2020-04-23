package Lab2.WinniePooh;

public class Bees implements Runnable {
    private Thread thread;
    private volatile Object[][] forest;
    private final int sizeOfForest;
    private volatile boolean isRunning;
    private volatile boolean isSearching;
    private final Hive hive;
    private int currentTask;

    Bees(Hive hive, Object[][] forest) {
        this.hive = hive;
        this.forest = forest;
        sizeOfForest = forest.length;
        isRunning = true;
        isSearching = true;
        thread = new Thread(this, "Bees");
    }

    void start() {
        this.thread.start();
    }

    public void run() {
        while (isRunning) {
            while (isSearching) {
                currentTask = hive.getTask();
                if (currentTask != -1) {
                    checkArea();
                } else {
                    isSearching = false;
                }
            }
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) { /* as expected */ }
            }
        }
    }

    void stopSearching() {
        isSearching = false;
    }

    void startSearching() {
        isSearching = true;
        synchronized (this) {
            this.notify();
        }
    }

    private void checkArea() {
        boolean b = true;
        for (int j = 0; ((j < sizeOfForest) && b); j++) {
            if (forest[currentTask][j] instanceof Bear) {
                Bear bear = (Bear) forest[currentTask][j];
                forest[currentTask][j] = null;
                b = false;
                System.out.println("The bear was found and punished :)");
                synchronized (bear) {
                    bear.notify();
                }
                synchronized (hive) {
                    hive.notify();
                }
            }
        }
    }

    synchronized void stop() {
        isSearching = false;
        isRunning = false;
        thread.interrupt();
    }
}
