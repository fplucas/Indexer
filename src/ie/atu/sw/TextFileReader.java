package ie.atu.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class TextFileReader {

    private static final int INITIAL_CAPACITY = 100;
    private String[] words = null;

    public TextFileReader() {
        words = new String[INITIAL_CAPACITY];
    }

    public String[] readDecryptedFile(String filePath) throws Exception {

        File file = new File(filePath);

        Scanner sc = new Scanner(file);
        int wordsIndex = 0;

        // While there are words in the file, append them to the array words
        while (sc.hasNext()) {

            // If we run out of space in the array, call getExpandedIndex to increase it
            if(wordsIndex >= words.length) {
                getExpandedIndex();
            }

            // Convert words to lower case
            words[wordsIndex] = sc.next().toLowerCase();
            wordsIndex++;
        }

        sc.close();

        // Call clearArray to remove empty spaces
        clearArray();

        return words;
    }

    public int[] readEncryptedFile(String filePath) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

        // Read the file (one-liner) and break it down into words (separated
        //  by any white space) to an array
        String[] encryptedWords = br.readLine().split("\s");
        int[] encryptedWordCiphers = new int[encryptedWords.length];

        // Convert codes to integer
        for (int i = 0; i < encryptedWords.length; i++) {

            // Try catch to validate if the input file is actually encrypted
            try {
                encryptedWordCiphers[i] = Integer.parseInt(encryptedWords[i]);
            } catch (Exception e) {
                System.out.println(ConsoleColour.RED);
                System.out.println("The input file is not encrypted. Aborting");
                break;
            }
        }

        return encryptedWordCiphers;
    }


    // Expand array
    private int getExpandedIndex() {

        // Create a temporary array double the size of the current one
        String[] temp = new String[words.length * 2];


        // Copy data from current array to the temp
        for (int i = 0; i < words.length; i++) {
            temp[i] = words[i];
        }

        int index = words.length;

        // Replace array by the temp
        words = temp;

        // Return where it was left of
        return index;
    }

    private void clearArray() {

        // Check how many slots in the array contain actual data
        int count = 0;
        for(String s : words) {
            if(s != null) {
                count++;
            }
        }

        // Create a temp array using the count of elements with data
        String[] temp = new String[count];

        // Populate temp array
        int i = 0;
        for(String s: words) {
            if(s != null) {
                temp[i] = s;
                i++;
            }
        }

        // Replace current array with temp
        words = temp;
    }

}
