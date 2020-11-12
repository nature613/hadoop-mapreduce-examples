package com.opstty.reducer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.apache.curator.shaded.com.google.common.collect.Ordering;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

import com.google.common.base.Function;

public class OldestDistrictReduceReducer extends Reducer<NullWritable, MapWritable, IntWritable, IntWritable> {
	public Integer getYear(MapWritable m) {
		return ((IntWritable) m.get(m.keySet().toArray()[0])).get();
	}
	
	public void reduce(NullWritable key, Iterable<MapWritable> values, Context context)
			throws IOException, InterruptedException {
		
		Integer min_year = StreamSupport.stream(values.spliterator(), false)
				.map((map) -> { return getYear(map); })
				.max(Float::compare)
				.get();
		
		StreamSupport.stream(values.spliterator(), false)
		.filter((map) -> getYear(map) == min_year)
		.forEach((map) -> { try {
			context.write((IntWritable) map.keySet().toArray()[0], (IntWritable) map.get(map.keySet().toArray()[0]));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}});
	}
}
