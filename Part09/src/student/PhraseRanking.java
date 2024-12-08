/**
 * File: PhraseRanking.java 
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 
 * 12/2024 Rachel Prejean - Created 
 *****************************************************************************
 */
package student;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PhraseRanking
 * Ranks the lyrics given to the actual lyrics of a song given.
 * @author Rachel Prejean
 */
public class PhraseRanking {

    /**
     * rankPhrase
     * This takes in 2 strings, the lyrics and the lyrics phrase being looked
     * for. It returns the ranking of the lyrics with respect to a search for
     * lyricsPhrase.
     * @param String lyrics
     * @param String lyricsPhrase
     * @return int
     */
    public static int rankPhrase(String lyrics, String lyricsPhrase){
        Pattern phrasePattern = Pattern.compile("[a-zA-z_0-9]+", Pattern.CASE_INSENSITIVE);
        Matcher phraseMatcher = phrasePattern.matcher(lyricsPhrase);
        List<String> phraseWords = new ArrayList<>();
        
        while (phraseMatcher.find()){
          phraseWords.add(phraseMatcher.group());
        }
        
        List<List<Integer>> matchers = new ArrayList<>();
        
        // for each word in the lyricPhrase create Integer Arraylist to store the index for each occurence of the word
          // modify the regular expression to be the particular word in the lyricPhrase (create a new regex string with "\\b" + phraseWords.get(i) + "\\b")
          // use the new regex to create a new pattern and matcher
          // while the new matcher.find() returns true
            // if this is the last word of the phrase add the index of the end of the word
            // otherwise we add the index of the start of the word
          // add all values found for the word occurences to the array list
        // The smallest current distance between the first and the last word
        Integer smallestCurrentDistance = Integer.MAX_VALUE;
        Integer bestFirst = -1;
        Integer bestLast = -1;
        
        for (Integer indexOfFirst : matchers.get(0)) {
          for (Integer indexOfLast : matchers.get(matchers.size() - 1)) {
            //here we check to see if the phrase is occuring in the correct order
            //first making sure that the index of the last word is greater than the index of the first word
            // and it must also be shorter than the current shortest distance in order to proceed
              // set a left and right index and a boolean value to True
              // here we will iterate through the lyrics searching to see if a match is located inbetween the previously defined matches
                // set the condition that a match word exists inbetween to false
                // for each index of current in matchers
                  // if we meet (indexOfCurrent > leftReference && indexOfCurrent < rightReference) we know that the there is a match word inbetween the previously defined match words
                    // we then set the index value of the new match word as the leftRefferenc in order to continue the searching.
                    //set the condition that a match word exist inbetween to true and break (yes, you can use it here)
                // if the word does not exist between the left and right references match found should be false and we break
              // if a match is found
                // set the best first index, best last index and the smallest distance the distance between the two
        // if the distance between the best first and the best last is greater than zero
          // best substring becomes the substring of the lyrics between best first and best last replacing all newlines with "nn"
          // return the best substring length
        }
        }
        return 0;
        
    } //end rankPhrase
   
} //end PhraseRanking class
