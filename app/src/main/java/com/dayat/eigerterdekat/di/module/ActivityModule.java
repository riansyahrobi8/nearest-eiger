package com.dayat.eigerterdekat.di.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;


// ini adalah class dimana
// setiap melakukan injecksi
// presenter ke activity
// maka akan di provide presenter
// untuk aktivity yg bersangkutan
@Module
public class ActivityModule {

    // dekalrasi variabel activity
    private Activity activity;

    // konstruktor class
    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    // fungsi untuk provide activity
    // dengan nilai balik adalah variabel activity
    // yg telah diinisialisasi
    @Provides
    public Activity provideActivity()  {
        return activity;
    }

    // fungsi untuk provide presenter pada activity login
//    @Provides
//    public MapFragmentContract.Presenter provideMapActivityPresenter() {
//        return new MapFragmentPresenter();
//    }
}
