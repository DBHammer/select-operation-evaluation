package ecnu.db.constraintchain.filter;

import ecnu.db.ColumnNotSetException;

/**
 * @author wangqingshuai
 * todo 当前认为所有的BoolExprNode都是相互独立的
 */
public interface BoolExprNode {
    /**
     * 获得当前布尔表达式节点的类型
     *
     * @return 类型
     */
    BoolExprType getType();

    /**
     * 获取生成好column以后，evaluate表达式的布尔值
     * @return evaluate表达式的布尔值
     */
    boolean evaluate() throws ColumnNotSetException;
}
