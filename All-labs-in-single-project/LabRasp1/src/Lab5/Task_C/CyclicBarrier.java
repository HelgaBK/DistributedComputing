package Lab5.Task_C;

public class CyclicBarrier {
    private int parties;
    private int currentParties = 0;
    private Runnable barrierAction;

    CyclicBarrier(int parties) {
        this.parties = parties;
    }

    CyclicBarrier(int parties, Runnable barrierAction) {
        this.parties = parties;
        this.barrierAction = barrierAction;
    }

    public synchronized void await() throws InterruptedException {
        currentParties++;
        if (currentParties == parties) {
            if (barrierAction != null) barrierAction.run();
            currentParties = 0;
            notifyAll();
        } else wait();
    }
}
