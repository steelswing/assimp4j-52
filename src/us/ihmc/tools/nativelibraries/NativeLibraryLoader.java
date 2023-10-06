
package us.ihmc.tools.nativelibraries;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import us.ihmc.tools.nativelibraries.NativeLibraryDescription.Architecture;
import us.ihmc.tools.nativelibraries.NativeLibraryDescription.OperatingSystem;

/**
 * Helper class that unpacks and optionally loads native libraries
 *
 * @author Jesper Smith
 */
public class NativeLibraryLoader {

    public static Path ROOT = new File(".").toPath().getRoot();

    public static String LIBRARY_LOCATION = new File(OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows ? System.getenv("SystemDrive") : "", ".ihmc" + File.separator + "lib").getAbsolutePath();
    private static final HashMap<String, List<String>> extractedLibraries = new HashMap<>();
    private static final HashSet<String> loadedLibraries = new HashSet<>();

    private NativeLibraryLoader() {
        // Disallow construction
    }

    /**
     * Convenience method to load a single library that's available on all platforms. Throws a
     * UnsatisfiedLinkError if the library cannot be found.
     *
     * @param packageName
     * @param libraryName
     */
    public static void loadLibrary(String packageName, String libraryName) {
        if (!loadLibrary(new DefaultNativeLibraryDescription(packageName, libraryName))) {
            throw new UnsatisfiedLinkError("Cannot load " + createPackagePrefix(packageName) + libraryName);
        }
    }

    /**
     * Extract the library if necessary, returns full path to the library. Useful for JNA libraries.
     *
     * @param packageName Name of the package
     * @param libraryName Name of the library
     * @return Full path to the library.
     */
    public static String extractLibrary(String packageName, String libraryName) {
        return extractLibraries(packageName, libraryName);
    }

    /**
     * Extract multiple JNA libraries. Returns the full path to the first library. The path
     * is based on the first library name.
     *
     * @param packageName Name of the package
     * @param mainLibrary Name of the main library. A SHA-1 hash is taken of this library.
     * @param libraries Names of the libraries
     * @return Full path to the mainLibrary
     */
    public static String extractLibraries(String packageName, String mainLibrary, String... libraries) {

        NativeLibraryWithDependencies library = NativeLibraryWithDependencies.fromPlatform(getOS(), getArchitecture(), mainLibrary, libraries);

        List<String> extracted = extractLibraryWithDependenciesAbsolute(packageName, library);

        return extracted.get(0);
    }

    @Deprecated
    public static String extractLibraryAbsolute(String packageName, String library) {
        NativeLibraryWithDependencies libraryWithDependencies = NativeLibraryWithDependencies.fromFilename(library);
        return extractLibraryWithDependenciesAbsolute(packageName, libraryWithDependencies).get(0);
    }

    /**
     * Extract multiple JNA libraries.
     * <p>
     * The extraction path is based on the first library name.
     *
     * @param packageName Name of the package
     * @param library NativeLibrary description of the libraries
     * @return List with paths to the libraries
     */
    public synchronized static List<String> extractLibraryWithDependenciesAbsolute(String packageName, NativeLibraryWithDependencies library) {

        try {

            String hash = getHash(packageName, library);

            if (extractedLibraries.containsKey(hash)) {
                return extractedLibraries.get(hash);
            }

            String prefix = createPackagePrefix(packageName);
            File packageDirectory = new File(LIBRARY_LOCATION, prefix);
            File directory = new File(packageDirectory, hash);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            List<String> libraryFiles = new ArrayList<>();

            List<InputStream> inputStreams = getInputstreams(packageName, library);

            libraryFiles.add(extractFile(directory, library.getLibraryFilename(), inputStreams.get(0)));

            for (int i = 1; i < inputStreams.size(); i++) {
                libraryFiles.add(extractFile(directory, library.getDependencyFilenames()[i - 1], inputStreams.get(i)));
            }

            closeInputStreams(inputStreams);

            extractedLibraries.put(hash, libraryFiles);
            return libraryFiles;
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new UnsatisfiedLinkError(e.getMessage());
        }

    }

    /**
     * Tries to load libraries in libraryDescription. Returns false if libraries cannot be loaded
     *
     * @param libraryDescription
     * @return true if the library is loaded
     */
    public synchronized static boolean loadLibrary(NativeLibraryDescription libraryDescription) {
        try {
            Architecture arch = getArchitecture();
            OperatingSystem platform = getOS();

            String packageName = libraryDescription.getPackage(platform, arch);
            NativeLibraryWithDependencies library = libraryDescription.getLibraryWithDependencies(platform, arch);
            if (library == null) {
                return false;
            }

            loadLibraryFromClassPath(platform, packageName, library);
        } catch (UnsatisfiedLinkError e) {
            System.err.println(e.getMessage());
            return false;
        }

        return true;
    }

    private static OperatingSystem getOS() {
        OsCheck.OSType osType = OsCheck.getOperatingSystemType();
        if (null == osType) {
            throw new UnsatisfiedLinkError("Cannot load library. Operating system not supported by native library loader: ");
        } else {
            switch (osType) {
                case Windows:
                    return OperatingSystem.WIN64;
                case MacOS:
                    return OperatingSystem.MACOSX64;
                case Linux:
                    return OperatingSystem.LINUX64;
                default:
                    throw new UnsatisfiedLinkError("Cannot load library. Operating system not supported by native library loader: ");
            }
        }
    }

    private static Architecture getArchitecture() {
        if (isARM_64()) {
            return Architecture.arm64;
        } else if (isX86_64()) {
            return Architecture.x64;
        } else {
            throw new UnsatisfiedLinkError("Cannot load library. Architecture not supported by native library loader: ");

        }
    }

    private synchronized static void loadLibraryFromClassPath(OperatingSystem os, String packageName, NativeLibraryWithDependencies library) {
        String identifier = packageName + "+" + library.getLibraryFilename();

        if (!loadedLibraries.contains(identifier)) {
            List<String> libraries = extractLibraryWithDependenciesAbsolute(packageName, library);

            // Load dependencies before loading the actual plugin
            for (int i = 1; i < libraries.size(); i++) {
                System.load(libraries.get(i));
            }

            System.load(libraries.get(0));
            loadedLibraries.add(identifier);
        }
    }

    private static String createPackagePrefix(String packageName) {
        packageName = packageName.trim().replace('.', '/');
        if (packageName.length() > 0) {
            packageName = packageName + '/';
        }
        return packageName;
    }

    private static String extractFile(File target, String name, InputStream inputStream) throws IOException {
        File targetFile = new File(target, name);
        if (!targetFile.exists()) {
            Files.copy(inputStream, targetFile.toPath());
        }

        return targetFile.getAbsolutePath();
    }

    private static void closeInputStreams(List<InputStream> inputStreams) {
        for (InputStream is : inputStreams) {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    private static List<InputStream> getInputstreams(String packageName, NativeLibraryWithDependencies nativeLibrary) {
        String prefix = createPackagePrefix(packageName);

        ArrayList<InputStream> inputStreams = new ArrayList<>();
        InputStream libraryInputStream = NativeLibraryLoader.class.getClassLoader().getResourceAsStream(prefix + nativeLibrary.getLibraryFilename());

        if (libraryInputStream == null) {
            throw new UnsatisfiedLinkError("Cannot load library " + prefix + nativeLibrary.getLibraryFilename());
        }

        inputStreams.add(libraryInputStream);

        for (String dependency : nativeLibrary.getDependencyFilenames()) {
            InputStream dependencyLibrary = NativeLibraryLoader.class.getClassLoader().getResourceAsStream(prefix + dependency);

            if (dependencyLibrary == null) {
                throw new UnsatisfiedLinkError("Cannot load library " + prefix + dependency);
            }

            inputStreams.add(dependencyLibrary);
        }

        return inputStreams;

    }

    private static String getHash(String packageName, NativeLibraryWithDependencies nativeLibrary) throws IOException, NoSuchAlgorithmException {
        List<InputStream> inputStreams = getInputstreams(packageName, nativeLibrary);

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

        for (InputStream is : inputStreams) {
            DigestInputStream digestStream = new DigestInputStream(is, messageDigest);
            byte[] buf = new byte[1024];

            while (digestStream.read(buf) > 0)
            ;
        }

        closeInputStreams(inputStreams);
        return printHexBinary(messageDigest.digest());

    }

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    public static String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    private static boolean isARM_64() {
        return false;//SystemUtils.OS_ARCH.equals("aarch64");
    }

    private static boolean isX86_64() {
        return true;
//        Processor processor = ArchUtils.getProcessor();
//        if (processor != null) {
//            return processor.isX86() && processor.is64Bit();
//        } else {
//            return false;
//        }
    }
}
