/*
 * Copyright 2019 Web3 Labs Ltd.
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
package org.web3j.console.project.templates;

import java.io.File;
import java.io.IOException;

import org.web3j.console.project.ProjectStructure;
import org.web3j.console.project.ProjectWriter;
import org.web3j.console.project.utils.InputVerifier;

public abstract class TemplateProvider<T extends TemplateProvider<T>> {
    private final String mainJavaClass;
    protected final String solidityContract;
    protected final String pathToSolidityFolder;
    private final String gradleBuild;
    private final String gradleSettings;
    private final String gradlewWrapperSettings;
    private final String gradlewBatScript;
    private final String gradlewScript;
    private final String gradlewJar;
    private final String packageNameReplacement;
    private final String projectNameReplacement;
    private final String passwordFileName;
    private final String walletNameReplacement;

    protected TemplateProvider(
            final String mainJavaClass,
            final String solidityContract,
            final String pathToSolidityFolder,
            final String gradleBuild,
            final String gradleSettings,
            final String gradlewWrapperSettings,
            final String gradlewBatScript,
            final String gradlewScript,
            final String gradlewJar,
            String packageNameReplacement,
            String projectNameReplacement,
            String passwordFileName,
            String walletNameReplacement) {
        this.mainJavaClass = mainJavaClass;
        this.solidityContract = solidityContract;
        this.pathToSolidityFolder = pathToSolidityFolder;
        this.gradleBuild = gradleBuild;
        this.gradleSettings = gradleSettings;
        this.gradlewWrapperSettings = gradlewWrapperSettings;
        this.gradlewBatScript = gradlewBatScript;
        this.gradlewScript = gradlewScript;
        this.gradlewJar = gradlewJar;
        this.packageNameReplacement = packageNameReplacement;
        this.projectNameReplacement = projectNameReplacement;
        this.passwordFileName = passwordFileName;
        this.walletNameReplacement = walletNameReplacement;
    }

    public String getSolidityContract() {
        return solidityContract;
    }

    public String getMainJavaClass() {
        return mainJavaClass;
    }

    public String getGradleBuild() {
        return gradleBuild;
    }

    public String getGradleSettings() {
        return gradleSettings;
    }

    public String getGradlewWrapperSettings() {
        return gradlewWrapperSettings;
    }

    public String getGradlewBatScript() {
        return gradlewBatScript;
    }

    public String getGradlewScript() {
        return gradlewScript;
    }

    public String getGradlewJar() {
        return gradlewJar;
    }

    public String loadMainJavaClass() throws IOException {
        return TemplateReader.readFile(mainJavaClass)
                .replaceAll(
                        "<project_name>",
                        InputVerifier.capitalizeFirstLetter(projectNameReplacement))
                .replaceAll("<package_name>", packageNameReplacement)
                .replaceAll("<wallet_name>", walletNameReplacement)
                .replaceAll("<password_file_name>", passwordFileName);
    }

    public String loadGradleBuild() throws IOException {
        return TemplateReader.readFile(gradleBuild)
                .replaceAll("<package_name>", packageNameReplacement)
                .replaceAll("<project_name>", projectNameReplacement);
    }

    public String loadSolidityContract() throws IOException {
        return TemplateReader.readFile(solidityContract);
    }

    public String loadGradleSettings() throws IOException {
        return TemplateReader.readFile(gradleSettings)
                .replaceAll("<project_name>", projectNameReplacement);
    }

    public String loadGradlewWrapperSettings() throws IOException {

        return TemplateReader.readFile(gradlewWrapperSettings);
    }

    public String loadGradlewBatScript() throws IOException {

        return TemplateReader.readFile(gradlewBatScript);
    }

    public String loadGradlewScript() throws IOException {

        return TemplateReader.readFile(gradlewScript);
    }

    public void generateFiles(ProjectStructure projectStructure) throws IOException {
        ProjectWriter.writeResourceFile(
                loadMainJavaClass(),
                InputVerifier.capitalizeFirstLetter(projectStructure.getProjectName() + ".java"),
                projectStructure.getMainPath());
        ProjectWriter.writeResourceFile(
                loadGradleBuild(), "build.gradle", projectStructure.getProjectRoot());
        ProjectWriter.writeResourceFile(
                loadGradleSettings(), "settings.gradle", projectStructure.getProjectRoot());
        if (solidityContract != null)
            ProjectWriter.writeResourceFile(
                    loadSolidityContract(), "HelloWorld.sol", projectStructure.getSolidityPath());
        if (pathToSolidityFolder != null) {
            ProjectWriter.importSolidityProject(
                    new File(pathToSolidityFolder), projectStructure.getSolidityPath());
        }
        ProjectWriter.writeResourceFile(
                loadGradlewWrapperSettings(),
                "gradle-wrapper.properties",
                projectStructure.getWrapperPath());
        ProjectWriter.writeResourceFile(
                loadGradlewScript(), "gradlew", projectStructure.getProjectRoot());
        ProjectWriter.writeResourceFile(
                loadGradlewBatScript(), "gradlew.bat", projectStructure.getProjectRoot());
        ProjectWriter.copyResourceFile(
                getGradlewJar(),
                projectStructure.getWrapperPath() + File.separator + "gradle-wrapper.jar");
    }

    public abstract static class TemplateBuilder<
            P extends TemplateProvider<P>, B extends TemplateBuilder<P, B>> {
        protected String mainJavaClass;
        protected String gradleBuild;
        protected String gradleSettings;
        protected String gradlewWrapperSettings;
        protected String gradlewBatScript;
        protected String gradlewScript;
        protected String solidityProject;
        protected String gradlewWrapperJar;
        protected String packageNameReplacement;
        protected String projectNameReplacement;
        protected String passwordFileName;
        protected String walletNameReplacement;
        protected String pathToSolidityFolder;

        public TemplateBuilder<P, B> withMainJavaClass(String mainJavaClass) {
            this.mainJavaClass = mainJavaClass;
            return this;
        }

        public TemplateBuilder<P, B> withGradleBuild(String gradleBuild) {
            this.gradleBuild = gradleBuild;
            return this;
        }

        public TemplateBuilder<P, B> withGradleSettings(String gradleSettings) {
            this.gradleSettings = gradleSettings;
            return this;
        }

        public TemplateBuilder<P, B> withWrapperGradleSettings(String gradlewWrapperSettings) {
            this.gradlewWrapperSettings = gradlewWrapperSettings;
            return this;
        }

        public TemplateBuilder<P, B> withGradleBatScript(String gradlewBatScript) {
            this.gradlewBatScript = gradlewBatScript;
            return this;
        }

        public TemplateBuilder<P, B> withGradleScript(String gradlewScript) {
            this.gradlewScript = gradlewScript;
            return this;
        }

        public TemplateBuilder<P, B> withGradlewWrapperJar(String gradlewWrapperJar) {
            this.gradlewWrapperJar = gradlewWrapperJar;
            return this;
        }

        public TemplateBuilder<P, B> withSolidityProject(String solidityProject) {
            this.solidityProject = solidityProject;
            return this;
        }

        public TemplateBuilder<P, B> withPathToSolidityFolder(String pathToSolidityFolder) {
            this.pathToSolidityFolder = pathToSolidityFolder;
            return this;
        }

        public TemplateBuilder<P, B> withPackageNameReplacement(String packageNameReplacement) {
            this.packageNameReplacement = packageNameReplacement;
            return this;
        }

        public TemplateBuilder<P, B> withProjectNameReplacement(String projectNameReplacement) {
            this.projectNameReplacement = projectNameReplacement;
            return this;
        }

        public TemplateBuilder<P, B> withPasswordFileName(String passwordFileName) {
            this.passwordFileName = passwordFileName;
            return this;
        }

        public TemplateBuilder<P, B> withWalletNameReplacement(String walletNameReplacement) {
            this.walletNameReplacement = walletNameReplacement;
            return this;
        }

        public abstract P build();
    }
}
