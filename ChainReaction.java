import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ChainReaction {

    private int guessCount;
    private int chainLength;
    private int currentIndex;
    String []gameWords;
    private boolean chainSet;

    ArrayList<ArrayList<String>> wordSets;
    ArrayList<String[]> chainWords = new ArrayList<>();
    Scanner input = new Scanner(System.in);

    public ChainReaction(int guesses, int chainLength,ArrayList<ArrayList<String>> set){
        this.guessCount = guesses;
        this.chainLength = chainLength;
        gameWords = new String[chainLength];
        wordSets = set;
        chainSet = false;
        currentIndex = 1;
    }

    public void playGame(){
        int guesses = 0;

        getWords();
        if(chainSet){
            createChain();
            updateChain();
            revealChainWord(0);
            revealChainWord(chainLength - 1);
            showChain();

            while(guesses <= getGuessCount()){
                System.out.println("Guesses Remaining: " + (getGuessCount() - guesses));
                System.out.print("Enter a guess for word " + (getCurrentIndex() + 1) + " :");
                String guess = input.nextLine().toLowerCase();
                if(guess.equals(gameWords[getCurrentIndex()])){
                    System.out.println("\nCorrect!....The word was " + guess);
                    revealChainWord(getCurrentIndex());
                    setCurrentIndex(getCurrentIndex()+1);
                    updateChain();
                    showChain();
                    if(getCurrentIndex() == chainLength - 1){
                        System.out.println("\nCONGRATULATIONS!  YOU HAVE COMPLETED THE CHAIN!\n");
                        break;
                    }
                }
                else{
                    System.out.println("\nIncorrect....Try Again");
                    updateChain();
                    showChain();
                }
                guesses++;
                if(guesses == getGuessCount()){
                    System.out.println("Sorry.  You have run out of guesses :(\n");
                    System.out.println("\nGAME OVER! :(");
                    System.out.println("Here is the chain:\n");
                    showChainWords();
                    break;
                }

            }
        }
    }

    public void getWords(){

        int randomSet = new Random().nextInt(wordSets.size());
        gameWords[0] = wordSets.get(randomSet).get(0);

        String prevWord = gameWords[0];

        prevWord = wordSets.get(randomSet).get(new Random().nextInt(wordSets.get(randomSet).size() - 1) + 1);
        gameWords[1] = prevWord;

        try{
            for (int i = 2; i < gameWords.length; i++) {
                boolean wordNeeded = true;
                int attempts = 0;
                while (wordNeeded) {
                    for (int j = 0; j < wordSets.size(); j++) {
                        if (prevWord.equals(wordSets.get(j).get(0))) {
                            prevWord = wordSets.get(j).get(new Random().nextInt(wordSets.get(j).size() - 1) + 1);
                            if (validateChain(prevWord, wordSets) || i == gameWords.length - 1) {
                                gameWords[i] = prevWord;
                                wordNeeded = false;
                            }
                        }
                    }
                    attempts++;
                    if (attempts > 20) {
                        throw new Exception("Unable to Complete Chain....Please Restart");
                    }
                }
            }
            chainSet = true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void showChainWords(){
        for (int i = 0; i < gameWords.length; i++) {
            System.out.println(gameWords[i]);
        }
    }

    public void createChain(){
        chainWords.clear();
        for (int i = 0; i < gameWords.length; i++) {
            String[] letters = new String[gameWords[i].length()];
            for (int j = 0; j < gameWords[i].length(); j++) {
                letters[j] = "_";
            }
            chainWords.add(letters);
        }
    }

    public void showChain(){
        for (int i = 0; i < chainWords.size(); i++) {
            for (int j = 0; j < chainWords.get(i).length; j++) {
                System.out.print(chainWords.get(i)[j] + " ");
            }
            System.out.println();
        }
    }

    public void updateChain(){
        for (int i = 0; i < chainWords.size(); i++) {
            boolean letterSet = false;
            for (int j = 0; j < chainWords.get(i).length; j++) {
                if(chainWords.get(i)[j].equals("_")){
                    chainWords.get(i)[j] = String.valueOf(gameWords[i].charAt(j));
                    letterSet = true;
                    break;
                }
            }
            if(letterSet){
                break;
            }
        }
    }

    public void revealChainWord(int index){
        chainWords.set(index,new String[]{gameWords[index]});
    }

    public boolean validateChain(String word, ArrayList<ArrayList<String>> wordSets){
        for(int i = 0; i < wordSets.size(); i++){
            for(int j = 0; j < wordSets.get(i).size(); j++){
                boolean wordFound = false;
                for(int k = 0; k < wordSets.size(); k++){
                    if(word.equals(wordSets.get(k).get(0))){
                        wordFound = true;
                        break;
                    }
                }
                if(!wordFound){
                    return false;
                }
            }
        }
        return true;
    }

    public int getGuessCount() {
        return guessCount;
    }

    public void setGuessCount(int guessCount) {
        this.guessCount = guessCount;
    }

    public int getChainLength() {
        return chainLength;
    }

    public void setChainLength(int chainLength) {
        this.chainLength = chainLength;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
