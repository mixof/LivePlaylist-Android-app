package com.example.liveplaylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper implements BaseColumns
{
    private static DbHelper instance = null;

	final static String databaseName = "PhonesDB.s3db";
	final static int databaseVersion = 1;
	
	private static final String COMPANY_TABLE = "company";
	public static final String KEY_ROWID = "rowid _id";
	public static final String KEY_COMPANY_ID = "company_id";
	public static final String KEY_COMPANY_NAME = "company_name";
	public static final String KEY_COMPANY_DESCRIPTION = "company_description";
	public static final String KEY_COMPANY_LOGO = "company_logo";
	public static final String KEY_COMPANY_ADRES = "company_adress";
	public static final String KEY_COMPANY_PHONE = "company_phone";
	public static final String KEY_COMPANY_CATEGORY = "category_name";
	
	
	public static final String KEY_IMAGE = "image";
	
	
	private static final String TRACKS_TABLE = "tracks";
	public static final String KEY_TRACK_ID = "track_id";
	public static final String KEY_TRACK_ARTIST = "artist_name";
	public static final String KEY_TRACK_NAME = "track_name";
	public static final String KEY_TRACK_URL = "track_url";
	public static final String KEY_TRACK_LASTPLAY = "lastplay";
	public static final String KEY_TRACK_CHECKDATE = "checkdate";

	
	private static final String DATABASE_CREATE_SCRIPT = "create table "
            + TRACKS_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + KEY_TRACK_ID
            + " integer not null, " + KEY_TRACK_CHECKDATE + " integer);";
	
	Context appContext;
	
	private SQLiteDatabase db;
	
	public static DbHelper getInstance(Context context) 
	{
        if (instance == null)
        {
            instance = new DbHelper(context);
        }
        Log.d("FRAGMENTS", "Getting authors DB!!!");
        return instance;
    }
	
	
	public DbHelper(Context context)
	{
		super(context, databaseName, null, databaseVersion);
		appContext = context;
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
        db.execSQL(DATABASE_CREATE_SCRIPT);
					
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	// Открыть базу для чтения и записи
	public SQLiteDatabase open() throws SQLException 
	{
		  db = this.getWritableDatabase();
		  return db;
	}


	
	public Cursor getTrack(String track_id) {
		
		String[] args = new String[]{track_id};
		
		Cursor cursor = db.query(TRACKS_TABLE, null, "track_id=?", args, null, null, null);
		if (cursor != null) cursor.moveToFirst();
		return cursor;
	}
	
	public Cursor getAllTracks() {
		
		Cursor cursor = db.query(TRACKS_TABLE, null, null, null, null, null, null);
		if (cursor != null) cursor.moveToFirst();
		return cursor;
	}
	
	public long addTrack(String track_id, long checkdate) 
	{
		Log.d("adding", "track aded!");
		ContentValues initialValues = new ContentValues();
	
		initialValues.put(KEY_TRACK_ID, track_id);
		initialValues.put(KEY_TRACK_CHECKDATE, checkdate);			
		   
		return db.insert(TRACKS_TABLE, null, initialValues);	 
	}	
	
	public void delTrack(String track_id) 
	{
		String[] args = new String[]{track_id};
			
		db.delete(TRACKS_TABLE, KEY_TRACK_ID+"= ?", args); 
	}
	
	
	public void delTracks(long checkdate) 
	{		
		Log.d("deleting", "tracks deleted!");
			
		db.delete(TRACKS_TABLE, KEY_TRACK_CHECKDATE+"<"+checkdate, null); 
	}
	
	
	public void updateTrack(String track_id, long checkdate)
	{
		Log.d("updating", "track updated!");
		ContentValues updateValues = new ContentValues();
				
		updateValues.put(KEY_TRACK_CHECKDATE, checkdate);	
		
		String[] args = new String[]{track_id};
		   
		db.update(TRACKS_TABLE, updateValues, "track_id=?", args);
	}
	

}
