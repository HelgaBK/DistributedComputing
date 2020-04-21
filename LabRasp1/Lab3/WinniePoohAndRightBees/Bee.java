package Lab3.WinniePoohAndRightBees;

public class Bee implements Runnable{
    Bear bear;
    Pot pot;
    Bee nextBee;
    int numberOfBee;
    volatile boolean isActive = false;
    Bee (int numberOfBee, Bear bear, Pot pot){
        this.numberOfBee = numberOfBee;
        this.bear = bear;
        this.pot = pot;
    }
    public void setNextBee(Bee bee){
        this.nextBee = bee;
    }
    public void setActive(){
        isActive = true;
    }
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Bee #" + numberOfBee + " + 1 honey");
            pot.addHoney();
            if (pot.isFull()) {
                System.out.println("Is full");
                bear.wakeUp();
            }
            isActive = false;
            //notifyAll();
           // nextBee.setActive();
        }
    }
}
