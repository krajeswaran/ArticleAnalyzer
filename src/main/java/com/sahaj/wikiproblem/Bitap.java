package com.sahaj.wikiproblem;

import java.util.HashMap;
import java.util.Map;

/**
 *  A Bitap algorithm implementation - https://en.wikipedia.org/wiki/Bitap_algorithm
 *  Borrowed and modified from - https://android.googlesource.com/platform/external/google-diff-match-patch/
 *  with minor modifications and cleanup
 */
public class Bitap {
    /**
     * At what point is no match declared (0.0 = perfection, 1.0 = very loose).
     */
    public static float Match_Threshold = 0.5f;

    /**
     * The number of bits in an int.
     */
    private static int Match_MaxBits = 64;

    /**
     * Locate the best instance of 'pattern' in 'text' near 'loc' using the
     * Bitap algorithm.  Returns -1 if no match found.
     * @param text The text to search.
     * @param pattern The pattern to search for.
     * @return Best match index or -1.
     */
    public static int match(String text, String pattern) {
        int loc = 0;

        assert (Match_MaxBits == 0 || pattern.length() <= Match_MaxBits)
                : "Pattern too long for this application.";
        // Initialise the alphabet.
        Map<Character, Integer> s = initialize(pattern);
        // Highest score beyond which we give up.
        double score_threshold = Match_Threshold;
        // Is there a nearby exact match? (speedup)
        int best_loc = text.indexOf(pattern, loc);
        if (best_loc != -1) {
            score_threshold = Math.min(bitapScore(0, pattern),
                    score_threshold);
            // What about in the other direction? (speedup)
            best_loc = text.lastIndexOf(pattern, loc + pattern.length());
            if (best_loc != -1) {
                score_threshold = Math.min(bitapScore(0, pattern),
                        score_threshold);
            }
        }
        // Initialise the bit arrays.
        int matchmask = 1 << (pattern.length() - 1);
        best_loc = -1;
        int bin_min, bin_mid;
        int bin_max = pattern.length() + text.length();
        // Empty initialization added to appease Java compiler.
        int[] last_rd = new int[0];
        for (int d = 0; d < pattern.length(); d++) {
            // Scan for the best match; each iteration allows for one more error.
            // Run a binary search to determine how far from 'loc' we can stray at
            // this error level.
            bin_min = 0;
            bin_mid = bin_max;
            while (bin_min < bin_mid) {
                if (bitapScore(d, pattern)
                        <= score_threshold) {
                    bin_min = bin_mid;
                } else {
                    bin_max = bin_mid;
                }
                bin_mid = (bin_max - bin_min) / 2 + bin_min;
            }
            // Use the result from this iteration as the maximum for the next.
            bin_max = bin_mid;
            int start = Math.max(1, loc - bin_mid + 1);
            int finish = Math.min(loc + bin_mid, text.length()) + pattern.length();
            int[] rd = new int[finish + 2];
            rd[finish + 1] = (1 << d) - 1;
            for (int j = finish; j >= start; j--) {
                int charMatch;
                if (text.length() <= j - 1 || !s.containsKey(text.charAt(j - 1))) {
                    // Out of range.
                    charMatch = 0;
                } else {
                    charMatch = s.get(text.charAt(j - 1));
                }
                if (d == 0) {
                    // First pass: exact match.
                    rd[j] = ((rd[j + 1] << 1) | 1) & charMatch;
                } else {
                    // Subsequent passes: fuzzy match.
                    rd[j] = ((rd[j + 1] << 1) | 1) & charMatch
                            | (((last_rd[j + 1] | last_rd[j]) << 1) | 1) | last_rd[j + 1];
                }
                if ((rd[j] & matchmask) != 0) {
                    double score = bitapScore(d, pattern);
                    // This match will almost certainly be better than any existing
                    // match.  But check anyway.
                    if (score <= score_threshold) {
                        // Told you so.
                        score_threshold = score;
                        best_loc = j - 1;
                        if (best_loc > loc) {
                            // When passing loc, don't exceed our current distance from loc.
                            start = Math.max(1, 2 * loc - best_loc);
                        } else {
                            // Already passed loc, downhill from here on in.
                            break;
                        }
                    }
                }
            }
            if (bitapScore(d + 1, pattern) > score_threshold) {
                // No hope for a (better) match at greater error levels.
                break;
            }
            last_rd = rd;
        }
        return best_loc;
    }
    /**
     * Compute and return the score for a match with e errors and x location.
     * @param e Number of errors in match.
     * @param pattern Pattern being sought.
     * @return Overall score for match (0.0 = good, 1.0 = bad).
     */
    private static double bitapScore(int e, String pattern) {
        return (float) e / pattern.length();
    }
    /**
     * Initialise the alphabet for the Bitap algorithm.
     * @param pattern The text to encode.
     * @return Hash of character locations.
     */
    protected static Map<Character, Integer> initialize(String pattern) {
        Map<Character, Integer> s = new HashMap<>();
        char[] char_pattern = pattern.toCharArray();
        for (char c : char_pattern) {
            s.put(c, 0);
        }
        int i = 0;
        for (char c : char_pattern) {
            s.put(c, s.get(c) | (1 << (pattern.length() - i - 1)));
            i++;
        }
        return s;
    }

}
