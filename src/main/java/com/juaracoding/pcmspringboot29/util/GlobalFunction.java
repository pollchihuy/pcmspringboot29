package com.juaracoding.pcmspringboot29.util;

import com.juaracoding.pcmspringboot29.config.OtherConfig;

public class GlobalFunction {

    public static void printConsole(Object o){
        if(OtherConfig.getEnablePrintConsole().equals("y")){
            System.out.println(o);
        }
    }
}
