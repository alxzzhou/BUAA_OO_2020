package assignments.i.ii;

import assignments.i.ii.entity.Element;
import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //reading data
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        ArrayList<String> definedFuncs = new ArrayList<>();
        int count = scanner.getCount();
        while (count > 0) {
            String str = scanner.readLine();
            definedFuncs.add(str);
            count--;
        }
        PreProcessing preProcessing = new PreProcessing(scanner.readLine());
        preProcessing.replaceUnrelatedChars();
        preProcessing.preProcessDefFuncs(definedFuncs);
        preProcessing.preProcessSums();
        preProcessing.replaceUnrelatedChars();
        preProcessing.rmBracketsFromTrig();

        //lexer initiation
        ArrayList<Token> tokens = new ArrayList<>();
        Lexer lexer = new Lexer();
        lexer.setTokens(tokens);
        lexer.setPos(0);

        //lexer processing
        lexer.lexerAnalysis(preProcessing.toString());

        //parser
        Parser parser = new Parser();
        parser.setGlbPos(0);
        parser.setTokensSize(lexer.getTokens().size());
        parser.setLexer(lexer);
        Element res = parser.parseExpr();

        res.specialJudgeTrigConst();    // sin(0) cos(0)
        res.specialJudgeTrigSq();
        res.output();

        /*HashMap<String, BigInteger> sinMap = new HashMap<>();
        HashMap<String, BigInteger> cosMap = new HashMap<>();
        Poly poly1 = new Poly();
        Poly poly2 = new Poly();
        sinMap.put("x", BigInteger.ONE);
        cosMap.put("x", BigInteger.ONE);
        poly1.setSinSet(sinMap);
        poly1.setCosSet(cosMap);
        poly1.setExp(BigInteger.ZERO);
        poly2.setSinSet(sinMap);
        poly2.setCosSet(cosMap);
        poly2.setExp(BigInteger.ZERO);
        Poly poly3 = new Poly();
        poly3 = poly1.mul(poly2);

        System.out.println(1);*/

    }

}
