package me.invertmc.configs;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {
    private File file;
    private FileConfiguration config;

    /**
     * Creates a new instance of configuration.  Make sure the folder exists for file.
     * @param file
     */
    public Configuration(File file){
        this.file = file;
    }

    /**
     * Loads the FileConfiguration. Be sure this method is called before getting the FileConfiguration.
     * @return This returns true if a new file is created.  False is returned if the file already exists.
     * @throws Exception If an exception is thrown there is most likely a problem with the config file.  Call broke() if it throws an exception.  After calling broke try loading again and be sure to set defaults.
     */
    public boolean loadConfig() throws Exception{
        boolean newFile = false;
        if (!file.exists()) {
            file.createNewFile();
            newFile = true;
        }
        config = new YamlConfiguration();
        config.load(file);
        return newFile;
    }

    /**
     * This will rename the file to filepath.extension.broken to show a broken file.
     * @throws Exception is thrown if it fails to rename the file.
     */
    public void broke() throws Exception{
        if(!file.exists()){
            return;
        }else{
            file.renameTo(new File(file.getAbsolutePath() + ".broken"));
        }
    }

    /**
     * This gets the File at which the config is located.
     * @return
     */
    public File getFile(){
        return file;
    }

    /**
     * This gets the file configuration.
     * @return
     */
    public FileConfiguration getConfig(){
        return config;
    }

    /**
     * Saves the config.
     * @throws Exception is thrown if it fails to save.
     */
    public void save() throws Exception{
        config.save(file);
    }
}