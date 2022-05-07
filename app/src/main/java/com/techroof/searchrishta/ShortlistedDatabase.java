package com.techroof.searchrishta;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Shortlisted.class}, version = 7)
public abstract class ShortlistedDatabase extends RoomDatabase {



    private static ShortlistedDatabase instance;

    public abstract ShortlistedDAO shortlistedDAO();

    public static synchronized ShortlistedDatabase getInstance(Context context) {


        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ShortlistedDatabase.class,
                    "short_listeddatabase").
                    fallbackToDestructiveMigration().
                    addCallback(roomcallback).build();


        }


        return instance;
    }

    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private ShortlistedDAO shortlistedDAO;

        private PopulateDbAsyncTask(ShortlistedDatabase database) {


            shortlistedDAO = database.shortlistedDAO();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            /*shortlistedDAO.insert(new Shortlisted("1", "Usama", "21-3-21",
                    "6-1", "Islam", "Bachelors", "Unmarried",
                    "Islamabad", "Punjab", "Pakistan"));

            //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
            Log.d("log", "doInBackground: ");*/
            return null;

        }
    }

}
