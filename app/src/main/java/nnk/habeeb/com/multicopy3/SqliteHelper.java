package nnk.habeeb.com.multicopy3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Habeeb on 12/19/2016.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ListItems";
    public static final int DATABASE_VER = 1;
    public static final String TABLE_NAME = "List";
    public static final String KEY_ID = "_id";
    public static final String NAME = "name";
    private final ArrayList<Items> contact_list = new ArrayList<Items>();
    private final ArrayList<String> listItems = new ArrayList();

    SQLiteDatabase db;
    Context context;
    private static SqliteHelper mInstance = null;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
        this.context = context;
    }
    public static synchronized SqliteHelper getInstance(Context context) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you don't accidentally leak an Activity's
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new SqliteHelper(context.getApplicationContext());
        }
        return mInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase dataBase) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " TEXT UNIQUE NOT NULL)";
        dataBase.execSQL(CREATE_CONTACTS_TABLE);
        Toast.makeText(context, "Table Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void putList(Items items)throws Exception {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, items.getNames());
        db.insert(TABLE_NAME, null, cv);
    }

    public void Delete_Contact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<Items> Get_Contacts() {
        try {
            contact_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_NAME;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Items items = new Items();

                    items.setNames(cursor.getString(1));
                    items.set_id(Integer.parseInt(cursor.getString(0)));
                    // Adding contact to list
                    contact_list.add(items);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return contact_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return contact_list;
    }
    public void putString(String string) throws Exception{
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, string);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }
     public void cleanDB(){
        db = this.getWritableDatabase();

      db.delete(TABLE_NAME,null,null);
         db.close();
     }

    public void open() throws SQLException {
        db = getWritableDatabase();
    }

    public ArrayList<String> GetListItem() {

        listItems.clear();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

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

    }