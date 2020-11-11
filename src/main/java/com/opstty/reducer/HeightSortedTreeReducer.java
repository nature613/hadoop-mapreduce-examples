package com.opstty.reducer;

import java.io.IOException;
import org.apache.curator.shaded.com.google.common.collect.Ordering;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.google.common.base.Function;

public class HeightSortedTreeReducer extends Reducer<FloatWritable, Text, Text, FloatWritable> {
	public void reduce(FloatWritable key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		for (Text val : values) {
			context.write(val, key);
		}
	}
}
