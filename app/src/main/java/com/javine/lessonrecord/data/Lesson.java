package com.javine.lessonrecord.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @文件描述 : 课程实体类
 * @文件作者 : KuangYu
 * @创建时间 : 18-9-25
 */
@Entity(tableName = "lessons")
public final class Lesson {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private String id;

    @ColumnInfo(name = "place")
    private String place;

    @ColumnInfo(name = "duration")
    private int duration;

    @ColumnInfo(name = "salary")
    private int salary;

    @ColumnInfo(name = "date")
    private Date date;       //  > 1900

    @Ignore
    private String salaryString;
    @Ignore
    private String weekString;
    @Ignore
    private String dateString;
    @Ignore
    private String durationString;

    public Lesson(){
        id = UUID.randomUUID().toString();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        durationString = null;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
        salaryString = null;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        weekString = null;
        dateString = null;
    }

    public String getSalaryString(){
        if (salaryString == null) {
            salaryString = "¥" + salary;
        }
        return salaryString;
    }

    public String getDurationString() {
        if (durationString == null) {
            durationString = duration + "分钟";
        }
        return durationString;
    }

    public String getWeekString() {
        if (weekString == null) {
            weekString = new SimpleDateFormat("EEEE").format(date);
        }
        return weekString;
    }

    public String getDateString() {
        if (dateString == null) {
            dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        }
        return dateString;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id='" + id + '\'' +
                ", place='" + place + '\'' +
                ", duration=" + duration +
                ", salary=" + salary +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return duration == lesson.duration &&
                date == lesson.date &&
                Objects.equals(id, lesson.id) &&
                Objects.equals(place, lesson.place);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, place, duration, date);
    }

    public boolean isEmpty(){
        return Strings.isNullOrEmpty(place) || date == null || duration == 0 || salary == 0;
    }
}
