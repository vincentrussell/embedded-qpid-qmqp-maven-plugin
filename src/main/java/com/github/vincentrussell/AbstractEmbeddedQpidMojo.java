package com.github.vincentrussell;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractEmbeddedQpidMojo extends AbstractMojo {

    @Parameter( defaultValue = "${project}", readonly = true )
    protected MavenProject project;

    @Parameter( defaultValue = "${session}", readonly = true )
    protected MavenSession session;

}
