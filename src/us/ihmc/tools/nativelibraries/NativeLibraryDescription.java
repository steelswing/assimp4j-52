package us.ihmc.tools.nativelibraries;

public interface NativeLibraryDescription
{
   public enum OperatingSystem
   {
      WIN64, MACOSX64, LINUX64
   }
   
   public enum Architecture
   {
      /**
       * x64 instruction set, also known as x86_64 or AMD64 
       * 
       * This is all desktop computers
       */
      x64,
      /**
       * Arm 64 bit instruction set, also known as aarch64
       */
      arm64
   }

   /**
    * Get the package the native libraries are stored in.
    * 
    * @return
    */
   public String getPackage(OperatingSystem operatingSystem, Architecture arch);

   /**
    * Get a library name and its dependency to load for current platform. 
    * 
    * On Windows, the dependencies are loaded in order. On other systems, you need to make sure to set the RPATH to $ORIGIN (Unix) or @rpath (MacOSX) to load the dependencies.
    * 
    * @param operatingSystem Get libraries for the selected operating system
    * @param arch Get libraries for given architecture
    * @return List The libraries to load in order, with their dependencies
    */
   NativeLibraryWithDependencies getLibraryWithDependencies(OperatingSystem operatingSystem, Architecture arch);
   
   
}
