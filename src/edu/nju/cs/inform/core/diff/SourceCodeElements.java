package edu.nju.cs.inform.core.diff;

import edu.nju.cs.inform.core.jdt.JavaElementsParser;
import edu.nju.cs.inform.core.type.ArtifactsCollection;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by niejia on 16/3/15.
 */
public class SourceCodeElements {

    private Set<String> packagesList;
    private Set<String> classesList;
    private Set<String> methodsList;
    private Set<String> fieldsList;

    public SourceCodeElements(ArtifactsCollection codeCollection) {
        packagesList = new LinkedHashSet<>();
        classesList = new LinkedHashSet<>();
        methodsList = new LinkedHashSet<>();
        fieldsList = new LinkedHashSet<>();

        for (String className : codeCollection.keySet()) {
            String contentInCode = codeCollection.get(className).text;
            JavaElementsParser parser = new JavaElementsParser(contentInCode, this);

        }

    }

    public void addPackage(String packageName) {
        getPackagesList().add(packageName);
    }

    public void addClass(String className) {
        getClassesList().add(className);
    }

    public void addMethod(String methodName) {
        getMethodsList().add(methodName);
    }

    public void addField(String fieldName) {
        getFieldsList().add(fieldName);
    }

    public Set<String> getPackagesList() {
        return packagesList;
    }

    public Set<String> getClassesList() {
        return classesList;
    }

    public Set<String> getMethodsList() {
        return methodsList;
    }

    public Set<String> getFieldsList() {
        return fieldsList;
    }
}
