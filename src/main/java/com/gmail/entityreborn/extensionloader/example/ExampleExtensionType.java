package com.gmail.entityreborn.extensionloader.example;

import com.gmail.entityreborn.extensionloader.api.BaseExtensionType;

public abstract class ExampleExtensionType extends BaseExtensionType {
    public abstract String getName();
    public abstract void init() throws Exception;
}
