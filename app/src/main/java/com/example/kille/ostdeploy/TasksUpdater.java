package com.example.kille.ostdeploy;

import java.util.ArrayList;

/**
 * Created by kille on 2017-07-17.
 */

public class TasksUpdater {

    private Main2Activity act;

    private static ArrayList<Tasks> listOfTasks = new ArrayList();
    private static ArrayList<SosTasks> listOfSosTasks = new ArrayList();

    public TasksUpdater(Main2Activity act) {
        this.act = act;
    }


    public static void setTasks(ArrayList<Tasks> tasks) {
        listOfTasks = tasks;
    }
    public static void setSosTasks(ArrayList<SosTasks> tasks) {
        listOfSosTasks = tasks;
    }

    public static ArrayList<Tasks> getTasks() {
        return listOfTasks;
    }
    public static ArrayList<SosTasks> getSosTasks() {
        return listOfSosTasks;
    }


}
