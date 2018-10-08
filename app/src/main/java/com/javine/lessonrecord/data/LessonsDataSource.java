package com.javine.lessonrecord.data;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @文件描述 : 定义访问数据的接口, 可扩展为本地数据和远程数据
 * @文件作者 : KuangYu
 * @创建时间 : 18-9-29
 */
public interface LessonsDataSource {

    interface LoadLessonsCallback {

        void onLessonsLoaded(List<Lesson> lessons);

        void onDataNotAvailable();
    }

    interface GetLessonCallback {

        void onLessonLoaded(Lesson lesson);

        void onDataNotAvailable();
    }

    void getLessons(@NonNull LoadLessonsCallback callback);

    void getLesson(@NonNull String lessonId, @NonNull GetLessonCallback callback);

    void saveLesson(@NonNull Lesson lesson);

    void refreshLessons();

    void deleteLessons(Lesson... lessons);

    void deleteLesson(@NonNull String lessonId);
}


