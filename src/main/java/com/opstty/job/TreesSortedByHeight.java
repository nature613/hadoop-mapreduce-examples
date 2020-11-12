package com.opstty.job;

import com.opstty.mapper.HeightSortedTreeMapper;
import com.opstty.reducer.HeightSortedTreeReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class TreesSortedByHeight {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: treesSortedByHeight <in> [<in>...] <out>");
            System.exit(2);
        }
        Job job = Job.getInstance(conf, "treesSortedByHeight");
        job.setJarByClass(TreesSortedByHeight.class);
        job.setMapperClass(HeightSortedTreeMapper.class);
        //job.setCombinerClass(HeightSortedTreeReducer.class);
        // The Mapper and the Reducer have mismatched key-value output types
        job.setReducerClass(HeightSortedTreeReducer.class);
        job.setMapOutputKeyClass(FloatWritable.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);
        for (int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job,
                new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}