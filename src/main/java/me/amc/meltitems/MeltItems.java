package me.amc.meltitems;

import org.bukkit.plugin.java.JavaPlugin;

public class MeltItems extends JavaPlugin {

     public static MeltItems instance; // Singleton

     public ConfigHelper configHelper;

     @Override
     public void onEnable() {
          instance = this;
          saveDefaultConfig();
          reloadConfig();

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
          configHelper = new ConfigHelper(this.getConfig());
     }

}
