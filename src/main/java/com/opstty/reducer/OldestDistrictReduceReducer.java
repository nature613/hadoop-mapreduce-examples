package com.opstty.reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class OldestDistrictReduceReducer extends Reducer<NullWritable, MapWritable, IntWritable, IntWritable> {
	public void reduce(NullWritable key, Iterable<MapWritable> values, Context context)
			throws IOException, InterruptedException {
		
		ArrayList<Integer[]> district_years = (ArrayList<Integer[]>) StreamSupport.stream(values.spliterator(), false)
				.map( mw ->  (new Integer[] { ((IntWritable) mw.keySet().toArray()[0]).get(), ((IntWritable) mw.get(mw.keySet().toArray()[0])).get() }))
				.collect(Collectors.toList());
		// Copies the iterable to an arraylist so multiple operations can be done on the iterable
		
		int min_year = district_years.stream().map((arr) -> arr[1]).min(Integer::compare).get();
		
		district_years.stream().filter(arr -> arr[1] == min_year).distinct().forEach((district) -> { try {
			context.write(new IntWritable(district[0]), new IntWritable(min_year));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} });
		
	}
}
