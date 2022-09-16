package org.example.agent.template;

import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.Set;

public class Premain {

    public static Set<String> PREFIX_WHITE_LIST = new HashSet<>();

    public static void premain(String options, Instrumentation ins) {
        System.out.println("******** Premain Start ********\n");
        LogUtils.agentInfo("******** Premain Start ********\n");
        parseArgs(options);
        ins.addTransformer(new TemplateTransformer());
    }

    public static void parseArgs(String args){
        if (args == null || args.equals(""))
            return;
        for (String argPair: args.split(";")){
            String[] kv = argPair.split("=");
            String key = kv[0];
            String value = kv[1];
            if (key.equals("instPrefix")){
                for (String prefix: value.split(",")){
                    PREFIX_WHITE_LIST.add(prefix);
                }
            }
        }
    }
}
