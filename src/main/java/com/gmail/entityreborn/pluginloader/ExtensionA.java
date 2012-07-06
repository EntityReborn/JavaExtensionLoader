package com.gmail.entityreborn.pluginloader;

import com.gmail.entityreborn.pluginloader.api.IExtension;

public class ExtensionA implements IExtension {
    
    public String getName() {
        return "Test";
    }
    
    public void init() throws Exception {
        System.out.println("Ohai!");
    }
    
}
