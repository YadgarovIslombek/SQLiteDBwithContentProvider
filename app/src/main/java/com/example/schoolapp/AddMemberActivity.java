package com.example.schoolapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.schoolapp.data.SchoolAppContract.MemberEntry;

public class AddMemberActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText nameEd, nameLastEd, kurs;
    private Spinner spinnerGender;
    private ArrayAdapter spinnerAdapter;
    private static final int MEMBER_LOADER_IN = 110;
    int gender = 0;
    private Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent intent = getIntent();
        currentUri = intent.getData();
        if (currentUri == null) {
            setTitle("O'quvchi qo'shish");
            invalidateOptionsMenu();
        } else {
            setTitle("O'zgartirish");
            getSupportLoaderManager().initLoader(MEMBER_LOADER_IN, null, this);
        }
        nameEd = findViewById(R.id.nameEd);
        nameLastEd = findViewById(R.id.nameLastEd);
        kurs = findViewById(R.id.kurs);
        spinnerGender = findViewById(R.id.spinnerGender);
//        spinnerArraylist = new ArrayList();
//        spinnerArraylist.add("Unkown");
//        spinnerArraylist.add("Male");
//        spinnerArraylist.add("Female");
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender, android.R.layout.simple_spinner_item);
        spinnerGender.setAdapter(spinnerAdapter);


        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedGender =
                        (String) adapterView.getItemAtPosition(i);
                if (!TextUtils.isEmpty(selectedGender)) {
                    if (selectedGender.equals("Erkak")) {
                        gender = MemberEntry.ERKAK;
                    } else if (selectedGender.equals("Ayol")) {
                        gender = MemberEntry.AYOL;
                    } else {
                        gender = MemberEntry.TANLANMAGAN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender = MemberEntry.TANLANMAGAN;
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(currentUri == null){
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveMember:
                saveMember();
                return true;
            case R.id.delete:
                showDeleteDialog();
                return true;

            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return true;
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("O'chirishni hohlaysizmi?");
        builder.setPositiveButton("HA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteMember();
            }
        }).setNegativeButton("YO'Q", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        }).show();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteMember() {
        if(currentUri != null){
            int rowsDeleted = getContentResolver().delete(currentUri,null,null);
            if(rowsDeleted == 0){

                    Toast.makeText(this, "Ma'lumot o'chirilmadi", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "Ma'lumot o'chirildi!", Toast.LENGTH_SHORT).show();

            }
            finish();
        }

    }

    private void saveMember() {
        String _firstName = nameEd.getText().toString().trim();
        String _lastName = nameLastEd.getText().toString().trim();
        String _kurs = kurs.getText().toString().trim();
        if(TextUtils.isEmpty(_firstName)){
            Toast.makeText(this, "Ismingizni yozing", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(_lastName)){
            Toast.makeText(this, "Familiyangizni yozing", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(_kurs)){
            Toast.makeText(this, "kursingizni yozing", Toast.LENGTH_SHORT).show();
        }
        else if(gender == MemberEntry.TANLANMAGAN){
            Toast.makeText(this, "Jins tanlanmagan", Toast.LENGTH_SHORT).show();
            return;
        }else {


            ContentValues contentValues = new ContentValues();
            contentValues.put(MemberEntry.COLUMN_FIRST_NAME, _firstName);
            contentValues.put(MemberEntry.COLUMN_LAST_NAME, _lastName);
            contentValues.put(MemberEntry.COLUMN_KURS, _kurs);
            contentValues.put(MemberEntry.COLUMNGENDER, gender);

            if (currentUri == null) {
                ContentResolver contentResolver = getContentResolver();
                Uri uri = contentResolver.insert(MemberEntry.CONTENT_URI, contentValues);
                if (uri == null) {
                    Toast.makeText(this, "TAble topilmadiquuu", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "malumot saqlandi", Toast.LENGTH_SHORT).show();

                }
            } else {
                int rowsChanged = getContentResolver().update(currentUri, contentValues, null, null);
                if (rowsChanged == 0) {
                    Toast.makeText(this, "Ma'lumotlarni saqlashda xatolik!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "O'zgartirish kiritildi!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
//implements LoaderManager.LoaderCallbacks<Cursor>
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                MemberEntry._ID,
                MemberEntry.COLUMN_FIRST_NAME,
                MemberEntry.COLUMN_LAST_NAME,
                MemberEntry.COLUMNGENDER,
                MemberEntry.COLUMN_KURS
        };
        return new CursorLoader(this, currentUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int FirstNameIndex = cursor.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME);
            int LastNameIndex = cursor.getColumnIndex(MemberEntry.COLUMN_LAST_NAME);
            int GenderIndex = cursor.getColumnIndex(MemberEntry.COLUMNGENDER);
            int KursIndex = cursor.getColumnIndex(MemberEntry.COLUMN_KURS);


            String currentFirstName = cursor.getString(FirstNameIndex);
            String currentLastName = cursor.getString(LastNameIndex);
            int currentGender = cursor.getInt(GenderIndex);
            String currentKurs = cursor.getString(KursIndex);

            nameEd.setText(currentFirstName);
            nameLastEd.setText(currentLastName);
            kurs.setText(currentKurs);

            switch (currentGender) {
                case MemberEntry.ERKAK:
                    spinnerGender.setSelection(1);
                    break;
                case MemberEntry.AYOL:
                    spinnerGender.setSelection(2);
                    break;
                case MemberEntry.TANLANMAGAN:
                    spinnerGender.setSelection(0);
                    break;
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader.cancelLoad();
    }
}