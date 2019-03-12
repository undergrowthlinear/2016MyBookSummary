package com.undergrowth.arthas.premain;


import java.lang.instrument.Instrumentation;

public class AgentPreMain {

    public static void premain(String args, Instrumentation inst) {
        System.out.println("Hi, I'm agent!");
        inst.addTransformer(new AgentPreMainTransformer());
    }
}

