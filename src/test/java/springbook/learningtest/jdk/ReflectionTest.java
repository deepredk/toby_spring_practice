package springbook.learningtest.jdk;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;

public class ReflectionTest {
    @Test
    public void invokeMethod() throws Exception {
        String name = "Spring";
        assertEquals(name.length(), 6);

        // length()
        Method lengthMethod = String.class.getDeclaredMethod("length");
        assertEquals((int) lengthMethod.invoke(name), 6);

        // charAt()
        assertEquals(name.charAt(0), 'S');
        
        Method charAtMethod = String.class.getDeclaredMethod("charAt", int.class);
        assertEquals((char) charAtMethod.invoke(name, 0), 'S');
    }
}
