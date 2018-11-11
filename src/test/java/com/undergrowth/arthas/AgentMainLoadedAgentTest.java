package com.undergrowth.arthas;


import java.lang.instrument.Instrumentation;

public class AgentMainLoadedAgentTest {
    @SuppressWarnings("rawtypes")
    public static void agentmain(String args, Instrumentation inst){
        Class[] classes = inst.getAllLoadedClasses();
        for(Class cls :classes){
            System.out.println(cls.getName());
        }
    }
}

