package com.techroof.searchrishta.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.techroof.searchrishta.Shortlisted;
import com.techroof.searchrishta.ShortlistedDAO;
import com.techroof.searchrishta.ShortlistedDatabase;

import java.util.List;

public class ShortListedRepositories {

    private ShortlistedDAO shortlistedDAO;
    private LiveData<List<Shortlisted>> allshortlisted;

    public ShortListedRepositories(Application application) {

        ShortlistedDatabase shortlistedDatabase = ShortlistedDatabase.getInstance(application);
        shortlistedDAO = shortlistedDatabase.shortlistedDAO();
        allshortlisted = shortlistedDAO.getAllShortlisted();


    }

    public void insert(Shortlisted shortlisted) {

        new InsertShortlistedAsynctask(shortlistedDAO).execute(shortlisted);

    }


    public void deletenote(String s) {

        new DeleteNoteAsyncTask(shortlistedDAO).execute(s);

    }


    public void deleteallAllNotes() {

        new DeleteAllNotesAsyncTask(shortlistedDAO).execute();

    }


    public LiveData<List<Shortlisted>> getalldata() {

        return allshortlisted;

    }

    public static class InsertShortlistedAsynctask extends AsyncTask<Shortlisted, Void, Void> {

        private ShortlistedDAO shortlistedDAO;

        private InsertShortlistedAsynctask(ShortlistedDAO shortlistedDAO) {

            this.shortlistedDAO = shortlistedDAO;

        }

        @Override
        protected Void doInBackground(Shortlisted... shortlisteds) {

            shortlistedDAO.insert(shortlisteds[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {

        private ShortlistedDAO shortlistedDAO;

        public DeleteAllNotesAsyncTask(ShortlistedDAO shortlistedDAO) {
            this.shortlistedDAO = shortlistedDAO;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            shortlistedDAO.AllNotes();
            return null;
        }


    }

    private static class DeleteNoteAsyncTask extends AsyncTask<String, Void, Void> {
        private ShortlistedDAO shortlistedDAO;

        private DeleteNoteAsyncTask(ShortlistedDAO shortlistedDAO) {
            this.shortlistedDAO = shortlistedDAO;
        }






        @Override
        protected Void doInBackground(String... strings) {
            shortlistedDAO.deleteByUserId(strings[0]);
            return null;
        }
    }
}