package com.example.schoolapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final  class SchoolAppContract {

    private SchoolAppContract() {
    }


        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "schooldb";

        public static final String SCHEME = "content://";
        public static final String AUTHORITY = "com.example.schoolapp";
        public static final String PATH_MEMBERS = "members";

        public static final Uri BASE_CONTENT_URI =
                Uri.parse(SCHEME + AUTHORITY);

    public static final class MemberEntry implements BaseColumns {

        public static final String TABLE_NAME = "members";

        //tablitsa maydonlari
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMNGENDER = "gender";
        public static final String COLUMN_KURS = "kurs";


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);

        public static final int TANLANMAGAN = 0;
        public static final int ERKAK = 1;
        public static final int AYOL = 2;

        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;
        public static final String CONTENT_BASE_ITEMS = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;
    }
}
