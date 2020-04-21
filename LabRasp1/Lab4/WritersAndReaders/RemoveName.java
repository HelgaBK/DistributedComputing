package Lab4.WritersAndReaders;

import java.io.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RemoveName implements Runnable {
    String name;
    String number;
    ReentrantReadWriteLock locker;
    String fileName = "input.txt";
    File newFile = new File(fileName + ".tmp");
    File oldFile = new File(fileName);

    RemoveName(String name, String number, ReentrantReadWriteLock locker) {
        this.name = name;
        this.number = number;
        this.locker = locker;
    }

    public void run() {

        locker.writeLock().lock();

        String line;
        boolean flag = false;

        try (PrintWriter printWriter = new PrintWriter(new FileWriter(newFile))) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(oldFile))) {
                while ((line = bufferedReader.readLine()) != null) {
                    String str[] = line.split(" ");
                    if (!str[0].equals(name) && !str[1].equals(number)) {
                        printWriter.println(line);
                    } else {
                        flag = true;
                    }

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (flag == true) {
            oldFile.delete();
            boolean a = newFile.renameTo(oldFile);
            System.out.println("RemoveName thread. Removed: " + name + " " + number);
        } else {
            newFile.delete();
            System.out.println("RemoveName thread. Not found: " + name + " " + number);
        }
        locker.writeLock().unlock();

    }
}
