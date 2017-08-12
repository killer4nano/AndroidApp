package com.example.kille.ostdeploy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by kille on 2017-07-22.
 */

public class MyJob extends AppCompatActivity {

    private Button sosButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.myjob);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int heright = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(heright *.6));
        setTitle("My Job");
        TextView jobName = (TextView) findViewById(R.id.textJobName);
        TextView jobDescription = (TextView) findViewById(R.id.textDescription);

        sosButton = (Button) findViewById(R.id.sosButton);
        if (Main2Activity.getServerCommunication().getCurrentTask().getSos()) {
            sosButton.setBackgroundColor(Color.parseColor("#FF0000"));
        }else {
            sosButton.setBackgroundColor(Color.parseColor("#ffffbb33"));
        }

        jobName.setText(ServerCommunication.getCurrentTaskName());
        jobDescription.setText(ServerCommunication.getCurrentTaskDescription());
    }


    public void sos (final View v) {
        if (Main2Activity.getServerCommunication().getCurrentTask().getSos()) {
            onBackPressed();
            v.setBackgroundColor(Color.parseColor("#ffffbb33"));
            Main2Activity.getServerCommunication().turnOffSos();
        }else {
            final EditText taskEditText = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("SOS")
                    .setMessage("What's the issue?")
                    .setView(taskEditText)
                    .setPositiveButton("SOS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                            String notes = "Notes:" + taskEditText.getText().toString();
                            Main2Activity.getServerCommunication().sosTask(notes);
                            v.setBackgroundColor(Color.parseColor("#FF0000"));
                        }
                    })
                    .setNegativeButton("CANCEL", null)
                    .create();
            dialog.show();

        }
    }

    public void finish(View v) {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Finish Task")
                .setMessage("Any notes before closing the job?")
                .setView(taskEditText)
                .setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String notes = taskEditText.getText().toString();
                        notes = "Notes:"+notes;
                        Main2Activity.getServerCommunication().finishJob(notes);
                        onBackPressed();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .create();
        dialog.show();

    }

    public void back(View v) {
        onBackPressed();
    }
}