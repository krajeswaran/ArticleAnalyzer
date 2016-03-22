package com.sahaj.wikiproblem;

import java.util.List;

/**
 *  Main harness for the app
 */
public class Harness {

    public static void main(String[] args) {
        List<DataSet> dataSets = InputParser.fetchDataSets();

        assert dataSets != null : "Can't parse any input dataset!";

        dataSets.forEach(data ->
                AnswerMatcher.findAnswers(data).forEach(System.out::println));
    }
}
