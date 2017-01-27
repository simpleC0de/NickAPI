package NickAPI.Main;

import org.bukkit.plugin.java.JavaPlugin;

public class NickAPI extends JavaPlugin{

	private static NickAPI instance;
	
	public void onEnable(){
		instance = this;
		new APIHandler();
	}
	
	public static NickAPI getInstance(){
		return instance;
	}
	
	
}
