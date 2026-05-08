package com.algorithms;

import javafx.concurrent.Task;

import java.util.ArrayList;

public class SEA_S_Oblivion{
    private static SEA_S_Oblivion ourInstance = new SEA_S_Oblivion();

    private SEA_S_Oblivion() {
    }

    public static SEA_S_Oblivion getInstance() {
        return ourInstance;
    }

    public ArrayList<Boolean> decideOffloadingFor(ArrayList<Task> tasks, Integer cpuCount) {
        return null;
    }
}
