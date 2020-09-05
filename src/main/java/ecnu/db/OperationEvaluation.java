package ecnu.db;

import ecnu.db.constraintchain.filter.SelectResult;
import ecnu.db.parser.SelectOperationLexer;
import ecnu.db.parser.SelectOperationParser;
import java_cup.runtime.ComplexSymbolFactory;

import java.io.StringReader;

public class OperationEvaluation {
    public static SelectResult getEvaluation(String expression) throws Exception {
        return new SelectOperationParser(new SelectOperationLexer(new StringReader("")),
                new ComplexSymbolFactory()).parseSelectOperatorInfo(expression);
    }
}
