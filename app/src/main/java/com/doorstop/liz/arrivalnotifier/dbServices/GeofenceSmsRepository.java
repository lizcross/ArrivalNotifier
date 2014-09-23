package com.doorstop.liz.arrivalnotifier.dbServices;

import com.doorstop.liz.arrivalnotifier.GeofenceSms;
import com.doorstop.liz.arrivalnotifier.GeofenceSmsDao;

import java.util.List;

import de.greenrobot.dao.AbstractDao;

/**
 * Created by liz on 04/09/14.
 */
public class GeofenceSmsRepository extends RepositoryActionsDao<GeofenceSms> {

    private GeofenceSmsDao mDao;

    public GeofenceSmsRepository(GeofenceSmsDao dao) {
        mDao = dao;
    }

    @Override
    protected AbstractDao<GeofenceSms, Long> getDao() {
        return mDao;
    }
}
