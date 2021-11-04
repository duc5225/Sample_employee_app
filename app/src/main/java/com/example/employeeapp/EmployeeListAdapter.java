package com.example.employeeapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeapp.database.Employee;
import com.example.employeeapp.database.EmployeeDatabase;
import com.example.employeeapp.databinding.ListTileBinding;
import com.example.employeeapp.fragments.AddFragment;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeListViewHolder> {

    private Context mContext;
    private List<Employee> employeeList;

    public void setData(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new EmployeeListViewHolder(ListTileBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        Drawable d = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_baseline_person_add_alt_1_24, mContext.getTheme());
        if (employee.getImage() == null) holder.binding.listItemAvatar.setImageDrawable(d);
        else holder.binding.listItemAvatar.setImageBitmap(employee.getImage());
        holder.binding.listItemName.setText(employee.getName());
        holder.binding.listItemRole.setText(employee.getRole());
        holder.binding.listItemOthers.setText(
                String.format("%s / %s tuổi / %S", employee.getGender(), employee.getAge(), employee.getPhone()));
        holder.binding.listItemDeleteButton.setOnClickListener(v -> {
            employeeList.remove(position);
            notifyItemRemoved(position);
            EmployeeDatabase.getInstance(mContext).employeeDAO().delete(employee);
        });
        holder.binding.listItemEditButton.setOnClickListener(v -> {
            Employee tempEmployee = employeeList.get(position);
            notifyItemChanged(position);
            AddFragment addFragment = new AddFragment(tempEmployee.getId());
            FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack("").replace(R.id.container, addFragment);
            fragmentTransaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return employeeList == null ? 0 : employeeList.size();
    }

    public static class EmployeeListViewHolder extends RecyclerView.ViewHolder {
        private final ListTileBinding binding;

        public EmployeeListViewHolder(ListTileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
