package ecnu.db.tidb.parser;

import ecnu.db.constraintchain.filter.SelectResult;
import ecnu.db.constraintchain.filter.logical.AndNode;
import java_cup.runtime.ComplexSymbolFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class TidbSelectOperatorInfoParserTest {
    private final TidbSelectOperatorInfoLexer lexer = new TidbSelectOperatorInfoLexer(new StringReader(""));
    private final TidbSelectOperatorInfoParser parser = new TidbSelectOperatorInfoParser(lexer, new ComplexSymbolFactory());

    @DisplayName("test TidbSelectOperatorInfoParser.parse method with arithmetic ops")
    @Test
    void testParseWithArithmeticOps() throws Exception {
        String testCase = "ge(mul(db.table.col1, plus(db.table.col2, 3)), 2)";
        SelectResult result = parser.parseSelectOperatorInfo(testCase);
        result.getColumn("db.table.col1").setValue(3);
        result.getColumn("db.table.col2").setValue(2);
        assertTrue(result.getCondition().evaluate());
        result.getColumn("db.table.col1").setValue(2);
        result.getColumn("db.table.col2").setValue(-2);
        assertTrue(result.getCondition().evaluate());
    }

    @DisplayName("test TidbSelectOperatorInfoParser.parse method with logical ops")
    @Test
    void testParseWithLogicalOps() throws Exception {
        String testCase = "or(ge(db.table.col1, 2), lt(db.table.col4, 3.0))";
        SelectResult result = parser.parseSelectOperatorInfo(testCase);
        result.getColumn("db.table.col1").setValue(3.0);
        result.getColumn("db.table.col4").setValue(4.0);
        assertTrue(result.getCondition().evaluate());
    }

    @DisplayName("test TidbSelectOperatorInfoParser.parse method with erroneous grammar")
    @Test()
    void testParseWithLogicalOpsFailed() {
        assertThrows(Exception.class, () -> {
            String testCase = "or(ge((db.table.col1), 2), mul(db.table.col2, 3))";
            parser.parseSelectOperatorInfo(testCase);
        });
    }

    @DisplayName("test TidbSelectOperatorInfoParser.parse method with not")
    @Test()
    void testParseWithNot() throws Exception {
        String testCase = "or(ge(db.table.col1, 2), not(in(db.table.col3, \"3\", \"2\")))";
        SelectResult result = parser.parseSelectOperatorInfo(testCase);
        result.getColumn("db.table.col1").setValue(1);
        result.getColumn("db.table.col3").setValue("5");
        assertTrue(result.getCondition().evaluate());
    }

    @DisplayName("test TidbSelectOperatorInfoParser.parse method with isnull")
    @Test()
    void testParseWithIsnull() throws Exception {
        String testCase = "or(ge(db.table.col1, 2), not(isnull(db.table.col2)))";
        SelectResult result = parser.parseSelectOperatorInfo(testCase);
        result.getColumn("db.table.col1").setValue(1);
        result.getColumn("db.table.col2").setValue(null);
        assertFalse(result.getCondition().evaluate());
    }

    @DisplayName("test TidbSelectOperatorInfoParser.parse method with like")
    @Test()
    void testParseLike() throws Exception {
        String testCase = "like(db.table.col1, \"%hello\", 92)";
        SelectResult result = parser.parseSelectOperatorInfo(testCase);
        result.getColumn("db.table.col1").setValue("zzzhello");
        assertTrue(result.getCondition().evaluate());
        result.getColumn("db.table.col1").setValue("ello");
        assertFalse(result.getCondition().evaluate());
    }
}