package ecnu.db.constraintchain.arithmetic.value;

import ecnu.db.ColumnNotSetException;
import ecnu.db.constraintchain.arithmetic.ArithmeticNode;
import ecnu.db.constraintchain.arithmetic.ArithmeticNodeType;
import ecnu.db.constraintchain.filter.Parameter;
import ecnu.db.constraintchain.filter.operation.CompareOperator;

import java.util.List;

/**
 * @author wangqingshuai
 */
public class ColumnNode extends ArithmeticNode {
    private String canonicalTableName;
    private String columnName;
    private Object value;

    public ColumnNode() {
        super(ArithmeticNodeType.COLUMN);
    }

    public ColumnNode(String columnName, String tableName) {
        super(ArithmeticNodeType.COLUMN);
        this.columnName = columnName;
        this.canonicalTableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getCanonicalTableName() {
        return canonicalTableName;
    }

    public void setCanonicalTableName(String canonicalTableName) {
        this.canonicalTableName = canonicalTableName;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s.%s", canonicalTableName, columnName);
    }

    @Override
    public double evaluate() throws ColumnNotSetException {
        if (value == null) {
            throw new ColumnNotSetException();
        }
        return (double) ((Integer) value);
    }
}
