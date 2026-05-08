package com.profiles;

public class ApplicationProfile {

    //total execution time of all local-only phases
    Integer C_L;

    //total execution time of all offloadable phases when they are executed locally
    Integer C_O;

    // offloading-induced delay that contains the communication time (i.e. time to transmit offloadable components
    // to/from the remote server) and the execution time on the re- mote server
    Integer S_O;

    // data transmission time
    Integer S_T;

    // remote computation time
    Integer S_S;

    // total offloading-induced overheads if τi’s offloadable phases are offload
    Integer C_OH;

    // illustrates the recurring nature of the workload, often known as the task period
    Integer T;

    // SEA way
    public ApplicationProfile(Integer c_L, Integer c_O, Integer s_T, Integer s_S, Integer c_OH, Integer t) {
        C_L = c_L;
        C_O = c_O;
        S_T = s_T;
        S_S = s_S;
        S_O = S_T + S_S;
        C_OH = c_OH;
        T = t;
    }

    //RODA way
    public  ApplicationProfile(Integer c_1, Integer c_2, Integer c_3, Integer s_T2, Integer s_S2, Integer c_E, Integer c_D, Integer t) {
        C_L = c_1 + c_3;
        C_O = c_2;
        S_T = s_T2;
        S_S = s_S2;
        S_O = S_T + S_S;
        C_OH = c_E + c_D;
        T = t;
    }

    public Integer getC_L() {
        return C_L;
    }

    public Integer getC_O() {
        return C_O;
    }

    public Integer getS_O() {
        return S_O;
    }

    public Integer getC_OH() {
        return C_OH;
    }

    public Integer getS_T() {
        return S_T;
    }

    public Integer getS_S() {
        return S_S;
    }

    public Integer getT() {
        return T;
    }
}
