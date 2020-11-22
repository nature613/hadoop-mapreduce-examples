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

### 1.1.2. Creating Your Own Namespace

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

### 1.1.3. Creating a Table

To easily create the table and precise both the name and the number of versions of the column families, we write the command using Ruby notation.

```bash
hbase(main):005:0> create 'istepanian:library', {NAME => 'author', VERSIONS => 2}, {NAME => 'book', VERSIONS => 3}
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

### 1.1.4. Adding Values to Tables

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
```

### 1.1.5. Counting Values

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

### 1.1.6. Retrieving Values

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

### 1.1.7. Tuple Browsing

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

We simply use the Ruby Hashes notation to access data per column family (or column family AND column name). To find rows with a row index starting with a specific symbol or a specific symbol range, we can use the `STARTROW` and `STOP` keywords; using filters (`FILTER`), we can also use the `RowFilter` (or `PrefixFilter` for a specific value). To get data from specific columns using a filter, we can use `FamilyFilter` for the column family and `QualifierFilter` for the column name; to chain filter, we can use the keywords `AND` or `OR`. The `SingleColumnValueFilter` filter can be used to scan cells based on their value for a given column. A complete list of HBase filters can be found in the Apache Documentations (http://hbase.apache.org/devapidocs/org/apache/hadoop/hbase/filter/package-summary.html).

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
                     
hbase(main):008:0> scan 'istepanian:library', {FILTER=>"SingleColumnValueFilter('book', 'title', =, 'binary:La Légende des siècles')"}
ROW                           COLUMN+CELL                                                                         
 vhugo                        column=author:firstname, timestamp=1605626539551, value=Victor                      
 vhugo                        column=author:lastname, timestamp=1605626571926, value=Hugo                         
 vhugo                        column=book:categ, timestamp=1605626539605, value=Po\xC3\xA8mes                     
 vhugo                        column=book:title, timestamp=1605626539575, value=La L\xC3\xA9gende des si\xC3\xA8cl
                              es                                                                                  
 vhugo                        column=book:year, timestamp=1605626539664, value=1883                               
1 row(s)
Took 0.0640 seconds

hbase(main):009:0> scan 'istepanian:library', {FILTER=>"SingleColumnValueFilter('book', 'year', <=, 'binary:1890')"}
ROW                           COLUMN+CELL                                                                         
 vhugo                        column=author:firstname, timestamp=1605626539551, value=Victor                      
 vhugo                        column=author:lastname, timestamp=1605626571926, value=Hugo                         
 vhugo                        column=book:categ, timestamp=1605626539605, value=Po\xC3\xA8mes                     
 vhugo                        column=book:title, timestamp=1605626539575, value=La L\xC3\xA9gende des si\xC3\xA8cl
                              es                                                                                  
 vhugo                        column=book:year, timestamp=1605626539664, value=1883                               
1 row(s)
Took 0.3437 seconds

hbase(main):010:0> scan 'istepanian:library', {FILTER=>"PrefixFilter('jv') AND ValueFilter(=,'regexstring:[A-Z]([a-z]+ ){2,}')"}
ROW                           COLUMN+CELL                                                                         
 jverne                       column=book:title, timestamp=1605626989392, value=Face au drapeau                   
1 row(s)
Took 0.0244 seconds
```

### 1.1.8. Updating a value

To showcase how versions work in HBase, we will overwrite the current values of the rows using successive `put` operations

```bash
hbase(main):001:0> put 'istepanian:library', 'vhugo', 'author:lastname', 'HAGO'
Took 0.0066 seconds

hbase(main):002:0> get 'istepanian:library', 'vhugo', COLUMNS=>'author', VERSIONS=>2
COLUMN                        CELL                                                                                
 author:firstname             timestamp=1605626539551, value=Victor                                               
 author:lastname              timestamp=1605983481397, value=HAGO                                                 
 author:lastname              timestamp=1605626571926, value=Hugo                                                 
1 row(s)
Took 0.0155 seconds 

hbase(main):003:0> put 'istepanian:library', 'vhugo', 'author:lastname', 'HUGO'
Took 0.3580 seconds                                                                                               
hbase(main):004:0> get 'istepanian:library', 'vhugo', COLUMNS=>'author', VERSIONS=>2
COLUMN                        CELL                                                                                
 author:firstname             timestamp=1605626539551, value=Victor                                               
 author:lastname              timestamp=1605984421312, value=HUGO                                                 
 author:lastname              timestamp=1605983481397, value=HAGO                                                 
1 row(s)
Took 0.0427 seconds

hbase(main):005:0> put 'istepanian:library', 'vhugo', 'author:firstname', 'Victor Marie'
Took 0.3452 seconds                          
                                                                     
hbase(main):006:0> put 'istepanian:library', 'vhugo', 'author:lastname', 'Hugo'
Took 0.0064 seconds          
                                                                                     
hbase(main):007:0> get 'istepanian:library', 'vhugo', 'author'
COLUMN                        CELL                                                                                
 author:firstname             timestamp=1605984517354, value=Victor Marie                                         
 author:lastname              timestamp=1605984526059, value=Hugo                                                 
1 row(s)
Took 0.0356 seconds     
                                                                                          
hbase(main):008:0> get 'istepanian:library', 'vhugo', COLUMNS=>'author'
COLUMN                        CELL                                                                                
 author:firstname             timestamp=1605984517354, value=Victor Marie                                         
 author:lastname              timestamp=1605984526059, value=Hugo                                                 
1 row(s)
Took 0.0169 seconds       
                                                                                        
hbase(main):009:0> get 'istepanian:library', 'vhugo', COLUMNS=>'author', VERSIONS=>2
COLUMN                        CELL                                                                                
 author:firstname             timestamp=1605984517354, value=Victor Marie                                         
 author:firstname             timestamp=1605626539551, value=Victor                                               
 author:lastname              timestamp=1605984526059, value=Hugo                                                 
 author:lastname              timestamp=1605984421312, value=HUGO                                                 
1 row(s)
Took 0.0164 seconds             
                                                                                  
hbase(main):010:0> 
```

As we can see, value are still conserved in memory after being overwritten; values will still be saved after `VERSIONS-1` overwrites (`VERSION` being one the property defined when a table is created), and then forgotten from the database.

### 1.1.9. Deleting a Value or a Column

To delete only the value for the timestamp of `author:name=HUGO`, we first check the usage of the `deleteall` command

```bash
hbase(main):001:0> help "deleteall"
Delete all cells in a given row; pass a table name, row, and optionally
a column and timestamp. Deleteall also support deleting a row range using a
row key prefix. Examples:

  hbase> deleteall 'ns1:t1', 'r1'
  hbase> deleteall 't1', 'r1'
  hbase> deleteall 't1', 'r1', 'c1'
  hbase> deleteall 't1', 'r1', 'c1', ts1
  hbase> deleteall 't1', 'r1', 'c1', ts1, {VISIBILITY=>'PRIVATE|SECRET'}

ROWPREFIXFILTER can be used to delete row ranges
  hbase> deleteall 't1', {ROWPREFIXFILTER => 'prefix'}
  hbase> deleteall 't1', {ROWPREFIXFILTER => 'prefix'}, 'c1'        //delete certain column family in the row ranges
  hbase> deleteall 't1', {ROWPREFIXFILTER => 'prefix'}, 'c1', ts1
  hbase> deleteall 't1', {ROWPREFIXFILTER => 'prefix'}, 'c1', ts1, {VISIBILITY=>'PRIVATE|SECRET'}

CACHE can be used to specify how many deletes batched to be sent to server at one time, default is 100
  hbase> deleteall 't1', {ROWPREFIXFILTER => 'prefix', CACHE => 100}


The same commands also can be run on a table reference. Suppose you had a reference
t to table 't1', the corresponding command would be:

  hbase> t.deleteall 'r1', 'c1', ts1, {VISIBILITY=>'PRIVATE|SECRET'}
  hbase> t.deleteall {ROWPREFIXFILTER => 'prefix', CACHE => 100}, 'c1', ts1, {VISIBILITY=>'PRIVATE|SECRET'}
```

To select only a specific timestamp, we use the fourth argument of the `deleteall` operation.

```bash
hbase(main):001:0> deleteall 'istepanian:library', 'vhugo', 'author:lastname', 1605984421312
Took 0.0106 seconds                               

hbase(main):002:0> get 'istepanian:library', 'vhugo', COLUMNS=>'author', VERSIONS=>2
COLUMN                        CELL                                                                               
 author:firstname             timestamp=1605984517354, value=Victor Marie                                        
 author:firstname             timestamp=1605626539551, value=Victor                                              
 author:lastname              timestamp=1605984526059, value=Hugo                                                
1 row(s)
Took 0.0107 seconds

hbase(main):002:0> scan 'istepanian:library', {COLUMNS => 'author:firstname'}
ROW                           COLUMN+CELL                                                                         
 jverne                       column=author:firstname, timestamp=1605627013567, value=Jules                       
1 row(s)
Took 0.0457 seconds

hbase(main):003:0> deleteall 'istepanian:library', 'vhugo'
Took 0.0070 seconds 

hbase(main):004:0> scan 'istepanian:library', {VERSIONS => 10}
ROW                           COLUMN+CELL                                                                         
 jverne                       column=author:firstname, timestamp=1605627013567, value=Jules                       
 jverne                       column=author:firstname, timestamp=1605626539683, value=Jules                       
 jverne                       column=author:lastname, timestamp=1605626989336, value=Verne                        
 jverne                       column=author:lastname, timestamp=1605626539700, value=Verne                        
 jverne                       column=book:publisher, timestamp=1605626989362, value=Hetzel                        
 jverne                       column=book:publisher, timestamp=1605626539719, value=Hetzel                        
 jverne                       column=book:title, timestamp=1605626989392, value=Face au drapeau                   
 jverne                       column=book:title, timestamp=1605626539736, value=Face au drapeau                   
 jverne                       column=book:year, timestamp=1605627001246, value=1896                               
 jverne                       column=book:year, timestamp=1605626539755, value=1896                               
1 row(s)
Took 0.0320 seconds
```

To showcase the `delete` operation, we first check the usage of the command:

```bash
hbase(main):005:0> help "delete"
Put a delete cell value at specified table/row/column and optionally
timestamp coordinates.  Deletes must match the deleted cell's
coordinates exactly.  When scanning, a delete cell suppresses older
versions. To delete a cell from  't1' at row 'r1' under column 'c1'
marked with the time 'ts1', do:

  hbase> delete 'ns1:t1', 'r1', 'c1', ts1
  hbase> delete 't1', 'r1', 'c1', ts1
  hbase> delete 't1', 'r1', 'c1', ts1, {VISIBILITY=>'PRIVATE|SECRET'}

The same command can also be run on a table reference. Suppose you had a reference
t to table 't1', the corresponding command would be:

  hbase> t.delete 'r1', 'c1',  ts1
  hbase> t.delete 'r1', 'c1',  ts1, {VISIBILITY=>'PRIVATE|SECRET'}
```

The command will put a new `delete` flag as the new cell values, making them invisible to `scan` and `get` operations. It will "erase" the latest value first.

```bash
hbase(main):006:0> scan 'istepanian:library', {VERSIONS => 10}
ROW                           COLUMN+CELL                                                                         
 jverne                       column=author:firstname, timestamp=1605627013567, value=Jules                       
 jverne                       column=author:firstname, timestamp=1605626539683, value=Jules                       
 jverne                       column=author:lastname, timestamp=1605626989336, value=Verne                        
 jverne                       column=author:lastname, timestamp=1605626539700, value=Verne                        
 jverne                       column=book:publisher, timestamp=1605626989362, value=Hetzel                        
 jverne                       column=book:publisher, timestamp=1605626539719, value=Hetzel                        
 jverne                       column=book:title, timestamp=1605626989392, value=Face au drapeau                   
 jverne                       column=book:title, timestamp=1605626539736, value=Face au drapeau                   
 jverne                       column=book:year, timestamp=1605627001246, value=1896                               
 jverne                       column=book:year, timestamp=1605626539755, value=1896                               
1 row(s)
Took 0.0144 seconds                                                                                               
hbase(main):007:0> delete 'istepanian:library', 'jverne', 'author:firstname'
Took 0.0118 seconds                                                                                               
hbase(main):008:0> scan 'istepanian:library', {VERSIONS => 10}
ROW                           COLUMN+CELL                                                                         
 jverne                       column=author:firstname, timestamp=1605626539683, value=Jules                       
 jverne                       column=author:lastname, timestamp=1605626989336, value=Verne                        
 jverne                       column=author:lastname, timestamp=1605626539700, value=Verne                        
 jverne                       column=book:publisher, timestamp=1605626989362, value=Hetzel                        
 jverne                       column=book:publisher, timestamp=1605626539719, value=Hetzel                        
 jverne                       column=book:title, timestamp=1605626989392, value=Face au drapeau                   
 jverne                       column=book:title, timestamp=1605626539736, value=Face au drapeau                   
 jverne                       column=book:year, timestamp=1605627001246, value=1896                               
 jverne                       column=book:year, timestamp=1605626539755, value=1896                               
1 row(s)
Took 0.0365 seconds                                                                                               
hbase(main):022:0> delete 'istepanian:library', 'jverne', 'author:firstname'
Took 0.0127 seconds                                                                                                                                                                                                                  
hbase(main):009:0> scan 'istepanian:library', {VERSIONS => 10}
ROW                                                        COLUMN+CELL                                                                                                                                                               
 jverne                                                    column=author:lastname, timestamp=1605626989336, value=Verne                                                                                                              
 jverne                                                    column=author:lastname, timestamp=1605626539700, value=Verne                                                                                                              
 jverne                                                    column=book:publisher, timestamp=1605626989362, value=Hetzel                                                                                                              
 jverne                                                    column=book:publisher, timestamp=1605626539719, value=Hetzel                                                                                                              
 jverne                                                    column=book:title, timestamp=1605626989392, value=Face au drapeau                                                                                                         
 jverne                                                    column=book:title, timestamp=1605626539736, value=Face au drapeau                                                                                                         
 jverne                                                    column=book:year, timestamp=1605627001246, value=1896                                                                                                                     
 jverne                                                    column=book:year, timestamp=1605626539755, value=1896                                                                                                                     
1 row(s)
Took 0.0256 seconds 
```

### 1.1.10. Deleting a Table

As we can see in the `help` menu, there is a `disable` command that allows us to disable a table before deleting it. Then, to delete the table, we use the `drop` command, just like in most DBMS software.

```bash
hbase(main):005:0> disable 'istepanian:library'
Took 1.1565 seconds                                                                                            
hbase(main):006:0> drop 'istepanian:library'
Took 0.4755 seconds

hbase(main):07:0> list_namespace_tables 'istepanian'
TABLE                                             
0 row(s)
Took 0.0113 seconds                   
=> []
```

## 1.2. Trees

### 1.2.1. Data Insertion

To insert our CSV file into a HBase table, we will use shell pipes and a simple Python script to convert the raw line-by-line data of the CSV files into HBase commands that will insert the table elements one by one. The first line that the script will output will `create` the table, and the next outputs will `put` the column values for each row into the table. We also need to avoid the first line of the CSV files, the header that contains the field names (we will hardcode the field names into script, as it will have use only for this specific case). Here is what such a script would look like:

```python
#!/ usr/bin/env python
""" script.py """

import sys
sys.stdout.write("create \"istepanian:trees\", {NAME => \"species\", VERSIONS => 2}, {NAME => \"information\", VERSIONS => 2}, {NAME => \"address\", VERSIONS => 2};\n")

count = 0
for line in sys.stdin:
    if count > 0:
        fields = line.split(";")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"species:GENRE\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"species:ESPECE\", \"" + fields[3] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"species:FAMILLE\", \"" + fields[4] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"species:NOM COMMUN\", \"" + fields[9] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"species:VARIETE\", \"" + fields[10] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"information:ANNEE PLANTATION\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"information:HAUTEUR\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"information:CIRCONFERENCE\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"address:GEOPOINT\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"address:ARRONDISSEMENT\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"address:ADRESSE\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"address:NOM_EV\", \"" + fields[2] + "\";\n")

    count += 1
```

We then pipe a `cat` of our `trees.csv` file into the script, and pipe the script's output into the HBase Shell (`hdfs dfs -cat trees.csv | python script.py | hbase shell`); we then `scan` our newly created `istepanian:trees` table to see that the script worked.

```bash
-sh-4.2$ hdfs dfs -cat trees.csv | python script.py | hbase shell
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
Took 0.0035 seconds                                                                                              
create "istepanian:trees", {NAME => "species", VERSIONS => 2}, {NAME => "information", VERSIONS => 2}, {NAME => "address", VERSIONS => 2};
put "istepanian:trees", "6", "species:GENRE", "Maclura";
put "istepanian:trees", "6", "species:ESPECE", "pomifera";
put "istepanian:trees", "6", "species:FAMILLE", "Moraceae";
put "istepanian:trees", "6", "species:NOM COMMUN", "Oranger des Osages";
put "istepanian:trees", "6", "species:VARIETE", "";
put "istepanian:trees", "6", "information:ANNEE PLANTATION", "Maclura";
put "istepanian:trees", "6", "information:HAUTEUR", "Maclura";
put "istepanian:trees", "6", "information:CIRCONFERENCE", "Maclura";
put "istepanian:trees", "6", "address:GEOPOINT", "Maclura";
put "istepanian:trees", "6", "address:ARRONDISSEMENT", "Maclura";
put "istepanian:trees", "6", "address:ADRESSE", "Maclura";
put "istepanian:trees", "6", "address:NOM_EV", "Maclura";
...
put "istepanian:trees", "86", "species:GENRE", "Platanus";
put "istepanian:trees", "86", "species:ESPECE", "orientalis";
put "istepanian:trees", "86", "species:FAMILLE", "Platanaceae";
put "istepanian:trees", "86", "species:NOM COMMUN", "Platane d'Orient";
put "istepanian:trees", "86", "species:VARIETE", "";
put "istepanian:trees", "86", "information:ANNEE PLANTATION", "Platanus";
put "istepanian:trees", "86", "information:HAUTEUR", "Platanus";
put "istepanian:trees", "86", "information:CIRCONFERENCE", "Platanus";
put "istepanian:trees", "86", "address:GEOPOINT", "Platanus";
put "istepanian:trees", "86", "address:ARRONDISSEMENT", "Platanus";
put "istepanian:trees", "86", "address:ADRESSE", "Platanus";
put "istepanian:trees", "86", "address:NOM_EV", "Platanus";
put "istepanian:trees", "91", "species:GENRE", "Acer";
put "istepanian:trees", "91", "species:ESPECE", "opalus";
put "istepanian:trees", "91", "species:FAMILLE", "Sapindaceae";
put "istepanian:trees", "91", "species:NOM COMMUN", "Erable d'Italie";
put "istepanian:trees", "91", "species:VARIETE", "";
put "istepanian:trees", "91", "information:ANNEE PLANTATION", "Acer";
put "istepanian:trees", "91", "information:HAUTEUR", "Acer";
put "istepanian:trees", "91", "information:CIRCONFERENCE", "Acer";
put "istepanian:trees", "91", "address:GEOPOINT", "Acer";
put "istepanian:trees", "91", "address:ARRONDISSEMENT", "Acer";
put "istepanian:trees", "91", "address:ADRESSE", "Acer";
put "istepanian:trees", "91", "address:NOM_EV", "Acer";


Created table istepanian:trees
Took 2.5275 seconds                                                                                              
Took 0.3480 seconds                                                                                              
Took 0.0058 seconds                                                                                              
Took 0.0047 seconds                                                                                              
Took 0.0047 seconds                                                                                              
Took 0.0053 seconds                                                                                              
Took 0.0046 seconds                                                                                              
Took 0.0057 seconds                                                                                              
Took 0.0045 seconds                                                                                              
Took 0.0042 seconds                                                                                              
Took 0.0034 seconds                                                                                              
Took 0.0033 seconds                                                                                              
Took 0.0032 seconds                                                                                              
Took 0.0031 seconds                                                                                              
Took 0.0033 seconds                                                                                              
Took 0.0034 seconds                                                                                              
Took 0.0044 seconds                                                                                              
Took 0.0032 seconds                                                                                              
Took 0.0033 seconds                                                                                              
...                                                        
Took 0.0041 seconds                                                                                              
Took 0.0040 seconds                                                                                              
Took 0.0038 seconds                                                                                              
Took 0.0038 seconds                                                                                              
Took 0.0041 seconds                                                                                              
Took 0.0038 seconds                                                                                              
Took 0.0037 seconds                                                                                              
Took 0.0040 seconds                                                                                              
Took 0.0036 seconds                                                                                              
Took 0.0038 seconds                                                                                              
Took 0.0037 seconds                                                                                              
Took 0.0036 seconds                                                                                                                                                        
Took 0.0039 seconds                                                                                              
Took 0.0030 seconds                                                                                                                                                              
Took 0.0029 seconds                                                                                              
Took 0.0028 seconds

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
Took 0.0026 seconds                                                                                              
hbase(main):001:0> scan 'istepanian:trees', {'LIMIT' => 5}
ROW                           COLUMN+CELL                                                                        
 1                            column=address:ADRESSE, timestamp=1606004999587, value=Corylus                     
 1                            column=address:ARRONDISSEMENT, timestamp=1606004999584, value=Corylus              
 1                            column=address:GEOPOINT, timestamp=1606004999581, value=Corylus                    
 1                            column=address:NOM_EV, timestamp=1606004999590, value=Corylus                      
 1                            column=information:ANNEE PLANTATION, timestamp=1606004999572, value=Corylus        
 1                            column=information:CIRCONFERENCE, timestamp=1606004999578, value=Corylus           
 1                            column=information:HAUTEUR, timestamp=1606004999575, value=Corylus                 
 1                            column=species:ESPECE, timestamp=1606004999559, value=colurna                      
 1                            column=species:FAMILLE, timestamp=1606004999562, value=Betulaceae                  
 1                            column=species:GENRE, timestamp=1606004999556, value=Corylus                       
 1                            column=species:NOM COMMUN, timestamp=1606004999565, value=Noisetier de Byzance     
 1                            column=species:VARIETE, timestamp=1606004999569, value=                            
 10                           column=address:ADRESSE, timestamp=1606004997708, value=Ginkgo                      
 10                           column=address:ARRONDISSEMENT, timestamp=1606004997704, value=Ginkgo               
 10                           column=address:GEOPOINT, timestamp=1606004997699, value=Ginkgo                     
 10                           column=address:NOM_EV, timestamp=1606004997712, value=Ginkgo                       
 10                           column=information:ANNEE PLANTATION, timestamp=1606004997686, value=Ginkgo         
 10                           column=information:CIRCONFERENCE, timestamp=1606004997695, value=Ginkgo            
 10                           column=information:HAUTEUR, timestamp=1606004997690, value=Ginkgo                  
 10                           column=species:ESPECE, timestamp=1606004997669, value=biloba                       
 10                           column=species:FAMILLE, timestamp=1606004997673, value=Ginkgoaceae                 
 10                           column=species:GENRE, timestamp=1606004997664, value=Ginkgo                        
 10                           column=species:NOM COMMUN, timestamp=1606004997678, value=Arbre aux quarante \xC3\x
                              A9cus                                                                              
 10                           column=species:VARIETE, timestamp=1606004997682, value=                            
 11                           column=address:ADRESSE, timestamp=1606004996611, value=Calocedrus                  
 11                           column=address:ARRONDISSEMENT, timestamp=1606004996607, value=Calocedrus           
 11                           column=address:GEOPOINT, timestamp=1606004996604, value=Calocedrus                 
 11                           column=address:NOM_EV, timestamp=1606004996614, value=Calocedrus                   
 11                           column=information:ANNEE PLANTATION, timestamp=1606004996593, value=Calocedrus     
 11                           column=information:CIRCONFERENCE, timestamp=1606004996601, value=Calocedrus        
 11                           column=information:HAUTEUR, timestamp=1606004996597, value=Calocedrus              
 11                           column=species:ESPECE, timestamp=1606004996574, value=decurrens                    
 11                           column=species:FAMILLE, timestamp=1606004996580, value=Cupressaceae                
 11                           column=species:GENRE, timestamp=1606004996570, value=Calocedrus                    
 11                           column=species:NOM COMMUN, timestamp=1606004996585, value=C\xC3\xA8dre \xC3\xA0 enc
                              ens                                                                                
 11                           column=species:VARIETE, timestamp=1606004996589, value=                            
 12                           column=address:ADRESSE, timestamp=1606004997746, value=Sequoiadendron              
 12                           column=address:ARRONDISSEMENT, timestamp=1606004997743, value=Sequoiadendron       
 12                           column=address:GEOPOINT, timestamp=1606004997741, value=Sequoiadendron             
 12                           column=address:NOM_EV, timestamp=1606004997748, value=Sequoiadendron               
 12                           column=information:ANNEE PLANTATION, timestamp=1606004997732, value=Sequoiadendron 
 12                           column=information:CIRCONFERENCE, timestamp=1606004997738, value=Sequoiadendron    
 12                           column=information:HAUTEUR, timestamp=1606004997735, value=Sequoiadendron          
 12                           column=species:ESPECE, timestamp=1606004997719, value=giganteum                    
 12                           column=species:FAMILLE, timestamp=1606004997722, value=Taxodiaceae                 
 12                           column=species:GENRE, timestamp=1606004997716, value=Sequoiadendron                
 12                           column=species:NOM COMMUN, timestamp=1606004997726, value=S\xC3\xA9quoia g\xC3\xA9a
                              nt                                                                                 
 12                           column=species:VARIETE, timestamp=1606004997729, value=                            
 13                           column=address:ADRESSE, timestamp=1606004999259, value=Platanus                    
 13                           column=address:ARRONDISSEMENT, timestamp=1606004999256, value=Platanus             
 13                           column=address:GEOPOINT, timestamp=1606004999253, value=Platanus                   
 13                           column=address:NOM_EV, timestamp=1606004999262, value=Platanus                     
 13                           column=information:ANNEE PLANTATION, timestamp=1606004999243, value=Platanus       
 13                           column=information:CIRCONFERENCE, timestamp=1606004999250, value=Platanus          
 13                           column=information:HAUTEUR, timestamp=1606004999246, value=Platanus                
 13                           column=species:ESPECE, timestamp=1606004999230, value=orientalis                   
 13                           column=species:FAMILLE, timestamp=1606004999234, value=Platanaceae                 
 13                           column=species:GENRE, timestamp=1606004999227, value=Platanus                      
 13                           column=species:NOM COMMUN, timestamp=1606004999237, value=Platane d'Orient         
 13                           column=species:VARIETE, timestamp=1606004999240, value=                            
5 row(s)
Took 0.3560 seconds                                                                                              
hbase(main):002:0> 
```

Our script worked as expected.