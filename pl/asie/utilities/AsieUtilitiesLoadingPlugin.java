package pl.asie.utilities;

import java.util.Map;

import pl.asie.utilities.interop.InteropClassTransformer;
import pl.asie.utilities.skin.SkinClassTransformer;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class AsieUtilitiesLoadingPlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{SkinClassTransformer.class.getName(),
				InteropClassTransformer.class.getName()};
	}
	
	@Override
	public String[] getLibraryRequestClass() { return null; }
	
	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}
}