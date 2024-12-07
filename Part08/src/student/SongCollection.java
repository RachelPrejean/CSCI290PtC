/**
 * File: SongCollection.java
 ************************************************************************
 *                     Revision History (newest first)
 ************************************************************************
 * 
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 -   Prof. Bob Boothe - Starting code and main for testing  
 ************************************************************************
 */

 package student;

import java.util.stream.Stream;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * SongCollection.java 
 * Reads the specified data file and build an array of songs.
 * @author boothe
 */
public class SongCollection {

    private Song[] songs;

    /**
     * Note: in any other language, reading input inside a class is simply not
     * done!! No I/O inside classes because you would normally provide
     * precompiled classes and I/O is OS and Machine dependent and therefore 
     * not portable. Java runs on a virtual machine that IS portable. So this 
     * is permissable because we are programming in Java and Java runs on a 
     * virtual machine not directly on the hardware.
     *
     * @param filename The path and filename to the datafile that we are using
     * must be set in the Project Properties as an argument.
     */
    public SongCollection(String filename) {

	// use a try catch block
        // read in the song file and build the songs array
        // there are several ways to read in the lyrics correctly.  
        // the line feeds between lines and the blank lines between verses
        // must be retained.
        ArrayList<Song> songArrayList = new ArrayList();
        try {
            FileInputStream songDB = new FileInputStream(filename);
            String[] data = new Scanner(songDB).useDelimiter("\\Z").next().split("\"");
            for (int i = 1; i < data.length; i = i + 6){
                songArrayList.add(new Song(data[i], data[i+2], data[i+4]));
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        
        // sort the songs array using Arrays.sort (see the Java API)
        // this will use the compareTo() in Song to do the job.
        songs = new Song[songArrayList.size()];
        songs = songArrayList.toArray(songs);
        Arrays.sort(songs);
    }
 
    /**
     * this is used as the data source for building other data structures
     * @return the songs array
     */
    public Song[] getAllSongs() {
        Arrays.sort(songs);
        return songs;
    }
 
    /**
     * unit testing method
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);

        // todo: show song count
        System.out.printf("Total songs: %d, first songs:\n",
                          sc.songs.length);
        Stream.of(sc.songs).limit(10).forEach(System.out::println);
    }
}