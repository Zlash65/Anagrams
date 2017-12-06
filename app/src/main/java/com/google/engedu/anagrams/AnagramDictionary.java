package com.google.engedu.anagrams;

import android.util.Log;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.jar.Pack200;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    private HashMap<String, ArrayList<String>> lettersToWord;
    private ArrayList<String> wordList;
	private HashSet<String> wordSet;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;

        lettersToWord = new HashMap<String, ArrayList<String>>();
       wordSet = new HashSet<String>();
        wordList = new ArrayList<String>();

        while((line = in.readLine()) != null) {
            String word = line.trim();

            // Add word to wordSet and wordList
        	wordSet.add(word);
            wordList.add(word);

            String sortedWord = sortLetters(word);
            // String sortedWord = word;
            // Arrays.sort(sortedWord);

            // Add word to lettersToWord
            // Check if the key already exists
            if (lettersToWord.containsKey(sortedWord)) {

                ArrayList<String> words = lettersToWord.get(sortedWord);
                words.add(word);
                lettersToWord.put(sortedWord, words);

            }
            // Key doesn't exist
            else {

                ArrayList<String> words = new ArrayList<String>();
                words.add(word);
                lettersToWord.put(sortedWord, words);

            }


        }
    }

    public boolean isGoodWord(String word, String base) {

        // Check whether the word has substring base.
        if (word.contains(base)) {
            return false;
        }

        // Check if word is a valid dictionary word
        if (wordSet.contains(word)) {
            return true;
        }

        // Not a valid word
        return false;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        // Iterate over each character
        for (char i = 'a'; i <= 'z'; i++) {

            // Add character to word;
            String newWord = word + i;

            // Get sorted word
            String sortedWord = sortLetters(newWord);

            // If sortedWord exists in lettersToWord,
            // add the words to result ArrayList
            if (lettersToWord.containsKey(sortedWord)) {

                // Get the words
                ArrayList<String> words = lettersToWord.get(sortedWord);

                for (int w = 0; w < words.size(); w++) {
                    String wrd = words.get(w);

                    // wrd is the new word
                    // word is the base word
                    if (isGoodWord(wrd, word)) {
                        result.add(wrd);
                    }
                }


            }

        }

        return result;
    }

    public String pickGoodStarterWord() {

        String pickedWord = "";

        while (true) {

            // Pick a random integer between 0 and number of words
            // in the dictionary
            int randomIndex = random.nextInt(wordList.size());

            pickedWord = wordList.get(randomIndex);

            Log.v("pickGoodStarterWord", pickedWord);

            if (pickedWord.length() >= DEFAULT_WORD_LENGTH && getAnagramsWithOneMoreLetter(pickedWord).size() >= MIN_NUM_ANAGRAMS && pickedWord.length() <= MAX_WORD_LENGTH) {
                break;
            }
        }


        return pickedWord;
    }

    public String sortLetters (String word) {
        char characters[] = word.toCharArray();

        for (int i = 0; i < characters.length; i++) {
            for (int j = 1; j < characters.length; j++) {

                if (characters[j] < characters[j - 1]) {

                    char temp = characters[j - 1];
                    characters[j - 1] = characters[j];
                    characters[j] = temp;

                }

            }
        }

        word = new String(characters);
        return word;
    }

}
