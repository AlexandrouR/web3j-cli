/*
 * Copyright 2020 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.console.project.java;

import org.web3j.commons.JavaVersion;
import org.web3j.console.project.BaseProject;
import org.web3j.console.project.ProjectStructure;
import org.web3j.console.project.templates.TemplateProvider;
import org.web3j.console.project.templates.java.JavaTemplateProvider;

public class JavaProject extends BaseProject<JavaProject, JavaTemplateProvider> {
    protected JavaProject(
            boolean withTests,
            boolean withFatJar,
            boolean withWallet,
            boolean withSampleCode,
            String command,
            String solidityImportPath,
            ProjectStructure projectStructure) {
        super(
                withTests,
                withFatJar,
                withWallet,
                withSampleCode,
                command,
                solidityImportPath,
                projectStructure);
    }

    public JavaTemplateProvider getTemplateProvider() {
        TemplateProvider.TemplateBuilder templateBuilder = getTemplateBuilder();
        if (command.equals("new")) {
            templateBuilder
                    .withGradleBuild(
                            JavaVersion.getJavaVersionAsDouble() < 11
                                    ? "build.gradle.template"
                                    : "build.gradleJava11.template")
                    .withSolidityProject("HelloWorld.sol")
                    .withMainJavaClass("Template-Kotlin");
        } else if (command.equals("import")) {
            templateBuilder
                    .withGradleBuild(
                            JavaVersion.getJavaVersionAsDouble() < 11
                                    ? "build.gradleImport.template"
                                    : "build.gradleImportJava11.template")
                    .withPathToSolidityFolder(solidityImportPath)
                    .withMainJavaClass("EmptyTemplate.java");
        }
        return (JavaTemplateProvider) templateBuilder.build();
    }

    @Override
    public TemplateProvider.TemplateBuilder getTemplateBuilder() {
        JavaTemplateProvider.JavaTemplateBuilder templateBuilder =
                new JavaTemplateProvider.JavaTemplateBuilder()
                        .withProjectNameReplacement(projectStructure.projectName)
                        .withPackageNameReplacement(projectStructure.packageName)
                        .withGradleBatScript("gradlew.bat.template")
                        .withGradleScript("gradlew.template")
                        .withWrapperGradleSettings("gradlew-wrapper.properties.template")
                        .withGradlewWrapperJar("gradle-wrapper.jar")
                        .withGradleSettings("settings.gradle.template");

        if (projectWallet != null) {
            templateBuilder.withWalletNameReplacement(projectWallet.getWalletName());
            templateBuilder.withPasswordFileName(projectWallet.getPasswordFileName());
        }
        return templateBuilder;
    }
}

public static class JavaBuilder extends BaseProject.BaseBuilder {

    @Override
    public JavaProject build() throws Exception {
        final ProjectStructure projectStructure =
                new JavaProjectStructure(rootDirectory, packageName, projectName);
        return new JavaProject(
                super.withTests,
                super.withFatJar,
                super.withWallet,
                super.withSampleCode,
                super.command,
                super.solidityImportPath,
                projectStructure);
    }
}
