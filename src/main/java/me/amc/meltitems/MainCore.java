package me.amc.meltitems;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
     private List<NamespacedKey> keys;

     private boolean newerVersion;

     @Override
     public void onEnable() {
          newerVersion = isNewerVersion();

          instance = this;
          saveDefaultConfig();
          reloadConfig();

          recipeModels = new HashMap<>();
          recipeMakers = new ArrayList<>();
          furnaceRecipes = new ArrayList<>();
          keys = new ArrayList<>();
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
         printNumberOfRecipes(false);
          if(!furnaceRecipes.isEmpty()) {

               if(newerVersion) { // Use Bukkit.removeRecipe using Java Reflection
                    getLogger().info("Trying newer reload!");
                    try {
                         Method m = Bukkit.class.getDeclaredMethod("removeRecipe", NamespacedKey.class);
                         for (NamespacedKey key : keys) {
                              m.invoke(null, key);
                         }
                    } catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
                         exception.printStackTrace();
                    }
               } else {
                    Iterator<Recipe> it = Bukkit.recipeIterator();
                    while (it.hasNext()) {
                         Recipe r = it.next();
                         if (!(r instanceof FurnaceRecipe)) continue;
                         FurnaceRecipe fr = (FurnaceRecipe) r;
                         //if(furnaceRecipes.contains(fr)) {
                         //for (FurnaceRecipe cfr : furnaceRecipes) {
                         for (RecipeMaker rm : recipeMakers) {
                              if (!areSameFurnaceRecipe(rm.getRecipe(), fr)) continue;
                              getLogger().info("Found one! key=" + rm.getKey());
                              //getLogger().info("Found one of my own!" + fr.getInput().toString() + " " + fr.getResult().toString());
                              it.remove();
                         }
                    }
               }

               furnaceRecipes.clear();
               recipeMakers.clear();
               recipeModels.clear();
               keys.clear();
          }
          printNumberOfRecipes(false);
          // Add all the custom recipes into the game
          for(String s : configHelper.recipes) {
               RecipeMaker rm = new RecipeMaker(s);
               recipeMakers.add(rm);
               furnaceRecipes.add(rm.getRecipe());
               Bukkit.addRecipe(rm.getRecipe());
          }
          printNumberOfRecipes(false);
     }

     private void printNumberOfRecipes(boolean show) {
          Iterator<Recipe> it = Bukkit.recipeIterator();
          int i = 0;
          while(it.hasNext()) {
               Recipe r = it.next();
               if(r instanceof FurnaceRecipe) {
                    i++;
                    if(show)
                         getLogger().info("recipe:"+((FurnaceRecipe) r).getInput().getType()+"=>"
                                                  +r.getResult().getType());
               }
          }
          getLogger().info("Number of furnace recipes="+i);
     }


     public NamespacedKey getKeyForRecipe(String name) {
          NamespacedKey key = new NamespacedKey(this, getDescription().getName().toLowerCase()+"_"+name);
          keys.add(key);
          return key;
     }

     public boolean areSameFurnaceRecipe(FurnaceRecipe fr1, FurnaceRecipe fr2) {
          return fr1.getInput().getType() == fr2.getInput().getType()
                  && fr1.getResult().getType() == fr2.getResult().getType();
     }

     private boolean isNewerVersion() {
          String a = getServer().getClass().getPackage().getName();
          String version = a.substring(a.lastIndexOf('.') + 1);
          getLogger().info("Version:  " + version);
          int v = Integer.parseInt(version.split("_")[1]);
          return v >= 16;
     }
}
