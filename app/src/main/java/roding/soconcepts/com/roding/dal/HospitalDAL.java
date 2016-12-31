package roding.soconcepts.com.roding.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import roding.soconcepts.com.roding.model.Hospital;

/**
 * Created by Soul on 13/11/2015.
 */
public class HospitalDAL {

    private SQLiteDatabase db;
    private LocalDatabaseHandler handler;
    private static HospitalDAL instance;

    private HospitalDAL() {
    }

    private HospitalDAL(Context context) {
        handler = new LocalDatabaseHandler(context);
    }

    public static synchronized HospitalDAL getInstance(Context context) {
        if (instance == null) {
            instance = new HospitalDAL(context);
        }
        return instance;
    }

    private void open() throws SQLException {
        db = handler.getWritableDatabase();
    }

    private void close() {
        handler.close();
    }

    public List<Hospital> getAllHospitals() {
        open();
        List<Hospital> hospitalList = new ArrayList<Hospital>();

        String query = "SELECT * FROM " + LocalDatabaseHandler.HOSPITAL_TABLE + " ORDER BY " + LocalDatabaseHandler.NAME + " ASC";
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Hospital hospital = new Hospital(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                hospitalList.add(hospital);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();

        return hospitalList;
    }

    public List<Hospital> getAllHospitalsInStates(String state1, String state2, String hospitalName) {
        open();
        List<Hospital> hospitalList = new ArrayList<>();
        Cursor cursor = null;

        if (state1 != null && hospitalName == null) {
            String query = "SELECT * FROM " + LocalDatabaseHandler.HOSPITAL_TABLE + " WHERE " + LocalDatabaseHandler.STATE + " IN (?, ?) ORDER BY " + LocalDatabaseHandler.NAME + " ASC";
            cursor = db.rawQuery(query, new String[]{state1, state2});
        }

        if (state1 == null && hospitalName != null) {
            String query = "SELECT * FROM " + LocalDatabaseHandler.HOSPITAL_TABLE + " WHERE " + LocalDatabaseHandler.NAME + " LIKE ? ORDER BY " + LocalDatabaseHandler.NAME + " ASC";
            cursor = db.rawQuery(query, new String[]{'%' + hospitalName + '%'});
        }

        if (state1 != null && hospitalName != null) {
            String query = "SELECT * FROM " + LocalDatabaseHandler.HOSPITAL_TABLE + " WHERE " + LocalDatabaseHandler.STATE + " IN (?,?) AND " + LocalDatabaseHandler.NAME + " LIKE ? ORDER BY " + LocalDatabaseHandler.NAME + " ASC";
            cursor = db.rawQuery(query, new String[]{state1, state2, '%' + hospitalName + '%'});
        }

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Hospital hospital = new Hospital(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                hospitalList.add(hospital);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();

        return hospitalList;
    }

    /*public User getUser(String userPin) {

        open();

        // columns from db table you want to use after the query
        String[] protection = {LocalDatabaseHandler.USER_PIN, LocalDatabaseHandler.FIRST_NAME, LocalDatabaseHandler.LAST_NAME, LocalDatabaseHandler.USER_BALANCE, LocalDatabaseHandler.PHONE_NUMBER, LocalDatabaseHandler.USER_EMAIL, LocalDatabaseHandler.ADDRESS, LocalDatabaseHandler.PASSWORD_CHANGED, LocalDatabaseHandler.USERNAME};
        String selection = LocalDatabaseHandler.USER_PIN + "= ?";
        String[] selectionArgs = new String[]{userPin+""};

        Cursor cursor = db.query(
                LocalDatabaseHandler.USER_TABLE,
                protection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            User user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
            cursor.close();
            close();
            return user;
        }
        cursor.close();
        close();
        return null;
    }

    public User getUser() {

        open();

        // columns from db table you want to use after the query
        String[] protection = {LocalDatabaseHandler.USER_PIN, LocalDatabaseHandler.FIRST_NAME, LocalDatabaseHandler.LAST_NAME, LocalDatabaseHandler.USER_BALANCE, LocalDatabaseHandler.PHONE_NUMBER, LocalDatabaseHandler.USER_EMAIL, LocalDatabaseHandler.ADDRESS, LocalDatabaseHandler.PASSWORD_CHANGED, LocalDatabaseHandler.USERNAME};

        Cursor cursor = db.query(
                LocalDatabaseHandler.USER_TABLE,
                protection,
                null,
                null,
                null,
                null,
                null,
                "1"
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                User user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
                cursor.close();
                close();
                return user;
            }
        }
        cursor.close();
        close();
        return null;
    }*/

    /*public void updateBranch(Branch branch) {
        if (branch == null)
            return;
        open();
        ContentValues newValues = new ContentValues();
        //String[] args = new String[]{user.getUserPin()+""};
        newValues.put(LocalDatabaseHandler.CITY, branch.getCity());
        newValues.put(LocalDatabaseHandler.LAST_NAME, user.getLastname());
        newValues.put(LocalDatabaseHandler.PHONE_NUMBER, user.getPhoneNumber());
        newValues.put(LocalDatabaseHandler.USER_BALANCE, user.getUserBalance());
        newValues.put(LocalDatabaseHandler.USER_EMAIL, user.getEmail());
        newValues.put(LocalDatabaseHandler.ADDRESS, user.getAddress());
        newValues.put(LocalDatabaseHandler.PASSWORD_CHANGED, user.getPasswordChanged());
        newValues.put(LocalDatabaseHandler.USERNAME, user.getUsername());
        db.update(LocalDatabaseHandler.USER_TABLE, newValues, null, null);
        close();
    }*/

    public void addHospital(Hospital hospital) {
        if (hospital == null)
            return;

        open();
        ContentValues values = new ContentValues();
        values.put(LocalDatabaseHandler.ID, hospital.getId());
        values.put(LocalDatabaseHandler.NAME, hospital.getName());
        values.put(LocalDatabaseHandler.CITY, hospital.getCity());
        values.put(LocalDatabaseHandler.HOSPITAL_ADDRESS, hospital.getAddress());
        values.put(LocalDatabaseHandler.STATE, hospital.getState());
        values.put(LocalDatabaseHandler.PHONE, hospital.getPhone());
        values.put(LocalDatabaseHandler.EMAIL, hospital.getEmail());
        db.insert(LocalDatabaseHandler.HOSPITAL_TABLE, null, values);
        close();
    }

    /*public void deleteUser(User user) {
        if (user == null)
            return;
        open();
        String[] args = new String[]{user.getUserPin()};
        db.delete(LocalDatabaseHandler.USER_TABLE, LocalDatabaseHandler.USER_PIN + "= ?", args);
        close();
    }*/

    public void deleteAllHospitals() {
        open();
        db.delete(LocalDatabaseHandler.HOSPITAL_TABLE, null, null);
        close();
    }
}
