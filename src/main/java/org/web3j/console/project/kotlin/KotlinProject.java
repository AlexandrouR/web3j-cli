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
package org.web3j.console.project.kotlin;

import org.web3j.commons.JavaVersion;
import org.web3j.console.project.BaseProject;
import org.web3j.console.project.ProjectStructure;
import org.web3j.console.project.templates.kotlin.KotlinTemplateProvider;

public class KotlinProject extends BaseProject {
    protected KotlinProject(
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

    @Override
    public KotlinTemplateProvider.KotlinTemplateBuilder getTemplateBuilder() {
        KotlinTemplateProvider.KotlinTemplateBuilder templateBuilder =
                (KotlinTemplateProvider.KotlinTemplateBuilder)
                        new KotlinTemplateProvider.KotlinTemplateBuilder()
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

    public KotlinTemplateProvider getTemplateProvider() {
        KotlinTemplateProvider.KotlinTemplateBuilder templateBuilder = getTemplateBuilder();
        if (command.equals("kotlin")) {
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
        return (KotlinTemplateProvider) templateBuilder.build();
    }

    public static class KotlinBuilder extends BaseProject.BaseBuilder {

        @Override
        protected KotlinProject build() throws Exception {
            final ProjectStructure projectStructure =
                    new KotlinProjectStructure(rootDirectory, packageName, projectName);
            return new KotlinProject(
                    super.withTests,
                    super.withFatJar,
                    super.withWallet,
                    super.withSampleCode,
                    super.command,
                    super.solidityImportPath,
                    projectStructure);
        }
    }
}
