using System;
using System.Diagnostics;
using System.Threading.Tasks;

class MultiplyMatrices
{
    #region Parallel_Loop
    static void MultiplyMatricesParallel(int[,] matA, int[,] matB, int[,] result)
    {
        int matACols = matA.GetLength(1);
        int matBCols = matB.GetLength(1);
        int matARows = matA.GetLength(0);
        
        Parallel.For(0, matARows, i =>
        {
            for (int j = 0; j < matBCols; j++)
            {
                int temp = 0;
                for (int k = 0; k < matACols; k++)
                {
                    temp += matA[i, k] * matB[k, j];
                }
                result[i, j] = temp;
            }
        });
    }
    #endregion


    #region Main
    static void Main(string[] args)
    {
        unsafe
        {

        }
        int colCount = 1000;
        int rowCount = 1000;
        int[,] m1 = InitializeMatrix(rowCount, colCount);
        int[,] m2 = InitializeMatrix(colCount, colCount);
        int[,] result = new int[rowCount, colCount];

        Stopwatch stopwatch = new Stopwatch();
        stopwatch.Start();
        MultiplyMatricesParallel(m1, m2, result);
        stopwatch.Stop();
        Console.Error.WriteLine("1000 x 1000: {0} ms", stopwatch.ElapsedMilliseconds);
    }
    #endregion

    #region Helper_Methods
    static int[,] InitializeMatrix(int rows, int cols)
    {
        int[,] matrix = new int[rows, cols];

        Random r = new Random();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                matrix[i, j] = r.Next(100);
            }
        }
        return matrix;
    }
    #endregion
}