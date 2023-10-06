/*
 * Copyright 2017 Florida Institute for Human and Machine Cognition (IHMC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jassimp;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

/**
 * Helper class for use within IHMC to automatically load the native libraries
 * for jassimp
 *
 * @author Jesper Smith
 *
 */
public class IHMCJassimp {

    static {
        Jassimp.setLibraryLoader(new IHMCJassimpNativeLibraryLoader());
    }

    /**
     * Imports a file via assimp without post processing.
     *
     * @param filename the file to import
     * @return the loaded scene
     *
     * @throws IOException if an error occurs
     */
    public static AiScene importFile(String filename) throws IOException {
        return Jassimp.importFile(filename, EnumSet.noneOf(AiPostProcessSteps.class));
    }

    /**
     * Imports a file via assimp without post processing.
     *
     * @param filename the file to import
     * @param ioSystem ioSystem to load files, or null for default
     * @return the loaded scene
     *
     * @throws IOException if an error occurs
     */
    public static AiScene importFile(String filename, AiIOSystem<?> ioSystem) throws IOException {
        return Jassimp.importFile(filename, EnumSet.noneOf(AiPostProcessSteps.class), ioSystem);
    }

    /**
     * Imports a file via assimp.
     *
     * @param filename the file to import
     * @param postProcessing post processing flags
     * @return the loaded scene, or null if an error occurred
     *
     * @throws IOException if an error occurs
     */
    public static AiScene importFile(String filename, Set<AiPostProcessSteps> postProcessing) throws IOException {
        return Jassimp.importFile(filename, postProcessing, null);
    }

    /**
     * Imports a file via assimp.
     *
     * @param filename the file to import
     * @param postProcessing post processing flags
     * @param ioSystem ioSystem to load files, or null for default
     * @return the loaded scene, or null if an error occurred
     *
     * @throws IOException if an error occurs
     */
    public static AiScene importFile(String filename, Set<AiPostProcessSteps> postProcessing, AiIOSystem<?> ioSystem) throws IOException {
        return Jassimp.importFile(filename, postProcessing, ioSystem);
    }
    

    /**
     * Imports a file via assimp.
     *
     * @param filename the file to import
     * @param postProcessing post processing flags
     * @return the loaded scene, or null if an error occurred
     *
     * @throws IOException if an error occurs
     */
    public static AiScene importFile(String filename, long postProcessing) throws IOException {
        return Jassimp.importFile(filename, postProcessing, null);
    }

    /**
     * Imports a file via assimp.
     *
     * @param filename the file to import
     * @param postProcessing post processing flags
     * @param ioSystem ioSystem to load files, or null for default
     * @return the loaded scene, or null if an error occurred
     *
     * @throws IOException if an error occurs
     */
    public static AiScene importFile(String filename, long postProcessing, AiIOSystem<?> ioSystem) throws IOException {
        return Jassimp.importFile(filename, postProcessing, ioSystem);
    }
}
