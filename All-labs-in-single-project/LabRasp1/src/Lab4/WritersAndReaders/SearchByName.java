package Lab4.WritersAndReaders;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SearchByName implements Runnable {
    String name;
    ReentrantReadWriteLock locker;
    String fileName = "input.txt";
    File file = new File("input.txt");

    SearchByName(String name, ReentrantReadWriteLock loсker) {
        this.name = name;
        this.locker = loсker;
    }

    public void run() {
        locker.readLock().lock();
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] str = line.split(" ");
                if (name.equals(str[0])) {
                    System.out.println("SearchByName thread. Found by name " + name + ": " + str[1]);
                    locker.readLock().unlock();
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("SearchByName thread. Not found " + name);

        locker.readLock().unlock();

    }
}
