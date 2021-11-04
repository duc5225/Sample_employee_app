package com.example.employeeapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.employeeapp.R;
import com.example.employeeapp.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding loginBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        return loginBinding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginBinding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();

        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        loginBinding.checkBox.setChecked(sharedPref.getBoolean("isChecked", false));
        if (loginBinding.checkBox.isChecked()){
            loginBinding.signInUsernameEditText.setText(sharedPref.getString(getString(R.string.save_usn),""));
            loginBinding.signInPasswordEditText.setText(sharedPref.getString(getString(R.string.save_pwd),""));
        }
        else {
            editor.putString("username", "");
            editor.putString("password", "");
        }

        loginBinding.signUpBtn.setOnClickListener(v -> {
            SignupFragment signupFragment = new SignupFragment();
            fragmentTransaction.replace(R.id.container, signupFragment);
            fragmentTransaction.addToBackStack("key").commit();
        });

        loginBinding.signInBtn.setOnClickListener(v -> {
            hideKeyboardFrom(getActivity(),view);
            editor.putBoolean("isChecked", loginBinding.checkBox.isChecked());
            if (sharedPref.getString(loginBinding.signInUsernameEditText.getText().toString(),
                    "never gonna give you up, never gonna let you down")
                    .equals(loginBinding.signInPasswordEditText.getText().toString()))
            {
                if (loginBinding.checkBox.isChecked()){
                    editor.putString(getString(R.string.save_usn), loginBinding.signInUsernameEditText.getText().toString());
                    editor.putString(getString(R.string.save_pwd), loginBinding.signInPasswordEditText.getText().toString());
                }
                else{
                    editor.putString("username", "");
                    editor.putString("password", "");
                }
                editor.apply();
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.container, homeFragment);
                fragmentTransaction.addToBackStack("").commit();
            }
            else Toast.makeText(getActivity(), "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();

        });

    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}