package ecnu.db.constraintchain.filter.operation;

import ecnu.db.constraintchain.arithmetic.value.ColumnNode;
import ecnu.db.constraintchain.filter.BoolExprType;

/**
 * @author alan
 */
public class IsNullFilterOperation extends AbstractFilterOperation {
    private ColumnNode columnNode;
    private Boolean hasNot = false;

    public IsNullFilterOperation() {
        super(CompareOperator.ISNULL);
    }

    public IsNullFilterOperation(ColumnNode columnNode) {
        super(CompareOperator.ISNULL);
        this.columnNode = columnNode;
    }


    @Override
    public BoolExprType getType() {
        return BoolExprType.ISNULL_FILTER_OPERATION;
    }

    @Override
    public boolean evaluate() {
        boolean ret = (columnNode.getValue() == null);
        if (hasNot) {
            return !ret;
        }
        return ret;
    }

    @Override
    public String toString() {
        if (hasNot) {
            return String.format("not(isnull(%s))", this.columnNode);
        }
        return String.format("isnull(%s)", this.columnNode);
    }

    public Boolean getHasNot() {
        return hasNot;
    }

    public void setHasNot(Boolean hasNot) {
        this.hasNot = hasNot;
    }

    public ColumnNode getColumnNode() {
        return columnNode;
    }

    public void setColumnNode(ColumnNode columnNode) {
        this.columnNode = columnNode;
    }
}
