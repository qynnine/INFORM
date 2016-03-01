package edu.nju.cs.inform.test.util;

import edu.nju.cs.inform.util.UrlDownload;
import org.junit.Test;

public class UrlDownloadTest {

    @Test
    public void testFileDownload() throws Exception {
        String fAddress = "https://github.com/ramblas/INFORM/archive/master.zip";
        String destinationDir = "data/demo/";

        UrlDownload.fileDownload(fAddress, destinationDir);
    }
}