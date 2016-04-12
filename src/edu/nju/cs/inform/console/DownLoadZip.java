package edu.nju.cs.inform.console;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by neo on 16/4/10.
 */
public class downloadZip {
    public  String downLoadZip(){
        int bytesum = 0;
        int byteread = 0;
        String fileName="4c66abf1a3e77cc0ec099beb58f9874da7fa86d2";
        String downloadURL="/Users/Che/INFORM/DownLoad/testProject/oldVersion/"+fileName+".zip";
        InputStream inStream=null;
        FileOutputStream fs =null;
        try {
//URL url = new URL(downloadURL + "/" + dateFloder + "/" + "page.zip");
            URL url = new URL("https://github.com/qynnine/AquaLush/archive/"+fileName+".zip");
            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();
            fs = new FileOutputStream(downloadURL);
            byte[] buffer = new byte[4028];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            System.out.println("文件下载成功.....");
        } catch (Exception e) {
            System.out.println("下载异常"+e);
            return "false";
        } finally{
            try {
                if(inStream!=null){
                    inStream.close();
                }
            } catch (IOException e) {
                inStream=null;
            }
            try {
                if(fs!=null){
                    fs.close();
                }
            } catch (IOException e) {
                fs=null;
            }
        }
        return downloadURL;

    }
    public String downLoadZip(String downLoadURL,String fileName,String downloadLocation){
        int bytesum = 0;
        int byteread = 0;
        //String fileName="4c66abf1a3e77cc0ec099beb58f9874da7fa86d2";
        String downloadURL=downloadLocation+fileName+".zip";
        InputStream inStream=null;
        FileOutputStream fs =null;
        try {
//URL url = new URL(downloadURL + "/" + dateFloder + "/" + "page.zip");
            URL url = new URL(downLoadURL+"/archive/"+fileName+".zip");
            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();
            fs = new FileOutputStream(downloadURL);
            byte[] buffer = new byte[4028];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            System.out.println(fileName+"文件下载成功.....");
        } catch (Exception e) {
            System.out.println("下载异常"+e);
            return "false";
        } finally{
            try {
                if(inStream!=null){
                    inStream.close();
                }
            } catch (IOException e) {
                inStream=null;
            }
            try {
                if(fs!=null){
                    fs.close();
                }
            } catch (IOException e) {
                fs=null;
            }
        }
        return downloadURL;

    }
/*
    public static void main(String[] args){

        //downloadZip t = new downloadZip();
        //t.downLoadZip();
        String downLoadUrl="https://github.com/qynnine/AquaLush";
        String new_Version_file="c6f082b6d22f102759bb9c7c72225848afe15d58";
        String old_Version_file="4c66abf1a3e77cc0ec099beb58f9874da7fa86d2";
        String oldVersion_download_location = "/Users/Che/INFORM/DownLoad/testProject/oldVersion/";
        String newVersion_download_location = "/Users/Che/INFORM/DownLoad/testProject/newVersion/";
        downloadZip oldVersion_Zip = new downloadZip();
        oldVersion_Zip.downLoadZip(downLoadUrl, old_Version_file, oldVersion_download_location);
        downloadZip newVersion_Zip = new downloadZip();
        newVersion_Zip.downLoadZip(downLoadUrl, new_Version_file, newVersion_download_location);
    }

*/
}
