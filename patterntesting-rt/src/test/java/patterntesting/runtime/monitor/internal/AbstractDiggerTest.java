package patterntesting.runtime.monitor.internal;

import static org.mockito.Mockito.*;

import java.io.File;
import java.net.*;

/**
 * The Class AbstractDiggerTest provides some common constants and setup
 * methodes which are used by the different derived unit tests.
 */
public class AbstractDiggerTest {

    /** The Constant WORLD_WAR. */
    protected static final File WORLD_WAR = new File("src/test/resources/patterntesting/runtime/monitor/world.war");
    
    /** The Constant WORLD_EAR. */
    protected static final File WORLD_EAR = new File("src/test/resources/patterntesting/runtime/monitor/world.ear");

    /**
     * An {@link URLClassLoader} is mocked to return only one classpath which
     * is constructed by the arguments.
     *
     * @param jar the JAR part of the classpath
     * @param path e.g. "!/WEB-INF/classes"
     * @return an mocked {@link URLClassLoader}
     * @throws MalformedURLException the malformed URL exception
     */
    protected static URLClassLoader mockURLClassLoader(File jar, String path) throws MalformedURLException {
        URLClassLoader mockedClassLoader = mock(URLClassLoader.class);
        URL[] webclasspath = { new URL(jar.toURI() + path) };
        when(mockedClassLoader.getURLs()).thenReturn(webclasspath);
        return mockedClassLoader;
    }

}