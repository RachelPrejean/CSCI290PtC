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
        //a bunch of complicated stuff that is taking the inputed lyrics to search
        //and taking out the not useable parts of it.
        Pattern phrasePattern = Pattern.compile("[a-zA-z_0-9]+", Pattern.CASE_INSENSITIVE);
        Matcher phraseMatcher = phrasePattern.matcher(lyricsPhrase);
        List<String> phraseWords = new ArrayList<>();
        
        //while the matcher finds a word add the word to the list of words
        while (phraseMatcher.find()){
          phraseWords.add(phraseMatcher.group());
        }
        
        //list of integrer list
        List<List<Integer>> matchers = new ArrayList<>();
        
        //for every word make a list to hold the indexes of the spots it is found 
        //and add it to the master list
        for(String word : phraseWords){
            List<Integer> indexes = new ArrayList<>();
            
            //change the regex to only find the word we are looking for
            String regex = "\\b" + word + "\\b";
            Pattern wordPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher wordMatcher = wordPattern.matcher(lyrics);
            
            //while the matcher finds the word in the lyrics add the index of the start of the word
            //or if it is the last word in the lyrics add the last index
            while(wordMatcher.find()){
                if(wordMatcher.end() == (lyrics.length() - 1)){
                    indexes.add(wordMatcher.end());
                } else {
                    indexes.add(wordMatcher.start());
                }
                //TODO: test test
                System.out.println(wordMatcher.start());
            }
            
            //add list to list of lists
            matchers.add(indexes);
        } //end finding matches
        
        //values to find distances between key values
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

    /**
     * Unit test for PhraseRanking
     */
    public static void main(String[] args){
        if (args.length < 1) {
            System.err.println("usage: prog songfile");
            return;
        } else if(args.length < 2){
            System.err.println("usage: No lyrics to search for");
            return;
        }
        
        SongCollection sc = new SongCollection(args[0]);
        Song[] songs = sc.getAllSongs();


        PhraseRanking.rankPhrase(songs[0].getLyrics(), args[1]);

    } //end unit test
   
} //end PhraseRanking class
