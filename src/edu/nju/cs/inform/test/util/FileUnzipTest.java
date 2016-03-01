package edu.nju.cs.inform.test.util;

import edu.nju.cs.inform.util.FileUnzip;

public class FileUnzipTest {

    @org.junit.Test
    public void testExtract() throws Exception {
        String file = "data/demo/master.zip";
        String outputPath = "data/demo/";
        FileUnzip.extract(file, outputPath);
    }
}