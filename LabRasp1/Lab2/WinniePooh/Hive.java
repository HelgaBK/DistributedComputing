package Lab2.WinniePooh;

import java.util.LinkedList;
import java.util.Queue;

public class Hive implements Runnable {
    private MainClass mainClass;
    private Thread thread;
    private volatile Object[][] forest;
    private final int sizeOfForest;
    private volatile Queue<Integer> portfolioOfTasks;
    private Bees[] groupsOfBees;
    private final int countOfGroupsOfBees;
    private final int numberOfCycles;
    private int currentCycle;
    private boolean isRunning;

    Hive(Object[][] forest, int countOfGroupsOfBees, int numberOfCycles, MainClass mainClass) {
        System.out.println("Hive was created");
        this.forest = forest;
        this.countOfGroupsOfBees = countOfGroupsOfBees;
        this.numberOfCycles = numberOfCycles;
        this.mainClass = mainClass;
        sizeOfForest = forest.length;
        portfolioOfTasks = new LinkedList<Integer>();

        currentCycle = 0;
        isRunning = true;

        thread = new Thread(this, "Hive");
        thread.start();

        groupsOfBees = new Bees[countOfGroupsOfBees];
        System.out.println("Creating all groups of bees");
        for (int i = 0; i < countOfGroupsOfBees; i++)
            groupsOfBees[i] = new Bees(this, forest);

        updatePortfolioOfTasks();

        System.out.println("Bees is starting searching");
        for (Bees bees : groupsOfBees)
            bees.start();
    }

    private synchronized void updatePortfolioOfTasks() {
        System.out.println("Updated portfolio of tasks");
        for (int i = 0; i < sizeOfForest; i++) {
            portfolioOfTasks.add(i);
        }
    }

    synchronized int getTask() {
        if (portfolioOfTasks.size() != 0)
            return portfolioOfTasks.poll();
        else return (-1);
    }

    private void stop() {
        isRunning = false;
        for (Bees bees : groupsOfBees) {
            bees.stop();
        }
        System.out.println("\nThe hive has finished its work");
        thread.interrupt();
    }

    private synchronized void bearWasFound() {
        portfolioOfTasks.clear();
        currentCycle++;
        if (currentCycle == numberOfCycles) {
            synchronized (mainClass) {
                mainClass.notify();
            }
            this.stop();
        } else {
            System.out.println("Bees stopped searching");
            for (Bees bees : groupsOfBees) {
                bees.stopSearching();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updatePortfolioOfTasks();

            System.out.println("Bees started searching");
            for (Bees bees : groupsOfBees) {
                bees.startSearching();
            }
        }
    }

    public void run() {
        while (isRunning) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            if (isRunning)
                bearWasFound();
        }
    }
}
