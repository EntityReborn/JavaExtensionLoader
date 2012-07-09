package com.gmail.entityreborn.extensionloader;

import java.io.File;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gmail.entityreborn.extensionloader.api.ExtensionAnnotation;
import com.gmail.entityreborn.extensionloader.api.BaseExtensionType;

@SuppressWarnings("rawtypes")
public abstract class ExtensionManager {
    protected List<Class> annotated;
    protected List<File> dirs;
    protected final Map<String, ExtensionClassLoader> loaders = new LinkedHashMap<String, ExtensionClassLoader>();
    protected List<BaseExtensionType> subclassed;
    
    public ExtensionManager() {
        this.subclassed = new ArrayList<BaseExtensionType>();
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
    
    public List<BaseExtensionType> getInterfaced() {
        return new ArrayList<BaseExtensionType>(subclassed);
    }
    
    @SuppressWarnings("unchecked")
    public List<BaseExtensionType> getInterfaced(Class iface) {
        List<BaseExtensionType> retn = new ArrayList<BaseExtensionType>();
        
        for (BaseExtensionType c : subclassed) {
            if (iface.isAssignableFrom(c.getClass())) {
                retn.add(c);
            }
        }
        
        return retn;
    }
    
    public ExtensionClassLoader getLoader(String klass) {
        return loaders.get(klass);
    }
    
    public boolean loadFor(Class type) {
        // Using BaseExtensionType to load extensions is
        // pointless. Also, we only want to load from types
        // that inherit BaseExtensionType.
        if ((type == BaseExtensionType.class) || (!type.isAnnotation()
                && !BaseExtensionType.class.isAssignableFrom(type))) {
            return false;
        }
        
        boolean retn = false;
        
        for (File dir : dirs) {
            if (!dir.isDirectory()) {
                continue;
            }
            
            File[] files = dir.listFiles();
            
            for (File file : files) {
                if (file.getName().endsWith(".jar") && file.isFile()) {
                    if (loadExtensionsInFile(file, type)) {
                        retn = true;
                    }
                }
            }
        }
        
        return retn;
    }
    
    protected abstract List<String> getClassPaths(File file);
    
    @SuppressWarnings("unchecked")
    protected boolean loadExtensionsInFile(File file, Class type) {
        ExtensionClassLoader loader;
        URL[] source;
        
        boolean retn = false;
        
        try {
            source = new URL[] { file.toURI().toURL() };
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return false;
        }
        
        loader = new ExtensionClassLoader(source, getClass().getClassLoader());
        
        for (String name : getClassPaths(file)) {
            Class<?> klass;
            try {
                klass = Class.forName(name, true, loader);
                
                if (klass == type) {
                    continue;
                }
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
                continue;
            }
            
            if (!loaders.containsKey(klass.toString())) {
                loaders.put(klass.getName(), loader);
            } else {
                // Already loaded!
                continue;
            }
            
            if (type.isAnnotation() && klass.isAnnotationPresent(type)) {
                annotated.add(klass);
                retn = true;
            } else if (type.isAssignableFrom(klass)) {
                BaseExtensionType inst;
                
                try {
                    Class<? extends BaseExtensionType> extension = klass
                            .asSubclass(type);
                    Constructor<? extends BaseExtensionType> constructor = extension
                            .getConstructor();
                    
                    inst = constructor.newInstance();
                    inst.instantiate(this, loader);
                    retn = true;
                    
                } catch (Throwable e) {
                    e.printStackTrace();
                    continue;
                }
                
                subclassed.add(inst);
            }
        }
        
        return retn;
    }
}
