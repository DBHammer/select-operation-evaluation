package ecnu.db.parser;

import ecnu.db.ColumnNotSetException;
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
        SelectResult result = parser.parseSelectOperatorInfo(testCase);
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
        SelectResult result = parser.parseSelectOperatorInfo(testCase);
        result.getColumn("table1.col1").setValue("ahelloa");
        assertTrue(result.getCondition().evaluate());
    }

    @Test
    public void testBetween() throws Exception {
        String testCase = "table1.col1 BETWEEN '1' and '3'";
        SelectResult result = parser.parseSelectOperatorInfo(testCase);
        result.getColumn("table1.col1").setValue(2);
        assertTrue(result.getCondition().evaluate());
    }

    @Test
    public void testIsNull() throws Exception {
        String testCase = "isnull table1.col1";
        SelectResult result = parser.parseSelectOperatorInfo(testCase);
        result.getColumn("table1.col1").setValue(null);
        assertTrue(result.getCondition().evaluate());
        result.getColumn("table1.col1").setValue(1);
        assertFalse(result.getCondition().evaluate());
    }

    @Test
    public void testIn() throws Exception {
        String testCase = "table1.col1 in ('1', '2', '3')";
        SelectResult result = parser.parseSelectOperatorInfo(testCase);
        result.getColumn("table1.col1").setValue(1);
        assertTrue(result.getCondition().evaluate());
        result.getColumn("table1.col1").setValue(4);
        assertFalse(result.getCondition().evaluate());
    }


}