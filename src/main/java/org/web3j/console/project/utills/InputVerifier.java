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
package org.web3j.console.project.utills;

import javax.lang.model.SourceVersion;

public class InputVerifier {
    public static boolean requiredArgsAreNotEmpty(final String... args) {
        for (final String argument : args) {
            if (argument.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean classNameIsValid(final String className) {
        if (!SourceVersion.isIdentifier(className) || SourceVersion.isKeyword(className)) {
            return false;
        }
        return true;
    }

    public static boolean packageNameIsValid(final String packageName) {
        String[] splitPackageName = packageName.split("[.]");
        for (final String argument : splitPackageName) {
            if (!SourceVersion.isIdentifier(argument) || SourceVersion.isKeyword(argument)) {
                return false;
            }
        }
        return true;
    }

    public static String capitalizeFirstLetter(String input) {

        if (Character.isUpperCase(input.charAt(0))) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
