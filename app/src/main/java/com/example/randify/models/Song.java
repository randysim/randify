package com.example.randify.models;

/**
 * The <code>Song</code> class is a wrapper that contains song data to be stored in <code>SongNode</code>
 *
 * @author Randy Sim<br>
 * Stony Brook ID: 116109354<br>
 * Recitation: R01
 **/
public class Song {
    private String artist;
    private String album;
    private String name;
    private int length;
    private int audioResourceId;

    /**
     * Returns an instance of <code>Song</code> with predetermined fields.
     **/
    public Song(
        String artist,
        String album,
        String name,
        int length,
        int audioResourceId
    ) {
        this.artist = artist;
        this.album = album;
        this.name = name;
        this.length = length;
        this.audioResourceId = audioResourceId;
    }

    /**
     * Returns the song's artist.
     *
     * @return
     *  The song's artist.
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Updates the song's artist.
     *
     * @param artist
     *  The song's new artist.
     *
     * <dt>Postconditions:
     *  <dd>The song's artist has been updated.</dd>
     * </dt>
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Returns the song's album.
     *
     * @return
     *  The song's album.
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Updates the song's album.
     *
     * @param album
     *  The song's new album.
     *
     * <dt>Postconditions:
     *  <dd>The song's album has been updated.</dd>
     * </dt>
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * Returns the song's name.
     *
     * @return
     *  The song's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the song's name.
     *
     * @param name
     *  The song's new name.
     *
     * <dt>Postconditions:
     *  <dd>The song's name has been updated.</dd>
     * </dt>
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the song's length.
     *
     * @return
     *  The song's length.
     */
    public int getLength() {
        return length;
    }

    /**
     * Updates the song's length.
     *
     * @param length
     *  The song's new length.
     *
     * <dt>Postconditions:
     *  <dd>The song's length has been updated.</dd>
     * </dt>
     */
    public void setLength(int length) {
        this.length = length;
    }

    public int getAudioResourceId() {
        return this.audioResourceId;
    }

    public void setAudioResourceId(int audioResourceId) {
        this.audioResourceId = audioResourceId;
    }
}
