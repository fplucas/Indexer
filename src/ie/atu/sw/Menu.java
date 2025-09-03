package ie.atu.sw;

import java.util.Scanner;

public class Menu {

    private boolean keepRunning = true;
    private Scanner s;
    private Encoder encoder;

    public Menu() {
        s = new Scanner(System.in);
        encoder = new Encoder();
    }

    public void start() {

        // keepRunning ensures that the program will only end when the user chooses option 8
        while(keepRunning) {
            showOptions();
            int choice = Integer.parseInt(s.next());
            switch (choice) {
                case 1  -> setMappingFile();
                case 2  -> setTextFileForEncoding();
                case 3  -> setOutputFile();
                case 4  -> swapFiles();
                case 5  -> printPaths();
                case 6  -> encodeFile();
                case 7  -> decodeFile();
                case 8  -> keepRunning = false;
                default -> System.out.println("[Error] Invalid Selection");
            }
        }

    }

    private static void showOptions() {

        System.out.println(ConsoleColour.WHITE);
        System.out.println("************************************************************");
        System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
        System.out.println("*                                                          *");
        System.out.println("*              Encoding Words with Suffixes                *");
        System.out.println("*                                                          *");
        System.out.println("*           Lucas Pugliesi Ferreira - G00472893            *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        System.out.println("(1) Specify Mapping File (default: ./encodings-1000.csv)");
        System.out.println("(2) Specify Text File to Encode/Decode");
        System.out.println("(3) Specify Output File (default: ./out.txt)");
        System.out.println("(4) Swap input and output files");
        System.out.println("(5) Print paths to files");
        System.out.println("(6) Encode Text File");
        System.out.println("(7) Decode Text File");
        System.out.println("(8) Quit");

        // Output a menu of options and solicit text from the user
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Select Option [1-7] > ");
        System.out.print(ConsoleColour.WHITE);
    }

    private void setMappingFile() {
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Specify path to the mapping file > ");
        String filePath = s.next();

        // try catch to handle exception
        try {
            encoder.setMappingFile(filePath);
            System.out.println("Mapping file set to "  + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTextFileForEncoding() {
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Specify path to the file for encoding > ");
        String filePath = s.next();

        // try catch to handle exception
        try {
            encoder.setInputFile(filePath);
            System.out.println("Encoding file set to "  + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOutputFile() {
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Specify path to the output file > ");
        String filePath = s.next();

        // try catch to handle exception
        try {
            encoder.setOutputFile(filePath);
            System.out.println("Output file set to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printPaths() {
        System.out.println(ConsoleColour.YELLOW);
        System.out.println("***************************************************");
        System.out.println("***                  File paths                 ***");
        System.out.println("***************************************************");
        System.out.println("Mapping file: " + encoder.getMappingFile());
        System.out.println("File for encoding: " + encoder.getInputFile());
        System.out.println("Output file: " + encoder.getOutputFile());
        System.out.println("");
    }

    private void encodeFile() {
        if (encoder.getInputFile() == null) {
            System.out.print(ConsoleColour.RED);
            System.out.println("No file for encoding was set. Process aborted");
        } else {

            // try catch to handle exception
            try {
                System.out.println(ConsoleColour.YELLOW);
                System.out.println("***************************************************");
                System.out.println("***                Encoding file                ***");
                System.out.println("***************************************************");
                encoder.encodeFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void decodeFile() {
        if (encoder.getInputFile() == null) {
            System.out.print(ConsoleColour.RED);
            System.out.println("No file for decoding was set. Process aborted");
        } else {

            // try catch to handle exception
            try {
                System.out.println(ConsoleColour.YELLOW);
                System.out.println("***************************************************");
                System.out.println("***                Decoding file                ***");
                System.out.println("***************************************************");
                encoder.decodeFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void swapFiles() {
        System.out.println(ConsoleColour.YELLOW);
        System.out.println("***************************************************");
        System.out.println("***                Swapped files                ***");
        System.out.println("***************************************************");
        encoder.swapFiles();
    }

}
