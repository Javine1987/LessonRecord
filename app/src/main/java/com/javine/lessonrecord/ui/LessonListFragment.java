package com.javine.lessonrecord.ui;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.javine.lessonrecord.MainActivity;
import com.javine.lessonrecord.R;
import com.javine.lessonrecord.data.Lesson;
import com.javine.lessonrecord.viewmodel.LessonViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @文件描述 : 课程列表页面
 * @文件作者 : KuangYu
 * @创建时间 : 18-9-26
 */
public class LessonListFragment extends Fragment {

    private static final String TAG = "LessonListFragment";

    private View mView;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    private LessonViewModel mLessonViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (mView == null) {
            mView = inflater.inflate(R.layout.content_main, container, false);
            mRecyclerView = mView.findViewById(R.id.recycler_view);
            mAdapter = new MyAdapter();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true));
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLessonViewModel = MainActivity.obtainViewModel(getActivity());
        mLessonViewModel.getLoadLessonsEvent().observe(this, new Observer<List<Lesson>>() {
            @Override
            public void onChanged(@Nullable List<Lesson> lessons) {
                if (lessons != null) {
                    if (mAdapter != null) {
                        mAdapter.setData(lessons);
                    }
                } else {
                    showEmpty();
                }
            }
        });
        Log.d(TAG, "onActivityCreated");
    }

    private void showEmpty(){

    }

    @Override
    public void onResume() {
        super.onResume();
        mLessonViewModel.start();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        private List<Lesson> mData;

        public void setData(List<Lesson> data){
            mData = data;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.lesson_item_layout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.bindContent(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        // TODO: 18-9-30 点击事件

    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_place, tv_salary, tv_week, tv_duration, tv_date;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_place = itemView.findViewById(R.id.tv_place);
            tv_salary = itemView.findViewById(R.id.tv_salary);
            tv_week = itemView.findViewById(R.id.tv_week);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_date = itemView.findViewById(R.id.tv_date);
        }

        public void bindContent(Lesson lesson){
            tv_place.setText(lesson.getPlace());
            tv_salary.setText(lesson.getSalaryString());
            tv_duration.setText(lesson.getDurationString());
            tv_week.setText(lesson.getWeekString());
            tv_date.setText(lesson.getDateString());
        }
    }
}
