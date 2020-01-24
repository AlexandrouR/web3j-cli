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
package org.web3j.console.project;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.web3j.codegen.Console;
import org.web3j.console.project.java.JavaTestCreator;
import org.web3j.console.project.templates.TemplateProvider;
import org.web3j.console.project.utils.ProgressCounter;
import org.web3j.console.project.utils.ProjectUtils;
import org.web3j.crypto.CipherException;

public abstract class BaseProject<T extends BaseProject<T, P>, P extends TemplateProvider<P>> {
    protected final boolean withTests;
    protected final boolean withFatJar;
    protected final boolean withWallet;
    protected final boolean withSampleCode;
    protected final String command;
    protected final String solidityImportPath;
    protected final ProjectStructure projectStructure;
    protected ProjectWallet projectWallet;
    protected ProgressCounter progressCounter = new ProgressCounter(true);

    protected BaseProject(
            boolean withTests,
            boolean withFatJar,
            boolean withWallet,
            boolean withSampleCode,
            String command,
            String solidityImportPath,
            ProjectStructure projectStructure) {
        this.withTests = withTests;
        this.withFatJar = withFatJar;
        this.withWallet = withWallet;
        this.withSampleCode = withSampleCode;
        this.command = command;
        this.solidityImportPath = solidityImportPath;
        this.projectStructure = projectStructure;
    }

    public ProjectStructure getProjectStructure() {
        return this.projectStructure;
    }

    public ProjectWallet getProjectWallet() {
        return this.projectWallet;
    }

    protected void buildGradleProject(final String pathToDirectory)
            throws IOException, InterruptedException {
        if (!isWindows()) {
            setExecutable(pathToDirectory, "gradlew");
            executeBuild(
                    new File(pathToDirectory), new String[] {"bash", "-c", "./gradlew build -q"});
        } else {
            setExecutable(pathToDirectory, "gradlew.bat");
            executeBuild(
                    new File(pathToDirectory),
                    new String[] {"cmd.exe", "/c", "gradlew.bat build -q"});
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    private void setExecutable(final String pathToDirectory, final String gradlew) {
        final File f = new File(pathToDirectory + File.separator + gradlew);
        final boolean isExecutable = f.setExecutable(true);
    }

    private void executeBuild(final File workingDir, final String[] command)
            throws InterruptedException, IOException {
        executeProcess(workingDir, command);
    }

    private void executeProcess(File workingDir, String[] command)
            throws InterruptedException, IOException {
        int exitCode =
                new ProcessBuilder(command)
                        .directory(workingDir)
                        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                        .redirectError(ProcessBuilder.Redirect.INHERIT)
                        .start()
                        .waitFor();
        if (exitCode != 0) {
            Console.exitError("Could not build project.");
        }
    }

    protected void createFatJar(String pathToDirectory) throws IOException, InterruptedException {
        if (!isWindows()) {
            executeProcess(
                    new File(pathToDirectory),
                    new String[] {"bash", "./gradlew", "shadowJar", "-q"});
        } else {
            executeProcess(
                    new File(pathToDirectory),
                    new String[] {"cmd.exe", "/c", "./gradlew.bat shadowJar", "-q"});
        }
    }

    protected void generateTopLevelDirectories(ProjectStructure projectStructure) {
        projectStructure.createMainDirectory();
        System.out.println(projectStructure.getMainPath());
        projectStructure.createTestDirectory();
        projectStructure.createSolidityDirectory();
        projectStructure.createWrapperDirectory();
    }

    protected void generateTests(ProjectStructure projectStructure) throws IOException {

        new JavaTestCreator(
                        projectStructure.getGeneratedJavaWrappers(),
                        projectStructure.getPathToTestDirectory())
                .generate();
    }

    protected void generateWallet()
            throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
                    NoSuchProviderException, IOException {
        projectStructure.createWalletDirectory();
        projectWallet =
                new ProjectWallet(
                        ProjectUtils.generateWalletPassword(), projectStructure.getWalletPath());
        ProjectWriter.writeResourceFile(
                projectWallet.getWalletPassword(),
                projectWallet.getPasswordFileName(),
                projectStructure.getWalletPath());
    }

    public abstract P getTemplateProvider();

    public abstract TemplateProvider.TemplateBuilder<P> getTemplateBuilder();

    public void createProject()
            throws IOException, InterruptedException, NoSuchAlgorithmException,
                    NoSuchProviderException, InvalidAlgorithmParameterException, CipherException {
        generateTopLevelDirectories(projectStructure);
        if (withWallet) {
            generateWallet();
        }
        getTemplateProvider().generateFiles(projectStructure);
        progressCounter.processing("Creating " + projectStructure.projectName);
        buildGradleProject(projectStructure.getProjectRoot());

        if (withTests) {
            generateTests(projectStructure);
        }
        if (withFatJar) {
            createFatJar(projectStructure.getProjectRoot());
        }
        progressCounter.setLoading(false);
    }

    public abstract static class BaseBuilder {

        protected String solidityImportPath;
        protected boolean withWallet;
        protected boolean withTests;
        protected String projectName;
        protected String packageName;
        protected String rootDirectory;
        protected boolean withSampleCode;
        protected boolean withFatJar;
        protected String command = "new";

        public <T extends BaseBuilder> BaseBuilder withSolidityFile(
                final String solidityImportPath) {
            this.solidityImportPath = solidityImportPath;
            return this;
        }

        public <T extends BaseBuilder> BaseBuilder withWalletProvider(boolean withWalletProvider) {
            this.withWallet = withWalletProvider;
            return this;
        }

        public <T extends BaseBuilder> BaseBuilder withSampleCode(boolean withSampleCode) {
            this.withSampleCode = withSampleCode;
            return this;
        }

        public <T extends BaseBuilder> BaseBuilder withTests(boolean withTests) {
            this.withTests = withTests;
            return this;
        }

        public <T extends BaseBuilder> BaseBuilder withFatJar(boolean withFatJar) {
            this.withFatJar = withFatJar;
            return this;
        }

        public <T extends BaseBuilder> BaseBuilder withCommand(String command) {
            this.command = command;
            return this;
        }

        public <T extends BaseBuilder> BaseBuilder withProjectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public <T extends BaseBuilder> BaseBuilder withPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public <T extends BaseBuilder> BaseBuilder withRootDirectory(String rootDirectory) {
            this.rootDirectory = rootDirectory;
            return this;
        }

        protected abstract <T extends BaseProject> T build() throws Exception;
    }
}
