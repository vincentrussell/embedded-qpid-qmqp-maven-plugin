package com.github.vincentrussell.qpid;

import org.apache.qpid.server.SystemLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EmbeddedBroker implements Closeable
{

    final Logger logger = LoggerFactory.getLogger(EmbeddedBroker.class);


    private URL initialConfigurationLocation;
    private int amqpPort = 5762;
    private int httpPort = 8080;
    private final File tempDirectory;
    private final File workDirectory;
    private final File systemPropertiesFile;

    public EmbeddedBroker(File tempDirectory) {
        this.tempDirectory = tempDirectory;
        this.systemPropertiesFile = new File(tempDirectory, "system.properties");
        this.workDirectory = new File(tempDirectory, "working");
        this.workDirectory.mkdirs();
    }

    public EmbeddedBroker setConfig(String initialConfigurationLocation) throws MalformedURLException {
        this.initialConfigurationLocation = normalizeUrlPath(initialConfigurationLocation);
        return this;
    }

    public EmbeddedBroker setAmqpPort(int amqpPort) {
        this.amqpPort = amqpPort;
        return this;
    }

    public EmbeddedBroker setHttpPort(int httpPort) {
        this.httpPort = httpPort;
        return this;
    }

    private URL normalizeUrlPath(String initialConfigurationLocation) throws MalformedURLException {
        if (initialConfigurationLocation.indexOf("classpath:") == 0) {
            return this.getClass().getResource("/" + initialConfigurationLocation.substring(10));
        } else {
            File file = Paths.get(initialConfigurationLocation).toFile();
            if (file.exists()) {
                return file.toURI().toURL();
            }
        }
        return new URL(initialConfigurationLocation);
    }

    private SystemLauncher systemLauncher;

    public void start() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("qpid.amqp_port", amqpPort + "");
        properties.setProperty("qpid.http_port", httpPort + "");
        properties.setProperty("qpid.work_dir", workDirectory.getAbsolutePath());
        try (FileOutputStream fileOutputStream = new FileOutputStream(systemPropertiesFile)) {
            properties.store(fileOutputStream, null);
        }

        systemLauncher = new SystemLauncher();
        systemLauncher.startup(createSystemConfig());

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                try {
                    close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });

    }

    public void stop() throws Exception {
        systemLauncher.shutdown();
    }

    private Map<String, Object> createSystemConfig() throws MalformedURLException {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("type", "Memory");
        attributes.put("initialConfigurationLocation", initialConfigurationLocation.toExternalForm());
        if (systemPropertiesFile.exists()) {
            attributes.put("initialSystemPropertiesLocation", systemPropertiesFile.toURI().toURL().toExternalForm());
        }
        attributes.put("startupLoggedToSystemOut", true);
        return attributes;
    }

    @Override
    public void close() throws IOException {
        try {
            stop();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
