package com.sahaj.wikiproblem;

import com.google.common.base.Splitter;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  Matches provided answers to a questions by scanning the article
 *
 *  Runtime complexity: O(mn + np),
 *  m = length of the article, n = length of answers, p = length of questions
 */
public class AnswerMatcher {

    private final static Splitter articleSplitter = Splitter.on('.')
            .omitEmptyStrings()
            .trimResults();

    public static List<String> findAnswers(final DataSet dataSet) {
        assert dataSet != null : "Input data can't be null";

        // scan the article for interesting sentences
        // each sentence partially containing an answer is considered interesting
        List<Map.Entry<String, String>> answerLookup =
                dataSet.providedAnswers
                        .parallelStream()
                        .map(answer -> getInterestingSentences(dataSet, answer))
                        .filter(tuple -> tuple != null)
                        .collect(Collectors.toList());

        // for each interesting sentence, find a question by similarity rank
        return dataSet.questions
                .parallelStream()
                .map(question -> matchQuestionToSentence(answerLookup, question))
                .collect(Collectors.toList());
    }

    static String matchQuestionToSentence(List<Map.Entry<String, String>> answerLookup, String question) {
        PotentialMatch match = new PotentialMatch();
        PotentialMatch anotherMatch = new PotentialMatch();
        answerLookup
                .stream()
                .forEach(tuple -> {
                    int rank = SentenceSimilarityRanker.rank(preProcess(tuple.getValue()), preProcess(question));
                    if (rank > match.rank) {
                        match.set(tuple.getKey(), question, tuple.getValue(), rank);
                        anotherMatch.clear();
                    } else if (rank == match.rank && rank > Integer.MIN_VALUE) {
                        anotherMatch.set(tuple.getKey(), question, tuple.getValue(), rank);
                    }
                });

        if (anotherMatch.potentialAnswer != null) {
            // two similar hits. let's try "dumb" word ranking
            int newScore = SentenceSimilarityRanker.bruteforce(preProcess(anotherMatch.interestingSentence), preProcess(question));
            int oldScore = SentenceSimilarityRanker.bruteforce(preProcess(match.interestingSentence), preProcess(question));

            match.potentialAnswer = newScore > oldScore ? anotherMatch.potentialAnswer: match.potentialAnswer;
        }
        return match.potentialAnswer;
    }

    static SimpleEntry<String, String> getInterestingSentences(DataSet dataSet, String answer) {
        final SimpleEntry<String, String> tuple = new SimpleEntry<>(answer, null);
        articleSplitter.splitToList(dataSet.article).forEach(sentence -> {
            if (sentence.contains(answer)) {  // String.indexOf is faster on smaller strings
                tuple.setValue(sentence);
            }
        });
        return tuple.getValue() == null ? null : tuple;
    }

    // optionally pre-process strings for better ranking with bitap
    public static String preProcess(final String text) {
        // ASSUMPTION english language
        // strip commonly used words which probably have no effect on actual answers
        // NOTE: this could be improved by using a common dictionary selected from top 10000 words --
        // https://github.com/first20hours/google-10000-english/blob/master/google-10000-english.txt
        return text
                .replaceAll("(?i)\\b(the|to|and|for|in|a|their|is|of|do|did|does|what|why|which|who|whom|how|it|are|at|by|,)\\b", "")
                .replaceAll("(\\?|;|,|\\s)+", " ")
                .trim()
                .toLowerCase();
    }
}
