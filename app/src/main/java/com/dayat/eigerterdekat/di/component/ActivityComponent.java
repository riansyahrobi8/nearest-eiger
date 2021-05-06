package com.dayat.eigerterdekat.di.component;

import com.dayat.eigerterdekat.di.module.ActivityModule;
import com.dayat.eigerterdekat.ui.activity.map.MapActivity;

import dagger.Component;

// ini adalah interface komponen aktivity
// agar fungsi inject dapat dipanggil
// maka fungsi tersebut sebelumnya harus didelarasi
// di interface ini
@Component(modules = { ActivityModule.class })
public interface ActivityComponent {

    // fungsi yg akan digunakan untuk diinject di activity map
    void inject(MapActivity mapActivity);
}
