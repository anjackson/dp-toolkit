@echo off
set JAVA_HOME=c:\j2sdk1.4.2_04\jre
set DOM4J_HOME=dom4j-full.jar
set XERCES_HOME=xercesImpl.jar
set XMLAPIS_HOME=xml-apis.jar
set XMLDIFF_HOME=XMLDiff.jar
set SCHEMA_HOME=XMLDiff_schema.xsd

%JAVA_HOME%\bin\java -cp %DOM4J_HOME%;%XMLDIFF_HOME%;%XERCES_HOME%;%XMLAPIS_HOME%;%SCHEMA_HOME% epcc.xmldiff.Main %1 %2 %3 %4 %5



