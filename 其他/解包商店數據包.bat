@echo off
@title Drop Retriever
COLOR 8F

set CLASSPATH=.;..\dist\*;..\lib\*;..\*;
java -server -Dpath=..\ tools.export.ShopAnalyzer
pause