package com.opstty.mapper;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class SpeciesCountMapper extends Mapper<Object, Text, Text, IntWritable> {
	public int curr_line = 0;

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (curr_line != 0) {
			context.write(new Text(value.toString().split(";")[3]), new IntWritable(1));
		}
		curr_line++;
	}
}