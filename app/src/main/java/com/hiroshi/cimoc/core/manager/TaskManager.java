package com.hiroshi.cimoc.core.manager;

import com.hiroshi.cimoc.model.Task;
import com.hiroshi.cimoc.model.TaskDao;
import com.hiroshi.cimoc.model.TaskDao.Properties;
import com.hiroshi.cimoc.ui.view.BaseView;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import rx.Observable;

/**
 * Created by Hiroshi on 2016/9/4.
 */
public class TaskManager {

    private static TaskManager mInstance;

    private TaskDao mTaskDao;

    private TaskManager(BaseView view) {
        mTaskDao = view.getAppInstance().getDaoSession().getTaskDao();
    }

    public List<Task> listValid() {
        return mTaskDao.queryBuilder()
                .where(Properties.Max.notEq(0))
                .list();
    }

    public List<Task> list(long key) {
        return mTaskDao.queryBuilder()
                .where(Properties.Key.eq(key))
                .list();
    }

    public Observable<List<Task>> listInRx(long key) {
        return mTaskDao.queryBuilder()
                .where(Properties.Key.eq(key))
                .rx()
                .list();
    }

    public Observable<List<Task>> listInRx() {
        return mTaskDao.queryBuilder()
                .rx()
                .list();
    }

    public void insert(Task task) {
        long id = mTaskDao.insert(task);
        task.setId(id);
    }

    public void insertInTx(Iterable<Task> entities) {
        mTaskDao.insertInTx(entities);
    }

    public void update(Task task) {
        mTaskDao.update(task);
    }

    public void delete(Task task) {
        mTaskDao.delete(task);
    }

    public void deleteInTx(Iterable<Task> entities) {
        mTaskDao.deleteInTx(entities);
    }

    public void deleteInTx(long key) {
        mTaskDao.queryBuilder()
                .where(Properties.Key.eq(key))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    public void insertIfNotExist(final Iterable<Task> entities) {
        mTaskDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (Task task : entities) {
                    QueryBuilder<Task> builder = mTaskDao.queryBuilder()
                            .where(Properties.Key.eq(task.getKey()), Properties.Path.eq(task.getPath()));
                    if (builder.unique() == null) {
                        mTaskDao.insert(task);
                    }
                }
            }
        });
    }

    public static TaskManager getInstance(BaseView view) {
        if (mInstance == null) {
            mInstance = new TaskManager(view);
        }
        return mInstance;
    }

}
