package tv.wanzi.demo.player;

import android.app.Application;
import android.content.Context;

/**
 * Created by drawf on 17/1/1.
 * ------------------------------
 */

public class MainApplication extends Application {
    private static MainApplication sInstance;
    private static Context sContext;

    public static MainApplication getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sContext = this.getApplicationContext();
        init();
    }

    private void init() {
    }
}
