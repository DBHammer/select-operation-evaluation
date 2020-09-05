package ecnu.db.constraintchain.filter;

import ecnu.db.constraintchain.filter.operation.CompareOperator;

/**
 * @author alan
 * 代表需要实例化的参数
 */
public class Parameter {
    /**
     * parameter的id，用于后续实例化
     */
    private Integer id;
    /**
     * parameter的data
     */
    private String data;

    public Parameter(Integer id, String data) {
        this.id = id;
        this.data = data;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("{id:%d, data:'%s'}", id, data);
    }
}
