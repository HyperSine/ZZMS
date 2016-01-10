@echo off
@title Dump
COLOR 8F

set CLASSPATH=.;..\dist\*;..\lib\*;..\*;
java -server -Dpath=..\ tools.export.CashShop
pause