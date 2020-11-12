package com.opstty.mapper;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class ConsolidateInputMapper extends Mapper<LongWritable, Text, NullWritable, MapWritable> {
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		MapWritable map = new MapWritable();
		map.put(new IntWritable((int) key.get()), new IntWritable(Integer.parseInt(value.toString())));
		context.write(NullWritable.get(), map);
	}
}