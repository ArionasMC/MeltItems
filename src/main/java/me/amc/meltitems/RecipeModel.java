package me.amc.meltitems;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RecipeModel {

     private String name;
     private Material placeholder;
     private List<String> list;
     private LinkedHashMap<Integer, ItemStack> results;

     public RecipeModel(String name, List<String> list, Material placeholder) {
          this.name = name;
          this.list = list;
          this.placeholder = placeholder;
          Collections.sort(this.list, new LineComparator());

          results = new LinkedHashMap<>();
          for(String line : this.list) {
               String[] parts = line.split(":")[1].split(",");
               Material mat = placeholder;
               if(!parts[0].equals("%material%"))
                    mat = Material.getMaterial(parts[0]);
               int quantity = Integer.parseInt(parts[1]);
               int key = Integer.parseInt(line.split(":")[0]);
               results.put(key, new ItemStack(mat, quantity));
          }
     }

     public ItemStack getResult(int percentage) {
          Iterator<Integer> iter = results.keySet().iterator();
          int previous = iter.next();
          for(int key : results.keySet()) {
               if(percentage == key)
                    return results.get(key);
               if (percentage > key)
                    return results.get(previous);
               previous = key;
          }
          return results.get(previous);
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
