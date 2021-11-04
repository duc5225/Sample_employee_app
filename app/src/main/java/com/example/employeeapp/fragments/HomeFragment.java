package com.example.employeeapp.fragments;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employeeapp.EmployeeGridAdapter;
import com.example.employeeapp.EmployeeListAdapter;
import com.example.employeeapp.database.Employee;
import com.example.employeeapp.database.EmployeeDatabase;
import com.example.employeeapp.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {

    List<Employee> employees;
    private FragmentHomeBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Quản lý nhân viên");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);

        layoutDecider(sharedPreferences.getBoolean("isGrid", false));
        binding.gridButton.setOnClickListener(v -> {
            layoutDecider(true);
        });
        binding.listButton.setOnClickListener(v -> {
            layoutDecider(false);
        });
    }
    private void layoutDecider(Boolean isGrid){
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        employees = EmployeeDatabase.getInstance(getActivity()).employeeDAO().getAllEmployee();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (isGrid){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            EmployeeGridAdapter employeeGridAdapter = new EmployeeGridAdapter();
            employeeGridAdapter.setData(employees);
            editor.putBoolean("isGrid", true);
            binding.homeRecyclerView.setAdapter(employeeGridAdapter);
            binding.homeRecyclerView.setLayoutManager(gridLayoutManager);
            binding.gridButton.setAlpha(1.0F);
            binding.listButton.setAlpha(0.5F);
        }
        else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
            EmployeeListAdapter employeeListAdapter = new EmployeeListAdapter();
            employeeListAdapter.setData(employees);
            editor.putBoolean("isGrid", false);
            binding.homeRecyclerView.setAdapter(employeeListAdapter);
            binding.homeRecyclerView.setLayoutManager(linearLayoutManager);
            binding.listButton.setAlpha(1.0F);
            binding.gridButton.setAlpha(0.5F);
        }
        editor.apply();
    }
}