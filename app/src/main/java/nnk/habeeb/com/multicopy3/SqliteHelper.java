package nnk.habeeb.com.multicopy3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SqliteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myFriendDB";
    ArrayList<String> listItems = new ArrayList<>();

    // Friend table name
    private static final String TABLE_FRIEND = "friend";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_JOB = "job";
    SQLiteDatabase db;

    public SqliteHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FRIEND_TABLE = "CREATE TABLE " + TABLE_FRIEND + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT UNIQUE NOT NULL )";
        db.execSQL(CREATE_FRIEND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);

        // Create tables again
        onCreate(db);
    }

    // Adding a new record (friend) to table
    public void addItems(Items friend)throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, friend.getNames());


        // inserting this record
        db.insert(TABLE_FRIEND, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Friends in Table of Database
    public List<Items> getAllFriends() {
        List<Items> friendList = new ArrayList<>();

        // select query
        String selectQuery = "SELECT  * FROM " + TABLE_FRIEND;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all table records and adding to list
        if (cursor.moveToFirst()) {
            do {
                Items friend = new Items();
                friend.set_id(Integer.parseInt(cursor.getString(0)));
                friend.setNames(cursor.getString(1));
                // Adding friend to list
                friendList.add(friend);
            } while (cursor.moveToNext());
        }

        return friendList;
    }

    // Updating a record in database table
    public int updateFriend(Items friend) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, friend.getNames());


        // updating row
        return db.update(TABLE_FRIEND, values, KEY_ID + " = ?", new String[]{String.valueOf(friend.get_id())});
    }

    // Deleting a record in database table
    public void deleteFriend(Items friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIEND, KEY_ID + " = ?", new String[]{String.valueOf(friend.get_id())});
        db.close();
    }

    // getting number of records in table
    public int getContactsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dataCount = db.rawQuery("select " + KEY_ID + " from " + TABLE_FRIEND, null);

        int count = dataCount.getCount();
        dataCount.close();
        db.close();

        return count;
    }
    public void cleanDB(){
        db = this.getWritableDatabase();

        db.delete(TABLE_FRIEND,null,null);
        db.close();
    }
    public ArrayList<String> GetListItem() {

        listItems.clear();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FRIEND;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {


                listItems.add(cursor.getString(1));

                // Adding contact to list

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listItems;

    }
    public void putString(String string) throws Exception{
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, string);
        db.insert(TABLE_FRIEND, null, cv);
        db.close();
    }

}