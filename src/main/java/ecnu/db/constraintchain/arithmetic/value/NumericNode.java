package ecnu.db.constraintchain.arithmetic.value;

import ecnu.db.constraintchain.arithmetic.ArithmeticNode;
import ecnu.db.constraintchain.arithmetic.ArithmeticNodeType;

/**
 * @author wangqingshuai
 */
public class NumericNode extends ArithmeticNode {
    private Double constant;

    public NumericNode() {
        super(ArithmeticNodeType.CONSTANT);
    }

    public Double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

    public void setConstant(int constant) {
        this.constant = (double) constant;
    }

    public void setConstant(String constant) {
        this.constant = Double.parseDouble(constant);
    }



    @Override
    public String toString() {
        return constant.toString();
    }

    @Override
    public double evaluate() {
        return constant;
    }
}
