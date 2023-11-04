/*
 * Ну вы же понимаете, что код здесь только мой?
 * Well, you do understand that the code here is only mine?
 */

package jassimp;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 * File: NativeLoader.java
 * Created on 4 нояб. 2023 г., 20:02:44
 *
 * @author LWJGL2
 */
public class NativeLoader {

    public static void init() {
        String osName = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        boolean is64bit = false;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            is64bit = (System.getenv("ProgramFiles(x86)") != null);
        } else {
            is64bit = (System.getProperty("os.arch").indexOf("64") != -1);
        }

        String detectedOS = "";
        if ((osName.indexOf("mac") >= 0) || (osName.indexOf("darwin") >= 0)) {
            detectedOS = "macos";
        } else if (osName.indexOf("win") >= 0) {
            detectedOS = "windows";
        } else if (osName.indexOf("nux") >= 0) {
            detectedOS = "linux";
        } else {
            detectedOS = osName;
        }

        File nativesDir = new File("cache/natives");
        nativesDir.mkdirs();

        File nativesFile = null;
        switch (detectedOS) {
            case "macos": {
                String libName = null;
                if (is64bit) {
                    libName = "libjassimp.dylib";
                }
                if (libName != null) {
                    nativesFile = new File(nativesDir, libName);
                    try {
                        InputStream stream = NativeLoader.class.getResourceAsStream("/jassimp/" + libName);
                        Files.copy(stream, nativesFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (Throwable e) {
                        System.out.println("Failed to copy library: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                break;
            }
            case "windows": {
                String libName = null;
                if (is64bit) {
                    libName = "libjassimp.dll";
                } else {
                    libName = "libjassimp-x86.dll";
                }
                if (libName != null) {
                    nativesFile = new File(nativesDir, libName);
                    try {
                        System.out.println("copy");
                        InputStream stream = NativeLoader.class.getResourceAsStream("/jassimp/" + libName);
                        Files.copy(stream, nativesFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (Throwable e) {
                        System.out.println("Failed to copy library: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                break;
            }
            case "linux": {
                String libName = null;
                if (is64bit) {
                    libName = "libjassimp.so";
                } else {
                    libName = "libjassimp.so";
                }
                if (libName != null) {
                    nativesFile = new File(nativesDir, libName);
                    try {
                        InputStream stream = NativeLoader.class.getResourceAsStream("/jassimp/" + libName);
                        Files.copy(stream, nativesFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (Throwable e) {
                        System.out.println("Failed to copy library: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
        if (nativesFile == null) {
            System.out.println("[LibGizmo] You Operating System not supported. os.name=" + osName + ", is64bit=" + is64bit);
            JOptionPane.showMessageDialog(null, "[LibGizmo] You Operating System not supported. os.name=" + osName + ", is64bit=" + is64bit, "Startup error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } else if (!nativesFile.exists()) {
            System.out.println("[LibGizmo] Natives file not found. You Operating System not supported. os.name=" + osName + ", is64bit=" + is64bit);
            JOptionPane.showMessageDialog(null, "[LibGizmo] Natives file not found. Maybe You Operating System not supported. os.name=" + osName + ", is64bit=" + is64bit, "Startup error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } else {
            System.load(nativesFile.getAbsolutePath());
        }

    }
}
