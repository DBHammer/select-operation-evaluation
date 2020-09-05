package ecnu.db.constraintchain.filter.logical;

import ecnu.db.constraintchain.filter.BoolExprNode;
import ecnu.db.constraintchain.filter.BoolExprType;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangqingshuai
 */
public class AndNode implements BoolExprNode {
    private BoolExprType type = BoolExprType.AND;
    private List<BoolExprNode> children = new LinkedList<>();

    public AndNode() {}

    public void addChild(BoolExprNode logicalNode) {
        children.add(logicalNode);
    }

    @Override
    public BoolExprType getType() {
        return type;
    }

    public boolean evaluate() {
        return children.stream()
                .map(BoolExprNode::evaluate)
                .reduce(true, (expr1, expr2) -> (expr1 & expr2));
    }

    public void setType(BoolExprType type) {
        this.type = type;
    }

    public List<BoolExprNode> getChildren() {
        return children;
    }

    public void setChildren(List<BoolExprNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return String.format("and(%s)", children.stream()
                .map(BoolExprNode::toString)
                .collect(Collectors.joining(", ")));
    }
}
