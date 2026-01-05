package com.juaracoding.pcmspringboot29.util;

import com.juaracoding.pcmspringboot29.config.OtherConfig;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalFunction {

    public static void printConsole(Object o){
        if(OtherConfig.getEnablePrintConsole().equals("y")){
            System.out.println(o);
        }
    }

    public static Map<String,Object> convertClassToMap(Object object){
        Map<String,Object> map = new LinkedHashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();//Reflection
        for(Field field : fields){
            field.setAccessible(true);
            try{
                map.put(field.getName(),field.get(object));
            }catch(Exception e){

            }
        }
        return map;
    }
    /** tanggalLahir */
    public static String camelToStandard(String camel){
        StringBuilder sb = new StringBuilder();
        char c = camel.charAt(0);
        sb.append(Character.toUpperCase(c));
        for (int i = 1; i < camel.length(); i++) {
            char c1 = camel.charAt(i);
            if(Character.isUpperCase(c1)){
                sb.append(' ').append(c1);
            }
            else {
                sb.append(c1);
            }
        }
        return sb.toString();
    }
}
