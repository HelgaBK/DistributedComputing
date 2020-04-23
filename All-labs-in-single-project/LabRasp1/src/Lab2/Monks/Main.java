package Lab2.Monks;

import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        final Integer monksNumber = 55;
        Competition comp = new Competition(monksNumber);
        ForkJoinPool pool = new ForkJoinPool();
        Monk winner = pool.invoke(comp);
        String monastery;
        if(winner.getMonastery() == 0){
            monastery = "Huan-un";
        }
        else{
            monastery = "Huan-in";
        }
        System.out.println("Winner: " + winner.getEnergy() + " Monastary: " +  monastery);
    }
}

