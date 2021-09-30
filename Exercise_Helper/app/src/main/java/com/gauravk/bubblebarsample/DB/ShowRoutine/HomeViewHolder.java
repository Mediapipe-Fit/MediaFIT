package com.gauravk.bubblebarsample.DB.ShowRoutine;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gauravk.bubblebarsample.R;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    TextView Exercise_nameTextView;
    TextView Set_numTextView;
    TextView Repeat_numTextView;
    TextView Rest_timeTextView;

    public HomeViewHolder(View itemView) {
        super(itemView);
        Exercise_nameTextView = itemView.findViewById(R.id.Exercise_nameTextView);
        Set_numTextView = itemView.findViewById(R.id.Set_numTextView);
        Repeat_numTextView = itemView.findViewById(R.id.Repeat_numTextView);
        Rest_timeTextView = itemView.findViewById(R.id.Rest_timeTextView);
    }
}
