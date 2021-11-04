package com.example.employeeapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Employee.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
abstract public class EmployeeDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "employee.db";
    private static EmployeeDatabase instance;
    public static synchronized EmployeeDatabase getInstance(Context context){
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), EmployeeDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
    public abstract EmployeeDAO employeeDAO();
}
