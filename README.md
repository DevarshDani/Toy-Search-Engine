# Toy-Search-Engine

all.txt - This contains the raw reviews. Each line is a review/document in this projects context. There are total 1248 documents. Documents in the posting list should be indexed from 1 (and not 0) where the first document refers to the first line in all.txt.

stopwords.txt - This corpus contains words in different formats like quote, quote by, clear, clearly and some words in which we are not interested like are, of, the etc.

How to run the project

1) The folder consists of BooleanRetrieval.txt just convert the extension to jar executable file(BooleanRetrieval.jar), src folder consists of source code.

2) Open the command prompt and set the working directory to the folder that has the jar file.

3) Using the jar file, you can enter the below command for running my program.

java -jar BooleanRetrieval.jar PLIST cpu cpu.txt 

java -jar BooleanRetrieval.jar AND error AND report errorreport.txt

java -jar BooleanRetrieval.jar AND-NOT lenovo AND (NOT logitech) ANDNOT.txt

Give appropriate output path file name.

Running the Program in Eclipse:

1) Create a project. Create 2 java class files the main method file should be named BooleanRetrieval.java and other file as DatasetFormatter.java

2) copy the java files present in the src folders.

3) Run the project by right clicking on the project name.

4) Select "Run as" then select "Run Configurations.."

5) Go to Arguments tab and give the arguments. Similar to above path conventions for jar files.

6) Apply changes and click on Run.