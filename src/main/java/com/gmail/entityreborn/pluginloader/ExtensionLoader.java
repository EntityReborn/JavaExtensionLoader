package com.gmail.entityreborn.pluginloader;

import java.io.File;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.gmail.entityreborn.pluginloader.api.IExtension;

@SuppressWarnings("rawtypes")
public class ExtensionLoader{
    private List<Class> annotated;
    private List<File> dirs;
    private List<IExtension> interfaced;
    
    public ExtensionLoader(){
        this.interfaced = new ArrayList<IExtension>();
        this.annotated = new ArrayList<Class>();
        this.dirs = new ArrayList<File>();
    }
    
    public boolean addDirectory(String dir) {
        if (!dirs.contains(dir)) {
            File d = new File(dir);
            
            if (d.isDirectory()) {
                dirs.add(d);
                return true;
            }
        }
        
        return false;
    }
    
    public List<Class> getAnnotated() {
        return new ArrayList<Class>(annotated);
    }
    
    @SuppressWarnings("unchecked")
    public List<Class> getAnnotated(Class type) {
        List<Class> retn = new ArrayList<Class>();
        
        for (Class c : annotated) {
            if (c.isAnnotationPresent(type)) {
                retn.add(c);
            }
        }
        
        return retn;
    }
    
    public List<IExtension> getInterfaced() {
        return new ArrayList<IExtension>(interfaced);
    }
    
    @SuppressWarnings("unchecked")
    public List<IExtension> getInterfaced(Class iface) {
        List<IExtension> retn = new ArrayList<IExtension>();
        
        for (IExtension c : interfaced) {
            if (iface.isAssignableFrom(c.getClass())) {
                retn.add(c);
            }
        }
        
        return retn;
    }
    
    public void loadFor(Class type) {
        for (File dir : dirs) {
            if (!dir.isDirectory()) {
                continue;
            }
            
            File[] files = dir.listFiles();
            
            for (File file : files) {
                if (file.getName().endsWith(".jar") && file.isFile()) {
                    loadExtensionsInFile(file, type);
                }
            }
        }
    }
    
    private List<String> getClassPaths(File file) {
        List<String> retn = new ArrayList<String>();
        ZipFile zip;
        
        try {
             zip = new ZipFile(file);
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
    
    @SuppressWarnings("unchecked")
    private void loadExtensionsInFile(File file, Class type){
        ClassLoader loader;
        
        try {
            loader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() });
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        
        for(String name : getClassPaths(file)) {
            Class<?> klass;
            
            try {
                klass = loader.loadClass(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                continue;
            }
            
            if (klass == type) {
                continue;
            }
            
            if(type.isAnnotation() && klass.isAnnotationPresent(type)) {
                annotated.add(klass);
            } else if(type.isAssignableFrom(klass)) {
                Object inst;
                
                try {
                    inst = type.cast(klass.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    continue;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
                
                interfaced.add((IExtension)inst);
            }
        }
    }
}
