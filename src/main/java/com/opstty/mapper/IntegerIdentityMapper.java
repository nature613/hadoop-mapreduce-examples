package com.opstty.mapper;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class IntegerIdentityMapper extends Mapper<IntWritable, IntWritable, IntWritable, IntWritable> {
	public void map(IntWritable key, IntWritable value, Context context) throws IOException, InterruptedException {
		context.write(key, value);
	}
}