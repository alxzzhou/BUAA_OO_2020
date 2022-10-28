package assignments.i.ii;

import assignments.i.ii.entity.Element;
import assignments.i.ii.entity.Poly;

import java.math.BigInteger;
import java.util.HashMap;

public class Parser {

    private Lexer lexer;
    private int glbPos;
    private int tokensSize;

    public void setTokensSize(int tokensSize) {
        this.tokensSize = tokensSize;
    }

    public void setGlbPos(int glbPos) {
        this.glbPos = glbPos;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    private boolean isMid(String str) {
        return str.equals("*");
    }

    private boolean isHigh(String str) {
        return str.equals("^");
    }

    private boolean isLow(String str) {
        return "+-".contains(curStr());
    }

    private void glbPosMove(int step) {
        glbPos += step;
    }

    private String curStr() {
        return lexer.getTokens().get(glbPos).getTokenValue();
    }

    /*
        public Expr parseExpr() {
            Expr expr = new Expr();
            return expr;
        }

        public Term parseTerm() {
            Term term = new Term();
            String ch;
            term.addUnit(parseUnit());
            term.setUnits(new ArrayList<>());
            while (isHigh(curStr()) || isMid(curStr())) {
                ch = curStr();
                glbPosMove(1);

            }
            return term;
        }

        public Element parseUnit() {
            int coe = (curStr().equals("-")) ? -1 : 1;
            glbPosMove("+-".contains(curStr()) ? 1 : 0);
            if (curStr().equals("x")) {
                Unit unit = new Unit(BigInteger.ONE, BigInteger.ONE, new Trig());
                if (glbPos == tokensSize - 1) {
                    unit = (coe == 1) ? unit : unit.rev();
                    return unit;
                }
                glbPosMove(1);
                if (curStr().equals("^")) {
                    unit = parsePower(unit);
                    if (glbPos == tokensSize - 1) {
                        unit = (coe == 1) ? unit : unit.rev();
                        return unit;
                    }
                }
            } else if (curStr().equals("(")) {
                Expr expr = new Expr();
                glbPosMove(1);
                expr = parseExpr();

                if (glbPos == tokensSize - 1) {
                    expr = (coe == 1) ? expr : expr.rev();
                    return expr;
                }

                glbPosMove(1);
                if (curStr().equals("^")) {
                    expr = parseExprPower(expr);
                    if (glbPos == tokensSize - 1) {
                        expr = (coe == 1) ? expr : expr.rev();
                        return expr;
                    }
                    glbPosMove(1);
                }
            } else if ("sc".contains(curStr())) {
                int triType = curStr().equals("s") ? 1 : 2;
                Expr expr = new Expr();
                glbPosMove(2);
                expr = parseExpr();
                Trig trig = new Trig();
                trig.setType(triType);
                trig.setExpr(expr);
                Unit unit = new Unit(BigInteger.ONE, BigInteger.ONE, trig);

                if (glbPos == tokensSize - 1) {
                    unit = (coe == 1) ? unit : unit.rev();
                    return unit;
                }

                if (curStr().equals("^")) {
                    unit = parsePower(unit);
                    if (glbPos == tokensSize - 1) {
                        unit = (coe == 1) ? unit : unit.rev();
                        return unit;
                    }
                    glbPosMove(1);
                }
            } else {
                Unit numUnit = new Unit(new BigInteger(curStr()), BigInteger.ZERO, new Trig());
                if (glbPos == tokensSize - 1) {
                    numUnit = (coe == 1) ? numUnit : numUnit.rev();
                    return numUnit;
                }
                glbPosMove(1);

                if (curStr().equals("^")) {
                    numUnit = parsePower(numUnit);
                    if (glbPos == tokensSize - 1) {
                        numUnit = (coe == 1) ? numUnit : numUnit.rev();
                        return numUnit;
                    }
                    glbPosMove(1);
                }
            }
            return null;
        }

        private Unit parsePower(Unit unit) {
            glbPosMove(1);
            if (curStr().equals("+")) {
                glbPosMove(1);
            }
            int pow = Integer.parseInt(curStr());
            return unit.power(BigInteger.valueOf(pow));
        }

        private Expr parseExprPower(Expr expr) {
            glbPosMove(1);
            if (curStr().equals("+")) {
                glbPosMove(1);
            }
            int pow = Integer.parseInt(curStr());
            return expr.pow(BigInteger.valueOf(pow));
        }
    */
    public Element parsePower(Element element) {
        glbPosMove(1);
        if (curStr().equals("+")) {
            glbPosMove(1);
        }
        int pow = Integer.parseInt(curStr());
        return element.pow(BigInteger.valueOf(pow));
    }

    public Element parseUnit() {
        int coe = 1;
        if ("+-".contains(curStr())) {
            coe = curStr().equals("+") ? 1 : -1;
            glbPosMove(1);
        }
        Element element = new Element();
        if (curStr().equals("x")) {
            element.setElementByInsert(BigInteger.valueOf(1), BigInteger.ONE);
            if (glbPos == tokensSize - 1) {
                element = (coe == 1) ? element : element.rev();
                return element;
            }
            glbPosMove(1);

            //initial
            if (curStr().equals("^")) {
                element = parsePower(element);
                if (glbPos == tokensSize - 1) {
                    element = (coe == 1) ? element : element.rev();
                    return element;
                }
                glbPosMove(1);


            } else {
                element.setElementByInsert(BigInteger.valueOf(1), BigInteger.ONE);
            }
        } else if (curStr().equals("(")) {
            glbPosMove(1);
            element = parseExpr();
            if (glbPos == tokensSize - 1) {
                element = (coe == 1) ? element : element.rev();
                return element;
            }
            glbPosMove(1);

            //for debug (对initial代码块的复用)
            if (curStr().equals("^")) {
                element = parsePower(element);
                if (glbPos == tokensSize - 1) {
                    element = (coe == 1) ? element : element.rev();
                    return element;
                }
                glbPosMove(1);
            }

        } else if ("sc".contains(curStr())) {
            String triTypeStr = curStr();
            glbPosMove(2);
            StringBuilder stringBuilder = new StringBuilder();
            while (!curStr().equals(")")) {
                stringBuilder.append(curStr());
                glbPosMove(1);
            }
            // put in TrigMap
            int triType = triTypeStr.equals("s") ? 1 : 2;
            HashMap<String, BigInteger> hashMap = new HashMap<>();
            hashMap.put(stringBuilder.toString(), BigInteger.ONE);
            Poly poly = new Poly();
            poly.setExp(BigInteger.ZERO);
            if (triType == 2) {
                poly.setCosSet(hashMap);
                poly.setSinSet(new HashMap<>());
            } else {
                poly.setSinSet(hashMap);
                poly.setCosSet(new HashMap<>());
            }

            HashMap<Poly, BigInteger> hashMapPutInEle = new HashMap<>();
            hashMapPutInEle.put(poly, BigInteger.ONE);
            element.setElements(hashMapPutInEle);

            if (glbPos == tokensSize - 1) {
                element = (coe == 1) ? element : element.rev();
                return element;
            }
            glbPosMove(1);

            if (curStr().equals("^")) {
                element = parsePower(element);
                if (glbPos == tokensSize - 1) {
                    element = (coe == 1) ? element : element.rev();
                    return element;
                }
                glbPosMove(1);
            }
        } else {
            BigInteger con = new BigInteger(curStr());
            element.setElementByInsert(BigInteger.valueOf(0), con);
            if (glbPos == tokensSize - 1) {
                element = (coe == 1) ? element : element.rev();
                return element;
            }
            glbPosMove(1);

            //for debug (对initial代码块的复用)
            if (curStr().equals("^")) {
                element = parsePower(element);
                if (glbPos == tokensSize - 1) {
                    element = (coe == 1) ? element : element.rev();
                    return element;
                }
                glbPosMove(1);
            }

        }
        element = (coe == 1) ? element : element.rev();
        return element;
    }   //  need to add ( expr ) ** pow

    public Element parseTerm() {
        Element element1 = parseUnit();
        Element element2 = new Element();
        element2.setElements(new HashMap<>());
        String ch;
        while (isMid(curStr()) || isHigh(curStr())) {
            ch = curStr();
            glbPosMove(1);
            element2 = parseUnit();
            if (ch.equals("*")) {
                element1 = element1.mul(element2);
            } else if (ch.equals("^")) {
                Poly poly = new Poly();
                poly.setExp(BigInteger.ZERO);
                poly.setSinSet(new HashMap<>());
                poly.setCosSet(new HashMap<>());
                element1 = element1.pow(element2.getElements().get(poly));
            }
        }
        return element1;
    }

    public Element parseExpr() {
        Element element1;
        Element element2;
        int coe = 1;
        String ch = curStr();
        if (ch.equals("-")) {
            glbPosMove(1);
            coe = -1;
        }
        element1 = (coe == 1) ? parseTerm() : parseTerm().rev();
        while (isLow(curStr())) {
            ch = curStr();
            glbPosMove(1);
            element2 = parseTerm();
            if (ch.equals("+")) {
                element1 = element1.add(element2);
            } else if (ch.equals("-")) {
                element1 = element1.add(element2.rev());
            }
        }
        return element1;
    }

}
