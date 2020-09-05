package ecnu.db.constraintchain.filter.operation;

import ecnu.db.constraintchain.arithmetic.value.ColumnNode;
import ecnu.db.constraintchain.filter.BoolExprType;
import ecnu.db.constraintchain.filter.Parameter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.stream.Collectors;

import static ecnu.db.constraintchain.filter.operation.CompareOperator.LE;

/**
 * @author wangqingshuai
 */
public class UniVarFilterOperation extends AbstractFilterOperation {
    private static final DateTimeFormatter FMT = new DateTimeFormatterBuilder()
            .appendOptional(new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
                    .toFormatter())
            .appendOptional(
                    new DateTimeFormatterBuilder()
                            .appendPattern("yyyy-MM-dd")
                            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                            .toFormatter())
            .toFormatter();

    protected ColumnNode columnNode;
    private boolean hasNot = false;

    public UniVarFilterOperation(ColumnNode columnNode, CompareOperator operator) {
        super(operator);
        this.columnNode = columnNode;
    }

    @Override
    public BoolExprType getType() {
        return null;
    }

    public void setHasNot(boolean hasNot) {
        this.hasNot = hasNot;
    }

    @Override
    public boolean evaluate() {
        Object value = columnNode.getValue();
        if (value == null) {
            throw new UnsupportedOperationException();
        }
        if (value instanceof String) {
            return evaluateString((String) value);
        }
        else if (value instanceof LocalDateTime) {
            return evaluateDateTime((LocalDateTime) value);
        }
        else if (value instanceof Double) {
            return evaluateDouble((Double) value);
        }
        else if (value instanceof Integer) {
            return evaluateInteger((Integer) value);
        }
        else {
            throw new UnsupportedOperationException("unknown column value type");
        }
    }

    private boolean evaluateString(String value) {
        switch (operator) {
            case EQ:
                return parameters.get(0).getData().equals(value);
            case NE:
                return !parameters.get(0).getData().equals(value);
            case IN:
                boolean ret = parameters.stream().map(Parameter::getData).collect(Collectors.toList()).contains(value);
                if (hasNot) {
                    ret = !ret;
                }
                return ret;
            case LIKE:
                String pattern = parameters.get(0).getData();
                if (pattern.startsWith("%") && pattern.endsWith("%")) {
                    ret = value.contains(pattern.substring(1, pattern.length() - 1));
                }
                else if (pattern.startsWith("%")) {
                    ret = value.endsWith(pattern.substring(1));
                }
                else if (pattern.endsWith("%")) {
                    ret = value.startsWith(pattern.substring(0, pattern.length() - 1));
                }
                else {
                    ret = pattern.equals(value);
                }
                if (hasNot) {
                    ret = !ret;
                }
                return ret;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private boolean evaluateDateTime(LocalDateTime value) {
        switch (operator) {
            case IN:
                boolean ret = parameters.stream()
                        .map((param) -> LocalDateTime.parse(param.getData(), FMT))
                        .collect(Collectors.toList()).contains(value);
                if (hasNot) {
                    ret = !ret;
                }
                return ret;
            case GT:
                LocalDateTime dateTime = LocalDateTime.parse(parameters.get(0).getData(), FMT);
                return value.compareTo(dateTime) > 0;
            case GE:
                dateTime = LocalDateTime.parse(parameters.get(0).getData(), FMT);
                return value.compareTo(dateTime) >= 0;
            case LT:
                dateTime = LocalDateTime.parse(parameters.get(0).getData(), FMT);
                return value.compareTo(dateTime) < 0;
            case LE:
                dateTime = LocalDateTime.parse(parameters.get(0).getData(), FMT);
                return value.compareTo(dateTime) <= 0;
            case EQ:
                dateTime = LocalDateTime.parse(parameters.get(0).getData(), FMT);
                return value.compareTo(dateTime) == 0;
            case NE:
                dateTime = LocalDateTime.parse(parameters.get(0).getData(), FMT);
                return value.compareTo(dateTime) != 0;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private boolean evaluateDouble(Double value) {
        switch (operator) {
            case IN:
                boolean ret = parameters.stream()
                        .map((param) -> Double.parseDouble(param.getData()))
                        .collect(Collectors.toList()).contains(value);
                if (hasNot) {
                    ret = !ret;
                }
                return ret;
            case GT:
                double db = Double.parseDouble(parameters.get(0).getData());
                return value.compareTo(db) > 0;
            case GE:
                db = Double.parseDouble(parameters.get(0).getData());
                return value.compareTo(db) >= 0;
            case LT:
                db = Double.parseDouble(parameters.get(0).getData());
                return value.compareTo(db) < 0;
            case LE:
                db = Double.parseDouble(parameters.get(0).getData());
                return value.compareTo(db) <= 0;
            case EQ:
                db = Double.parseDouble(parameters.get(0).getData());
                return value.compareTo(db) == 0;
            case NE:
                db = Double.parseDouble(parameters.get(0).getData());
                return value.compareTo(db) != 0;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private boolean evaluateInteger(Integer value) {
        switch (operator) {
            case IN:
                boolean ret = parameters.stream()
                        .map((param) -> Integer.parseInt(param.getData()))
                        .collect(Collectors.toList()).contains(value);
                if (hasNot) {
                    ret = !ret;
                }
                return ret;
            case GT:
                int integer = Integer.parseInt(parameters.get(0).getData());
                return value.compareTo(integer) > 0;
            case GE:
                integer = Integer.parseInt(parameters.get(0).getData());
                return value.compareTo(integer) >= 0;
            case LT:
                integer = Integer.parseInt(parameters.get(0).getData());
                return value.compareTo(integer) < 0;
            case LE:
                integer = Integer.parseInt(parameters.get(0).getData());
                return value.compareTo(integer) <= 0;
            case EQ:
                integer = Integer.parseInt(parameters.get(0).getData());
                return value.compareTo(integer) == 0;
            case NE:
                integer = Integer.parseInt(parameters.get(0).getData());
                return value.compareTo(integer) != 0;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        List<String> content = parameters.stream().map(Parameter::toString).collect(Collectors.toList());
        content.add(0, String.format("%s", columnNode));
        if (hasNot) {
            return String.format("not(%s(%s))", operator.toString().toLowerCase(), String.join(", ", content));
        }
        return String.format("%s(%s)", operator.toString().toLowerCase(), String.join(", ", content));
    }
}
