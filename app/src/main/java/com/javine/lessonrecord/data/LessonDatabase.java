package com.javine.lessonrecord.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * @文件描述 : 课程记录数据库
 * @文件作者 : KuangYu
 * @创建时间 : 18-9-26
 */
@Database(entities = {Lesson.class}, version = 1)
@TypeConverters({DataConverters.class})
public abstract class LessonDatabase extends RoomDatabase{

    private static LessonDatabase INSTANCE;

    public abstract LessonDao lessonDao();

    private static final Object sLock = new Object();

    public static LessonDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        LessonDatabase.class, "Lessons.db").build();
            }
            return INSTANCE;
        }
    }
}
