package com.opstty.reducer;

import java.io.IOException;
import java.util.stream.StreamSupport;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

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
