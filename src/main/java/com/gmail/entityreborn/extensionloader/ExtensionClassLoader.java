package com.gmail.entityreborn.extensionloader;

import java.net.URL;
import java.net.URLClassLoader;

public class ExtensionClassLoader extends URLClassLoader {
    public ExtensionClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}