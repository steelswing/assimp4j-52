package us.ihmc.tools.nativelibraries;

import us.ihmc.tools.nativelibraries.NativeLibraryDescription.Architecture;
import us.ihmc.tools.nativelibraries.NativeLibraryDescription.OperatingSystem;

public class NativeLibraryWithDependencies
{
   private final String libraryFilename;
   private final String dependencyFilenames[];

   private NativeLibraryWithDependencies(String libraryFilename, String... dependencyFilenames)
   {
      this.libraryFilename = libraryFilename;
      this.dependencyFilenames = dependencyFilenames;
   }

   public String getLibraryFilename()
   {
      return libraryFilename;
   }

   public String[] getDependencyFilenames()
   {
      return dependencyFilenames;
   }

   /**
    * Get a platform specific name for a given library name. 
    * 
    * If the architecture is x64, no architecture string is appended. 
    * 
    * This does not use system.mapLibraryName to allow more control, at the cost of manually having to add new operating systems should they arrise.
    * 
    * Example:
    * Library name: example
    * 
    * Windows: example[-arch].dll
    * Linux: libexample[-arch].so
    * Mac: libexample[-arch].dylib
    * 
    * @param operatingSystem The OS to load the library for
    * @param arch The processor architecture to load the library for
    * @param libraryName Name of the library
    * 
    * @return Library named mapped following the example
    */
   public static String getPlatformName(OperatingSystem operatingSystem, Architecture arch, String libraryName)
   {
      String platformIdentifier;
      switch(arch)
      {
         case arm64:
            platformIdentifier = "-arm64";
            break;
         case x64:
            platformIdentifier = "";
            break;
            
         default:
            throw new RuntimeException("Unsupported architecture: " + arch);
      }
      
      
      switch(operatingSystem)
      {
         case LINUX64:
            return "lib" + libraryName + platformIdentifier + ".so";
         case WIN64:
            return libraryName + platformIdentifier + ".dll";
         case MACOSX64:
            return "lib" + libraryName + platformIdentifier + ".dylib";
         default:
            throw new RuntimeException("Unsupported operating system: " + operatingSystem);
      }
      
   }

   /**
    * Create a library description with the library names based on the current platform
    * 
    * @param operatingSystem
    * @param arch
    * @param libraryName
    * 
    * @return A description for the library to load
    */
   public static NativeLibraryWithDependencies fromPlatform(OperatingSystem operatingSystem, Architecture arch, String libraryName, String... dependencies)
   {
      String[] platformDependencies;
      if(dependencies != null)
      {
         platformDependencies = new String[dependencies.length];
   
         for (int i = 0; i < dependencies.length; i++)
         {
            platformDependencies[i] = getPlatformName(operatingSystem, arch, dependencies[i]);
         }
      }
      else
      {
         platformDependencies = null;
      }

      return new NativeLibraryWithDependencies(getPlatformName(operatingSystem, arch, libraryName), platformDependencies);
   }

   /**
    * Create a library description with the raw library names
    * 
    * @param libraryFile
    * @param dependencyFiles
    * 
    * @return
    */
   public static NativeLibraryWithDependencies fromFilename(String libraryFile, String... dependencyFiles)
   {
      return new NativeLibraryWithDependencies(libraryFile, dependencyFiles);
   }

}
