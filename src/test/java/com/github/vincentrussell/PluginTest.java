package com.github.vincentrussell;


import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Rule;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.util.SocketUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;

public class PluginTest
{
    @Rule
    public MojoRule rule = new MojoRule()
    {
        private MavenProject mavenProject = null;

        @Override
        public MavenProject readMavenProject(File basedir ) throws Exception {
            if (mavenProject == null) {
                mavenProject = super.readMavenProject(basedir);
                return mavenProject;
            }
            return mavenProject;
        }

        @Override
        protected void before() throws Throwable 
        {
        }

        @Override
        protected void after()
        {
        }
    };

    /**
     * @throws Exception if any
     */
    @Test
    public void startAndStop()
            throws Exception
    {
        File pom = new File( "target/test-classes/project-to-test/" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        int amqpPort = SocketUtils.findAvailableTcpPort();
        int httpPort = SocketUtils.findAvailableTcpPort();

        StartMojo startMojo = (StartMojo) rule.lookupConfiguredMojo( pom, "start" );
        setField(startMojo, "amqpPort", amqpPort);
        setField(startMojo, "httpPort", httpPort);
        assertNotNull(startMojo);
        startMojo.execute();

        StopMojo stopMojo = (StopMojo) rule.lookupConfiguredMojo( pom, "stop" );
        assertNotNull(stopMojo);
        stopMojo.execute();


    }

    private void setField(Object target, String fieldNmae, Object value) throws IllegalAccessException {
        Field field = ReflectionUtils.getFieldByNameIncludingSuperclasses(fieldNmae, target.getClass());
        field.setAccessible(true);
        field.set(target, value);
    }


}

