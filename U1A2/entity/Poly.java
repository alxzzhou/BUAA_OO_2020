package assignments.i.ii.entity;

import java.math.BigInteger;
import java.util.HashMap;

public class Poly {

    private BigInteger exp;
    private HashMap<String, BigInteger> sinSet;
    private HashMap<String, BigInteger> cosSet;

    public BigInteger getExp() {
        return exp;
    }

    public Poly copy(Poly poly) {
        Poly copy = new Poly();
        copy.setExp(poly.getExp());
        HashMap<String,BigInteger> sin = new HashMap<>();
        for (String str : poly.getSinSet().keySet()) {
            sin.put(str,poly.getSinSet().get(str));
        }
        HashMap<String,BigInteger> cos = new HashMap<>();
        for (String str : poly.getCosSet().keySet()) {
            cos.put(str,poly.getCosSet().get(str));
        }
        copy.setSinSet(sin);
        copy.setCosSet(cos);
        return copy;
    }

    public void setExp(BigInteger exp) {
        this.exp = exp;
    }

    public void setCosSet(HashMap<String, BigInteger> cosSet) {
        this.cosSet = cosSet;
    }

    public void setSinSet(HashMap<String, BigInteger> sinSet) {
        this.sinSet = sinSet;
    }

    public HashMap<String, BigInteger> getCosSet() {
        return cosSet;
    }

    public HashMap<String, BigInteger> getSinSet() {
        return sinSet;
    }

    public boolean equal(Poly poly) {
        if (exp.equals(poly.exp) &&
                sinSet.size() == poly.sinSet.size() &&
                cosSet.size() == poly.cosSet.size()) {
            return sinSet.equals(poly.sinSet) && cosSet.equals(poly.cosSet);
        } else {
            return false;
        }
    }

    public Poly mul(Poly poly) {
        Poly p  = copy(this);
        p.exp = p.exp.add(poly.exp);
        poly.sinSet.forEach((k,v) -> p.sinSet.merge(k,v, BigInteger::add));
        poly.cosSet.forEach((k,v) -> p.cosSet.merge(k,v, BigInteger::add));
        return p;
    }

}
