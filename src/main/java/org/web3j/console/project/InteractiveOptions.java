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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Optional;
import java.util.Scanner;

import org.web3j.console.project.utills.InputVerifier;

class InteractiveOptions {

    private final Scanner scanner;
    private final Writer writer;

    InteractiveOptions() {
        this(System.in, System.out);
    }

    InteractiveOptions(final InputStream inputStream, final OutputStream outputStream) {
        this.scanner = new Scanner(inputStream);
        this.writer = new PrintWriter(outputStream);
    }

    protected final String getProjectName() {
        print("Please enter the project name (Required Field):");
        String projectName = getUserInput();
        while (!InputVerifier.classNameIsValid(projectName)) {
            System.out.println(projectName + " is a not valid name.");
            projectName = getUserInput();
        }
        return projectName;
    }

    protected final String getPackageName() {
        print("Please enter the package name for your project (Required Field): ");
        String packageName = getUserInput();
        while (!InputVerifier.packageNameIsValid(packageName)) {
            System.out.println(packageName + " is not a valid package name.");
            packageName = getUserInput();
        }
        return packageName;
    }

    protected final Optional<String> getProjectDestination() {
        print("Please enter the destination of your project (Current by default): ");
        final String projectDest = getUserInput();
        return projectDest.isEmpty() ? Optional.empty() : Optional.of(projectDest);
    }

    String getUserInput() {

        return scanner.nextLine();
    }

    private void print(final String text) {
        System.out.println(text);
    }
}
