package com.example.schoolapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolapp.data.SchoolAppContract;

import org.w3c.dom.Text;

public class CursorAdapter extends androidx.cursoradapter.widget.CursorAdapter {


    public CursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.member_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView firstName = view.findViewById(R.id.first_name);
        TextView lastName = view.findViewById(R.id.last_name);
        TextView kurs = view.findViewById(R.id.kurs);

        String _firstName = cursor.getString(cursor.getColumnIndexOrThrow(SchoolAppContract.MemberEntry.COLUMN_FIRST_NAME));
        String _lastName = cursor.getString(cursor.getColumnIndexOrThrow(SchoolAppContract.MemberEntry.COLUMN_LAST_NAME));
        String _kurs = cursor.getString(cursor.getColumnIndexOrThrow(SchoolAppContract.MemberEntry.COLUMN_KURS));

        firstName.setText(_firstName);
        lastName.setText(_lastName);
        kurs.setText(_kurs);
    }
}
