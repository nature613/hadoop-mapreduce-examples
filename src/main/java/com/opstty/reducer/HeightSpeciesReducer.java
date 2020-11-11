package com.opstty.reducer;

import java.io.IOException;
import org.apache.curator.shaded.com.google.common.collect.Ordering;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.google.common.base.Function;

public class HeightSpeciesReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
	@SuppressWarnings("rawtypes")
	public void reduce(Text key, Iterable<FloatWritable> values, Context context)
			throws IOException, InterruptedException {
		context.write(key, Ordering.natural().onResultOf(
					(Function<FloatWritable, Comparable>) (v) -> { return v.get(); }
					// Lambda implementing the functional interface for the onResultOf() method, "converting" the FloatWritable to float 
				).max(values));
	}
}
