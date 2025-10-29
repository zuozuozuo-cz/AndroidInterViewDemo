package com.example.nav_plugin;

import com.android.build.api.instrumentation.AsmClassVisitorFactory;
import com.android.build.api.instrumentation.ClassContext;
import com.android.build.api.instrumentation.ClassData;
import com.android.build.api.instrumentation.InstrumentationContext;
import com.android.build.api.instrumentation.InstrumentationParameters;

import org.gradle.api.provider.Property;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassVisitor;

import java.io.PrintWriter;

import groovyjarjarasm.asm.util.TraceClassVisitor;

public abstract class FooClassVisitorFactory implements AsmClassVisitorFactory<InstrumentationParameters.None> {

}
