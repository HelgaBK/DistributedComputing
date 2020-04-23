package Lab6;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FieldPanelRepainer implements Runnable {

    ReentrantReadWriteLock lock;
    volatile Field field;
    FieldPanel fieldPanel;

    public FieldPanelRepainer(Field field, FieldPanel fieldPanel, ReentrantReadWriteLock lock) {
        this.field = field;
        this.fieldPanel = fieldPanel;
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.writeLock().lock();
        // read lock
        field.updateField();

        // read unlock
        lock.writeLock().unlock();
        fieldPanel.repaint();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }
}
