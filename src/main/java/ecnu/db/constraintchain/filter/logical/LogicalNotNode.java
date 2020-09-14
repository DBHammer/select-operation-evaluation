package ecnu.db.constraintchain.filter.logical;

import ecnu.db.constraintchain.filter.BoolExprNode;
import ecnu.db.constraintchain.filter.BoolExprType;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author alan
 */
public class LogicalNotNode implements BoolExprNode {
    private BoolExprType type = BoolExprType.NOT;
    private BoolExprNode expr;

    public LogicalNotNode() {}

    @Override
    public BoolExprType getType() {
        return type;
    }

    public boolean evaluate() {
        return !expr.evaluate();
    }

    public void setType(BoolExprType type) {
        this.type = type;
    }

    public BoolExprNode getExpr() {
        return expr;
    }

    public void setExpr(BoolExprNode expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return String.format("not(%s)", expr.toString());
    }
}