package de.aquas_server.test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MyEvents implements Listener{
	static Map<UUID,Location> loc, loc2;
	static Map<UUID,Integer> state;
	public static Map<UUID, Map<String, Location>> jump;
	static {
		loc = new HashMap<UUID,Location>();
		loc2 = new HashMap<UUID,Location>();
		state = new HashMap<UUID,Integer>();
		jump = new HashMap<UUID,Map<String,Location>>();
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		UUID p;
		Location l;
		
		p = event.getPlayer().getUniqueId();
		l = event.getClickedBlock().getLocation();
		if(event.hasItem() && event.getItem().getType() == Material.STICK){
			if(state.get(p) == null)
					state.put(p, 0);
			switch(state.get(p).intValue()){
			case 0:
				loc.put(p, l);
				state.put(p, 1);
				break;
			case 1:
				loc2.put(p, l);
				state.put(p, 2);
				break;
			case 2:
				Bukkit.getServer().dispatchCommand(event.getPlayer(), "clone " +
						loc.get(p).getX() + " " + loc.get(p).getY() + " " + loc.get(p).getZ() + " " +
						loc2.get(p).getX() + " " + loc2.get(p).getY() + " " + loc2.get(p).getZ() + " " +
						l.getX() + " " + l.getY() + " " + l.getZ());
				break;
			}
		}
		if(event.hasItem() && event.getItem().getType() == Material.BLAZE_ROD)
			state.put(p, 0);
	}
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		if(event.getPlayer().getItemInHand().getType() == Material.BONE && event.getRightClicked().getType().isAlive()){
				event.getPlayer().getWorld().spawn(event.getRightClicked().getLocation(), Wolf.class);
				event.getRightClicked().remove();
				
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		if(event.getPlayer().getItemInHand().getType() == Material.LEATHER_BOOTS && event.getPlayer().getItemInHand().getEnchantmentLevel(Enchantment.DEPTH_STRIDER) >= 0 && event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.STATIONARY_WATER)
			event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.STONE);
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		UUID p;
		
		p = event.getEntity().getUniqueId();
		if(jump.get(p) == null)
			jump.put(p, new HashMap<String, Location>());
		jump.get(p).put("back", event.getEntity().getLocation());
	}
}
