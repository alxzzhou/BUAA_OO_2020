package assignments.i.ii.entity;

import java.math.BigInteger;
import java.util.HashMap;

public class Element {

    private HashMap<Poly, BigInteger> elements;

    private Element copy(Element element) {
        Element copy = new Element();
        HashMap<Poly, BigInteger> copyEle = new HashMap<>();
        for (Poly poly : element.elements.keySet()) {
            copyEle.put(poly.copy(poly), element.elements.get(poly));
        }
        copy.setElements(copyEle);
        return copy;
    }

    public void setElements(HashMap<Poly, BigInteger> elements) {
        this.elements = elements;
    }

    public HashMap<Poly, BigInteger> getElements() {
        return elements;
    }

    private void merge() {
        HashMap<Poly, BigInteger> origin = elements;
        HashMap<Poly, BigInteger> dest = new HashMap<>();
        for (Poly originPoly : elements.keySet()) {
            boolean eq = false;
            for (Poly destPoly : dest.keySet()) {
                if (originPoly.equal(destPoly)) {
                    eq = true;
                    dest.replace(destPoly, dest.get(destPoly).add(origin.get(originPoly)));
                    break;
                }
            }
            if (!eq) {
                dest.put(originPoly, origin.get(originPoly));
            }
        }
        this.setElements(dest);
    }

    public void setElementByInsert(BigInteger exp, BigInteger coe) {
        Poly poly = new Poly();
        poly.setExp(exp);
        poly.setCosSet(new HashMap<>());
        poly.setSinSet(new HashMap<>());
        HashMap<Poly, BigInteger> ele = new HashMap<>();
        ele.put(poly, coe);
        this.setElements(ele);
    }

    public Element add(Element element) {
        Element res = new Element();
        res.setElements(new HashMap<>());
        for (Poly thisPoly : elements.keySet()) {
            for (Poly elsePoly : element.elements.keySet()) {
                {
                    res.elements.put(thisPoly, elements.get(thisPoly));
                    res.elements.put(elsePoly, element.elements.get(elsePoly));
                }
            }
        }
        res.merge();
        return res;
    }

    public Element mul(Element element) {
        Element res = new Element();
        res.setElements(new HashMap<>());
        for (Poly thisPoly : elements.keySet()) {
            for (Poly elsePoly : element.elements.keySet()) {
                res.elements.merge(thisPoly.mul(elsePoly),
                        elements.get(thisPoly).multiply(element.elements.get(elsePoly)),
                        BigInteger::add);
            }
        }
        res.merge();
        return res;
    }

    public Element pow(BigInteger pow) {
        if (pow.equals(BigInteger.ZERO)) {
            Element zero = new Element();
            zero.setElementByInsert(BigInteger.ZERO, BigInteger.ONE);
            return zero;
        }
        BigInteger p = pow;
        Element element = new Element();
        element.setElements(this.getElements());
        while (p.compareTo(BigInteger.ONE) > 0) {
            element = element.mul(this);
            p = p.subtract(BigInteger.ONE);
        }
        return element;
    }

    public Element rev() {
        for (Poly poly : elements.keySet()) {
            elements.computeIfPresent(poly, (k, v) -> v.negate());
        }
        return this;
    }

    public void output() {
        StringBuilder res = new StringBuilder();
        boolean firstTerm = true;
        for (Poly poly : elements.keySet()) {
            BigInteger coe = elements.get(poly);
            boolean firstUnit = true;
            if (coe.equals(BigInteger.ZERO)) {
                continue;
            }

            // output coefficient
            if (!firstTerm) {
                // sign
                res.append((coe.compareTo(BigInteger.ZERO) > 0) ? "+" : "");
            }
            boolean constant = poly.getExp().equals(BigInteger.ZERO) &&
                    poly.getSinSet().size() == 0 &&
                    poly.getCosSet().size() == 0;
            res.append(constant ? coe : coe.equals(BigInteger.ONE) ? "" :
                    coe.equals(BigInteger.ONE.negate()) ? "-" : coe);
            if (!coe.equals(BigInteger.ONE) && !coe.equals(BigInteger.ONE.negate())) {
                firstUnit = false;
            }

            // output x^n
            BigInteger exp = poly.getExp();
            res.append(exp.equals(BigInteger.ZERO) ? "" :
                    firstUnit ? (exp.equals(BigInteger.ONE) ? "x" : "x**" + exp) :
                            (exp.equals(BigInteger.ONE) ? "*x" : "*x**" + exp));
            if (!exp.equals(BigInteger.ZERO)) {
                firstUnit = false;
            }

            // output sin
            HashMap<String, BigInteger> sinMap = poly.getSinSet();
            for (String str : sinMap.keySet()) {
                if (!firstUnit) {
                    res.append("*");
                }
                res.append("sin(").append(str).append(")")
                        .append(sinMap.get(str).equals(BigInteger.ONE) ? "" :
                                "**" + sinMap.get(str));
                firstUnit = false;
            }

            // output cos
            HashMap<String, BigInteger> cosMap = poly.getCosSet();
            for (String str : cosMap.keySet()) {
                if (!firstUnit) {
                    res.append("*");
                }
                res.append("cos(").append(str).append(")")
                        .append(cosMap.get(str).equals(BigInteger.ONE) ? "" :
                                "**" + cosMap.get(str));
                firstUnit = false;
            }

            firstTerm = false;
        }
        System.out.println(res.toString().equals("") ? "0" : res.toString().replaceAll("\\^","**"));
    }

    public void specialJudgeTrigConst() {
        for (Poly poly : elements.keySet()) {
            if (poly.getSinSet().containsKey("0")) {
                elements.replace(poly, BigInteger.ZERO);
            } else {
                poly.getCosSet().remove("0");
            }
        }
        this.merge();
    }

    public void specialJudgeTrigSq() {
        Element copy = copy(this);
        HashMap<Poly,String> polyList = new HashMap<>(); //
        for (Poly thisPoly : elements.keySet()) {
            BigInteger thisCoe = elements.get(thisPoly);
            for (Poly elsePoly : copy.elements.keySet()) {
                if (thisPoly.equal(elsePoly)) {
                    continue;
                }
                BigInteger elseCoe = copy.elements.get(elsePoly);
                for (String sinStr : thisPoly.getSinSet().keySet()) { // index in this.sinSet
                    Poly thisCopy = thisPoly.copy(thisPoly);
                    Poly elseCopy = elsePoly.copy(elsePoly);
                    if (elsePoly.getCosSet().containsKey(sinStr) &&
                            thisPoly.getSinSet().get(sinStr).equals(BigInteger.valueOf(2)) &&
                            elsePoly.getCosSet().get(sinStr).equals(BigInteger.valueOf(2))) {
                        thisCopy.getSinSet().remove(sinStr);
                        elseCopy.getCosSet().remove(sinStr);
                        if (thisCopy.equal(elseCopy)) {
                            polyList.put(elsePoly, sinStr);
                            elements.computeIfPresent(thisPoly,(k, v) -> v.subtract(elseCoe));
                        }
                    }
                }
            }
        }
        for (Poly poly : polyList.keySet()) {
            for (Poly p : elements.keySet()) {
                if (p.equal(poly)) {
                    p.getCosSet().remove(polyList.get(poly));
                    break;
                }
            }
        }
        this.merge();
    }

}
