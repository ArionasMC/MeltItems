package me.amc.meltitems;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public class RecipeModel {

     private String name;
     private List<String> list;
     private LinkedHashMap<Integer, ItemStack> results;

     public RecipeModel(String name, List<String> list) {
          this.name = name;
          this.list = list;
          Collections.sort(this.list, new LineComparator());

          results = new LinkedHashMap<>();
          for(String line : this.list) {
               String[] parts = line.split(":")[1].split(",");
               Material mat = Material.getMaterial(parts[0]);
               int quantity = Integer.parseInt(parts[1]);
               int key = Integer.parseInt(line.split(":")[0]);
               results.put(key, new ItemStack(mat, quantity));
          }
     }

     public ItemStack getResult(int percentage) {
          int i = 0;
          for(int key : results.keySet()) {
               if (percentage >= key)
                    return results.get(key);
               i = key;
          }
          return results.get(i);
     }

}

class LineComparator implements Comparator<String> {
     @Override
     public int compare(String line1, String line2) {
          int per1 = Integer.parseInt(line1.split(":")[0]);
          int per2 = Integer.parseInt(line2.split(":")[0]);
          return per2 - per1;
     }
}

