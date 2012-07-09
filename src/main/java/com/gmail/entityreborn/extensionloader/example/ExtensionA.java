package com.gmail.entityreborn.extensionloader.example;

public class ExtensionA extends ExampleExtensionType {
    public String getName() {
        return "Test";
    }
    
    public void init() throws Exception {
        System.out.println("Ohai!");
    }
}
