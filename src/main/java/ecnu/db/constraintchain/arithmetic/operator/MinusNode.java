package ecnu.db.constraintchain.arithmetic.operator;

import ecnu.db.constraintchain.arithmetic.ArithmeticNode;
import ecnu.db.constraintchain.arithmetic.ArithmeticNodeType;

/**
 * @author wangqingshuai
 */
public class MinusNode extends ArithmeticNode {
    public MinusNode() {
        super(ArithmeticNodeType.MINUS);
    }

    public double evaluate() {
        double leftValue = leftNode.evaluate(), rightValue = rightNode.evaluate();
        return leftValue - rightValue ;
    }

    @Override
    public String toString() {
        return String.format("minus(%s, %s)", leftNode.toString(), rightNode.toString());
    }
}
