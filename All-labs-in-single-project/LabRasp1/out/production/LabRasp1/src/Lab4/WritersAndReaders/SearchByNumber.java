package Lab4.WritersAndReaders;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SearchByNumber implements Runnable {
    String number;
    ReentrantReadWriteLock locker;
    String fileName = "input.txt";
    File file = new File(fileName);

    SearchByNumber(String number, ReentrantReadWriteLock loсker) {
        this.number = number;
        this.locker = loсker;
    }

    @Override
    public void run() {
        locker.readLock().lock();

        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] str = line.split(" ");
                if (number.equals(str[1])) {
                    System.out.println("SearchByNumber thread. Found by number " + number + ": " + str[0]);
                    locker.readLock().unlock();
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SearchByNumber thread. Not found " + number);
        locker.readLock().unlock();

    }
}
