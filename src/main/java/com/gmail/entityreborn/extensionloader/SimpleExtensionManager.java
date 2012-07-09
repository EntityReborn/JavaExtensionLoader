package com.gmail.entityreborn.extensionloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class SimpleExtensionManager extends ExtensionManager {
    protected List<String> getClassPaths(File file) {
        List<String> retn = new ArrayList<String>();
        JarFile zip;
        
        try {
             zip = new JarFile(file);
        } catch (ZipException e1) {
            e1.printStackTrace();
            return retn;
        } catch (IOException e1) {
            e1.printStackTrace();
            return retn;
        }
        
        Enumeration<? extends ZipEntry> entries = zip.entries();
        
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            
            if(!name.matches(".*\\$(?:\\d)*\\.class") && name.endsWith(".class")){
                name = name.replaceAll("\\.class", "").replaceAll("\\\\", "/").replaceAll("[/]", ".");
                retn.add(name);
            }
        }
        
        return retn;
    }
}
