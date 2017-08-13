package com.example.kille.ostdeploy;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private static ServerCommunication serverCommunication;
    ArrayList<String> name = new ArrayList();
    private String clientName;
    ListAdapter taskAdapter;
    ListView taskView;
    Main2Activity act;
    private TasksUpdater tasksUpdater;
    private boolean isVisible;


    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }


    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Tasks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        FirebaseMessaging.getInstance().subscribeToTopic("tasks");
        setContentView(R.layout.activity_main2);
        act = this;
        isVisible = true;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        taskAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, name) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                if (position < TasksUpdater.getSosTasks().size()) {
                    view.setBackgroundColor(Color.parseColor("#FF0000"));
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#000000"));
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                }
                return view;
            }
        };
        taskView = (ListView) findViewById(R.id.taskView);
        taskView.setAdapter(taskAdapter);

        tasksUpdater = new TasksUpdater(this);
        //tasksUpdater.start();

        clientName = "super";

        taskView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                if (!(name.get(0).contains("No new "))) {
                    AlertDialog.Builder descAlert = new AlertDialog.Builder(act);
                    descAlert.setCancelable(true);

                    if (TasksUpdater.getSosTasks().size() != 0 && (position < TasksUpdater.getSosTasks().size())) {
                        descAlert.setTitle("SOS:" + TasksUpdater.getSosTasks().get(position).getName());
                        descAlert.setMessage(TasksUpdater.getSosTasks().get(position).getDescription()+"\r\n\r\n"+TasksUpdater.getSosTasks().get(position).getNotes());
                    } else {
                        final Tasks task = tasksUpdater.getTasks().get(position - TasksUpdater.getSosTasks().size());
                        descAlert.setTitle(task.getTaskName());
                        descAlert.setMessage(task.getTaskDescription());


                        AlertDialog.Builder alertDialogbuilder = descAlert.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (!serverCommunication.isOnJob()) {
                                    serverCommunication.acceptTask(task);
                                } else {
                                    showMessage("You can't do more than one job!");
                                }
                            }
                        }).setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //This just closes the dialog box... No need to do anything here.
                            }
                        });
                    }
                    AlertDialog alertDialog = descAlert.create();
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                }
            }
        });
        findViewById(R.id.jobButton).setBackgroundColor(Color.parseColor("#03FF13"));
        serverCommunication = Transfer.getConnection();
        serverCommunication.setAct(this);
        if (serverCommunication.isOnJob()) {
            setButtonColor("ffffbb33");
            if (serverCommunication.getCurrentTask().getSos()) {
                setButtonColor("FF0000");
            }
        }
    }


    public boolean isVisible() {
        return isVisible;
    }

    public void showMyJob(View v) {
        if (serverCommunication.isOnJob()) {
            startActivity(new Intent(Main2Activity.this, MyJob.class));
        } else {
            showMessage("You are currently not doing anything.");

        }

    }
    public void test(View v) {

    }

    public void setName(String name) {
        clientName = name;
    }

    public String getName() {
        return clientName;
    }


    public void addToList() {
        name.clear();
        int sosTasksSize = TasksUpdater.getSosTasks().size();
        int size = 0;
        try {
            size = tasksUpdater.getTasks().size();
        } catch (Exception e) {
            Log.e("debug", e.getMessage());
        }
        if (sosTasksSize != 0) {
            for (int i = 0; i < sosTasksSize; i++) {
                name.add(TasksUpdater.getSosTasks().get(i).getName());
            }
        }
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                name.add(tasksUpdater.getTasks().get(i).getTaskName());
            }
        } else if (size == 0 && sosTasksSize == 0) {
            name.add(0, "No new tasks!");
        }
        updateTasks();
    }

    public static ServerCommunication getServerCommunication() {
        return serverCommunication;
    }

    public void updateTasks() {
        runOnUiThread(new Runnable() {
            public void run() {
                taskView.invalidateViews();
            }
        });
    }


    public void showMessage(String message) {
        Snackbar snack = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        snack.show();
    }

    public void setButtonColor(final String color) {
        runOnUiThread(new Runnable() {
            public void run() {
                Button myJobButton = (Button) findViewById(R.id.jobButton);
                myJobButton.setBackgroundColor(Color.parseColor("#" + color));
            }
        });

    }

    @TargetApi(16)
    public void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notif = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher2)
                .setContentTitle("New Tasks")
                .setAutoCancel(true)
                .setContentText("New tasks received!")
                .build();
        notif.contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Main2Activity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationManager.notify(0, notif);


    }
}