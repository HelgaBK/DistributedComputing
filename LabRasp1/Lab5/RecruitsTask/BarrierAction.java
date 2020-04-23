package Lab5.RecruitsTask;

public class BarrierAction implements Runnable {
    private final Position[] recruits;

    BarrierAction(Position[] recruits) {
        this.recruits = recruits;
    }

    @Override
    public void run() {

        boolean finished = true;
        for (int i = 1; i < recruits.length && finished; ++i) {
            Position left = recruits[i - 1];
            Position right = recruits[i];
            if (left != right)
                finished = false;
        }

        for (int i = 0; i < recruits.length ; i++) {
            System.out.print(recruits[i] + " ");
        }
        System.out.println();
    }
}
