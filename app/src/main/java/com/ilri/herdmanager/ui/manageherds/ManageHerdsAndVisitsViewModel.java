package com.ilri.herdmanager.ui.manageherds;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ManageHerdsAndVisitsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ManageHerdsAndVisitsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}