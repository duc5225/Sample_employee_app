package com.example.employeeapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.employeeapp.databinding.FragmentSingupBinding;

public class SignupFragment extends Fragment {
    private FragmentSingupBinding singupBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        singupBinding = FragmentSingupBinding.inflate(inflater, container, false);
        return singupBinding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singupBinding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        singupBinding.signUpBtn.setOnClickListener(v -> {
            lengthCheck();
            if (singupBinding.signInUsernameEditText.getText().length() >= 8
                    && singupBinding.signInPasswordEditText.getText().length() >= 8){
                editor.putString(singupBinding.signInUsernameEditText.getText().toString(),
                        singupBinding.signInPasswordEditText.getText().toString());
                editor.apply();
                fragmentManager.popBackStack();
                Toast.makeText(getActivity(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
    void lengthCheck(){
        if (singupBinding.signInUsernameEditText.length() == 0) {
            singupBinding.signInUsernameLayout.setError("Bạn bắt buộc phải nhập username");
        } else if (isBetween(singupBinding.signInUsernameEditText.length(), 1, 7)){
            singupBinding.signInUsernameLayout.setError("Không đủ ký tự");
        } else {
            singupBinding.signInUsernameLayout.setError(null);
        }
        if (singupBinding.signInPasswordEditText.length() == 0) {
            singupBinding.signInPasswordLayout.setError("Bạn bắt buộc phải nhập password");
        } else if (isBetween(singupBinding.signInPasswordEditText.length(), 1, 7)){
            singupBinding.signInPasswordLayout.setError("Password không đủ dài");
        } else {
            singupBinding.signInPasswordLayout.setError(null);
        }
    }
}