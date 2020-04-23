package Lab7;

public class Duck extends GameObject {
    Thread thread;
    private boolean isMovingRight;
    private boolean isDead;

    Duck(int x, int y, int dx, int dy) {
        super(x, y, dx, dy);

        if (dx > 0) {
            isMovingRight = true;
            loadImage("duck_r.gif");
        } else {
            isMovingRight = false;
            loadImage("duck_l.gif");
        }
        width = 200;
        height = 100;


        thread = new Thread(this);
        thread.start();
    }

    void setIsDead(boolean b) {
        isDead = b;
        loadImage("dead_duck.jpeg");
    }

    boolean isDead() {
        return isDead;
    }

    @Override
    public void run() {
        while (!isDead) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            move();
            if (x > GamePanel.GAME_WIDTH - 200 || x < 0) {
                dx = -dx;
                if (isMovingRight) {
                    isMovingRight = false;
                    loadImage("duck_l.gif");
                } else {
                    isMovingRight = true;
                    loadImage("duck_r.gif");
                }
            }
        }
        try {
            Thread.sleep(sleepTime * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        visible = false;
    }

    @Override
    protected void loadImage(String imageName) {
        super.loadImage(imageName);
        image = image.getScaledInstance(100,100, 0);
        getImageDimensions();
    }

    @Override
    protected void getImageDimensions() {
        width = 100;
        height = 100;
    }
}
