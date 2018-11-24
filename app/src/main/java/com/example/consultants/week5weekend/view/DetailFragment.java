package com.example.consultants.week5weekend.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.consultants.week5weekend.R;

//class to display a fragment for detailed contact info
public class DetailFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvNumber = view.findViewById(R.id.tvNumber);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvLocation = view.findViewById(R.id.tvLocation);
        //set textviews to info from bundle arguments
        tvName.setText("Name: " + getArguments().getString("name"));
        tvNumber.setText("Number: " + getArguments().getString("number"));
        tvEmail.setText("Email: " + getArguments().getString("email"));

        //makes the location clickable
        SpannableString ss = new SpannableString("Location: " + getArguments().getString("location"));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //when clicked, start MapsActivity and send location data to it with an intent
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("location", getArguments().getString("location"));
                startActivity(intent);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        //starts after 10th char, so word 'Location:' isn't clickable, but the location afterward is
        ss.setSpan(clickableSpan, 10, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.BLUE);
        ss.setSpan(fcs, 10, ss.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        tvLocation.setText(ss);
        tvLocation.setMovementMethod(LinkMovementMethod.getInstance());
        tvLocation.setHighlightColor(Color.TRANSPARENT);

        //ok button to dismiss detailed info fragment
        Button btnOk = view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }
}
