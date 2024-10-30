import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class ChainReactionMain {

    public static void main(String[] args) {

        ArrayList<ArrayList<String>> wordSets = new ArrayList<>();
        String filename = "wordList.txt";

        try {
            FileInputStream file = new FileInputStream(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String words = scanner.nextLine();
                String[] wordArray = words.split(",");
                ArrayList<String> row = new ArrayList<>(Arrays.asList(wordArray));
                wordSets.add(row);
            }
        } catch (FileNotFoundException e) {
            System.out.printf("File %s does not exist\n", filename);
        }

        cleanData(wordSets);

        while (true) {
            int chainLength = 0;
            int guesses = 0;

            System.out.println("*********************************");
            System.out.println("*                               *");
            System.out.println("*       CHAIN REACTION          *");
            System.out.println("*  CAN YOU COMPLETE THE CHAIN?  *");
            System.out.println("*                               *");
            System.out.println("*********************************\n");
            System.out.println("Tutorial..................press 0");
            System.out.println("Beginner..................press 1");
            System.out.println("Pro.......................press 2");
            System.out.println("Superstar.................press 3");
            System.out.println("Custom....................press 4\n");
            System.out.print("SELECT DIFFICULTY: ");
            int difficulty = new Scanner(System.in).nextInt();
            switch (difficulty) {
                case 0: {
                    chainLength = 3;
                    guesses = 3;
                    break;
                }
                case 1: {
                    chainLength = 3;
                    guesses = 10;
                    break;
                }
                case 2: {
                    chainLength = 5;
                    guesses = 15;
                    break;
                }
                case 3: {
                    chainLength = 7;
                    guesses = 20;
                    break;
                }
                default: {
                    System.out.print("Enter Chain Length: ");
                    chainLength = new Scanner(System.in).nextInt();
                    System.out.print("Enter Number of Guesses: ");
                    guesses = new Scanner(System.in).nextInt();
                    break;
                }
            }

            ChainReaction c = new ChainReaction(guesses, chainLength, wordSets);
            c.playGame();
            System.out.println("\nPlay Again (y)es or (n)o");
            char option = new Scanner(System.in).next().toLowerCase().charAt(0);
            if (option == 'n') {
                System.out.println("\nTHANK YOU FOR PLAYING!");
                break;
            }
        }
    }

    /**
     * You will need to remove invalid words from the ArrayList. An invalid word
     * will fit at least one of the following criteria:
     * 1. The word never appears as the first word in a row of words.
     * 2. A row of words only has 1 valid word.
     * 3. If one instance of a word is removed all instances of that word must be removed.
     **/
    public static void cleanData(ArrayList<ArrayList<String>> wordSets) {
        /**Stores all invalid words here to be deleted**/
        ArrayList<String> invalidWords = new ArrayList<>(); //Initialize new String ArrayList to store invalid words

        /**
         * Invalid words never appear as the first word in a row of words
         * To remove words that are never the first word refer to step 1 and step 2
         **/

        /**Step 1: Identify all words that appear as the first word in any row**/
        ArrayList<String> firstWords = new ArrayList<>(); //ArrayList that holds strings to store first word of each row
        for (ArrayList<String> wordSet : wordSets) { //Iterate over each word set in the collection of word sets
            if (!wordSet.isEmpty()) { //If the word set is not empty
                firstWords.add(wordSet.get(0)); //Add the first word of the word set (the word at index zero) to the firstWords list
            }
        }

        /**Step 2: Compare all words in wordsets to the words in firstWords, if the word does not appear in firstWords it is invalid**/
        for (ArrayList<String> wordSet : wordSets) { //Iterate over each word set in the list of word sets
            for (String word : wordSet) { //Iterate over each word in the current word set
                if (!firstWords.contains(word)) { //If the word is not in the firstWords list
                    invalidWords.add(word); //Add the word to the invalidWords list
                }
            }
        }

        /**
         * If there is only one valid word, that entire set is then invalid
         **/
        for (int i = 0; i < wordSets.size(); i++) { //Iterate over each wordsset in the list of word sets
            ArrayList<String> wordSet = wordSets.get(i); //Get the current wordset
            wordSet.removeIf(invalidWords::contains); //Remove words from the wordset that are in the invalidWords list
            if (wordSet.size() <= 1) { //If that word set does not have more than one valid word
                invalidWords.addAll(wordSet); //Add all words in that word set to the invalidWords list
                wordSets.remove(i); //Remove the word set from the list of word sets
                i--; //Subtract one from the index to account for the removal
            }
        }

        /**
         * If one instance of a word is removed, all instances of that word must be removed
         **/
        for (ArrayList<String> wordSet : wordSets) { //Iterate over each word set in the list of word sets
            wordSet.removeIf(invalidWords::contains); //Remove words from the word set that are in the invalidWords list
        }

        /**
         * Base case: If no invalid words were found and removed, stop the recursion
         **/
        if (invalidWords.isEmpty()) { //Check if the invalidWords list is empty, if so
            validate(wordSets); //Call validate to check the dataset status
            return; //Return to end the recursion
        }

        /**
         * Recursive case: If invalid words were found and removed, call cleanData again
         **/
        cleanData(wordSets); //Call cleanData again to continue cleaning the words (do the same thing over again)
    }

    public static void validate(ArrayList<ArrayList<String>> wordSets) {
        final int wordCountValid = 8033;
        final int wordSetCountValid = 2334;

        int numTotalWords = 0;
        for (ArrayList<String> wordSet : wordSets) {
            for (int j = 0; j < wordSet.size(); j++) {
                numTotalWords++;
            }
        }
        System.out.println("Total Word Count: " + numTotalWords);
        System.out.println("Number of Unique Words: " + wordSets.size());
        String status = "Incomplete";
        if (wordCountValid == numTotalWords && wordSetCountValid == wordSets.size()) {
            status = "Complete";
        }
        System.out.println("Dataset Cleaning: " + status);
    }
}
