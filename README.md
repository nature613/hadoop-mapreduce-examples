# TP4 Lab2 - MapReduce2/Yarn/Java Jobs

## 1.1. Installing OpenJDK

Using Linux Ubuntu, we can install OpenJDK 8 using ```sudo apt-get install openjdk-8-jdk```. Once installed, we can switch the currently used version to Java 8 using the ```update-alternatives --config java``` command:

```
~$ sudo update-alternatives --config java
There are 2 choices for the alternative java (providing /usr/bin/java).

  Selection    Path                                            Priority   Status
------------------------------------------------------------
* 0            /usr/lib/jvm/java-14-openjdk-amd64/bin/java      1411      auto mode
  1            /usr/lib/jvm/java-14-openjdk-amd64/bin/java      1411      manual mode
  2            /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java   1081      manual mode

Press <enter> to keep the current choice[*], or type selection number: 2

update-alternatives: using /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java
to provide /usr/bin/java (java) in manual mode
```

## 1.2. Installing Git

We can install git with the command ```sudo apt-get install git```. We will install hub as well to simplify the process of creating the repository. To install hub, we can use Homebrew (https://docs.brew.sh/Homebrew-on-Linux) and execute ```brew install hub```.

## 1.3. Installing a Java IDE

We will code using Eclipse (installed via ```sudo snap install --classic eclipse```).

## 1.4. Cloning the project

To use hub, one needs to create a SSH key associated with their GitHub account. Once the key is added, we can clone the original repository (```git clone https://github.com/makayel/hadoop-examples-mapreduce```) and create our own repository derived from it using the ```hub create``` command, followed by ```git push -u origin main``` (setting the origin of the newly created repository to the "main" branch of the project, the renamed master branch).

## 1.5. Importing the project

We import the project using Eclipse's Maven project importer : ```File > Import... > Maven > Existing Maven Projects```, ```Browse > Open```, select the ```pom.xml``` and press ```Finish```.

To generate the JAR, we can launch the maven install goal in the start directory, running the command ```mvn install``` (can also be executed using the M2Eclipse plugin). To install maven, use ```sudo apt install maven```.

## 1.6. Send the JAR to the edge node

To simplify the process of importing the target JAR to our edge node, we will directly clone the repository of our project in our local cloud storage (```https://github.com/iv-stpn/hadoop-examples-mapreduce```) and generate the new target files with maven directly on the edge (we could also generate it before, and use ```git pull``` to retrieve the target folder, were it to be included in the repository). To modify the source java files, we will use Eclipse on our machine, `commit` and `push` the changes, and `pull` the on the edge node before regenerating the jar. Using Linux, we will directly connect to the edge node via `ssh`.

## 1.7. The repository

You can find our repository at https://github.com/iv-stpn/hadoop-examples-mapreduce and visualize all the commits. We invite you to clone the repository to test it.

## 1.8. The Project

We first download the dataset using wget on the edge node, and put it on the HDFS:

```bash
-sh-4.2$ wget https://raw.githubusercontent.com/makayel/hadoop-examples-mapreduce/main/src/test/resources/data/trees.csv
--2020-11-10 17:21:36-- https://raw.githubusercontent.com/makayel/hadoop-examples-mapreduce/main/src/test/resources/data/trees.csv
Resolving raw.githubusercontent.com (raw.githubusercontent.com)... 151.101.120.133
Connecting to raw.githubusercontent.com (raw.githubusercontent.com)|151.101.120.133|:443... connected.
HTTP request sent, awaiting response... 200 OK
Length: 16680 (16K) [text/plain]
Saving to: ‘trees.csv’

100%[======================================>] 16 680      --.-K/s   in 0,001s  

2020-11-17:22:01 (15,6 MB/s) - ‘trees.csv’ saved [16680/16680]

-sh-4.2$ hdfs dfs -put trees.csv

-sh-4.2$ hdfs dfs -ls
Found 18 items
...
-rw-r--r--   3 istepanian hdfs     121271 2020-11-10 17:22 trees.csv
```

Using the default job already implemented in the target JAR (wordcount), we can test it on our dataset (we will create an alias, `launch_job`, to avoid rewriting the full command):

```bash
-sh-4.2$ alias launch_job="yarn jar ~/hadoop-examples-mapreduce/target/hadoop-examples-mapreduce-1.0-SNAPSHOT-jar-with-dependencies.jar"

-sh-4.2$ launch_job wordcount trees.csv count
...
20/11/10 17:27:55 INFO mapreduce.Job: Running job: job_1603290159664_3340
...
20/11/10 17:28:06 INFO mapreduce.Job:  map 0% reduce 0%
20/11/10 17:28:15 INFO mapreduce.Job:  map 100% reduce 0%
20/11/10 17:28:20 INFO mapreduce.Job:  map 100% reduce 100%
...
	File Input Format Counters
		Bytes Read=121271
	File Output Format Counters
		Bytes Written=68405

-sh-4.2$ hdfs dfs -cat count/part-r-00000
...
·	2
à	13
écus;;10;Parc	1
écus;;31;Jardin	1
écus;;46;Parc	1
écus;;64;Bois	1
écus;;84;Bois	1
île	1
...
```

Everything works correctly, the job completed successfully.

## 1.8.1. DistinctDistricts

For this MapReduce job, we create a simple job based on the files from the previous job `wordcount`, creating a job class `DistinctDistricts`, a mapper class `TreesMapper` and a reducer class `TreesReducer`

We then add our class to the `AppDriver` so the program can interpret our new command `distinctDistricts`:

```java
programDriver.addClass("distinctDistricts", DistinctDistricts.class,
        "A map/reduce program that returns the distinct districts with trees in a predefined CSV formatting.");
```

We can also add a custom command description in case of a wrong typing:

```java
if (otherArgs.length < 2) {
    System.err.println("Usage: distinctDistricts <in> [<in>...] <out>");
    System.exit(2);
}
```

Then, we set our mapper and reducer classes for the job in `DistinctDistricts`:

```java
if (otherArgs.length < 2) {
    System.err.println("Usage: distinctDistricts <in> [<in>...] <out>");
    System.exit(2);
}
Job job = Job.getInstance(conf, "distinctDistricts");
job.setJarByClass(DistinctDistricts.class);
job.setMapperClass(TreesMapper.class);
job.setCombinerClass(TreesReducer.class);
job.setReducerClass(TreesReducer.class);
```

Because the only information we will require is the district number, all we need is a `Text` key (that will contain the name of the district) with a `null` (`NullWritable`) value. By default, because of the way the MapReduce programming model works, all the keys will be made distinct, and we will get an `Iterable` of `NullWritable` instances aggregated. We can then just return the keys, as they will all be the distinct districts.

In `DistinctDistricts.java`:

```java
...
job.setOutputKeyClass(Text.class);
job.setOutputValueClass(NullWritable.class);
...
```

In `TreesMapper.java`:java

```java
public class TreesMapper extends Mapper<Object, Text, Text, NullWritable> {
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
    ...
    context.write(district, new NullWritable());
  }
}
```

In `TreesReducer.java`:

```java
public class TreesReducer extends Reducer<Text, NullWritable, Text, NullWritable> {
    public void reduce(Text key, Iterable<NullWritable> values, Context context)
            throws IOException, InterruptedException {
    ...
    context.write(district, new NullWritable());
  }
}
```

However, just for fun, we will use an `IntWritable` for our outputs, and count the number of trees each district has (you can just keep the keys of the final output to know the distinct districts themselves).

Now that we know the types of our outputs (we will note that the `Mapper` output is also the `Reducer` input, and because we set the `Combiner` class to the `Reducer` class, the output of the `Mapper` and the `Reducer` must be the same), we can work on the logic of our `MapReduce` job.

The `Mapper` retrieves the file's data line by line and may do the operation `context.write(k,v)` (i.e. output key value couples) as many times as it wants for every input. In our case, we will output one key-value couple for every input as there is only one tree & one district described per line, except the first line, which contains the column names and not data. To ignore it, we can either ignore a line if it contains unique data found in the first line (e.g. the column names themselves are not found in the rest of the data) or use a line counter and ignore the first iteration (or simply use a boolean, since only the first line needs to be ignored, and afterwards all lines are processed). We will use the latter option as it is safer, more universal and would allow us to know the current line being processed in a future implementation of the mapper (which can be useful information).

To retrieve the district number from the lines, we simply need to access the value of the second column the same way that a CSV interpreter does: by splitting the line along its separator (in our case, a semi-colon). To be able to count the number of trees per district in the reducer, we will associate the value 1 to the keys (the district numbers), so as to be summed during the aggregation in the `Reducer`.

`Mapper.java`

```java
public class TreesMapper extends Mapper<Object, Text, Text, IntWritable> {
	public int curr_line = 0;
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (curr_line != 0) {
			context.write(new Text(value.toString().split(";")[1]), new IntWritable(1));
		}
		curr_line++;
	}
}
```

For the reducer, we do the same as a `wordcount`, summing all the values aggregated for the distinct keys and writing `(key, sum)` to the context.

`Reducer.java`

```java
public class TreesReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
    	int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
    	context.write(key, new IntWritable(sum));
    }
}
```

We then commit the modifications, push to the repository, pull back on the edge node, build the JAR and launch the command.
To simplify this process on the edge's we will use an alias to do all those operations directly (simply pulling with Git causes issues because of the target folder, so we will reclone for simplicity):

```bash
-sh-4.2$ alias refresh_project='cd ~; rm -R -f hadoop-examples-mapreduce/; git clone https://github.com/iv-stpn/hadoop-examples-mapreduce; cd hadoop-examples-mapreduce/; mvn install'
```

We will also create a new alias to check for the result of a job:

```bash
-sh-4.2$ alias result='function _result() { hdfs dfs -cat "$1"/part-r-00000; } ; _result'
```

```bash
-sh-4.2$ launch_job distinctDistricts trees.csv districts

...
20/11/11 21:29:17 INFO mapreduce.Job:  map 0% reduce 0%
20/11/11 21:29:26 INFO mapreduce.Job:  map 100% reduce 0%
20/11/11 21:29:32 INFO mapreduce.Job:  map 100% reduce 100%
...
	File Input Format Counters
		Bytes Read=16680
	File Output Format Counters
		Bytes Written=80

-sh-4.2$ result districts

11	1
12	29
13	2
14	3
15	1
16	36
17	1
18	1
19	6
20	3
3	1
4	1
5	2
6	1
7	3
8	5
9	1
```

The job works as expected.

## 1.8.2. & 1.8.3  TreeSpecies / TreeSpeciesCount

This job is extremely similar to the previous one; just instead of using the district number obtained from the second column as the key, we use the species of the trees obtained from the fourth column. Just like for the previous job, we will print the number of trees for each species. To just recover the unique species, it is possible to return `NullWritable` values for the keys and just return the values obtained from the `Mapper` in the `Reducer`. We will showcase both cases with `TreeSpecies` and `TreeSpeciesCount`:

`AppDriver.java`

```java
...
programDriver.addClass("treeSpecies", TreeSpecies.class,
			"A map/reduce program that returns the distinct tree species in the Remarkable Trees of Paris dataset.");
...
```



`TreeSpecies.java`

```java
...
if (otherArgs.length < 2) {
    System.err.println("Usage: treeSpecies <in> [<in>...] <out>");
    System.exit(2);
}
Job job = Job.getInstance(conf, "treeSpecies");
job.setJarByClass(TreeSpecies.class);
job.setMapperClass(SpeciesMapper.class);
job.setCombinerClass(SpeciesReducer.class);
job.setReducerClass(SpeciesReducer.class);
job.setOutputKeyClass(Text.class);
job.setOutputValueClass(NullWritable.class);
...
```

`SpeciesMapper.java`

```java
...
public class SpeciesMapper extends Mapper<Object, Text, Text, NullWritable> {
	public int curr_line = 0;

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (curr_line != 0) {
			context.write(new Text(value.toString().split(";")[3]), NullWritable.get());
		}
		curr_line++;
	}
}
```

`SpeciesReducer.java`

```java
...
public class SpeciesReducer extends Reducer<Text, IntWritable, Text, NullWritable> {
	public void reduce(Text key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		context.write(key, NullWritable.get());
	}
}
```

```bash
-sh-4.2$ launch_job treeSpecies trees.csv species; printf "\n"; result species

...
20/11/12 14:48:54 INFO mapreduce.Job:  map 0% reduce 0%
20/11/12 14:49:03 INFO mapreduce.Job:  map 100% reduce 0%
20/11/12 14:49:13 INFO mapreduce.Job:  map 100% reduce 100%
...

    File Input Format Counters
		Bytes Read=16680
	File Output Format Counters
		Bytes Written=451

araucana
atlantica
australis
baccata
bignonioides
biloba
bungeana
cappadocicum
carpinifolia
colurna
coulteri
decurrens
dioicus
distichum
excelsior
fraxinifolia
giganteum
giraldii
glutinosa
grandiflora
hippocastanum
ilex
involucrata
japonicum
kaki
libanii
monspessulanum
nigra
nigra laricio
opalus
orientalis
papyrifera
petraea
pomifera
pseudoacacia
sempervirens
serrata
stenoptera
suber
sylvatica
tomentosa
tulipifera
ulmoides
virginiana
x acerifolia
```

The job without the count works as expected.

`AppDriver.java`

```java
...
programDriver.addClass("treeSpeciesCount", TreeSpeciesCount.class,
					"A map/reduce program that returns the distinct tree species (and the number of trees for each one) in the Remarkable Trees of Paris dataset.");
...
```

`TreeSpeciesCount.java`

```java
...
if (otherArgs.length < 2) {
    System.err.println("Usage: treeSpeciesCount <in> [<in>...] <out>");
    System.exit(2);
}
Job job = Job.getInstance(conf, "treeSpecies");
job.setJarByClass(TreeSpecies.class);
job.setMapperClass(SpeciesMapper.class);
job.setCombinerClass(SpeciesReducer.class);
job.setReducerClass(SpeciesReducer.class);
job.setOutputKeyClass(Text.class);
job.setOutputValueClass(IntWritable.class);
...
```

`SpeciesCountMapper.java`

```java
...
public class SpeciesMapper extends Mapper<Object, Text, Text, IntWritable> {
	public int curr_line = 0;

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (curr_line != 0) {
			context.write(new Text(value.toString().split(";")[3]), new IntWritable(1));
		}
		curr_line++;
	}
}
```

`SpeciesCountReducer.java`

```java
...
public class SpeciesReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
	public void reduce(Text key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		int sum = 0;
		for (IntWritable val : values) {
			sum += val.get();
		}
		context.write(key, new IntWritable(sum));
	}
}
```

```bash
-sh-4.2$ launch_job treeSpeciesCount trees.csv species_count; printf "\n"; result species_count;

...
20/11/12 14:51:35 INFO mapreduce.Job:  map 0% reduce 0%
20/11/12 14:51:44 INFO mapreduce.Job:  map 100% reduce 0%
20/11/12 14:51:53 INFO mapreduce.Job:  map 100% reduce 100%
...
	File Input Format Counters
		Bytes Read=16680
	File Output Format Counters
		Bytes Written=542

araucana	1
atlantica	2
australis	1
baccata	2
bignonioides	1
biloba	5
bungeana	1
cappadocicum	1
carpinifolia	4
colurna	3
coulteri	1
decurrens	1
dioicus	1
distichum	3
excelsior	1
fraxinifolia	2
giganteum	5
giraldii	1
glutinosa	1
grandiflora	1
hippocastanum	3
ilex	1
involucrata	1
japonicum	1
kaki	2
libanii	2
monspessulanum	1
nigra	3
nigra laricio	1
opalus	1
orientalis	8
papyrifera	1
petraea	2
pomifera	1
pseudoacacia	1
sempervirens	1
serrata	1
stenoptera	1
suber	1
sylvatica	8
tomentosa	2
tulipifera	2
ulmoides	1
virginiana	2
x acerifolia	11
```

The job with the count works as expected.

## 1.8.4 MaxHeightSpecies

For this job, we need to find the height of the highest tree of each species, which means we need to aggregate heights for each species. We will use the species as the key output (string => `Text`) and the heights as the value outputs (numerical) of our `Mapper` and `Reducer`. If we look at the height column (the seventh one), we can see that the values are actually floating-point decimal, but that they all end in `.0` (i.e they are all actually integers, which means we could use an `IntWritable` by doing `IntWritable height = new IntWritable((int) Float.parseFloat(value.toString().split(";")[6])))` in the mapper). Even though the values might all be integers in the dataset logic, we will suppose that future values in the dataset might have floating-point precision, and so we will use `FloatWritable` as the numerical type. We can also notice that unlike the species and the district where the trees are, the height is not always a known for trees in the dataset. This makes sense within the logic of the dataset because while the district number is implicit from the location of the tree and the species is obvious from identifying the tree, its height might be more difficult to obtain, as it needs to be measured officially. For this reason, we will add a security check in the `Mapper` for the height (unlike for the previous operations where it is unnecessary in the dataset's logic) so that height values that can't be converted to a `Float` (i.e. empty values) are ignored.

From this point on, we will use the class `StreamSupport` from Java Utils to make aggregation/mapping operations on the iterables in the `Reducer` inputs.

`AppDriver.java`

```java
...
programDriver.addClass("maxHeightSpecies", MaxHeightSpecies.class,
			"A map/reduce program that returns the highest height of trees per species in the Remarkable Trees of Paris dataset.");

...
```

`MaxHeightSpecies.java`

```java
...
if (otherArgs.length < 2) {
    System.err.println("Usage: maxHeightSpecies <in> [<in>...] <out>");
    System.exit(2);
}
Job job = Job.getInstance(conf, "maxHeightSpecies");
job.setJarByClass(MaxHeightSpecies.class);
job.setMapperClass(HeightSpeciesMapper.class);
job.setCombinerClass(HeightSpeciesReducer.class);
job.setReducerClass(HeightSpeciesReducer.class);
job.setOutputKeyClass(Text.class);
job.setOutputValueClass(FloatWritable.class);
...
```

`HeightSpeciesMapper.java`

```java
...
public class HeightSpeciesMapper extends Mapper<Object, Text, Text, FloatWritable> {
	public int curr_line = 0;

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (curr_line != 0) {
			try {
				Float height = Float.parseFloat(value.toString().split(";")[6]);
				context.write(new Text(value.toString().split(";")[3]), new FloatWritable(height));
			} catch (NumberFormatException ex) {
				// If the value is not a float, skip it by catching the error from the parseFloat() method
			}
		} curr_line++;
	}
}
```

`HeightSpeciesReducer.java`

```java
...
public class HeightSpeciesReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
    public class HeightSpeciesReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
    	public void reduce(Text key, Iterable<FloatWritable> values, Context context)
    			throws IOException, InterruptedException {
    		context.write(key, new FloatWritable(StreamSupport.stream(values.spliterator(), false)
    				.map((v) -> { return v.get(); }).
    				max(Float::compare).get()));
    	}
    }

}

```

```bash
-sh-4.2$ launch_job maxHeightSpecies trees.csv max_heights; printf "\n"; result max_heights;

...
20/11/12 15:12:35 INFO mapreduce.Job:  map 0% reduce 0%
20/11/12 15:12:44 INFO mapreduce.Job:  map 100% reduce 0%
20/11/12 15:12:54 INFO mapreduce.Job:  map 100% reduce 100%
...
File Input Format Counters
    Bytes Read=16821
File Output Format Counters
    Bytes Written=675

araucana	9.0
atlantica	25.0
australis	16.0
baccata	13.0
bignonioides	15.0
biloba	33.0
bungeana	10.0
cappadocicum	16.0
carpinifolia	30.0
colurna	20.0
coulteri	14.0
decurrens	20.0
dioicus	10.0
distichum	35.0
excelsior	30.0
fraxinifolia	27.0
giganteum	35.0
giraldii	35.0
glutinosa	16.0
grandiflora	12.0
hippocastanum	30.0
ilex	15.0
involucrata	12.0
japonicum	10.0
kaki	14.0
libanii	30.0
monspessulanum	12.0
nigra	30.0
nigra laricio	30.0
opalus	15.0
orientalis	34.0
papyrifera	12.0
petraea	31.0
pomifera	13.0
pseudoacacia	11.0
sempervirens	30.0
serrata	18.0
stenoptera	30.0
suber	10.0
sylvatica	30.0
tomentosa	20.0
tulipifera	35.0
ulmoides	12.0
virginiana	14.0
x acerifolia	45.0
```

The job works as expected.

## 1.8.5 TreesSortedByHeight

For this job, we suppose that we need an identifier to associate to the tree heights, instead of just sorting the heights without indicating which trees correspond to the heights. To accomplish just a job, we will create a special string in the `Mapper`, combining the OBJECTID of the tree (twelth colum) with its species/family and genus to have some extra information about the tree. As the identifiers (`Text`) are associated to each height (`FloatWritable`), there will actually be no aggregation in the `Reducer` over the identifiers; we will not use the reducer to sort the heights. Instead, we're simply going to use the sort operation over the keys that is necessarily part of `MapReduce` operations, in between the `Mapper` and `Reducer`. To do so, we will use the heights as keys to the identifiers and then return the identifiers associated with each height in order in the `Reducer` (as multiple trees can have the same height).

Note: in this case the `Mapper` and `Reducer` do not share the same output types (they are inverted), and as such we need to set the `Mapper` output types separately in the job configuration, and remove `Reducer` as the combiner class.

`AppDriver.java`

```java
...
programDriver.addClass("treesSortedByHeight", TreesSortedByHeight.class,
			"A map/reduce program that returns all the trees in the Remarkable Trees of Paris dataset, sorted by height.");
...
```

`TreesSortedByHeight.java`

```java
...
if (otherArgs.length < 2) {
    System.err.println("Usage: treesSortedByHeight <in> [<in>...] <out>");
    System.exit(2);
}
Job job = Job.getInstance(conf, "treesSortedByHeight");
job.setJarByClass(TreesSortedByHeight.class);
job.setMapperClass(HeightSortedTreeMapper.class);
//job.setCombinerClass(HeightSortedTreeReducer.class);
// The Mapper and the Reducer have mismatched key-value output types
job.setReducerClass(HeightSortedTreeReducer.class);
job.setMapOutputKeyClass(FloatWritable.class);
job.setMapOutputValueClass(Text.class);
job.setOutputKeyClass(Text.class);
job.setOutputValueClass(FloatWritable.class);
...
```

`HeightSortedTreeMapper.java`

```java
...
public class HeightSortedTreeMapper extends Mapper<Object, Text, FloatWritable, Text> {
	public int curr_line = 0;

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (curr_line != 0) {
			try {
				String[] line_tokens = value.toString().split(";");
				Float height = Float.parseFloat(line_tokens[6]);
				context.write(new FloatWritable(height),
						new Text(line_tokens[11] + " - " + line_tokens[2] + " " + line_tokens[3] + " (" + line_tokens[4] + ")"));
			} catch (NumberFormatException ex) {
				// If the value is not a float, skip by catching the error from the parseFloat() method
			}
		} curr_line++;
	}
}
```

`HeightSortedTreeReducer.java`

```java
...
public class HeightSortedTreeReducer extends Reducer<FloatWritable, Text, Text, FloatWritable> {
	public void reduce(FloatWritable key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		for (Text val : values) {
			context.write(val, key);
		}
	}
}
```

```bash
-sh-4.2$ launch_job treesSortedByHeight trees.csv sorted_heights; printf "\n"; result sorted_heights;

...
20/11/12 15:28:45 INFO mapreduce.Job:  map 0% reduce 0%
20/11/12 15:28:54 INFO mapreduce.Job:  map 100% reduce 0%
20/11/12 15:29:05 INFO mapreduce.Job:  map 100% reduce 100%
...
File Input Format Counters
    Bytes Read=16680
File Output Format Counters
    Bytes Written=3994

3 - Fagus sylvatica (Fagaceae)	2.0
89 - Taxus baccata (Taxaceae)	5.0
62 - Cedrus atlantica (Pinaceae)	6.0
39 - Araucaria araucana (Araucariaceae)	9.0
44 - Styphnolobium japonicum (Fabaceae)	10.0
32 - Quercus suber (Fagaceae)	10.0
95 - Pinus bungeana (Pinaceae)	10.0
61 - Gymnocladus dioicus (Fabaceae)	10.0
63 - Fagus sylvatica (Fagaceae)	10.0
4 - Robinia pseudoacacia (Fabaceae)	11.0
93 - Diospyros virginiana (Ebenaceae)	12.0
66 - Magnolia grandiflora (Magnoliaceae)	12.0
50 - Zelkova carpinifolia (Ulmaceae)	12.0
7 - Eucommia ulmoides (Eucomiaceae)	12.0
48 - Acer monspessulanum (Sapindacaees)	12.0
58 - Diospyros kaki (Ebenaceae)	12.0
33 - Broussonetia papyrifera (Moraceae)	12.0
71 - Davidia involucrata (Cornaceae)	12.0
36 - Taxus baccata (Taxaceae)	13.0
6 - Maclura pomifera (Moraceae)	13.0
68 - Diospyros kaki (Ebenaceae)	14.0
96 - Pinus coulteri (Pinaceae)	14.0
94 - Diospyros virginiana (Ebenaceae)	14.0
91 - Acer opalus (Sapindaceae)	15.0
5 - Catalpa bignonioides (Bignoniaceae)	15.0
70 - Fagus sylvatica (Fagaceae)	15.0
2 - Ulmus carpinifolia (Ulmaceae)	15.0
98 - Quercus ilex (Fagaceae)	15.0
28 - Alnus glutinosa (Betulaceae)	16.0
78 - Acer cappadocicum (Sapindaceae)	16.0
75 - Zelkova carpinifolia (Ulmaceae)	16.0
16 - Celtis australis (Cannabaceae)	16.0
64 - Ginkgo biloba (Ginkgoaceae)	18.0
83 - Zelkova serrata (Ulmaceae)	18.0
23 - Aesculus hippocastanum (Sapindaceae)	18.0
60 - Fagus sylvatica (Fagaceae)	18.0
34 - Corylus colurna (Betulaceae)	20.0
51 - Platanus x acerifolia (Platanaceae)	20.0
43 - Tilia tomentosa (Malvaceae)	20.0
15 - Corylus colurna (Betulaceae)	20.0
11 - Calocedrus decurrens (Cupressaceae)	20.0
1 - Corylus colurna (Betulaceae)	20.0
8 - Platanus orientalis (Platanaceae)	20.0
20 - Fagus sylvatica (Fagaceae)	20.0
35 - Paulownia tomentosa (Paulowniaceae)	20.0
12 - Sequoiadendron giganteum (Taxodiaceae)	20.0
87 - Taxodium distichum (Taxodiaceae)	20.0
13 - Platanus orientalis (Platanaceae)	20.0
10 - Ginkgo biloba (Ginkgoaceae)	22.0
47 - Aesculus hippocastanum (Sapindaceae)	22.0
86 - Platanus orientalis (Platanaceae)	22.0
14 - Pterocarya fraxinifolia (Juglandaceae)	22.0
88 - Liriodendron tulipifera (Magnoliaceae)	22.0
18 - Fagus sylvatica (Fagaceae)	23.0
24 - Cedrus atlantica (Pinaceae)	25.0
31 - Ginkgo biloba (Ginkgoaceae)	25.0
92 - Platanus x acerifolia (Platanaceae)	25.0
49 - Platanus orientalis (Platanaceae)	25.0
97 - Pinus nigra (Pinaceae)	25.0
84 - Ginkgo biloba (Ginkgoaceae)	25.0
73 - Platanus orientalis (Platanaceae)	26.0
65 - Pterocarya fraxinifolia (Juglandaceae)	27.0
42 - Platanus orientalis (Platanaceae)	27.0
85 - Juglans nigra (Juglandaceae)	28.0
76 - Pinus nigra laricio (Pinaceae)	30.0
19 - Quercus petraea (Fagaceae)	30.0
72 - Sequoiadendron giganteum (Taxodiaceae)	30.0
54 - Pterocarya stenoptera (Juglandaceae)	30.0
29 - Zelkova carpinifolia (Ulmaceae)	30.0
27 - Sequoia sempervirens (Taxodiaceae)	30.0
25 - Fagus sylvatica (Fagaceae)	30.0
41 - Platanus x acerifolia (Platanaceae)	30.0
77 - Taxodium distichum (Taxodiaceae)	30.0
55 - Platanus x acerifolia (Platanaceae)	30.0
69 - Pinus nigra (Pinaceae)	30.0
38 - Fagus sylvatica (Fagaceae)	30.0
59 - Sequoiadendron giganteum (Taxodiaceae)	30.0
52 - Fraxinus excelsior (Oleaceae)	30.0
37 - Cedrus libanii (Pinaceae)	30.0
22 - Cedrus libanii (Pinaceae)	30.0
30 - Aesculus hippocastanum (Sapindaceae)	30.0
80 - Quercus petraea (Fagaceae)	31.0
9 - Platanus orientalis (Platanaceae)	31.0
82 - Platanus x acerifolia (Platanaceae)	32.0
46 - Ginkgo biloba (Ginkgoaceae)	33.0
45 - Platanus orientalis (Platanaceae)	34.0
56 - Taxodium distichum (Taxodiaceae)	35.0
81 - Liriodendron tulipifera (Magnoliaceae)	35.0
17 - Platanus x acerifolia (Platanaceae)	35.0
53 - Ailanthus giraldii (Simaroubaceae)	35.0
57 - Sequoiadendron giganteum (Taxodiaceae)	35.0
26 - Platanus x acerifolia (Platanaceae)	40.0
74 - Platanus x acerifolia (Platanaceae)	40.0
40 - Platanus x acerifolia (Platanaceae)	40.0
90 - Platanus x acerifolia (Platanaceae)	42.0
21 - Platanus x acerifolia (Platanaceae)	45.0
```

The job works as expected.

## 1.8.6 OldestTreeDistrictSort / OldestTreeDistrictReduce

For this job, the guideline states that it is not possible to use the information directly as key-values and suggests to use a
single were all the data couples (used as a value output via `ArrayWritable` or `MapWritable`) are aggregated in the `Reducer` and successive comparisons are applied to all the second index of the couple (i.e. the years, `IntWritable`), so that the first indices of the couples (as there can be multiple districts where trees were planted the same year) with the lowest years (i.e. the district number, `IntWritable`) are returned. However, this statement is untrue, as it is possible once again to use the sorting operation that is part of `MapReduce` operations to have the first key-values couple received by the `Reducer` be the one with the smallest year (with all the districts with a tree planted at the smallest year aggregated), and ignore all other couples. It is possible to do so because the operation requires the selection of one data point that can be found directly in the sort result; if the aggregation we had to do was more complicated (like a count or calculating an average), it would require us to use the first method.

For the sake of completeness, we will showcase both methods, with `OldestTreeDistrictSort` using the more effective method with the automatic `MapReduce` sort and `OldestTreeDistrictReduce` consolidating all the data into a single key for processing

Note: for clarity, we will return the year of plantation along with the numbers of the districts where the oldest trees were planted.

In the current dataset, the oldest tree is the one with OBJECTID 4 (line 28 of the `trees.csv` file), planted in 1601 in the 5th district. For testing purposes, we will add two trees to the dataset: another one planted in district 5 in the same, and one planted in district 3 in 1601 as well.

```bash
-sh-4.2$ echo ";5;Cedrus;atlantica;Pinaceae;1601;;;Cèdre bleu de l'Atlas;Glauca;99;" | hdfs dfs -appendToFile - "trees.csv"
-sh-4.2$ echo ";3;Cedrus;atlantica;Pinaceae;1601;;;Cèdre bleu de l'Atlas;Glauca;100;" | hdfs dfs -appendToFile - "trees.csv"
-sh-4.2$ hdfs dfs -cat trees.csv

...
;5;Cedrus;atlantica;Pinaceae;1601;;;Cèdre bleu de l'Atlas;Glauca;99;
;3;Cedrus;atlantica;Pinaceae;1601;;;Cèdre bleu de l'Atlas;Glauca;100;
```

`AppDriver.java`

```java
...
programDriver.addClass("oldestTreeDistrictSort", OldestTreeDistrictSort.class,
			"A map/reduce program that returns the district(s) with the oldest tree(s) in the Remarkable Trees of Paris dataset, using a sort.");
...
```

`OldestTreeDistrictSort.java`

```java
...
if (otherArgs.length < 2) {
    System.err.println("Usage: oldestTreeDistrictSort <in> [<in>...] <out>");
    System.exit(2);
}
Job job = Job.getInstance(conf, "oldestTreeDistrictSort");
job.setJarByClass(OldestTreeDistrictSort.class);
job.setMapperClass(OldestDistrictSortMapper.class);
job.setCombinerClass(OldestDistrictSortReducer.class);
job.setReducerClass(OldestDistrictSortReducer.class);
job.setOutputKeyClass(IntWritable.class);
job.setOutputValueClass(IntWritable.class);
// The Mapper & the Reducer have the same output key-values
...
```

`OldestDistrictSortMapper.java`

```java
...
public class OldestDistrictSortMapper extends Mapper<Object, Text, IntWritable, IntWritable> {
	public int curr_line = 0;

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (curr_line != 0) {
			try {
				Integer year = Integer.parseInt(value.toString().split(";")[5]);
				context.write(new IntWritable(year), new IntWritable(Integer.parseInt(value.toString().split(";")[1])));
			} catch (NumberFormatException ex) {
				// If the year is not an integer, skip by catching the error from the parseFloat() method
			}
		} curr_line++;
	}
}
```

`OldestDistrictSortReducer.java`

```java
...
public class OldestDistrictSortReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
	public boolean first = true;

	public void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		if (first) {
			StreamSupport.stream(values.spliterator(), false).distinct().forEach(v -> {
				try {
					context.write(key, v);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		first = false;
	}
}
```

```bash
-sh-4.2$ launch_job oldestTreeDistrictSort trees.csv oldest_sort; printf "\n"; result oldest_sort;

...
20/11/12 20:54:21 INFO mapreduce.Job:  map 0% reduce 0%
20/11/12 20:54:30 INFO mapreduce.Job:  map 100% reduce 0%
20/11/12 20:54:40 INFO mapreduce.Job:  map 100% reduce 100%
...
	File Input Format Counters
		Bytes Read=16821
	File Output Format Counters
		Bytes Written=14

1601	5
1601	3
```

The job worked as expected: the job only shows the distinct districts with trees planted at the oldest date.

`AppDriver.java`

```java
...
programDriver.addClass("oldestTreeDistrictReduce", OldestTreeDistrictReduce.class,
			"A map/reduce program that returns the district(s) with the oldest tree(s) in the Remarkable Trees of Paris dataset, checking through all the data.");
...
```

`OldestTreeDistrictSort.java`

```java
...
if (otherArgs.length < 2) {
    System.err.println("Usage: oldestTreeDistrictReduce <in> [<in>...] <out>");
    System.exit(2);
}
Job job = Job.getInstance(conf, "oldestTreeDistrictReduce");
job.setJarByClass(OldestTreeDistrictReduce.class);
job.setMapperClass(OldestDistrictReduceMapper.class);
//job.setCombinerClass(OldestDistrictReduceReducer.class);
// The Mapper and Reducer have mismatch key value output types
job.setReducerClass(OldestDistrictReduceReducer.class);
job.setMapOutputKeyClass(NullWritable.class);
job.setMapOutputValueClass(MapWritable.class);
job.setOutputKeyClass(IntWritable.class);
job.setOutputValueClass(IntWritable.class);
...
```

`OldestDistrictSortMapper.java`

```java
...
public class OldestDistrictReduceMapper extends Mapper<Object, Text, NullWritable, MapWritable> {
	public int curr_line = 0;

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (curr_line != 0) {
			try {
				Integer year = Integer.parseInt(value.toString().split(";")[5]);
				MapWritable map = new MapWritable();
				map.put(new IntWritable(Integer.parseInt(value.toString().split(";")[1])), new IntWritable(year));
				context.write(NullWritable.get(), map);
			} catch (NumberFormatException ex) {
				// If the year is not a integer, skip by catching the error from the parseFloat() method
			}
		} curr_line++;
	}
}
```

`OldestDistrictSortReducer.java`

```java
...
public class OldestDistrictReduceReducer extends Reducer<NullWritable, MapWritable, IntWritable, IntWritable> {
	public void reduce(NullWritable key, Iterable<MapWritable> values, Context context)
			throws IOException, InterruptedException {

		ArrayList<Integer[]> district_years = (ArrayList<Integer[]>) StreamSupport.stream(values.spliterator(), false)
				.map( mw ->  (new Integer[] { ((IntWritable) mw.keySet().toArray()[0]).get(), ((IntWritable) mw.get(mw.keySet().toArray()[0])).get() }))
				.collect(Collectors.toList());
		// Copies the iterable to an arraylist so multiple operations can be done on the iterable

		int min_year = district_years.stream().map((arr) -> arr[1]).min(Integer::compare).get();

		district_years.stream().filter(arr -> arr[1] == min_year).map(arr -> arr[0]).distinct().forEach((district) -> { try {
			context.write(new IntWritable(min_year), new IntWritable(district));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} });
	}
}
```

```bash
-sh-4.2$ launch_job oldestTreeDistrictReduce trees.csv oldest_reduce; printf "\n"; result oldest_reduce;

...
20/11/12 20:56:09 INFO mapreduce.Job:  map 0% reduce 0%
20/11/12 20:56:18 INFO mapreduce.Job:  map 100% reduce 0%
20/11/12 20:56:27 INFO mapreduce.Job:  map 100% reduce 100%
...
    File Input Format Counters
        Bytes Read=16821
    File Output Format Counters
        Bytes Written=14

1601	5
1601	3
```

The job worked as expected.

## 1.8.7 MaxTreesDistrict / MaxTreesDistrict2

As we saw in the last job, a `Reducer` that aggregates all data in one iteration over a single key with which all the data elements sent from the `Mapper` are associated is very inefficient. For this last job that involves finding the districts with the maximum number of trees, we will design a job that uses multiple key-value outputs for the `Mapper` with a proper aggregation in the reducer. To do so, there are two methods: the first one, mentioned in the guideline, involves creating a second layer of `MapReduce` by chaining two jobs together. We will use our previous `DistinctDistricts` job as the first job (it will give us the number of trees associated with each district, cf. 1.8.1) and for the second job we will use a mapper similar to that of `OldestDistrictReduceReducer` (returning all the key value couples with a single key with an unimportant value for consolidation in the `Reducer`) and a simple maximum `Reducer` using the values as the elements to be compared and returning the corresponding key value couple(s). The other method, more effective and requiring less classes and less operations, is to add a `cleanup()` method to the `Reducer` of the `DistinctDistricts` job so that it returns the district with the most trees out of a collection of associated district numbers and number of trees planted.

Once again, we will showcase both methods, with `MaxTreesDistrict` corresponding to the most effective method, and `MaxTreesDistrict2` corresponding to the method involving two `MapReduce` jobs.

`AppDriver.java`

```java
...
programDriver.addClass("maxTreesDistrict", MaxTreesDistrict.class,
					"A map/reduce program that returns the district(s) with the most trees in the Remarkable Trees of Paris dataset, checking through all the data, using the Reducer's cleanup.");
...
```

`MaxTreesDistrict.java`

```java
...
if (otherArgs.length < 2) {
    System.err.println("Usage: maxTreesDistrict <in> [<in>...] <out>");
    System.exit(2);
}
Job job = Job.getInstance(conf, "maxTreesDistrict");
job.setJarByClass(MaxTreesDistrict.class);
job.setMapperClass(TreesMapper.class);
job.setCombinerClass(MaxTreesDistrictReducer.class);
job.setReducerClass(MaxTreesDistrictReducer.class);
job.setOutputKeyClass(IntWritable.class);
job.setOutputValueClass(IntWritable.class);
...
```

`MaxTreesDistrictReducer.java`

```java
...
public class MaxTreesDistrictReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
    public ArrayList<Integer[]> sum_districts = new ArrayList<Integer[]>();

	public void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
    	int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        sum_districts.add(new Integer[] {key.get(), sum });
	}

	public void cleanup(Context context) {
		int max = sum_districts.stream().map(arr -> arr[1]).max(Integer::compare).get();
		sum_districts.stream().filter(arr -> arr[1] == max)
		.forEach(arr -> {
			try {
				context.write(new IntWritable(arr[0]), new IntWritable(max));
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		});
	}
}
```

```bash
-sh-4.2$ launch_job maxTreesDistrict trees.csv max_trees_district; printf "\n"; result max_trees_district;

...
20/11/12 22:41:11 INFO mapreduce.Job:  map 0% reduce 0%
20/11/12 22:41:20 INFO mapreduce.Job:  map 100% reduce 0%
20/11/12 22:41:24 INFO mapreduce.Job:  map 100% reduce 100%
...
    File Input Format Counters
        Bytes Read=16821
    File Output Format Counters
        Bytes Written=6

16	36
```

The job worked as expected: district number 16 is indeed the one with the most trees (with a total of 36).

`AppDriver.java`

```java
...
programDriver.addClass("maxTreesDistrict", MaxTreesDistrict.class,
					"A map/reduce program that returns the district(s) with the most trees in the Remarkable Trees of Paris dataset, checking through all the data, using the Reducer's cleanup.");
...
```

`MaxTreesDistrict2.java`

```java
...
public class MaxTreesDistrict2 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: maxTreesDistrict2 <in> [<in>...] <out>");
            System.exit(2);
        }
        Job job = Job.getInstance(conf, "maxTreesDistrict2");
        job.setJarByClass(MaxTreesDistrict2.class);
        job.setMapperClass(TreesMapper.class);
        job.setCombinerClass(TreesReducer.class);
        job.setReducerClass(TreesReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);


        Path temp_file = new Path("##.temp");
        for (int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job,
        		temp_file);
        job.waitForCompletion(true);

        Configuration conf_max = new Configuration();
        Job job_max = Job.getInstance(conf_max, "max");
        job_max.setJarByClass(MaxTreesDistrict2.class);
        job_max.setMapperClass(ConsolidateInputMapper.class);
        //job_max.setCombinerClass(MaxTreesDistrictReducer2.class);
        job_max.setReducerClass(MaxTreesDistrictReducer2.class);
        job_max.setMapOutputKeyClass(NullWritable.class);
        job_max.setMapOutputValueClass(MapWritable.class);
        job_max.setOutputKeyClass(IntWritable.class);
        job_max.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job_max, temp_file);
        FileOutputFormat.setOutputPath(job_max,
        		new Path(otherArgs[otherArgs.length-1]));

        boolean finished = job_max.waitForCompletion(true);

        FileUtils.deleteDirectory(new File(temp_file.toString()));
        System.exit(finished ? 0 : 1);
    }
}
```

`ConsolidateInputMapper.java`

```java
public class ConsolidateInputMapper extends Mapper<LongWritable, IntWritable, NullWritable, MapWritable> {
	public void map(LongWritable key, IntWritable value, Context context) throws IOException, InterruptedException {
		MapWritable map = new MapWritable();
		map.put(new IntWritable((int) key.get()), value);
		context.write(NullWritable.get(), map);
	}
}
```

`MaxTreesDistrictReducer2.java`

```java
...
public class MaxTreesDistrictReducer2 extends Reducer<NullWritable, MapWritable, IntWritable, IntWritable> {
    public void reduce(NullWritable key, Iterable<MapWritable> values, Context context)
            throws IOException, InterruptedException {
		ArrayList<Integer[]> district_trees = (ArrayList<Integer[]>) StreamSupport.stream(values.spliterator(), false)
				.map( mw ->  (new Integer[] { ((IntWritable) mw.keySet().toArray()[0]).get(), ((IntWritable) mw.get(mw.keySet().toArray()[0])).get() }))
				.collect(Collectors.toList());
		// Copies the iterable to an arraylist so multiple operations can be done on the iterable

		int max_trees = district_trees.stream().map((arr) -> arr[1]).max(Integer::compare).get();

		district_trees.stream().filter(arr -> arr[1] == max_trees).map(arr -> arr[0]).distinct().forEach((district) -> { try {
			context.write(new IntWritable(max_trees), new IntWritable(district));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} });
    }
}
```

```bash
-sh-4.2$ launch_job maxTreesDistrict2 trees.csv max_trees_district2; printf "\n"; result max_trees_district2;

...
20/11/12 23:17:41 INFO mapreduce.Job:  map 0% reduce 0%
20/11/12 23:17:50 INFO mapreduce.Job:  map 100% reduce 0%
20/11/12 23:17:59 INFO mapreduce.Job:  map 100% reduce 100%
...
File Input Format Counters
    Bytes Read=16821
File Output Format Counters
    Bytes Written=80
...
20/11/12 23:18:11 INFO mapreduce.Job:  map 0% reduce 0%
20/11/12 23:18:20 INFO mapreduce.Job:  map 100% reduce 0%
20/11/12 23:18:29 INFO mapreduce.Job:  map 100% reduce 100%
...
    File Input Format Counters
        Bytes Read=80
    File Output Format Counters
        Bytes Written=6

16	36

```

The job worked as expected.



N.B. We tried adding JUnit tests to our project but could not proceed because of some error with the library we could not solve:

```java
Wanted but not invoked:
context.write(pomifera, (null));
-> at com.opstty.mapper.SpeciesMapperTest.testMap(SpeciesMapperTest.java:34)
Actually, there were zero interactions with this mock.
```

We managed to finish the lab without using the unit tests, thanks to effective debugging.
