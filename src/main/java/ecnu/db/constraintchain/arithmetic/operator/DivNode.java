package ecnu.db.constraintchain.arithmetic.operator;

import ecnu.db.constraintchain.arithmetic.ArithmeticNode;
import ecnu.db.constraintchain.arithmetic.ArithmeticNodeType;

/**
 * @author wangqingshuai
 * todo 处理rightValue可能出现的0或很小的值
 */
public class DivNode extends ArithmeticNode {
    public DivNode() {
        super(ArithmeticNodeType.DIV);
    }



    @Override
    public String toString() {
        return String.format("div(%s, %s)", leftNode.toString(), rightNode.toString());
    }

    @Override
    public double evaluate() {
        double leftValue = leftNode.evaluate(), rightValue = rightNode.evaluate();
        return leftValue / rightValue;
    }
}
