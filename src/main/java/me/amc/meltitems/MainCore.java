package me.amc.meltitems;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainCore extends JavaPlugin {

     public static MainCore instance; // Singleton

     public ConfigHelper configHelper;
     public List<RecipeMaker> recipeMakers;
     public HashMap<String, RecipeModel> recipeModels;

     private List<FurnaceRecipe> furnaceRecipes;

     @Override
     public void onEnable() {
          instance = this;
          saveDefaultConfig();
          reloadConfig();

          recipeModels = new HashMap<>();
          recipeMakers = new ArrayList<>();
          furnaceRecipes = new ArrayList<>();
          initFurnaceRecipes();

          new MeltEvents();

          getCommand("meltitems").setExecutor(new MeltItemsCommand());

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

     public void initFurnaceRecipes() {
          // Remove recipes on reload to initialize again
          if(!furnaceRecipes.isEmpty()) {
               Iterator<Recipe> it = getServer().recipeIterator();
               while(it.hasNext()) {
                    Recipe r = it.next();
                    if (!(r instanceof FurnaceRecipe)) continue;
                    FurnaceRecipe fr = (FurnaceRecipe) r;
                    //if(furnaceRecipes.contains(fr)) {
                    for (FurnaceRecipe cfr : furnaceRecipes) {
                         if (!areSameFurnaceRecipe(cfr, fr)) continue;
                         //getLogger().info("Found one of my own!" + fr.getInput().toString() + " " + fr.getResult().toString());
                         it.remove();
                    }
               }
               furnaceRecipes.clear();
               recipeMakers.clear();
               recipeModels.clear();
          }
          // Add all the custom recipes into the game
          for(String s : configHelper.recipes) {
               RecipeMaker rm = new RecipeMaker(s);
               recipeMakers.add(rm);
               furnaceRecipes.add(rm.getRecipe());
               getServer().addRecipe(rm.getRecipe());
          }
     }

     public NamespacedKey getKeyForRecipe(String name) {
          return new NamespacedKey(this, getDescription().getName().toLowerCase()+"_"+name);
     }

     public boolean areSameFurnaceRecipe(FurnaceRecipe fr1, FurnaceRecipe fr2) {
          return fr1.getInput().getType() == fr2.getInput().getType()
                  && fr1.getResult().getType() == fr2.getResult().getType();
     }

}
