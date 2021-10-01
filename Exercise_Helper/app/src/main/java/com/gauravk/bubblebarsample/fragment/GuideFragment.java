package com.gauravk.bubblebarsample.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.gauravk.bubblebarsample.R;


public class GuideFragment extends Fragment {

    private Button button;

    public static GuideFragment newInstance() {
        GuideFragment fragment = new GuideFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        button = getView().findViewById(R.id.guide_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/Mediapipe-Fit/MediaFIT/blob/main/README.md"));
                startActivity(intent);
            }
        });
    }
}

