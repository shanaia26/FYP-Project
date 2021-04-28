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
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.CustomerEnquiries.CustomerUploadEnquiryActivity;
import com.example.fyp_project.R;
import com.example.fyp_project.RegisterActivity;
import com.example.fyp_project.StartActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.OutputStream;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DesignSketchActivity extends AppCompatActivity {
    private PaintView paintView;
    private SeekBar seekBar;
    private LinearLayout canvasLayout;

    private FloatingActionButton fabStroke;
    private FloatingActionButton fabUndo;
    private FloatingActionButton fabPalette;
    private Button saveDesignButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_sketch);

        canvasLayout = findViewById(R.id.canvas_layout);
        fabStroke = findViewById(R.id.fab_stroke);
        fabUndo = findViewById(R.id.fab_undo);
        fabPalette = findViewById(R.id.fab_palette);
        seekBar = findViewById(R.id.seek_bar);
        saveDesignButton = findViewById(R.id.save_design_button);

        paintView = new PaintView(DesignSketchActivity.this, null);
        canvasLayout.addView(paintView, 0);
        paintView.requestFocus();

        fabPalette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog();
            }
        });

        fabUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.undo();
            }
        });

        fabStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seekBar.getVisibility() == View.VISIBLE)
                    seekBar.setVisibility(View.GONE);
                else
                    seekBar.setVisibility(View.VISIBLE);
            }
        });

        //Gets value of stroke width user picked
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                paintView.setStrokeWidth((int) progressChangedValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        saveDesignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting the bitmap from PaintView class
                Bitmap bmp = paintView.save();

                // opening a OutputStream to write into the file
                OutputStream imageOutStream = null;
                ContentValues content = new ContentValues();
                // name of the file
                content.put(MediaStore.Images.Media.DISPLAY_NAME, "painting.png");
                // type of the file
                content.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

                // location of the file to be saved
                content.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                // get the Uri of the file which is to be created in the storage
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content);
                try {
                    // open the output stream with the above uri
                    imageOutStream = getContentResolver().openOutputStream(uri);
                    // this method writes the files in storage
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);
                    // close the output stream after use
                    imageOutStream.close();
                    //Let user know their painting is saved in their gallery
                    Toast.makeText(DesignSketchActivity.this, "Painting saved in your gallery.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DesignSketchActivity.this, CustomerUploadEnquiryActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        fabPen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                paintView = new PaintView(DesignActivity.this, null);
//                canvasLayout.addView(paintView, 0);
//                paintView.requestFocus();
//            }
//        });
//
//        fabFill.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //paintView.setPaintStyle(Paint.Style.FILL_AND_STROKE);
////                paintViewFill = new PaintViewFill(DesignActivity.this, null);
////                canvasLayout.addView(paintViewFill, 0);
////                paintViewFill.requestFocus();
//            }
//        });
    }

    private void ColorPickerDialog() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, Common.COLOUR_SELECTED, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int colour) {
                Common.COLOUR_SELECTED = colour;
                paintView.setPathColor(Common.COLOUR_SELECTED);

            }
        });
        colorPicker.show();
    }

    public void onRadioButtonClicked(View view) {
        //Check if button is checked
        boolean checked = ((RadioButton) view).isChecked();
        //Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_normal:
                if (checked) {
                    paintView.normal();
                }
                break;
            case R.id.radio_emboss:
                if (checked) {
                    paintView.emboss();
                }
                break;
            case R.id.radio_blur:
                if (checked) {
                    paintView.blur();
                }
                break;
            case R.id.radio_clear:
                if (checked) {
                    paintView.clear();
                }
                break;
            default:
        }
    }

}