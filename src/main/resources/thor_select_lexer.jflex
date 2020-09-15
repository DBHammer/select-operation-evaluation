package ecnu.db.parser;

import ecnu.db.constraintchain.Token;
import java_cup.runtime.*;
import ecnu.db.constraintchain.arithmetic.ArithmeticNodeType;
import ecnu.db.constraintchain.filter.operation.CompareOperator;
import java_cup.runtime.ComplexSymbolFactory;
%%

%public
%class ThorSelectOperationLexer
/* throws UnsupportedOperationException */
%yylexthrow{
UnsupportedOperationException
%yylexthrow}

%{
  private StringBuilder str_buff = new StringBuilder();
  private Symbol symbol(int type) {
     return new ComplexSymbolFactory.ComplexSymbol(ThorSelectSymbol.terminalNames[type].toLowerCase(), type,
        new ComplexSymbolFactory.Location(1, yycolumn + 1), new ComplexSymbolFactory.Location(1, yycolumn + 1), null);
  }

  private Symbol symbol(int type, Object value) {
     return new ComplexSymbolFactory.ComplexSymbol(ThorSelectSymbol.terminalNames[type].toLowerCase(), type,
        new ComplexSymbolFactory.Location(1, yycolumn + 1), new ComplexSymbolFactory.Location(1, yycolumn + 1), value);
  }

  public void init() {
    System.out.println("initialized");
  }
%}

%implements ThorSelectSymbol
%line
%column
%state STRING_LITERAL
%unicode
%cupsym ThorSelectSymbol
%cup

/* tokens */
AND_SYM=(and|AND)
OR_SYM=(or|OR)
BET_SYM=(BETWEEN|between)
IN_SYM=(in|IN)
LIKE_SYM=(like|LIKE)
ISNULL_SYM=(isnull|ISNULL)
NOT_SYM=(not|NOT)
WHITE_SPACE_CHAR=[\n\r\ \t\b\012]
SCHEMA_NAME_CHAR=[A-Za-z0-9$_]
COLUMN=(({SCHEMA_NAME_CHAR})+\.({SCHEMA_NAME_CHAR})+)|(({SCHEMA_NAME_CHAR})+)
PARAM='[^\n\r\'\\]+'

%%

<YYINITIAL> {
  /* logical operators */
  {AND_SYM} {
    return symbol(AND);
  }
  {OR_SYM} {
    return symbol(OR);
  }

  /* compare operators */
  {IN_SYM} {
    return symbol(IN);
  }
  {LIKE_SYM} {
    return symbol(LIKE);
  }
  {BET_SYM} {
    return symbol(BETWEEN);
  }
  "<" {
    return symbol(LT);
  }
  ">" {
    return symbol(GT);
  }
  "<=" {
    return symbol(LE);
  }
  ">=" {
    return symbol(GE);
  }
  "=" {
    return symbol(EQ);
  }
  "<>" {
    return symbol(NE);
  }

  /* not operators */
  {NOT_SYM} {
    return symbol(NOT);
  }

  /* isnull operators */
  {ISNULL_SYM} {
    return symbol(ISNULL);
  }

  /* column */
  {COLUMN} {
    return symbol(COLUMN, yytext());
  }

  /* constants */
  {PARAM} {
    String val = yytext();
    return symbol(PARAM, val.substring(1, val.length() - 1));
  }

  /* delimiters */
  ", " {}

  /* white spaces */
  {WHITE_SPACE_CHAR}+ {}

  /* parentheses */
  \( {
     return symbol(LPAREN);
  }
  \) {
     return symbol(RPAREN);
  }
}

<<EOF>>                          { return symbol(EOF); }

. {
   throw new UnsupportedOperationException(String.format("'%s' at line: %d, column: %d", yytext(), yyline + 1, yycolumn + 1));
}

