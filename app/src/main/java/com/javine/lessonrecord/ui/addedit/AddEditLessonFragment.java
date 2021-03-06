package com.javine.lessonrecord.ui.addedit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.javine.lessonrecord.R;
import com.javine.lessonrecord.data.Lesson;
import com.javine.lessonrecord.util.SnackbarUtils;
import com.javine.lessonrecord.viewmodel.AddEditViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @文件描述 :
 * @文件作者 : KuangYu
 * @创建时间 : 18-10-8
 */
public class AddEditLessonFragment extends Fragment implements View.OnClickListener {

    public static final String ARGUMENT_EDIT_LESSON_ID = "EDIT_TASK_ID";

    private AddEditViewModel mViewModel;

    private String mLessonId = null;

    private Lesson mCurrentLesson;
    private Date mCurrentDate;
    private Calendar mSetCalendar;

    private AutoCompleteTextView mStudioTextView; //舞蹈室名称
    private EditText mDurationEditText;  //时长
    private EditText mSalaryEditText;   //报酬
    private TextView mDateTextView;     //日期
    private TextView mTimeTextView;     //时间
    private AlertDialog mDeleteDialog;

    public static AddEditLessonFragment newInstance(){
        return new AddEditLessonFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.addlesson_frag, container, false);
        initViews(root);
        mViewModel = AddEditLessonActivity.obtainViewModel(getActivity());
        setHasOptionsMenu(true);
        setRetainInstance(false);
        return root;
    }

    private void initViews(View root) {
        mStudioTextView = root.findViewById(R.id.studio_name);
        mDurationEditText = root.findViewById(R.id.duration);
        mSalaryEditText = root.findViewById(R.id.salary);
        mDateTextView = root.findViewById(R.id.date);
        mTimeTextView = root.findViewById(R.id.time);
        mDateTextView.setOnClickListener(this);
        mTimeTextView.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupFab();
        setupActionBar();
        mViewModel.getCurrentLesson().observe(this, new Observer<Lesson>() {
            @Override
            public void onChanged(@Nullable Lesson lesson) {
                if (lesson != null){
                    updateUi(lesson);
                }else {
                    showEmpty();
                }
            }
        });

        loadData();
    }

    private void loadData() {
        mLessonId = getArguments() != null ? getArguments().getString(ARGUMENT_EDIT_LESSON_ID): null;
        if (mLessonId != null) {
            mViewModel.startGetLesson(mLessonId);
        } else {
            setTimeUI();
        }
    }

    /**
     * 给日期控件设置当前时间
     */
    private void setTimeUI() {
        if (mCurrentDate == null){
            mCurrentDate = new Date();
        }
        if (mSetCalendar == null) {
            mSetCalendar = Calendar.getInstance();
        }
        //同步date到calendar
        mSetCalendar.setTime(mCurrentDate);
        String dateString = new SimpleDateFormat("yyyy-MM-dd E").format(mCurrentDate);
        mDateTextView.setText(dateString);
        String timeString = new SimpleDateFormat("HH:mm").format(mCurrentDate);
        mTimeTextView.setText(timeString);
    }

    private void showEmpty() {

    }

    private void updateUi(Lesson lesson) {
        mCurrentLesson = lesson;
        mStudioTextView.setText(lesson.getPlace());
        mDurationEditText.setText(String.valueOf(lesson.getDuration()));
        mSalaryEditText.setText(String.valueOf(lesson.getSalary()));
        mCurrentDate = lesson.getDate();
        setTimeUI();
    }

    private Lesson getCurrentLesson(){
        if (hasContent(mStudioTextView) && hasContent(mSalaryEditText) && hasContent(mDurationEditText)) {

            if (mCurrentLesson == null) {
                mCurrentLesson = new Lesson();
            }

            mCurrentLesson.setPlace(mStudioTextView.getText().toString());
            mCurrentLesson.setDuration(Integer.valueOf(mDurationEditText.getText().toString()));
            mCurrentLesson.setSalary(Integer.valueOf(mSalaryEditText.getText().toString()));

            if (mCurrentDate != null) {
                mCurrentLesson.setDate(mCurrentDate);
            } else {
                mCurrentDate = new Date();
                mCurrentLesson.setDate(mCurrentDate);
            }

            return mCurrentLesson;
        }
        return null;
    }

    private void setupFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lesson lesson = getCurrentLesson();
                if (lesson != null) {
                    mViewModel.saveLesson(mCurrentLesson);
                } else {
                    SnackbarUtils.showSnackbar(getView(), "请填写完整再提交");
                }
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        if (ifHasLessonId()) {
            actionBar.setTitle(R.string.edit_lesson);
        } else {
            actionBar.setTitle(R.string.add_lesson);
        }
    }

    private boolean ifHasLessonId() {
        return mLessonId != null
                || (getArguments() != null && (mLessonId = getArguments().getString(ARGUMENT_EDIT_LESSON_ID)) != null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getActivity() != null && ifHasLessonId()){
            getActivity().getMenuInflater().inflate(R.menu.edit_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            if (mDeleteDialog == null) {
                mDeleteDialog = new AlertDialog.Builder(getContext()).setMessage("删除这节课的记录？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.deleteLesson(mLessonId);
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                }).create();
            }
            mDeleteDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean hasContent(EditText editText) {
        return editText != null && editText.getText().length() > 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date:
                showDatePicker();
                break;
            case R.id.time:
                showDateTimePicker();
                break;
        }
    }

    private void showDateTimePicker() {
        if (mSetCalendar == null) {
            mSetCalendar = Calendar.getInstance();
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                mSetCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                mSetCalendar.set(Calendar.MINUTE, timePicker.getMinute());
                mCurrentDate = mSetCalendar.getTime();
                setTimeUI();
            }
        }, mSetCalendar.get(Calendar.HOUR_OF_DAY), mSetCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void showDatePicker() {
        if (mSetCalendar == null) {
            mSetCalendar = Calendar.getInstance();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mSetCalendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                mSetCalendar.set(Calendar.MONTH, datePicker.getMonth());
                mSetCalendar.set(Calendar.YEAR, datePicker.getYear());
                mCurrentDate = mSetCalendar.getTime();
                setTimeUI();
                Log.d("Javine", "setDate" + mCurrentDate.toString());
            }
        });
        datePickerDialog.show();
    }
}
