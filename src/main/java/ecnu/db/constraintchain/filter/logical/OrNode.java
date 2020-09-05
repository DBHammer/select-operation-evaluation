package ecnu.db.constraintchain.filter.logical;

import ecnu.db.constraintchain.filter.BoolExprNode;
import ecnu.db.constraintchain.filter.BoolExprType;

/**
 * @author wangqingshuai
 */
public class OrNode implements BoolExprNode {
    private final BoolExprType type = BoolExprType.OR;
    private BoolExprNode leftNode;
    private BoolExprNode rightNode;

    public BoolExprNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(BoolExprNode leftNode) {
        this.leftNode = leftNode;
    }

    public BoolExprNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(BoolExprNode rightNode) {
        this.rightNode = rightNode;
    }

    @Override
    public BoolExprType getType() {
        return type;
    }

    public boolean evaluate() {
        boolean leftValue = leftNode.evaluate(), rightValue = rightNode.evaluate();
        return leftValue || rightValue;
    }

    @Override
    public String toString() {
        return String.format("or(%s, %s)", leftNode.toString(), rightNode.toString());
    }
}
