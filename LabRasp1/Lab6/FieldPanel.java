package Lab6;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FieldPanel extends JPanel {
    final static Color[] COLORS = {Color.WHITE, Color.GREEN, Color.YELLOW, Color.BLUE, Color.DARK_GRAY, Color.CYAN};

    final static int NUMBER_OF_THREADS = 5;
    private volatile Field field;

    private ReentrantReadWriteLock lock;
    private CyclicBarrier barrier = null;

    FieldLogicThread[] threads = null;
    private FieldPanelRepainer repainer = null;

    private int cellSize;
    private final int CELL_GAP = 1;

    public FieldPanel(int width, int height, int cellSize) {
        lock = new ReentrantReadWriteLock();
        this.cellSize = cellSize;
        field = new Field(width, height);

    }

    public void startLife(int numberOfСivilizations) {
        stopLife();

        field = new Field(field.width, field.height);
        lock = new ReentrantReadWriteLock();
        repainer = new FieldPanelRepainer(field, this, lock);

        // call thread #6 (
        barrier = new CyclicBarrier(NUMBER_OF_THREADS, repainer);

        field.clear();
        field.generate(numberOfСivilizations);
        int threadBlockSize = field.height / NUMBER_OF_THREADS;

        threads = new FieldLogicThread[NUMBER_OF_THREADS];
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i] = new FieldLogicThread(field, barrier, lock, threadBlockSize * i, threadBlockSize * (i + 1), numberOfСivilizations);
        }
        for (int i = 0; i < NUMBER_OF_THREADS; i++) threads[i].start();
    }

    public void stopLife() {
        if (threads != null) {
            for (int i = 0; i < threads.length; i++) {
                threads[i].interrupt();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (field != null) {
            lock.readLock().lock();
            super.paintComponent(g);
            Insets b = getInsets();
            for (int y = 0; y < field.height; y++) {
                for (int x = 0; x < field.width; x++) {
                    int civilization = field.getCell(x, y).civilization;
                    g.setColor(COLORS[civilization]);
                    g.fillRect(b.left + CELL_GAP + x * (cellSize + CELL_GAP),
                            b.top + CELL_GAP + y * (cellSize + CELL_GAP), cellSize, cellSize);
                }
            }
            lock.readLock().unlock();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (field != null) {
            Insets b = getInsets();
            return new Dimension((cellSize + CELL_GAP) * field.width + CELL_GAP + b.left + b.right,
                    (cellSize + CELL_GAP) * field.height + CELL_GAP + b.top + b.bottom);
        } else
            return new Dimension(300, 300);
    }
}
