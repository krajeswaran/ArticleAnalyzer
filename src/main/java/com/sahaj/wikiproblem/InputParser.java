package com.sahaj.wikiproblem;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  Parse input files into questions/found answers/wiki article
 *  Note: Rudimentary parser. Can use NIO / sophisticated parsing for large data input
 */
public final class InputParser {

    private static final String ANSWERS_DELIMITER = ";";
    public static String INPUT_DIRECTORY = "input";

    public static List<DataSet> fetchDataSets() {
        ArrayList<DataSet> dataSets = new ArrayList<>();

        Files.fileTreeTraverser().children(new File(INPUT_DIRECTORY)).forEach(file -> {

            try {
                List<String> lines = Resources.readLines(file.toURL(), Charsets.UTF_8);

                assert lines != null : "Input file empty!";

                DataSet dataSet = new DataSet();
                dataSet.article = lines.get(0);
                dataSet.questions= lines.subList(1, 6);
                dataSet.providedAnswers = Arrays.asList(lines.get(6).split(ANSWERS_DELIMITER));
                dataSets.add(dataSet);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("OOPS! Input file invalid, exception : " + e);
            } catch (IOException e) {
                System.err.println("OOPS! can't read input file, exception : " + e);
            }

        });
        return dataSets;
    }
}
