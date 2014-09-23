package com.doorstop.liz.arrivalnotifier.dbServices;

import com.doorstop.liz.arrivalnotifier.GeofenceModel;
import com.doorstop.liz.arrivalnotifier.GeofenceModelDao;

import de.greenrobot.dao.AbstractDao;

/**
 * Created by liz on 17/09/14.
 */
public class GeofenceModelRepository extends RepositoryActionsDao<GeofenceModel> {
    private GeofenceModelDao mDao;

    public GeofenceModelRepository(GeofenceModelDao dao) {
        mDao = dao;
    }

    @Override
    protected AbstractDao<GeofenceModel, Long> getDao() {
        return mDao;
    }
}
