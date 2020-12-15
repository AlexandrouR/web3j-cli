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
package org.web3j.console.project.java;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.console.project.ProjectStructure;
import org.web3j.console.project.utils.Folders;
import org.web3j.crypto.WalletUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaProjectTest {
    private ProjectStructure projectStructure;

    @BeforeEach
    public void setUpProject() throws Exception {
        final String rootDirectory = Folders.tempBuildFolder().getAbsolutePath();
        projectStructure = new JavaProjectStructure(rootDirectory, "test", "test");
        Map<String, String> walletCredentials = new HashMap<>();
        final File testWalletDirectory = new File(rootDirectory + File.separator + "keystore");
        testWalletDirectory.mkdirs();
        String absoluteWalletPath =
                testWalletDirectory
                        + File.separator
                        + WalletUtils.generateNewWalletFile("", testWalletDirectory);
        walletCredentials.put("path", absoluteWalletPath);
        walletCredentials.put("password", "");
        JavaProject javaProject =
                new JavaBuilder()
                        .withProjectName(projectStructure.getProjectName())
                        .withPackageName(projectStructure.getPackageName())
                        .withRootDirectory(rootDirectory)
                        .build();
        javaProject.createProject();
    }

    @Test
    public void directoryCreationTest() {
        final boolean mainProjectDir = new File(projectStructure.getMainPath()).exists();
        final boolean gradleWrapperDir = new File(projectStructure.getWrapperPath()).exists();
        final boolean testProjectDir = new File(projectStructure.getPathToTestDirectory()).exists();
        final boolean solidityPath = new File(projectStructure.getSolidityPath()).exists();
        assertTrue(mainProjectDir && gradleWrapperDir && testProjectDir && solidityPath);
    }

    @Test
    public void fileCreationTest() {
        final boolean mainJavaClass =
                new File(projectStructure.getMainPath() + File.separator + "Test.java").exists();
        final boolean greeterContract =
                new File(projectStructure.getSolidityPath() + File.separator + "HelloWorld.sol")
                        .exists();
        final boolean gradleBuild =
                new File(projectStructure.getProjectRoot() + File.separator + "build.gradle")
                        .exists();
        final boolean gradleSettings =
                new File(projectStructure.getProjectRoot() + File.separator + "settings.gradle")
                        .exists();
        final boolean gradleWrapperSettings =
                new File(
                                projectStructure.getWrapperPath()
                                        + File.separator
                                        + "gradle-wrapper.properties")
                        .exists();
        final boolean gradleWrapperJar =
                new File(projectStructure.getWrapperPath() + File.separator + "gradle-wrapper.jar")
                        .exists();
        final boolean gradlewBatScript =
                new File(projectStructure.getProjectRoot() + File.separator + "gradlew.bat")
                        .exists();
        final boolean gradlewScript =
                new File(projectStructure.getProjectRoot() + File.separator + "gradlew").exists();

        assertTrue(
                mainJavaClass
                        && greeterContract
                        && gradleBuild
                        && gradleSettings
                        && gradleWrapperSettings
                        && gradleWrapperJar
                        && gradlewBatScript
                        && gradlewScript);
    }
}
