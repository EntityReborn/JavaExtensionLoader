package com.gmail.entityreborn.pluginloader;

import com.gmail.entityreborn.pluginloader.ExtensionLoader;
import com.gmail.entityreborn.pluginloader.api.Extension;
import com.gmail.entityreborn.pluginloader.api.IExtension;

public class Main {
	public static void main(String[] arguments) throws Throwable {
		new Main();
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Main() throws Throwable {
		ExtensionLoader loader = new ExtensionLoader();
		loader.addDirectory("extensions");
		
		loader.loadFor(IExtension.class);
		loader.loadFor(Extension.class);

	    for (IExtension ext : loader.getInterfaced(IExtension.class)) {
            System.out.println("Running " + ext.getName());
            
            try {
                ext.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	    
	    for (Class ext : loader.getAnnotated(Extension.class)) {
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
