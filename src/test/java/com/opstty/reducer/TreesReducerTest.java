package com.opstty.reducer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.verify;

//@RunWith(MockitoJUnitRunner.class)
public class TreesReducerTest {
    /*@Mock
    private Reducer.Context context;
    private SpeciesReducer speciesReducer;

    @Before
    public void setup() {
        this.speciesReducer = new SpeciesReducer();
    }

    @Test
    public void testReduce() throws IOException, InterruptedException {
        String key = "5";
               
        this.speciesReducer.reduce(new Text(key), NullWritable.get(), this.context);
        verify(this.context).write(new Text(key), NullWritable.get());
    }*/
}
