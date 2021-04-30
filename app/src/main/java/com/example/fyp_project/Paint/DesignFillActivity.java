package com.example.fyp_project.Paint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.CustomerEnquiries.CustomerUploadEnquiryActivity;
import com.example.fyp_project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.OutputStream;

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

        saveDesignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bmp = paintViewFill.save();

                OutputStream imageOutStream = null;
                ContentValues content = new ContentValues();
                content.put(MediaStore.Images.Media.DISPLAY_NAME, "painting.png");
                content.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

                content.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content);
                try {
                    imageOutStream = getContentResolver().openOutputStream(uri);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);
                    imageOutStream.close();

                    //Let user know their painting is saved in their gallery
                    Toast.makeText(DesignFillActivity.this, Common.SavedSuccessKey, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DesignFillActivity.this, CustomerUploadEnquiryActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
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