package me.amc.meltitems;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigHelper {

     private FileConfiguration config;

     public List<String> recipes;

     public ConfigHelper(FileConfiguration config) {
          this.config = config;
          initVariables();
     }

     private void initVariables() {
          recipes = config.getStringList("recipes");
     }

}
