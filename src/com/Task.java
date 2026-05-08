package com;

import com.profiles.ApplicationProfile;

import java.util.Comparator;

public class Task implements Comparator<Task> {

    public static final int UNOFFLOADED = 0;
    public static final int OFFLOADED = 1;
    public static final int UNDECIDED = 2;

    private Integer id;
    private ApplicationProfile applicationProfile;
    // whether on not this task should be offloaded
    private Integer offloadStatus;

    public Task(Integer id, ApplicationProfile applicationProfile) {
        this.id = id;
        this.offloadStatus = UNDECIDED;
        this.applicationProfile = applicationProfile;
    }

    public Task() {
    }

    public void setOffloadingDecision(Boolean offload){
        if(offload){
            this.offloadStatus = OFFLOADED;
        }
        else{
            this.offloadStatus = UNOFFLOADED;
        }
    }

    public ApplicationProfile getApplicationProfile() {
        return applicationProfile;
    }

    public Integer getId() {
        return id;
    }

    public Integer getOffloadStatus() {
        return offloadStatus;
    }

    @Override
    public int compare(Task o1, Task o2) {
        Float first = o1.getApplicationProfile().getS_O()/(float)o1.getApplicationProfile().getT();
        Float ssecond = o2.getApplicationProfile().getS_O()/(float)o2.getApplicationProfile().getT();
        return first.compareTo(ssecond);
    }
}
