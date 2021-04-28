package com.example.fyp_project.Paint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.fyp_project.LoginActivity;
import com.example.fyp_project.R;
import com.example.fyp_project.RegisterActivity;
import com.example.fyp_project.StartActivity;

public class StartDesignActivity extends AppCompatActivity {
    private ImageView logoImage;
    private LinearLayout linearLayout;
    private Button sketchPaintButton;
    private Button fillPaintButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_design);

        logoImage = findViewById(R.id.logo_image);
        linearLayout = findViewById(R.id.linear_layout);
        sketchPaintButton = findViewById(R.id.sketch_paint_button);
        fillPaintButton =findViewById(R.id.fill_paint_button);

        linearLayout.animate().alpha(0f).setDuration(1);

        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -1000);
        animation.setDuration(3000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());

        logoImage.setAnimation(animation);

        sketchPaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartDesignActivity.this, DesignSketchActivity.class);
                startActivity(intent);
            }
        });

        fillPaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartDesignActivity.this, DesignFillActivity.class);
                startActivity(intent);
            }
        });
    }

    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation){

        }

        @Override
        public void onAnimationEnd(Animation animation){
            logoImage.clearAnimation();
            logoImage.setVisibility(View.INVISIBLE);
            linearLayout.animate().alpha(1f).setDuration(2000);
        }

        @Override
        public void onAnimationRepeat(Animation animation){

        }
    }
}