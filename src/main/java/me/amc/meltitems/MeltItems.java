package me.amc.meltitems;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MeltItems extends JavaPlugin {

     public static MeltItems instance; // Singleton

     public ConfigHelper configHelper;

     private List<FurnaceRecipe> furnaceRecipes;

     @Override
     public void onEnable() {
          instance = this;
          saveDefaultConfig();
          reloadConfig();

          furnaceRecipes = new ArrayList<>();
          initFurnaceRecipes();

          if(isEnabled()) {
               getLogger().info(getDescription().getName()+" "+getDescription().getVersion()+" is enabled!");
          }
     }

     @Override
     public void onDisable() {
          getLogger().info(getDescription().getName()+" "+getDescription().getVersion()+" is disabled!");
     }

     @Override
     public void reloadConfig() {
          super.reloadConfig();
          configHelper = new ConfigHelper(getConfig());
     }

     private void initFurnaceRecipes() {
          // Remove recipes on reload to initialize again
          if(!furnaceRecipes.isEmpty()) {
               Iterator<Recipe> it = getServer().recipeIterator();
               while(it.hasNext()) {
                    Recipe r = it.next();
                    if(!(r instanceof FurnaceRecipe)) continue;
                    FurnaceRecipe fr = (FurnaceRecipe) r;
                    if(furnaceRecipes.contains(fr)) {
                         getLogger().info("Found one of my own!"+fr.getInput().toString()+" "+fr.getResult().toString());
                         it.remove();
                    }
               }
               furnaceRecipes.clear();
          }
          // Add all the custom recipes into the game
          for(String s : configHelper.recipes) {
               RecipeMaker rm = new RecipeMaker(s);
               furnaceRecipes.add(rm.getRecipe());
               getServer().addRecipe(rm.getRecipe());
          }
     }

     public NamespacedKey getKeyForRecipe(String name) {
          return new NamespacedKey(this, getDescription().getName().toLowerCase()+"_"+name);
     }
}
