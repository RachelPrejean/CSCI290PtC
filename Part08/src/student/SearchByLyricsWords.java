/**
 * File: SearchByLyricsWords.java 
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 
 * 12/2024 Rachel Prejean - Created 
 *****************************************************************************

 */
package student;

import java.util.*;
import java.io.*;


/**
 *
 * @author preje
 */
public class SearchByLyricsWords {
    //CLASS LEVEL VARIABLES
    //******************************************************************************

    // keep a local direct reference to the song array
    private Song[] songs;

    //set of common words using commonWords.txt
    private Set<String> commonWordSet = new HashSet<>();

    //treeMap uses Strings as keys to map to a TreeSet.
    private TreeMap<String, TreeSet<Song>> treeMapLyrics = new TreeMap<>();

    //******************************************************************************

    /**
     * constructor initializes the property and makes a set of common words
     * @param sc a SongCollection object
     */
    public SearchByLyricsWords(SongCollection sc) {
        songs = sc.getAllSongs();

        try {
            //create a scanner to read commonWords.txt
            FileInputStream commonWordFile = new FileInputStream("Part07//commonWords.txt");
            Scanner fileScanner = new Scanner(commonWordFile);

            //add words to commonWordSet
            while(fileScanner.hasNext()){
                commonWordSet.add(fileScanner.next());
            }

            fileScanner.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        
        }

        //Call fillTreeMapLyrics to handle the filling of the treeMap
        fillTreeMapLyrics(songs);

    } //end constructor

    /**
     * fillTreeMapLyrics
     * Fills the treeMap of lyrics using the song[]
     * @param Song[] songs
     */
    public void fillTreeMapLyrics(Song[] songs){
        for(int i = 0; i < songs.length; i++){
            //array of all words in the lyrics
            String[] allWords = songs[i].getLyrics().split("[^\\p{L}]+");
            Set<String> goodWords = new HashSet<>();

            //check every word to see if it is in common words or it has already been added
            for(String word : allWords){
                    word = word.toLowerCase();

                    if(!commonWordSet.contains(word) && !goodWords.contains(word) && word.length() > 1){
                        goodWords.add(word);
                    }

            }

            //use goodWords as the keys to the song. 
            for(String word : goodWords){
                //if the word isnt already a key create a new key and TreeSet associaded with it
                treeMapLyrics.putIfAbsent(word, new TreeSet<Song>()); 

                //add Song to TreeSet
                treeMapLyrics.get(word).add(songs[i]);
            }
            
        } //end songs[] loop 

    } //end fillTreeMapLyrics

    /**
     * search
     * Searches for the search words in the tree map
     * @param String searchWords
     * @return Song[]
     */
    public Song[] search(String searchWords){
        //Convert the String searchWords into a set containing the good words
        String[] allWords = searchWords.split("[^\\p{L}]+");
        Set<String> goodWords = new HashSet<>();

        //check every word to see if it is in common words or it has already been added
        for(String word : allWords){
                word = word.toLowerCase();

                if(!commonWordSet.contains(word) && !goodWords.contains(word) && word.length() > 1){
                    goodWords.add(word);
                }
        }

        //temp set for adding seached songs to
        Set<Song> retainedSongs = new HashSet<>();

        //make goodWords an array
        String[] goodWordsArray = goodWords.toArray(new String[0]);

        try{
            //now put all songs in the the first good word key set into retainedSongs
            //This is to start retained songs with something
            for(Song song : treeMapLyrics.get(goodWordsArray[0])){
                retainedSongs.add(song);
            }

        } catch(NullPointerException e){
            System.err.println("Error: The key " + goodWordsArray[0] + " does not exist in treeMapLyrics.");
        }

        //add the coresponding treeset for each goodWord to foundSongs
        for(int i = 1; i < goodWordsArray.length; i++){
            //fill tempSet with next keys songs
            Set<Song> tempSet = new HashSet<>();
            for(Song song : treeMapLyrics.get(goodWordsArray[i])){
                tempSet.add(song);
            }
            
            //do the retainAll to only keep the things that are the same
            retainedSongs.retainAll(tempSet);        
        }

        //convert to Array to match needed Song[] return
        Song[] arraySongs = retainedSongs.toArray(new Song[0]);
        
        //TODO: sorted array. Dont know if needed for future
        Arrays.sort(arraySongs);
        return arraySongs;

    } //end search


    /**
     * Statistics
     * Prints the number of keys in treeMapLyrics, print ALL song references in the map
     * Do some other math
     */
    private void statistics(){
        System.out.println("Number of keys in treeMapLyrics");
        System.out.println("Should be: 31385");
        System.out.println("Actual: " + treeMapLyrics.size());
        
        System.out.println("\nTotal number of song references");
        int total = 0;
        for(String key : treeMapLyrics.keySet()){
            total += treeMapLyrics.get(key).size();
        }
        System.out.println("Should be: Big Number");
        System.out.println("Actual: " + total);
        
        //not sure on these numbers
        System.out.println("\nCalculations");
        System.out.println("(made up numbers: bits per letter = 16)");
        System.out.println("Space for keys: " + (treeMapLyrics.size() * 144 + " bits"));
        System.out.println("Space for sets: " + (treeMapLyrics.size() * 20 * 64 + " bits"));


    } //end statistics

    /**
     * Unit test for SearchByLysicsWords
     */
    public static void main(String[] args){
        if (args.length < 1) {
            System.err.println("usage: prog songfile");
            return;
        } else if(args.length < 2){
            System.err.println("usage: No song to search");
            return;
        }
        
        SongCollection sc = new SongCollection(args[0]);
        SearchByLyricsWords sblw = new SearchByLyricsWords(sc);

        //sblw.statistics();
        Song[] songArray = sblw.search(args[1]);

        //Print the first ten results and the total result
        for(int i = 0; i < songArray.length && i < 10; i++){
            System.out.println(songArray[i]);
        }
        System.out.println("Total Songs Found: " + songArray.length);


    } //end unit test
    
} //end SearchByLyricsWords class
