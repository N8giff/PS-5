/**
 * @title Viterbi Algorithm Testing
 * @subtitle Assignment: PS-5
 * @Author Nathan Giffard
 * @class Dartmouth CS 10, Winter 2023
 * @date February 27th, 2023
 * @description Test cases for the Viterbi Algorithm
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ViterbiTesting {
    public static void main(String[] args) {
        String testSentencePath = "PS5/texts/test-sentences.txt";
        String testTagPath = "PS5/texts/test-tags.txt";
        String exampleSentencePath = "PS5/texts/example-sentences.txt";
        String exampleTagsPath = "PS5/texts/example-tags.txt";
        String simpleTrainSentencePath = "PS5/texts/simple-train-sentences.txt";
        String simpleTrainTagsPath = "PS5/texts/simple-train-tags.txt";
        String SimpleTestSentencePath = "PS5/texts/simple-test-sentences.txt";
        String SimpleTestTagsPath = "PS5/texts/simple-test-tags.txt";

        //Test loading sentences and tags
        ArrayList<String> sentenceTest = new ArrayList<>(Arrays.asList("#", "cat","chase","dog", "#","cat",
                "watch","chase", "#","chase","get","watch","#","chase","watch","dog","and","cat", "#","dog","watch",
                "cat","watch","dog", "#","cat","watch","watch", "and","chase","#","dog","watch","and","chase","chase"));

        ArrayList<String> tagTest = new ArrayList<>(Arrays.asList("#","n","v","n","#","n","v","np","#","np","v","n",
                "#","np","v","n","cnj","n","#","n","v","n","v","n","#","n","v","n","cnj","np","#","n","v","cnj","v","np"));

        ViterbiAlgorithm va = new ViterbiAlgorithm();
        ArrayList<String> sentenceTestResult = va.loadTrainingSentences(testSentencePath);
        ArrayList<String> tagTestResult = va.loadTrainingTags(testTagPath);

        System.out.println();
        if(sentenceTestResult.equals(sentenceTest)){
            System.out.println("EXAMPLE TEST 1 - LOAD SENTENCES: " + "PASSED!");
        }else{
            System.out.println("EXAMPLE TEST 1 - LOAD SENTENCES: " + "FAILED!");
        }
        if(tagTestResult.equals(tagTest)){
            System.out.println("EXAMPLE TEST 2 - LOAD TAGS: " + "PASSED!");
        } else{
            System.out.println("EXAMPLE TEST 2 - LOAD TAGS: " + "FAILED!");
        }

        //Test creating transition (trainedTags) emission (trained words) maps
        Map<String, Map<String, Double>> transmissionTest = new HashMap<>();
        Map<String, Double> inner = new HashMap<>();
        inner.put("#",-0.6931471805599453);
        inner.put("v", -0.6931471805599453);
        transmissionTest.put("np",inner);
        inner = new HashMap<>();
        inner.put("np", -1.252762968495368);
        inner.put("n",-0.3364722366212129);
        transmissionTest.put("#",inner);
        inner = new HashMap<>();
        inner.put("np", -2.0794415416798357);
        inner.put("n", -0.2876820724517809);
        inner.put("cnj",-2.0794415416798357);
        transmissionTest.put("v",inner);
        inner = new HashMap<>();
        inner.put("#",-1.0986122886681098);
        inner.put("v", -0.6931471805599453);
        inner.put("cnj",-1.791759469228055);
        transmissionTest.put("n",inner);
        inner = new HashMap<>();
        inner.put("np", -1.0986122886681098);
        inner.put("v", -1.0986122886681098);
        inner.put("n",-1.0986122886681098);
        transmissionTest.put("cnj",inner);
        inner = new HashMap<>();
        //System.out.println(transmissionTest);

        Map<String, Map<String, Double>> emissionTest = new HashMap<>();
        inner = new HashMap<>();
        inner.put("#",0.0);
        emissionTest.put("#",inner);
        inner = new HashMap<>();
        inner.put("chase", 0.0);
        emissionTest.put("np",inner);
        inner = new HashMap<>();
        inner.put("cat",-0.8754687373538999);
        inner.put("dog",-0.8754687373538999);
        inner.put("watch",-1.791759469228055);
        emissionTest.put("n",inner);
        inner = new HashMap<>();
        inner.put("chase",-1.5040773967762742);
        inner.put("get",-2.1972245773362196);
        inner.put("watch",-0.40546510810816444);
        emissionTest.put("v",inner);
        inner = new HashMap<>();
        inner.put("and",0.0);
        emissionTest.put("cnj",inner);
        inner = new HashMap<>();
        //System.out.println(emissionTest);

        va.train(sentenceTestResult,tagTestResult);
        Map<String, Map<String, Double>> emissionResult = va.getTrainedWords();
        Map<String, Map<String, Double>> transmissionResult = va.getTrainedTags();
        //System.out.println(transmissionResult);
        //System.out.println(emissionResult);

        System.out.println();
        if(transmissionResult.equals(transmissionTest)){
            System.out.println("EXAMPLE TEST 3 - BUILD TRANSMISSION MAP: " + "PASSED!");
        }else{
            System.out.println("EXAMPLE TEST 3 - BUILD TRANSMISSION MAP: " + "FAILED!");
        }
        if(emissionResult.equals(emissionTest)){
            System.out.println("EXAMPLE TEST 4 - BUILD EMISSION MAP: " + "PASSED!");
        } else{
            System.out.println("EXAMPLE TEST 4 - BUILD EMISSION MAP: " + "FAILED!");
        }
        System.out.println();


        //Test loading sentences and tags
        ArrayList<String> sentenceTest2 = new ArrayList<>(Arrays.asList("#", "i", "fish", "#", "will", "eats",
                "the", "fish", "#", "will", "you", "cook", "the", "fish", "#", "one", "cook", "uses", "a",
                "saw", "#", "a", "saw", "has", "many", "uses", "#", "you", "saw", "me", "color", "a", "fish",
                "#", "jobs", "wore", "one", "color", "#", "the", "jobs", "were", "mine", "#", "the", "mine",
                "has", "many", "fish", "#", "you", "can", "cook", "many"));
        ArrayList<String> tagTest2 = new ArrayList<>(Arrays.asList("#", "pro", "v", "#", "np", "v", "det", "n", "#", "mod", "pro",
                "v", "det", "n", "#", "det", "n", "v", "det", "n", "#", "det", "n", "v", "det", "n", "#", "pro", "vd", "pro", "v",
                "det", "n", "#", "np", "vd", "det", "n", "#", "det", "n", "vd", "pro", "#", "det", "n", "v", "det", "n", "#", "pro",
                "mod", "v", "pro"));
        ViterbiAlgorithm va1 = new ViterbiAlgorithm();
        ArrayList<String> sentenceTestResult2 = va1.loadTrainingSentences(exampleSentencePath);
        ArrayList<String> tagTestResult2 = va1.loadTrainingTags(exampleTagsPath);

        System.out.println();
        if(sentenceTestResult2.equals(sentenceTest2)){
            System.out.println("EXAMPLE TEST 5 - LOAD SENTENCES: " + "PASSED!");
        }else{
            System.out.println("EXAMPLE TEST 5 - LOAD SENTENCES: " + "FAILED!");
        }
        if(tagTestResult2.equals(tagTest2)){
            System.out.println("EXAMPLE TEST 6 - LOAD TAGS: " + "PASSED!");
        } else{
            System.out.println("EXAMPLE TEST 6 - LOAD TAGS: " + "FAILED!");
        }

        //Test creating transition (trainedTags) emission (trained words) maps
        Map<String, Map<String, Double>> transmissionTest2 = new HashMap<>();
        inner = new HashMap<>();
        inner.put("v",-0.6931471805599453);
        inner.put("vd", -0.6931471805599453);
        transmissionTest2.put("np",inner);
        inner = new HashMap<>();
        inner.put("n", 0.0);
        transmissionTest2.put("det",inner);
        inner = new HashMap<>();
        inner.put("np", -1.6094379124341003);
        inner.put("det", -0.916290731874155);
        inner.put("mod", -2.3025850929940455);
        inner.put("pro", -1.2039728043259361);
        transmissionTest2.put("#", inner);
        inner = new HashMap<>();
        inner.put("v", -0.6931471805599453);
        inner.put("pro", -0.6931471805599453);
        transmissionTest2.put("mod", inner);
        inner = new HashMap<>();
        inner.put("det", -0.15415067982725836);
        inner.put("#", -1.9459101490553135);
        transmissionTest2.put("v", inner);
        inner = new HashMap<>();
        inner.put("#", -1.791759469228055);
        inner.put("mod", -1.791759469228055);
        inner.put("v", -0.6931471805599453);
        inner.put("vd", -1.791759469228055);
        transmissionTest2.put("pro", inner);
        inner = new HashMap<>();
        inner.put("#", -0.45198512374305727);
        inner.put("v", -1.2992829841302609);
        inner.put("vd", -2.3978952727983707);
        transmissionTest2.put("n", inner);
        inner = new HashMap<>();
        inner.put("det", -1.0986122886681098);
        inner.put("pro", -0.40546510810816444);
        transmissionTest2.put("vd", inner);
        //System.out.println(transmissionTest2 + "\t");

        Map<String, Map<String, Double>> emissionTest2 = new HashMap<>();
        inner = new HashMap<>();
        inner.put("will",-0.6931471805599453);
        inner.put("jobs", -0.6931471805599453);
        emissionTest2.put("np",inner);
        inner = new HashMap<>();
        inner.put("the", -1.0116009116784799);
        inner.put("a", -1.2992829841302609);
        inner.put("one", -1.7047480922384253);
        inner.put("many", -1.7047480922384253);
        emissionTest2.put("det",inner);
        inner = new HashMap<>();
        inner.put("#", 0.0);
        emissionTest2.put("#",inner);
        inner = new HashMap<>();
        inner.put("can", -0.6931471805599453);
        inner.put("will", -0.6931471805599453);
        emissionTest2.put("mod", inner);
        inner = new HashMap<>();
        inner.put("eats", -2.0794415416798357);
        inner.put("color", -2.0794415416798357);
        inner.put("cook", -1.3862943611198906);
        inner.put("fish", -2.0794415416798357);
        inner.put("uses", -2.0794415416798357);
        inner.put("has", -1.3862943611198906);
        emissionTest2.put("v", inner);
        inner = new HashMap<>();
        inner.put("mine", -1.9459101490553135);
        inner.put("me", -1.9459101490553135);
        inner.put("i", -1.9459101490553135);
        inner.put("many", -1.9459101490553135);
        inner.put("you", -0.8472978603872037);
        emissionTest2.put("pro", inner);
        inner = new HashMap<>();
        inner.put("mine", -2.3978952727983707);
        inner.put("color", -2.3978952727983707);
        inner.put("cook", -2.3978952727983707);
        inner.put("fish", -1.0116009116784799);
        inner.put("jobs", -2.3978952727983707);
        inner.put("saw", -1.7047480922384253);
        inner.put("uses", -2.3978952727983707);
        emissionTest2.put("n", inner);
        inner = new HashMap<>();
        inner.put("were", -1.0986122886681098);
        inner.put("saw", -1.0986122886681098);
        inner.put("wore", -1.0986122886681098);
        emissionTest2.put("vd", inner);
        //System.out.println(emissionTest2);

        va1.train(sentenceTestResult2,tagTestResult2);
        Map<String, Map<String, Double>> emissionResult2 = va1.getTrainedWords();
        Map<String, Map<String, Double>> transmissionResult2 = va1.getTrainedTags();
        //System.out.println(transmissionResult2);
        //System.out.println(emissionResult2);

        System.out.println();
        if(transmissionResult2.equals(transmissionTest2)){
            System.out.println("EXAMPLE TEST 7 - BUILD TRANSMISSION MAP: " + "PASSED!");
        }else{
            System.out.println("EXAMPLE TEST 7 - BUILD TRANSMISSION MAP: " + "FAILED!");
        }
        if(emissionResult2.equals(emissionTest2)){
            System.out.println("EXAMPLE TEST 8 - BUILD EMISSION MAP: " + "PASSED!");
        } else{
            System.out.println("EXAMPLE TEST 8 - BUILD EMISSION MAP: " + "FAILED!");
        }
        System.out.println();


        //Test the algorithm on SIMPLE text
        System.out.println("DECODING SIMPLE TEXT - ");
        ViterbiAlgorithm va2 = new ViterbiAlgorithm();
        ArrayList<String> trainingWords = va2.loadTrainingSentences(simpleTrainSentencePath);
        ArrayList<String> trainingTags = va2.loadTrainingTags(simpleTrainTagsPath);
        va2.train(trainingWords,trainingTags);

        //Decoding text
        ArrayList<String> obs = va2.loadObservations(SimpleTestSentencePath);
        va2.decode(obs);

        //Test accuracy
        va2.accuracyTest(SimpleTestTagsPath);

        //Console input
        //va2.consoleInput(va2);
    }
}