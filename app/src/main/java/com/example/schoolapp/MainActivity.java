package com.example.schoolapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.schoolapp.data.SchoolAppContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    FloatingActionButton floatingActionButton;
    ListView listView;
    CursorAdapter cursorAdapter;
    private static final int MEMBER_LOADER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.ListView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        cursorAdapter = new CursorAdapter(this,null,false);
        listView.setAdapter(cursorAdapter);
        getSupportLoaderManager().initLoader(MEMBER_LOADER,null,this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
                startActivity(intent);

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long longId) {
                Intent intent
                         = new Intent(MainActivity.this,AddMemberActivity.class);
                Uri currentUri1 = ContentUris.withAppendedId(SchoolAppContract.MemberEntry.CONTENT_URI,longId);
                intent.setData(currentUri1
                );
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //displayData();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            String[] projection = {
                    SchoolAppContract.MemberEntry._ID,
                    SchoolAppContract.MemberEntry.COLUMN_FIRST_NAME,
                    SchoolAppContract.MemberEntry.COLUMN_LAST_NAME,
                    SchoolAppContract.MemberEntry.COLUMN_KURS
            };

            CursorLoader cursorLoader = new CursorLoader(this, SchoolAppContract.MemberEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
            );
            return cursorLoader;
        }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
cursorAdapter.swapCursor(null);
    }


//    public void displayData() {
//        String[] projection = {
//               MemberEntry._ID,
//                MemberEntry.COLUMN_FIRST_NAME,
//                MemberEntry.COLUMN_LAST_NAME,
//                MemberEntry.COLUMNGENDER,
//                MemberEntry.COLUMN_KURS
//        };
//
//        Cursor cursor = getContentResolver().query(MemberEntry.CONTENT_URI,
//                projection, null, null,
//                null
//        );
//        //textViewga chiqarish
////   textView.setText("ALL MEMBERS\n\n");
////   textView.append(MemberEntry._ID + " " +
////           MemberEntry.COLUMN_FIRST_NAME + " " +
////           MemberEntry.COLUMN_LAST_NAME + " " +
////           MemberEntry.COLUMNGENDER + " " +
////           MemberEntry.COLUMN_KURS);
////
////   int idIndex = cursor.getColumnIndex(MemberEntry._ID);
////   int FirstNameIndex = cursor.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME);
////   int LastNameIndex = cursor.getColumnIndex(MemberEntry.COLUMN_LAST_NAME);
////   int GenderIndex = cursor.getColumnIndex(MemberEntry.COLUMNGENDER);
////   int KursIndex = cursor.getColumnIndex(MemberEntry.COLUMN_KURS);
////
////   while (cursor.moveToNext()){
////       int currentID = cursor.getInt(idIndex);
////       String currentFirstName = cursor.getString(FirstNameIndex);
////       String currentLastName = cursor.getString(LastNameIndex);
////       int currentGender = cursor.getInt(GenderIndex);
////       String currentKurs = cursor.getString(KursIndex);
////        textView.append("\n " +
////                currentID + " " +
////                currentFirstName + " " +
////                currentLastName + " " +
////                currentGender + " " +
////                currentKurs + " "
////        );
////   }
////        cursor.close();
//        CursorAdapter cursorAdapter = new CursorAdapter(this,cursor,false);
//        listView.setAdapter(cursorAdapter);
//
//
// }

}