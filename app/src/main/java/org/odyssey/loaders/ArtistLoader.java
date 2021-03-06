package org.odyssey.loaders;

import java.util.ArrayList;
import java.util.List;

import org.odyssey.utils.MusicLibraryHelper;
import org.odyssey.models.ArtistModel;
import org.odyssey.utils.PermissionHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.preference.PreferenceManager;

/*
 * Custom Loader for ARTIST with ALBUM_ART
 */
public class ArtistLoader extends AsyncTaskLoader<List<ArtistModel>> {

    private boolean mShowAlbumArtistsOnly;

    Context mContext;

    public ArtistLoader(Context context) {
        super(context);
        this.mContext = context;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mShowAlbumArtistsOnly = sharedPref.getBoolean("pref_key_album_artists_only",true);
    }

    /*
     * Creates an list of all artists on this device and caches them inside the
     * memory(non-Javadoc)
     *
     * @see android.support.v4.content.AsyncTaskLoader#loadInBackground()
     */
    @Override
    public List<ArtistModel> loadInBackground() {
        ArrayList<ArtistModel> artists = new ArrayList<ArtistModel>();
        String artist, artistKey, coverPath;
        if ( !mShowAlbumArtistsOnly ) {
            // get all album covers
            Cursor cursorAlbumArt = PermissionHelper.query(mContext, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.ALBUM}, MediaStore.Audio.Albums.ALBUM_ART + "<>\"\" ) GROUP BY (" + MediaStore.Audio.Albums.ARTIST, null,
                    MediaStore.Audio.Albums.ARTIST + " COLLATE NOCASE ASC");

            // get all artists
            Cursor cursorArtists = PermissionHelper.query(mContext, MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, MusicLibraryHelper.projectionArtists, "", null, MediaStore.Audio.Artists.ARTIST + " COLLATE NOCASE ASC");

            if( cursorAlbumArt != null || cursorArtists != null) {
                // join both cursor if match is found
                long artistID;

                int artistTitleColumnIndex = cursorArtists.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
                int artistKeyColumnIndex = cursorArtists.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY);
                int artistIDColumnIndex = cursorArtists.getColumnIndex(MediaStore.Audio.Artists._ID);

                int albumArtistTitleColumnIndex = cursorAlbumArt.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
                int albumCoverPathColumnIndex = cursorAlbumArt.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

                if (cursorAlbumArt.getCount() > 0) {
                    cursorAlbumArt.moveToPosition(0);
                }
                for (int i = 0; i < cursorArtists.getCount(); i++) {
                    cursorArtists.moveToPosition(i);

                    artist = cursorArtists.getString(artistTitleColumnIndex);
                    artistKey = cursorArtists.getString(artistKeyColumnIndex);
                    artistID = cursorArtists.getLong(artistIDColumnIndex);

                    if (cursorAlbumArt.getString(albumArtistTitleColumnIndex).equals(cursorArtists.getString(artistTitleColumnIndex))) {
                        // Found right album art
                        coverPath = cursorAlbumArt.getString(albumCoverPathColumnIndex);
                        if (!cursorAlbumArt.isLast()) {
                            cursorAlbumArt.moveToNext();
                        }
                    } else {
                        coverPath = null;
                    }

                    artists.add(new ArtistModel(artist, coverPath, artistKey, artistID));

                }

                // return new custom cursor
                cursorAlbumArt.close();
                cursorArtists.close();
            }
        } else {
            // get all album covers
            Cursor cursorAlbumArt = PermissionHelper.query(mContext, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.ALBUM}, MediaStore.Audio.Albums.ARTIST + "<>\"\" ) GROUP BY (" + MediaStore.Audio.Albums.ARTIST, null,
                    MediaStore.Audio.Albums.ARTIST + " COLLATE NOCASE ASC");

            if (cursorAlbumArt != null) {
                int albumArtistTitleColumnIndex = cursorAlbumArt.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
                int albumCoverPathColumnIndex = cursorAlbumArt.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

                for (int i = 0; i < cursorAlbumArt.getCount(); i++) {
                    cursorAlbumArt.moveToPosition(i);

                    artist = cursorAlbumArt.getString(albumArtistTitleColumnIndex);
                    coverPath = cursorAlbumArt.getString(albumCoverPathColumnIndex);

                    artists.add(new ArtistModel(artist, coverPath, "", -1));
                }
                cursorAlbumArt.close();
            }
        }
        return artists;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
