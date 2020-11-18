# Session 6 - Apache HBase

# 1. HBase Client

## 1.1. First Commands

### 1.1.1. Base Commands

Once again, we connect to the edge using SSH:

```bash
$ ssh istepanian@hadoop-edge01.efrei.online

Welcome to EFREI Hadoop Cluster

Password:
Last login: Mon Nov 16 16:11:01 2020 from XX-XXX-XX-XX.XXX.XXXXXXX.XXX

-sh-4.2$
```

It isn't necessary to manually initialize a new Kerberos ticket, as it is automatically created.

```bash
-sh-4.2$ klist

Ticket cache: FILE:/tmp/krb5cc_20170518
Default principal: istepanian@EFREI.ONLINE

Valid starting       Expires              Service principal
17/11/2020 15:27:17  18/11/2020 15:27:17  krbtgt/EFREI.ONLINE@EFREI.ONLINE
	renew until 24/11/2020 15:27:17
```

We can launch the HBase Client (Shell) by using the `hbase shell` command.

```bash
-sh-4.2$ hbase shell

SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hadoop/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hdp/3.1.5.0-152/hbase/lib/client-facing-thirdparty/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
HBase Shell
Use "help" to get list of supported commands.
Use "exit" to quit this interactive shell.
For Reference, please visit: http://hbase.apache.org/2.0/book.html#shell
Version 2.1.6.3.1.5.0-152, rUnknown, Thu Dec 12 20:16:57 UTC 2019
Took 0.0039 seconds

hbase(main):001:0> status
1 active master, 1 backup masters, 3 servers, 0 dead, 13.3333 average load
Took 0.4078 seconds                                                             
hbase(main):002:0> version
2.1.6.3.1.5.0-152, rUnknown, Thu Dec 12 20:16:57 UTC 2019
Took 0.0007 seconds                                                                                                                                
hbase(main):003:0> whoami
istepanian@EFREI.ONLINE (auth:KERBEROS)
    groups: istepanian, hadoop-users
Took 0.0297 seconds                                                                                                                                
hbase(main):004:0> list

TABLE                                                                                                                                              
ATLAS_ENTITY_AUDIT_EVENTS                                                                                                                          
agoncalves:library                                                                                                                                 
agoubeau:library                                                                                                                                   
atlas_titan                                                                                                                                        
ccarayon:library                                                                                                                                   
cloupec:library                                                                                                                                    
cmarcou:library                                                                                                                                    
cspatz:library                                                                                                                                     
dsonethavy:library                                                                                                                                 
enitschke:library                                                                                                                                  
fshonggeu:library                                                                                                                                  
gjaouen:library                                                                                                                                    
gshanmuganathapillai:library                                                                                                                       
idelaronciere:library                                                                                                                              
imuntz:library                                                                                                                                     
jly:library                                                                                                                                        
jtang:library                                                                                                                                      
jtran:library                                                                                                                                      
kkouadio:library                                                                                                                                   
lbenyamin:library                                                                                                                                  
lboumriche:library                                                                                                                                 
ldelthil:library                                                                                                                                   
lvaio:library                                                                                                                                      
mbartier:library                                                                                                                                   
mbury:library                                                                                                                                      
mebrahim:library                                                                                                                                   
pchen:library                                                                                                                                      
pdorffer:library                                                                                                                                   
psalvaudon:library                                                                                                                                 
qcourtois:library                                                                                                                                  
skeutcha:library                                                                                                                                   
tdura:library                                                                                                                                      
32 row(s)
Took 0.0391 seconds                                                                                                                                
=> ["ATLAS_ENTITY_AUDIT_EVENTS", "agoncalves:library", "agoubeau:library", "atlas_titan", "ccarayon:library", "cloupec:library", "cmarcou:library", "cspatz:library", "dsonethavy:library", "enitschke:library", "fshonggeu:library", "gjaouen:library", "gshanmuganathapillai:library", "idelaronciere:library", "imuntz:library", "jly:library", "jtang:library", "jtran:library", "kkouadio:library", "lbenyamin:library", "lboumriche:library", "ldelthil:library", "lvaio:library", "mbartier:library", "mbury:library", "mebrahim:library", "pchen:library", "pdorffer:library", "psalvaudon:library", "qcourtois:library", "skeutcha:library", "tdura:library"]

hbase(main):005:0> exit

-sh-4.2$
```

`list` allows us to see all the tables created in the system; we can see the namespaces (and tables) created by the other users.

## 1.1.2. Creating Your Own Namespace

We can see all available HBase commands using the `help` command.

```bash
hbase(main):001:0> help

HBase Shell, version 2.1.6.3.1.5.0-152, rUnknown, Thu Dec 12 20:16:57 UTC 2019
Type 'help "COMMAND"', (e.g. 'help "get"' -- the quotes are necessary) for help on a specific command.
Commands are grouped. Type 'help "COMMAND_GROUP"', (e.g. 'help "general"') for help on a command group.

COMMAND GROUPS:
  Group name: general
  Commands: processlist, status, table_help, version, whoami

  Group name: ddl
  Commands: alter, alter_async, alter_status, clone_table_schema, create, describe, disable, disable_all, drop, drop_all, enable, enable_all, exists, get_table, is_disabled, is_enabled, list, list_regions, locate_region, show_filters

  Group name: namespace
  Commands: alter_namespace, create_namespace, describe_namespace, drop_namespace, list_namespace, list_namespace_tables

  Group name: dml
  Commands: append, count, delete, deleteall, get, get_counter, get_splits, incr, put, scan, truncate, truncate_preserve

  Group name: tools
  Commands: assign, balance_switch, balancer, balancer_enabled, catalogjanitor_enabled, catalogjanitor_run, catalogjanitor_switch, cleaner_chore_enabled, cleaner_chore_run, cleaner_chore_switch, clear_block_cache, clear_compaction_queues, clear_deadservers, close_region, compact, compact_rs, compaction_state, flush, hbck_chore_run, is_in_maintenance_mode, list_deadservers, major_compact, merge_region, move, normalize, normalizer_enabled, normalizer_switch, split, splitormerge_enabled, splitormerge_switch, stop_master, stop_regionserver, trace, unassign, wal_roll, zk_dump

  Group name: replication
  Commands: add_peer, append_peer_exclude_namespaces, append_peer_exclude_tableCFs, append_peer_namespaces, append_peer_tableCFs, disable_peer, disable_table_replication, enable_peer, enable_table_replication, get_peer_config, list_peer_configs, list_peers, list_replicated_tables, remove_peer, remove_peer_exclude_namespaces, remove_peer_exclude_tableCFs, remove_peer_namespaces, remove_peer_tableCFs, set_peer_bandwidth, set_peer_exclude_namespaces, set_peer_exclude_tableCFs, set_peer_namespaces, set_peer_replicate_all, set_peer_serial, set_peer_tableCFs, show_peer_tableCFs, update_peer_config

  Group name: snapshots
  Commands: clone_snapshot, delete_all_snapshot, delete_snapshot, delete_table_snapshots, list_snapshots, list_table_snapshots, restore_snapshot, snapshot

  Group name: configuration
  Commands: update_all_config, update_config

  Group name: quotas
  Commands: list_quota_snapshots, list_quota_table_sizes, list_quotas, list_snapshot_sizes, set_quota

  Group name: security
  Commands: grant, list_security_capabilities, revoke, user_permission

  Group name: procedures
  Commands: list_locks, list_procedures

  Group name: visibility labels
  Commands: add_labels, clear_auths, get_auths, list_labels, set_auths, set_visibility

  Group name: rsgroup
  Commands: add_rsgroup, balance_rsgroup, get_rsgroup, get_server_rsgroup, get_table_rsgroup, list_rsgroups, move_namespaces_rsgroup, move_servers_namespaces_rsgroup, move_servers_rsgroup, move_servers_tables_rsgroup, move_tables_rsgroup, remove_rsgroup, remove_servers_rsgroup

SHELL USAGE:
Quote all names in HBase Shell such as table and column names.  Commas delimit
command parameters.  Type <RETURN> after entering a command to run it.
Dictionaries of configuration used in the creation and alteration of tables are
Ruby Hashes. They look like this:

  {'key1' => 'value1', 'key2' => 'value2', ...}

and are opened and closed with curley-braces.  Key/values are delimited by the
'=>' character combination.  Usually keys are predefined constants such as
NAME, VERSIONS, COMPRESSION, etc.  Constants do not need to be quoted.  Type
'Object.constants' to see a (messy) list of all constants in the environment.

If you are using binary keys or values and need to enter them in the shell, use
double-quote'd hexadecimal representation. For example:

  hbase> get 't1', "key\x03\x3f\xcd"
  hbase> get 't1', "key\003\023\011"
  hbase> put 't1', "test\xef\xff", 'f1:', "\x01\x33\x40"

The HBase shell is the (J)Ruby IRB with the above HBase-specific commands added.
For more on the HBase Shell, see http://hbase.apache.org/book.html
```

All the namespace commands are in the `Group name: namespace` section. The right command to create a namespace is `create_namespace "<name>"`

```bash
hbase(main):002:0> create_namespace "istepanian"

Took 0.5880 seconds                                                                                                                                
hbase(main):003:0> list_namespace

NAMESPACE                                                                                                                                          
agoncalves                                                                                                                                         
agoubeau                                                                                                                                           
aledeuf                                                                                                                                            
apauly                                                                                                                                             
apignerol                                                                                                                                          
avignaud                                                                                                                                           
ccarayon                                                                                                                                           
cloupec                                                                                                                                            
cmarcou                                                                                                                                            
comnes                                                                                                                                             
cspatz                                                                                                                                             
default                                                                                                                                            
dsonethavy                                                                                                                                         
ebertin                                                                                                                                            
enitschke                                                                                                                                          
fshonggeu                                                                                                                                          
gjaouen                                                                                                                                            
gshanmuganathapillai                                                                                                                               
hbase                                                                                                                                              
idelaronciere                                                                                                                                      
imuntz                                                                                                                                             
istepanian                                                                                                                                         
jly                                                                                                                                                
jtang                                                                                                                                              
jtran                                                                                                                                              
kkouadio                                                                                                                                           
lbenyamin                                                                                                                                          
lboumriche                                                                                                                                         
ldelthil                                                                                                                                           
lgailhac                                                                                                                                           
lvaio                                                                                                                                              
mbartier                                                                                                                                           
mbury                                                                                                                                              
mebrahim                                                                                                                                           
meccher                                                                                                                                            
mhatoum                                                                                                                                            
nsahnou                                                                                                                                            
oallouache                                                                                                                                         
pchen                                                                                                                                              
pdorffer                                                                                                                                           
psalvaudon                                                                                                                                         
qcourtois                                                                                                                                          
rcailliot                                                                                                                                          
sborongobialibawa                                                                                                                                  
skeutcha                                                                                                                                           
ssedraoui                                                                                                                                          
tdura                                                                                                                                              
ttea                                                                                                                                               
vserena                                                                                                                                            
49 row(s)
Took 0.0434 seconds                                                                                                                                
hbase(main):004:0>
```

Our namespace has correctly been created.

## Creating a Table

To easily create the table and precise both the name and the number of versions of the column families, we write the command using Ruby notation.

```bash
hbase(main):005:0> create 'istepanian:library', {NAME => 'author', VERSIONS => 2},{NAME => 'book', VERSIONS => 3}
Created table istepanian:library
Took 0.7625 seconds                                                                                                                                
=> Hbase::Table - istepanian:library

hbase(main):006:0> describe 'istepanian:library'

Table istepanian:library is ENABLED                                                                                                                
istepanian:library                                                                                                                                 
COLUMN FAMILIES DESCRIPTION                                                                                                                        
{NAME => 'author', VERSIONS => '2', EVICT_BLOCKS_ON_CLOSE => 'false', NEW_VERSION_BEHAVIOR => 'false', KEEP_DELETED_CELLS => 'FALSE', CACHE_DATA_ON
_WRITE => 'false', DATA_BLOCK_ENCODING => 'NONE', TTL => 'FOREVER', MIN_VERSIONS => '0', REPLICATION_SCOPE => '0', BLOOMFILTER => 'ROW', CACHE_INDE
X_ON_WRITE => 'false', IN_MEMORY => 'false', CACHE_BLOOMS_ON_WRITE => 'false', PREFETCH_BLOCKS_ON_OPEN => 'false', COMPRESSION => 'NONE', BLOCKCACH
E => 'true', BLOCKSIZE => '65536'}                                                                                                                 
{NAME => 'book', VERSIONS => '3', EVICT_BLOCKS_ON_CLOSE => 'false', NEW_VERSION_BEHAVIOR => 'false', KEEP_DELETED_CELLS => 'FALSE', CACHE_DATA_ON_W
RITE => 'false', DATA_BLOCK_ENCODING => 'NONE', TTL => 'FOREVER', MIN_VERSIONS => '0', REPLICATION_SCOPE => '0', BLOOMFILTER => 'ROW', CACHE_INDEX_
ON_WRITE => 'false', IN_MEMORY => 'false', CACHE_BLOOMS_ON_WRITE => 'false', PREFETCH_BLOCKS_ON_OPEN => 'false', COMPRESSION => 'NONE', BLOCKCACHE
=> 'true', BLOCKSIZE => '65536'}                                                                                                                   
2 row(s)
Took 0.1385 seconds                                                                                                                                
hbase(main):007:0> scan 'istepanian:library'

ROW                                    COLUMN+CELL                                                                                                  
0 row(s)
Took 0.1325 seconds                                                                                                                                 
hbase(main):008:0> is_enabled 'istepanian:library'

true                                                                                                                                                
Took 0.0113 seconds                                                                                                                                 
=> true

hbase(main):009:0>
```

The table is enabled by default.

## 1.1.4. Adding Values to Tables

Using put <table> <row_key> <column> <value>, we can create our new rows 'vhugo' and 'jverne' in our newly created table `library`.

```bash
hbase(main):003:0> put 'istepanian:library', 'vhugo', 'author:lastname', 'Hugo'
Took 0.0070 seconds  

hbase(main):004:0> put 'istepanian:library', 'vhugo', 'book:title', 'La Légende des siècles'
Took 0.0050 seconds                                                                                                                                 
hbase(main):005:0> put 'istepanian:library', 'vhugo', 'book:categ', 'Poèmes'
Took 0.0061 seconds                                                                                                                                 
hbase(main):006:0> put 'istepanian:library', 'vhugo', 'book:year', 1855
Took 0.0047 seconds                                                                                                                                 
hbase(main):007:0> put 'istepanian:library', 'vhugo', 'book:year', 1877
Took 0.0048 seconds                                                                                                                                 
hbase(main):008:0> put 'istepanian:library', 'vhugo', 'book:year', 1883
Took 0.0048 seconds                                                                                                                                 
hbase(main):009:0> put 'istepanian:library', 'jverne', 'author:firstname', 'Jules'
Took 0.0046 seconds                                                                                                                                 
hbase(main):010:0> put 'istepanian:library', 'jverne', 'author:lastname', 'Verne'
Took 0.0045 seconds                                                                                                                                 
hbase(main):011:0> put 'istepanian:library', 'jverne', 'book:publisher', 'Hetzel'
Took 0.0049 seconds                                                                                                                                 
hbase(main):012:0> put 'istepanian:library', 'jverne', 'book:title', 'Face au drapeau'
Took 0.0048 seconds                                                                                                                                 
hbase(main):013:0> put 'istepanian:library', 'jverne', 'book:year', 1896
Took 0.0049 seconds                                                                                 

...

hbase(main):001:0> put 'istepanian:library', 'jverne', 'author:firstname', 'Jules'
Took 0.0060 seconds     

hbase(main):002:0> put 'istepanian:library', 'jverne', 'author:lastname', 'Verne'
Took 0.4529 seconds                                                                                                                                 
hbase(main):003:0> put 'istepanian:library', 'jverne', 'book:publisher', 'Hetzel'
Took 0.0044 seconds                                                                                                                                 
hbase(main):004:0> put 'istepanian:library', 'jverne', 'book:title', 'Face au drapeau'
Took 0.0067 seconds                                                                                                                                 
hbase(main):005:0> put 'istepanian:library', 'jverne', 'book:year', 1896
Took 0.0077 seconds
```

## 1.1.5. Counting Values

In the `Group name: ddl` of the `help` page, we can see that there is a `count` command. Using `help "count"`, we can get details about the command usage.

```bash
hbase(main):001:0> help "count"

Count the number of rows in a table.  Return value is the number of rows.
This operation may take a LONG time (Run '$HADOOP_HOME/bin/hadoop jar
hbase.jar rowcount' to run a counting mapreduce job). Current count is shown
every 1000 rows by default. Count interval may be optionally specified. Scan
caching is enabled on count scans by default. Default cache size is 10 rows.
If your rows are small in size, you may want to increase this
parameter. Examples:

 hbase> count 'ns1:t1'
 hbase> count 't1'
 hbase> count 't1', INTERVAL => 100000
 hbase> count 't1', CACHE => 1000
 hbase> count 't1', INTERVAL => 10, CACHE => 1000
 hbase> count 't1', FILTER => "
    (QualifierFilter (>=, 'binary:xyz')) AND (TimestampsFilter ( 123, 456))"
 hbase> count 't1', COLUMNS => ['c1', 'c2'], STARTROW => 'abc', STOPROW => 'xyz'

The same commands also can be run on a table reference. Suppose you had a reference
t to table 't1', the corresponding commands would be:

 hbase> t.count
 hbase> t.count INTERVAL => 100000
 hbase> t.count CACHE => 1000
 hbase> t.count INTERVAL => 10, CACHE => 1000
 hbase> t.count FILTER => "
    (QualifierFilter (>=, 'binary:xyz')) AND (TimestampsFilter ( 123, 456))"
 hbase> t.count COLUMNS => ['c1', 'c2'], STARTROW => 'abc', STOPROW => 'xyz'

hbase(main):002:0> count 'istepanian:library'

2 row(s)
Took 0.3549 seconds                                                                                                                                 
=> 2

hbase(main):003:0>
```

There are currently two rows in our table (vhugo and jverne).

## 1.1.6. Retrieving Values

We use the `get` command to retrieve the value with different queries.

```bash
hbase(main):003:0> get 'istepanian:library', 'vhugo'
COLUMN                                 CELL                                                                                                         
 author:firstname                      timestamp=1605626539551, value=Victor                                                                        
 author:lastname                       timestamp=1605626571926, value=Hugo                                                                          
 book:categ                            timestamp=1605626539605, value=Po\xC3\xA8mes                                                                 
 book:title                            timestamp=1605626539575, value=La L\xC3\xA9gende des si\xC3\xA8cles                                          
 book:year                             timestamp=1605626539664, value=1883                                                                          
1 row(s)
Took 0.0736 seconds                                                                                                                                 
hbase(main):004:0> get 'istepanian:library', 'vhugo','author'
COLUMN                                 CELL                                                                                                         
 author:firstname                      timestamp=1605626539551, value=Victor                                                                        
 author:lastname                       timestamp=1605626571926, value=Hugo                                                                          
1 row(s)
Took 0.0082 seconds                                                                                                                                 
hbase(main):005:0> get 'istepanian:library', 'vhugo','author:firstname'
COLUMN                                 CELL                                                                                                         
 author:firstname                      timestamp=1605626539551, value=Victor                                                                        
1 row(s)
Took 0.0061 seconds                                                                                                                                 
hbase(main):006:0> get 'istepanian:library', 'jverne', COLUMN=>'book'
COLUMN                                 CELL                                                                                                         
 book:publisher                        timestamp=1605626989362, value=Hetzel                                                                        
 book:title                            timestamp=1605626989392, value=Face au drapeau                                                               
 book:year                             timestamp=1605627001246, value=1896                                                                          
1 row(s)
Took 0.0096 seconds                                                                                                                                 
hbase(main):007:0> get 'istepanian:library', 'jverne', COLUMN=>'book:title'
COLUMN                                 CELL                                                                                                         
 book:title                            timestamp=1605626989392, value=Face au drapeau                                                               
1 row(s)
Took 0.0059 seconds                                                                                                                                 
hbase(main):008:0> get 'istepanian:library', 'jverne', COLUMN=>['book:title','book:year','book:publisher']
COLUMN                                 CELL                                                                                                         
 book:publisher                        timestamp=1605626989362, value=Hetzel                                                                        
 book:title                            timestamp=1605626989392, value=Face au drapeau                                                               
 book:year                             timestamp=1605627001246, value=1896                                                                          
1 row(s)
Took 0.0071 seconds                                                                                                                                 
hbase(main):009:0> get 'istepanian:library', 'jverne', FILTER=>"ValueFilter(=,'binary:Jules')"
COLUMN                                 CELL                                                                                                         
 author:firstname                      timestamp=1605627013567, value=Jules                                                                         
1 row(s)
Took 0.0530 seconds                                                                                                                                 
hbase(main):010:0>
```

For the given row keys, we observe each cell described with the column/version/value logic. The column is identified with the column family and the column name, the version is identified by the timestamp, and the value is the data contained in the cell.

## 1.1.7. Tuple browsing

We will use the `scan` command to browse the row tuples, similarly to the `get` command. To get more information about the usage of the `scan` command, we will check out `help "scan"`.

```bash
hbase(main):001:0> help "scan"
Scan a table; pass table name and optionally a dictionary of scanner
specifications.  Scanner specifications may include one or more of:
TIMERANGE, FILTER, LIMIT, STARTROW, STOPROW, ROWPREFIXFILTER, TIMESTAMP,
MAXLENGTH, COLUMNS, CACHE, RAW, VERSIONS, ALL_METRICS, METRICS,
REGION_REPLICA_ID, ISOLATION_LEVEL, READ_TYPE, ALLOW_PARTIAL_RESULTS,
BATCH or MAX_RESULT_SIZE

If no columns are specified, all columns will be scanned.
To scan all members of a column family, leave the qualifier empty as in
'col_family'.

The filter can be specified in two ways:
1. Using a filterString - more information on this is available in the
Filter Language document attached to the HBASE-4176 JIRA
2. Using the entire package name of the filter.

If you wish to see metrics regarding the execution of the scan, the
ALL_METRICS boolean should be set to true. Alternatively, if you would
prefer to see only a subset of the metrics, the METRICS array can be
defined to include the names of only the metrics you care about.

Some examples:

  hbase> scan 'hbase:meta'
  hbase> scan 'hbase:meta', {COLUMNS => 'info:regioninfo'}
  hbase> scan 'ns1:t1', {COLUMNS => ['c1', 'c2'], LIMIT => 10, STARTROW => 'xyz'}
  hbase> scan 't1', {COLUMNS => ['c1', 'c2'], LIMIT => 10, STARTROW => 'xyz'}
  hbase> scan 't1', {COLUMNS => 'c1', TIMERANGE => [1303668804000, 1303668904000]}
  hbase> scan 't1', {REVERSED => true}
  hbase> scan 't1', {ALL_METRICS => true}
  hbase> scan 't1', {METRICS => ['RPC_RETRIES', 'ROWS_FILTERED']}
  hbase> scan 't1', {ROWPREFIXFILTER => 'row2', FILTER => "
    (QualifierFilter (>=, 'binary:xyz')) AND (TimestampsFilter ( 123, 456))"}
  hbase> scan 't1', {FILTER =>
    org.apache.hadoop.hbase.filter.ColumnPaginationFilter.new(1, 0)}
  hbase> scan 't1', {CONSISTENCY => 'TIMELINE'}
  hbase> scan 't1', {ISOLATION_LEVEL => 'READ_UNCOMMITTED'}
  hbase> scan 't1', {MAX_RESULT_SIZE => 123456}
For setting the Operation Attributes
  hbase> scan 't1', { COLUMNS => ['c1', 'c2'], ATTRIBUTES => {'mykey' => 'myvalue'}}
  hbase> scan 't1', { COLUMNS => ['c1', 'c2'], AUTHORIZATIONS => ['PRIVATE','SECRET']}
For experts, there is an additional option -- CACHE_BLOCKS -- which
switches block caching for the scanner on (true) or off (false).  By
default it is enabled.  Examples:

  hbase> scan 't1', {COLUMNS => ['c1', 'c2'], CACHE_BLOCKS => false}

Also for experts, there is an advanced option -- RAW -- which instructs the
scanner to return all cells (including delete markers and uncollected deleted
cells). This option cannot be combined with requesting specific COLUMNS.
Disabled by default.  Example:

  hbase> scan 't1', {RAW => true, VERSIONS => 10}

There is yet another option -- READ_TYPE -- which instructs the scanner to
use a specific read type. Example:

  hbase> scan 't1', {READ_TYPE => 'PREAD'}

Besides the default 'toStringBinary' format, 'scan' supports custom formatting
by column.  A user can define a FORMATTER by adding it to the column name in
the scan specification.  The FORMATTER can be stipulated:

 1. either as a org.apache.hadoop.hbase.util.Bytes method name (e.g, toInt, toString)
 2. or as a custom class followed by method name: e.g. 'c(MyFormatterClass).format'.

Example formatting cf:qualifier1 and cf:qualifier2 both as Integers:
  hbase> scan 't1', {COLUMNS => ['cf:qualifier1:toInt',
    'cf:qualifier2:c(org.apache.hadoop.hbase.util.Bytes).toInt'] }

Note that you can specify a FORMATTER by column only (cf:qualifier). You can set a
formatter for all columns (including, all key parts) using the "FORMATTER"
and "FORMATTER_CLASS" options. The default "FORMATTER_CLASS" is
"org.apache.hadoop.hbase.util.Bytes".

  hbase> scan 't1', {FORMATTER => 'toString'}
  hbase> scan 't1', {FORMATTER_CLASS => 'org.apache.hadoop.hbase.util.Bytes', FORMATTER => 'toString'}

Scan can also be used directly from a table, by first getting a reference to a
table, like such:

  hbase> t = get_table 't'
  hbase> t.scan

Note in the above situation, you can still provide all the filtering, columns,
options, etc as described above.

hbase(main):002:0> scan 'istepanian:library'
ROW                            COLUMN+CELL                                                                           
 jverne                        column=author:firstname, timestamp=1605627013567, value=Jules                         
 jverne                        column=author:lastname, timestamp=1605626989336, value=Verne                          
 jverne                        column=book:publisher, timestamp=1605626989362, value=Hetzel                          
 jverne                        column=book:title, timestamp=1605626989392, value=Face au drapeau                     
 jverne                        column=book:year, timestamp=1605627001246, value=1896                                 
 vhugo                         column=author:firstname, timestamp=1605626539551, value=Victor                        
 vhugo                         column=author:lastname, timestamp=1605626571926, value=Hugo                           
 vhugo                         column=book:categ, timestamp=1605626539605, value=Po\xC3\xA8mes                       
 vhugo                         column=book:title, timestamp=1605626539575, value=La L\xC3\xA9gende des si\xC3\xA8cles
 vhugo                         column=book:year, timestamp=1605626539664, value=1883                                 
2 row(s)
Took 0.3744 seconds                                                                                                  
hbase(main):003:0> scan 'istepanian:library', {COLUMNS => 'book'}
ROW                            COLUMN+CELL                                                                           
 jverne                        column=book:publisher, timestamp=1605626989362, value=Hetzel                          
 jverne                        column=book:title, timestamp=1605626989392, value=Face au drapeau                     
 jverne                        column=book:year, timestamp=1605627001246, value=1896                                 
 vhugo                         column=book:categ, timestamp=1605626539605, value=Po\xC3\xA8mes                       
 vhugo                         column=book:title, timestamp=1605626539575, value=La L\xC3\xA9gende des si\xC3\xA8cles
 vhugo                         column=book:year, timestamp=1605626539664, value=1883                                 
2 row(s)
Took 0.0197 seconds                                                                                                  
hbase(main):004:0> scan 'istepanian:library', {COLUMNS => 'book:year'}
ROW                            COLUMN+CELL                                                                           
 jverne                        column=book:year, timestamp=1605627001246, value=1896                                 
 vhugo                         column=book:year, timestamp=1605626539664, value=1883                                 
2 row(s)
Took 0.0096 seconds                                                                                                  
hbase(main):005:0> 
```

We simply use the Ruby Hashes notation to access data per column family (or column family and column name). To find rows with a row index starting with a specific symbol or a specific symbol range, we can use the `STARTROW` and `STOP` keywords; if we use the `FILTER` keyword, we can use the `RowFilter` filter. To get data from specific columns using a filter, we can use `FamilyFilter` for the column family and `QualifierFilter` for the column name; to chain filter, we can use the keywords `AND` or `OR`. The `SingleColumnValueFilter` filter can be used to scan cells based on their value for a given column. A complete list of HBase filters can be found in the Apache Documentations (http://hbase.apache.org/devapidocs/org/apache/hadoop/hbase/filter/package-summary.html).

```bash
hbase(main):005:0> scan 'istepanian:library', {STARTROW => 'a', STOPROW => 'n', COLUMNS => 'author'}
ROW                            COLUMN+CELL                                                                           
 jverne                        column=author:firstname, timestamp=1605627013567, value=Jules                         
 jverne                        column=author:lastname, timestamp=1605626989336, value=Verne                          
1 row(s)
Took 0.0386 seconds 

hbase(main):006:0> scan 'istepanian:library', {FILTER=>"RowFilter(>=, 'binary:a') AND RowFilter(<=, 'binary:n') AND FamilyFilter(=, 'binary:author')"}
ROW                            COLUMN+CELL                                                                           
 jverne                        column=author:firstname, timestamp=1605627013567, value=Jules                         
 jverne                        column=author:lastname, timestamp=1605626989336, value=Verne                          
1 row(s)
Took 0.0436 seconds
hbase(main):007:0> scan 'istepanian:library', {FILTER=>"FamilyFilter (=, 'binary:author') AND QualifierFilter (=, 'binary:firstname')"}
ROW                            COLUMN+CELL                                                                           
 jverne                        column=author:firstname, timestamp=1605627013567, value=Jules                         
 vhugo                         column=author:firstname, timestamp=1605626539551, value=Victor                        
2 row(s)
Took 0.0287 seconds
                     
hbase(main):008:0>scan ``
```
