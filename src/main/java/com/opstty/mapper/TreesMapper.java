package com.opstty.mapper;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class TreesMapper extends Mapper<Object, Text, IntWritable, IntWritable> {
	public int curr_line = 0;
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (curr_line != 0) {
			context.write(new IntWritable(Integer.parseInt(value.toString().split(";")[1])), new IntWritable(1));
		}
		curr_line++;
	}
}