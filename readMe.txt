1. Configure and run PageRank

Description: This program mainly compute the page ranking value for each job postings. It also compute the tag for the job by analysis the Title attribute.

Execute: The program takes three parameters:
			1) input directory of json files. 
			2) the dictionary file. You can find in pageRank/bin
			3) the size of json files.
		How to run:
			1) compile .java files. Execute the following sentence in command prompt, jar can be find in pageRank/lib
			javac -classpath <path to json-simple-1.1.1.jar>; pageRank.java
			2) cd to .class file
			3) execute the following sentence in command prompt:
			java pageRank <path to json file directory> <path to dictionary> <the size of json files>




2. Configure OODT:

Our OODT is based on the Radix installation 0.7 version.

just copy all folders to your OODT home folder and replace the policy and etc and any other folders in workflow, pge, filemgr and crawler folder.

After you replaced all those folders,
Be sure to change some path variables according to comments in files below:

  /pge/file_concatenator/pge-configs/PGEConfig.xml 

  /workflow/policy/tasks.xml

then you can cd to your [OODT_HOME]/crawler/bin folder
and execute the following command to post json to Solr using OODT

note that you need to change the productPath argument value to your json folder and 
metExtractorConfig argument value to [your oodt home folder]/crawler/extractors/tikaextractor.config

/crawler_launcher --filemgrUrl http://localhost:9000 --operation --launchMetCrawler --clientTransferer org.apache.oodt.cas.filemgr.datatransfer.LocalDataTransferFactory --productPath /Users/jixin/Downloads/subsetJson --metExtractor org.apache.oodt.cas.metadata.extractors.TikaCmdLineMetExtractor --metExtractorConfig /usr/local/oodt/crawler/extractors/tikaextractor.config -ais TriggerPostIngestWorkflow -wm http://localhost:9001



3. Configure solr:
In solr file you can find schema.xml and solrconfig.xml.
You can simply put this two files to solr_home/collection1/conf.
Override the original file.
Then you can run solr.

4. Query process:
Description: This program execute query and retrieve data from solr in json file. 

Execute: The program takes several parameters, the number of them based on query type.
The first parameter is query number, range from 1 to 4.
Here are the details:
Query 1:
1) query number, EX: 1
2) longitude, EX: 23.333
3) latitude, EX: 4.444

Query 2:
1) query number, EX: 2
2) location, EX: Cundinamarca

Query 3:
1) query number, EX: 3
2) location, EX: Cundinamarca

Query 4:
1) query number, EX: 4

How to run:
	1) compile .java files. Execute the following sentence in command prompt, jar can be find in queryProcess/lib
	javac -classpath <path to org.json.jar>; queryProcess.java
	2) cd to .class file
	3) execute the following sentence in command prompt:
	java queryProcess <query number> <one or parameters based on query> 