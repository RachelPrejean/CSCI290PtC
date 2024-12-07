/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;

import java.util.*;
import java.io.*;


/**
 *
 * @author preje
 */
public class SearchByLyricsWords {
    // keep a local direct reference to the song array
    private Song[] songs;

    private Set<String> commonWordSet = new HashSet<>();

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
    } //end constructor




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
