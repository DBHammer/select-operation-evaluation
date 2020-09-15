package ecnu.db.parser;

import ecnu.db.ColumnNotSetException;
import ecnu.db.constraintchain.arithmetic.value.ColumnNode;
import ecnu.db.constraintchain.filter.SelectResult;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class ThorSelectOperationParserTest {
    private final ThorSelectOperationLexer lexer = new ThorSelectOperationLexer(new StringReader(""));
    private final ThorSelectOperationParser parser = new ThorSelectOperationParser(lexer, new ComplexSymbolFactory());

    @Test
    public void testLexer() throws Exception {
        String testCase = "table1.col1 = '1' and table1.col2 = '2'";
        lexer.yyreset(new StringReader(testCase));
        while(!lexer.yyatEOF()) {
            Symbol symbol = lexer.next_token();
        }
    }

    @Test
    public void testLogical() throws Exception {
        String testCase = "table1.col1 = '1' and table1.col2 = '2' or table1.col3 = '3'";
        SelectResult result = parser.parseSelectOperatorInfo(testCase, null);
        result.getColumn("table1.col1").setValue(2);
        result.getColumn("table1.col3").setValue(3);
        // test short cut case
        assertTrue(result.getCondition().evaluate());
        result.getColumn("table1.col1").setValue(null);
        result.getColumn("table1.col2").setValue(2);
        result.getColumn("table1.col3").setValue(3);
        assertThrows(ColumnNotSetException.class, () -> {
            result.getCondition().evaluate();
        });
    }

    @Test
    public void testLike() throws Exception {
        String testCase = "table1.col1 like '%hello%'";
        SelectResult result = parser.parseSelectOperatorInfo(testCase, null);
        result.getColumn("table1.col1").setValue("ahelloa");
        assertTrue(result.getCondition().evaluate());
    }

    @Test
    public void testBetween() throws Exception {
        String testCase = "table1.col1 BETWEEN '1' and '3'";
        SelectResult result = parser.parseSelectOperatorInfo(testCase, null);
        result.getColumn("table1.col1").setValue(2);
        assertTrue(result.getCondition().evaluate());
    }

    @Test
    public void testIsNull() throws Exception {
        String testCase = "isnull table1.col1";
        SelectResult result = parser.parseSelectOperatorInfo(testCase, null);
        result.getColumn("table1.col1").setValue(null);
        assertTrue(result.getCondition().evaluate());
        result.getColumn("table1.col1").setValue(1);
        assertFalse(result.getCondition().evaluate());
    }

    @Test
    public void testIn() throws Exception {
        String testCase = "table1.col1 in ('1', '2', '3')";
        SelectResult result = parser.parseSelectOperatorInfo(testCase, null);
        result.getColumn("table1.col1").setValue(1);
        assertTrue(result.getCondition().evaluate());
        result.getColumn("table1.col1").setValue(4);
        assertFalse(result.getCondition().evaluate());
    }

    @Test
    public void testThorCase1() throws Exception {
        String testCase = "primarykey0 between '2031639134' and '1223845983'";
        SelectResult result = parser.parseSelectOperatorInfo(testCase, "table5");
        ColumnNode node = result.getColumn("table5.primarykey0");
        if (node != null) {
            node.setValue(664506773);
        }
        assertFalse(result.getCondition().evaluate());
    }

    @Test
    public void testThorCase2() throws Exception {
        String testCase = "not not table13.primarykey1 > '151386696'";
        SelectResult result = parser.parseSelectOperatorInfo(testCase, null);
        ColumnNode node;
        node  = result.getColumn("table13.primarykey0");
        if (node != null) {
            node.setValue(409813489 );
        }
        node = result.getColumn("table13.primarykey1");
        if (node != null) {
            node.setValue(607047326);
        }
        assertTrue(result.getCondition().evaluate());
    }

    @Test
    public void testThorCase3() throws Exception {
        String testCase = "attribute5 >= '1943411926.00000'";
        SelectResult result = parser.parseSelectOperatorInfo(testCase, "table13");
        ColumnNode node;
        node  = result.getColumn("table13.attribute5");
        if (node != null) {
            node.setValue(325728596.94310);
        }
        assertFalse(result.getCondition().evaluate());
    }

    @Test
    public void testThorCase4() throws Exception {
        String testCase = "attribute5 >= '1943411926.00000' and attribute2 = '1010'";
        SelectResult result1 = parser.parseSelectOperatorInfo(testCase, "table13");
        ColumnNode node;
        node  = result1.getColumn("table13.attribute5");
        if (node != null) {
            node.setValue(325728596.94310);
        }
        assertFalse(result1.getCondition().evaluate());
        testCase = "attribute5 >= '1943411926.00000' or attribute2 = '1010'";
        SelectResult result2 = parser.parseSelectOperatorInfo(testCase, "table13");
        node  = result2.getColumn("table13.attribute5");
        if (node != null) {
            node.setValue(325728596.94310);
        }
        assertThrows(ColumnNotSetException.class, () -> {
           result2.getCondition().evaluate();
        });
    }

    @Test
    public void testThorCase5() throws Exception {
        String testCase = "table13.attribute5 >= '1943411926.00000' and table1.attriubte3 = '2212'";
        SelectResult result1 = parser.parseSelectOperatorInfo(testCase, "table13");
        ColumnNode node;
        node  = result1.getColumn("table13.attribute5");
        if (node != null) {
            node.setValue(325728596.94310);
        }
        assertFalse(result1.getCondition().evaluate());
        testCase = "table13.attribute5 >= '1943411926.00000' or table1.attriubte3 = '241753765.00000'";
        SelectResult result2 = parser.parseSelectOperatorInfo(testCase, "table13");
        node  = result2.getColumn("table13.attribute5");
        if (node != null) {
            node.setValue(325728596.94310);
        }
        assertThrows(ColumnNotSetException.class, () -> {
            result2.getCondition().evaluate();
        });
    }

}