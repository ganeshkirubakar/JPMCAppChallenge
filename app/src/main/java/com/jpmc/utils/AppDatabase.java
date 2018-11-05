package com.jpmc.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jpmc.constants.AppConstants;
import com.jpmc.model.Album;

import io.realm.RealmList;


public class AppDatabase extends SQLiteOpenHelper {

    private static final String TAG = AppDatabase.class.getName();

    private static AppDatabase appDatabase;

    /**
     * Instance of AppDatabase for saving data and get Data
     * @param context context
     * @return instance of class
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = new AppDatabase(context.getApplicationContext());
        }
        return appDatabase;
    }

    private AppDatabase(Context context) {
        super(context, AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createAlbumTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + AppConstants.TABLE_ALBUM);
            db.execSQL(createAlbumTable());
        }
    }

    /**
     * Create table query for Table Album creation in Database
     * @return query for table Creation Album
     */
    private String createAlbumTable(){
        return "CREATE TABLE " + AppConstants.TABLE_ALBUM +
                "(" +
                AppConstants.KEY_ALBUM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                AppConstants.KEY_ALBUM_USER_ID + " INTEGER," +
                AppConstants.KEY_ALBUM_TITLE + " TEXT" +
                ")";
    }

    /**
     * Add or Update Table based on API response
     * @param albums save album to Table Album
     */
    public void addAlbum(RealmList<Album> albums){
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();

        for(int i =0; i < albums.size(); i++){
            ContentValues values = new ContentValues();
            values.put(AppConstants.KEY_ALBUM_ID, albums.get(i).getId());
            values.put(AppConstants.KEY_ALBUM_USER_ID, albums.get(i).getUserID());
            values.put(AppConstants.KEY_ALBUM_TITLE, albums.get(i).getTitle());

            int rows = database.update(AppConstants.TABLE_ALBUM, values, AppConstants.KEY_ALBUM_ID + "= ?", new String[]{ String.valueOf(albums.get(i).getId())});
            if (rows == 1) {
                String albumSelectQuery = String.format("SELECT * FROM %s WHERE %s = ?",
                        AppConstants.TABLE_ALBUM, AppConstants.KEY_ALBUM_ID);
                Cursor cursor = database.rawQuery(albumSelectQuery, new String[]{String.valueOf(albums.get(i).getId())});
                try {
                    if (cursor.moveToFirst()) {
                        cursor.getInt(0);
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }else{
                database.insertOrThrow(AppConstants.TABLE_ALBUM, null, values);
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    /**
     * Get Album List from Database table Album
     * @return list Of Album
     */
    public RealmList<Album> getAlbumList(){
        RealmList<Album> albums = new RealmList<>();
        String ALBUM_SELECT_QUERY =
                String.format("SELECT * FROM %s ORDER BY %s ASC",
                        AppConstants.TABLE_ALBUM,
                        AppConstants.KEY_ALBUM_TITLE);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ALBUM_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Album album = new Album();
                    album.setUserID(cursor.getInt(cursor.getColumnIndex(AppConstants.KEY_ALBUM_USER_ID)));
                    album.setId(cursor.getInt(cursor.getColumnIndex(AppConstants.KEY_ALBUM_ID)));
                    album.setTitle(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_ALBUM_TITLE)));
                    albums.add(album);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return albums;
    }
}
