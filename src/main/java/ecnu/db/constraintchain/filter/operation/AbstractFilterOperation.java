package ecnu.db.constraintchain.filter.operation;

import ecnu.db.constraintchain.filter.BoolExprNode;
import ecnu.db.constraintchain.filter.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangqingshuai
 */
public abstract class AbstractFilterOperation implements BoolExprNode {
    /**
     * 此filter包含的参数
     */
    protected List<Parameter> parameters = new ArrayList<>();
    /**
     * 此filter operation的操作符
     */
    protected CompareOperator operator;

    public AbstractFilterOperation(CompareOperator operator) {
        this.operator = operator;
    }

    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public CompareOperator getOperator() {
        return operator;
    }

    public void setOperator(CompareOperator operator) {
        this.operator = operator;
    }

}
