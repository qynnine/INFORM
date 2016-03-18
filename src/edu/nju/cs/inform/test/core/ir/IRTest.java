package edu.nju.cs.inform.test.core.ir;

import edu.nju.cs.inform.core.diff.CodeElementsComparer;
import edu.nju.cs.inform.core.ir.IR;
import edu.nju.cs.inform.core.ir.IRModelConst;
import edu.nju.cs.inform.core.preprocess.ArtifactPreprocessor;
import edu.nju.cs.inform.core.type.Artifact;
import edu.nju.cs.inform.core.type.ArtifactsCollection;
import edu.nju.cs.inform.core.type.SimilarityMatrix;
import edu.nju.cs.inform.io.ArtifactsReader;
import org.junit.Test;

public class IRTest {

    @Test
    public void testCompute() throws Exception {
        String newVersionCodePath = "data/sample/AquaLush_Change4";
        String oldVersionCodePath = "data/sample/AquaLush_Change3";
        String requirementPath = "data/sample/AquaLush_Requirement";

        CodeElementsComparer comparer = new CodeElementsComparer(newVersionCodePath, oldVersionCodePath);
        comparer.diff();

        ArtifactsCollection changeDescriptionCollection = comparer.getChangeDescriptionCollection();
        ArtifactsCollection requirementCollection = ArtifactsReader.getCollections(requirementPath, ".txt");

        for (String change : changeDescriptionCollection.keySet()) {
            Artifact artifact = changeDescriptionCollection.get(change);
            artifact.text = ArtifactPreprocessor.handleJavaFile(artifact.text);
        }

        for (String requirement : requirementCollection.keySet()) {
            Artifact artifact = requirementCollection.get(requirement);
            artifact.text = ArtifactPreprocessor.handlePureTextFile(artifact.text);
        }

        SimilarityMatrix similarityMatrix = IR.compute(changeDescriptionCollection, requirementCollection, IRModelConst.VSM);
        System.out.println(similarityMatrix );
    }
}