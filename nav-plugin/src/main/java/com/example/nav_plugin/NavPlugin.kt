package com.example.nav_plugin;

import com.android.build.api.variant.AndroidComponentsExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class NavPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        System.out.println(">>>>>> this is " + this.getClass().getName());
        AndroidComponentsExtension androidComponentsExtensions = project.getExtensions().getByType(AndroidComponentsExtension.class);

    }
}
