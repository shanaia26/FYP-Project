package com.example.fyp_project.Paint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DesignFillActivity extends AppCompatActivity {
    private PaintViewFill paintViewFill;
    private LinearLayout canvasLayout;

    private FloatingActionButton fabClear;
    private FloatingActionButton fabUndo;
    private FloatingActionButton fabPalette;
    private Button saveDesignButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_fill);

        canvasLayout = findViewById(R.id.canvas_layout);
        fabClear = findViewById(R.id.fab_clear);
        fabUndo = findViewById(R.id.fab_undo);
        fabPalette = findViewById(R.id.fab_palette);
        saveDesignButton = findViewById(R.id.save_design_button);

        paintViewFill = new PaintViewFill(DesignFillActivity.this, null);
        canvasLayout.addView(paintViewFill, 0);
        paintViewFill.requestFocus();

        fabPalette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog();
            }
        });

        fabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintViewFill.clear();
            }
        });

        fabUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintViewFill.undo();
            }
        });
    }

    private void ColorPickerDialog() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, Common.COLOUR_SELECTED, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int colour) {
                Common.COLOUR_SELECTED = colour;
                paintViewFill.setPathColor(Common.COLOUR_SELECTED);

            }
        });
        colorPicker.show();
    }
}