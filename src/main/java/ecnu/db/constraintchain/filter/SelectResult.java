package ecnu.db.constraintchain.filter;

import ecnu.db.constraintchain.arithmetic.value.ColumnNode;
import ecnu.db.constraintchain.filter.logical.AndNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author alan
 */
public class SelectResult {
    private AndNode condition;
    private List<Parameter> parameters;
    private String tableName;
    private Map<String, ColumnNode> columns;

    public AndNode getCondition() {
        return condition;
    }

    public void setCondition(AndNode condition) {
        this.condition = condition;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, ColumnNode> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, ColumnNode> columns) {
        this.columns = columns;
    }

    public ColumnNode getColumn(String canonicalColumnName) {
        return columns.get(canonicalColumnName);
    }
}
