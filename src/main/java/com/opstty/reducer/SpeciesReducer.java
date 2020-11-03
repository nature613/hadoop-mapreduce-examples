package com.opstty.reducer;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class SpeciesReducer extends Reducer<Text, NullWritable,
Text,
NullWritable> { //IntWritable> {
    public void reduce(Text key, Iterable<NullWritable> values, Context context)
            throws IOException, InterruptedException {
    	System.out.println(key);
    	System.out.println(values);
    	context.write(key, NullWritable.get());
    }
}
