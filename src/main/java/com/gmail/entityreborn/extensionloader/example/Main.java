package com.gmail.entityreborn.extensionloader.example;

import com.gmail.entityreborn.extensionloader.SimpleExtensionManager;
import com.gmail.entityreborn.extensionloader.api.BaseExtensionType;
import com.gmail.entityreborn.extensionloader.api.ExtensionAnnotation;

public class Main {
	public static void main(String[] arguments) throws Throwable {
		new Main();
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Main() throws Throwable {
		SimpleExtensionManager loader = new SimpleExtensionManager();
		loader.addDirectory("extensions");
		
		loader.loadFor(ExampleExtensionType.class);
		loader.loadFor(ExtensionAnnotation.class);

	    for (BaseExtensionType ext : loader.getInterfaced(ExampleExtensionType.class)) {
	        ExampleExtensionType extension = (ExampleExtensionType)ext;
	        
            System.out.println("Running " + extension.getName());
            
            try {
                extension.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	    
	    for (Class ext : loader.getAnnotated(ExtensionAnnotation.class)) {
	        try {
    	        System.out.println("Running " + ext.getMethod("getName").invoke(null));
                
                try {
                    ext.getMethod("init").invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
	        } catch (NoSuchMethodException e) {
	            System.out.println("An extention didn't provide the appropriate methods!");
	        }
        }
	}
}
