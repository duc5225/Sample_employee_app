package com.example.employeeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.employeeapp.database.Employee;
import com.example.employeeapp.databinding.GridTileBinding;
import com.example.employeeapp.databinding.ListTileBinding;
import com.example.employeeapp.fragments.AddFragment;

import java.util.List;

public class EmployeeGridAdapter extends RecyclerView.Adapter<EmployeeGridAdapter.EmployeeGridViewHolder> {

    private Context mContext;
    private List<Employee> employeeList;

    public void setData(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new EmployeeGridViewHolder(GridTileBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeGridViewHolder holder, int position) {
        holder.binding.getRoot().setOnClickListener(v -> {
            Employee tempEmployee = employeeList.get(position);
            notifyItemChanged(position);
            AddFragment addFragment = new AddFragment(tempEmployee.getId());
            FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack("").replace(R.id.container, addFragment);
            fragmentTransaction.commit();
        });
        Employee employee = employeeList.get(position);
        Glide.with(mContext)
                .load(employee.getImage())
                .fitCenter()
                .into(holder.binding.gridItemImage);
        holder.binding.gridItemName.setText(employee.getName());
        holder.binding.gridItemRole.setText(employee.getRole());
    }


    @Override
    public int getItemCount() {
        return employeeList == null ? 0 : employeeList.size();
    }

    public static class EmployeeGridViewHolder extends RecyclerView.ViewHolder {
        private final GridTileBinding binding;

        public EmployeeGridViewHolder(GridTileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
