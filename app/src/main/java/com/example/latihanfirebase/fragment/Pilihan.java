package com.example.latihanfirebase.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.latihanfirebase.R;

public class Pilihan extends DialogFragment implements View.OnClickListener {

    private OnOptionDialogListener onOptionDialogListener;

    public interface OnOptionDialogListener {
        void onOptionChoosen(String text);
    }

    public Pilihan() {

    }

    public OnOptionDialogListener getOnOptionDialogListener() {
        return onOptionDialogListener;
    }

    public void setOnOptionDialogListener(OnOptionDialogListener onOptionDialogListener) {
        this.onOptionDialogListener = onOptionDialogListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pilihan, container, false);
        view.findViewById(R.id.teks_saja).setOnClickListener(this);
        view.findViewById(R.id.dengan_foto).setOnClickListener(this);
        view.findViewById(R.id.pilihan_batal).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        String pilihan = "kosong";
        switch (v.getId()) {
            case R.id.batal:
                dismiss();
                break;
            case R.id.teks_saja:
                pilihan = "teks";
                break;
            case R.id.dengan_foto:
                pilihan = "foto";
                break;
        }
        getOnOptionDialogListener().onOptionChoosen(pilihan);
        dismiss();
    }
}
