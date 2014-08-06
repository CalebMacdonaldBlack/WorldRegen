package gigabyteDX;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static final String fileName = "regenData";
	public static String pluginName;
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Main plugin;

	public static List<BlockData> blockList = new ArrayList<BlockData>();
	public static Configuration conf;
	
	public static String RegenerateWorld = "RegenerateWorld";
	public static String MineHeight = "MineHeight";
	public static String SurfaceResetTime = "SurfaceResetTime";
	public static String MineResetTime = "MineResetTime";
	
	

	public static String getRegenerateWorld() {
	    return RegenerateWorld;
	}

	public static String getMineHeight() {
	    return MineHeight;
	}

	public static String getSurfaceResetTime() {
	    return SurfaceResetTime;
	}

	public static String getMineResetTime() {
	    return MineResetTime;
	}

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disabled!");
		try {
			InitConfigs.save(fileName);
		} catch (FileNotFoundException e) { 
			e.printStackTrace();
		}
		//setDefault();
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		Main.pluginName = pdfFile.getName();
		System.out.println(pdfFile.getName());
		new InitConfigs(this);
		
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BlockListener(this), this);
		
		//addDefault();
	}
	


}
