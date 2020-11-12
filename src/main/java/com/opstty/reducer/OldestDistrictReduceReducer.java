package com.opstty.reducer;

import java.io.IOException;
import java.util.Calendar;

import org.apache.curator.shaded.com.google.common.collect.Ordering;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.google.common.base.Function;

public class OldestDistrictReduceReducer extends Reducer<NullWritable, MapWritable, IntWritable, IntWritable> {
	public Integer getYear(MapWritable m) {
		return ((IntWritable) m.get(m.keySet().toArray()[0])).get();
	}
	
	@SuppressWarnings("rawtypes")
	public void reduce(NullWritable key, Iterable<MapWritable> values, Context context)
			throws IOException, InterruptedException {
		MapWritable max_couple = Ordering.natural().onResultOf(
				(Function<MapWritable, Comparable>) (v) -> { return getYear(v); }
				// Lambda implementing the functional interface for the onResultOf() method,
				// using the year as the sorting key 
			).min(values);
		Integer min_year = getYear(max_couple);
		
		for (MapWritable couple : values) {
			if (getYear(couple) == min_year) {
				context.write((IntWritable) couple.keySet().toArray()[0],
						new IntWritable(Calendar.getInstance().get(Calendar.YEAR)-min_year));
			}
		}
	}
}
