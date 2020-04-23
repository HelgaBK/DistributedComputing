package Lab5.Task_C;

import java.util.Random;

public class ArrayThread extends Thread {
    private int[] array;
    private int currentSum = 0;
    private Action nextAction;
    private CyclicBarrier barrier;
    final private Random rand = new Random();

    public ArrayThread(CyclicBarrier barrier, int[] array) {
        this.array = array;
        this.barrier = barrier;
        this.nextAction = rand.nextInt(2) == 1 ? Action.DECREMENT : Action.INCREMENT;
    }

    public ArrayThread(CyclicBarrier barrier) {
        this(barrier, null);
        int len = 15;
        int[] arr = new int[len];
        for (int i = 0; i < len; i++)
            arr[i] = rand.nextInt(100);
        this.array = arr;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            this.applyAction();
            int sum = this.determineSum();
            System.out.printf("The action is done: %s, Sum: %d\n", this.nextAction, sum);

            this.setCurrentSum(sum);
            try {
                Thread.sleep(100);
                barrier.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private int determineSum() {
        int sum = 0;
        for (int i1 : array)
            sum += i1;
        return sum;
    }

    public void setNextAction(Action nextAction) {
        this.nextAction = nextAction;
    }

    synchronized private void setCurrentSum(int currentSum) {
        this.currentSum = currentSum;
    }

    synchronized public int getCurrentSum() {
        return currentSum;
    }

    private void applyAction() {
        if (nextAction.equals(Action.DECREMENT))
            this.decrement();
        else if (nextAction.equals(Action.INCREMENT))
            this.increment();
    }

    private void decrement() {
        int index = rand.nextInt(array.length);
        array[index]--;
    }

    private void increment() {
        int index = rand.nextInt(array.length);
        array[index]++;
    }
}
