package ie.atu.sw;

import java.io.*;

public class Encoder {

    private String mappingFile = "./encodings-10000.csv";
    private String[][] cipherMatrix = new String[10_000][2];
    private String inputFile;
    private String outputFile = "./out.txt";

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) throws Exception {
        // Throw Exception if file does not exist/or is a directory
        File f = new File(inputFile);
        if (!f.exists() || f.isDirectory()) {
            throw new Exception("File not found");
        } else {
            this.inputFile = inputFile;
        }
    }

    public String getMappingFile() {
        return mappingFile;
    }

    public void setMappingFile(String mappingFile) throws Exception {
        // Throw exception if the file doesn't exist/or is a directory
        File f = new File(mappingFile);
        if (!f.exists() || f.isDirectory()) {
            throw new Exception("File not found");
        } else {
            this.mappingFile = mappingFile;
        }
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String filePath) throws Exception {
        outputFile = filePath;
    }

    public void readMappingFile() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mappingFile)));
        String next;
        int index = 0;

        // Create a 2d array to store all keys and their values
        String[][] mappingMatrix = new String[10_000][2];

        // Read mapping file and populate matrix
        while ((next = br.readLine()) != null) {
            String[] mappedWord = next.split(",");
            mappingMatrix[index] = mappedWord;
            index++;
        }

        br.close();
        cipherMatrix = mappingMatrix;
    }

    public void encodeFile() throws Exception {
        TextFileReader tfr = new TextFileReader();

        // Populate array words with the decrypted file
        String[] words = tfr.readDecryptedFile(inputFile);

        // Instantiate a new array three times bigger than the words one
        // to allow enough space for 2-cipher words and punctuations.
        int[] encodedArray = new int[words.length * 3];
        int encodedArrayIndex = 0;

        // Read the cipher file and populate the matrix
        readMappingFile();

        int progress = 0;

        // Loop through each word
        for (String word : words) {
            // Check if the word ends in punctuation, and if so, split them into two
            // different variables
            String punctuation = null;
            if (word.matches(".*\\p{Punct}")) {
                punctuation = String.valueOf(word.charAt(word.length() - 1));
                word = word.substring(0, word.length() - 1);
            }

            // Call getIdentifiers to obtain the codes for the word
            int[] identifiers = getIdentifiers(word);
            encodedArray[encodedArrayIndex] = identifiers[0];
            encodedArrayIndex++;

            // Check if the word is composed by prefix and suffix
            if (identifiers[1] != 0) {
                encodedArray[encodedArrayIndex] = identifiers[1];
                encodedArrayIndex++;
            }

            // If the word contained punctuation, add it at the end
            if (punctuation != null) {
                identifiers = getIdentifiers(punctuation);
                encodedArray[encodedArrayIndex] = identifiers[0];
                encodedArrayIndex++;

            }

            // Update progress bar
            System.out.print(ConsoleColour.YELLOW); // Change the colour of the console text
            ProgressBar.printProgress(progress + 1, words.length); // After each (some) steps, update the progress meter
            progress++;

        }

        // Output codes to the output file
        FileWriter fw = new FileWriter(outputFile);
        for (int i = 0; i < encodedArrayIndex; i++) {
            fw.write(String.valueOf(encodedArray[i]) + " ");
        }
        fw.flush();
        fw.close();
    }

    public int[] getIdentifiers(String word) {
        int[] identifiers = new int[2];

        // Look for a full match, if found, return
        for (int i = cipherMatrix.length - 1; i > 0; i--) {
            if (word.equals(cipherMatrix[i][0])) {
                identifiers[0] = Integer.parseInt(cipherMatrix[i][1]);
                return identifiers;
            }
        }

        // Look for partial matches
        for (int i = cipherMatrix.length - 1; i > 0; i--) {

            // If word starts with current key, it's a potential match
            if (word.startsWith(cipherMatrix[i][0])) {
                identifiers[0] = Integer.parseInt(cipherMatrix[i][1]);

                // Loop through ciphers again to try match the suffix
                for (int j = cipherMatrix.length - 1; j > 0; j--) {
                    String suffix;

                    // If cipher starts with @@, ignore the first 2 characters
                    if (cipherMatrix[j][0].startsWith("@@")) {
                        suffix = cipherMatrix[j][0].substring(2);
                    } else {
                        suffix = cipherMatrix[j][0];
                    }

                    // If a suffix is found
                    if (word.substring(cipherMatrix[i][0].length()).equals(suffix)) {
                        identifiers[1] = Integer.parseInt(cipherMatrix[j][1]);

                        // Check if prefix + suffix is the actual word and not a false positive
                        if (word.equals(cipherMatrix[i][0] + suffix)) {
                            return identifiers;
                        }
                    }
                }
            }
        }

        // If no match is found, 0 is returned
        return new int[] { 0, 0 };
    }

    public void decodeFile() throws Exception {
        TextFileReader tfr = new TextFileReader();
        int[] codes = tfr.readEncryptedFile(inputFile);
        StringBuilder sb = new StringBuilder();

        // Read cipher file and populate the matrix
        readMappingFile();

        int progress = 0;

        // We can use predefined knowledge about consiquence of codes 0 - 9'999
        // so we can simlify algorithm of decoding

        try {
            // Loop through all codes in the file
            for (int c : codes) {

                // get current token using corresponding code
                var currentKey = cipherMatrix[c][0];

                // If cipher value starts with @@, remove last character from string builder
                // which will be an empty space and append the value without @@
                if (currentKey.startsWith("@@")) {
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(currentKey.substring(2) + " ");
                    continue;
                }
                // If the current key is a punctuation, remove the last character
                // from string builder and append the punctuation
                else if (currentKey.matches("\\p{Punct}")) {
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(currentKey + " ");
                    continue;
                } else {
                    // If none of the above, simply append the text to the string builder
                    sb.append(currentKey + " ");
                }

                // Print progress bar
                System.out.print(ConsoleColour.YELLOW); // Change the colour of the console text
                ProgressBar.printProgress(progress + 1, codes.length); // After each (some) steps, update the progress
                                                                       // meter
                progress++;
            }
        } catch (Exception e) {
            throw new Exception("Decoding error - wrong code!!!");
        }

        // Write output to the file
        FileWriter fw = new FileWriter(outputFile);
        fw.write(String.valueOf(sb));
        fw.flush();
        fw.close();

    }

    public void swapFiles() {
        // Create temp variable to swap input and output
        String tmp = inputFile;
        inputFile = outputFile;
        outputFile = tmp;
    }

}
