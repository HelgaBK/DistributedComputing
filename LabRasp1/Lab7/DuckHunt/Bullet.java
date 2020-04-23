package Lab7;

public class Bullet extends GameObject {
    Thread thread;

    public Bullet(int x, int y) {
        super(x, y, 0, -5);

        dy = -5;

        loadImage("bullet.png");
        getImageDimensions();

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (visible) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            move();
            if (y < 0) {
                visible = false;
                thread.interrupt();
            }
        }
    }

//    @Override
//    protected void loadImage(String imageName) {
//        super.loadImage(imageName);
//        image = image.getScaledInstance(20,50, 1);
//        getImageDimensions();
//    }
//
//    @Override
//    protected void getImageDimensions() {
//        width = 20;
//        height = 50;
//    }

}
