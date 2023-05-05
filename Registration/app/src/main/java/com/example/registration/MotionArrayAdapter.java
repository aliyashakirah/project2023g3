package com.example.registration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MotionArrayAdapter extends ArrayAdapter<UserHelperClass> {

    public MotionArrayAdapter(@NonNull Context context, List<UserHelperClass> motions) {
        super(context, 0, motions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.motion_list_item, parent, false);
        }

        UserHelperClass motion = getItem(position);

        TextView motionTextView = convertView.findViewById(R.id.motion_text_view);

        motionTextView.setText(motion.getMotion());

        return convertView;
    }
}
