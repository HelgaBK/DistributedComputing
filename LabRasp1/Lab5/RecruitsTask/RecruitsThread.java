package Lab5.RecruitsTask;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class RecruitsThread implements Runnable {

    private final CyclicBarrier barrier;
    private final Position[] recruits;
    private final int start, end;

    RecruitsThread(CyclicBarrier barrier, Position[] recruit, int start, int end) {
        this.barrier = barrier;
        this.recruits = recruit;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        while (!isDone(recruits)) {
            for (int i = start; i < end; ++i) {
                if (i == 0)
                    continue;

                Position left = recruits[i - 1];
                Position right = recruits[i];
                if (left != right && left == Position.RIGHT) {
                    recruits[i - 1] = inversePosition(recruits[i - 1]);
                    recruits[i] = inversePosition(recruits[i]);
                }
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private Position inversePosition(Position p) {
        if (p == Position.LEFT) return Position.RIGHT;
        if (p == Position.RIGHT) return Position.LEFT;
        return p;
    }

    private boolean isDone(Position[] recruits) {
        for (int i = 0; i < recruits.length - 1; i++) {
            Position left = recruits[i];
            Position right = recruits[i+1];
            if (left != right && left == Position.RIGHT) {
               return false;
            }
        }
        return true;
    }
}
