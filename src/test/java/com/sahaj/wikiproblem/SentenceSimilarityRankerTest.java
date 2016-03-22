package com.sahaj.wikiproblem;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SentenceSimilarityRankerTest {
    @Test
    public void testBruteforce() throws Exception {
        try {
            SentenceSimilarityRanker.bruteforce("abcdefghijklm abc", null);
            SentenceSimilarityRanker.bruteforce(null, null);
        } catch (AssertionError error) {
            assertTrue("Null check", error.getMessage().equals("Text or pattern can't be null"));
        }
        assertEquals("Full match", 21, SentenceSimilarityRanker.bruteforce("abcdefghijklm abc def", "abcdefghijklm abc def"));
        assertEquals("Full word match", 7, SentenceSimilarityRanker.bruteforce("abcdefghijklm abc abc", "abc abc"));
        assertEquals("Partial phrase match", 11, SentenceSimilarityRanker.bruteforce("abcdefghijklm abc abc", "klm abc abc"));
        assertEquals("No match", 0, SentenceSimilarityRanker.bruteforce("abcdefghijklm abc", "xyz"));
    }

    @Test
    public void testRank() throws Exception {
        try {
            SentenceSimilarityRanker.rank("abcdefghijklm abc", null);
            SentenceSimilarityRanker.rank(null, null);
        } catch (AssertionError error) {
            assertTrue("Null check", error.getMessage().equals("Text or pattern can't be null"));
        }
        assertEquals("Full match", 21, SentenceSimilarityRanker.rank("abcdefghijklm abc def", "abcdefghijklm abc def"));
        assertEquals("Full word match", 2, SentenceSimilarityRanker.rank("abcdefghijklm abc abc", "abc abc"));
        assertEquals("Partial phrase match", 1, SentenceSimilarityRanker.rank("abcdefghijklm abc abc", "klm abc abc"));
        assertEquals("No match", Integer.MIN_VALUE, SentenceSimilarityRanker.rank("abcdefghijklm abc", "xyz"));
        assertEquals("Fuzzy full match", 1, SentenceSimilarityRanker.rank("abcdefghijklm abc", "abcceffhijkmn acb"));
    }
}