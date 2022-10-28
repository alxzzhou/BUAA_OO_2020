package assignments.i.ii;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreProcessing {

    private final Pattern[] userDefinedFunc = {
            Pattern.compile("([fgh])[(]([xyz])[)]"),
            Pattern.compile("([fgh])[(]([xyz]),([xyz])[)]"),
            Pattern.compile("([fgh])[(]([xyz]),([xyz]),([xyz])[)]")};
    private final Pattern[] inputFunc = {
            Pattern.compile("([fgh])[(](.*)[)]"),
            Pattern.compile("([fgh])[(](.*),(.*)[)]"),
            Pattern.compile("([fgh])[(](.*),(.*),(.*)[)]")};
    private final Pattern sumFunc =
            Pattern.compile("[#][(]i,(.*),(.*),(.*)[)]");

    private String input;

    public void replaceUnrelatedChars() {
        input = input.replaceAll("[*]{2}", "^");
        input = input.replaceAll("[ \t]+", "");
        input = input.replaceAll("sum", "#");
        input = input.replaceAll("sin", "s");
        input = input.replaceAll("cos", "c");
    }

    public PreProcessing(String expr) {
        this.input = expr;
    }

    /*public String toString() {
        return input;
    }*/

    public void preProcessSums() {
        Matcher sumMatcher;
        for (String str : getFuncFromInput("#")) {
            StringBuilder recentSum = new StringBuilder("(");
            sumMatcher = sumFunc.matcher(str);
            sumMatcher.find();
            int start = Integer.parseInt(sumMatcher.group(1));
            int end = Integer.parseInt(sumMatcher.group(2));
            String expr = sumMatcher.group(3);
            for (int i = start; i <= end; i++) {
                recentSum.append("(").append(expr.replace("i", String.valueOf(i))).append(")")
                        .append((i == end) ? "" : "+");
            }
            recentSum.append(start > end ? "0" : "");
            recentSum.append(")");
            input = input.replace(sumMatcher.group(0), recentSum.toString());
        }
    }

    private ArrayList<String> getFuncFromInput(String funcSignifier) {
        int pos = 0;
        ArrayList<String> resFuncs = new ArrayList<>();
        StringBuilder recentFunc = new StringBuilder();
        while (pos < input.length()) {
            if (!funcSignifier.contains(String.valueOf(input.charAt(pos)))) {
                pos++;
                continue;
            }
            recentFunc.append(input.charAt(pos));
            pos++;
            int fl = 0;
            for (; ; pos++) {
                fl += (input.charAt(pos) == '(') ? 1 :
                        (input.charAt(pos) == ')') ? -1 : 0;
                if (fl > 0) {
                    recentFunc.append(input.charAt(pos));
                } else {
                    break;
                }
            }
            recentFunc.append(")");
            resFuncs.add(String.valueOf(recentFunc));
            recentFunc = new StringBuilder();
        }
        return resFuncs;
    }

    public void preProcessDefFuncs(ArrayList<String> definedFuncs) {
        for (String definedFunc : definedFuncs) {

            String func = definedFunc.replaceAll("[ \t]", "");
            String funcLeft = func.split("=")[0];
            String funcRight = func.split("=")[1];
            int varNum = funcLeft.replaceAll("[^xyz]", "").length();

            Matcher defFuncMatcher = userDefinedFunc[varNum - 1].matcher(funcLeft);
            defFuncMatcher.find();
            Matcher inputMatcher;

            for (String str : getFuncFromInput(String.valueOf(definedFunc.charAt(0)))) {
                inputMatcher = inputFunc[varNum - 1].matcher(str);
                inputMatcher.find();
                if (!inputMatcher.group(1).equals(defFuncMatcher.group(1))) {
                    continue;
                }
                String substitute = funcRight;
                for (int j = 2; j < varNum + 2; j++) {
                    substitute = substitute.replaceAll(defFuncMatcher.group(j),
                            "(" + inputMatcher.group(j).replaceAll("x", "w") + ")");
                }
                input = input.replace(inputMatcher.group(0), "(" + substitute + ")");
            }
            input = input.replace("w", "x");
        }
    }

    private String getString(int pos) {
        return String.valueOf(input.charAt(pos));
    }

    public void rmBracketsFromTrig() {
        int end = input.length();
        StringBuilder sb = new StringBuilder(input);
        int pos;
        int brNum;
        for (pos = 0; pos < end; pos++) {
            brNum = 1;
            if ("sc".contains(getString(pos))) {
                pos += 2;
                while (brNum > 0) {
                    brNum += getString(pos).equals("(") ? 1 :
                            getString(pos).equals(")") ? -1 : 0;
                    if (brNum == 0) {
                        break;
                    }
                    if ("()".contains(getString(pos))) {
                        sb.replace(pos, pos + 1, getString(pos).equals("(") ? "@" : "#");
                    }
                    pos++;
                }
            }
        }
        input = sb.toString()
                .replaceAll("[@]","")
                .replaceAll("[#]","");
    }
}
