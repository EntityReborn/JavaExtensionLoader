package com.gmail.entityreborn.extensionloader.api;

import com.gmail.entityreborn.extensionloader.ExtensionClassLoader;
import com.gmail.entityreborn.extensionloader.ExtensionManager;

public abstract class BaseExtensionType {
    protected ExtensionManager manager;
    protected ExtensionClassLoader loader;
    
    public BaseExtensionType() {}
    
    public void instantiate(ExtensionManager managr, ExtensionClassLoader loadr) {
        this.manager = managr;
        this.loader = loadr;
    }
}
