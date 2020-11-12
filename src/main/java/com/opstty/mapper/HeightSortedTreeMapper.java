package com.opstty.mapper;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class HeightSortedTreeMapper extends Mapper<Object, Text, FloatWritable, Text> {
	public int curr_line = 0;

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (curr_line != 0) {
			try {
				String[] line_tokens = value.toString().split(";");
				Float height = Float.parseFloat(line_tokens[6]);
				context.write(new FloatWritable(height), 
						new Text(line_tokens[11] + " - " + line_tokens[2] + " " + line_tokens[3] + " (" + line_tokens[4] + ")"));
			} catch (NumberFormatException ex) {
				// If the value is not a float, skip by catching the error from the parseFloat() method
			}
		} curr_line++;
	}
}