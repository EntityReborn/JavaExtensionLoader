package com.gmail.entityreborn.pluginloader.api;

public interface IExtension {
    public abstract String getName();
    public abstract void init() throws Exception;
}
