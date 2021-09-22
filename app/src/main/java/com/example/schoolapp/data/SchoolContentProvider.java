package com.example.schoolapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.schoolapp.CursorAdapter;

public class SchoolContentProvider extends ContentProvider{
    SchoolDatabaseHandler dbOpenHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int MEMBERS = 111;
    private static final int MEMBERS_ID = 222;

    CursorAdapter cursorAdapter;

    static {
        uriMatcher.addURI(SchoolAppContract.AUTHORITY, SchoolAppContract.PATH_MEMBERS, MEMBERS);
        uriMatcher.addURI(SchoolAppContract.AUTHORITY, SchoolAppContract.PATH_MEMBERS + "/#", MEMBERS_ID);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new SchoolDatabaseHandler(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                cursor = db.query(SchoolAppContract.MemberEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MEMBERS_ID:
                selection = SchoolAppContract.MemberEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(SchoolAppContract.MemberEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            default:

                throw new IllegalArgumentException("Can't query incorrect URI " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        String firstName = contentValues.getAsString(SchoolAppContract.MemberEntry.COLUMN_FIRST_NAME);
        if (firstName == null) {
            throw new IllegalArgumentException("You have to input first name ");
        }

        String lastName = contentValues.getAsString(SchoolAppContract.MemberEntry.COLUMN_LAST_NAME);
        if (lastName == null) {
            throw new IllegalArgumentException("You have to input last Name name ");
        }
        Integer gender = contentValues.getAsInteger(SchoolAppContract.MemberEntry.COLUMNGENDER);
        if (gender == null || !(gender == SchoolAppContract.MemberEntry.TANLANMAGAN || gender == SchoolAppContract.MemberEntry.ERKAK
                || gender == SchoolAppContract.MemberEntry.AYOL)) {

            throw new IllegalArgumentException("You have to input gender ");
        }
        String kurs = contentValues.getAsString(SchoolAppContract.MemberEntry.COLUMN_KURS);
        if (kurs == null) {
            throw new IllegalArgumentException("You have to input kurs ");
        }

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                long id = db.insert(SchoolAppContract.MemberEntry.TABLE_NAME, null, contentValues);
                if (id == -1) {
                    Log.e("Insert Method", "Insertion of data in the table failed for " + uri);
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return ContentUris.withAppendedId(uri, id);

            default:
                throw new IllegalArgumentException("Can't insert incorrect URI " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowDelete;
        switch (match) {
            case MEMBERS:
                rowDelete = db.delete(SchoolAppContract.MemberEntry.TABLE_NAME, s, strings);
                break;
            case MEMBERS_ID:
                s = SchoolAppContract.MemberEntry._ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDelete = db.delete(SchoolAppContract.MemberEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Can't delete incorrect URI " + uri);
        }
        if(rowDelete != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowDelete;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int rowUpdate;

        if (contentValues.containsKey(SchoolAppContract.MemberEntry.COLUMN_FIRST_NAME)) {

            String firstName = contentValues.getAsString(SchoolAppContract.MemberEntry.COLUMN_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException("You have to input first name ");
            }
        }

        if (contentValues.containsKey(SchoolAppContract.MemberEntry.COLUMN_LAST_NAME)) {
            String lastName = contentValues.getAsString(SchoolAppContract.MemberEntry.COLUMN_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("You have to input last Name name ");
            }

        }
        if (contentValues.containsKey(SchoolAppContract.MemberEntry.COLUMNGENDER)) {

            Integer gender = contentValues.getAsInteger(SchoolAppContract.MemberEntry.COLUMNGENDER);
            if (gender == null || !(gender == SchoolAppContract.MemberEntry.TANLANMAGAN || gender == SchoolAppContract.MemberEntry.ERKAK
                    || gender == SchoolAppContract.MemberEntry.AYOL)) {

                throw new IllegalArgumentException("You have to input gender ");
            }
        }
        if (contentValues.containsKey(SchoolAppContract.MemberEntry.COLUMN_KURS)) {


            String kurs = contentValues.getAsString(SchoolAppContract.MemberEntry.COLUMN_KURS);
            if (kurs == null) {
                throw new IllegalArgumentException("You have to input kurs ");
            }
        }


        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
               rowUpdate =  db.update(SchoolAppContract.MemberEntry.TABLE_NAME, contentValues, s, strings);
               break;
                case MEMBERS_ID:
                s = SchoolAppContract.MemberEntry._ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowUpdate =  db.update(SchoolAppContract.MemberEntry.TABLE_NAME, contentValues, s, strings);
                break;
                default:
                throw new IllegalArgumentException("Can't update incorrect URI " + uri);
        }
        if(rowUpdate != 0){
            getContext().getContentResolver().notifyChange(uri,null);

        }
        return rowUpdate;

    }


    @Override
    public String getType(Uri uri) {
        //SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                return SchoolAppContract.MemberEntry.CONTENT_MULTIPLE_ITEMS;
            case MEMBERS_ID:
                return SchoolAppContract.MemberEntry.CONTENT_BASE_ITEMS;
            default:
                throw new IllegalArgumentException("Unkown Uri" + uri);
        }
    }


}

// URI - Unified Recourse Identifier
// URL - Unified Resourse Locator