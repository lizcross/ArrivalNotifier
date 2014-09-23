package com.doorstop.liz.arrivalnotifier;

import android.app.Application;

import com.doorstop.liz.arrivalnotifier.dbServices.GeofenceModelRepository;
import com.doorstop.liz.arrivalnotifier.dbServices.GeofenceSmsRepository;
import com.doorstop.liz.arrivalnotifier.dbServices.RepositoryActions;

/**
 * Created by liz on 04/09/14.
 */
public class ArrivalNotifierApplication extends Application {

    private DaoSession mDaoSession;
    private GeofenceSmsRepository mGeofenceSmsRepository;
    private GeofenceModelRepository mGeofenceModelRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();

    }

    private void setupDatabase() {
        //Activity contains the db session and passes the session to the fragments
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "arrivalNotifier-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        mDaoSession = daoMaster.newSession();
    }

    public RepositoryActions<GeofenceSms> getGeofenceSmsRepository() {
        if (null == mGeofenceSmsRepository) {
            mGeofenceSmsRepository = new GeofenceSmsRepository(mDaoSession.getGeofenceSmsDao());
        }
        return mGeofenceSmsRepository;
    }

    public RepositoryActions<GeofenceModel> getGeofenceModelRepository() {
        if (null == mGeofenceModelRepository) {
            mGeofenceModelRepository = new GeofenceModelRepository(mDaoSession.getGeofenceModelDao());
        }
        return mGeofenceModelRepository;
    }
}
