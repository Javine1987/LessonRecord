package com.javine.lessonrecord.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * @文件描述 : 数据库字段类型转换
 * @文件作者 : KuangYu
 * @创建时间 : 18-9-26
 */
public class DataConverters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
