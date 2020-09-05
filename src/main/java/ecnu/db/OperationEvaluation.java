package ecnu.db;

import ecnu.db.constraintchain.filter.SelectResult;
import ecnu.db.parser.SelectOperatorInfoLexer;
import ecnu.db.parser.SelectOperatorInfoParser;
import java_cup.runtime.ComplexSymbolFactory;

import java.io.StringReader;

public class OperationEvaluation {
    public static SelectResult getEvaluation(String expression) throws Exception {
        return new SelectOperatorInfoParser(new SelectOperatorInfoLexer(new StringReader("")),
                new ComplexSymbolFactory()).parseSelectOperatorInfo(expression);
    }
}
