package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import android.util.Log;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static HashSet<String> wordSet = new HashSet<String>();
    private static HashMap<String, ArrayList<String>> wordMap = new HashMap<String,ArrayList<String>>();
    private static ArrayList<String> wordList= new ArrayList<>();
    private static HashMap<Integer,ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String word2 = sortLetters(word);
            if(wordMap.get(word2) != null) {
                wordMap.get(word2).add(word);
                wordSet.add(word);
            } else {
                wordMap.put(word2, new ArrayList<String>());
                wordMap.get(word2).add(word);
                wordSet.add(word);
            }
            if (sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                sizeToWords.put(word.length(),new ArrayList<String>());
                sizeToWords.get(word.length()).add(word);
            }
            wordList.add(word);
        }
    }
    public boolean isGoodWord(String word, String base) {

        boolean check = true;

        if (wordSet.contains(word)) {
            if (word.contains(base)) {
                check = false;
            }
        } else {
            check = false;
        }
        return check;
    }

    private String sortLetters(String word) {
        char[] temp = word.toCharArray();
        Arrays.sort(temp);
        String res = new String(temp);
        return res;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        return getAnagramsWithOneMoreLetter(targetWord);
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String wordTemp = "";
        for (char i = 'a'; i<='z';i++) {
            for (char j = 'a'; j<='z';j++) {
                wordTemp = word + i + j;
                if (wordMap.get(sortLetters(wordTemp))!=null) {
                    result.addAll(wordMap.get(sortLetters(wordTemp)));
                }
            }
        }
        return result;
    }
    public String pickGoodStarterWord() {

        boolean found = false;

        for (int val = DEFAULT_WORD_LENGTH; val<MAX_WORD_LENGTH; val++) {
            int num = random.nextInt(sizeToWords.get(val).size());
            for (int i = num; i<sizeToWords.get(val).size(); i++) {
                String temp = sizeToWords.get(val).get(i);
                if (wordMap.get(sortLetters(temp)).size() >= MIN_NUM_ANAGRAMS) return temp;
            }
            for (int i = 0; i < num; i++) {
                String temp = sizeToWords.get(val).get(i);
                if (wordMap.get(sortLetters(temp)).size() >= MIN_NUM_ANAGRAMS) return temp;
            }
        }
        return null;
    }
}
