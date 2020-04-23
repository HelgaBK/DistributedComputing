package Lab8;

import mpi.MPI;

//--------------------------Стрічкова схема--------------------------//

public class RibbonSchemeMatrix {
    public static void determine(String[] args, int matrixSize) {
        MPI.Init(args);
        int procRank = MPI.COMM_WORLD.Rank();

        int procNum = MPI.COMM_WORLD.Size();

        Matrix matrix_A = new Matrix(matrixSize, "A");
        Matrix matrix_B = new Matrix(matrixSize, "B");
        Matrix matrix_C = new Matrix(matrixSize, "C");
        long startTime = 0L;

        if (procRank == 0) {
            matrix_B.fillUpRandom(4);
            matrix_A.fillUpRandom(4);
            startTime = System.currentTimeMillis();
        }

        int lineHeight = matrixSize / procNum;

        int[] buffer_A = new int[lineHeight * matrixSize];
        int[] buffer_B = new int[lineHeight * matrixSize];
        int[] buffer_C = new int[lineHeight * matrixSize];


        MPI.COMM_WORLD.Scatter(matrix_A.matrix, 0, lineHeight * matrixSize, MPI.INT, buffer_A, 0, lineHeight * matrixSize, MPI.INT, 0);
        MPI.COMM_WORLD.Scatter(matrix_B.matrix, 0, lineHeight * matrixSize, MPI.INT, buffer_B, 0, lineHeight * matrixSize, MPI.INT, 0);

        int nextProc = (procRank + 1) % procNum;
        int prevProc = procRank - 1;
        if (prevProc < 0)
            prevProc = procNum - 1;
        int prevDataNum = procRank;

        for (int p = 0; p < procNum; p++) {
            for (int i = 0; i < lineHeight; i++)
                for (int j = 0; j < matrixSize; j++)
                    for (int k = 0; k < lineHeight; k++)

                        buffer_C[i * matrixSize + j] += buffer_A[prevDataNum * lineHeight + i * matrixSize + k] * buffer_B[k * matrixSize + j];
            prevDataNum -= 1;
            if (prevDataNum < 0)
                prevDataNum = procNum - 1;


            MPI.COMM_WORLD.Sendrecv_replace(buffer_B, 0, lineHeight * matrixSize, MPI.INT, nextProc, 0, prevProc, 0);
        }


        MPI.COMM_WORLD.Gather(buffer_C, 0, lineHeight * matrixSize, MPI.INT, matrix_C.matrix, 0, lineHeight * matrixSize, MPI.INT, 0);

        if (procRank == 0) {
            System.out.print("2) " + matrixSize + " x " + matrixSize + ", ");
            System.out.println(System.currentTimeMillis() - startTime + " ms");
        }
        MPI.Finalize();
    }
}