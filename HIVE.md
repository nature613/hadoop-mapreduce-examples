# Session 5 - Apache Hive - Beeline Client

## 1.1. Creating a Connection to the Beeline Client

First, we connect via SSH to our edge node on the HDFS cluster of the school.

```bash
$ ssh istepanian@hadoop-edge01.efrei.online

Welcome to EFREI Hadoop Cluster

Password:
Last login: Sat Nov 14 14:50:12 2020 from XX-XXX-XX-XX.XXX.XXXXXXX.XXX

-sh-4.2$
```
Then we can immediatly connect to the Hive Client using the command `beeline` (the Kerberos ticket is still in memory).

```bash
-sh-4.2$ beeline

SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hive/lib/log4j-slf4j-impl-2.10.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hadoop/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]
Connecting to jdbc:hive2://hadoop-master01.efrei.online:2181,hadoop-master02.efrei.online:2181,hadoop-master03.efrei.online:2181/default;httpPath=cliservice;principal=hive/_HOST@EFREI.ONLINE;serviceDiscoveryMode=zooKeeper;ssl=true;transportMode=http;zooKeeperNamespace=hiveserver2
20/11/14 20:58:09 [main]: INFO jdbc.HiveConnection: Connected to hadoop-master02.efrei.online:10001
Connected to: Apache Hive (version 3.1.0.3.1.5.0-152)
Driver: Hive JDBC (version 3.1.0.3.1.5.0-152)
Transaction isolation: TRANSACTION_REPEATABLE_READ
Beeline version 3.1.0.3.1.5.0-152 by Apache Hive

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

We are automatically connected to our school's master nodes thank to the bindings in the logger.

The list of `beeline` commands can be checked using the `!help` command:

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> !help

!addlocaldriverjar  Add driver jar file in the beeline client side.
!addlocaldrivername Add driver name that needs to be supported in the beeline
                    client side.
!all                Execute the specified SQL against all the current connections
!autocommit         Set autocommit mode on or off
!batch              Start or execute a batch of statements
!brief              Set verbose mode off
!call               Execute a callable statement
!close              Close the current connection to the database

...
!list               List the current connections
...

typeinfo           Display the type map for the current connection
!verbose            Set verbose mode on

Comments, bug reports, and patches go to ???
```

We can then see the current connections from our client by using the command `!list`:

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> !list

1 active connection:
 #0  open     jdbc:hive2://hadoop-master01.efrei.online:2181,hadoop-master02.efrei.online:2181,hadoop-master03.efrei.online:2181/default;httpPath=cliservice;principal=hive/_HOST@EFREI.ONLINE;serviceDiscoveryMode=zooKeeper;ssl=true;transportMode=http;zooKeeperNamespace=hiveserver2

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

We can see the connection that we are currently using with some extra meta-data; even though our current connection (prefixed in the command-line) displays `0: jdbc:hive2://hadoop-master01.efrei.online:`, we are actually connected to three Hadoop master nodes (`hadoop-master01.efrei.online`, `hadoop-master02.efrei.online`, `hadoop-master03.efrei.online`), on the port 2181.

To list all the databases, we can use the HiveQL query `SHOW DATABASES;`:

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> SHOW DATABASES;

INFO  : Compiling command(queryId=hive_20201114215216_af42c506-309b-41e6-87b6-a5aaf35bc2b3): SHOW DATABASES
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:database_name, type:string, comment:from deserializer)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201114215216_af42c506-309b-41e6-87b6-a5aaf35bc2b3); Time taken: 0.079 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201114215216_af42c506-309b-41e6-87b6-a5aaf35bc2b3): SHOW DATABASES
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201114215216_af42c506-309b-41e6-87b6-a5aaf35bc2b3); Time taken: 0.015 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+--------------------+
|   database_name    |
+--------------------+
| aelquarati         |
| agoncalves         |
| agoubeau           |
| aledeuf            |
...
| hel_brsm           |
| idelaronciere      |
| jly                |
...
| ttea               |
| vgonnot            |
| vserena            |
+--------------------+
58 rows selected (0.224 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

Our database doesn't currently exist, we can create it using the HiveQL query `CREATE DATABASE istepanian;`

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> CREATE DATABASE istepanian;

INFO  : Compiling command(queryId=hive_20201115195814_e9a5c47c-d15a-41d8-9342-203d90926620): CREATE DATABASE istepanian
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115195814_e9a5c47c-d15a-41d8-9342-203d90926620); Time taken: 0.001 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115195814_e9a5c47c-d15a-41d8-9342-203d90926620): CREATE DATABASE istepanian
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115195814_e9a5c47c-d15a-41d8-9342-203d90926620); Time taken: 0.049 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (0.067 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

We aren't automatically redirected to the newly created database. To select it, we use the command `USE`:

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> USE istepanian;

INFO  : Compiling command(queryId=hive_20201115200335_0b9991fd-09a9-4bca-9091-76d8e41af180): USE istepanian
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115200335_0b9991fd-09a9-4bca-9091-76d8e41af180); Time taken: 0.139 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115200335_0b9991fd-09a9-4bca-9091-76d8e41af180): USE istepanian
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115200335_0b9991fd-09a9-4bca-9091-76d8e41af180); Time taken: 0.014 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (0.163 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

There are currently no tables in our database, let us create a test table called `temp`:

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> SHOW TABLES;

INFO  : Compiling command(queryId=hive_20201115200502_5a5c7c08-d9e8-4765-bde1-d9abfe4f5bd8): SHOW TABLES
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:tab_name, type:string, comment:from deserializer)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115200502_5a5c7c08-d9e8-4765-bde1-d9abfe4f5bd8); Time taken: 0.01 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115200502_5a5c7c08-d9e8-4765-bde1-d9abfe4f5bd8): SHOW TABLES
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115200502_5a5c7c08-d9e8-4765-bde1-d9abfe4f5bd8); Time taken: 0.011 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------+
| tab_name  |
+-----------+
+-----------+
No rows selected (0.045 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> CREATE TABLE temp(col string);

INFO  : Compiling command(queryId=hive_20201115201249_58c98176-d352-4fa5-8d5c-52e43b1039b7): CREATE TABLE temp(col string)
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115201249_58c98176-d352-4fa5-8d5c-52e43b1039b7); Time taken: 0.123 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115201249_58c98176-d352-4fa5-8d5c-52e43b1039b7): CREATE TABLE temp(col string)
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115201249_58c98176-d352-4fa5-8d5c-52e43b1039b7); Time taken: 0.058 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (0.196 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

We can view our new table's info with the commands `SHOW COLUMNS FROM temp;`, `SHOW TBLPROPERTIES temp;`, `SHOW TABLE EXTENDED LIKE temp;` or `DESCRIBE temp;`:

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> SHOW COLUMNS FROM temp;

INFO  : Compiling command(queryId=hive_20201115203522_8ec5e20a-2e3d-48bd-af81-5ce2a91ab22f): SHOW COLUMNS FROM temp
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:field, type:string, comment:from deserializer)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115203522_8ec5e20a-2e3d-48bd-af81-5ce2a91ab22f); Time taken: 0.021 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115203522_8ec5e20a-2e3d-48bd-af81-5ce2a91ab22f): SHOW COLUMNS FROM temp
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115203522_8ec5e20a-2e3d-48bd-af81-5ce2a91ab22f); Time taken: 0.009 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+--------+
| field  |
+--------+
| col    |
+--------+
1 row selected (0.075 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SHOW TBLPROPERTIES temp;

INFO  : Compiling command(queryId=hive_20201115203545_251321c9-29fd-4244-b5eb-695882da5dbb): SHOW TBLPROPERTIES temp
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:prpt_name, type:string, comment:from deserializer), FieldSchema(name:prpt_value, type:string, comment:from deserializer)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115203545_251321c9-29fd-4244-b5eb-695882da5dbb); Time taken: 0.034 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115203545_251321c9-29fd-4244-b5eb-695882da5dbb): SHOW TBLPROPERTIES temp
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115203545_251321c9-29fd-4244-b5eb-695882da5dbb); Time taken: 0.028 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-------------------------+----------------------------------------------------+
|        prpt_name        |                     prpt_value                     |
+-------------------------+----------------------------------------------------+
| COLUMN_STATS_ACCURATE   | {"BASIC_STATS":"true","COLUMN_STATS":{"col":"true"}} |
| EXTERNAL                | TRUE                                               |
| TRANSLATED_TO_EXTERNAL  | TRUE                                               |
| bucketing_version       | 2                                                  |
| external.table.purge    | TRUE                                               |
| numFiles                | 0                                                  |
| numRows                 | 0                                                  |
| rawDataSize             | 0                                                  |
| totalSize               | 0                                                  |
| transient_lastDdlTime   | 1605467569                                         |
+-------------------------+----------------------------------------------------+
10 rows selected (0.124 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SHOW TABLE EXTENDED LIKE temp;
INFO  : Compiling command(queryId=hive_20201115203719_0ebb6f14-4a0d-4d5d-adf8-b6b346584cf9): SHOW TABLE EXTENDED LIKE temp
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:tab_name, type:string, comment:from deserializer)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115203719_0ebb6f14-4a0d-4d5d-adf8-b6b346584cf9); Time taken: 0.003 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115203719_0ebb6f14-4a0d-4d5d-adf8-b6b346584cf9): SHOW TABLE EXTENDED LIKE temp
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115203719_0ebb6f14-4a0d-4d5d-adf8-b6b346584cf9); Time taken: 0.019 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+----------------------------------------------------+
|                      tab_name                      |
+----------------------------------------------------+
| tableName:temp                                     |
| owner:istepanian                                   |
| location:hdfs://efrei/warehouse/tablespace/external/hive/istepanian.db/temp |
| inputformat:org.apache.hadoop.mapred.TextInputFormat |
| outputformat:org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat |
| columns:struct columns { string col}               |
| partitioned:false                                  |
| partitionColumns:                                  |
| totalNumberFiles:0                                 |
| totalFileSize:0                                    |
| maxFileSize:0                                      |
| minFileSize:0                                      |
| lastAccessTime:0                                   |
| lastUpdateTime:1605467569505                       |
|                                                    |
+----------------------------------------------------+
15 rows selected (0.05 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> DESCRIBE EXTENDED temp;
INFO  : Compiling command(queryId=hive_20201115203946_60f383fe-f404-4244-80a0-7cf98230f611): DESCRIBE EXTENDED temp
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:col_name, type:string, comment:from deserializer), FieldSchema(name:data_type, type:string, comment:from deserializer), FieldSchema(name:comment, type:string, comment:from deserializer)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115203946_60f383fe-f404-4244-80a0-7cf98230f611); Time taken: 0.031 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115203946_60f383fe-f404-4244-80a0-7cf98230f611): DESCRIBE EXTENDED temp
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115203946_60f383fe-f404-4244-80a0-7cf98230f611); Time taken: 0.06 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------------------+----------------------------------------------------+----------+
|          col_name           |                     data_type                      | comment  |
+-----------------------------+----------------------------------------------------+----------+
| col                         | string                                             |          |
|                             | NULL                                               | NULL     |
| Detailed Table Information  | Table(tableName:temp, dbName:istepanian, owner:istepanian, createTime:1605467569, lastAccessTime:0, retention:0, sd:StorageDescriptor(cols:[FieldSchema(name:col, type:string, comment:null)], location:hdfs://efrei/warehouse/tablespace/external/hive/istepanian.db/temp, inputFormat:org.apache.hadoop.mapred.TextInputFormat, outputFormat:org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat, compressed:false, numBuckets:-1, serdeInfo:SerDeInfo(name:null, serializationLib:org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe, parameters:{serialization.format=1}), bucketCols:[], sortCols:[], parameters:{}, skewedInfo:SkewedInfo(skewedColNames:[], skewedColValues:[], skewedColValueLocationMaps:{}), storedAsSubDirectories:false), partitionKeys:[], parameters:{external.table.purge=TRUE, totalSize=0, numRows=0, rawDataSize=0, EXTERNAL=TRUE, COLUMN_STATS_ACCURATE={\"BASIC_STATS\":\"true\",\"COLUMN_STATS\":{\"col\":\"true\"}}, numFiles=0, transient_lastDdlTime=1605467569, TRANSLATED_TO_EXTERNAL=TRUE, bucketing_version=2}, viewOriginalText:null, viewExpandedText:null, tableType:EXTERNAL_TABLE, rewriteEnabled:false, catName:hive, ownerType:USER, writeId:0, accessType:8) |          |
+-----------------------------+----------------------------------------------------+----------+
3 rows selected (0.146 seconds)
```

Now we can remove the test table using `DROP TABLE temp;`:

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> DROP TABLE temp;

INFO  : Compiling command(queryId=hive_20201115204254_bc2cfd0a-1ce8-4650-a55b-37a081eb5739): DROP TABLE temp
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115204254_bc2cfd0a-1ce8-4650-a55b-37a081eb5739); Time taken: 0.043 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115204254_bc2cfd0a-1ce8-4650-a55b-37a081eb5739): DROP TABLE temp
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115204254_bc2cfd0a-1ce8-4650-a55b-37a081eb5739); Time taken: 0.237 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (0.296 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> !quit

Closing: 0: jdbc:hive2://hadoop-master01.efrei.online:2181,hadoop-master02.efrei.online:2181,hadoop-master03.efrei.online:2181/default;httpPath=cliservice;principal=hive/_HOST@EFREI.ONLINE;serviceDiscoveryMode=zooKeeper;ssl=true;transportMode=http;zooKeeperNamespace=hiveserver2

-sh-4.2$
```

## 1.2. Create tables

We will first create an external table using the `CREATE EXTERNAL TABLE trees_external(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/table';`, after creating a new folder to contain it in our Hadoop File System.

```bash
-sh-4.2$ beeline

SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hive/lib/log4j-slf4j-impl-2.10.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hadoop/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]
Connecting to jdbc:hive2://hadoop-master01.efrei.online:2181,hadoop-master02.efrei.online:2181,hadoop-master03.efrei.online:2181/default;httpPath=cliservice;principal=hive/_HOST@EFREI.ONLINE;serviceDiscoveryMode=zooKeeper;ssl=true;transportMode=http;zooKeeperNamespace=hiveserver2
20/11/15 20:55:47 [main]: INFO jdbc.HiveConnection: Connected to hadoop-master03.efrei.online:10001
Connected to: Apache Hive (version 3.1.0.3.1.5.0-152)
Driver: Hive JDBC (version 3.1.0.3.1.5.0-152)
Transaction isolation: TRANSACTION_REPEATABLE_READ
Beeline version 3.1.0.3.1.5.0-152 by Apache Hive

0: jdbc:hive2://hadoop-master01.efrei.online:> USE istepanian;

INFO  : Compiling command(queryId=hive_20201115215844_b8881d56-b1d2-4531-b30c-88dd200cb371): USE istepanian
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115215844_b8881d56-b1d2-4531-b30c-88dd200cb371); Time taken: 0.013 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115215844_b8881d56-b1d2-4531-b30c-88dd200cb371): USE istepanian
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115215844_b8881d56-b1d2-4531-b30c-88dd200cb371); Time taken: 0.007 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (0.045 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> !sh hdfs dfs -mkdir trees_table

SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hive/lib/log4j-slf4j-impl-2.10.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hadoop/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]

0: jdbc:hive2://hadoop-master01.efrei.online:> CREATE EXTERNAL TABLE trees_external(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1");

INFO  : Compiling command(queryId=hive_20201115212404_1a7f67c7-eee9-4426-b6e4-73373679917a): CREATE EXTERNAL TABLE trees_external(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1")
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115212404_1a7f67c7-eee9-4426-b6e4-73373679917a); Time taken: 0.039 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115212404_1a7f67c7-eee9-4426-b6e4-73373679917a): CREATE EXTERNAL TABLE trees_external(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1")
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115212404_1a7f67c7-eee9-4426-b6e4-73373679917a); Time taken: 0.072 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (0.125 seconds)
0: jdbc:hive2://hadoop-master01.efrei.online:> SHOW TABLES;
INFO  : Compiling command(queryId=hive_20201115212408_0548d694-f96e-48e7-9eff-3d2a52952219): SHOW TABLES
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:tab_name, type:string, comment:from deserializer)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115212408_0548d694-f96e-48e7-9eff-3d2a52952219); Time taken: 0.009 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115212408_0548d694-f96e-48e7-9eff-3d2a52952219): SHOW TABLES
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115212408_0548d694-f96e-48e7-9eff-3d2a52952219); Time taken: 0.013 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------+
|    tab_name     |
+-----------------+
| trees_external  |
+-----------------+
1 row selected (0.054 seconds)
0: jdbc:hive2://hadoop-master01.efrei.online:>
```

We add the `TBLPROPERTIES ("skip.header.line.count"="1")` part to the `CREATE TABLE` query to ensure that the CSV header (the column names) will be skipped when the data is loaded. We can now load the data into our external table using `LOAD DATA`.

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> LOAD DATA INPATH "/user/istepanian/trees.csv" OVERWRITE INTO TABLE trees_external;

INFO  : Compiling command(queryId=hive_20201115212828_af80db19-f1a7-422e-93f5-70a51e9fd17e): LOAD DATA INPATH "/user/istepanian/trees.csv" OVERWRITE INTO TABLE trees_external
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115212828_af80db19-f1a7-422e-93f5-70a51e9fd17e); Time taken: 0.047 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115212828_af80db19-f1a7-422e-93f5-70a51e9fd17e): LOAD DATA INPATH "/user/istepanian/trees.csv" OVERWRITE INTO TABLE trees_external
INFO  : Starting task [Stage-0:MOVE] in serial mode
INFO  : Loading data to table istepanian.trees_external from hdfs://efrei/user/istepanian/trees.csv
INFO  : Starting task [Stage-1:STATS] in serial mode
INFO  : Completed executing command(queryId=hive_20201115212828_af80db19-f1a7-422e-93f5-70a51e9fd17e); Time taken: 0.262 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (0.326 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT * FROM TREES_EXTERNAL LIMIT 5;

INFO  : Compiling command(queryId=hive_20201115213055_a9ad6974-4acd-49bb-bb74-e0d6cf104584): SELECT * FROM TREES_EXTERNAL LIMIT 5
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:trees_external.geopoint, type:string, comment:null), FieldSchema(name:trees_external.arrondissement, type:int, comment:null), FieldSchema(name:trees_external.genre, type:string, comment:null), FieldSchema(name:trees_external.espece, type:string, comment:null), FieldSchema(name:trees_external.famille, type:string, comment:null), FieldSchema(name:trees_external.anneeplantation, type:int, comment:null), FieldSchema(name:trees_external.hauteur, type:float, comment:null), FieldSchema(name:trees_external.circonference, type:float, comment:null), FieldSchema(name:trees_external.adresse, type:string, comment:null), FieldSchema(name:trees_external.nomcommun, type:string, comment:null), FieldSchema(name:trees_external.variete, type:string, comment:null), FieldSchema(name:trees_external.objectid, type:int, comment:null), FieldSchema(name:trees_external.nom_ev, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115213055_a9ad6974-4acd-49bb-bb74-e0d6cf104584); Time taken: 0.107 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115213055_a9ad6974-4acd-49bb-bb74-e0d6cf104584): SELECT * FROM TREES_EXTERNAL LIMIT 5
INFO  : Completed executing command(queryId=hive_20201115213055_a9ad6974-4acd-49bb-bb74-e0d6cf104584); Time taken: 0.012 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
|     trees_external.geopoint     | trees_external.arrondissement  | trees_external.genre  | trees_external.espece  | trees_external.famille  | trees_external.anneeplantation  | trees_external.hauteur  | trees_external.circonference  |               trees_external.adresse               | trees_external.nomcommun  | trees_external.variete  | trees_external.objectid  |        trees_external.nom_ev         |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
| (48.857140829, 2.29533455314)   | 7                              | Maclura               | pomifera               | Moraceae                | 1935                            | 13.0                    | NULL                          | Quai Branly, avenue de La Motte-Piquet, avenue de la Bourdonnais, avenue de Suffren | Oranger des Osages        |                         | 6                        | Parc du Champs de Mars               |
| (48.8685686134, 2.31331809304)  | 8                              | Calocedrus            | decurrens              | Cupressaceae            | 1854                            | 20.0                    | 195.0                         | Cours-la-Reine, avenue Franklin-D.-Roosevelt, avenue Matignon, avenue Gabriel | Cèdre à encens            |                         | 11                       | Jardin des Champs Elysées            |
| (48.8768191638, 2.33210374339)  | 9                              | Pterocarya            | fraxinifolia           | Juglandaceae            | 1862                            | 22.0                    | 330.0                         | Place d'Estienne-d'Orves                           | Pérocarya du Caucase      |                         | 14                       | Square Etienne d'Orves               |
| (48.8373323894, 2.40776275516)  | 12                             | Celtis                | australis              | Cannabaceae             | 1906                            | 16.0                    | 295.0                         | 27, boulevard Soult                                | Micocoulier de Provence   |                         | 16                       | Avenue 27 boulevard Soult            |
| (48.8341842636, 2.46130493573)  | 12                             | Quercus               | petraea                | Fagaceae                | 1784                            | 30.0                    | 430.0                         | route ronde des Minimes                            | Chêne rouvre              |                         | 19                       | Bois de Vincennes (lac des minimes)  |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
5 rows selected (0.17 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> !sh hdfs dfs -ls trees_table
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hive/lib/log4j-slf4j-impl-2.10.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hadoop/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]
Found 1 items
-rw-r--r--   3 istepanian hdfs      16821 2020-11-12 20:51 trees_table/trees.csv
```

The import worked successfully, and simply added the file to the path of the external table. We can also directly put the file in the external table's folder and it will recognize it as the table's data (if we remove the file, the table will lose its data).

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> !sh hdfs dfs -rm trees_table/trees.csv

SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hive/lib/log4j-slf4j-impl-2.10.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hadoop/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]

0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT * FROM TREES_EXTERNAL LIMIT 5;

INFO  : Compiling command(queryId=hive_20201115213745_cc1bfd2a-b791-4838-acf7-f9393a1a0821): SELECT * FROM TREES_EXTERNAL LIMIT 5
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:trees_external.geopoint, type:string, comment:null), FieldSchema(name:trees_external.arrondissement, type:int, comment:null), FieldSchema(name:trees_external.genre, type:string, comment:null), FieldSchema(name:trees_external.espece, type:string, comment:null), FieldSchema(name:trees_external.famille, type:string, comment:null), FieldSchema(name:trees_external.anneeplantation, type:int, comment:null), FieldSchema(name:trees_external.hauteur, type:float, comment:null), FieldSchema(name:trees_external.circonference, type:float, comment:null), FieldSchema(name:trees_external.adresse, type:string, comment:null), FieldSchema(name:trees_external.nomcommun, type:string, comment:null), FieldSchema(name:trees_external.variete, type:string, comment:null), FieldSchema(name:trees_external.objectid, type:int, comment:null), FieldSchema(name:trees_external.nom_ev, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115213745_cc1bfd2a-b791-4838-acf7-f9393a1a0821); Time taken: 0.182 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115213745_cc1bfd2a-b791-4838-acf7-f9393a1a0821): SELECT * FROM TREES_EXTERNAL LIMIT 5
INFO  : Completed executing command(queryId=hive_20201115213745_cc1bfd2a-b791-4838-acf7-f9393a1a0821); Time taken: 0.003 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+--------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+-------------------------+---------------------------+-------------------------+--------------------------+------------------------+
| trees_external.geopoint  | trees_external.arrondissement  | trees_external.genre  | trees_external.espece  | trees_external.famille  | trees_external.anneeplantation  | trees_external.hauteur  | trees_external.circonference  | trees_external.adresse  | trees_external.nomcommun  | trees_external.variete  | trees_external.objectid  | trees_external.nom_ev  |
+--------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+-------------------------+---------------------------+-------------------------+--------------------------+------------------------+
+--------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+-------------------------+---------------------------+-------------------------+--------------------------+------------------------+
No rows selected (0.205 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> !sh hdfs dfs -put trees.csv trees_table

SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hive/lib/log4j-slf4j-impl-2.10.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hadoop/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]

0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT * FROM TREES_EXTERNAL LIMIT 5;

INFO  : Compiling command(queryId=hive_20201115214019_95728446-6307-4117-bd74-5590d87de9ae): SELECT * FROM TREES_EXTERNAL LIMIT 5
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:trees_external.geopoint, type:string, comment:null), FieldSchema(name:trees_external.arrondissement, type:int, comment:null), FieldSchema(name:trees_external.genre, type:string, comment:null), FieldSchema(name:trees_external.espece, type:string, comment:null), FieldSchema(name:trees_external.famille, type:string, comment:null), FieldSchema(name:trees_external.anneeplantation, type:int, comment:null), FieldSchema(name:trees_external.hauteur, type:float, comment:null), FieldSchema(name:trees_external.circonference, type:float, comment:null), FieldSchema(name:trees_external.adresse, type:string, comment:null), FieldSchema(name:trees_external.nomcommun, type:string, comment:null), FieldSchema(name:trees_external.variete, type:string, comment:null), FieldSchema(name:trees_external.objectid, type:int, comment:null), FieldSchema(name:trees_external.nom_ev, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115214019_95728446-6307-4117-bd74-5590d87de9ae); Time taken: 0.283 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115214019_95728446-6307-4117-bd74-5590d87de9ae): SELECT * FROM TREES_EXTERNAL LIMIT 5
INFO  : Completed executing command(queryId=hive_20201115214019_95728446-6307-4117-bd74-5590d87de9ae); Time taken: 0.003 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
|     trees_external.geopoint     | trees_external.arrondissement  | trees_external.genre  | trees_external.espece  | trees_external.famille  | trees_external.anneeplantation  | trees_external.hauteur  | trees_external.circonference  |               trees_external.adresse               | trees_external.nomcommun  | trees_external.variete  | trees_external.objectid  |        trees_external.nom_ev         |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
| (48.857140829, 2.29533455314)   | 7                              | Maclura               | pomifera               | Moraceae                | 1935                            | 13.0                    | NULL                          | Quai Branly, avenue de La Motte-Piquet, avenue de la Bourdonnais, avenue de Suffren | Oranger des Osages        |                         | 6                        | Parc du Champs de Mars               |
| (48.8685686134, 2.31331809304)  | 8                              | Calocedrus            | decurrens              | Cupressaceae            | 1854                            | 20.0                    | 195.0                         | Cours-la-Reine, avenue Franklin-D.-Roosevelt, avenue Matignon, avenue Gabriel | Cèdre à encens            |                         | 11                       | Jardin des Champs Elysées            |
| (48.8768191638, 2.33210374339)  | 9                              | Pterocarya            | fraxinifolia           | Juglandaceae            | 1862                            | 22.0                    | 330.0                         | Place d'Estienne-d'Orves                           | Pérocarya du Caucase      |                         | 14                       | Square Etienne d'Orves               |
| (48.8373323894, 2.40776275516)  | 12                             | Celtis                | australis              | Cannabaceae             | 1906                            | 16.0                    | 295.0                         | 27, boulevard Soult                                | Micocoulier de Provence   |                         | 16                       | Avenue 27 boulevard Soult            |
| (48.8341842636, 2.46130493573)  | 12                             | Quercus               | petraea                | Fagaceae                | 1784                            | 30.0                    | 430.0                         | route ronde des Minimes                            | Chêne rouvre              |                         | 19                       | Bois de Vincennes (lac des minimes)  |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
5 rows selected (0.359 seconds)

: jdbc:hive2://hadoop-master01.efrei.online:> DROP TABLE trees_external;

INFO  : Compiling command(queryId=hive_20201115214116_22167b5d-02e7-4611-bd70-94d223a65492): DROP TABLE trees_external
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115214116_22167b5d-02e7-4611-bd70-94d223a65492); Time taken: 0.03 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115214116_22167b5d-02e7-4611-bd70-94d223a65492): DROP TABLE trees_external
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115214116_22167b5d-02e7-4611-bd70-94d223a65492); Time taken: 0.836 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (0.88 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> !sh hdfs dfs -ls trees_table

SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hive/lib/log4j-slf4j-impl-2.10.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hadoop/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]
Found 1 items
-rw-r--r--   3 istepanian hdfs      16680 2020-11-15 21:39 trees_table/trees.csv

0: jdbc:hive2://hadoop-master01.efrei.online:> CREATE EXTERNAL TABLE trees_external(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1");

INFO  : Compiling command(queryId=hive_20201115214647_e5304ce9-aced-4971-bf97-074b3d0cde0b): CREATE EXTERNAL TABLE trees_external(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1")
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115214647_e5304ce9-aced-4971-bf97-074b3d0cde0b); Time taken: 0.041 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115214647_e5304ce9-aced-4971-bf97-074b3d0cde0b): CREATE EXTERNAL TABLE trees_external(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1")
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115214647_e5304ce9-aced-4971-bf97-074b3d0cde0b); Time taken: 0.069 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (0.124 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SHOW TABLES;

INFO  : Compiling command(queryId=hive_20201115214714_b62a0f00-c90f-4c77-8378-46f59273cac1): SHOW TABLES
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:tab_name, type:string, comment:from deserializer)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115214714_b62a0f00-c90f-4c77-8378-46f59273cac1); Time taken: 0.012 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115214714_b62a0f00-c90f-4c77-8378-46f59273cac1): SHOW TABLES
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115214714_b62a0f00-c90f-4c77-8378-46f59273cac1); Time taken: 0.018 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------+
|    tab_name     |
+-----------------+
| trees_external  |
+-----------------+
1 row selected (0.061 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT * FROM TREES_EXTERNAL LIMIT 5;

INFO  : Compiling command(queryId=hive_20201115214759_bb86844e-a5c6-48c1-9cd8-d2481aed74d7): SELECT * FROM TREES_EXTERNAL LIMIT 5
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:trees_external.geopoint, type:string, comment:null), FieldSchema(name:trees_external.arrondissement, type:int, comment:null), FieldSchema(name:trees_external.genre, type:string, comment:null), FieldSchema(name:trees_external.espece, type:string, comment:null), FieldSchema(name:trees_external.famille, type:string, comment:null), FieldSchema(name:trees_external.anneeplantation, type:int, comment:null), FieldSchema(name:trees_external.hauteur, type:float, comment:null), FieldSchema(name:trees_external.circonference, type:float, comment:null), FieldSchema(name:trees_external.adresse, type:string, comment:null), FieldSchema(name:trees_external.nomcommun, type:string, comment:null), FieldSchema(name:trees_external.variete, type:string, comment:null), FieldSchema(name:trees_external.objectid, type:int, comment:null), FieldSchema(name:trees_external.nom_ev, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115214759_bb86844e-a5c6-48c1-9cd8-d2481aed74d7); Time taken: 0.173 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115214759_bb86844e-a5c6-48c1-9cd8-d2481aed74d7): SELECT * FROM TREES_EXTERNAL LIMIT 5
INFO  : Completed executing command(queryId=hive_20201115214759_bb86844e-a5c6-48c1-9cd8-d2481aed74d7); Time taken: 0.001 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
|     trees_external.geopoint     | trees_external.arrondissement  | trees_external.genre  | trees_external.espece  | trees_external.famille  | trees_external.anneeplantation  | trees_external.hauteur  | trees_external.circonference  |               trees_external.adresse               | trees_external.nomcommun  | trees_external.variete  | trees_external.objectid  |        trees_external.nom_ev         |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
| (48.857140829, 2.29533455314)   | 7                              | Maclura               | pomifera               | Moraceae                | 1935                            | 13.0                    | NULL                          | Quai Branly, avenue de La Motte-Piquet, avenue de la Bourdonnais, avenue de Suffren | Oranger des Osages        |                         | 6                        | Parc du Champs de Mars               |
| (48.8685686134, 2.31331809304)  | 8                              | Calocedrus            | decurrens              | Cupressaceae            | 1854                            | 20.0                    | 195.0                         | Cours-la-Reine, avenue Franklin-D.-Roosevelt, avenue Matignon, avenue Gabriel | Cèdre à encens            |                         | 11                       | Jardin des Champs Elysées            |
| (48.8768191638, 2.33210374339)  | 9                              | Pterocarya            | fraxinifolia           | Juglandaceae            | 1862                            | 22.0                    | 330.0                         | Place d'Estienne-d'Orves                           | Pérocarya du Caucase      |                         | 14                       | Square Etienne d'Orves               |
| (48.8373323894, 2.40776275516)  | 12                             | Celtis                | australis              | Cannabaceae             | 1906                            | 16.0                    | 295.0                         | 27, boulevard Soult                                | Micocoulier de Provence   |                         | 16                       | Avenue 27 boulevard Soult            |
| (48.8341842636, 2.46130493573)  | 12                             | Quercus               | petraea                | Fagaceae                | 1784                            | 30.0                    | 430.0                         | route ronde des Minimes                            | Chêne rouvre              |                         | 19                       | Bois de Vincennes (lac des minimes)  |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
5 rows selected (0.221 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

We can now create our internal table with the `CREATE TABLE` query. The data is automatically linked to the internal table:

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> CREATE TABLE trees_internal(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1");

INFO  : Compiling command(queryId=hive_20201115220445_874e743c-cec6-4fa5-a531-08988c6a3bab): CREATE TABLE trees_internal(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1")
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115220445_874e743c-cec6-4fa5-a531-08988c6a3bab); Time taken: 0.153 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115220445_874e743c-cec6-4fa5-a531-08988c6a3bab): CREATE TABLE trees_internal(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1")
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115220445_874e743c-cec6-4fa5-a531-08988c6a3bab); Time taken: 0.094 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (0.256 seconds)
INFO  : Compiling command(queryId=hive_20201115222830_0f7f8d14-7967-4ea7-8030-b2da630a4607): SELECT * FROM trees_internal LIMIT 5
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:trees_internal.geopoint, type:string, comment:null), FieldSchema(name:trees_internal.arrondissement, type:int, comment:null), FieldSchema(name:trees_internal.genre, type:string, comment:null), FieldSchema(name:trees_internal.espece, type:string, comment:null), FieldSchema(name:trees_internal.famille, type:string, comment:null), FieldSchema(name:trees_internal.anneeplantation, type:int, comment:null), FieldSchema(name:trees_internal.hauteur, type:float, comment:null), FieldSchema(name:trees_internal.circonference, type:float, comment:null), FieldSchema(name:trees_internal.adresse, type:string, comment:null), FieldSchema(name:trees_internal.nomcommun, type:string, comment:null), FieldSchema(name:trees_internal.variete, type:string, comment:null), FieldSchema(name:trees_internal.objectid, type:int, comment:null), FieldSchema(name:trees_internal.nom_ev, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115222830_0f7f8d14-7967-4ea7-8030-b2da630a4607); Time taken: 0.286 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115222830_0f7f8d14-7967-4ea7-8030-b2da630a4607): SELECT * FROM trees_internal LIMIT 5
INFO  : Completed executing command(queryId=hive_20201115222830_0f7f8d14-7967-4ea7-8030-b2da630a4607); Time taken: 0.001 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
|     trees_internal.geopoint     | trees_internal.arrondissement  | trees_internal.genre  | trees_internal.espece  | trees_internal.famille  | trees_internal.anneeplantation  | trees_internal.hauteur  | trees_internal.circonference  |               trees_internal.adresse               | trees_internal.nomcommun  | trees_internal.variete  | trees_internal.objectid  |        trees_internal.nom_ev         |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
| (48.857140829, 2.29533455314)   | 7                              | Maclura               | pomifera               | Moraceae                | 1935                            | 13.0                    | NULL                          | Quai Branly, avenue de La Motte-Piquet, avenue de la Bourdonnais, avenue de Suffren | Oranger des Osages        |                         | 6                        | Parc du Champs de Mars               |
| (48.8685686134, 2.31331809304)  | 8                              | Calocedrus            | decurrens              | Cupressaceae            | 1854                            | 20.0                    | 195.0                         | Cours-la-Reine, avenue Franklin-D.-Roosevelt, avenue Matignon, avenue Gabriel | Cèdre à encens            |                         | 11                       | Jardin des Champs Elysées            |
| (48.8768191638, 2.33210374339)  | 9                              | Pterocarya            | fraxinifolia           | Juglandaceae            | 1862                            | 22.0                    | 330.0                         | Place d'Estienne-d'Orves                           | Pérocarya du Caucase      |                         | 14                       | Square Etienne d'Orves               |
| (48.8373323894, 2.40776275516)  | 12                             | Celtis                | australis              | Cannabaceae             | 1906                            | 16.0                    | 295.0                         | 27, boulevard Soult                                | Micocoulier de Provence   |                         | 16                       | Avenue 27 boulevard Soult            |
| (48.8341842636, 2.46130493573)  | 12                             | Quercus               | petraea                | Fagaceae                | 1784                            | 30.0                    | 430.0                         | route ronde des Minimes                            | Chêne rouvre              |                         | 19                       | Bois de Vincennes (lac des minimes)  |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
5 rows selected (0.348 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT * FROM trees_internal LIMIT 5;

INFO  : Compiling command(queryId=hive_20201115223221_96a9786d-943f-492d-9fd9-1077aa32e61c): SELECT * FROM trees_internal LIMIT 5
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:trees_internal.geopoint, type:string, comment:null), FieldSchema(name:trees_internal.arrondissement, type:int, comment:null), FieldSchema(name:trees_internal.genre, type:string, comment:null), FieldSchema(name:trees_internal.espece, type:string, comment:null), FieldSchema(name:trees_internal.famille, type:string, comment:null), FieldSchema(name:trees_internal.anneeplantation, type:int, comment:null), FieldSchema(name:trees_internal.hauteur, type:float, comment:null), FieldSchema(name:trees_internal.circonference, type:float, comment:null), FieldSchema(name:trees_internal.adresse, type:string, comment:null), FieldSchema(name:trees_internal.nomcommun, type:string, comment:null), FieldSchema(name:trees_internal.variete, type:string, comment:null), FieldSchema(name:trees_internal.objectid, type:int, comment:null), FieldSchema(name:trees_internal.nom_ev, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115223221_96a9786d-943f-492d-9fd9-1077aa32e61c); Time taken: 0.117 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115223221_96a9786d-943f-492d-9fd9-1077aa32e61c): SELECT * FROM trees_internal LIMIT 5
INFO  : Completed executing command(queryId=hive_20201115223221_96a9786d-943f-492d-9fd9-1077aa32e61c); Time taken: 0.001 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
|     trees_internal.geopoint     | trees_internal.arrondissement  | trees_internal.genre  | trees_internal.espece  | trees_internal.famille  | trees_internal.anneeplantation  | trees_internal.hauteur  | trees_internal.circonference  |               trees_internal.adresse               | trees_internal.nomcommun  | trees_internal.variete  | trees_internal.objectid  |        trees_internal.nom_ev         |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
| (48.857140829, 2.29533455314)   | 7                              | Maclura               | pomifera               | Moraceae                | 1935                            | 13.0                    | NULL                          | Quai Branly, avenue de La Motte-Piquet, avenue de la Bourdonnais, avenue de Suffren | Oranger des Osages        |                         | 6                        | Parc du Champs de Mars               |
| (48.8685686134, 2.31331809304)  | 8                              | Calocedrus            | decurrens              | Cupressaceae            | 1854                            | 20.0                    | 195.0                         | Cours-la-Reine, avenue Franklin-D.-Roosevelt, avenue Matignon, avenue Gabriel | Cèdre à encens            |                         | 11                       | Jardin des Champs Elysées            |
| (48.8768191638, 2.33210374339)  | 9                              | Pterocarya            | fraxinifolia           | Juglandaceae            | 1862                            | 22.0                    | 330.0                         | Place d'Estienne-d'Orves                           | Pérocarya du Caucase      |                         | 14                       | Square Etienne d'Orves               |
| (48.8373323894, 2.40776275516)  | 12                             | Celtis                | australis              | Cannabaceae             | 1906                            | 16.0                    | 295.0                         | 27, boulevard Soult                                | Micocoulier de Provence   |                         | 16                       | Avenue 27 boulevard Soult            |
| (48.8341842636, 2.46130493573)  | 12                             | Quercus               | petraea                | Fagaceae                | 1784                            | 30.0                    | 430.0                         | route ronde des Minimes                            | Chêne rouvre              |                         | 19                       | Bois de Vincennes (lac des minimes)  |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
5 rows selected (0.177 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

If we removed the internal table, the whole folder would be deleted, and we would have to reimport the `trees.csv` file.

```
0: jdbc:hive2://hadoop-master01.efrei.online:> DROP TABLE trees_internal;

INFO  : Compiling command(queryId=hive_20201115223350_acbecfdc-04a7-434b-a597-81a0026b750c): DROP TABLE trees_internal
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115223350_acbecfdc-04a7-434b-a597-81a0026b750c); Time taken: 0.033 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115223350_acbecfdc-04a7-434b-a597-81a0026b750c): DROP TABLE trees_internal
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115223350_acbecfdc-04a7-434b-a597-81a0026b750c); Time taken: 1.082 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (1.127 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> !sh hdfs dfs -ls trees_table

SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hive/lib/log4j-slf4j-impl-2.10.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hadoop/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]
ls: `trees_table': No such file or directory
Command failed with exit code = 1
0: jdbc:hive2://hadoop-master01.efrei.online:> SHOW TABLES;
INFO  : Compiling command(queryId=hive_20201115223449_cc919a47-7ea1-4a67-8c25-8fc8b687443e): SHOW TABLES
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:tab_name, type:string, comment:from deserializer)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115223449_cc919a47-7ea1-4a67-8c25-8fc8b687443e); Time taken: 0.012 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115223449_cc919a47-7ea1-4a67-8c25-8fc8b687443e): SHOW TABLES
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115223449_cc919a47-7ea1-4a67-8c25-8fc8b687443e); Time taken: 0.021 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------+
|    tab_name     |
+-----------------+
| trees_external  |
+-----------------+
1 row selected (0.067 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT * FROM trees_external LIMIT 5;

INFO  : Compiling command(queryId=hive_20201115223536_3dfbb502-5f56-45e8-b9c5-110184c380ce): SELECT * FROM trees_external LIMIT 5
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:trees_external.geopoint, type:string, comment:null), FieldSchema(name:trees_external.arrondissement, type:int, comment:null), FieldSchema(name:trees_external.genre, type:string, comment:null), FieldSchema(name:trees_external.espece, type:string, comment:null), FieldSchema(name:trees_external.famille, type:string, comment:null), FieldSchema(name:trees_external.anneeplantation, type:int, comment:null), FieldSchema(name:trees_external.hauteur, type:float, comment:null), FieldSchema(name:trees_external.circonference, type:float, comment:null), FieldSchema(name:trees_external.adresse, type:string, comment:null), FieldSchema(name:trees_external.nomcommun, type:string, comment:null), FieldSchema(name:trees_external.variete, type:string, comment:null), FieldSchema(name:trees_external.objectid, type:int, comment:null), FieldSchema(name:trees_external.nom_ev, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115223536_3dfbb502-5f56-45e8-b9c5-110184c380ce); Time taken: 0.185 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115223536_3dfbb502-5f56-45e8-b9c5-110184c380ce): SELECT * FROM trees_external LIMIT 5
INFO  : Completed executing command(queryId=hive_20201115223536_3dfbb502-5f56-45e8-b9c5-110184c380ce); Time taken: 0.001 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+--------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+-------------------------+---------------------------+-------------------------+--------------------------+------------------------+
| trees_external.geopoint  | trees_external.arrondissement  | trees_external.genre  | trees_external.espece  | trees_external.famille  | trees_external.anneeplantation  | trees_external.hauteur  | trees_external.circonference  | trees_external.adresse  | trees_external.nomcommun  | trees_external.variete  | trees_external.objectid  | trees_external.nom_ev  |
+--------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+-------------------------+---------------------------+-------------------------+--------------------------+------------------------+
+--------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+-------------------------+---------------------------+-------------------------+--------------------------+------------------------+
No rows selected (0.2 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> !sh hdfs dfs -mkdir trees_table

SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hive/lib/log4j-slf4j-impl-2.10.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hadoop/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]

0: jdbc:hive2://hadoop-master01.efrei.online:> !sh hdfs dfs -put trees.csv trees_table

SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hive/lib/log4j-slf4j-impl-2.10.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hadoop/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]

0: jdbc:hive2://hadoop-master01.efrei.online:> CREATE TABLE trees_internal(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1");

INFO  : Compiling command(queryId=hive_20201115223653_2f6ee372-5864-4814-b7e6-da80f106100d): CREATE TABLE trees_internal(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1")
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:null, properties:null)
INFO  : Completed compiling command(queryId=hive_20201115223653_2f6ee372-5864-4814-b7e6-da80f106100d); Time taken: 0.024 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115223653_2f6ee372-5864-4814-b7e6-da80f106100d): CREATE TABLE trees_internal(GEOPOINT string, ARRONDISSEMENT int, GENRE string, ESPECE string, FAMILLE string, ANNEEPLANTATION int, HAUTEUR float, CIRCONFERENCE float, ADRESSE string, NOMCOMMUN string, VARIETE string, OBJECTID int, NOM_EV string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ';' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION 'hdfs://efrei/user/istepanian/trees_table' TBLPROPERTIES ("skip.header.line.count"="1")
INFO  : Starting task [Stage-0:DDL] in serial mode
INFO  : Completed executing command(queryId=hive_20201115223653_2f6ee372-5864-4814-b7e6-da80f106100d); Time taken: 0.076 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
No rows affected (0.106 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT * FROM trees_internal LIMIT 5;

INFO  : Compiling command(queryId=hive_20201115223705_2ab5dfa3-3ddc-41ca-8e46-42f7b109fca9): SELECT * FROM trees_internal LIMIT 5
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:trees_internal.geopoint, type:string, comment:null), FieldSchema(name:trees_internal.arrondissement, type:int, comment:null), FieldSchema(name:trees_internal.genre, type:string, comment:null), FieldSchema(name:trees_internal.espece, type:string, comment:null), FieldSchema(name:trees_internal.famille, type:string, comment:null), FieldSchema(name:trees_internal.anneeplantation, type:int, comment:null), FieldSchema(name:trees_internal.hauteur, type:float, comment:null), FieldSchema(name:trees_internal.circonference, type:float, comment:null), FieldSchema(name:trees_internal.adresse, type:string, comment:null), FieldSchema(name:trees_internal.nomcommun, type:string, comment:null), FieldSchema(name:trees_internal.variete, type:string, comment:null), FieldSchema(name:trees_internal.objectid, type:int, comment:null), FieldSchema(name:trees_internal.nom_ev, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115223705_2ab5dfa3-3ddc-41ca-8e46-42f7b109fca9); Time taken: 0.105 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115223705_2ab5dfa3-3ddc-41ca-8e46-42f7b109fca9): SELECT * FROM trees_internal LIMIT 5
INFO  : Completed executing command(queryId=hive_20201115223705_2ab5dfa3-3ddc-41ca-8e46-42f7b109fca9); Time taken: 0.002 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
|     trees_internal.geopoint     | trees_internal.arrondissement  | trees_internal.genre  | trees_internal.espece  | trees_internal.famille  | trees_internal.anneeplantation  | trees_internal.hauteur  | trees_internal.circonference  |               trees_internal.adresse               | trees_internal.nomcommun  | trees_internal.variete  | trees_internal.objectid  |        trees_internal.nom_ev         |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
| (48.857140829, 2.29533455314)   | 7                              | Maclura               | pomifera               | Moraceae                | 1935                            | 13.0                    | NULL                          | Quai Branly, avenue de La Motte-Piquet, avenue de la Bourdonnais, avenue de Suffren | Oranger des Osages        |                         | 6                        | Parc du Champs de Mars               |
| (48.8685686134, 2.31331809304)  | 8                              | Calocedrus            | decurrens              | Cupressaceae            | 1854                            | 20.0                    | 195.0                         | Cours-la-Reine, avenue Franklin-D.-Roosevelt, avenue Matignon, avenue Gabriel | Cèdre à encens            |                         | 11                       | Jardin des Champs Elysées            |
| (48.8768191638, 2.33210374339)  | 9                              | Pterocarya            | fraxinifolia           | Juglandaceae            | 1862                            | 22.0                    | 330.0                         | Place d'Estienne-d'Orves                           | Pérocarya du Caucase      |                         | 14                       | Square Etienne d'Orves               |
| (48.8373323894, 2.40776275516)  | 12                             | Celtis                | australis              | Cannabaceae             | 1906                            | 16.0                    | 295.0                         | 27, boulevard Soult                                | Micocoulier de Provence   |                         | 16                       | Avenue 27 boulevard Soult            |
| (48.8341842636, 2.46130493573)  | 12                             | Quercus               | petraea                | Fagaceae                | 1784                            | 30.0                    | 430.0                         | route ronde des Minimes                            | Chêne rouvre              |                         | 19                       | Bois de Vincennes (lac des minimes)  |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
5 rows selected (0.172 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT * FROM trees_external LIMIT 5;

INFO  : Compiling command(queryId=hive_20201115223806_428f42cc-3c1d-4a63-bf39-0a07e2714dd4): SELECT * FROM trees_external LIMIT 5
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:trees_external.geopoint, type:string, comment:null), FieldSchema(name:trees_external.arrondissement, type:int, comment:null), FieldSchema(name:trees_external.genre, type:string, comment:null), FieldSchema(name:trees_external.espece, type:string, comment:null), FieldSchema(name:trees_external.famille, type:string, comment:null), FieldSchema(name:trees_external.anneeplantation, type:int, comment:null), FieldSchema(name:trees_external.hauteur, type:float, comment:null), FieldSchema(name:trees_external.circonference, type:float, comment:null), FieldSchema(name:trees_external.adresse, type:string, comment:null), FieldSchema(name:trees_external.nomcommun, type:string, comment:null), FieldSchema(name:trees_external.variete, type:string, comment:null), FieldSchema(name:trees_external.objectid, type:int, comment:null), FieldSchema(name:trees_external.nom_ev, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115223806_428f42cc-3c1d-4a63-bf39-0a07e2714dd4); Time taken: 0.188 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115223806_428f42cc-3c1d-4a63-bf39-0a07e2714dd4): SELECT * FROM trees_external LIMIT 5
INFO  : Completed executing command(queryId=hive_20201115223806_428f42cc-3c1d-4a63-bf39-0a07e2714dd4); Time taken: 0.001 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
|     trees_external.geopoint     | trees_external.arrondissement  | trees_external.genre  | trees_external.espece  | trees_external.famille  | trees_external.anneeplantation  | trees_external.hauteur  | trees_external.circonference  |               trees_external.adresse               | trees_external.nomcommun  | trees_external.variete  | trees_external.objectid  |        trees_external.nom_ev         |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
| (48.857140829, 2.29533455314)   | 7                              | Maclura               | pomifera               | Moraceae                | 1935                            | 13.0                    | NULL                          | Quai Branly, avenue de La Motte-Piquet, avenue de la Bourdonnais, avenue de Suffren | Oranger des Osages        |                         | 6                        | Parc du Champs de Mars               |
| (48.8685686134, 2.31331809304)  | 8                              | Calocedrus            | decurrens              | Cupressaceae            | 1854                            | 20.0                    | 195.0                         | Cours-la-Reine, avenue Franklin-D.-Roosevelt, avenue Matignon, avenue Gabriel | Cèdre à encens            |                         | 11                       | Jardin des Champs Elysées            |
| (48.8768191638, 2.33210374339)  | 9                              | Pterocarya            | fraxinifolia           | Juglandaceae            | 1862                            | 22.0                    | 330.0                         | Place d'Estienne-d'Orves                           | Pérocarya du Caucase      |                         | 14                       | Square Etienne d'Orves               |
| (48.8373323894, 2.40776275516)  | 12                             | Celtis                | australis              | Cannabaceae             | 1906                            | 16.0                    | 295.0                         | 27, boulevard Soult                                | Micocoulier de Provence   |                         | 16                       | Avenue 27 boulevard Soult            |
| (48.8341842636, 2.46130493573)  | 12                             | Quercus               | petraea                | Fagaceae                | 1784                            | 30.0                    | 430.0                         | route ronde des Minimes                            | Chêne rouvre              |                         | 19                       | Bois de Vincennes (lac des minimes)  |
+---------------------------------+--------------------------------+-----------------------+------------------------+-------------------------+---------------------------------+-------------------------+-------------------------------+----------------------------------------------------+---------------------------+-------------------------+--------------------------+--------------------------------------+
5 rows selected (0.25 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

We can now start querying the internal table.

## 1.3. Creating queries

We will create the following queries for our internal table:

- A query that displays the list of distinct districts containing trees

For this query, we will simply use the `DISTINCT` keyboard when retrieving the `arrondissement` column from our table.

```sql
SELECT DISTINCT arrondissement FROM trees_internal;
```

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT DISTINCT arrondissement FROM trees_internal;

INFO  : Compiling command(queryId=hive_20201115224123_fd5ad9b0-c613-4617-a8dc-908d8e98e048): SELECT DISTINCT arrondissement FROM trees_internal
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:arrondissement, type:int, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115224123_fd5ad9b0-c613-4617-a8dc-908d8e98e048); Time taken: 0.092 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115224123_fd5ad9b0-c613-4617-a8dc-908d8e98e048): SELECT DISTINCT arrondissement FROM trees_internal
INFO  : Query ID = hive_20201115224123_fd5ad9b0-c613-4617-a8dc-908d8e98e048
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20201115224123_fd5ad9b0-c613-4617-a8dc-908d8e98e048
INFO  : Tez session hasn't been created yet. Opening session
INFO  : Dag name: SELECT DISTINCT arrondissem...trees_internal (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1603290159664_5405)

----------------------------------------------------------------------------------------------
        VERTICES      MODE        STATUS  TOTAL  COMPLETED  RUNNING  PENDING  FAILED  KILLED  
----------------------------------------------------------------------------------------------
Map 1 .......... container     SUCCEEDED      1          1        0        0       0       0  
Reducer 2 ...... container     SUCCEEDED      1          1        0        0       0       0  
----------------------------------------------------------------------------------------------
VERTICES: 02/02  [==========================>>] 100%  ELAPSED TIME: 4.11 s     
----------------------------------------------------------------------------------------------
INFO  : Completed executing command(queryId=hive_20201115224123_fd5ad9b0-c613-4617-a8dc-908d8e98e048); Time taken: 12.702 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------+
| arrondissement  |
+-----------------+
| 3               |
| 4               |
| 5               |
| 6               |
| 7               |
| 8               |
| 9               |
| 11              |
| 12              |
| 13              |
| 14              |
| 15              |
| 16              |
| 17              |
| 18              |
| 19              |
| 20              |
+-----------------+
17 rows selected (12.888 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

- A query that displays the list of different tree species

To display the list of different (i.e. distinct) tree species, we can simply use the `DISTINCT` keyword in our query once again.

```sql
SELECT DISTINCT espece FROM trees_internal;
```

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT DISTINCT espece FROM trees_internal;

INFO  : Compiling command(queryId=hive_20201115224449_b61c25d7-0628-4396-b386-a7a6024db04e): SELECT DISTINCT espece FROM trees_internal
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:espece, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115224449_b61c25d7-0628-4396-b386-a7a6024db04e); Time taken: 0.297 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115224449_b61c25d7-0628-4396-b386-a7a6024db04e): SELECT DISTINCT espece FROM trees_internal
INFO  : Query ID = hive_20201115224449_b61c25d7-0628-4396-b386-a7a6024db04e
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20201115224449_b61c25d7-0628-4396-b386-a7a6024db04e
INFO  : Session is already open
INFO  : Dag name: SELECT DISTINCT espece FROM trees_internal (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1603290159664_5405)

----------------------------------------------------------------------------------------------
        VERTICES      MODE        STATUS  TOTAL  COMPLETED  RUNNING  PENDING  FAILED  KILLED  
----------------------------------------------------------------------------------------------
Map 1 .......... container     SUCCEEDED      1          1        0        0       0       0  
Reducer 2 ...... container     SUCCEEDED      1          1        0        0       0       0  
----------------------------------------------------------------------------------------------
VERTICES: 02/02  [==========================>>] 100%  ELAPSED TIME: 5.84 s     
----------------------------------------------------------------------------------------------
INFO  : Completed executing command(queryId=hive_20201115224449_b61c25d7-0628-4396-b386-a7a6024db04e); Time taken: 6.534 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------+
|     espece      |
+-----------------+
| araucana        |
| atlantica       |
| australis       |
| baccata         |
| bignonioides    |
| biloba          |
| bungeana        |
| cappadocicum    |
| carpinifolia    |
| colurna         |
| coulteri        |
| decurrens       |
| dioicus         |
| distichum       |
| excelsior       |
| fraxinifolia    |
| giganteum       |
| giraldii        |
| glutinosa       |
| grandiflora     |
| hippocastanum   |
| ilex            |
| involucrata     |
| japonicum       |
| kaki            |
| libanii         |
| monspessulanum  |
| nigra           |
| nigra laricio   |
| opalus          |
| orientalis      |
| papyrifera      |
| petraea         |
| pomifera        |
| pseudoacacia    |
| sempervirens    |
| serrata         |
| stenoptera      |
| suber           |
| sylvatica       |
| tomentosa       |
| tulipifera      |
| ulmoides        |
| virginiana      |
| x acerifolia    |
+-----------------+
45 rows selected (6.914 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

- A query that selects the number of trees for each kind

To select the number of trees for each kind (i.e. variety, variété in French) we can use a `GROUP BY` to aggregate over the `variete` column and use the `COUNT` aggregator to count the number of rows in the aggregations for each variety "key":

```sql
SELECT variete, COUNT(*) FROM trees_internal GROUP BY variete;
```

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT variete, COUNT(*) FROM trees_internal GROUP BY variete;

INFO  : Compiling command(queryId=hive_20201115225759_a9740820-b128-4859-a62a-bb3c4be51b97): SELECT variete, COUNT(*) FROM trees_internal GROUP BY variete
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:variete, type:string, comment:null), FieldSchema(name:_c1, type:bigint, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115225759_a9740820-b128-4859-a62a-bb3c4be51b97); Time taken: 0.166 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115225759_a9740820-b128-4859-a62a-bb3c4be51b97): SELECT variete, COUNT(*) FROM trees_internal GROUP BY variete
INFO  : Query ID = hive_20201115225759_a9740820-b128-4859-a62a-bb3c4be51b97
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20201115225759_a9740820-b128-4859-a62a-bb3c4be51b97
INFO  : Session is already open
INFO  : Dag name: SELECT variete, COUNT(*) FROM tree...variete (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1603290159664_5405)

----------------------------------------------------------------------------------------------
        VERTICES      MODE        STATUS  TOTAL  COMPLETED  RUNNING  PENDING  FAILED  KILLED  
----------------------------------------------------------------------------------------------
Map 1 .......... container     SUCCEEDED      1          1        0        0       0       0  
Reducer 2 ...... container     SUCCEEDED      1          1        0        0       0       0  
----------------------------------------------------------------------------------------------
VERTICES: 02/02  [==========================>>] 100%  ELAPSED TIME: 5.88 s     
----------------------------------------------------------------------------------------------
INFO  : Completed executing command(queryId=hive_20201115225759_a9740820-b128-4859-a62a-bb3c4be51b97); Time taken: 6.53 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------+------+
|     variete     | _c1  |
+-----------------+------+
|                 | 86   |
| Austriaca       | 1    |
| Glauca          | 1    |
| Glauca pendula  | 1    |
| Pendula         | 3    |
| Purpurea        | 4    |
| Tortuosa        | 1    |
+-----------------+------+
7 rows selected (6.777 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

Because only 11 trees are associated a variety in the dataset, 86 of them appear as having no variety (an empty string); it we display the number of trees for each species:

```sql
SELECT espece, COUNT(*) FROM trees_internal GROUP BY espece;
```

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT espece, COUNT(*) FROM trees_internal GROUP BY espece;

INFO  : Compiling command(queryId=hive_20201115230125_04e62be8-83ac-4f4c-90b4-6c6920ecb066): SELECT espece, COUNT(*) FROM trees_internal GROUP BY espece
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:espece, type:string, comment:null), FieldSchema(name:_c1, type:bigint, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115230125_04e62be8-83ac-4f4c-90b4-6c6920ecb066); Time taken: 0.271 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115230125_04e62be8-83ac-4f4c-90b4-6c6920ecb066): SELECT espece, COUNT(*) FROM trees_internal GROUP BY espece
INFO  : Query ID = hive_20201115230125_04e62be8-83ac-4f4c-90b4-6c6920ecb066
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20201115230125_04e62be8-83ac-4f4c-90b4-6c6920ecb066
INFO  : Session is already open
INFO  : Dag name: SELECT espece, COUNT(*) FROM trees_...espece (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1603290159664_5405)

----------------------------------------------------------------------------------------------
        VERTICES      MODE        STATUS  TOTAL  COMPLETED  RUNNING  PENDING  FAILED  KILLED  
----------------------------------------------------------------------------------------------
Map 1 .......... container     SUCCEEDED      1          1        0        0       0       0  
Reducer 2 ...... container     SUCCEEDED      1          1        0        0       0       0  
----------------------------------------------------------------------------------------------
VERTICES: 02/02  [==========================>>] 100%  ELAPSED TIME: 3.76 s     
----------------------------------------------------------------------------------------------
INFO  : Completed executing command(queryId=hive_20201115230125_04e62be8-83ac-4f4c-90b4-6c6920ecb066); Time taken: 4.49 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------+------+
|     espece      | _c1  |
+-----------------+------+
| araucana        | 1    |
| atlantica       | 2    |
| australis       | 1    |
| baccata         | 2    |
| bignonioides    | 1    |
| biloba          | 5    |
| bungeana        | 1    |
| cappadocicum    | 1    |
| carpinifolia    | 4    |
| colurna         | 3    |
| coulteri        | 1    |
| decurrens       | 1    |
| dioicus         | 1    |
| distichum       | 3    |
| excelsior       | 1    |
| fraxinifolia    | 2    |
| giganteum       | 5    |
| giraldii        | 1    |
| glutinosa       | 1    |
| grandiflora     | 1    |
| hippocastanum   | 3    |
| ilex            | 1    |
| involucrata     | 1    |
| japonicum       | 1    |
| kaki            | 2    |
| libanii         | 2    |
| monspessulanum  | 1    |
| nigra           | 3    |
| nigra laricio   | 1    |
| opalus          | 1    |
| orientalis      | 8    |
| papyrifera      | 1    |
| petraea         | 2    |
| pomifera        | 1    |
| pseudoacacia    | 1    |
| sempervirens    | 1    |
| serrata         | 1    |
| stenoptera      | 1    |
| suber           | 1    |
| sylvatica       | 8    |
| tomentosa       | 2    |
| tulipifera      | 2    |
| ulmoides        | 1    |
| virginiana      | 2    |
| x acerifolia    | 11   |
+-----------------+------+
45 rows selected (4.799 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

We can see that the job worked correctly.

- A query that calculates the height of the tallest tree of each kind

Just like for the previous query, we will use the `GROUP BY` keyword to do an aggregation, but use the `MAX` function instead of the `COUNT` function for the aggregator over the `hauteur` column, to get the height of the highest tree for each species.

```sql
SELECT espece, MAX(hauteur) FROM trees_internal GROUP BY espece;
```

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT espece, MAX(hauteur) FROM trees_internal GROUP BY espece;

INFO  : Compiling command(queryId=hive_20201115230602_ff659726-3941-44ff-819a-7578652dd821): SELECT espece, MAX(hauteur) FROM trees_internal GROUP BY espece
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:espece, type:string, comment:null), FieldSchema(name:_c1, type:float, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115230602_ff659726-3941-44ff-819a-7578652dd821); Time taken: 0.279 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115230602_ff659726-3941-44ff-819a-7578652dd821): SELECT espece, MAX(hauteur) FROM trees_internal GROUP BY espece
INFO  : Query ID = hive_20201115230602_ff659726-3941-44ff-819a-7578652dd821
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20201115230602_ff659726-3941-44ff-819a-7578652dd821
INFO  : Session is already open
INFO  : Dag name: SELECT espece, MAX(hauteur) FROM tr...espece (Stage-1)
INFO  : Completed executing command(queryId=hive_20201115230602_ff659726-3941-44ff-819a-7578652dd821); Time taken: 0.751 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
----------------------------------------------------------------------------------------------
        VERTICES      MODE        STATUS  TOTAL  COMPLETED  RUNNING  PENDING  FAILED  KILLED  
----------------------------------------------------------------------------------------------
Map 1 .......... container     SUCCEEDED      1          1        0        0       0       0  
Reducer 2 ...... container     SUCCEEDED      1          1        0        0       0       0  
----------------------------------------------------------------------------------------------
VERTICES: 02/02  [==========================>>] 100%  ELAPSED TIME: 0.59 s     
----------------------------------------------------------------------------------------------
+-----------------+-------+
|     espece      |  _c1  |
+-----------------+-------+
| araucana        | 9.0   |
| atlantica       | 25.0  |
| australis       | 16.0  |
| baccata         | 13.0  |
| bignonioides    | 15.0  |
| biloba          | 33.0  |
| bungeana        | 10.0  |
| cappadocicum    | 16.0  |
| carpinifolia    | 30.0  |
| colurna         | 20.0  |
| coulteri        | 14.0  |
| decurrens       | 20.0  |
| dioicus         | 10.0  |
| distichum       | 35.0  |
| excelsior       | 30.0  |
| fraxinifolia    | 27.0  |
| giganteum       | 35.0  |
| giraldii        | 35.0  |
| glutinosa       | 16.0  |
| grandiflora     | 12.0  |
| hippocastanum   | 30.0  |
| ilex            | 15.0  |
| involucrata     | 12.0  |
| japonicum       | 10.0  |
| kaki            | 14.0  |
| libanii         | 30.0  |
| monspessulanum  | 12.0  |
| nigra           | 30.0  |
| nigra laricio   | 30.0  |
| opalus          | 15.0  |
| orientalis      | 34.0  |
| papyrifera      | 12.0  |
| petraea         | 31.0  |
| pomifera        | 13.0  |
| pseudoacacia    | 11.0  |
| sempervirens    | 30.0  |
| serrata         | 18.0  |
| stenoptera      | 30.0  |
| suber           | 10.0  |
| sylvatica       | 30.0  |
| tomentosa       | 20.0  |
| tulipifera      | 35.0  |
| ulmoides        | 12.0  |
| virginiana      | 14.0  |
| x acerifolia    | 45.0  |
+-----------------+-------+
45 rows selected (1.102 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

The query worked as expected.

- A query that sorts the trees' heights from the shortest to the tallest

For this query, we simply use the SQL `ORDER BY <field> ASC` or the HiveQL `SORT BY <field> ASC` keyword (`ASC` stands for `ASCENDING`, sorting from smallest to largest in the order of the given type) to query the trees' heights (`hauteur`).

```sql
SELECT objectid, hauteur FROM trees_internal SORT BY hauteur ASC;
SELECT objectid, hauteur FROM trees_internal ORDER BY hauteur ASC;
```

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT objectid, hauteur FROM trees_internal SORT BY hauteur ASC;

INFO  : Compiling command(queryId=hive_20201115231451_35de7ddf-8761-4a9b-a5e6-f259ebe6e8a0): SELECT objectid, hauteur FROM trees_internal SORT BY hauteur ASC
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:objectid, type:int, comment:null), FieldSchema(name:hauteur, type:float, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115231451_35de7ddf-8761-4a9b-a5e6-f259ebe6e8a0); Time taken: 0.077 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115231451_35de7ddf-8761-4a9b-a5e6-f259ebe6e8a0): SELECT objectid, hauteur FROM trees_internal SORT BY hauteur ASC
INFO  : Query ID = hive_20201115231451_35de7ddf-8761-4a9b-a5e6-f259ebe6e8a0
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20201115231451_35de7ddf-8761-4a9b-a5e6-f259ebe6e8a0
INFO  : Tez session hasn't been created yet. Opening session
INFO  : Dag name: SELECT objectid, hauteur FROM trees_in...ASC (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1603290159664_5414)

----------------------------------------------------------------------------------------------
        VERTICES      MODE        STATUS  TOTAL  COMPLETED  RUNNING  PENDING  FAILED  KILLED  
----------------------------------------------------------------------------------------------
Map 1 .......... container     SUCCEEDED      1          1        0        0       0       0  
Reducer 2 ...... container     SUCCEEDED      1          1        0        0       0       0  
----------------------------------------------------------------------------------------------
VERTICES: 02/02  [==========================>>] 100%  ELAPSED TIME: 4.43 s     
----------------------------------------------------------------------------------------------
INFO  : Completed executing command(queryId=hive_20201115231451_35de7ddf-8761-4a9b-a5e6-f259ebe6e8a0); Time taken: 12.853 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------+----------+
| objectid  | hauteur  |
+-----------+----------+
| 67        | NULL     |
| 3         | 2.0      |
| 89        | 5.0      |
| 62        | 6.0      |
| 39        | 9.0      |
| 32        | 10.0     |
| 44        | 10.0     |
| 63        | 10.0     |
| 61        | 10.0     |
| 95        | 10.0     |
| 4         | 11.0     |
| 66        | 12.0     |
| 48        | 12.0     |
| 58        | 12.0     |
| 71        | 12.0     |
| 93        | 12.0     |
| 7         | 12.0     |
| 33        | 12.0     |
| 50        | 12.0     |
| 36        | 13.0     |
| 6         | 13.0     |
| 68        | 14.0     |
| 96        | 14.0     |
| 94        | 14.0     |
| 91        | 15.0     |
| 98        | 15.0     |
| 2         | 15.0     |
| 70        | 15.0     |
| 5         | 15.0     |
| 16        | 16.0     |
| 75        | 16.0     |
| 28        | 16.0     |
| 78        | 16.0     |
| 60        | 18.0     |
| 83        | 18.0     |
| 64        | 18.0     |
| 23        | 18.0     |
| 15        | 20.0     |
| 11        | 20.0     |
| 35        | 20.0     |
| 20        | 20.0     |
| 8         | 20.0     |
| 1         | 20.0     |
| 34        | 20.0     |
| 13        | 20.0     |
| 12        | 20.0     |
| 87        | 20.0     |
| 51        | 20.0     |
| 43        | 20.0     |
| 10        | 22.0     |
| 88        | 22.0     |
| 86        | 22.0     |
| 47        | 22.0     |
| 14        | 22.0     |
| 18        | 23.0     |
| 97        | 25.0     |
| 92        | 25.0     |
| 84        | 25.0     |
| 31        | 25.0     |
| 49        | 25.0     |
| 24        | 25.0     |
| 73        | 26.0     |
| 65        | 27.0     |
| 42        | 27.0     |
| 85        | 28.0     |
| 38        | 30.0     |
| 22        | 30.0     |
| 52        | 30.0     |
| 37        | 30.0     |
| 59        | 30.0     |
| 54        | 30.0     |
| 77        | 30.0     |
| 76        | 30.0     |
| 19        | 30.0     |
| 41        | 30.0     |
| 72        | 30.0     |
| 69        | 30.0     |
| 29        | 30.0     |
| 27        | 30.0     |
| 30        | 30.0     |
| 55        | 30.0     |
| 25        | 30.0     |
| 80        | 31.0     |
| 9         | 31.0     |
| 82        | 32.0     |
| 46        | 33.0     |
| 45        | 34.0     |
| 53        | 35.0     |
| 17        | 35.0     |
| 56        | 35.0     |
| 57        | 35.0     |
| 81        | 35.0     |
| 40        | 40.0     |
| 74        | 40.0     |
| 26        | 40.0     |
| 90        | 42.0     |
| 21        | 45.0     |
+-----------+----------+
97 rows selected (13.026 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT objectid, hauteur FROM trees_internal ORDER BY hauteur ASC;

INFO  : Compiling command(queryId=hive_20201115231535_0dc9efc4-8da7-4a60-b450-703004e4769a): SELECT objectid, hauteur FROM trees_internal ORDER BY hauteur ASC
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:objectid, type:int, comment:null), FieldSchema(name:hauteur, type:float, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115231535_0dc9efc4-8da7-4a60-b450-703004e4769a); Time taken: 0.263 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115231535_0dc9efc4-8da7-4a60-b450-703004e4769a): SELECT objectid, hauteur FROM trees_internal ORDER BY hauteur ASC
INFO  : Query ID = hive_20201115231535_0dc9efc4-8da7-4a60-b450-703004e4769a
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20201115231535_0dc9efc4-8da7-4a60-b450-703004e4769a
INFO  : Session is already open
INFO  : Dag name: SELECT objectid, hauteur FROM trees_in...ASC (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1603290159664_5414)

----------------------------------------------------------------------------------------------
        VERTICES      MODE        STATUS  TOTAL  COMPLETED  RUNNING  PENDING  FAILED  KILLED  
----------------------------------------------------------------------------------------------
Map 1 .......... container     SUCCEEDED      1          1        0        0       0       0  
Reducer 2 ...... container     SUCCEEDED      1          1        0        0       0       0  
----------------------------------------------------------------------------------------------
VERTICES: 02/02  [==========================>>] 100%  ELAPSED TIME: 6.31 s     
----------------------------------------------------------------------------------------------
INFO  : Completed executing command(queryId=hive_20201115231535_0dc9efc4-8da7-4a60-b450-703004e4769a); Time taken: 7.027 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------+----------+
| objectid  | hauteur  |
+-----------+----------+
| 67        | NULL     |
| 3         | 2.0      |
| 89        | 5.0      |
| 62        | 6.0      |
| 39        | 9.0      |
| 32        | 10.0     |
| 44        | 10.0     |
| 63        | 10.0     |
| 61        | 10.0     |
| 95        | 10.0     |
| 4         | 11.0     |
| 66        | 12.0     |
| 48        | 12.0     |
| 58        | 12.0     |
| 71        | 12.0     |
| 93        | 12.0     |
| 7         | 12.0     |
| 33        | 12.0     |
| 50        | 12.0     |
| 36        | 13.0     |
| 6         | 13.0     |
| 68        | 14.0     |
| 96        | 14.0     |
| 94        | 14.0     |
| 91        | 15.0     |
| 98        | 15.0     |
| 2         | 15.0     |
| 70        | 15.0     |
| 5         | 15.0     |
| 16        | 16.0     |
| 75        | 16.0     |
| 28        | 16.0     |
| 78        | 16.0     |
| 60        | 18.0     |
| 83        | 18.0     |
| 64        | 18.0     |
| 23        | 18.0     |
| 15        | 20.0     |
| 11        | 20.0     |
| 35        | 20.0     |
| 20        | 20.0     |
| 8         | 20.0     |
| 1         | 20.0     |
| 34        | 20.0     |
| 13        | 20.0     |
| 12        | 20.0     |
| 87        | 20.0     |
| 51        | 20.0     |
| 43        | 20.0     |
| 10        | 22.0     |
| 88        | 22.0     |
| 86        | 22.0     |
| 47        | 22.0     |
| 14        | 22.0     |
| 18        | 23.0     |
| 97        | 25.0     |
| 92        | 25.0     |
| 84        | 25.0     |
| 31        | 25.0     |
| 49        | 25.0     |
| 24        | 25.0     |
| 73        | 26.0     |
| 65        | 27.0     |
| 42        | 27.0     |
| 85        | 28.0     |
| 38        | 30.0     |
| 22        | 30.0     |
| 52        | 30.0     |
| 37        | 30.0     |
| 59        | 30.0     |
| 54        | 30.0     |
| 77        | 30.0     |
| 76        | 30.0     |
| 19        | 30.0     |
| 41        | 30.0     |
| 72        | 30.0     |
| 69        | 30.0     |
| 29        | 30.0     |
| 27        | 30.0     |
| 30        | 30.0     |
| 55        | 30.0     |
| 25        | 30.0     |
| 80        | 31.0     |
| 9         | 31.0     |
| 82        | 32.0     |
| 46        | 33.0     |
| 45        | 34.0     |
| 53        | 35.0     |
| 17        | 35.0     |
| 56        | 35.0     |
| 57        | 35.0     |
| 81        | 35.0     |
| 40        | 40.0     |
| 74        | 40.0     |
| 26        | 40.0     |
| 90        | 42.0     |
| 21        | 45.0     |
+-----------+----------+
97 rows selected (7.376 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

Both queries work as expected.

- A query that displays the district(s) containing the oldest tree

For such a query, there are multiple methods as we mentioned in the Yarn/Java Lab2 (https://github.com/iv-stpn/hadoop-examples-mapreduce) for the last `MapReduce` job. We will show both methods successively for the two last . The first method involves taking the first result of a sorted aggregation (the equivalent with an SQL-like language would be `SELECT <aggregation_function>(<field>) FROM <table> GROUP BY <field> ORDER BY <field> ASC LIMIT 1` for a `MIN` aggregation or `SELECT <aggregation_function>(<field>) FROM <table> GROUP BY <field> ORDER BY <field> DESC LIMIT 1` for a `MAX` aggregation). The second method involves using two `Reduce` jobs, which would be equivalent to using a subquery in SQL; the second method allows us to get all the rows with the maximum/minimum of the given field, while the sort allows us to get only the first . In our case, to find the oldest trees, we need to select the row with the oldest years of plantation (i.e. find the minimum `anneeplantation`); just like for the `MapReduce` job, we need to exclude the `NULL` fields:

```sql
SELECT arrondissement, anneeplantation FROM trees_internal WHERE anneeplantation IS NOT NULL ORDER BY anneeplantation ASC LIMIT 1;
SELECT arrondissement, anneeplantation FROM trees_internal WHERE anneeplantation = (SELECT MIN(anneeplantation) FROM trees_internal);
```

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT arrondissement, anneeplantation FROM trees_internal WHERE anneeplantation IS NOT NULL ORDER BY anneeplantation ASC LIMIT 1;

INFO  : Compiling command(queryId=hive_20201115234153_e5caf09f-11ec-4490-bdc3-0616eeb1c93a): SELECT arrondissement, anneeplantation FROM trees_internal WHERE anneeplantation IS NOT NULL ORDER BY anneeplantation ASC LIMIT 1
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:arrondissement, type:int, comment:null), FieldSchema(name:anneeplantation, type:int, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115234153_e5caf09f-11ec-4490-bdc3-0616eeb1c93a); Time taken: 0.194 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115234153_e5caf09f-11ec-4490-bdc3-0616eeb1c93a): SELECT arrondissement, anneeplantation FROM trees_internal WHERE anneeplantation IS NOT NULL ORDER BY anneeplantation ASC LIMIT 1
INFO  : Query ID = hive_20201115234153_e5caf09f-11ec-4490-bdc3-0616eeb1c93a
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20201115234153_e5caf09f-11ec-4490-bdc3-0616eeb1c93a
INFO  : Session is already open
INFO  : Dag name: SELECT arrondissement, anneeplantation F...1 (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1603290159664_5419)

----------------------------------------------------------------------------------------------
        VERTICES      MODE        STATUS  TOTAL  COMPLETED  RUNNING  PENDING  FAILED  KILLED  
----------------------------------------------------------------------------------------------
Map 1 .......... container     SUCCEEDED      1          1        0        0       0       0  
Reducer 2 ...... container     SUCCEEDED      1          1        0        0       0       0  
----------------------------------------------------------------------------------------------
VERTICES: 02/02  [==========================>>] 100%  ELAPSED TIME: 3.96 s     
----------------------------------------------------------------------------------------------
INFO  : Completed executing command(queryId=hive_20201115234153_e5caf09f-11ec-4490-bdc3-0616eeb1c93a); Time taken: 4.694 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------+------------------+
| arrondissement  | anneeplantation  |
+-----------------+------------------+
| 5               | 1601             |
+-----------------+------------------+
1 row selected (4.966 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT arrondissement, anneeplantation FROM trees_internal WHERE anneeplantation = (SELECT MIN(anneeplantation) FROM trees_internal);

INFO  : Compiling command(queryId=hive_20201115234312_cc63da94-7f90-4572-9c0a-5ffd1e9375e3): SELECT arrondissement, anneeplantation FROM trees_internal WHERE anneeplantation = (SELECT MIN(anneeplantation) FROM trees_internal)
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:arrondissement, type:int, comment:null), FieldSchema(name:anneeplantation, type:int, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115234312_cc63da94-7f90-4572-9c0a-5ffd1e9375e3); Time taken: 0.19 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115234312_cc63da94-7f90-4572-9c0a-5ffd1e9375e3): SELECT arrondissement, anneeplantation FROM trees_internal WHERE anneeplantation = (SELECT MIN(anneeplantation) FROM trees_internal)
INFO  : Query ID = hive_20201115234312_cc63da94-7f90-4572-9c0a-5ffd1e9375e3
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20201115234312_cc63da94-7f90-4572-9c0a-5ffd1e9375e3
INFO  : Session is already open
INFO  : Dag name: SELECT arrondissement, ann...trees_internal) (Stage-1)
INFO  : Setting tez.task.scale.memory.reserve-fraction to 0.30000001192092896
INFO  : Status: Running (Executing on YARN cluster with App id application_1603290159664_5419)

----------------------------------------------------------------------------------------------
        VERTICES      MODE        STATUS  TOTAL  COMPLETED  RUNNING  PENDING  FAILED  KILLED  
----------------------------------------------------------------------------------------------
Map 2 .......... container     SUCCEEDED      1          1        0        0       0       0  
Reducer 3 ...... container     SUCCEEDED      1          1        0        0       0       0  
Map 1 .......... container     SUCCEEDED      1          1        0        0       0       0  
----------------------------------------------------------------------------------------------
VERTICES: 03/03  [==========================>>] 100%  ELAPSED TIME: 7.20 s     
----------------------------------------------------------------------------------------------
INFO  : Completed executing command(queryId=hive_20201115234312_cc63da94-7f90-4572-9c0a-5ffd1e9375e3); Time taken: 7.89 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------+------------------+
| arrondissement  | anneeplantation  |
+-----------------+------------------+
| 5               | 1601             |
+-----------------+------------------+
1 row selected (8.156 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

The district n°5 indeed contains the oldest tree.

- A query that displays the district that contains the most trees

For this query, the same as the last query applies.

```sql
SELECT arrondissement, COUNT(*) AS n_trees FROM trees_internal GROUP BY arrondissement ORDER BY n_trees DESC LIMIT 1;
SELECT arrondissement, COUNT(*) FROM trees_internal GROUP BY arrondissement HAVING COUNT(*) = (SELECT COUNT(*) AS n_trees FROM trees_internal GROUP BY arrondissement ORDER BY n_trees DESC LIMIT 1);
```

```bash
0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT arrondissement, COUNT(*) AS n_trees FROM trees_internal GROUP BY arrondissement ORDER BY n_trees DESC LIMIT 1;

INFO  : Compiling command(queryId=hive_20201115234952_29e98355-21dc-441c-9add-895ed4985fdc): SELECT arrondissement, COUNT(*) AS n_trees FROM trees_internal GROUP BY arrondissement ORDER BY n_trees DESC LIMIT 1
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:arrondissement, type:int, comment:null), FieldSchema(name:n_trees, type:bigint, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201115234952_29e98355-21dc-441c-9add-895ed4985fdc); Time taken: 0.302 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201115234952_29e98355-21dc-441c-9add-895ed4985fdc): SELECT arrondissement, COUNT(*) AS n_trees FROM trees_internal GROUP BY arrondissement ORDER BY n_trees DESC LIMIT 1
INFO  : Query ID = hive_20201115234952_29e98355-21dc-441c-9add-895ed4985fdc
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20201115234952_29e98355-21dc-441c-9add-895ed4985fdc
INFO  : Session is already open
INFO  : Dag name: SELECT arrondissement, COUNT(*) AS n_tre...1 (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1603290159664_5419)

----------------------------------------------------------------------------------------------
        VERTICES      MODE        STATUS  TOTAL  COMPLETED  RUNNING  PENDING  FAILED  KILLED  
----------------------------------------------------------------------------------------------
Map 1 .......... container     SUCCEEDED      1          1        0        0       0       0  
Reducer 2 ...... container     SUCCEEDED      1          1        0        0       0       0  
Reducer 3 ...... container     SUCCEEDED      1          1        0        0       0       0  
----------------------------------------------------------------------------------------------
VERTICES: 03/03  [==========================>>] 100%  ELAPSED TIME: 3.76 s     
----------------------------------------------------------------------------------------------
INFO  : Completed executing command(queryId=hive_20201115234952_29e98355-21dc-441c-9add-895ed4985fdc); Time taken: 4.441 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------+----------+
| arrondissement  | n_trees  |
+-----------------+----------+
| 16              | 36       |
+-----------------+----------+
1 row selected (4.819 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:> SELECT arrondissement, COUNT(*) FROM trees_internal GROUP BY arrondissement HAVING COUNT(*) = (SELECT COUNT(*) AS n_trees FROM trees_internal GROUP BY arrondissement ORDER BY n_trees DESC LIMIT 1);

INFO  : Compiling command(queryId=hive_20201116161132_ec11b83c-06d4-4171-a5b1-8e464bd0bd60): SELECT arrondissement, COUNT(*) FROM trees_internal GROUP BY arrondissement HAVING COUNT(*) = (SELECT COUNT(*) AS n_trees FROM trees_internal GROUP BY arrondissement ORDER BY n_trees DESC LIMIT 1)
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Warning: Map Join MAPJOIN[49][bigTable=?] in task 'Reducer 4' is a cross product
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:arrondissement, type:int, comment:null), FieldSchema(name:_c1, type:bigint, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20201116161132_ec11b83c-06d4-4171-a5b1-8e464bd0bd60); Time taken: 0.431 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20201116161132_ec11b83c-06d4-4171-a5b1-8e464bd0bd60): SELECT arrondissement, COUNT(*) FROM trees_internal GROUP BY arrondissement HAVING COUNT(*) = (SELECT COUNT(*) AS n_trees FROM trees_internal GROUP BY arrondissement ORDER BY n_trees DESC LIMIT 1)
INFO  : Query ID = hive_20201116161132_ec11b83c-06d4-4171-a5b1-8e464bd0bd60
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20201116161132_ec11b83c-06d4-4171-a5b1-8e464bd0bd60
INFO  : Tez session hasn't been created yet. Opening session
INFO  : Dag name: SELECT arrondissement, COUNT(*) FROM tr...1) (Stage-1)
INFO  : Setting tez.task.scale.memory.reserve-fraction to 0.30000001192092896
INFO  : Status: Running (Executing on YARN cluster with App id application_1603290159664_5433)

INFO  : Completed executing command(queryId=hive_20201116161132_ec11b83c-06d4-4171-a5b1-8e464bd0bd60); Time taken: 16.098 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager
+-----------------+------+
| arrondissement  | _c1  |
+-----------------+------+
| 16              | 36   |
+-----------------+------+
1 row selected (16.654 seconds)

0: jdbc:hive2://hadoop-master01.efrei.online:>
```

The district n°16 is indeed the district with the highest number of trees.
