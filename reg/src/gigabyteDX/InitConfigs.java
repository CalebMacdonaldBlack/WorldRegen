package gigabyteDX;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class InitConfigs {

    Main plugin;

    public InitConfigs(Main plugin) {

	// initiate start up tasks
	this.plugin = plugin;
	plugin.saveDefaultConfig();
	setWorldsInConf();
	plugin.saveConfig();
	Main.conf = plugin.getConfig();
	checkData();
	saveRareList();
    }

    @SuppressWarnings("unchecked")
    private void saveRareList() {

	// save list from config file to a list variable
	BlockListener.ores = (List<String>) Main.conf.getList("MiningBlocks");

    }

    private void checkData() {

	// check to see if a file exists. If not, create a new one.
	try {
	    read(Main.fileName);
	} catch (FileNotFoundException e) {
	    try {
		save(Main.fileName);
	    } catch (FileNotFoundException e1) {
		e1.printStackTrace();
	    }
	}

    }

    public static void save(String fileName) throws FileNotFoundException {

	// Save data. I'm not going to explain what is happening here because I
	// hardly know myself.it saves an object to a file.
	FileOutputStream fout = new FileOutputStream("plugins/" + Main.pluginName + "/" + fileName);
	ObjectOutputStream oos;

	try {
	    oos = new ObjectOutputStream(fout);
	    try {
		oos.writeObject(Main.blockList);

		try {
		    fout.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	} catch (IOException e1) {
	    e1.printStackTrace();
	}

    }

    @SuppressWarnings("unchecked")
    public void read(String fileName) throws FileNotFoundException {

	// read data. I'm not going to explain what is happening here because I
	// hardly know myself.it assigns an object from a file.
	FileInputStream fin = new FileInputStream("plugins/" + Main.pluginName + "/" + fileName);
	ObjectInputStream ois;
	try {
	    ois = new ObjectInputStream(fin);
	    try {
		Main.blockList = (List<BlockData>) ois.readObject();
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	try {
	    fin.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void setWorldsInConf() {

	// writes world defaults to the config file
	for (World x : Bukkit.getWorlds()) {
	    switch (x.getName()) {
	    case ("world"):
		writeConfigWorldSettings(x, true, 35, 24, 720);
		break;
	    case ("world_nether"):
		writeConfigWorldSettings(x, true, 0, 24, 0);
		break;
	    case ("world_the_end"):
		writeConfigWorldSettings(x, true, 0, 24, 0);
		break;
	    default:
		writeConfigWorldSettings(x, false, 0, 0, 0);
		break; 
	    }
	}
	plugin.saveConfig();
    }

    private void writeConfigWorldSettings(World x, boolean useWorld, int getMaxMiningHeight, int getSurfaceResetTime, int getMineResetTime) {

	// I forget what this does. I think it's important.
	if (!plugin.getConfig().isConfigurationSection(x.toString())) {
	    plugin.getConfig().createSection(x.getName());
	    plugin.getConfig().getConfigurationSection(x.getName()).set("RegenerateWorld", useWorld);
	    plugin.getConfig().getConfigurationSection(x.getName()).set("MineHeight", getMaxMiningHeight);
	    plugin.getConfig().getConfigurationSection(x.getName()).set("SurfaceResetTime", getSurfaceResetTime);
	    plugin.getConfig().getConfigurationSection(x.getName()).set("MineResetTime", getMineResetTime);
	}

    }
}
