package com.opstty.reducer;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class MaxTreesDistrictReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
    public ArrayList<Integer[]> sum_districts = new ArrayList<Integer[]>();
	
	public void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
    	int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        sum_districts.add(new Integer[] {key.get(), sum });
	}
	
	public void cleanup(Context context) {
		int max = sum_districts.stream().map(arr -> arr[1]).max(Integer::compare).get();
		sum_districts.stream().filter(arr -> arr[0] == max)
		.forEach(arr -> {
			try {
				context.write(new IntWritable(arr[0]), new IntWritable(max));
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		});
	}
}
