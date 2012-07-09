package com.gmail.entityreborn.extensionloader.example;

import com.gmail.entityreborn.extensionloader.api.ExtensionAnnotation;

@ExtensionAnnotation
public class ExtensionB {
    public static String getName() {
        return "Test2";
    }
    
    public static void init() throws Exception {
        System.out.println("Ohai2!");
    }
    
}
