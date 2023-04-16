package com.example.bluetoothapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class FragmentOne extends Fragment {
    public FragmentOne() { }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    TextView text1, text2, text3, text4, text5, text6;
    private final Handler mHandler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fragment_one, container, false);
        text1 = root.findViewById(R.id.text1);
        text2 = root.findViewById(R.id.text2);
        text3 = root.findViewById(R.id.text3);
        text4 = root.findViewById(R.id.text4);
        text5 = root.findViewById(R.id.text5);
        text6 = root.findViewById(R.id.text6);
        mToastRunnable.run();
        return root;
    }

    private final Runnable mToastRunnable = new Runnable() {
        @Override
        public void run()
        {
            mHandler.postDelayed(this, 1000);
            SharedPreferences sh = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String str1 = sh.getString("s1", "0");
            String str2 = sh.getString("s2", "0");
            String str3 = sh.getString("s3", "0");
            String str4 = sh.getString("s4", "0");
            String str5 = sh.getString("s5", "0");
            String str6 = sh.getString("s6", "0");
            text1.setText(str1);
            text2.setText(str2);
            text3.setText(str3);
            text4.setText(str4);
            text5.setText(str5);
            text6.setText(str6);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mToastRunnable);
    }
}

