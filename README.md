# SQL Operator Evaluation

SQL Operator Evaluation（以下简称SOE）是一款SQL表达式计算工具，对给定的Operator表达式可快速计算输入数据在该Operator下的逻辑结果。SOE项目派生自[Touchstone For Cloud](https://github.com/DBHammer/TouchstoneToolChain), 对于给定的Operator支持单值计算和向量计算两种模式。单值计算通过设置一组给定值计算该Operator的逻辑状态，多值计算通过设置一组给定值的向量批量计算Operator的逻辑状态。

## 表达式语法规则
SOE使用波兰式的方式设置待计算的表达式，支持算数表达式，比较表达式和逻辑表达式三种。算数表达式支持四则运算，比较表达式支持常见的各种比较运算符，逻辑表达式支持and，or和not。下面给出相应的的语法规则。

>**特殊符号定义**
>
>| 词法表示 | 说明     | 输入样式 |
>| -------- | -------- | -------- |
>| LPAREN   | 左括号   | `(`      |
>| RPAREN   | 右括号   | `)`      |
>| COMMA    | 逗号     | `,`      |
>| PLUS     | 加法算子 | `plus`   |
>| MINUS    | 减法算子 | `minus`  |
>| MUL      | 乘法算子 | `mul`    |
>| DIV      | 除法算子 | `div`    |
>| INTEGER  | 整数     | 79       |
>| FLOAT    | 浮点数   | 335.6    |

### 算数表达式

算数算子的语法规则如下

```python
arithmetic_expr = arithmetic_operator LPAREN arithmetic_expr COMMA arithmetic_expr RPAREN | CANONICAL_COLUMN_NAME | INTEGER ｜ FLOAT
arithmetic_operator = PLUS ｜ MINUS ｜ MUL ｜ DIV
```

其中的CANONICAL_COLUMN_NAME词法定义如下，输入样式为`database.table.column`，例如`tpch.order.orderId`

```
SCHEMA_NAME_CHAR=[A-Za-z0-9$_]
CANONICAL_COL_NAME=({SCHEMA_NAME_CHAR})+\.({SCHEMA_NAME_CHAR})+\.({SCHEMA_NAME_CHAR})+
```

**例子**: `mul(db.table.col1, plus(db.table.col2, 3))`

### 比较表达式

> **比较运算符定义**
>
> | 词法表示 | 说明                                                    | 输入样式 |
> | -------- | ------------------------------------------------------- | -------- |
> | LT       | 小于                                                    | lt       |
> | GT       | 大于                                                    | gt       |
> | LE       | 小于等于                                                | le       |
> | GE       | 大于等于                                                | ge       |
> | EQ       | 等于                                                    | eq       |
> | NE       | 不等于                                                  | ne       |
> | IN       | 包含                                                    | in       |
> | LIKE     | 模糊字符串匹配，支持前缀模糊，后缀模糊和字符串内模糊3种 | like     |
> | ISNULL   | 该列数据为NULL                                          | isnull   |

比较算子包含单值比较，多值比较和ISNULL判断三种。其中单值比较用于判断某列或者某表达式和某一个单值的关系，多值比较判断某列和多个值的关系，ISNULL用于判断该列的数据是否为null

+ 单值比较语法规则

```
uni_compare_expr = uni_compare_operator LPAREN arithmetic_expr COMMA uni_compare_constant RPAREN
uni_compare_operator = LT｜GT｜LE｜GE｜EQ｜NE
uni_compare_constant = INTEGER｜FLOAT｜DATE｜STRING
```

**例子：**`ge((db.table.col1), 2)`

+ 多值比较的语法规则

  > in表达式的输入定义暂时只支持string，但是在计算时会对根据输入的数据列类型进行转换

```
multi_compare_expr = in_compare_expr|like_compare_expr
in_compare_expr = IN LPAREN CANONICAL_COLUMN_NAME in_arguments RPAREN
in_arguments ::= STRING | in_arguments COMMA STRING    
like_compare_expr =  LIKE LPAREN CANONICAL_COLUMN_NAME COMMA STRING RPAREN
```

**例子** `in(db.table.col3, "3", "2")`，`like(db.table.col1, "%hello")`

+ ISNULL判断的语法规则

```
isnull_expr = ISNULL LPAREN CANONICAL_COLUMN_NAME RPAREN    
```

**例子：**`isnull(db.table.col3)`

### 逻辑表达式

> **逻辑运算符定义**
>
> | 词法表示 | 说明 | 输入样式 |
> | -------- | ---- | -------- |
> | AND      | 与   | and      |
> | OR       | 或   | or       |
> | NOT      | 非   | not      |

逻辑表达式包含and表达式，or表达式和not表达式

+ and表达式的语法规则

```
and_expr = AND LPAREN logical_arguments:children RPAREN 
logical_arguments = bool_expr COMMA bool_expr | bool_expr COMMA logical_arguments
```

**例子：**`and(gt(db.table.col2,2),isnull(db.table.col3),ge(mul(db.table.col1, plus(db.table.col2, 3)), 2))`

+ or表达式的语法规则

```
or_expr = OR LPAREN bool_expr COMMA bool_expr RPAREN
```

**例子：**`or(gt(db.table.col2,2),isnull(db.table.col3))`

+ not表达式的语法规则

```
not_expr = NOT LPAREN not_arguments RPAREN
not_arguments = like_compare_expr | in_compare_expr | isnull_expr
```

**例子：**`not(isnull(db.table.col3))`

## 快速上手

在class path中，引入sql-operator-evaluation-{version}.jar包即可使用，请在release页面下载jar包，使用方式如下

```java
import ecnu.db.SqlOperatorEvaluation;
import ecnu.db.constraintchain.filter.SelectResult;

public class Main {

    public static void main(String[] args) throws Exception {

        // db.table.col1 * (db.table.col2 + 3) >= 2 ?
        SelectResult result = SqlOperatorEvaluation.getEvaluation("ge(mul(db.table.col1, plus(db.table.col2, 3)), 2)");

        result.getColumn("db.table.col1").setValue(3);
        result.getColumn("db.table.col2").setValue(2);

        // 3 * (2 + 3) = 15 > 2 --> true
        System.out.println(result.getCondition().evaluate());

        result.getColumn("db.table.col1").setValue(2);
        result.getColumn("db.table.col2").setValue(-2);

        // 2 * (-2 + 3) = 2 = 2 --> true
        System.out.println(result.getCondition().evaluate());

        // db.table.col1 >= 2 or db.table.col2 not isnull ?
        result = SqlOperatorEvaluation.getEvaluation("or(ge(db.table.col1, 2), not(isnull(db.table.col2)))");

        result.getColumn("db.table.col1").setValue(1);
        result.getColumn("db.table.col2").setValue(null);

        // (1 < 2 --> false) or (db.table.col2 is null --> false) --> false
        System.out.println(result.getCondition().evaluate());
    }
}
```

输出为

```java
true
true
false
```

## 联系我们

研发团队：华东师范大学数据科学与工程学院 DBHammer数据库项目组。

该工作的主要成员：王清帅 在读博士，连薛超 在读硕士。

地址：上海市普陀区中山北路3663号。

邮政编码：200062。

对于文档内容和实现源码有任何疑问，可通过发起issue与我们联系，我们收到后将尽快给您反馈。
