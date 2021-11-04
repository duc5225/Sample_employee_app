package com.example.employeeapp.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmployeeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Employee employee);

    @Query("select * from employee order by name")
    List<Employee> getAllEmployee();

    @Delete
    void delete(Employee employee);

    @Query("select * from employee where id = :id")
    Employee getEmployee(int id);
}
