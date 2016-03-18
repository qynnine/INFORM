package edu.nju.cs.inform.core.diff;

import edu.nju.cs.inform.core.type.*;
import edu.nju.cs.inform.io.ArtifactsReader;
import edu.nju.cs.inform.util.JavaElement;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by niejia on 16/3/15.
 */
public class CodeElementsComparer {

    private String postfixName = ".java";

    private SourceCodeElements newVersionCodeElements;
    private SourceCodeElements oldVersionCodeElements;

    private Set<CodeElementChange> codeElementChangesList;

    private ArtifactsCollection changeDescriptionCollection;

    public CodeElementsComparer(String newVersionCodeDirPath, String oldVersionCodeDirPath) {
        ArtifactsCollection newVersionCodeCollection = ArtifactsReader.getCollections(newVersionCodeDirPath, postfixName);
        ArtifactsCollection oldVersionCodeCollection = ArtifactsReader.getCollections(oldVersionCodeDirPath, postfixName);

        this.newVersionCodeElements = new SourceCodeElements(newVersionCodeCollection);
        this.oldVersionCodeElements = new SourceCodeElements(oldVersionCodeCollection);
        this.codeElementChangesList = new LinkedHashSet<>();
        this.changeDescriptionCollection = new ArtifactsCollection();
    }

    public void diff() {
        identifyChanges(newVersionCodeElements.getPackagesList(), oldVersionCodeElements.getPackagesList(), ElementType.Package);
        identifyChanges(newVersionCodeElements.getClassesList(), oldVersionCodeElements.getClassesList(), ElementType.Class);
        identifyChanges(newVersionCodeElements.getMethodsList(), oldVersionCodeElements.getMethodsList(), ElementType.Method);
        identifyChanges(newVersionCodeElements.getFieldsList(), oldVersionCodeElements.getFieldsList(), ElementType.Field);

        // filter the level duplication
        filterOn();
        extractChangeDescription();
    }

    private void filterOn() {

        Set<String> changedPackagesList = getChangedElementsByElementType(ElementType.Package);
        Set<String> changedClassesList = getChangedElementsByElementType(ElementType.Class);

        Iterator iterator1 = codeElementChangesList.iterator();

        while (iterator1.hasNext()) {
            CodeElementChange codeElementChange = (CodeElementChange) iterator1.next();
            if (codeElementChange.getElementType().equals(ElementType.Class)) {
                String className = codeElementChange.getElementName();
                if (classBelongsToChangedPackage(className, changedPackagesList)) {
                    iterator1.remove();
                }
            }
        }

        Iterator iterator2 = codeElementChangesList.iterator();

        while (iterator2.hasNext()) {
            CodeElementChange codeElementChange = (CodeElementChange) iterator2.next();
            if (codeElementChange.getElementType().equals(ElementType.Method)
                    || codeElementChange.getElementType().equals(ElementType.Field)) {
                String elementName = codeElementChange.getElementName();
                if (elementBelongsToChangedClass(elementName, changedClassesList)) {
                    iterator2.remove();
                }
            }
        }
    }

    private boolean elementBelongsToChangedClass(String elementName, Set<String> changedClassesList) {
        String classNameOfTarget = JavaElement.getClassName(elementName);
        for (String cp : changedClassesList) {
            if (cp.equals(classNameOfTarget)) {
                return true;
            }
        }
        return false;
    }

    private void extractChangeDescription() {
        // simply mock some change description
        for (CodeElementChange elementChange : codeElementChangesList) {
            Artifact artifact = new Artifact(elementChange.getElementName(), elementChange.getElementName());
            changeDescriptionCollection.put(elementChange.getElementName(), artifact);
        }
    }

    private boolean classBelongsToChangedPackage(String className, Set<String> changedPackagesList) {
        String packageNameOfTarget = JavaElement.getPackageName(className);
        for (String cp : changedPackagesList) {
            if (cp.equals(packageNameOfTarget)) {
                return true;
            }
        }
        return false;
    }

    private Set<String> getChangedElementsByElementType(ElementType elementType) {
        Set<String> changedElementsList = new LinkedHashSet<>();

        for (CodeElementChange elementChange : codeElementChangesList) {
            if (elementChange.getElementType().equals(elementType)) {
                changedElementsList.add(elementChange.getElementName());
            }
        }

        return changedElementsList;
    }

    private void identifyChanges(Set<String> elementsInNewVersionCode, Set<String> elementsInOldVersionCode, ElementType elementType) {
        Set<String> unchangedElements = new LinkedHashSet<>();

        for (String p : elementsInNewVersionCode) {
            if (elementsInOldVersionCode.contains(p)) {
                unchangedElements.add(p);
            }
        }

        for (String p : elementsInNewVersionCode) {
            if (!unchangedElements.contains(p)) {
                CodeElementChange elementChange = new CodeElementChange(p, elementType, ChangeType.Added);
                codeElementChangesList.add(elementChange);
            }
        }

        for (String p : elementsInOldVersionCode) {
            if (!unchangedElements.contains(p)) {
                CodeElementChange elementChange = new CodeElementChange(p, elementType, ChangeType.Removed);
                codeElementChangesList.add(elementChange);
            }
        }
    }

    public Set<CodeElementChange> getCodeElementChangesList() {
        return codeElementChangesList;
    }

    public ArtifactsCollection getChangeDescriptionCollection() {
        return changeDescriptionCollection;
    }
}
