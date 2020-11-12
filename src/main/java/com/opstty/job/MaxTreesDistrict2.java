package com.opstty.job;

import com.opstty.mapper.ConsolidateInputMapper;
import com.opstty.mapper.TreesMapper;
import com.opstty.reducer.MaxTreesDistrictReducer2;
import com.opstty.reducer.TreesReducer;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MaxTreesDistrict2 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: maxTreesDistrict2 <in> [<in>...] <out>");
            System.exit(2);
        }
        Job job = Job.getInstance(conf, "maxTreesDistrict2");
        job.setJarByClass(MaxTreesDistrict2.class);
        job.setMapperClass(TreesMapper.class);
        job.setCombinerClass(TreesReducer.class);
        job.setReducerClass(TreesReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        
        
        Path temp_file = new Path("##.temp");
        for (int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job,
        		temp_file);
        job.waitForCompletion(true);
        
        Configuration conf_max = new Configuration();
        Job job_max = Job.getInstance(conf_max, "max");
        job_max.setJarByClass(MaxTreesDistrict2.class);
        job_max.setMapperClass(ConsolidateInputMapper.class);
        //job_max.setCombinerClass(MaxTreesDistrictReducer2.class);
        job_max.setReducerClass(MaxTreesDistrictReducer2.class);
        job_max.setMapOutputKeyClass(NullWritable.class);
        job_max.setMapOutputValueClass(MapWritable.class);
        job_max.setOutputKeyClass(IntWritable.class);
        job_max.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job_max, temp_file);
        FileOutputFormat.setOutputPath(job_max,
        		new Path(otherArgs[otherArgs.length-1]));
        
        boolean finished = job_max.waitForCompletion(true);
        
        FileUtils.deleteDirectory(new File(temp_file.toString()));
        System.exit(finished ? 0 : 1);
    }
}
