package com.kartik.goallist.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kartik.goallist.Model.AddGoalModel;
import com.kartik.goallist.Model.MainGoalModel;

import java.util.ArrayList;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "goalDairy";

    private static final String MAIN_GOAL_TABLE = "main_goal";
    private static final String SUB_GOAL_TABLE = "sub_goal";

    private static final String ID = "id";
    private static final String MAIN_GOAL_TITLE = "main_goal_title";
    private static final String MAIN_GOAL_END_DATE = "main_goal_end_date";
    private static final String MAIN_RECYCLER_POSITION = "main_goal_recycler_position";

    private static final String SUB_GOAL_TITLE = "sub_goal_title";
    private static final String SUB_MAIN_GOAL_ID = "main_goal_id";

    //TODO: create Table Sub Goal
    public static final String CREATE_SUB_GOAL_TABLE = "CREATE TABLE "
            + SUB_GOAL_TABLE + "(" + ID + " INTEGER PRIMARY KEY, "
            + SUB_GOAL_TITLE + " TEXT NOT NULL, "
            + SUB_MAIN_GOAL_ID + " INT, "
            + "FOREIGN KEY(" + SUB_MAIN_GOAL_ID + ") REFERENCES "
            + MAIN_GOAL_TABLE + "(id) " + ")";
    //TODO: create Table MAin Goal
    private static final String CREATE_MAIN_GOAL_TABLE = "CREATE TABLE "
            + MAIN_GOAL_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MAIN_GOAL_TITLE + " TEXT NOT NULL ,"
            + MAIN_GOAL_END_DATE + " DATE ,"
            + MAIN_RECYCLER_POSITION + " INTEGER )";
    private long id;


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MAIN_GOAL_TABLE);
        db.execSQL(CREATE_SUB_GOAL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MAIN_GOAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SUB_GOAL_TABLE);

        onCreate(db);
    }

    //TODO: call when insert goal in main goal table
    public long insertMainGoal(MainGoalModel mainGoal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (!mainGoal.getMaingoalTitle().isEmpty() && !mainGoal.getEndDate().isEmpty()) {
            values.put(MAIN_GOAL_TITLE, mainGoal.getMaingoalTitle());
            values.put(MAIN_GOAL_END_DATE, mainGoal.getEndDate());
            values.put(MAIN_RECYCLER_POSITION, mainGoal.getPosition());
        }

        id = db.insert(MAIN_GOAL_TABLE, null, values);
        return id;
    }

    //TODO: call when insert goal in sub goal table
    public void insertSubGoal(String subGoalData, long mainGoalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (!subGoalData.isEmpty()) {
            values.put(SUB_GOAL_TITLE, subGoalData);
            values.put(SUB_MAIN_GOAL_ID, mainGoalId);
        }
        db.insert(SUB_GOAL_TABLE, null, values);
    }

    //TODO: call when retrieve all main goal
    public ArrayList<MainGoalModel> getAllMainGoal() {
        ArrayList<MainGoalModel> mainGoalList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuary = "SELECT * FROM " + MAIN_GOAL_TABLE + " order by " + MAIN_RECYCLER_POSITION + " ASC ";
        Log.d("TAG", "getAllMainGoal: " + selectQuary);
        Cursor cursor = db.rawQuery(selectQuary, null);
        cursor.moveToNext();
        for (int i = 0; i < cursor.getCount(); i++) {
            MainGoalModel model = new MainGoalModel(cursor.getLong(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(MAIN_GOAL_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MAIN_GOAL_END_DATE)),
                    cursor.getLong(cursor.getColumnIndex(MAIN_RECYCLER_POSITION)));
            mainGoalList.add(model);
            cursor.moveToNext();
        }
        return mainGoalList;
    }

    //TODO: call when retrieve all sub goal
    public ArrayList<AddGoalModel> getSubGoal(long id) {
        ArrayList<AddGoalModel> subGoalList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuary = "SELECT * FROM " + SUB_GOAL_TABLE + " WHERE " + SUB_MAIN_GOAL_ID + " = " + id;
        Cursor cursor = db.rawQuery(selectQuary, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            AddGoalModel model = new AddGoalModel(cursor.getLong(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(SUB_GOAL_TITLE)),
                    cursor.getLong(cursor.getColumnIndex(SUB_MAIN_GOAL_ID)));
            subGoalList.add(model);
            cursor.moveToNext();
        }
        return subGoalList;
    }

    //TODO: call when update main goal
    public long updateMainGoal(MainGoalModel mainGoal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MAIN_GOAL_TITLE, mainGoal.getMaingoalTitle());
        values.put(MAIN_GOAL_END_DATE, mainGoal.getEndDate());

        return db.update(MAIN_GOAL_TABLE, values, ID + " = ? ",
                new String[]{String.valueOf(mainGoal.getId())});


    }

    //TODO: call when delete any of main goal
    public int deleteMainGoal(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MAIN_GOAL_TABLE, ID + " = ? ",
                new String[]{String.valueOf(id)});
    }

    //TODO: call when delete any of sub goal
    public int deleteAnySubGoal(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SUB_GOAL_TABLE, ID + " = ? "
                        + " AND " + SUB_MAIN_GOAL_ID + " = ? ",
                new String[]{String.valueOf(id)});
    }

    //TODO: call when delete all of sub goal
    public int deleteAllSubGoal(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SUB_GOAL_TABLE, SUB_MAIN_GOAL_ID + " = ? ",
                new String[]{String.valueOf(id)});
    }

    //TODO: use for get swaping both position
    public int getPositionofRecord(long position) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fromPositionQuary = "SELECT * FROM " + MAIN_GOAL_TABLE + " WHERE " + MAIN_RECYCLER_POSITION + " = " + position;
        Cursor cursor = db.rawQuery(fromPositionQuary, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            return id;
        }
        return -1;
    }

    //TODO: use for update swaping position
    public void updatePositionRecord(int id, long position) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MAIN_RECYCLER_POSITION, position);

        db.update(MAIN_GOAL_TABLE, values, ID + " = ? ",
                new String[]{String.valueOf(id)});

    }

    public long getMaxPosition() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String getMaxQuary = "SELECT MAX (" + MAIN_RECYCLER_POSITION + ") FROM " + MAIN_GOAL_TABLE;
            Cursor cursor = db.rawQuery(getMaxQuary, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int position = cursor.getInt(0);
                return position + 1;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }
}
