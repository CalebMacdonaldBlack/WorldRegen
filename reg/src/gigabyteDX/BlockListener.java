package gigabyteDX;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class BlockListener implements Listener {

	Main plugin;

	// create list that will hold the ores. These will take longer to
	// regenerate.
	public static List<String> ores = new ArrayList<String>();

	public BlockListener(Main plugin) {

		this.plugin = plugin;

		// begin repeating scheduler to start regeneration
		replaceBlocks();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockDestroy(BlockBreakEvent e) {

		// check whether a player is an op. if so, do not log block destroy.
		if (!e.getPlayer().isOp()) {

			// check to see if block being destroyed it within a region. If so,
			// do not log block destroy
			if (WorldGuardPlugin.inst().getRegionManager(e.getBlock().getWorld()).getApplicableRegions(e.getBlock().getLocation()).size() < 1 && useWorld(e.getBlock().getWorld().getName())) {

				// execute method to log block data
				createList(e.getBlock().getWorld(), e.getBlock().getType().toString(), e.getBlock().getData(), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {

		// check whether a player is an op. if so, do not log block place
		if (!e.getPlayer().isOp()) {

			// check to see if block being destroyed it within a region. If so,
			// do not log block place
		
			if (WorldGuardPlugin.inst().getRegionManager(e.getBlock().getWorld()).getApplicableRegions(e.getBlock().getLocation()).size() < 1 &&  useWorld(e.getBlock().getWorld().getName())) {

				// execute method to log block data
				createList(e.getBlock().getWorld(), "AIR", e.getBlock().getData(), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ());
			}
		}
	}

	private void createList(World world, String material, Byte blockData, int x, int y, int z) {

		// check to see if block is authorized to regenerate. if so, log the
		// block data
		if (isAllowed(material))
			Main.blockList.add(new BlockData(world.getName(), material, blockData, System.currentTimeMillis(), x, y, z));
	}

	private void replaceBlocks() {

		// begin repeating task to check whether a block is due to respawn.
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			int X = 0;
			@Override
			public void run() {

				// loop through all the logged blocks until an
				// IndexOutOfBoundsException occurs which indicates the
				// end of
				// the list. break the loop when this happens and
				// restart on
				// next scheduled repeat
					try {
						
						// check to see whether the the current block
						// data is an
						// ore or not. If so, the block will be handled
						// as a
						// mining block and will not regenerate for a
						// month (by
						// default). It will check to see if that time
						// has
						// passed. If not, it will be handled like every
						// other
						// block.
						if (!isOre(X)) {
							// check to see whether the block is below a
							// certain
							// height. If so, the block will be handled
							// as a
							// mining block and will not regenerate for
							// a month
							// (by default). if not, the block will
							// regenerate
							// much quicker (a day by default).

							// if block is on the surface.
							if (Main.blockList.get(X).getY() > getMaxMiningHeight(Main.blockList.get(X).getWorld()) && new Date(System.currentTimeMillis()).after(new Date(getSurfaceResetTime(Main.blockList.get(X).getWorld()) + Main.blockList.get(X).getDate()))) {

								// execute method to reset the block
								setBlock(X);

								// if block is below the surface
								       
							} else if (new Date(System.currentTimeMillis()).after(new Date((getMineResetTime(Main.blockList.get(X).getWorld()) * 1000 * 60 * 60)  + Main.blockList.get(X).getDate()))) {
							
								setBlock(X);
							}

							// block was an ore and will be handled as a
							// mining
							// block.
						} else {

							// check to see if block is due for
							// regeneration
							if (new Date(System.currentTimeMillis()).after(new Date(getMineResetTime(Main.blockList.get(X).getWorld()) + Main.blockList.get(X).getDate())))
								setBlock(X);

						}
					} catch (IndexOutOfBoundsException e) {
						X = -1;
					}
				
					X++;
			}

			private boolean isOre(int X) {

				// loop through each material in the "ores" list
				if(Main.conf.getList("MiningBlocks").contains(Main.blockList.get(X).getMaterial()))
					return true;
				/*
				for (String s : BlockListener.ores) {

					// check to see if the material matches any of the
					// materials
					// in the list. If so, the material is an ore.
					// return true.
					if (Main.blockList.get(X).getMaterial().toString().equals(s));
					System.out.println(BlockListener.ores.get(X));
				} */
				return false;
			}

			@SuppressWarnings("deprecation")
			private void setBlock(int X) {

				// check to see if the block being reset is in a region.
				// if so, do not reset.
				if (WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorld(Main.blockList.get(X).getWorld())).getApplicableRegions(new Location(Bukkit.getWorld(Main.blockList.get(X).getWorld()), Main.blockList.get(X).getX(), Main.blockList.get(X).getY(), Main.blockList.get(X).getZ())).size() < 1) {

					// reset block at the logged location
					Bukkit.getWorld(Main.blockList.get(X).getWorld()).getBlockAt(new Location(Bukkit.getWorld(Main.blockList.get(X).getWorld()), Main.blockList.get(X).getX(), Main.blockList.get(X).getY(), Main.blockList.get(X).getZ())).setType(Material.getMaterial(Main.blockList.get(X).getMaterial()));

					// apply any block data. Type of leaves or logs for
					// example.
					Bukkit.getWorld(Main.blockList.get(X).getWorld()).getBlockAt(new Location(Bukkit.getWorld(Main.blockList.get(X).getWorld()), Main.blockList.get(X).getX(), Main.blockList.get(X).getY(), Main.blockList.get(X).getZ())).setData(Main.blockList.get(X).getData());

				}
				// Execute method to remove logged data from list.
				Main.blockList.remove(X);

			}
		}, Main.conf.getInt("RegenerationTimeInSeconds") * 5, Main.conf.getInt("RegenerationTimeInSeconds") * 5);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void leafDecay(LeavesDecayEvent e) {

		// check to see whether the leaf decay occurred in a region. if so, do
		// not log. If not, log block to be reset after a period of time
		if (WorldGuardPlugin.inst().getRegionManager(e.getBlock().getWorld()).getApplicableRegions(e.getBlock().getLocation()).size() < 1 && useWorld(e.getBlock().getWorld().getName())) {

			// log block
			createList(e.getBlock().getWorld(), e.getBlock().getType().toString(), e.getBlock().getData(), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ());
		}
	}

	public boolean isAllowed(String string) {

		// check to see whether the block is authorized to regenerate
		if (Main.conf.getList("RegenBlocks").contains(string)) {
			return true;
		}
		return false;

	}

	/*---------------set variables---------------*/

	public boolean useWorld(String world) {
		if (Main.conf.getConfigurationSection(world).get(Main.getRegenerateWorld()).equals(true))
			return true;
		return false;
	}

	public int getMaxMiningHeight(String world) {
		return (int) Main.conf.getConfigurationSection(world).get(Main.getMineHeight());
	}

	public int getSurfaceResetTime(String world) {
		return (int) Main.conf.getConfigurationSection(world).get(Main.getSurfaceResetTime()) * 1000 * 60 * 60;
	}

	public long getMineResetTime(String world) {
		Integer i = (int) Main.conf.getConfigurationSection(world).get(Main.getMineResetTime());
		long l = i.longValue();
		l = l * 1000 * 60 * 60;
		return l ;
	}

}
