package com.opstty.reducer;

import java.io.IOException;
import java.util.Calendar;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class OldestDistrictSortReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
	public boolean first = true;
	
	public void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
			if (first) {
				for (IntWritable district : values) {
					context.write(district, key);
				}
			} first = false;
	}
}
