package com.example.employeeapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.employeeapp.R;
import com.example.employeeapp.database.Employee;
import com.example.employeeapp.database.EmployeeDatabase;
import com.example.employeeapp.databinding.FragmentAddBinding;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;

public class AddFragment extends Fragment implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "noice";
    private final Employee employee = new Employee();
    private FragmentAddBinding binding;
    private final Integer ID;
    private ArrayAdapter<CharSequence> arrayAdapter;

    public AddFragment(Integer id) {
        this.ID = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        if (this.ID != null) {
            getActivity().getMenuInflater().inflate(R.menu.add_bar, menu);
            menu.findItem(R.id.add_bar_remove).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Employee temp = EmployeeDatabase.getInstance(getActivity()).employeeDAO().getEmployee(ID);
                    EmployeeDatabase.getInstance(getActivity()).employeeDAO().delete(temp);
                    requireActivity().getSupportFragmentManager().popBackStack();
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.roles, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.addRoleSpinner.setAdapter(arrayAdapter);
        binding.addRoleSpinner.setOnItemSelectedListener(this);

        if (this.ID != null) {
            autoFill();
            actionBar.setTitle("Sửa nhân viên");
            binding.addBtn.setText("Sửa");
        } else actionBar.setTitle("Thêm nhân viên");
        binding.addBirthdayEditText.setEnabled(false);
        binding.addBirthdayLayout.setEndIconOnClickListener(v -> {
            showDatePickerDialog();
        });

        binding.addImageButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        2000);
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("Chọn ảnh").setMessage("Bạn muốn chụp ảnh hay chọn ảnh từ bộ nhớ");
            builder.setPositiveButton("Chụp ảnh", (dialog, which) -> {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, 500);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Something wrong", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Chọn ảnh", (dialog, id) -> {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1000);
            });
            builder.setNeutralButton("Quay lại", (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        });

        binding.addBtn.setOnClickListener(v ->

        {
            if (this.ID != null) {
                employee.setId(this.ID);
            }
            employee.setName(binding.addNameEditText.getText().toString());
            employee.setPhone(binding.addPhoneEditText.getText().toString());
            Boolean isMale = null;
            int selectedRadioButtonId = binding.radioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonId == binding.radiobuttonMale.getId()) {
                isMale = true;
            } else if (selectedRadioButtonId == binding.radiobuttonFemale.getId()) {
                isMale = false;
            }
            employee.setMale(isMale);
            employee.setRole(binding.addRoleSpinner.getSelectedItem().toString());
            if (dataCheck(isMale)) {
                EmployeeDatabase.getInstance(getActivity()).employeeDAO().insert(employee);
                requireActivity().getSupportFragmentManager().popBackStack();
            } else
                Toast.makeText(getActivity(), "Làm ơn hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        });
    }

    private void autoFill() {
        Employee tempEmployee = EmployeeDatabase.getInstance(getActivity()).employeeDAO().getEmployee(this.ID);
        if (tempEmployee.getImage() != null) {
            Glide.with(this)
                    .load(tempEmployee.getImage())
                    .fitCenter()
                    .into(binding.addImageButton);
        }
        binding.addNameEditText.setText(tempEmployee.getName());
        binding.addPhoneEditText.setText(tempEmployee.getPhone());
        binding.addBirthdayEditText.setText(tempEmployee.birthdayToString());
        employee.setBirthday(tempEmployee.getBirthday());
        employee.setImage(tempEmployee.getImage());
        binding.addRoleSpinner.setSelection(arrayAdapter.getPosition(tempEmployee.getRole()));

        if (tempEmployee.getMale()) binding.radioGroup.check(binding.radiobuttonMale.getId());
        else binding.radioGroup.check(binding.radiobuttonFemale.getId());
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        Log.d("noice", "showDatePickerDialog: " + Calendar.getInstance().get(Calendar.YEAR));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dayOfMonth).append("/").append(month + 1).append("/").append(year);
        binding.addBirthdayEditText.setText(stringBuilder);
        employee.setBirthday(LocalDate.of(year, month, dayOfMonth));
    }

    private Boolean dataCheck(Boolean isMale) {

        if (binding.addNameEditText.length() == 0
                || binding.addPhoneEditText.length() == 0
                || binding.addBirthdayEditText.length() == 0
                || isMale == null) {
            if (binding.addNameEditText.length() == 0)
                binding.addNameEditText.setError("Bạn bắt buộc phải nhập name");
            if (binding.addPhoneEditText.length() == 0)
                binding.addPhoneEditText.setError("Bạn bắt buộc phải nhập số điện thoại");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Glide.with(this)
                    .load(imageBitmap)
                    .fitCenter()
                    .into(binding.addImageButton);
            employee.setImage(imageBitmap);
        }
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            Glide.with(this)
                    .load(data.getData())
                    .fitCenter()
                    .into(binding.addImageButton);
            try {
                employee.setImage(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        employee.setRole(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}