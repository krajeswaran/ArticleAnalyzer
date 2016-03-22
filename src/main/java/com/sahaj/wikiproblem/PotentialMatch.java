package com.sahaj.wikiproblem;

/**
 *  To hold a potential match
 */
public class PotentialMatch {
    String potentialAnswer;
    String potentialQuestion;
    String interestingSentence;
    int rank = Integer.MIN_VALUE;

    public void set(String answer, String question, String sentence, int rank) {
        this.potentialAnswer = answer;
        this.potentialQuestion = question;
        this.interestingSentence = sentence;
        this.rank = rank;
    }

    public void clear() {
        potentialAnswer = potentialQuestion = interestingSentence = null;
        rank = Integer.MIN_VALUE;
    }
}
