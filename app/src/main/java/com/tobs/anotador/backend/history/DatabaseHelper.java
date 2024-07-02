package com.tobs.anotador.backend.history;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "games_history.db";
    private static final String TABLE_NAME = "games_history";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "game_type TEXT NOT NULL," +
                "player1 TEXT NOT NULL," +
                "player2 TEXT NOT NULL," +
                "result_player1 INTEGER NOT NULL," +
                "result_player2 INTEGER NOT NULL" +
                ")";
        db.execSQL(createTableSQL);

        String insertInitialValuesSQL = "INSERT INTO " + TABLE_NAME + " (game_type, player1, player2, result_player1, result_player2) VALUES " +
                "('Tru', 'tobi', 'jose', 0, 0)," +
                "('Esc', 'tobi', 'jose', 0, 0)," +
                "('Can', 'tobi', 'jose', 0, 0)," +
                "('Gen', 'tobi', 'jose', 0, 0)," +
                "('Bat', 'tobi', 'jose', 0, 0)";
        db.execSQL(insertInitialValuesSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Unused
    }

    public void incrementResult(String game, String name) {
        SQLiteDatabase db = getWritableDatabase();
        String player = "";
        if(name.equals("tobi")) {
            player = "player1";
        } else if(name.equals("jose")) {
            player = "player2";
        }
        String query = "SELECT result_" + player + " FROM " + TABLE_NAME + " WHERE game_type = '" + game + "'";

        Cursor cursor = db.rawQuery(query, null);

        int currentResult = 0;
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("result_" + player);
            if (columnIndex != -1) {
                currentResult = cursor.getInt(columnIndex);
            }
        }

        int newResult = currentResult + 1;
        String updateSQL = "UPDATE " + TABLE_NAME + " SET result_" + player + " = " + newResult + " WHERE game_type = '" + game + "'";
        db.execSQL(updateSQL);

        cursor.close();
        db.close();
    }

    public void updateResult(String game, String player, int newResult) {
        SQLiteDatabase db = getWritableDatabase();
        String column = "";
        if (player.equals("tobi")) {
            column = "result_player1";
        } else if (player.equals("jose")) {
            column = "result_player2";
        }
        String updateSQL = "UPDATE " + TABLE_NAME + " SET " + column + " = " + newResult + " WHERE game_type = ?";
        SQLiteStatement stmt = db.compileStatement(updateSQL);
        stmt.bindString(1, game);
        stmt.executeUpdateDelete();
        db.close();
    }

    public String getResult(String name, String game) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT ";
        if (name.equals("tobi")) {
            query += "result_player1";
        } else if (name.equals("jose")) {
            query += "result_player2";
        }
        query += " FROM " + TABLE_NAME + " WHERE game_type = '" + game + "'";

        Cursor cursor = db.rawQuery(query, null);

        int result = -1;
        if (cursor.moveToFirst()) {
            result = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return String.valueOf(result);
    }
}
