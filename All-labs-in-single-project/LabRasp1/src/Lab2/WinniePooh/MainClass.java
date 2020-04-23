package Lab2.WinniePooh;

public class MainClass {
    private static final int sizeOfForest = 1000;
    private static final int countOfGroupsOfBees = 2;
    private static final int numberOfCycles = 5;


    public static void main(String[] args) {
        new MainClass().start();
    }

    private void start() {
        Object[][] forest = new Object[sizeOfForest][sizeOfForest];
        Bear bear = new Bear(forest);
        Hive hive = new Hive(forest, countOfGroupsOfBees, numberOfCycles, this);

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bear.stop();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\nMain Thread has been finished");
    }
}
