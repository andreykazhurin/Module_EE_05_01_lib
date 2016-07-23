package ua.com.myCalculator;

import java.util.*;

/**
 * Created by andrii.kazhurin on 13.07.2016.
 */
public class ExpressionParser {

    private Calculator calculator;

    private String operators;
    private String delimiters;


    public ExpressionParser(Calculator calculator) {
        this.calculator = calculator;
        operators = calculator.getStringSupportOperations();
        delimiters = "() " + operators;
    }

    private boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }

    public static boolean isOperator(String token, String operators) {
        if (token.equals("u-")) return true;
        for (int i = 0; i < operators.length(); i++) {
            if (token.charAt(0) == operators.charAt(i)) return true;
        }
        return false;
    }

    public  List<String> parse(String infix) throws FailedExpression {

        if ((infix == null) || (infix.length()==0)) {
            throw new IllegalArgumentException("Пустое выражение");
        }

        List<String> postfix = new ArrayList<String>();
        LinkedList<String> stack = new LinkedList<String>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String prev = "";
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(curr, operators)) {
                throw new FailedExpression("Некорректное выражение.");
            }
            if (curr.equals(" ")) continue;

            else if (isDelimiter(curr)) {
                if (curr.equals("(")) stack.addFirst(curr);
                else if (curr.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.removeFirst());
                        if (stack.isEmpty()) {
                            throw new FailedExpression("Скобки не согласованы.");
                        }
                    }
                    stack.removeFirst();
                    if (!stack.isEmpty()) {
                        postfix.add(stack.removeFirst());
                    }
                }
                else {
                    if (curr.equals("-") && (prev.equals("") || (isDelimiter(prev)  && !prev.equals(")")))) {
                        // унарный минус
                        curr = "um";
                    }
                    else {
                        while (!stack.isEmpty() && (calculator.getPriority(curr) <= calculator.getPriority(stack.peek()))) {
                            postfix.add(stack.removeFirst());
                        }

                    }
                    stack.addFirst(curr);
                }

            }

            else {
                postfix.add(curr);
            }
            prev = curr;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek(), operators)) postfix.add(stack.removeFirst());
            else {
                throw new FailedExpression("Скобки не согласованы.");
            }
        }
        return postfix;
    }

}
