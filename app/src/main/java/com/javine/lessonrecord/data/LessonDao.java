package com.javine.lessonrecord.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * @文件描述 : Room数据库操作接口
 * @文件作者 : KuangYu
 * @创建时间 : 18-9-26
 */
@Dao
public interface LessonDao {

    @Query("SELECT * FROM lessons")
    List<Lesson> getAllLessons();

    @Query("SELECT * FROM lessons WHERE entryid = :lessonId")
    Lesson getLessonById(String lessonId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLesson(Lesson lesson);

    @Update
    int update(Lesson lesson);

    @Query("DELETE FROM lessons WHERE entryid = :lessonId")
    int deleteLessonById(String lessonId);

    @Delete
    void deleteLessons(Lesson... lessons);
}
