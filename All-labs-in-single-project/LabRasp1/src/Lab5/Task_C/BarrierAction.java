package Lab5.Task_C;

import java.util.ArrayList;

public class BarrierAction implements Runnable {
    private ArrayList<ArrayThread> threads = new ArrayList<>();

    public void addThread(ArrayThread thread) {
        this.threads.add(thread);
    }

    @Override
    public void run() {
        int mean = this.determineMean();
        System.out.printf("Mean value: %d\n", mean);

        if (this.checkIfEnd(mean)) {
            this.interruptAll();
            System.out.println("Barrier finished work!");
        } else {
            this.setActions(mean);
            System.out.println("Next run.....");
            System.out.println("----------------------------------------------------");
        }

    }

    private void interruptAll() {
        for (ArrayThread thread : threads)
            thread.interrupt();
    }

    private boolean checkIfEnd(int mean) {
        for (ArrayThread thread : threads)
            if (mean != thread.getCurrentSum())
                return false;
        return true;
    }

    private int determineMean() {
        int sum = 0;
        for (ArrayThread thread : threads)
            sum += thread.getCurrentSum();
        return sum / threads.size();
    }

    private void setActions(int mean) {
        for (ArrayThread thread : threads) {
            if (mean > thread.getCurrentSum())
                thread.setNextAction(Action.INCREMENT);
            else if (mean < thread.getCurrentSum())
                thread.setNextAction(Action.DECREMENT);
            else thread.setNextAction(Action.NOACTION);
        }
    }
}
