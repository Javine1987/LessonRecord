package com.javine.lessonrecord.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.javine.lessonrecord.SingleLiveEvent;
import com.javine.lessonrecord.data.Lesson;
import com.javine.lessonrecord.data.LessonsDataSource;
import com.javine.lessonrecord.data.LessonsRepository;

/**
 * @文件描述 : 编辑页面vm
 * @文件作者 : KuangYu
 * @创建时间 : 18-10-8
 */
public class AddEditViewModel extends AndroidViewModel implements LessonsDataSource.GetLessonCallback {

    private LessonsRepository mRepository;

    private final SingleLiveEvent<Void> mLessonUpdated = new SingleLiveEvent<>();

    private final SingleLiveEvent<Lesson> mCurrentLesson = new SingleLiveEvent<>();

    public AddEditViewModel(@NonNull Application application, LessonsRepository repository) {
        super(application);
        mRepository = repository;
    }

    public SingleLiveEvent<Void> getLessonUpdated() {
        return mLessonUpdated;
    }

    public SingleLiveEvent<Lesson> getCurrentLesson() {
        return mCurrentLesson;
    }

    public void saveLesson(Lesson lesson){
        mRepository.saveLesson(lesson);
        mLessonUpdated.call();
    }

    public void startGetLesson(String id){
        mRepository.getLesson(id, this);
    }

    @Override
    public void onLessonLoaded(Lesson lesson) {
        mCurrentLesson.setValue(lesson);
    }

    @Override
    public void onDataNotAvailable() {
        mCurrentLesson.setValue(null);
    }

    public void deleteLesson(String lessonId) {
        mRepository.deleteLesson(lessonId);
    }
}
