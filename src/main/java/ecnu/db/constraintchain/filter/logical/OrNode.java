package ecnu.db.constraintchain.filter.logical;

import ecnu.db.ColumnNotSetException;
import ecnu.db.constraintchain.filter.BoolExprNode;
import ecnu.db.constraintchain.filter.BoolExprType;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangqingshuai
 */
public class OrNode implements BoolExprNode {
    private final BoolExprType type = BoolExprType.OR;
    private List<BoolExprNode> children = new LinkedList<>();

    public List<BoolExprNode> getChildren() {
        return children;
    }

    public void setChildren(List<BoolExprNode> children) {
        this.children = children;
    }

    public void addChild(BoolExprNode logicalNode) {
        children.add(logicalNode);
    }

    @Override
    public BoolExprType getType() {
        return type;
    }

    public boolean evaluate() throws ColumnNotSetException {
        for (BoolExprNode child : children) {
            if (child.evaluate()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("or(%s)", children.stream()
                .map(BoolExprNode::toString)
                .collect(Collectors.joining(", ")));
    }
}
