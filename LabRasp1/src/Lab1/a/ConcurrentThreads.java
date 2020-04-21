package Lab1.a;

import javax.swing.*;

public class ConcurrentThreads {

    private JSlider jSlider;
    private int decreasePriority = 1;
    private int increasePriority = 1;

    private Thread threadDecreaser;
    private Thread threadIncreaser;



    public ConcurrentThreads(JSlider s) {
        jSlider = s;
    }

    public void incIncPriority() {
        if (increasePriority < 10) {
            increasePriority++;
            System.out.println(increasePriority);
            threadIncreaser.setPriority(increasePriority);
        }
    }

    public void decIncPriority() {
        if (increasePriority > 1) {
            increasePriority--;
            System.out.println(increasePriority);
            threadIncreaser.setPriority(increasePriority);
        }
    }

    public void incDecPriority() {
        if (decreasePriority < 10) {
            decreasePriority++;
            System.out.println(decreasePriority);
            threadDecreaser.setPriority(decreasePriority);
        }
    }

    public void decDecPriority() {
        if (decreasePriority > 1) {
            decreasePriority--;
            System.out.println(decreasePriority);
            threadDecreaser.setPriority(decreasePriority);
        }
    }

    public void startTesting() {
        threadIncreaser = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    synchronized (jSlider) {
                        jSlider.setValue(jSlider.getValue() + 1);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });threadIncreaser.setDaemon(true);

        threadDecreaser = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    synchronized (jSlider) {
                        jSlider.setValue(jSlider.getValue() - 1);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });threadDecreaser.setDaemon(true);

        threadDecreaser.start();
        threadIncreaser.start();
    }
}
