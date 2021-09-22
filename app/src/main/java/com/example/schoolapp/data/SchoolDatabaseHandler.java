package com.example.schoolapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.schoolapp.data.SchoolAppContract.MemberEntry;
public class SchoolDatabaseHandler extends SQLiteOpenHelper {
    public SchoolDatabaseHandler(@Nullable Context context) {
        super(context, SchoolAppContract.DATABASE_NAME, null, SchoolAppContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SCHOOL_TABLE = "CREATE TABLE " + MemberEntry.TABLE_NAME + "("
                + MemberEntry._ID + " INTEGER PRIMARY KEY,"
                + MemberEntry.COLUMN_FIRST_NAME + " TEXT,"
                + MemberEntry.COLUMN_LAST_NAME + " TEXT,"
                + MemberEntry.COLUMNGENDER + " INTEGER NOT NULL,"
                + MemberEntry.COLUMN_KURS + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_SCHOOL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SchoolAppContract.DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }
}
