package com.opstty.mapper;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class TreesMapper extends Mapper<Object, Text, Text, IntWritable> {

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		// context.write(NullWritable.get(), NullWritable.get());
		if (!value.toString().contains("ARRONDISSEMENT")) {
			Text district = new Text("");
			try {
				district = new Text(value.toString().split(";")[1]);
			} catch (Exception e) {
				System.out.println(value.toString());
			}
			context.write(district, new IntWritable(1));
		}
	}
}