package com.gmail.entityreborn.pluginloader;

import com.gmail.entityreborn.pluginloader.api.Extension;

@Extension
public class ExtensionB {
    public static String getName() {
        return "Test2";
    }
    
    public static void init() throws Exception {
        System.out.println("Ohai2!");
    }
    
}
