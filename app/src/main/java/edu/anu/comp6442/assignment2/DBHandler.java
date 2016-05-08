package edu.anu.comp6442.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snigdha on 5/8/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Set database name
    private static final String DATABASE_NAME = "HISTORY_DATABASE";

    //history table name
    private static final String TABLE_NAME = "historyTable";

    //history table columns
    private static final String KEY_ID = "id";
    private static final String KEY_EXPRESSION = "expression";
    private static final String KEY_VALUES = "values";

    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creating table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HISTORY_TABLE ="CREATE TABLE "+TABLE_NAME+
                " ( "+KEY_ID+" INTEGER PRIMARY KEY, " +KEY_EXPRESSION
                +" TEXT, "+KEY_VALUES+" TEXT );";
    db.execSQL(CREATE_HISTORY_TABLE);}

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);

        //create table again
        onCreate(db);
    }
//methods for handling the database's read and write operations

    //Adding new expression
    public void addExpression(History history){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EXPRESSION, history.getExp()); //expression
        values.put(KEY_VALUES, history.getValues());//values

        //Inserting row
        db.insert(TABLE_NAME, null, values);
        db.close();//closing database connection
    }


    //Getting All expressions
    public List<History> getAllHistory(){
        List<History> historyList = new ArrayList<History>();

        //Select all query
        String selectQuery = "SELECT * FROM "+TABLE_NAME+";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all rows and adding to list
        if(cursor.moveToFirst())
        {
            do{
                History history = new History();
                history.setId(Integer.parseInt(cursor.getString(0)));
                history.setExp(cursor.getString(1));
                history.setValues(cursor.getString(2));
                //Adding history to list
                historyList.add(history);
            }while(cursor.moveToNext());
        }

        //return contact list
        return historyList;
    }

    //updating record
    public int updateHistory(History history){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EXPRESSION, history.getExp());
        values.put(KEY_VALUES, history.getValues());

        //updating row
        return db.update(TABLE_NAME, values, KEY_ID+" = ?",
                new String[]{String.valueOf(history.getId())});

    }

    //delete history
    public void deleteHistory(History history){
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete all query
        String deleteQuery ="SELECT * FROM " + TABLE_NAME + " ;";
        db.rawQuery(deleteQuery,null);
        db.close();
    }

}
