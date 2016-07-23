package ua.com.myCalculator;

/**
 * Created by andrii.kazhurin on 13.07.2016.
 */
public class FailedExpression extends Exception {
    public FailedExpression(String message) {
        super(message);
    }
}
