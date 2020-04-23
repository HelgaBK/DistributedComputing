package Lab8;

import mpi.MPI;

//--------------------------Послідовний алгоритм--------------------------//

public class SimpleMatrix {
    public static void determine(String[] args, int matrixSize) {

        MPI.Init(args);
        int procRank = MPI.COMM_WORLD.Rank();

        Matrix matrix_A = new Matrix(matrixSize, "A");
        Matrix matrix_B = new Matrix(matrixSize, "B");
        Matrix matrix_C = new Matrix(matrixSize, "C");
        long startTime = 0L;

        if (procRank == 0) {
            matrix_A.fillUpRandom(3);
            matrix_B.fillUpRandom(3);
            startTime = System.currentTimeMillis();
        }

        for (int i = 0; i < matrix_A.width; i++)
            for (int j = 0; j < matrix_B.height; j++)
                for (int k = 0; k < matrix_A.height; k++)
                    matrix_C.matrix[i * matrix_A.width + j] += matrix_A.matrix[i * matrix_A.width + k] * matrix_B.matrix[k * matrix_B.width + j];

        if (procRank == 0) {
            System.out.print("1) " + matrixSize + " x " + matrixSize + ", ");
            System.out.println(System.currentTimeMillis() - startTime + " ms");
        }
        MPI.Finalize();
    }
}