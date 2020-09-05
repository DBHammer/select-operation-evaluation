package ecnu.db.constraintchain.arithmetic.operator;

import ecnu.db.constraintchain.arithmetic.ArithmeticNode;
import ecnu.db.constraintchain.arithmetic.ArithmeticNodeType;

/**
 * @author wangqingshuai
 */
public class MulNode extends ArithmeticNode {
    public MulNode() {
        super(ArithmeticNodeType.MUL);
    }

    public double evaluate() {
        double leftValue = leftNode.evaluate(), rightValue = rightNode.evaluate();
        leftValue *= rightValue;
        return leftValue;
    }

    @Override
    public String toString() {
        return String.format("mul(%s, %s)", leftNode.toString(), rightNode.toString());
    }
}
