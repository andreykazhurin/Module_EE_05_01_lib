package ua.com.myCalculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by andrii.kazhurin on 13.07.2016.
 */
public class SimpleCalc implements Calculator {

    private HashMap<String, String> supportOperations;

    public SimpleCalc() {
        setSupportOperations();
    }

    public HashMap<String, String> getSupportOperations() {
        return supportOperations;
    }

    public int getPriority(String token) {
        if (token.equals("(")) return 1;
        if (token.equals("+") || token.equals("-")) return 2;
        return 3;
    }


    public void setSupportOperations() {
        supportOperations = new HashMap<String, String>();
        supportOperations.put("+", "add");

        supportOperations.put("-", "sub");
    }

    public Double add(Double a, Double b){
        return a + b;
    }

    public Double sub(Double a, Double b){
        return a - b;
    }


    public String calculate(String expression) throws FailedExpression {
        ExpressionParser parser = new ExpressionParser(this);
        parser.parse(expression);
        return expression + " = " + calcRPN(parser.parse(expression));
    }

    public String getStringSupportOperations(){
        StringBuilder result = new StringBuilder();
        for(String next : supportOperations.keySet()){
            result.append(next);
        }
        return result.toString();
    }

    public Double calcRPN(List<String> postfix) {
        LinkedList<Double> stack = new LinkedList<Double>();

        String operators = getStringSupportOperations();

        for (String x : postfix) {

            if(ExpressionParser.isOperator(x, operators)){
                String methodName = supportOperations.get(x);
                Double b = stack.removeFirst(), a = stack.removeFirst();
                stack.addFirst(doSmth(a, b, methodName, this));
            }

//            if (x.equals("+")){
//                Double b = stack.removeFirst(), a = stack.removeFirst();
//                stack.addFirst(a + b);
//            }
//            else if (x.equals("-")) {
//                Double b = stack.removeFirst(), a = stack.removeFirst();
//                stack.addFirst(a - b);
//            }
            else if (x.equals("um")) stack.addFirst(-stack.removeFirst());
            else stack.addFirst(Double.valueOf(x));
        }
        return stack.removeFirst();
    }

    private Double doSmth(Double a, Double b, String methodName, Calculator calculator){
        Double result = 0d;
        try{
            Class clazz = calculator.getClass();
            Class[] paramTypes = new Class[]{Double.class, Double.class};
            Method method = clazz.getMethod(methodName, paramTypes);
            Double[] args = new Double[]{a, b};
            result = (Double) method.invoke(calculator, args);
        }catch (NoSuchMethodException ex){
            //NOP
        }catch (IllegalAccessException ex){
            //NOP
        }catch (InvocationTargetException ex){
            //NOP
        }
        return result;
    }

}
