package assignments.i.ii;

import java.util.ArrayList;

public class Lexer {

    private ArrayList<Token> tokens;
    private int pos;
    private String input;

    public void lexerAnalysis(String str) {
        this.input = str;
        while (pos != input.length()) {
            this.readAll();
        }
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    private String getNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            stringBuilder.append(input.charAt(pos));
            pos++;
        }
        return stringBuilder.toString();
    }

    private String getAddOrSub() {
        StringBuilder stringBuilder = new StringBuilder();
        while (pos < input.length() && "+-".indexOf(input.charAt(pos)) != -1) {
            stringBuilder.append(input.charAt(pos));
            pos++;
        }
        String stringBuilderWithoutMinus = stringBuilder.toString().replaceAll("-", "");
        int numOfMinus = stringBuilder.toString().length() - stringBuilderWithoutMinus.length();
        return (numOfMinus % 2 == 0) ? "+" : "-";
    }

    private void readAll() {
        char ch = input.charAt(pos);
        Token token = new Token();
        if (Character.isDigit(ch)) {
            token.setTokenValue(getNumber());
            tokens.add(token);
        } else if ("+-".indexOf(ch) != -1) {
            token.setTokenValue(getAddOrSub());
            tokens.add(token);
        } else {
            token.setTokenValue(String.valueOf(ch));
            tokens.add(token);
            pos++;
        }
    }

}