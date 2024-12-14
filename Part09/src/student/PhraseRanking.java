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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
     * for. Returns The rank of the phrase (length of the shortest substring containing
     * all words in the correct order), or -1 if the phrase is not found or
     * if either input is empty.
     * @param String lyrics
     * @param String lyricsPhrase
     * @return int 
     */
    public static int rankPhrase(String lyrics, String lyricsPhrase){

        // handle empty lyrics or phrase
        if (lyrics == null || lyrics.isEmpty() || lyricsPhrase == null || lyricsPhrase.isEmpty()) {
            return -1; // Handle empty lyrics or phrase
        }

        // Create a regular expression pattern to match words (alphanumeric characters and underscores) which is case-insensitive.
        Pattern phrasePattern = Pattern.compile("[a-zA-z_0-9]+", Pattern.CASE_INSENSITIVE);
        Matcher phraseMatcher = phrasePattern.matcher(lyricsPhrase);
        // Create an ArrayList to store the individual words found in the lyricsPhrase.
        List<String> phraseWords = new ArrayList<>();

        // Iterate through the lyricsPhrase, finding each word that matches the pattern.
        while (phraseMatcher.find()){ 
          phraseWords.add(phraseMatcher.group()); 
        }
        
        // Create a list to hold lists of integers, where each inner list will store the indices of a word's occurrences.
        List<List<Integer>> matchers = new ArrayList<>();
        
        //for every word make a list to hold the indexes of the spots it is found 
        //and add it to the master list
        for(String word : phraseWords){
            List<Integer> indexes = new ArrayList<>();
            
            //change the regex to the current word as a whole word (surrounded by word boundaries)
            String regex = "\\b" + word + "\\b";
            Pattern wordPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            // Create a matcher to find matches of the word pattern in the lyrics string.
            Matcher wordMatcher = wordPattern.matcher(lyrics);
            
            // Find all occurrences of the current word within the song lyrics.
            while (wordMatcher.find()) {
                indexes.add(wordMatcher.start());
            }
            matchers.add(indexes);
        } //end finding matches
        
        if (matchers.isEmpty() || matchers.get(0).isEmpty()) {
            return -1;
        }

        // Initialize a variable to store the smallest distance found so far.
        Integer smallestCurrentDistance = Integer.MAX_VALUE;

        // Iterate through the list of indices for the first word (serves as starting points).
        for (int i = 0; i < matchers.get(0).size(); i++){
            int leftWindow = matchers.get(i).get(0); // Set the left boundary of the window to the current index of the first word.
            int rightWindow = leftWindow; // initialize the right boundary of the window temporarily to the left boundary.
            Boolean validWindow = true; // Initialize a boolean to track if the current window is valid AKA contains all words in the correct order.

            // Iterate through the remaining words in the phrase. using the index of the first word as the starting point.
            for (int j = 1; j < matchers.get(i).size(); j++){ 
                Boolean matchFound = false; // Initialize a boolean to track if a match is found within the current window

                // Iterate looking for the next word in the phrase that is within the current window.
                for (int indexOfCurrent : matchers.get(i)){ 
                    // If the index of the current word is within the current window.
                    if (indexOfCurrent >= rightWindow){
                        rightWindow = indexOfCurrent + phraseWords.get(j).length()-1; // Set the right boundary of the window to the end of the current word.
                        matchFound = true; // flag that a match was found.
                        break; // break out of the loop and move to the next word in the phrase.
                    }
                }
                if (!matchFound){ // if the next word in the phrase is not found within the current window.
                    validWindow = false; // flag that the current window does not contain all words in the correct order.
                    break; // break out of the loop and move to the next starting point.
                }
            }
            if (!validWindow){ // If the current window does not contain all words in the correct order.
                smallestCurrentDistance = Math.min(smallestCurrentDistance, rightWindow - leftWindow + 1); // store the smallest distance found so far.
            }
            // Calculate the distance between the left and right boundaries of the current window.
        }
        if (smallestCurrentDistance == Integer.MAX_VALUE) { // If no match was ever found.
            return -1;
        }
        return smallestCurrentDistance; // if a match was found, return the smallest distance found.

        }
        
    //end rankPhrase

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

            // Read the song file and build the SongCollection
        SongCollection sc = new SongCollection(args[0]);
        Song[] songs = sc.getAllSongs();

        if (songs.length == 0) {
            System.err.println("No songs found in the file.");
            return;
        }

    // Get the lyrics of the first song
        String lyrics = songs[0].getLyrics();
        String lyricsPhrase = args[1];

        // Rank the phrase in the lyrics
        int rank = PhraseRanking.rankPhrase(lyrics, lyricsPhrase);
        if (rank == -1) {
            System.out.println("Phrase not found in the lyrics.");
        } else {
            System.out.println("The rank of the phrase is: " + rank);
        }
    } //end unit test
   
} //end PhraseRanking class
