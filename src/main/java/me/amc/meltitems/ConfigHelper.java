package me.amc.meltitems;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigHelper {

     private FileConfiguration config;

     public List<String> recipes;

     public ConfigHelper(FileConfiguration config) {
          this.config = config;
          recipes = config.getStringList("recipes");
     }

     public List<String> getModel(String name) {
          return config.getStringList("models."+name.toLowerCase());
     }

}
