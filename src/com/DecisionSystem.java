package com;

import com.algorithms.SEA_S_Aware;
import com.profiles.ApplicationProfile;
import com.profiles.EnergyProfile;

import java.util.ArrayList;

public class DecisionSystem {
    public static void main(String[] args) {
        ArrayList<Task> taskList = new ArrayList<>();
            taskList.add(new Task(1, new ApplicationProfile(1, 8, 0, 2, 5, 2, 1, 12)));
            taskList.add(new Task(2, new ApplicationProfile(0, 5, 1, 1, 1, 0, 0, 12)));
            taskList.add(new Task(3, new ApplicationProfile(1, 8, 0, 2, 4, 0, 0, 12)));
            taskList.add(new Task(4, new ApplicationProfile(1, 2, 0, 1, 1, 0, 0, 8)));
            taskList.add(new Task(5, new ApplicationProfile(1, 2, 0, 1, 1, 1, 0, 12)));
            taskList.add(new Task(6, new ApplicationProfile(0, 1, 1, 1, 1, 0, 0, 8)));

        SEA_S_Aware decisionMaker = SEA_S_Aware.getInstance();
        EnergyProfile energyProfile = new EnergyProfile(10, 12, 1);
//        decisionMaker.decideOffloadingFor(taskList, 2);
        decisionMaker.decideOffloadingFor(taskList, energyProfile, 2);

        for (Task task : taskList){
            System.out.println("id: " + task.getId() + ", O: " + task.getOffloadStatus());
        }


    }
}
