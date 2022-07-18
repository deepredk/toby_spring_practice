package springbook.learningtest.template;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {

    Calculator calculator;
    String numFilePath;
    
    @Before
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilePath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {
        assertEquals(Integer.valueOf(10), calculator.calcSum(numFilePath));
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        assertEquals(Integer.valueOf(24), calculator.calcMultiply(numFilePath));
    }

    @Test
    public void concatenateStrings() throws IOException {
        assertEquals("1234", calculator.concatenate(numFilePath));
    }

}
