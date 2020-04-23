package Lab4.WritersAndReaders;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {

    static final ArrayList<String> NAMES = new ArrayList<>(Arrays.asList("Bob", "Steve", "Carl", "Jack", "Adolf", "AAA", "FFF", "Tile", "Peter", "Crap", "Sandra", "Winny"));
    static final ArrayList<String> NUMBERS = new ArrayList<>(Arrays.asList("1212213", "1212214", "1212215", "498658546", "12122135461", "4568465", "7465486564", "1328473", "79863543543", "153485475", "778852135", "134289999"));

    public static void main(String[] args) {
        ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

        Random rand = new Random();

        for (int i = 0; i <= NAMES.size() - 1; i++) {
            new Thread(new WriteName(NAMES.get(i), NUMBERS.get(i), rwlock)).start();
            int numberIdx = Math.abs(rand.nextInt()) % NUMBERS.size();
            new Thread(new SearchByNumber(NUMBERS.get(numberIdx), rwlock)).start();
            int nameIdx = Math.abs(rand.nextInt()) % NAMES.size();
            new Thread(new SearchByName(NAMES.get(nameIdx), rwlock)).start();
            int r = rand.nextInt(10);
            new Thread(new RemoveName(NAMES.get(r), NUMBERS.get(r), rwlock)).start();
        }
    }
}
