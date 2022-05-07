package com.techroof.searchrishta.ViewModel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.techroof.searchrishta.Repositories.ShortListedRepositories;
import com.techroof.searchrishta.Shortlisted;
import com.techroof.searchrishta.ShortlistedDAO;

import java.util.List;

public class ShortlistedViewModel extends AndroidViewModel {

    private ShortListedRepositories shortListedRepositories;
    private LiveData<List<Shortlisted>> allshortlisted;

    public ShortlistedViewModel(@NonNull Application application) {
        super(application);
        shortListedRepositories = new ShortListedRepositories(application);
        allshortlisted = shortListedRepositories.getalldata();

    }

    public void insert(Shortlisted shortlisted) {


        shortListedRepositories.insert(shortlisted);
    }

    /*public void delete(Shortlisted shortlisted) {
        shortListedRepositories.delete(shortlisted);
    }*/

    public void deletenote(String s) {
        shortListedRepositories.deletenote(s);
    }

    public void deleteAllNotes() {

        shortListedRepositories.deleteallAllNotes();
    }

    public LiveData<List<Shortlisted>> getAllshortlisted() {

        return allshortlisted;

    }

    /*private static class InsertShortlistedAsynctask extends AsyncTask<Shortlisted,Void,Void>{
        @Override
        protected Void doInBackground(Shortlisted... shortlisteds) {


            return null;
        }
    }*/

}
