package com.gauravk.bubblebarsample.DB.ShowRoutine;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gauravk.bubblebarsample.R;


public class CustomViewHolder extends RecyclerView.ViewHolder {

    TextView Exercise_nameTextView;
    TextView Set_numTextView;
    TextView Repeat_numTextView;
    TextView Rest_timeTextView;
    ImageView crossButtonImageView;
    ImageView editButtonImageView;

    public CustomViewHolder(View itemView) {
        super(itemView);
        Exercise_nameTextView = itemView.findViewById(R.id.Exercise_name);
        Set_numTextView = itemView.findViewById(R.id.Set_num);
        Repeat_numTextView = itemView.findViewById(R.id.Repeat_num);
        Rest_timeTextView = itemView.findViewById(R.id.Rest_time);
        crossButtonImageView = itemView.findViewById(R.id.crossImageView);
        editButtonImageView = itemView.findViewById(R.id.editImageView);
    }

}