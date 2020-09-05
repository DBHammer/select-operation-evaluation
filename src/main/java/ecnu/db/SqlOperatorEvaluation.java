package ecnu.db;

import ecnu.db.constraintchain.filter.SelectResult;
import ecnu.db.tidb.parser.TidbSelectOperatorInfoLexer;
import ecnu.db.tidb.parser.TidbSelectOperatorInfoParser;
import java_cup.runtime.ComplexSymbolFactory;

import java.io.StringReader;

public class SqlOperatorEvaluation {
    public static SelectResult getEvaluation(String expression) throws Exception {
        return new TidbSelectOperatorInfoParser(new TidbSelectOperatorInfoLexer(new StringReader("")),
                new ComplexSymbolFactory()).parseSelectOperatorInfo(expression);
    }
}
