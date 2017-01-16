package com.hiroshi.cimoc.core.manager;

import com.hiroshi.cimoc.model.Tag;
import com.hiroshi.cimoc.model.TagDao;
import com.hiroshi.cimoc.ui.view.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by Hiroshi on 2016/10/10.
 */

public class TagManager {

    public static final long TAG_CONTINUE = -11;
    public static final long TAG_FINISH = -10;

    private static TagManager mInstance;

    private TagDao mTagDao;

    private TagManager(BaseView view) {
        mTagDao = view.getAppInstance().getDaoSession().getTagDao();
    }

    public Observable<List<Tag>> listInRx() {
        return mTagDao.queryBuilder()
                .rx()
                .list();
    }

    public Tag load(String title) {
        return mTagDao.queryBuilder()
                .where(TagDao.Properties.Title.eq(title))
                .limit(1)
                .unique();
    }

    public void insert(Tag tag) {
        long id = mTagDao.insert(tag);
        tag.setId(id);
    }

    public void update(Tag tag) {
        mTagDao.update(tag);
    }

    public void delete(Tag entity) {
        mTagDao.delete(entity);
    }

    public static TagManager getInstance(BaseView view) {
        if (mInstance == null) {
            mInstance = new TagManager(view);
        }
        return mInstance;
    }

}
