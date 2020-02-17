
# embedded-qpid-qmqp-maven-plugin

---

## Usage

```xml
<plugin>
  <groupId>com.github.vincentrussell</groupId>
  <artifactId>embedded-qpid-qmqp-maven-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
  <executions>
    <execution>
      <id>start</id>
      <goals>
        <goal>start</goal>
      </goals>
      <configuration>
        <embeddedqpid.amqp-port>5672</embeddedqpid.amqp-port>
        <embeddedqpid.http-port>8080</embeddedqpid.http-port>
        <embeddedqpid.config>file://pathToConfigFile</embeddedqpid.config>
      </configuration>
    </execution>
    <execution>
      <id>stop</id>
      <goals>
        <goal>stop</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```