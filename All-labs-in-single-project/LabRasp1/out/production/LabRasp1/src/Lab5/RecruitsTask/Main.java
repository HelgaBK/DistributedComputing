package Lab5.RecruitsTask;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Main {
    private static int numberOfWorkers = 3;
    private static int dataForWorker = 5;

    public static void main(String[] args) {
        Position[] recruits = new Position[numberOfWorkers * dataForWorker];
        CyclicBarrier barrier = new CyclicBarrier(numberOfWorkers, new BarrierAction(recruits));
        Thread[] workers = new Thread[numberOfWorkers];

        for (int i = 0; i < numberOfWorkers; ++i) {
            int start = i * dataForWorker;
            int end = (i + 1) * dataForWorker;
            workers[i] = new Thread(new RecruitsThread(barrier, recruits, start, end));
        }

        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < recruits.length; ++i) {
            recruits[i] = (random.nextInt() % 2 == 0? Position.LEFT: Position.RIGHT);
        }

        System.out.println(recruits);
        for (Thread worker : workers)
            worker.start();
    }
}
