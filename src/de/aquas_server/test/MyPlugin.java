package de.aquas_server.test;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {
	public void onEnable(){
		this.getLogger().log(Level.INFO, "Tada!");
		Bukkit.getServer().getPluginManager().registerEvents(new MyEvents(), this);
	}
	
	public void onDisable(){
		this.getLogger().log(Level.INFO, "Bye!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("jump")){
			if(!(sender instanceof Player)){
				sender.sendMessage("Befehl nur für Spieler zulässig");
				return false;
			}
			Player player = (Player) sender;
			if(args.length != 1){
				sender.sendMessage("Syntaxfehler: /jump <location>");
				return false;
			}
			if(MyEvents.jump.get(player.getUniqueId()) == null){
				sender.sendMessage("unbekannter Ort " + args[0]);
				return false;
			}
			Location loc = MyEvents.jump.get(player.getUniqueId()).get(args[0]);
			if(loc == null){
				sender.sendMessage("unbekannter Ort " + args[0]);
				return false;
			}
			player.teleport(loc);
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("setjump")){
			if(!(sender instanceof Player)){
				sender.sendMessage("Befehl nur für Spieler zulässig");
				return false;
			}
			Player player = (Player) sender;
			if(args.length != 1){
				sender.sendMessage("Syntaxfehler: /setjump <location>");
				return false;
			}
			if(MyEvents.jump.get(player.getUniqueId()) == null)
				MyEvents.jump.put(player.getUniqueId(), new HashMap<String, Location>());
			MyEvents.jump.get(player.getUniqueId()).put(args[0], player.getLocation());
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("kistenfuellen")){
			if(args.length != 7){
				sender.sendMessage("Syntaxfehler: /kistenfuellen wname x1 y1 z1 x2 y2 z2");
				return false;
			}
			try{
				World w = Bukkit.getWorld(args[0]);
				int x1 = Integer.parseInt(args[1]);
				int y1 = Integer.parseInt(args[2]);
				int z1 = Integer.parseInt(args[3]);
				int x2 = Integer.parseInt(args[4]);
				int y2 = Integer.parseInt(args[5]);
				int z2 = Integer.parseInt(args[6]);
				int x, y, z;
				ChestItems items = new ChestItems("test.txt");
				for(x = x1; x <= x2; x++){
					for(y = y1; y <= y2; y++){
						for(z = z1; z <= z2; z++){
							Block b = w.getBlockAt(x, y, z);
							if(b.getType() == Material.CHEST)
								items.fill((InventoryHolder) b.getState());
						}
					}
				}
			}catch(Exception e){
				sender.sendMessage(e.getClass().getName() + ":" + e.getMessage());
				return false;
			}
			return true;
		}
		return false;
	}
}