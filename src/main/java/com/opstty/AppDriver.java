package com.opstty;

import com.opstty.job.DistinctDistricts;
import com.opstty.job.MaxHeightSpecies;
import com.opstty.job.OldestTreeDistrictReduce;
import com.opstty.job.OldestTreeDistrictSort;
import com.opstty.job.TreeSpecies;
import com.opstty.job.TreesSortedByHeight;
import com.opstty.job.WordCount;
import org.apache.hadoop.util.ProgramDriver;

public class AppDriver {
	public static void main(String argv[]) {
		int exitCode = -1;
		ProgramDriver programDriver = new ProgramDriver();

		try {
			programDriver.addClass("wordcount", WordCount.class,
					"A map/reduce program that counts the words in the input files.");

			programDriver.addClass("distinctDistricts", DistinctDistricts.class,
					"A map/reduce program that returns the distinct districts with trees in the Remarkable Trees of Paris dataset.");

			programDriver.addClass("treeSpecies", TreeSpecies.class,
					"A map/reduce program that returns the distinct tree species in the Remarkable Trees of Paris dataset.");

			programDriver.addClass("maxHeightSpecies", MaxHeightSpecies.class,
					"A map/reduce program that returns the highest height of trees per species in the Remarkable Trees of Paris dataset.");
			
			programDriver.addClass("treesSortedByHeight", TreesSortedByHeight.class,
					"A map/reduce program that returns all the trees in the Remarkable Trees of Paris dataset, sorted by height.");
			
			programDriver.addClass("OldestTreeDistrictSort", OldestTreeDistrictSort.class,
					"A map/reduce program that returns the district(s) with the oldest tree(s) in the Remarkable Trees of Paris dataset, using a sort.");
			
			programDriver.addClass("OldestTreeDistrictReduce", OldestTreeDistrictReduce.class,
					"A map/reduce program that returns the district(s) with the oldest tree(s) in the Remarkable Trees of Paris dataset, checking through all the data.");
			
			exitCode = programDriver.run(argv);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}

		System.exit(exitCode);
	}
}
