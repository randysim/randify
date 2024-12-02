package com.example.randify.models;

/**
 * The <code>SongNode</code> class is a wrapper that stores
 * a reference to a <code>Song</code> instance. Provides LinkedList node functionality
 * to song data.
 *
 * @author Randy Sim<br>
 * Stony Brook ID: 116109354<br>
 * Recitation: R01
 **/
public class SongNode {
    private SongNode prev;
    private SongNode next;
    private Song data;

    /**
     * Returns an instance of <code>SongNode</code> with empty fields.
     **/
    public SongNode() {}

    /**
     * Returns the previous node in the playlist.
     *
     * @return
     *  The previous node in the playlist.
     */
    public SongNode getPrev() {
        return prev;
    }

    /**
     * Updates the link to the previous node in the playlist.
     *
     * @param prev
     *  The new previous node.
     *
     * <dt>Postconditions:
     *  <dd>The reference to the previous node has been updated.</dd>
     * </dt>
     */
    public void setPrev(SongNode prev) {
        this.prev = prev;
    }

    /**
     * Returns the next node in the playlist.
     *
     * @return
     *  The next node in the playlist.
     */
    public SongNode getNext() {
        return next;
    }

    /**
     * Updates the link to the next node in the playlist.
     *
     * @param next
     *  The new next node.
     *
     * <dt>Postconditions:
     *  <dd>The reference to the next node has been updated.</dd>
     * </dt>
     */
    public void setNext(SongNode next) {
        this.next = next;
    }

    /**
     * Returns the song data associated with the node.
     *
     * @return
     *  The associated song data.
     */
    public Song getData() {
        return data;
    }

    /**
     * Updates the associated song data.
     *
     * @param data
     *  The new song data.
     *
     * <dt>Postconditions:
     *  <dd>The associated song data has been updated.</dd>
     * </dt>
     */
    public void setData(Song data) {
        this.data = data;
    }
}
