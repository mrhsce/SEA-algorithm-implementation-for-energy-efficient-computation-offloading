package com.algorithms;


import com.Task;
import com.profiles.EnergyProfile;

import java.util.ArrayList;
import java.util.Collections;

public class SEA_S_Aware {
    private static SEA_S_Aware ourInstance = new SEA_S_Aware();

    private SEA_S_Aware() {

    }

    public static SEA_S_Aware getInstance() {
        return ourInstance;
    }

    public void decideOffloadingFor(ArrayList<Task> tasks, EnergyProfile energyProfile, Integer cpuCount) {
        decideOffloadingFor(tasks, cpuCount);

        ArrayList<Task> potentialOffloadedTasks = new ArrayList<>();
        ArrayList<Task> rest = new ArrayList<>();
        ArrayList<Task> unoffloadedTasks = new ArrayList<>();

        // separate offloaded Tasks
        for (Task task : tasks) {
            if (task.getOffloadStatus() == Task.OFFLOADED) {
                rest.add(task);
                potentialOffloadedTasks.add(task);
            }
            else{
                unoffloadedTasks.add(task);
            }
        }

        // Sort by largest ratio
        Collections.sort(potentialOffloadedTasks, Collections.reverseOrder(new Task()));

        //Improve energy consumption preserving response time
        Float totalLocalProcess = calculateTotalLocalProcess(tasks);
        for(Task task: potentialOffloadedTasks){
            if(checkEnergy(task, energyProfile)){
                task.setOffloadingDecision(false);
                rest.remove(task);
                unoffloadedTasks.add(task);
                if(!checkForInequality(rest, unoffloadedTasks, totalLocalProcess, cpuCount)){
                    task.setOffloadingDecision(true);
                    rest.add(task);
                    unoffloadedTasks.remove(task);
                }
            }
        }
    }

    public Boolean checkEnergy(Task task, EnergyProfile energyProfile){

        Integer totalEnergy = energyProfile.getP_O()*task.getApplicationProfile().getS_T() +
                energyProfile.getP_I()*task.getApplicationProfile().getS_S()+ energyProfile.getP_L()*
                (task.getApplicationProfile().getC_OH()-task.getApplicationProfile().getC_O());
        return totalEnergy >= 0;
    }

    public void decideOffloadingFor(ArrayList<Task> allTasks, Integer cpuCount) {
        ArrayList<Task> unoffloadedTasks = new ArrayList<>();
        ArrayList<Task> rest = new ArrayList<>();

        filterVeryHighOverhead(allTasks);
        // separateUnoffloadedTasks
        for (Task task : allTasks) {
            if (task.getOffloadStatus() == Task.UNOFFLOADED) {
                unoffloadedTasks.add(task);
            } else {
                rest.add(task);
            }
        }

        Collections.sort(rest, Collections.reverseOrder(new Task()));

        Float totalLocalProcess = calculateTotalLocalProcess(allTasks);
        while(rest.size()>0 && !checkForInequality(rest, unoffloadedTasks, totalLocalProcess, cpuCount)){
            rest.get(0).setOffloadingDecision(false);
            unoffloadedTasks.add(rest.get(0));
            rest.remove(0);
        }
        for(Task task : rest){
            task.setOffloadingDecision(true);
        }
    }


    private void filterVeryHighOverhead(ArrayList<Task> tasks) {
        //These tasks obviously should not be selected for offloading because the additional overhead incurred at the
        // local side is already longer than the execution time if executed locally

        for (Task task : tasks) {
            if (task.getApplicationProfile().getC_OH() >= task.getApplicationProfile().getC_O()) {
                task.setOffloadingDecision(false);
            }
        }
    }

    private Boolean checkForInequality(ArrayList<Task> rest, ArrayList<Task> unoffloadedTasks, Float totalLocalProcess, Integer cpu) {
        Float total = totalLocalProcess + calculateMlargestSuspensionRation(rest, cpu) +
                calculateLocalOffloadableProcess(unoffloadedTasks) + calculateTotalOverhead(rest);
        return total <= cpu;
    }

    private float calculateMlargestSuspensionRation(ArrayList<Task> rest, Integer cpu){
        int n = 0;
        Float total = 0f;
        while(n<rest.size() && n < cpu){
            total += rest.get(n).getApplicationProfile().getS_O()/(float)rest.get(n).getApplicationProfile().getT();
            n++;
        }
        return total;
    }

    private float calculateTotalLocalProcess(ArrayList<Task> tasks){
        float total = 0f;
        for(Task task: tasks){
            total += task.getApplicationProfile().getC_L()/(float)task.getApplicationProfile().getT();
        }
        return total;
    }

    private float calculateLocalOffloadableProcess(ArrayList<Task> unoffloadedTasks){
        Float total = 0f;
        for(Task task: unoffloadedTasks){
            total += task.getApplicationProfile().getC_O()/(float)task.getApplicationProfile().getT();
        }
        return total;
    }

    private float calculateTotalOverhead(ArrayList<Task> rest){
        Float total = 0f;
        for(Task task: rest){
            total += task.getApplicationProfile().getC_OH()/(float)task.getApplicationProfile().getT();
        }
        return total;
    }

}
