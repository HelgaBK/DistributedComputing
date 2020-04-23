package Lab4.WritersAndReaders;

import java.io.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WriteName implements Runnable {
    String name;
    String number;
    ReentrantReadWriteLock locker;
    String fileName = "input.txt";
File file = new File("input.txt");
    WriteName(String name, String number, ReentrantReadWriteLock locker) {
        this.name = name;
        this.number = number;
        this.locker = locker;
    }

    @Override
    public void run() {
        locker.writeLock().lock();

        try (PrintWriter printWriter = new PrintWriter(new FileWriter(fileName, true))) {
            printWriter.append(name + " " + number);
            printWriter.println();
            System.out.println("Writer thread. Added: " + name + " " + number);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        locker.writeLock().unlock();
    }
}
