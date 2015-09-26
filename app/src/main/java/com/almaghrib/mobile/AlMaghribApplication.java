package com.almaghrib.mobile;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class AlMaghribApplication extends Application {

    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "KDXdbTDgB8oB6Zqa8kzXN94N7KKhxUFGWaN4ftBY", "PDSxlXq9Z7oKfg6sZ7oFucozxT3s0DZs47g1J1gS");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}
