package com.opstty.reducer;

import java.io.IOException;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class HeightSortedTreeReducer extends Reducer<FloatWritable, Text, Text, FloatWritable> {
	public void reduce(FloatWritable key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		for (Text val : values) {
			context.write(val, key);
		}
	}
}
