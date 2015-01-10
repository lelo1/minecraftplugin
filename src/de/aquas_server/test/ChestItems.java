package de.aquas_server.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ChestItems {
	Map<Material,Double> prob;
	Map<Material,Integer> count;
	Random rnd;
	double tot;
	public ChestItems(String filename) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(filename));
		rnd = new Random();
		prob = new HashMap<Material,Double>();
		count = new HashMap<Material,Integer>();
		for(String s: lines){
			String[] a = s.split(" ");
			Material m;
			if(a.length != 3)
				throw new IOException("ungültiges Format");
			m = Material.matchMaterial(a[0]);
			if(m == null)
				throw new IOException("unbekanntes Material "+a[0]);
			double d = Double.parseDouble(a[1]);
			tot += d;
			prob.put(m, d);
			count.put(m, Integer.parseInt(a[2]));
		}
	}
	int poisson(double lambda)
	{
		int x = 0;
		double p = Math.exp(-lambda);
		double s = p;
		double u = rnd.nextDouble();
		while(u > s){
			x++;
			p = p*lambda/x;
			s = s + p;
		}
		return x;
	}
	public void fill(InventoryHolder c)
	{
		double x;
		Inventory iv = c.getInventory();
		int n = poisson(4);
		while(n-- != 0){
			x = rnd.nextDouble() * tot;
			for(Map.Entry<Material, Double> e : prob.entrySet()){
				x -= e.getValue();
				if(x < 0){
					iv.addItem(new ItemStack(e.getKey(), poisson(count.get(e.getKey()))));
					break;
				}
			}
		}
	}
}
