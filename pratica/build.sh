#!/bin/sh
# Gera e compila o verificador semântico da linguagem Mini
set -e
jflex -q mini.flex                  # -> Yylex.java
byaccj -J -Jnorun mini.y            # -> Parser.java, ParserVal.java  (main está em mini.y)
javac -encoding UTF-8 -Xlint:none *.java
echo "ok. Uso: java Parser [-v] exemplos/ok.mini"
