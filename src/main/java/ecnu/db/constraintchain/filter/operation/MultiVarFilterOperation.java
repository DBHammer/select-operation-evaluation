package ecnu.db.constraintchain.filter.operation;

import ecnu.db.constraintchain.arithmetic.ArithmeticNode;
import ecnu.db.constraintchain.filter.BoolExprType;
import ecnu.db.constraintchain.filter.Parameter;

import java.util.stream.Collectors;

/**
 * @author wangqingshuai
 */
public class MultiVarFilterOperation extends AbstractFilterOperation {
    private ArithmeticNode arithmeticTree;

    public MultiVarFilterOperation() {
        super(null);
    }

    public MultiVarFilterOperation(CompareOperator operator, ArithmeticNode arithmeticTree) {
        super(operator);
        this.arithmeticTree = arithmeticTree;
    }

    @Override
    public BoolExprType getType() {
        return BoolExprType.MULTI_FILTER_OPERATION;
    }

    @Override
    public boolean evaluate() {
        double val = arithmeticTree.evaluate();
        switch (operator) {
            case GT:
                return val > Double.parseDouble(parameters.get(0).getData());
            case GE:
                return val >= Double.parseDouble(parameters.get(0).getData());
            case LT:
                return val < Double.parseDouble(parameters.get(0).getData());
            case LE:
                return val <= Double.parseDouble(parameters.get(0).getData());
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", operator.toString().toLowerCase(),
                arithmeticTree.toString(),
                parameters.stream().map(Parameter::toString).collect(Collectors.joining(", ")));
    }

    public ArithmeticNode getArithmeticTree() {
        return arithmeticTree;
    }

    public void setArithmeticTree(ArithmeticNode arithmeticTree) {
        this.arithmeticTree = arithmeticTree;
    }
}
