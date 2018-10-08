package com.javine.lessonrecord.ui.addedit;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.javine.lessonrecord.R;
import com.javine.lessonrecord.ViewModelFactory;
import com.javine.lessonrecord.util.ActivityUtils;
import com.javine.lessonrecord.viewmodel.AddEditViewModel;

/**
 * @文件描述 : 新增和编辑课程页面
 * @文件作者 : KuangYu
 * @创建时间 : 18-10-8
 */
public class AddEditLessonActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0x11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlesson_act);
        setupActionBar();

        AddEditLessonFragment fragment = obtainViewFragment();
        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);

        AddEditViewModel viewModel = obtainViewModel(this);
        viewModel.getLessonUpdated().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void aVoid) {
                onTaskSaved();
            }
        });

    }

    private void setupActionBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    public static AddEditViewModel obtainViewModel(FragmentActivity activity){
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(AddEditViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void onTaskSaved() {
        setResult(RESULT_OK);
        finish();
    }

    private AddEditLessonFragment obtainViewFragment() {
        // View Fragment
        AddEditLessonFragment addEditTaskFragment = (AddEditLessonFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (addEditTaskFragment == null) {
            addEditTaskFragment = AddEditLessonFragment.newInstance();

            // Send the task ID to the fragment
            Bundle bundle = new Bundle();
            bundle.putString(AddEditLessonFragment.ARGUMENT_EDIT_LESSON_ID,
                    getIntent().getStringExtra(AddEditLessonFragment.ARGUMENT_EDIT_LESSON_ID));
            addEditTaskFragment.setArguments(bundle);
        }
        return addEditTaskFragment;
    }
}
