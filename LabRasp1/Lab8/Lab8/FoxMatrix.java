package Lab8;

import mpi.Cartcomm;
import mpi.MPI;

import javax.print.DocFlavor;
import java.util.Arrays;

//--------------------------Метод Фокса--------------------------//

public class FoxMatrix {
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

    public static void determine(String[] args, int matSize) {
        MPI.Init(args);
        int procRank = MPI.COMM_WORLD.Rank();
        int procNum = MPI.COMM_WORLD.Size();

        int gridSize = (int) Math.sqrt(procNum);

        if (procNum != gridSize * gridSize) {
            if (procRank == 0)
                System.out.println("3) " + matSize + " x " + matSize + ", 0 ms (procNum != gridSize * gridSize)");
            MPI.Finalize();
            return;
        }

        Cartcomm gridComm;

        int blockSize = matSize / gridSize;

        var matrix_A = new Matrix(matSize, "A");
        var matrix_B = new Matrix(matSize, "B");
        var matrix_C = new Matrix(matSize, "C");

        int[] A_Block = new int[blockSize * blockSize];
        int[] BBlock = new int[blockSize * blockSize];
        int[] C_Block = new int[blockSize * blockSize];
        int[] tempABlock = new int[blockSize * blockSize];

        long startTime = 0L;
        if (procRank == 0) {
            matrix_A.fillUpRandom(5);
            matrix_B.fillUpRandom(5);
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

        matrixScatter(matrix_A.matrix, tempABlock, matSize, blockSize);
        matrixScatter(matrix_B.matrix, BBlock, matSize, blockSize);

        for (int iter = 0; iter < gridSize; iter++) {
            int pivot = (gridCoords[0] + iter) % gridSize;

            if (gridCoords[1] == pivot)
                if (blockSize * blockSize >= 0)
                    System.arraycopy(tempABlock, 0, A_Block, 0, blockSize * blockSize);

            RowComm.Bcast(A_Block, 0, blockSize * blockSize, MPI.INT, pivot);

            for (int i = 0; i < blockSize; i++)
                for (int j = 0; j < blockSize; j++)
                    for (int k = 0; k < blockSize; k++)
                        C_Block[i * blockSize + j] += A_Block[i * blockSize + k] * DocFlavor.BYTE_ARRAYBlock[k * blockSize + j];


            int nextProc = gridCoords[0] + 1;
            if (gridCoords[0] == gridSize - 1)
                nextProc = 0;
            int prevProc = gridCoords[0] - 1;
            if (gridCoords[0] == 0)
                prevProc = gridSize - 1;

            ColComm.Sendrecv_replace(BBlock, 0, blockSize * blockSize, MPI.INT, nextProc, 0, prevProc, 0);
        }

        int[] resultRow = new int[matSize * blockSize];
        for (int i = 0; i < blockSize; i++) {
            int[] subRow = Arrays.copyOfRange(C_Block, i * blockSize, C_Block.length);
            int[] subRowRes = new int[gridSize * blockSize];

            RowComm.Gather(subRow, 0, blockSize, MPI.INT, subRowRes, 0, blockSize, MPI.INT, 0);
            if (gridSize * blockSize >= 0)
                System.arraycopy(subRowRes, 0, resultRow, i * matSize, gridSize * blockSize);
        }

        if (gridCoords[1] == 0)
            ColComm.Gather(resultRow, 0, blockSize * matSize, MPI.INT, matrix_C.matrix, 0, blockSize * matSize, MPI.INT, 0);

        if (procRank == 0) {
            System.out.print("3) " + matSize + " x " + matSize + ", ");
            System.out.println(System.currentTimeMillis() - startTime + " ms");
        }
        MPI.Finalize();
    }
}