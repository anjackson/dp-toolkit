XMLDiff Build Instructions

This document describes how to build XMLDiff from the xmldiff source
using the provided ant build file.

Pre-requisites
==============

The following are required to build XMLDiff:

   * Java 2 Standard Edition (J2SE) SDK v1.4 or above
   * ant v1.5.3 or above
   * dom4j v1.4 or above (v1.4 distributed with XMLDiff)
   * Xerces-J v2.6.2 or above (essential libraries, as distributed with 
     dom4j v1.4, are distributed with XMLDiff)

Build Procedures
================

XMLDiff requires the "dom4j-full.jar" from the dom4j installation, the
"xercesImpl.jar" from the Xerces installation and the "xml-apis.jar"
from the Xerces installation.


To build the "xmldiff.jar" jar
------------------------------
  * ensure you are in the directory above the xmldiff "src" directory

  * if the "dom4j-full.jar", "xercesImpl.jar" and "xml-apis.jar" jars
    are in the current directory, run the following command:

      ant

  * if these jars are located elsewhere, run the following command:

      ant -Ddom4j.dir="<path to dom4j installation>" -Dxerces.dir="<path to xerces installation>" -Dxmlapis.dir="<path to xerces installation>"


To generate Javadoc
-------------------
  * ensure you are in the directory above the xmldiff "src" directory

  * run the following command:

     ant javadoc


To remove all files created by build process
--------------------------------------------
  * ensure you are in the directory above the xmldiff "src" directory
  
  * run the following command:

     ant clean


To get information on the build file
------------------------------------
  * ensure you are in the directory above the xmldiff "src" directory
  
  * run the following command:

     ant -projecthelp

