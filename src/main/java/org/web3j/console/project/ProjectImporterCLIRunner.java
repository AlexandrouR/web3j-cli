/*
 * Copyright 2019 Web3 Labs LTD.
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

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import org.web3j.console.project.utills.InputVerifier;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.console.project.ProjectImporter.COMMAND_IMPORT;

@Command(name = COMMAND_IMPORT)
public class ProjectImporterCLIRunner extends ProjectCreatorCLIRunner {

    @Option(
            names = {"-s", "--solidity-path"},
            description = "path to solidity file/folder",
            required = true)
    String solidityImportPath;

    @Override
    public void run() {
        if (InputVerifier.requiredArgsAreNotEmpty(projectName, packageName, solidityImportPath)) {
            if (InputVerifier.classNameIsValid(projectName)) {
                if (InputVerifier.packageNameIsValid(packageName)) {
                    try {
                        new ProjectImporter(outputDir, packageName, projectName, solidityImportPath)
                                .generate();
                    } catch (final Exception e) {
                        exitError(e);
                    }
                } else {
                    exitError(packageName + " is not a valid package name.");
                }
            } else {
                exitError(projectName + " is not a valid name.");
            }

        } else {
            exitError("Please make sure the required parameters are not empty.");
        }
    }
}
