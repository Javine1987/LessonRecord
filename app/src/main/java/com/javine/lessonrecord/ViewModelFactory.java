package com.javine.lessonrecord;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.javine.lessonrecord.data.LessonDatabase;
import com.javine.lessonrecord.data.LessonsLocalDataSource;
import com.javine.lessonrecord.data.LessonsRepository;
import com.javine.lessonrecord.util.AppExecutors;
import com.javine.lessonrecord.viewmodel.AddEditViewModel;
import com.javine.lessonrecord.viewmodel.LessonViewModel;

/**
 * @文件描述 :
 * @文件作者 : KuangYu
 * @创建时间 : 18-9-29
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private static volatile ViewModelFactory sInstance;

    private final Application mApplication;

    private final LessonsRepository mRepository;

    private ViewModelFactory(Application application, LessonsRepository lessonsRepository) {
        mApplication = application;
        mRepository = lessonsRepository;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (sInstance == null) {
            synchronized (ViewModelFactory.class) {
                if (sInstance == null) {
                    sInstance = new ViewModelFactory(application,
                            LessonsRepository.getInstance(
                                    LessonsLocalDataSource.getInstance(
                                            new AppExecutors(),
                                            LessonDatabase.getInstance(application).lessonDao())));
                }
            }
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // TODO: 18-9-30 构造不同的ViewModel实例
        if (modelClass.isAssignableFrom(LessonViewModel.class)) {
            return (T) new LessonViewModel(mApplication, mRepository);
        } else if (modelClass.isAssignableFrom(AddEditViewModel.class)){
            return (T) new AddEditViewModel(mApplication, mRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
