package com.doorstop.liz.arrivalnotifier.dbServices;

import com.doorstop.liz.arrivalnotifier.GeofenceModel;

import java.util.List;

import de.greenrobot.dao.AbstractDao;

/**
 * Create an interface for database operations so that app doesn't know directly
 * about GreenDao infrastructure. Opens up possibility of unit testing without using
 * GreenDao infrastructure.
 * Created by liz on 04/09/14.
 */
public interface RepositoryActions<T> {
    public Long insertOrUpdate(T item);
    public void clearItems();
    public void deleteItemWithId(long id);
    public List<T> getAllItems();
    public T getItemForId(long id);
}
