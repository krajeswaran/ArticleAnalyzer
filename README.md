What
====

English language article analyzer as described in problem statement, uses some limited NLP techniques - q-grams and fuzzy string matching.

For extra testing, there are two additional files - moscow.txt and jobs.txt at the top-level similar to the input, formulated from Wikipedia. 
All text files under "input" folder will be processed.

TODO
-----
* Add more unit tests :(

Installation Notes
-------------------

Just a simple gradle project, can be run using `gradle run` from command line or imported with your favorite (IntelliJ :)) IDE.

Requirements
------------

* Gradle >=2.8, with gradle present in the path and GRADLE_HOME set
* JDK >= 1.8

Also Internet connection do download dependencies(Guava and JUnit).

Problem statement
=================
You are provided with short paragraphs of text from Wikipedia, about well known places,
persons, animals, flowers etc. For each paragraphs, there is a set of five simple questions,
which can be directly answered by reading the paragraph. You are also provided with the
answers to each of these questions, though they are jumbled up, and provided in no
specific order. Your task is to identify, which answer corresponds to which question.

Input Format
------------
The first line contains a short paragraph of text from Wikipedia.
Line Number 2 to 6 contain a group of 5 simple questions, expecting factual answers,
which can be directly answered by reading the provided paragraph.
Line Number 7 contains the five answers which have been jumbled up. The answers are
grouped into one string and separated by semi-colons.

Input Constraints
-----------------
The text snippet will have no more than 5000 characters.
The five questions will expect answers of an entirely factual nature, such as date,
numbers, or names. The answers will strictly be exact substrings of the text fragment
provided. The answers could either be one word, or a group of words, or a complete
sentence.

Output Format
-------------
Five lines - the first line should contain the answer to the first question, second line should
contain the answer to the second question and so on.
The answers should be entirely confined to one of the possible answers listed in the last
line of the input file, i.e. Line Number 7.
