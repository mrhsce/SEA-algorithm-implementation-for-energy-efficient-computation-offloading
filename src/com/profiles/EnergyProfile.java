package com.profiles;

public class EnergyProfile {

    // energy consumption rate when the WIFI component is running
    Integer P_O;

    // energy consumption rate when the CPU component is computing
    Integer P_L;

    // energy consumption rate when both the WIFI component and the CPU component are idle
    Integer P_I;

    public EnergyProfile(Integer p_O, Integer p_L, Integer p_I) {
        P_O = p_O;
        P_L = p_L;
        P_I = p_I;
    }

    public Integer getP_O() {
        return P_O;
    }

    public Integer getP_L() {
        return P_L;
    }

    public Integer getP_I() {
        return P_I;
    }
}
