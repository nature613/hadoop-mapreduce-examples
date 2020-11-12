package com.opstty.mapper;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsolidateInputMapper extends Mapper<LongWritable, Text, NullWritable, MapWritable> {
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String regex = "([+\\-]{0,1}[\\d]+(?:\\.[\\d]+)*)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value.toString());
		
		int count = 0;
		int keyval[] = new int[2];
		while (matcher.find()) {
			if(count < 2)
				keyval[count] = Integer.parseInt(matcher.group(1));
			++count;
		}
		
		MapWritable map = new MapWritable();
		map.put(new IntWritable(keyval[0]), new IntWritable(keyval[1]));
		context.write(NullWritable.get(), map);
	}
}