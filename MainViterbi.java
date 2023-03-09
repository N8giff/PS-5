/**
 * @title Viterbi Algorithm Main
 * @subtitle Assignment: PS-5
 * @Author Nathan Giffard
 * @class Dartmouth CS 10, Winter 2023
 * @date February 27th, 2023
 * @description Imports Brown files, trains algorithm, and decodes text with accuracy statistics
 */

import java.util.*;

public class MainViterbi {
    /**
     * Loads sentence and tag files, trains the algorithm and decodes the sentences.
     * Performs an accuracy test, comparing algorithm tags to given tags
     * @param args
     */
    public static void main(String[] args) {
        String brownTrainSentencePath = "PS5/texts/brown-train-sentences.txt";
        String brownTrainTagsPath = "PS5/texts/brown-train-tags.txt";

        String brownTestSentencePath = "PS5/texts/brown-test-sentences.txt";
        String brownTestTagsPath = "PS5/texts/brown-test-tags.txt";

        System.out.println("DECODING TEST: BROWN");
        ViterbiAlgorithm va2 = new ViterbiAlgorithm();
        ArrayList<String> trainingWords = va2.loadTrainingSentences(brownTrainSentencePath);
        ArrayList<String> trainingTags = va2.loadTrainingTags(brownTrainTagsPath);
        va2.train(trainingWords,trainingTags);

        //Decoding text
        System.out.println("Results:");
        ArrayList<String> obs = va2.loadObservations(brownTestSentencePath);
        va2.decode(obs);

        //Test accuracy
        va2.accuracyTest(brownTestTagsPath);

        va2.consoleInput(va2);
    }
}