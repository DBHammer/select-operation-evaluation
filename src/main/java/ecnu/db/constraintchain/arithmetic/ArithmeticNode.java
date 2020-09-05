package ecnu.db.constraintchain.arithmetic;

/**
 * @author wangqingshuai
 */
public abstract class ArithmeticNode {
    protected ArithmeticNode leftNode;
    protected ArithmeticNode rightNode;
    protected ArithmeticNodeType type;

    public ArithmeticNode(ArithmeticNodeType type) {
        this.type = type;
    }

    public ArithmeticNodeType getType() {
        return this.type;
    }

    public void setType(ArithmeticNodeType type) {
        this.type = type;
    }

    public ArithmeticNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(ArithmeticNode leftNode) {
        this.leftNode = leftNode;
    }

    public ArithmeticNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(ArithmeticNode rightNode) {
        this.rightNode = rightNode;
    }

    abstract public double evaluate();
}
