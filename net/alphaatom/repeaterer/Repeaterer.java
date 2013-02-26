package net.alphaatom.repeaterer;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Repeaterer extends JavaPlugin implements Listener {
	
	public HashMap<String, Integer> playerPreferences = new HashMap<String, Integer>();
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockPlaceListener(BlockPlaceEvent event) {
		if (!event.isCancelled()) {
			Block blockPlaced = event.getBlockPlaced();
			if (blockPlaced.getType().equals(Material.DIODE_BLOCK_OFF)) {
				if (playerPreferences.containsKey(event.getPlayer().getName())) {
					int delay = playerPreferences.get(event.getPlayer().getName());
					getServer().getScheduler().runTaskLater(this, new ChangeBlockTask(blockPlaced,delay), 1);
				}
			}
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rpticks")) {
			if (args.length >= 1) {
				if (sender instanceof Player) {
					String playerName = ((Player) sender).getName();
					if (sender.hasPermission("repeaterer.rpticks")) {
						if (args[0].equalsIgnoreCase("reset")) {
							if (playerPreferences.containsKey(playerName)) {
								playerPreferences.remove(playerName);
								sender.sendMessage("[§4Repeaterer§f] Successfully reset your repeater delay preferences.");
								return true;
							} else {
								sender.sendMessage("[§4Repeaterer§f] You have not yet set any preferences.");
								return true;
							}
						} else {
							int ticks = 0;
							try {
								ticks = Integer.parseInt(args[0]);
							} catch (NumberFormatException ex) {
								return false;
							}
							if (ticks <= 4 && ticks > 0) {
								playerPreferences.put(playerName, ticks);
								sender.sendMessage("[§4Repeaterer§f] Successfully set your tick preference to: §4" + ticks + "§f.");
								return true;
							}
						}
					} else {
						sender.sendMessage("[§4Repeaterer§f] You do not have permission to run this command.");
						return true;
					}
				} else {
					sender.sendMessage("[§4Repeaterer§f] This command may only be run by players.");
					return true;
				}
			} else {
				sender.sendMessage("[§4Repeaterer§f] To set the default number of ticks/delay for a repeater.");
				sender.sendMessage("[§4Repeaterer§f] Usage: /rpticks <1,2,3,4,reset>");
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (playerPreferences.containsKey(player.getName())) {
						sender.sendMessage("[§4Repeaterer§f] Your current setting is: " + playerPreferences.get(player.getName()));
					}
				}
				return true;
			}
		}
		return false;
	}

}
