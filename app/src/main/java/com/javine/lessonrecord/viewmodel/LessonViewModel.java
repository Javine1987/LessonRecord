package com.javine.lessonrecord.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.javine.lessonrecord.SingleLiveEvent;
import com.javine.lessonrecord.data.Lesson;
import com.javine.lessonrecord.data.LessonsDataSource;
import com.javine.lessonrecord.data.LessonsRepository;
import com.javine.lessonrecord.ui.addedit.AddEditLessonActivity;

import java.util.List;

/**
 * @文件描述 :
 * @文件作者 : KuangYu
 * @创建时间 : 18-9-28
 */
public class LessonViewModel extends AndroidViewModel {

    private LessonsRepository mRepository;

    private Context mContext;

    private SingleLiveEvent<String> mOpenLessonEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<List<Lesson>> mLoadLessonsEvent = new SingleLiveEvent<>();

    public LessonViewModel(@NonNull Application application, LessonsRepository repository) {
        super(application);
        mContext = application;
        mRepository = repository;

    }

    public SingleLiveEvent<List<Lesson>> getLoadLessonsEvent() {
        return mLoadLessonsEvent;
    }

    public SingleLiveEvent<String> getOpenLessonEvent() {
        return mOpenLessonEvent;
    }

    public void start() {
        loadLessons(false, true);
    }

    private void loadLessons(boolean forceUpdate, boolean showLoadingUI) {
        if (forceUpdate) {
            mRepository.refreshLessons();
        }

        mRepository.getLessons(new LessonsDataSource.LoadLessonsCallback() {
            @Override
            public void onLessonsLoaded(List<Lesson> lessons) {
                mLoadLessonsEvent.setValue(lessons);
            }

            @Override
            public void onDataNotAvailable() {
                mLoadLessonsEvent.setValue(null);
            }
        });
    }

    public void handleActivityResult(int requestCode, int resultCode){
        if (AddEditLessonActivity.REQUEST_CODE == requestCode) {
            switch (requestCode) {

            }
        }
    }

}
