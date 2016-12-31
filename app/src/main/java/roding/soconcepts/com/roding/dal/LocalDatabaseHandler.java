package roding.soconcepts.com.roding.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fmontet on 12/11/2015.
 */

public class LocalDatabaseHandler extends SQLiteOpenHelper {

    public static final String RODING_DB = "roding.db";
    // if you change the database schema, you must increment the database version
    public static final int DATABASE_VERSION = 3;

    public static final String STATE_TABLE = "state";
    // user table column names
    public static final String CODE = "code";

    public static final String HOSPITAL_TABLE = "hospital";
    // branch table column names
    public static final String ID = "id";
    public static final String CITY = "city";
    public static final String HOSPITAL_ADDRESS = "address";
    public static final String STATE = "state";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String NAME = "name";

    private static final String SQL_CREATE_STATE_TABLE =
            "CREATE TABLE " + STATE_TABLE + " ("
                    + CODE + " TEXT PRIMARY KEY,"
                    + STATE + " TEXT"
                    + ")";

    private static final String SQL_DELETE_STATE_TABLE =
            "DROP TABLE IF EXISTS " + STATE_TABLE;

    private static final String SQL_CREATE_HOSPITAL_TABLE =
            "CREATE TABLE " + HOSPITAL_TABLE + " ("
                    + ID + " INTEGER,"
                    + NAME + " TEXT,"
                    + CITY + " TEXT NULL,"
                    + HOSPITAL_ADDRESS + " TEXT NULL,"
                    + STATE + " TEXT NULL,"
                    + PHONE + " TEXT NULL,"
                    + EMAIL + " TEXT NULL"
                    + ")";

    private static final String SQL_DELETE_HOSPITAL_TABLE =
            "DROP TABLE IF EXISTS " + HOSPITAL_TABLE;

    public static final String MEMBER_TABLE = "member";
    public static final String SURNAME = "surname";
    public static final String ACTIVE = "active";
    public static final String OTHERNAMES = "other_names";
    public static final String MEMBER_ID = "member_id";

    private static final String SQL_CREATE_MEMBER_TABLE =
            "CREATE TABLE " + MEMBER_TABLE + " ("
                    + ID + " INTEGER PRIMARY KEY,"
                    + MEMBER_ID + " TEXT,"
                    + SURNAME + " TEXT,"
                    + OTHERNAMES + " TEXT,"
                    + ACTIVE + " INT"
                    + ")";

    private static final String SQL_DELETE_MEMBER_TABLE =
            "DROP TABLE IF EXISTS " + MEMBER_TABLE;

    public LocalDatabaseHandler(Context context) {
        super(context, RODING_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STATE_TABLE);
        db.execSQL(SQL_CREATE_HOSPITAL_TABLE);
        db.execSQL(SQL_CREATE_MEMBER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_STATE_TABLE);
        db.execSQL(SQL_DELETE_HOSPITAL_TABLE);
        db.execSQL(SQL_DELETE_MEMBER_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
