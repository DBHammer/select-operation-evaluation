package ecnu.db.parser;

import ecnu.db.constraintchain.Token;
import java_cup.runtime.*;
import ecnu.db.constraintchain.arithmetic.ArithmeticNodeType;
import ecnu.db.constraintchain.filter.operation.CompareOperator;
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
    return new Token(type, yycolumn+1);
  }

  private Symbol symbol(int type, Object value) {
    return new Token(type, yycolumn+1, value);
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
AND_SYM=((and)|(AND))
OR_SYM=((or)|(OR))
BET_SYM=((BETWEEN)|(between))
IN_SYM=((in)|(IN))
LIKE_SYM=((like)|(LIKE))
ISNULL_SYM=((isnull)|(ISNULL))
NOT_SYM=((not)|(NOT))
DIGIT=[0-9]
WHITE_SPACE_CHAR=[\n\r\ \t\b\012]
SCHEMA_NAME_CHAR=[A-Za-z0-9$_]
COLUMN=(({SCHEMA_NAME_CHAR})+\.({SCHEMA_NAME_CHAR})+)|(({SCHEMA_NAME_CHAR})+)
FLOAT='(0|([1-9]({DIGIT}*)))\.({DIGIT}*)'
INTEGER='(0|[1-9]({DIGIT}*))'
DATETIME='({DIGIT}{4}-{DIGIT}{2}-{DIGIT}{2} {DIGIT}{2}:{DIGIT}{2}:{DIGIT}{2}\.{DIGIT}{3})'

%%

<YYINITIAL> {
  /* logical operators */
  AND_SYM {
    return symbol(AND);
  }
  OR_SYM {
    return symbol(OR);
  }

  /* compare operators */
  IN_SYM {
    return symbol(IN);
  }
  LIKE_SYM {
    return symbol(LIKE);
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
  NOT_SYM {
    return symbol(NOT);
  }

  /* isnull operators */
  ISNULL_SYM {
    return symbol(ISNULL);
  }

  /* column */
  {COLUMN} {
    return symbol(COLUMN, yytext());
  }

  /* constants */
  {DATETIME} {
    return symbol(DATETIME, yytext());
  }
  {FLOAT} {
    String val = yytext();
    return symbol(FLOAT, Float.valueOf(val.substring(1, val.length() - 1)));
  }
  {INTEGER} {
    String val = yytext();
    return symbol(INTEGER, Integer.valueOf(val.substring(1, val.length() - 1)));
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

  /* string start */
  "'" {
    str_buff.setLength(0); yybegin(STRING_LITERAL);
  }

}
<STRING_LITERAL> {
  "'" {
    yybegin(YYINITIAL);
    return symbol(STRING, str_buff.toString());
  }
  [^\n\r\"\\]+                   { str_buff.append( yytext() ); }
  \\t                            { str_buff.append('\t'); }
  \\n                            { str_buff.append('\n'); }
  \\r                            { str_buff.append('\r'); }
  \\\"                           { str_buff.append('\"'); }
  \\                             { str_buff.append('\\'); }
}

<<EOF>>                          { return symbol(EOF); }

. {
   throw new UnsupportedOperationException(String.format("'%s' at line: %d, column: %d", yytext(), yyline + 1, yycolumn + 1));
}

