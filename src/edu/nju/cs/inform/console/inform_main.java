package edu.nju.cs.inform.console;

import edu.nju.cs.inform.core.diff.CodeElementsComparer;
import edu.nju.cs.inform.core.ir.IRModelConst;
import edu.nju.cs.inform.core.ir.Retrieval;
import edu.nju.cs.inform.core.type.ArtifactsCollection;
import edu.nju.cs.inform.io.ArtifactsReader;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by neo on 16/4/8.
 */
public class inform_main {
    public static void main(String[] args){
        String downLoadUrl = args[0];
        String new_Version_file = args[1];
        String old_Version_file = args[2];
        String requirement_file = args[3];

        String projectPath = getProjectPath();

        String filePath = createFile(downLoadUrl, new_Version_file, old_Version_file, projectPath);

        listComparer(new_Version_file, old_Version_file, requirement_file, filePath);

    }

    private static void listComparer(String new_Version_file, String old_Version_file, String requirement_file, String filePath) {
        CodeElementsComparer comparer = new CodeElementsComparer(filePath + "AquaLush-" + new_Version_file + "/src", filePath + "AquaLush-" + old_Version_file + "/src");
        comparer.diff();

        // get change description from code changes
        ArtifactsCollection changeDescriptionCollection = comparer.getChangeDescriptionCollection();
        final ArtifactsCollection requirementCollection = ArtifactsReader.getCollections(filePath + "AquaLush-" + new_Version_file + "/" + requirement_file, ".txt");

        // retrieval change description to requirement
        Retrieval retrieval = new Retrieval(changeDescriptionCollection, requirementCollection, IRModelConst.VSM);
        retrieval.tracing();

        Map<String, Double> candidatedOutdatedRequirementsRank = retrieval.getCandidateOutdatedRequirementsRank();
        //将map转换成list
        java.util.List<Map.Entry<String, Double>> list = new ArrayList<>(candidatedOutdatedRequirementsRank.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            //降序排列
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Map.Entry<String, Double> map : list) {
            System.out.println(map.getKey()+"  "+String.valueOf(map.getValue()));
        }
    }

    private static String createFile(String downLoadUrl, String new_Version_file, String old_Version_file, String projectPath) {
        //create file
        String filePath = projectPath+new_Version_file+" and "+old_Version_file+"/";
        File fp = new File(filePath);
        fp.mkdir();
        //download
        downloadZip oldVersion_Zip = new downloadZip();
        oldVersion_Zip.downLoadZip(downLoadUrl, old_Version_file, filePath);
        downloadZip newVersion_Zip = new downloadZip();
        newVersion_Zip.downLoadZip(downLoadUrl, new_Version_file, filePath);
        //decompressing
        zipDecompressing oldVersion_directory = new zipDecompressing();
        zipDecompressing newVersion_directory = new zipDecompressing();
        ArrayList<String> oldcode_list = oldVersion_directory.Ectract(filePath + old_Version_file + ".zip", filePath);
        ArrayList<String> newcode_list = newVersion_directory.Ectract(filePath + new_Version_file + ".zip", filePath);
        return filePath;
    }

    private static String getProjectPath() {
        File desktopDir = FileSystemView.getFileSystemView()
                .getHomeDirectory();
        String desktopPath = desktopDir.getAbsolutePath();

        String projectPath = desktopPath+"/Documents/Projects/";
        File project = new File(projectPath);
        // 目录已存在创建文件夹
        if (!project.exists()) {
            project.mkdir();
        }
        return projectPath;
    }
}
