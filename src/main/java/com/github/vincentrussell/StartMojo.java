package com.github.vincentrussell;


import com.github.vincentrussell.qpid.EmbeddedBroker;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Goal which starts embedded amqp
 */
@Mojo(name="start", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class StartMojo extends AbstractEmbeddedQpidMojo {

    private static final String PACKAGE_NAME = StartMojo.class.getPackage().getName();
    public static final String CONTEXT_PROPERTY_NAME = PACKAGE_NAME + ".embeddedQpid";
    public static final String DEFAULT_CONFIG = "classpath:test-initial-config.json";

    /**
     * Location of the file.
     */
    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
    private File outputDirectory;

    @Parameter(property = "embeddedqpid.config", defaultValue = DEFAULT_CONFIG)
    private String initialConfigurationLocation = DEFAULT_CONFIG;

    @Parameter(property = "embeddedqpid.amqp-port", defaultValue = "5672")
    private int amqpPort = 5672;

    @Parameter(property = "embeddedqpid.http-port", defaultValue = "8080")
    private int httpPort = 8080;

    public void execute() throws MojoExecutionException {

        try {
            File outputDir = new File(outputDirectory, "broker-tempDirectory");
            FileUtils.deleteQuietly(outputDir);
            outputDir.mkdirs();
            EmbeddedBroker broker = new EmbeddedBroker(outputDir);
            broker.setConfig(initialConfigurationLocation);
            broker.setAmqpPort(amqpPort);
            broker.setHttpPort(httpPort);
            broker.start();
            project.setContextValue(CONTEXT_PROPERTY_NAME, broker);
        } catch (Exception e) {
            getLog().error(e.getMessage(), e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
