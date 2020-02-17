package com.github.vincentrussell;


import com.github.vincentrussell.qpid.EmbeddedBroker;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

import static com.github.vincentrussell.StartMojo.CONTEXT_PROPERTY_NAME;

/**
 * Goal which starts embedded amqp
 */
@Mojo(name="stop", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class StopMojo extends AbstractEmbeddedQpidMojo {

    /**
     * Location of the file.
     */
    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
    private File outputDirectory;

    public void execute() throws MojoExecutionException, MojoFailureException {
        EmbeddedBroker broker  = (EmbeddedBroker) project.getContextValue(CONTEXT_PROPERTY_NAME);

        if (broker != null) {
            try {
                broker.stop();
            } catch (Exception e) {
                getLog().error(e.getMessage(), e);
                throw new MojoExecutionException(e.getMessage(), e);
            }
        } else {
            throw new MojoFailureException("No embeddedbroker process found, it appears embedmongo:start was not called");
        }
    }
}
