package com.example.fyp_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.VerticalStepView;
import com.baoyachi.stepview.bean.StepBean;

import java.util.ArrayList;
import java.util.List;

public class TrackOrderActivity extends AppCompatActivity {
    VerticalStepView verticalStepView;
    com.example.fyp_project.databinding.ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        //binding = com.example.fyp_project.databinding.ActivityMainBinding
//        verticalStepView = findViewById(R.id.vertical_stepview);

        List<String> sources = new ArrayList<>();
//        StepBean stepBean0 = new StepBean("Order Received",1);
//        StepBean stepBean1 = new StepBean("Order Confirmed",1);
//        StepBean stepBean2 = new StepBean("Order in Progress",1);
//        StepBean stepBean3 = new StepBean("Order Finished",0);
//        StepBean stepBean4 = new StepBean("Order Shipped",-1);
        sources.add("Order Received");
        sources.add("Order Confirmed");
        sources.add("Order in Progress");
        sources.add("Order Finished");
        sources.add("Order Shipped");

//        verticalStepView
//                .setStepsViewIndicatorComplectingPosition(sources.size())
//                .reverseDraw(false)
//                .setStepViewTexts(sources)
//                .setTextSize(12)
//                .setLinePaddingProportion(0.85f)
//                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(TrackOrderActivity.this, android.R.color.holo_purple))
//                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(TrackOrderActivity.this, R.color.colorAccent))
//                .setStepViewComplectedTextColor(ContextCompat.getColor(TrackOrderActivity.this, android.R.color.primary_text_dark))
//                .setStepViewUnComplectedTextColor(ContextCompat.getColor(TrackOrderActivity.this, R.color.uncompleted_text_color))
//                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(TrackOrderActivity.this, R.drawable.complted))
//                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(TrackOrderActivity.this, R.drawable.default_icon))
//                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(TrackOrderActivity.this, R.drawable.attention));
//
    }
}