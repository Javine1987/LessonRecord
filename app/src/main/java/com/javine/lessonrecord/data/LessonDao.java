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

    @Query("SELECT * FROM lessons ORDER BY date DESC")
    List<Lesson> getAllLessons();

    @Query("SELECT * FROM lessons WHERE entryid = :lessonId")
    Lesson getLessonById(String lessonId);

    /**
     * 按时间段查询记录，并按事件逆序排列
     * @param begin 开始时间
     * @param end 结束时间
     * @return 给定范围内的记录列表
     */
    @Query("SELECT * FROM lessons WHERE date >= :begin AND date <= :end ORDER BY date DESC")
    List<Lesson> getLessonsByDate(Long begin, Long end);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLesson(Lesson lesson);

    @Update
    int update(Lesson lesson);

    @Query("DELETE FROM lessons WHERE entryid = :lessonId")
    int deleteLessonById(String lessonId);

    @Delete
    void deleteLessons(Lesson... lessons);
}
