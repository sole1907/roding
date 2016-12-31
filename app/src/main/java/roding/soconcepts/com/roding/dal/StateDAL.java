package roding.soconcepts.com.roding.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import roding.soconcepts.com.roding.model.State;

/**
 * Created by Soul on 13/11/2015.
 */
public class StateDAL {

    private SQLiteDatabase db;
    private LocalDatabaseHandler handler;
    private static StateDAL instance;

    private StateDAL() {
    }

    private StateDAL(Context context) {
        handler = new LocalDatabaseHandler(context);
    }

    public static synchronized StateDAL getInstance(Context context) {
        if (instance == null) {
            instance = new StateDAL(context);
        }
        return instance;
    }

    private void open() throws SQLException {
        db = handler.getWritableDatabase();
    }

    private void close() {
        handler.close();
    }

    public List<State> getAllStates() {
        open();
        List<State> states = new ArrayList<State>();

        String query = "SELECT * FROM " + LocalDatabaseHandler.STATE_TABLE + " ORDER BY " + LocalDatabaseHandler.CODE + " ASC";
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                State state = new State(cursor.getString(0), cursor.getString(1));
                states.add(state);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();

        return states;
    }

    public State getStateByCode(String code) {
        open();
        State state = null;

        String query = "SELECT * FROM " + LocalDatabaseHandler.STATE_TABLE + " WHERE " + LocalDatabaseHandler.CODE + " = ? ORDER BY " + LocalDatabaseHandler.CODE + " ASC";
        Cursor cursor = db.rawQuery(query, new String[]{code});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            state = new State(cursor.getString(0), cursor.getString(1));
        }
        cursor.close();
        close();

        return state;
    }

    public void addState(State state) {
        if (state == null)
            return;

        open();
        ContentValues values = new ContentValues();
        values.put(LocalDatabaseHandler.CODE, state.getCode());
        values.put(LocalDatabaseHandler.STATE, state.getState());
        db.insert(LocalDatabaseHandler.STATE_TABLE, null, values);
        close();
    }

    public void deleteAllStates() {
        open();
        db.delete(LocalDatabaseHandler.STATE_TABLE, null, null);
        close();
    }
}
