package me.amc.meltitems;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class RecipeMaker {

     private Material source;
     private ItemStack result;
     private FurnaceRecipe recipe;
     private String model;
     private NamespacedKey key;
     private int exp;
     private int time;

     public RecipeMaker(String recipeLine) {
          String[] parts = recipeLine.split(",");
          this.source = Material.getMaterial(parts[0]);
          Material resultMaterial = Material.getMaterial(parts[1]);

          if(this.source.getMaxDurability() > 0) { // Implement RecipeModel on items with durability
               String modelName = parts[2]+"_"+parts[1].toLowerCase();
               if(MainCore.instance.recipeModels.containsKey(modelName)) {
                    this.result = MainCore.instance.recipeModels.get(modelName).getResult(100);
               } else { // if model does not exist then create it and then get the result
                    RecipeModel rm = new RecipeModel(modelName, MainCore.instance.configHelper.getModel(parts[2]), resultMaterial);
                    MainCore.instance.recipeModels.put(modelName, rm);
                    this.result = rm.getResult(100);
               }
               this.model = modelName;
          } else {
               this.result = new ItemStack(resultMaterial, Integer.parseInt(parts[2]));
          }
          this.exp = Integer.parseInt(parts[3]);
          this.time = Integer.parseInt(parts[4]);

          NamespacedKey key = MainCore.instance.getKeyForRecipe(parts[0].toLowerCase());
          this.key = key;

          this.recipe = new FurnaceRecipe(key, this.result, this.source, this.exp, this.time*20);
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

     public RecipeModel getRecipeModel() {
          if(MainCore.instance.recipeModels.containsKey(model))
               return MainCore.instance.recipeModels.get(model);
          return null;
     }

     public NamespacedKey getKey() {
          return this.key;
     }

     public int getExp() {
          return this.exp;
     }

     public int getTime() {
          return this.time;
     }

}
