package com.doorstop.liz.arrivalnotifier.dbServices;

import android.content.Context;

import java.util.List;

import de.greenrobot.dao.AbstractDao;

/**
 * Created by liz on 04/09/14.
 */
public abstract class RepositoryActionsDao<T> implements RepositoryActions<T> {

    @Override
    public Long insertOrUpdate(T item) {
        long id = getDao().insertOrReplace(item);
        return new Long(id);
    }

    @Override
    public void clearItems() {
        getDao().deleteAll();
    }

    @Override
    public void deleteItemWithId(long id) {
        getDao().delete(getItemForId(id));
    }

    @Override
    public List<T> getAllItems() {
        return getDao().loadAll();
    }

    @Override
    public T getItemForId(long id) {
        return getDao().load(id);
    }

    protected abstract AbstractDao<T, Long> getDao();
}
