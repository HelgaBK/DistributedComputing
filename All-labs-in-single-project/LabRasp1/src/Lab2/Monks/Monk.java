package Lab2.Monks;

import java.util.Random;

public class Monk implements Comparable{
    private Integer energy;
    private int monastery;

    Monk() {
        Random r = new Random();
        energy = r.nextInt(1000);
        monastery = r.nextInt(2);
    }

    Integer getEnergy() {
        return energy;
    }
    int getMonastery(){
        return monastery;
    }

    @Override
    public int compareTo(Object o) {
        Monk other = (Monk)o;
        if(this.energy > other.energy) {
            return 1;
        } else if (this.energy < other.energy){
            return -1;
        } else {
            return 0;
        }
    }
    public static Monk max(Object first, Object second) {
        return Monk.max((Monk)first, (Monk)second);
    }

    static Monk max(Monk first, Monk second){
        if(first.energy > second.energy){
            return first;
        } else {
            return second;
        }
    }
}
