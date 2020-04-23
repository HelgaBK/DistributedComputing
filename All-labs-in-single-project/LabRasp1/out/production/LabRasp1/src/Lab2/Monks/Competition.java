package Lab2.Monks;

import java.util.concurrent.RecursiveTask;

public class Competition extends RecursiveTask<Monk> {
    private Monk[] monks;
    private int end;
    private int start;

    Competition(Integer monksNumber) {
        this.monks = new Monk[monksNumber];
        for(int i = 0; i < monksNumber; i++){
            this.monks[i] = new Monk();
        }

        for (int i =0; i < monksNumber; i++) {
            System.out.println(this.monks[i].getEnergy());
        }

        this.start = 0;
        this.end = monks.length - 1;
    }

    private Competition(Monk[] monks, int end, int start) {
        this.monks = monks;
        this.end = end;
        this.start = start;
    }

    @Override
    public Monk compute() {
        int length = end - start;

        if (length == 0) {
            return monks[end];
        }
        else if (length == 1) {
            return Monk.max(monks[end], monks[start]);
        } else {
            Competition leftChampion = new Competition(monks, start, (end + start)/2);
            leftChampion.fork();

            Competition rightChampion = new Competition(monks, (end + start)/2 + 1, end);
            rightChampion.fork();

            return Monk.max(leftChampion.join(), rightChampion.join());
        }
    }

}
