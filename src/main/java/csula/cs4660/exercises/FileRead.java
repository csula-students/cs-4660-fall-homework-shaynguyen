package csula.cs4660.exercises;

import java.io.*;
import java.net.URL;
import java.util.Arrays;

/**
 * Introduction Java exercise to read file
 */
public class FileRead {
    private int[][] numbers = new int[5][8];

    /**
     * Read the file and store the content to 2d array of int
     *
     * @param file read file
     */
    public FileRead(File file) throws IOException {

        // TODO: read the file content and store content into numbers
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        int row = 0;

        while ((line = br.readLine()) != null) {

            String[] strNum = line.split(" ");
            int[] intNum = new int[strNum.length];


            for (int i = 0; i < intNum.length; i++) {
                numbers[row][i] = Integer.valueOf(strNum[i]);
            }

            row++;
        }

        br.close();
    }

    /**
     * Read the file assuming following by the format of split by space and next
     * line. Display the sum for each line and tell me
     * which line has the highest mean.
     * <p>
     * lineNumber starts with 0 (programming friendly!)
     */
    public int mean(int lineNumber) {
        int sum = sum(lineNumber);
        return sum  / 8;
    }

    public int max(int lineNumber) {
        int max = numbers[lineNumber][0];
        for (int i = 0; i < numbers[lineNumber].length; i++) {
            int value = numbers[lineNumber][i];
            if(value > max) {
                max = value;
            }
        }
        return max;
    }

    public int min(int lineNumber) {
        int min = numbers[lineNumber][0];
        for (int i = 0; i < numbers[lineNumber].length; i++) {
            int value = numbers[lineNumber][i];
            if(value < min) {
                min = value;
            }
        }
        return min;
    }

    public int sum(int lineNumber) {
        int sum = 0;
        for (int i = 0; i < numbers[lineNumber].length; i++) {
            sum += numbers[lineNumber][i];
        }
        return sum;
    }
}

