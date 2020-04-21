package Lab6;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FieldLogicThread extends Thread {

    private volatile Field field;
    private ReentrantReadWriteLock lock;
    private CyclicBarrier barrier;

    private int fromX;
    private int toX;
    private int numberOfCivilizations;

    public FieldLogicThread(Field field, CyclicBarrier barrier, ReentrantReadWriteLock lock, int fromX, int toX, int numberOfСivilization) {
        this.field = field;
        this.barrier = barrier;
        this.lock = lock;
        this.fromX = fromX;
        this.toX = toX;
        this.numberOfCivilizations = numberOfСivilization;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            lock.writeLock().lock();
            field.simulate(numberOfCivilizations, fromX, toX);
            lock.writeLock().unlock();

            try {
                barrier.await();
            } catch (InterruptedException e) {
//                e.printStackTrace();
            } catch (BrokenBarrierException e) {
//                e.printStackTrace();
            }
        }
    }
}
