package Lab8;

public class MainRunner {
    public static void main(String[] args) {
        int[] sizes = {100, 1000, 5000};

        for (int matrixSize : sizes) {
            SimpleMatrix.determine(args, matrixSize);
            StringMatrix.determine(args, matrixSize);
            FoxMatrix.determine(args, matrixSize);
            CannonMatrix.determine(args, matrixSize);
        }
    }
}