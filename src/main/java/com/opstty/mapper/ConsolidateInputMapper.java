package com.opstty.mapper;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class ConsolidateInputMapper extends Mapper<IntWritable, IntWritable, NullWritable, MapWritable> {
	public void map(IntWritable key, IntWritable value, Context context) throws IOException, InterruptedException {
		MapWritable map = new MapWritable();
		map.put(key, value);
		context.write(NullWritable.get(), map);
	}
}