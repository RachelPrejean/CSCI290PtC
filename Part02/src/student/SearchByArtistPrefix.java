/**
 * File: SearchByArtistPrefix.java
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 *
 * 8/2015 Anne Applin - Added formatting and JavaDoc
 * 2015 - Bob Boothe - starting code
 *****************************************************************************
 *
 */
package student;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Search by Artist Prefix searches the artists in the song database for artists
 * that begin with the input String
 *
 * @author Bob Booth
 */

public class SearchByArtistPrefix {

    // keep a local direct reference to the song array
    private Song[] songs;

    /**
     * constructor initializes the property. [Done]
     *
     * @param sc a SongCollection object
     */
    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches) converts artistPrefix to lowercase and
     * creates a Song object with artist prefix as the artist in order to have a
     * Song to compare. walks back to find the first "beginsWith" match, then
     * walks forward adding to the arrayList until it finds the last match.
     *
     * @param artistPrefix all or part of the artist's name
     * @return an array of songs by artists with substrings that match the
     * prefix
     */
    public Song[] search(String artistPrefix) {
        Song toFind = new Song(artistPrefix, "", "");
        ArrayList<Song> matches = new ArrayList<>();
        Song.CmpArtist cmp = new Song.CmpArtist();
        // O(log n)
        int toCheck = Arrays.binarySearch(songs, toFind, cmp);
        int binIdx = toCheck;
        int bSearchComp = cmp.cmpCnt;
        if (toCheck <= 0) {
            toCheck = -(toCheck + 1);
        }
        // O(k), where k is the number of matches
        
        while (toCheck > 0 && songs[toCheck].getArtist().toLowerCase().startsWith(artistPrefix.toLowerCase())) {
            toCheck--;
            cmp.cmpCnt++;
        }
        
        if (toCheck != 0){
            toCheck++;
        }
        int frontIdx = toCheck;
        while (toCheck < songs.length && songs[toCheck].getArtist().toLowerCase().startsWith(artistPrefix.toLowerCase())) {
            matches.add(songs[toCheck]);
            toCheck++;
            cmp.cmpCnt++;
        }

        System.out.printf("""
                          Searching for: %s
                          Index from binary search is: %d
                          Binary search comparisons: %d
                          Front found at: %d
                          Comparisons to build the list: %d
                          Actual complexity is: %d
                          
                          'k' is %d
                          log_{2}(n) = %d
                          Theoretical complexity is: %d
                          
                          """,
                artistPrefix, binIdx, bSearchComp,
                frontIdx, cmp.cmpCnt - bSearchComp, cmp.cmpCnt,
                matches.size(),
                (int) Math.ceil(Math.log(songs.length) / Math.log(2)),
                (int) Math.ceil(matches.size()
                        + (Math.log(songs.length) / Math.log(2))));

        return matches.toArray(Song[]::new);
    }

    /**
     * testing method for this unit
     *
     * @param args command line arguments set in Project Properties - the first
     * argument is the data file name and the second is the partial artist name,
     * e.g. be which should return beatles, beach boys, bee gees, etc.
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                Song[] byArtistResult = sbap.search(args[i]);
                System.out.printf("Found %d matches, first 10 are:\n", byArtistResult.length);
                Stream.of(byArtistResult).limit(10).forEach(System.out::println);
                System.out.println("--------------------------------------------------\n");
            }
        }
    }
}
