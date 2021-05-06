package com.dayat.eigerterdekat.ui.activity.map;

import androidx.annotation.Nullable;

import com.dayat.eigerterdekat.base.BaseContract;
import com.dayat.eigerterdekat.model.location.LocationModel;
import com.dayat.eigerterdekat.model.location.RequestLocation;

import java.util.ArrayList;

public class MapActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    public interface View extends BaseContract.View {

        // fungsi response saat mendapatkan data
        public void onGetListLocation(@Nullable ArrayList<LocationModel> locations);

        // fungsi response saat progress atau loading
        public void showProgressGetListLocation(Boolean show);

        // fungsi response saat mendapatkan error
        public void showErrorGetListLocation(String error);
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    public interface Presenter extends BaseContract.Presenter<MapActivityContract.View> {

        // fungsi untyk mendapatkan data
        // wisata kuliner terdekat
        public void getListLocation(RequestLocation requestLocation, boolean enableLoading);
    }
}
