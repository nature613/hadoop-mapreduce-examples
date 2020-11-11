package com.opstty.mapper;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class HeightSpeciesMapper extends Mapper<Object, Text, Text, FloatWritable> {
	public int curr_line = 0;

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (curr_line != 0) {
			String height = value.toString().split(";")[6];
			try {
				context.write(new Text(value.toString().split(";")[3]), new FloatWritable(Float.parseFloat(height)));
			} catch (NumberFormatException ex) {
				// If the value is not a float, skip by catching the error from the parseFloat() method
			}
		} curr_line++;
	}
}