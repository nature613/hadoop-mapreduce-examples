package com.opstty.mapper;

import org.apache.hadoop.io.LongWritable;
        import org.apache.hadoop.io.NullWritable;
        import org.apache.hadoop.io.Text;
        import org.apache.hadoop.mapreduce.Mapper;
        import java.io.IOException;

public class TreesMapper extends Mapper<Object, Text, 
		Text,
		NullWritable> {

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
    	//context.write(NullWritable.get(), NullWritable.get());
        if(!value.toString().contains("ARRONDISSEMENT")){
            Text district = new Text(value.toString().split(";")[1]);
            context.write(district, NullWritable.get());
        }
    }
}