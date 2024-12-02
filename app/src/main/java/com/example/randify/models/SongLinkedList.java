package com.example.randify.models;

import com.example.randify.PlayerService;

import java.io.File;
import java.util.Random;

/**
 * The <code>SongLinkedList</code> class is a wrapper that stores instances of <code>SongNode</code>,
 * providing LinkedList functionality to a group of songs.
 *
 * @author Randy Sim<br>
 * Stony Brook ID: 116109354<br>
 * Recitation: R01
 **/

public class SongLinkedList {
    private SongNode head;
    private SongNode tail;
    private SongNode cursor;
    private int size;

    /**
     * Returns an instance of <code>SongLinkedList</code> with empty fields.
     */
    public SongLinkedList() {}

    /**
     * Plays a song.
     *
     * @param name
     *  The name of the song
     *
     * <dt>Preconditions:
     *  <dd>The name of the song must match a song in the playlist and there must be a file associated witwh it.</dd>
     * </dt>
     *
     * <dt>Postconditions:
     *  <dd>The song is now playing.</dd>
     * </dt>
     *
     * @throws IllegalArgumentException
     *  The song was not found or could not be played.
     */
    public void play(String name) throws IllegalArgumentException {
        SongNode originalCursor = cursor;

        // is song name in playlist
        cursor = head;
        boolean found = false;
        for (int i = 0; i < size; ++i) {
            if (cursor.getData().getName().equals(name)) {
                found = true;
                break;
            }

            if (i != size - 1) {
                cursorForward();
            }
        }

        Song playData = cursor.getData();
        cursor = originalCursor;

        if (!found) {
            throw new IllegalArgumentException(
                    String.format(
                            "'%s' not found.",
                            name
                    )
            );
        }

        PlayerService.getInstance(null).playSong(name);

        System.out.println(
                String.format(
                        "'%s' by %s is now playing.",
                        playData.getName(),
                        playData.getArtist()
                )
        );
    }

    /**
     * Moves the cursor forward in the playlist.
     * <dt><b>Preconditions:</b>
     *  <dd>The list is not empty.</dd>
     * </dt>
     *
     * <dt><b>Postconditions:</b>
     *  <dd>The cursor now points to the next SongNode in the playlist.</dd>
     * </dt>
     *
     * @throws IllegalStateException
     *  The list is empty or the cursor has reached the end.
     */
    public void cursorForward() throws IllegalStateException {
        if (cursor == null) {
            throw new IllegalStateException("Cannot move cursor forward. List is empty.");
        }

        if (cursor != tail) {
            cursor = cursor.getNext();
        } else {
            throw new IllegalStateException("Already at the end of the playlist.");
        }
    }

    /**
     * Moves the cursor backward in the playlist.
     * <dt><b>Preconditions:</b>
     *  <dd>The list is not empty.</dd>
     * </dt>
     *
     * <dt><b>Postconditions:</b>
     *  <dd>The cursor now points to the previous SongNode in the playlist.</dd>
     * </dt>
     *
     * @throws IllegalStateException
     *  The list is empty or the cursor reached the beginning.
     */
    public void cursorBackward() throws IllegalStateException {
        if (cursor == null) {
            throw new IllegalStateException("Cannot move cursor backward. List is empty.");
        }

        if (cursor != head) {
            cursor = cursor.getPrev();
        } else {
            throw new IllegalStateException("Already at the beginning of the playlist.");
        }
    }

    /**
     * Insert a <code>SongNode</code> after the current cursor position.
     *
     * @param newSong
     *  The song data to be stored in the newly created <code>SongNode</code>
     *
     *  <dt>Preconditions:
     *      <dd>The newSong object has been instantiated</dd>
     *  </dt>
     *
     *  <dt>Postconditions:
     *   <dd>A SongNode with the given Song data is now inserted after the current cursor position</dd>
     *   <dd>The current cursor position is moved forward.</dd>
     *  </dt>
     *
     * @throws IllegalArgumentException
     *  Song data is null.
     */
    public void insertAfterCursor(Song newSong) throws IllegalArgumentException {
        if (newSong == null) {
            throw new IllegalArgumentException("Cannot insert a null song.");
        }

        SongNode node = new SongNode();
        node.setData(newSong);
        size++;

        if (cursor == null) {
            head = node;
            tail = node;
            cursor = node;
        } else if (cursor == tail) {
            cursor.setNext(node);
            node.setPrev(cursor);
            cursor = node;
            tail = cursor;
        } else {
            cursor.getNext().setPrev(node);
            node.setNext(cursor.getNext());
            node.setPrev(cursor);
            cursor.setNext(node);

            cursor = node;
            if (cursor.getNext() == null) {
                tail = node;
            }
        }
    }

    /**
     * Removes the SongNode at the current cursor position.
     * <dt><b>Preconditions:</b>
     *  <dd>The cursor is not null.</dd>
     * </dt>
     *
     * <dt><b>Postconditions:</b>
     *  <dd>The current reference node has been removed from the playlist.</dd>
     *  <dd>The cursor now references the next node, or the previous node if no node exists.</dd>
     * </dt>
     *
     * @return
     *  The song data held by the removed <code>SongNode</code>
     *
     * @throws IllegalStateException
     *  The playlist is empty.
     */
    public Song removeCursor() throws IllegalStateException {
        SongNode node = removeCursorNode();

        return node.getData();
    }

    /**
     * Returns the size of the playlist.
     *
     * @return
     *  The size of the playlist.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Play and return a random song in the playlist.
     * <dt><b>Postconditions:</b>
     *  <dd>The randomly selected song is now playing.</dd>
     * </dt>
     *
     * @return
     *  The data of the randomly picked song.
     */
    public Song random() {
        SongNode originalCursor = cursor;
        setCursorRandom();
        Song randomSong = cursor.getData();

        cursor = originalCursor;

        play(randomSong.getName());
        return randomSong;
    }

    /**
     * Randomly shuffles the order of the songs contained within the playlist.
     * <dt><b>Postconditions:</b>
     *  <dd>The playlist is randomly reordered.</dd>
     *  <dd>Cursor should reference the SongNode which contains the same Song as when this method was entered.</dd>
     * </dt>
     */
    public void shuffle() {
        SongNode newHead = null;
        SongNode newTail = null;

        SongNode originalCursor = cursor;
        int originalSize = size;

        while (size > 0) {
            setCursorRandom();
            SongNode removedCursor = removeCursorNode();
            removedCursor.setPrev(null);
            removedCursor.setNext(null);

            if (newHead == null) {
                newHead = removedCursor;
                newTail = removedCursor;
            } else {
                if (newHead == newTail) {
                    newHead.setNext(removedCursor);
                    removedCursor.setPrev(newHead);
                    newTail = removedCursor;
                } else {
                    newTail.setNext(removedCursor);
                    removedCursor.setPrev(newTail);
                    newTail = removedCursor;
                }
            }
        }

        size = originalSize;

        head = newHead;
        tail = newTail;
        cursor = originalCursor;
    }

    /**
     * Prints the playlist in a neatly formatted table.
     */
    public void printPlaylist() {
        System.out.println(this);

    }

    /**
     * Deletes all the songs from the playlist.
     * <dt><b>Postconditions:</b>
     *  <dd>All the songs have been deleted.</dd>
     * </dt>
     */
    public void deleteAll() {
        while (cursor != null) {
            removeCursor();
        }
    }

    /**
     * Converts the playlist into a tabular, string format.
     *
     * @return
     *  The string representation of the playlist.
     */
    @Override
    public String toString() {
        SongNode originalCursor = cursor;
        cursor = head;
        String songString = returnHeader();

        for (int i = 0; i < size; i++) {
            Song songData = cursor.getData();
            String songRow = String.format(
                    "\n%-26s%-27s%-31s%d",
                    songData.getName(),
                    songData.getArtist(),
                    songData.getAlbum(),
                    songData.getLength()
            );

            if (originalCursor == cursor) {
                songRow += "   <-";
            }

            songString += songRow;

            if (i != size-1) {
                cursorForward();
            }
        }

        cursor = originalCursor;

        return songString;
    }

    /* HELPER METHODS */
    private void setCursorRandom() {
        Random rand = new Random();

        int index = rand.nextInt(size);
        cursor = head;

        for (int i = 0; i < index; i++) {
            cursorForward();
        }
    }

    private SongNode removeCursorNode() throws IllegalStateException {
        if (cursor == null) {
            throw new IllegalStateException("Your playlist is empty.");
        }

        SongNode originalCursor = cursor;
        size--;

        if (cursor == head && head == tail) {
            head = null;
            tail = null;
            cursor = null;

            return originalCursor;
        }

        if(cursor == head) {
            head = cursor.getNext();
            head.setPrev(null);
            cursor = head;

            return originalCursor;
        }

        if (cursor == tail) {
            tail = cursor.getPrev();
            tail.setNext(null);
            cursor = tail;

            return originalCursor;
        }

        cursor.getNext().setPrev(cursor.getPrev());
        cursor.getPrev().setNext(cursor.getNext());
        cursor = cursor.getNext();
        return originalCursor;
    }

    private String returnHeader() {
        return (
                "Playlist:\n" +
                        "Song                     | Artist                   | Album                    | Length (s)\n" +
                        "-------------------------------------------------------------------------------------------"
        );
    }
}
