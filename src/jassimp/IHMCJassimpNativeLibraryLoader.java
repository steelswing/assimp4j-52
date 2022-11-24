
package jassimp;

import us.ihmc.tools.nativelibraries.NativeLibraryLoader;

public class IHMCJassimpNativeLibraryLoader extends JassimpLibraryLoader {

    @Override
    public void loadLibrary() {
        NativeLibraryLoader.loadLibrary("jassimp", "libassimp-5");
        NativeLibraryLoader.loadLibrary("jassimp", "libjassimp");
    }
}
