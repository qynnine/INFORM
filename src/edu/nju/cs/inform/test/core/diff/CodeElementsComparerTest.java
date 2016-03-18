package edu.nju.cs.inform.test.core.diff;

import edu.nju.cs.inform.core.diff.CodeElementsComparer;
import edu.nju.cs.inform.core.type.ArtifactsCollection;
import org.junit.Test;

public class CodeElementsComparerTest {

    @Test
    public void testDiff() throws Exception {
        String newVersionCodePath = "data/code sample/AquaLush_Change4";
        String oldVersionCodePath = "data/code sample/AquaLush_Change3";

//        String newVersionCodePath = "data/code sample/iTrust_v11";
//        String oldVersionCodePath = "data/code sample/iTrust_v10";

        CodeElementsComparer comparer = new CodeElementsComparer(newVersionCodePath, oldVersionCodePath);
        comparer.diff();

        ArtifactsCollection changeDescriptionCollection = comparer.getChangeDescriptionCollection();

        System.out.println(changeDescriptionCollection.size());
    }
}