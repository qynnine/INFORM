package edu.nju.cs.inform.console;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by neo on 16/4/10.
 */
public class zipDecompressing {
    /*public static void main(String[] args) {
        String downLoadUrl="https://github.com/qynnine/AquaLush";
        String new_Version_file="c6f082b6d22f102759bb9c7c72225848afe15d58";
        String old_Version_file="4c66abf1a3e77cc0ec099beb58f9874da7fa86d2";
        String oldVersion_download_location = "/Users/Che/INFORM/DownLoad/testProject/oldVersion/";
        String newVersion_download_location = "/Users/Che/INFORM/DownLoad/testProject/newVersion/";

        zipDecompressing oldVersion_directory = new zipDecompressing();
        zipDecompressing newVersion_directory = new zipDecompressing();
        ArrayList<String> oldcode_list = oldVersion_directory.Ectract(oldVersion_download_location + old_Version_file + ".zip", oldVersion_download_location);
        ArrayList<String> newcode_list = newVersion_directory.Ectract(newVersion_download_location + new_Version_file + ".zip", newVersion_download_location);
        for(String s : a){
            System.out.println(s);
        }
    }*/


    @SuppressWarnings("unchecked")
    public static ArrayList Ectract(String sZipPathFile, String sDestPath) {
        ArrayList<String> allFileName = new ArrayList<String>();
        try {
            // 先指定压缩档的位置和档名，建立FileInputStream对象
            FileInputStream fins = new FileInputStream(sZipPathFile);
            // 将fins传入ZipInputStream中
            ZipInputStream zins = new ZipInputStream(fins);
            ZipEntry ze = null;
            byte[] ch = new byte[256];
            while ((ze = zins.getNextEntry()) != null) {
                File zfile = new File(sDestPath + ze.getName());
                File fpath = new File(zfile.getParentFile().getPath());
                if (ze.isDirectory()) {
                    if (!zfile.exists())
                        zfile.mkdirs();
                    zins.closeEntry();
                } else {
                    if (!fpath.exists())
                        fpath.mkdirs();
                    FileOutputStream fouts = new FileOutputStream(zfile);
                    int i;
                    allFileName.add(zfile.getAbsolutePath());
                    while ((i = zins.read(ch)) != -1)
                        fouts.write(ch, 0, i);
                    zins.closeEntry();
                    fouts.close();
                }
            }
            fins.close();
            zins.close();
        } catch (Exception e) {
            System.err.println("Extract error:" + e.getMessage());
        }
        return allFileName;
    }
}
