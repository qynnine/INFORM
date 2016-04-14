package edu.nju.cs.inform.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Created by niejia on 16/2/24.
 */
public class FileUnzip {

    public static void extract(String source, String target) {
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(target);
            System.out.println("Unzip file " + zipFile.getFile().getName() + " successfully");
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String file = "data/demo/master.zip";
        String outputPath = "data/output/";

        FileUnzip.extract(file, outputPath);
    }
}
