package com.javine.lessonrecord.data;

import android.support.annotation.NonNull;

import com.javine.lessonrecord.util.AppExecutors;

import java.util.List;

/**
 * @文件描述 : 封装数据库的操作
 * @文件作者 : KuangYu
 * @创建时间 : 18-9-29
 */
public class LessonsLocalDataSource implements LessonsDataSource {

    private static volatile LessonsLocalDataSource sInstance;

    private LessonDao mLessonDao;

    private AppExecutors mAppExecutors;

    private LessonsLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull LessonDao lessonDao){
        mAppExecutors = appExecutors;
        mLessonDao = lessonDao;
    }

    public static LessonsLocalDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull LessonDao lessonDao){
        if (sInstance == null) {
            synchronized (LessonsLocalDataSource.class) {
                if (sInstance == null) {
                    sInstance = new LessonsLocalDataSource(appExecutors, lessonDao);
                }
            }
        }
        return sInstance;
    }


    @Override
    public void getLessons(@NonNull final LoadLessonsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Lesson> lessons = mLessonDao.getAllLessons();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (lessons.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onLessonsLoaded(lessons);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getLesson(@NonNull final String lessonId, @NonNull final GetLessonCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Lesson lesson = mLessonDao.getLessonById(lessonId);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (lesson != null) {
                            callback.onLessonLoaded(lesson);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveLesson(@NonNull final Lesson lesson) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mLessonDao.insertLesson(lesson);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshLessons() {
        /**
         * 由 {@link LessonsRepository} 实现
         */
    }

    @Override
    public void deleteLessons(final Lesson... lessons) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mLessonDao.deleteLessons(lessons);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteLesson(@NonNull final String lessonId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mLessonDao.deleteLessonById(lessonId);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    static void clearInstance() {
        sInstance = null;
    }
}
