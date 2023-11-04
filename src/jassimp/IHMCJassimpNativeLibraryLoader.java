
package jassimp;

public class IHMCJassimpNativeLibraryLoader extends JassimpLibraryLoader {

    @Override
    public void loadLibrary() {
        NativeLoader.init();
    }
}
