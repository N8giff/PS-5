/**
 * @title Viterbi Algorithm
 * @subtitle Assignment: PS-5
 * @Author Nathan Giffard
 * @class Dartmouth CS 10, Winter 2023
 * @date February 27th, 2023
 * @description Loads training texts and tags, trains algorithm and decodes the test text.
 */
import java.util.*;
import java.io.*;
public class ViterbiAlgorithm {
    ArrayList<String> testTags;
    ArrayList<String> testSentences;
    Map<String,Map<String,Integer>> transWords;
    Map<String, Map<String, Integer>> transTags;
    Map<String, Map<String,Double>> trainedWords;
    Map<String,Map<String, Double>> trainedTags;

   ArrayList<String> path = new ArrayList<>();
   BufferedReader in = null;

    /**
     * Get trained words
     */
    public Map<String,Map<String, Double>> getTrainedWords(){
        return trainedWords;
    }

    /**
     * Get trained tags
     */
    public Map<String,Map<String, Double>> getTrainedTags(){
        return trainedTags;
    }

    /**
     * Load the words of each sentence into an array
     */
    public ArrayList<String> loadTrainingSentences(String path){
        testSentences = new ArrayList<>();
        try{
            in = new BufferedReader(new FileReader(path));
            String line;
            while((line = in.readLine()) != null){
                String lower = line.toLowerCase();
                String [] ids = lower.split(" ");
                testSentences.add("#");
                Collections.addAll(testSentences, ids);
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("Sentence file not found.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return testSentences;
    }

    /**
     * Load the tags of each sentence into an array
     */
    public ArrayList<String> loadTrainingTags(String path){
        testTags = new ArrayList<>();
        try{
            in = new BufferedReader(new FileReader(path));
            String line;
            while((line = in.readLine()) != null){
                String lower = line.toLowerCase();
                String[] ids = lower.split(" ");
                testTags.add("#");
                Collections.addAll(testTags, ids);
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("Tag file not found.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return testTags;
    }

    public ArrayList<String> loadObservations(String path){
        ArrayList<String> observations = new ArrayList<>();
        try{
            in = new BufferedReader(new FileReader(path));
            String line;
            while((line = in.readLine()) != null){
                String lower = line.toLowerCase();
                String [] ids = lower.split(" ");
                observations.add("#");
                observations.addAll(Arrays.asList(ids));
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("Observation file not found.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return observations;
    }

    /**
     * Uses sentences and tags to train program and produce transmission and emission maps
     * @param testSentences ArrayList of words in sentences
     * @param testTags  ArrayList of corresponding tags for words
     */
    public void train(ArrayList<String> testSentences, ArrayList<String> testTags){
        transWords = new HashMap<>();
        transTags = new HashMap<>();

        int i = 0;  //counter to track next tag

        //Loop through tags...
        //create a transition map of the tags
        //tags -> tags -> count
        for(String tag : testTags){
            if(i != testTags.size()-2){
                i += 1;
                if(transTags.containsKey(tag)){
                    if (transTags.get(tag).containsKey(testTags.get(i))) {
                        int newCount = transTags.get(tag).get(testTags.get(i)) + 1;
                        transTags.get(tag).put(testTags.get(i),newCount);
                    }
                    else{
                        transTags.get(tag).put(testTags.get(i), 1);
                    }
                }
                else{
                    String next = testTags.get(i);
                    Map<String, Integer> newTag = new HashMap<>();
                    newTag.put(next,1);
                    transTags.put(tag,newTag);
                }
            }
        }
        //reset counter to track tags
        i = 0;

        //Loop through words...
        //create an emission map of the words
        //tags -> words -> count
        for(String word : testSentences){
            if(transWords.containsKey(testTags.get(i))){
                if(transWords.get(testTags.get(i)).containsKey(word)){
                    int current = transWords.get(testTags.get(i)).get(word) + 1;
                    transWords.get(testTags.get(i)).put(word,current);
                }
                else{
                    transWords.get(testTags.get(i)).put(word,1);
                }
            }
            else{
                Map<String, Integer> newWord = new HashMap<>();
                newWord.put(word,1);
                transWords.put(testTags.get(i),newWord);
            }
            i += 1;
        }

        //Loop through the tags in transTags and create a transition map
        // tag -> tag -> probability
        trainedTags = new HashMap<>(); //final transition map
        for(String tag : transTags.keySet()){
            i = 0; //reset to use i for total # hits for a given tag
            for(String currTag : transTags.get(tag).keySet()){
                i = i + transTags.get(tag).get(currTag);
            }
            Map<String, Double> nextState = new HashMap<>();
            for(String currTag : transTags.get(tag).keySet()){
                double prob = (double)(transTags.get(tag).get(currTag)) / (double)(i);
                nextState.put(currTag,Math.log(prob));
            }
            trainedTags.put(tag,nextState);
        }

        //Loop through the words in transWords and create an emission map
        // tag -> word -> probability
        trainedWords = new HashMap<>(); //final emission map
        for(String tag : transWords.keySet()){
            i = 0; //reset count
            for(String currWord : transWords.get(tag).keySet()){
                i = i + transWords.get(tag).get(currWord);
            }
            Map<String,Double> nextState = new HashMap<>();
            for(String currWord : transWords.get(tag).keySet()){
                double prob = (double)(transWords.get(tag).get(currWord)) / (double)(i);
                nextState.put(currWord,Math.log(prob));
            }
            trainedWords.put(tag,nextState);
        }
    }

    /**
     * Uses the Viterbi Algorithm to tag the input words
     * @param obs   ArrayList of words to tag
     */
    public void decode(ArrayList<String> obs){
        ArrayList<Map<String,String>> trace = new ArrayList<>();
        Set<String> currStates = new HashSet<>();
        currStates.add("#");
        Map<String,Double> currScores = new HashMap<>();
        currScores.put("#",0.0);

        for(int i = 0 ; i < obs.size() -1; i++){
            Set<String> nextStates = new HashSet<>();
            Map<String, Double> nextScores = new HashMap<>();
            Map<String, String> backTrace = new HashMap<>();

            for(String currState : currStates){
                for(String nextState : trainedTags.get(currState).keySet()){
                    nextStates.add(nextState);
                    double nextScore;
                    if(!trainedWords.get(nextState).containsKey(obs.get(i+1))){
                        nextScore = (double)currScores.get(currState) + trainedTags.get(currState).get(nextState) - 1000; //penalty of -10
                    }
                    else{
                        nextScore = currScores.get(currState) + trainedTags.get(currState).get(nextState)+ trainedWords.get(nextState).get(obs.get(i+1));
                    }
                    if(!nextScores.containsKey(nextState) || nextScore > nextScores.get(nextState)){
                        nextScores.put(nextState,nextScore);
                        backTrace.put(nextState, currState);
                    }
                }
            }
            trace.add(backTrace);
            currStates = nextStates;
            currScores = nextScores;
        }
        //find the highest score in the currScores map
        String highScore = null;
        for(String x : currScores.keySet()){
            if(highScore == null){
                highScore = x;
            }
            else if(currScores.get(x) > currScores.get(highScore)){
                highScore = x;
            }
        }

        //print results
        path = new ArrayList<>();
        String current = highScore;
        for(int i = trace.size() -1; i > -1; i--){
            path.add(0,current);
            current = trace.get(i).get(current);
        }
        printTags(path);
    }

    /**
     * Calculates accuract of algorithm tags using a given file
     * @param tagPath   file used as gold standard to compare algorithm output
     */
    public void accuracyTest(String tagPath){
        ArrayList<String> performance = new ArrayList<>();
        BufferedReader in;
        try{
            in = new BufferedReader(new FileReader(tagPath));
            String line;
            while((line = in.readLine()) != null){
                String lower = line.toLowerCase();
                String[] ids = lower.split(" ");
                performance.add("#");
                performance.addAll(Arrays.asList(ids));
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("Tag file not loaded");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //printTags("Expected tags: " + "\n" + performance);

        int matches = 0;
        int mismatches = 0;
        int sentCount = 1;
        for(int i = 0; i < path.size(); i++){
            if(performance.get(i+1).equals("#")){
                sentCount += 1;
            }
            else if(performance.get(i+1).equals(path.get(i))){
                matches +=1;
            }
            else{
                mismatches += 1;
            }
        }
        int totalTags = matches + mismatches;
        double percentAcc = (double)matches * 100 / totalTags;
        System.out.println(" ");
        System.out.println("The algorithm tagged " +
                sentCount + " sentences with " + percentAcc +
                "% accuracy." + "\n" + "There were " + matches + " tag matches and " +
                mismatches + " tag mismatches.");
    }
    /**
     * Helper method to print tags to console
     * @param tags  ArrayList of tags to print
     */
    public void printTags(ArrayList<String> tags){
        for (String s : tags) {
            if (s.equals(".")) {
                System.out.print(".");
                System.out.println();
            } else if (s.equals("#")) {
                System.out.print("");
            } else {
                System.out.print(s.toUpperCase() + " ");
            }
        }
    }

    /**
     * Takes input sentences from console and returns tags
     * @param va    Viterbi Algorithm object
     */
    public void consoleInput(ViterbiAlgorithm va){
        ArrayList<String> inputs = new ArrayList<>();
        System.out.println("\n" + "Enter a sentence to tag. Enter 'q' to quit");
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println(":>");
            String sentence = in.nextLine();
            String[] words = sentence.split(" ");
            //System.out.println("Number of words: " + words.length);
            if(sentence.equals("q")){
                break;
            }
            inputs.add("#");
            Collections.addAll(inputs, words);
            if(inputs.size() == 0){
                System.out.println("Invalid input. Please try again...");
            }
            else{
                va.decode(inputs);
            }
            inputs = new ArrayList<>();
            System.out.println("\n");
        }
    }
}