/**  
 * File: RaggedArrayList.java
 * ****************************************************************************
 *                           Revision History
 * ****************************************************************************
 * 
 * 8/2015 - Anne Applin - Added formatting and JavaDoc 
 * 2015 - Bob Boothe - starting code
 * ****************************************************************************
 */
package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * *
 * The RaggedArrayList is a 2 level data structure that is an array of arrays.
 *
 * It keeps the items in sorted order according to the comparator. Duplicates
 * are allowed. New items are added after any equivalent items.
 *
 * NOTE: normally fields, internal nested classes and non API methods should all
 * be private, however they have been made public so that the tester code can
 * set them
 * 
 * @author Bob Booth
 * @param <E> A generic data type so that this structure can be built with any
 *            data type (object)
 */
public class RaggedArrayList<E> implements Iterable<E> {

    // must be even so when split get two equal pieces

    private static final int MINIMUM_SIZE = 4;
    /**
     * The total number of elements in the entire RaggedArrayList
     */
    public int size;
    /**
     * really is an array of L2Array, but compiler won't let me cast to that
     */
    public Object[] l1Array;
    /**
     * The number of elements in the l1Array that are used.
     */
    public int l1NumUsed;
    /**
     * a Comparator object so we can use compare for Song
     */
    private Comparator<E> comp;

    /**
     * create an empty list always have at least 1 second level array even if
     * empty, makes code easier (DONE - do not change)
     *
     * @param c a comparator object
     */
    public RaggedArrayList(Comparator<E> c) {
        size = 0;
        // you can't create an array of a generic type
        l1Array = new Object[MINIMUM_SIZE];
        // first 2nd level array
        l1Array[0] = new L2Array(MINIMUM_SIZE);
        l1NumUsed = 1;
        comp = c;
    }

    /**
     * ***********************************************************
     * nested class for 2nd level arrays
     * read and understand it.
     * (DONE - do not change)
     */
    public class L2Array {

        /**
         * the array of items
         */
        public E[] items;
        /**
         * number of items in this L2Array with values
         */
        public int numUsed;

        /**
         * Constructor for the L2Array
         *
         * @param capacity the initial length of the array
         */
        public L2Array(int capacity) {
            // you can't create an array of a generic type
            items = (E[]) new Object[capacity];
            numUsed = 0;
        }
    }// end of nested class L2Array

    // ***********************************************************

    /**
     * total size (number of entries) in the entire data structure
     * (DONE - do not change)
     *
     * @return total size of the data structure
     */
    public int size() {
        return size;
    }

    /**
     * null out all references so garbage collector can grab them but keep
     * otherwise empty l1Array and 1st L2Array
     * (DONE - Do not change)
     */
    public void clear() {
        size = 0;
        // clear all but first l2 array
        Arrays.fill(l1Array, 1, l1Array.length, null);
        l1NumUsed = 1;
        L2Array l2Array = (L2Array) l1Array[0];
        // clear out l2array
        Arrays.fill(l2Array.items, 0, l2Array.numUsed, null);
        l2Array.numUsed = 0;
    }

    /**
     * *********************************************************
     * nested class for a list position used only internally 2 parts:
     * level 1 index and level 2 index
     */
    public class ListLoc {

        /**
         * Level 1 index
         */
        public int level1Index;

        /**
         * Level 2 index
         */
        public int level2Index;

        /**
         * Parameterized constructor DONE (Do Not Change)
         *
         * @param level1Index input value for property
         * @param level2Index input value for property
         */
        public ListLoc(int level1Index, int level2Index) {
            this.level1Index = level1Index;
            this.level2Index = level2Index;
        }

        /**
         * test if two ListLoc's are to the same location
         * (done -- do not change)
         *
         * @param otherObj the other listLoc
         * @return true if they are the same location and false otherwise
         */
        public boolean equals(Object otherObj) {
            // not really needed since it will be ListLoc
            if (getClass() != otherObj.getClass()) {
                return false;
            }
            ListLoc other = (ListLoc) otherObj;

            return level1Index == other.level1Index
                    && level2Index == other.level2Index;
        }

        /**
         * move ListLoc to next entry when it moves past the very last entry it
         * will be one index past the last value in the used level 2 array. Can
         * be used internally to scan through the array for sublist also can be
         * used to implement the iterator.
         */
        public void moveToNext() {
            L2Array subarr = (L2Array) l1Array[this.level1Index];
            if (level2Index + 1 >= subarr.numUsed) {
                level1Index += 1;
                level2Index = 0;
            } else {
                level2Index += 1;
            }
        }
    }

    /**
     * find 1st matching entry
     *
     * @param item the thing we are searching for a place to put.
     * @return ListLoc of 1st matching item or of 1st item greater than
     *         the item if no match this might be an unused slot at the end of a
     *         level 2 array
     */
    public ListLoc findFront(E item) {

        // TODO: Simplify algorithm -- whiteboard out each case && do alg by hand
        int l1 = 1;
        int l2 = 0;
        // If (0,0) is null
        if (((L2Array) l1Array[0]).items[0] == null) {
            return new ListLoc(0, 0);
        }
        // While there are still l1 arrays and the element searched is less than
        // the item
        while (l1Array[l1] != null && comp.compare(((L2Array) l1Array[l1]).items[0], item) < 0) {
            // If the thing at l1Array[i][0] is greater than the value we're
            // searching for then the previous array is the one we want
            l1++;
        }
        // if we're looking at an empty l1 index or the current element is
        // greater than the item
        if (l1Array[l1] == null || comp.compare(((L2Array) l1Array[l1]).items[0], item) > 0) {
            l1--;
            // Iterate through the list to find the first index that is less
            // than the target
            while (((L2Array) l1Array[l1]).items[l2 + 1] != null
                    && comp.compare(((L2Array) l1Array[l1]).items[l2], item) < 0) {
                l2++;
            }
            // If the current indexes are too small
            if (comp.compare(((L2Array) l1Array[l1]).items[l2], item) < 0) {
                // l1++;
                l2++;
            }
        }
        // If l1 is null, we want to append to the previous sub list
        if (l1Array[l1] == null) {
            l1--;
        }
        // Else if the first thing in the list is the element we're looking for
        // search the previous list to find a potential earlier element
        // (and there is a previous list)
        // (and the current list isn't null)
        else if (comp.compare(((L2Array) l1Array[l1]).items[0], item) == 0 && l1 != 0) {
            l1--;
            // Iterate through the list to find the first index that is less
            // than the target
            while (((L2Array) l1Array[l1]).items[l2 + 1] != null
                    && comp.compare(((L2Array) l1Array[l1]).items[l2], item) < 0) {
                l2++;
            }
            // If the current indexes don't match the element we already found
            if (comp.compare(((L2Array) l1Array[l1]).items[l2], item) != 0) {
                l1++;
                l2 = 0;
            }
        }
        return new ListLoc(l1, l2); // when finished should return: new ListLoc(l1,l2);
    }

    /**
     * find location after the last matching entry or if no match, it finds
     * the index of the next larger item this is the position to add a new
     * entry this might be an unused slot at the end of a level 2 array
     *
     * @param item the thing we are searching for a place to put.
     * @return the location where this item should go
     */
    public ListLoc findEnd(E item) {
        int l1 = l1Array.length - 1;

        // If the item is smaller than (0,0) or (0,0) is null
        if (((L2Array) l1Array[0]).items[0] == null || comp.compare(((L2Array) l1Array[0]).items[0], item) > 0) {
            return new ListLoc(0, 0);
        }

        // Find the first element that matches the following conditions:
        // - Array isn't null
        // - Has a valid index and is less than the item we're looking for
        while (l1Array[l1] == null || (l1 >= 0 &&
                comp.compare(((L2Array) l1Array[l1]).items[0], item) > 0)) {
            l1--;
        }
        int l2 = ((L2Array) l1Array[l1]).items.length - 1;
        // While the current element is null
        while (((L2Array) l1Array[l1]).items[l2] == null) {
            l2--;
        }
        // While the current element is greater than the item we're looing for
        while (comp.compare(((L2Array) l1Array[l1]).items[l2], item) > 0) {
            l2--;
        }
        // add 1 to l2 to get the index after
        return new ListLoc(l1, l2 + 1); // when finished should return: new ListLoc(l1,l2);
    }

    /**
     * add object after any other matching values findEnd will give the
     * insertion position
     *
     * @param item the thing we are searching for a place to put.
     * @return
     */
    public boolean add(E item) {
        ListLoc idx = findEnd(item);
        L2Array subarray = (L2Array) l1Array[idx.level1Index];
        // If we can add another element without needing to split
        if (subarray.items.length - 2 >= subarray.numUsed) {
            // If we need to shift existing elements
            if (subarray.items[idx.level2Index] != null) {
                System.arraycopy(subarray.items, idx.level2Index,
                        subarray.items, idx.level2Index + 1,
                        subarray.numUsed - idx.level2Index);
            }
            subarray.items[idx.level2Index] = item;
            // Increase the number of used elements
            subarray.numUsed++;
            // Else if we can extend the subarray by doubling the length
        } else if (subarray.items.length <= (l1Array.length / 2)) {
            // Expand the current subarray
            subarray.items = Arrays.copyOf(subarray.items, subarray.items.length * 2);
            // Shift existing elements
            System.arraycopy(subarray.items, idx.level2Index,
                    subarray.items, idx.level2Index + 1,
                    subarray.numUsed - idx.level2Index);
            subarray.items[idx.level2Index] = item;
            subarray.numUsed++;
            // Else we need to split the subarray
        } else {
            // If we need to expand the l1Array
            if (this.l1NumUsed > l1Array.length - 2) {
                l1Array = Arrays.copyOf(l1Array, l1Array.length * 2);
            }
            // Shift the existing l2Arrays
            System.arraycopy(l1Array, idx.level1Index,
                    l1Array, idx.level1Index + 1,
                    this.l1NumUsed - idx.level1Index);
            l1NumUsed++;
            // Create a reference to the new array and empty it
            // NOTE: the new instance is because the shift from copy didn't
            // create a new instance
            l1Array[idx.level1Index + 1] = new L2Array(subarray.items.length);
            L2Array newSplit = (L2Array) l1Array[idx.level1Index + 1];
            // Find the midpoint of the subarray assuming we've made the split
            int midpoint = (subarray.numUsed + 1) / 2;
            // Fill the new array with the second half of the original subarray
            // and empty the copied values from the original subarray
            System.arraycopy(subarray.items, midpoint,
                    newSplit.items, 0,
                    subarray.numUsed - midpoint);
            Arrays.fill(subarray.items,
                    midpoint, subarray.numUsed,
                    null);
            newSplit.numUsed = (subarray.numUsed + 1) - midpoint;
            subarray.numUsed = midpoint;
            // Get the new location to insert
            ListLoc newIdx = findEnd(item);
            // If the location after the split is in the old list
            if (newIdx.level1Index == idx.level1Index) {
                // If the location is at the midpoint of the split
                if (newIdx.level2Index >= subarray.numUsed) {
                    // Shift the array over to insert at zero
                    System.arraycopy(newSplit.items, 0,
                            newSplit.items, 1,
                            newSplit.numUsed);
                    newSplit.items[0] = item;
                    // Else we insert the last element of the array into the new
                    // split and insert the item
                } else {
                    System.arraycopy(newSplit.items, 0,
                            newSplit.items, 1,
                            newSplit.numUsed);
                    newSplit.items[0] = subarray.items[subarray.numUsed - 1];
                    System.arraycopy(subarray.items, newIdx.level2Index,
                            subarray.items, newIdx.level2Index + 1,
                            (subarray.numUsed - 1) - newIdx.level2Index);
                    subarray.items[newIdx.level2Index] = item;
                }
                // Else we can insert into the new split normally
            } else {
                System.arraycopy(newSplit.items, newIdx.level2Index,
                        newSplit.items, newIdx.level2Index + 1,
                        newSplit.numUsed - newIdx.level2Index);
                newSplit.items[newIdx.level2Index] = item;
            }

        }
        this.size += 1;
        return true;
    }

    /**
     * check if list contains a match
     *
     * @param item the thing we are looking for.
     * @return true if the item is already in the data structure
     */
    public boolean contains(E item) {
        ListLoc loc = findFront(item);

        return ((L2Array) l1Array[loc.level1Index]).items[loc.level2Index].equals(item);
    }

    /**
     * copy the contents of the RaggedArrayList into the given array
     *
     * @param a - an array of the actual type and of the correct size
     * @return the filled in array
     */
    public E[] toArray(E[] a) {
        if (a.length >= size) {
            int idx = 0;
            Iterator itr = this.iterator();
            while (itr.hasNext()) {
                a[idx] = (E) itr.next();
                idx++;
            }
        }
        return a;
    }

    /**
     * returns a new independent RaggedArrayList whose elements range from
     * fromElemnt, inclusive, to toElement, exclusive. The original list is
     * unaffected findStart and findEnd will be useful here
     *
     * @param fromElement the starting element
     * @param toElement   the element after the last element we actually want
     * @return the sublist
     */
    public RaggedArrayList<E> subList(E fromElement, E toElement) {
        RaggedArrayList<E> result = new RaggedArrayList(comp);
        ListLoc frontLoc = findFront(fromElement);
        ListLoc endLoc = findFront(toElement);
        while (!frontLoc.equals(endLoc)) {
            result.add(((L2Array) l1Array[frontLoc.level1Index]).items[frontLoc.level2Index]);
            frontLoc.moveToNext();
        }
        return result;
    }

    /**
     * returns an iterator for this list this method just creates an instance
     * of the inner Itr() class (DONE)
     *
     * @return an iterator
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Written by Bob Booth as part of the testing harness for
     * Project Part 5, this stats method allows any RaggedArrayList
     * object to print its own statistics. You must remember to
     * reset the Comparator object's count field before creating
     * a ragged array list.
     * Modified to be a RAL method by Anne Applin
     */
    public void stats() {
        System.out.println("STATS:");
        int size = this.size();
        System.out.println("list size N = " + size);

        // level 1 array
        int l1NumUsed = this.l1NumUsed;
        System.out.println("level 1 array " + l1NumUsed + " of "
                + this.l1Array.length + " used.");

        // level 2 arrays
        int minL2size = Integer.MAX_VALUE, maxL2size = 0;
        for (int i1 = 0; i1 < this.l1NumUsed; i1++) {
            RaggedArrayList<Song>.L2Array l2array = (RaggedArrayList<Song>.L2Array) (this.l1Array[i1]);
            minL2size = Math.min(minL2size, l2array.numUsed);
            maxL2size = Math.max(maxL2size, l2array.numUsed);
        }
        System.out.printf("level 2 array sizes: min = %d used, avg = %.1f "
                + "used, max = %d used.%n%n",
                minL2size,
                (double) size / l1NumUsed, maxL2size);
    }

    /**
     * Iterator is just a list loc. It starts at (0,0) and finishes with index2
     * 1 past the last item in the last block
     */
    private class Itr implements Iterator<E> {

        private ListLoc loc;

        /*
         * create iterator at start of list
         * (DONE)
         */
        Itr() {
            loc = new ListLoc(0, 0);
        }

        /**
         * check to see if there are more items
         */
        public boolean hasNext() {
            if (l1Array[loc.level1Index] != null) {
                return ((L2Array) l1Array[loc.level1Index]).items[loc.level2Index] != null;
            } else
                return false;
        }

        /**
         * return item and move to next throws NoSuchElementException if
         * off end of list. An exception is thrown here because calling
         * next() without calling hasNext() shows a certain level or stupidity
         * on the part of the programmer, so it can blow up. They deserve it.
         */
        public E next() {
            if (l1Array[loc.level1Index] != null) {
                E next = ((L2Array) l1Array[loc.level1Index]).items[loc.level2Index];
                loc.moveToNext();
                if (next == null) {
                    throw new IndexOutOfBoundsException();
                }
                return next;
            }
            throw new IndexOutOfBoundsException();

        }

        /**
         * Remove is not implemented. Just use this code. (DONE)
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
