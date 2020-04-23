package Lab6;

import java.util.Random;

public class Field {
    int width;
    int height;

    private volatile Cell[][] currentState;
    private volatile Cell[][] nextState;

    int[][] neighbourMask;


    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        currentState = new Cell[width][height];
        nextState = new Cell[width][height];

        neighbourMask = new int[][]{
                {-1, -1},
                {-1, 0},
                {-1, 1},
                {0, -1},
                {0, 1},
                {1, -1},
                {1, 0},
                {1, 1}
        };

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentState[i][j] = new Cell();
                nextState[i][j] = new Cell();
            }
        }
    }

    public void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentState[i][j].civilization = 0;
                nextState[i][j].civilization = 0;
            }
        }
    }

    public void generate(int numberOfСivilizations) {
        Random rand = new Random();
        int cellsPerCivilization = (int) (width * height * 0.5 / numberOfСivilizations);    // for what 0.5? (to make not every cell full of life)
        for (int i = 1; i <= numberOfСivilizations; i++) {               // why from i = 1 (to escape white color?)
            for (int j = 0; j < cellsPerCivilization; j++) {
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);

                if (currentState[x][y].civilization == 0) {       // if cell has not been occupied by some life earlier
                    currentState[x][y].civilization = i;
                }
            }
        }
    }

    public Cell getCell(int x, int y) {
        return currentState[x][y];
    }

    void updateField() {
        currentState = nextState;
        nextState = new Cell[height][width];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                nextState[i][j] = new Cell();
            }
        }
    }

    private int updateCell(int x, int y, int currentProcessingCivilization) {
        int countOfNeighbours = livingNeighbours(x, y, currentProcessingCivilization);

        // if current cell contains life of the current processing civilization
        if (currentState[x][y].civilization == currentProcessingCivilization) {
            // die because of loneliness ( < 2 neighbours)
            if (countOfNeighbours < 2) return 0;

                // live
            else if (countOfNeighbours == 2 || countOfNeighbours == 3) return currentProcessingCivilization;

                // die beacause of overfilled ( > 3 neighbours)
            else return 0;

            // if there is no life in the cell and count of neighbours of the current processing civilization == 3 then there is born new life of the the current processing civilization
        } else if (currentState[x][y].civilization == 0 && countOfNeighbours == 3) return currentProcessingCivilization;

        // if there is life of civilization that is not currently in processing, just don't touch it (return what is already in it's nextState!!!)
        return nextState[x][y].civilization;
    }

    private boolean isInBounds(int x, int y) {
        return (x >= 0 && x < height && y >= 0 && y < width);
    }

    private int livingNeighbours(int x, int y, int currentProcessingCivilization) {
        int result = 0;

        for (int i = 0; i < 8; i++) {
            int neighbourX = x + neighbourMask[i][0];
            int neighbourY = y + neighbourMask[i][1];
            if (isInBounds(neighbourX, neighbourY)) {
                if (currentState[neighbourX][neighbourY].civilization == currentProcessingCivilization) {
                    result++;
                }
            }
        }
        return result;
    }

    void simulate(int numberOfCivilizations, int fromX, int toX) {            // why we need fromX toX? (each thread has unique block)
        for (int civilization = 1; civilization <= numberOfCivilizations; civilization++) {
            for (int x = fromX; x < toX; x++) {
                for (int y = 0; y < width; y++) {

                    // delete locks (limits cells)

                    // read lock
                    int temp = updateCell(x, y, civilization);
                    // read unlock

                    // write lock ( не нужно, ибо у каждого своя полоса)
                    nextState[x][y].civilization = temp;
                    // write unlock
                }
            }
        }
    }

}
