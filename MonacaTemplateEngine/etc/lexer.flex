package mobi.monaca.framework.template;

import java_cup.runtime.*;

%%
// This file is acceptable for jflex.

%class TemplateLexer
%unicode
%line
// %debug
%char
%public
%cup

%init{
    yybegin(RAW);
%init}

%eofval{
    return new Symbol(Symbols.EOF);
%eofval}

%{
    protected String templatePath = "";

    public TemplateLexer(java.io.Reader in, String templatePath) {
        this(in);
        this.templatePath = templatePath;
    }

    public String getTemplatePath() {
        return templatePath;
    }
  
    private Symbol symbol(int sym) {
        return new Symbol(sym, yyline + 1, yycolumn + 1);
    }
    
    private <T> Symbol symbol(int sym, T val) {
        return new Symbol(sym, yyline + 1, yycolumn + 1, new Token<T>(sym, yyline + 1, val));
    }
    
    protected void error(String message) {
        throw new LexerError(message, templatePath, yyline + 1);
    }
%}

%state RAW, STATEMENT, COMMENT, ECHO

%%

<RAW> {
  ([^{]|\{[^#%{])+ { return symbol(Symbols.T_RAW, yytext()); }
  "{#"            { yybegin(COMMENT); }
  "{%"            { yybegin(STATEMENT); }
  "{{"            { yybegin(ECHO); return symbol(Symbols.T_ECHO); }
}

<STATEMENT> {
  "extends"  { return symbol(Symbols.T_EXTENDS); }
  "parent"   { return symbol(Symbols.T_PARENT); }
  "include"  { return symbol(Symbols.T_INCLUDE); }
  "block"    { return symbol(Symbols.T_BLOCK); }
  "endblock" { return symbol(Symbols.T_ENDBLOCK); }
  "end"      { return symbol(Symbols.T_END); }
  "if"       { return symbol(Symbols.T_IF); }
  "elseif"   { return symbol(Symbols.T_ELSEIF); }
  "else"     { return symbol(Symbols.T_ELSE); }
  "endif"    { return symbol(Symbols.T_ENDIF); }

  "%}" { yybegin(RAW); }
}

<ECHO> {
  "|"  { return symbol(Symbols.T_PIPE); }
  "}}" { yybegin(RAW); }
}

<STATEMENT, ECHO> {

  "and" { return symbol(Symbols.T_AND); }
  "or" { return symbol(Symbols.T_OR); }
  "not" { return symbol(Symbols.T_NOT); }

  [a-zA-Z_][a-zA-Z0-9_]* { return symbol(Symbols.T_ID, yytext()); } 

  [\r\n \t]+ { } 

  "(" { return symbol(Symbols.T_OPENPAREN); }
  ")" { return symbol(Symbols.T_CLOSEPAREN); }
  "." { return symbol(Symbols.T_DOT); }
  "==" { return symbol(Symbols.T_EQUAL); }
  "!=" { return symbol(Symbols.T_NOTEQUAL); }

  \/\*([^*]|\*[^/])*\*\/ { }

  \"([^\"]|\\.)*\"  { return symbol(Symbols.T_STRING, StringUtil.normalizeDoubleQuoteString(yytext())); }
  '([^']|\\['\\])*' { return symbol(Symbols.T_STRING, StringUtil.normalizeSingleQuoteString(yytext())); }

}

<STATEMENT> {
  <<EOF>> { error("Lexer closing on STATEMENT state"); }
}

<ECHO> {
  <<EOF>> { error("Lexer closing on ECHO state"); }
}

<STATEMENT, ECHO> {
  . { error("Lexer recognition fail"); }
}

<COMMENT> {
  <<EOF>> { error("Lexer closing on COMMENT state"); }

  "#}" { yybegin(RAW); }

  .|\r|\n { } 
}
