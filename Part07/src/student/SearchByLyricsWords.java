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

    //***************************************************************************

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
            String[] words = songs[i].getLyrics().split("[^a-zA-z]+");

        }
        
    } //end fillTreeMapLyrics


    /**
     * Unit test for SearchByLysicsWords
     * (just basic)
     */
    public static void main(String[] args){
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        
        SongCollection sc = new SongCollection(args[0]);
        SearchByLyricsWords sblw = new SearchByLyricsWords(sc);

    } //end unit test
    
} //end SearchByLyricsWords class
