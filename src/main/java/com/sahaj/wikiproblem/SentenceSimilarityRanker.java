package com.sahaj.wikiproblem;

import com.google.common.base.Splitter;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Ranks an article line by similarity to a given question
 *  Uses n-gram for phrase matching and Bitap approximate matching of each n-gram
 */
public class SentenceSimilarityRanker {

    private final static Splitter wordSplitter = Splitter.on(' ')
            .omitEmptyStrings()
            .trimResults();

    private static final Map<Map.Entry<String, String>, Integer> lookup = new HashMap<>();

    /**
     * Approximate ranking for a given pattern in some text
     * @param text Preprocessed article sentence to search
     * @param pattern Preprocessed question to search
     * @return return match score of the question. Integer.MIN_VALUE if no match found
     */
    public static int rank(String text, String pattern) {
        assert text != null && pattern != null : "Text or pattern can't be null";

        if (text.equals(pattern)) {
            //  lucky!
            return pattern.length();
        }

        int K = wordSplitter.splitToList(pattern).size() < 3 ? 2 : 3;

        List<String> nGramsText = generateNgram(K, text);
        List<String> nGramsPattern = generateNgram(K, pattern);

        int score = 0;
        for (String haystack : nGramsText) {
            for (String needle: nGramsPattern) {
                int loc = lookup.getOrDefault(new SimpleEntry<>(haystack, needle), Integer.MIN_VALUE);

                if (loc == Integer.MIN_VALUE) {
                    loc = Bitap.match(haystack, needle);
                    lookup.put(new SimpleEntry<>(haystack, needle), loc);
                }

                if (loc >= 0) {
                    score += 1.0;
                }
            }
        }

        return score == 0 ? Integer.MIN_VALUE : score;
    }

    private static List<String> generateNgram(int k, String source) {
        List<String> nGrams = new ArrayList<>();
        List<String> words = wordSplitter.splitToList(source);

        for (int i = 0; i < words.size() - k + 1; i++) {
            nGrams.add(concat(words, i, i + k));
        }

        return nGrams;
    }

    private static String concat(List<String> words, int start, int end) {
        StringBuilder sb = new StringBuilder();

        for (int i = start; i < end; i++) {
            sb.append(i > start ? " " : "").append(words.get(i));
        }

        return sb.toString();
    }

    /**
     * "Dumb" matcher when two sentences are ranked equally.
     * Tries to do full text searches and assigns an rank appropriately
     * @param text to search for
     * @param pattern pattern to rank
     * @return returns 0 if no match
     */
    public static int bruteforce(String text, String pattern) {
        assert text != null && pattern != null : "Text or pattern can't be null";

        if (text.contains(pattern)) {
            return pattern.length();
        }

        final int[] score = {0};
        final List<String> foundWords = new ArrayList<>();
        wordSplitter.splitToList(pattern)
                .stream()
                .filter(word -> !foundWords.contains(word))
                .forEach(word ->
                        wordSplitter.splitToList(text).forEach(textWord -> {
                            if (textWord.equals(word)) {
                                score[0] += 2;
                                foundWords.add(word);
                            } else if (textWord.contains(word) || word.contains(textWord)) {
                                score[0]++;
                                foundWords.add(word);
                            }
                        }));

        return score[0];
    }
}
