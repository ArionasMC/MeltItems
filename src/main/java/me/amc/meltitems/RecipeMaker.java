package me.amc.meltitems;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class RecipeMaker {

     private Material source;
     private ItemStack result;
     private FurnaceRecipe recipe;

     public RecipeMaker(Material source, ItemStack result) {
          this.source = source;
          this.result = result;

          NamespacedKey key = MeltItems.instance.getKeyForRecipe(source.toString().toLowerCase());
          this.recipe = new FurnaceRecipe(key, this.result, this.source, 0, 1*20);
     }

     public RecipeMaker(String recipeLine) {
          String[] parts = recipeLine.split(",");
          this.source = Material.getMaterial(parts[0]);
          this.result = new ItemStack(Material.getMaterial(parts[1]), Integer.parseInt(parts[2]));

          NamespacedKey key = MeltItems.instance.getKeyForRecipe(parts[0].toLowerCase());
          this.recipe = new FurnaceRecipe(key, this.result, this.source, 0, 1*20);
     }

     public Material getSource() {
          return this.source;
     }

     public ItemStack getResult() {
          return this.result;
     }

     public FurnaceRecipe getRecipe() {
          return this.recipe;
     }

}
