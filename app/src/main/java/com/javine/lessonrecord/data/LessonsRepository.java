package com.javine.lessonrecord.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * @文件描述 : 数据仓库,可兼容本地数据及远程数据, 并且缓存取出的数据供全局使用
 * @文件作者 : KuangYu
 * @创建时间 : 18-9-29
 */
public class LessonsRepository implements LessonsDataSource {

    private volatile static LessonsRepository sInstance = null;

    private final LessonsDataSource mLessonDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    private ArrayList<Lesson> mCachedLessons;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

    private LessonsRepository(@NonNull LessonsDataSource lessonsDataSource) {
        mLessonDataSource = checkNotNull(lessonsDataSource);
    }

    public static LessonsRepository getInstance(@NonNull LessonsDataSource lessonsDataSource) {
        if (sInstance == null) {
            synchronized (LessonsRepository.class) {
                if (sInstance == null) {
                    sInstance = new LessonsRepository(lessonsDataSource);
                }
            }
        }
        return sInstance;
    }

    public static void destroyInstance(){
        sInstance = null;
    }

    @Override
    public void getLessons(@NonNull final LoadLessonsCallback callback) {
        checkNotNull(callback);

        if (mCachedLessons != null && !mCacheIsDirty) {
            callback.onLessonsLoaded(new ArrayList<>(mCachedLessons));
            return;
        }

        // TODO: 18-10-14 需要修改：根据时间查询最近两月的数据
        mLessonDataSource.getLessons(new LoadLessonsCallback() {
            @Override
            public void onLessonsLoaded(List<Lesson> lessons) {
                refreshCache(lessons);
                callback.onLessonsLoaded(lessons);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getLesson(@NonNull final String lessonId, @NonNull final GetLessonCallback callback) {
        checkNotNull(lessonId);
        checkNotNull(callback);

        Lesson cachedLesson = getLessonWithId(lessonId);
        if (cachedLesson != null) {
            callback.onLessonLoaded(cachedLesson);
            return;
        }

        mLessonDataSource.getLesson(lessonId, new GetLessonCallback() {
            @Override
            public void onLessonLoaded(Lesson lesson) {
                if (mCachedLessons == null) {
                    mCachedLessons = new ArrayList<>();
                }
                mCachedLessons.add(lesson);
                callback.onLessonLoaded(lesson);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveLesson(@NonNull Lesson lesson) {
        checkNotNull(lesson);
        mLessonDataSource.saveLesson(lesson);

        if (mCachedLessons == null) {
            mCachedLessons = new ArrayList<>();
            mCachedLessons.add(lesson);
        } else {
            insertToCacheByDateOrder(lesson);
        }

    }

    /**
     * 插入数据到缓存列表中，以date排序
     * @param lesson
     */
    private void insertToCacheByDateOrder(Lesson lesson) {
        int count = mCachedLessons.size();
        int insertPoistion = -1;
        long curDateTime = lesson.getDate().getTime();
        int minIndex = 0;
        int maxIndex = count - 1;
        // 二分查询法 查找需要插入的position
        while (minIndex < maxIndex) {
            Lesson firstItem = mCachedLessons.get(minIndex);
            if (curDateTime > firstItem.getDate().getTime()){
                insertPoistion = minIndex;
                break;
            }
            if (minIndex + 1 < maxIndex) {
                int middle = ((maxIndex + minIndex) / 2);
                Lesson middleItem = mCachedLessons.get(middle);
                long middleDateTime = middleItem.getDate().getTime();

                if (curDateTime < middleDateTime) {
                    minIndex = middle;
                } else if (curDateTime > middleDateTime) {
                    maxIndex = middle;
                    minIndex++;
                }
            } else {
                if (curDateTime > mCachedLessons.get(maxIndex).getDate().getTime()) {
                    insertPoistion = maxIndex;
                } else {
                    insertPoistion = maxIndex + 1;
                }
                break;
            }
        }

        if (insertPoistion >= 0 && insertPoistion < count) {
            mCachedLessons.add(insertPoistion, lesson);
        } else {
            mCachedLessons.add(lesson);
        }
    }

    @Override
    public void refreshLessons() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteLessons(Lesson... lessons) {
        mLessonDataSource.deleteLessons(lessons);
        for (Lesson lesson : lessons) {
            mCachedLessons.remove(lesson);
        }
    }

    @Override
    public void deleteLesson(@NonNull String lessonId) {
        mLessonDataSource.deleteLesson(checkNotNull(lessonId));
        mCachedLessons.remove(getLessonWithId(lessonId));
    }

    private void refreshCache(List<Lesson> lessons) {
        if (mCachedLessons == null) {
            mCachedLessons = new ArrayList<>();
        }
        mCachedLessons.clear();
        mCachedLessons.addAll(lessons);
        mCacheIsDirty = false;
    }

    private Lesson getLessonWithId(String lessonId) {
        checkNotNull(lessonId);
        if (mCachedLessons == null || mCachedLessons.isEmpty()) {
            return null;
        } else {
            for (Lesson lesson : mCachedLessons){
                if (lessonId.equals(lesson.getId())){
                    return lesson;
                }
            }
            return null;
        }
    }
}
