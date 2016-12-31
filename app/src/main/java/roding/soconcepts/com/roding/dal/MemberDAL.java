package roding.soconcepts.com.roding.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import roding.soconcepts.com.roding.model.Hospital;
import roding.soconcepts.com.roding.model.Member;

/**
 * Created by Soul on 13/11/2015.
 */
public class MemberDAL {

    private SQLiteDatabase db;
    private LocalDatabaseHandler handler;
    private static MemberDAL instance;

    private MemberDAL() {
    }

    private MemberDAL(Context context) {
        handler = new LocalDatabaseHandler(context);
    }

    public static synchronized MemberDAL getInstance(Context context) {
        if (instance == null) {
            instance = new MemberDAL(context);
        }
        return instance;
    }

    private void open() throws SQLException {
        db = handler.getWritableDatabase();
    }

    private void close() {
        handler.close();
    }

    public List<Member> getAllMembers() {
        open();
        List<Member> memberList = new ArrayList<Member>();

        String query = "SELECT * FROM " + LocalDatabaseHandler.MEMBER_TABLE + " ORDER BY " + LocalDatabaseHandler.SURNAME + " ASC";
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Member member = new Member(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4)>0);
                memberList.add(member);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();

        return memberList;
    }

    public List<Member> filterHospital(String surname, boolean isActive) {
        open();
        List<Member> memberList = new ArrayList<>();
        Cursor cursor = null;

        if (surname != null) {
            String query = "SELECT * FROM " + LocalDatabaseHandler.MEMBER_TABLE + " WHERE " + LocalDatabaseHandler.SURNAME + " LIKE ? AND " + LocalDatabaseHandler.ACTIVE + " = ? ORDER BY " + LocalDatabaseHandler.SURNAME + " ASC";
            cursor = db.rawQuery(query, new String[]{'%' + surname + '%', isActive ? "1" : "0"});
        } else {
            String query = "SELECT * FROM " + LocalDatabaseHandler.MEMBER_TABLE + " WHERE " + LocalDatabaseHandler.ACTIVE + " = ? ORDER BY " + LocalDatabaseHandler.SURNAME + " ASC";
            cursor = db.rawQuery(query, new String[]{isActive ? "1" : "0"});
        }

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Member member = new Member(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4) > 0);
                memberList.add(member);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();

        return memberList;
    }

    public void addMember(Member member) {
        if (member == null)
            return;

        open();
        ContentValues values = new ContentValues();
        values.put(LocalDatabaseHandler.ID, member.getId());
        values.put(LocalDatabaseHandler.SURNAME, member.getSurname());
        values.put(LocalDatabaseHandler.OTHERNAMES, member.getOtherNames());
        values.put(LocalDatabaseHandler.MEMBER_ID, member.getMemberId());
        values.put(LocalDatabaseHandler.ACTIVE, member.isActive() ? 1 : 0);
        db.insert(LocalDatabaseHandler.MEMBER_TABLE, null, values);
        close();
    }

   public void deleteAllMembers() {
        open();
        db.delete(LocalDatabaseHandler.MEMBER_TABLE, null, null);
        close();
    }
}
