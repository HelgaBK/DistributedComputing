package Lab8;

import mpi.Cartcomm;
import mpi.MPI;

import java.util.Arrays;

//--------------------------Метод Канона--------------------------//
public class CannonMatrix {
    private static int[] gridCoords = new int[2];
    private static Cartcomm ColComm;
    private static Cartcomm RowComm;

    private static void matrixScatter(int[] matrix, int[] matrixBlock, int matrixSize, int blockSize) {
        int[] matrixRow = new int[blockSize * matrixSize];
        if (gridCoords[1] == 0)
            ColComm.Scatter(matrix, 0, blockSize * matrixSize, MPI.INT, matrixRow, 0, blockSize * matrixSize, MPI.INT, 0);
        for (int i = 0; i < blockSize; i++) {
            int[] subRow = Arrays.copyOfRange(matrixRow, i * matrixSize, matrixRow.length);
            int[] subRowRes = new int[blockSize];

            RowComm.Scatter(subRow, 0, blockSize, MPI.INT, subRowRes, 0, blockSize, MPI.INT, 0);
            System.arraycopy(subRowRes, 0, matrixBlock, i * blockSize, blockSize);
        }
    }

    public static void determine(String[] args, int matrixSize) {
        MPI.Init(args);

        int procRank = MPI.COMM_WORLD.Rank();
        int procNum = MPI.COMM_WORLD.Size();
        int gridSize = (int) Math.sqrt(procNum);

        if (procNum != gridSize * gridSize) {
            if (procRank == 0)
                System.out.println("4) " + matrixSize + " x " + matrixSize + ", 0 ms (procNum != gridSize * gridSize)");
            MPI.Finalize();
            return;
        }

        Cartcomm gridComm;

        int blockSize = matrixSize / gridSize;

        Matrix matrix_A = new Matrix(matrixSize, "A");
        Matrix matrix_B = new Matrix(matrixSize, "B");
        Matrix matrix_C = new Matrix(matrixSize, "C");

        int[] A_Block = new int[blockSize * blockSize];
        int[] B_Block = new int[blockSize * blockSize];
        int[] C_Block = new int[blockSize * blockSize];

        long startTime = 0L;
        if (procRank == 0) {
            matrix_A.fillUpRandom(5);
            matrix_B.fillUpRandom(4);
            startTime = System.currentTimeMillis();
        }

        boolean[] subdims = new boolean[2];

        gridComm = MPI.COMM_WORLD.Create_cart(new int[]{gridSize, gridSize}, new boolean[]{false, false}, true);
        gridCoords = gridComm.Coords(procRank);
        subdims[1] = true;
        RowComm = gridComm.Sub(subdims);

        subdims[0] = true;
        subdims[1] = false;
        ColComm = gridComm.Sub(subdims);

        matrixScatter(matrix_A.matrix, A_Block, matrixSize, blockSize);
        matrixScatter(matrix_B.matrix, B_Block, matrixSize, blockSize);

        if (gridCoords[0] != 0) {
            int nextProc = gridCoords[1] - gridCoords[0];
            if (nextProc < 0)
                nextProc += gridSize;
            RowComm.Sendrecv_replace(A_Block, 0, blockSize * blockSize, MPI.INT, nextProc, 0, MPI.ANY_SOURCE, 0);
        }

        if (gridCoords[1] != 0) {
            int nextProc = gridCoords[0] - gridCoords[1];
            if (nextProc < 0) nextProc += gridSize;
            ColComm.Sendrecv_replace(B_Block, 0, blockSize * blockSize, MPI.INT, nextProc, 1, MPI.ANY_SOURCE, 1);
        }

        MPI.COMM_WORLD.Barrier();

        for (int i = 0; i < blockSize; i++)
            for (int j = 0; j < blockSize; j++)
                for (int k = 0; k < blockSize; k++)
                    C_Block[i * blockSize + j] += A_Block[i * blockSize + k] * B_Block[k * blockSize + j];


        for (int iter = 0; iter < gridSize - 1; iter++) {
            int nextProc = gridCoords[1] - 1;
            if (nextProc < 0)
                nextProc += gridSize;
            RowComm.Sendrecv_replace(A_Block, 0, blockSize, MPI.INT, nextProc, 0, MPI.ANY_SOURCE, 0);

            nextProc = gridCoords[0] - 1;
            if (nextProc < 0)
                nextProc += gridSize;

            ColComm.Sendrecv_replace(B_Block, 0, blockSize, MPI.INT, nextProc, 1, MPI.ANY_SOURCE, 1);

            for (int i = 0; i < blockSize; i++)
                for (int j = 0; j < blockSize; j++)
                    for (int k = 0; k < blockSize; k++)
                        C_Block[i * blockSize + j] += A_Block[i * blockSize + k] * B_Block[k * blockSize + j];
        }

        int[] resultRow = new int[matrixSize * blockSize];
        for (int i = 0; i < blockSize; i++) {
            int[] subRow = Arrays.copyOfRange(C_Block, i * blockSize, C_Block.length);
            int[] subRowRes = new int[gridSize * blockSize];

            RowComm.Gather(subRow, 0, blockSize, MPI.INT, subRowRes, 0, blockSize, MPI.INT, 0);
            System.arraycopy(subRowRes, 0, resultRow, i * matrixSize, gridSize * blockSize);
        }

        if (gridCoords[1] == 0)
            ColComm.Gather(resultRow, 0, blockSize * matrixSize, MPI.INT, matrix_C.matrix, 0, blockSize * matrixSize, MPI.INT, 0);

        if (procRank == 0) {
            System.out.print("4) " + matrixSize + " x " + matrixSize + ", ");
            System.out.println(System.currentTimeMillis() - startTime + " ms\n");
        }
        MPI.Finalize();
    }
}