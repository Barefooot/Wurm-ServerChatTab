package com.worfeus.wurm.serverchattab;

import com.wurmonline.server.players.Player;
import com.wurmonline.server.Message;

import java.awt.Color;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.PlayerLoginListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

public class ServerChatTab implements WurmServerMod, Configurable, PlayerLoginListener {
	static final String modName = "ServerChatTab"; 
	static final Logger logger = Logger.getLogger(modName);
	static final String EmptyString = "";
	static final String ColorWhiteString = "FFFFFF";

	
    boolean enabled;
    boolean debug;
    
    int tabsCount;

    String[] tabsNames;
    String[][] tabsLines;
    Color[] tabsColors;
    
    
    public void configure(Properties properties) {
    	try {
    		enabled = Boolean.valueOf(properties.getProperty("enabled", "false"));
		} catch (Exception ex) {
			enabled = false;
		}
    	if (!enabled) 
    		return;
    	
    	try {
    		debug = Boolean.valueOf(properties.getProperty("debug", "false"));
		} catch (Exception ex) {
			debug = false;
		}
    	
    	if (debug)
    		logInfo("enabled=" + enabled);
    	
    	try {
    		tabsCount = Math.max(0, Integer.valueOf(properties.getProperty("tabsCount", "0").trim()));
		} catch (Exception e) {
    		tabsCount = 0;
		}
    	if (tabsCount == 0) return;
    	
    	if (debug)
    		logInfo("tabsCount=" + tabsCount);
    	

	    tabsNames = new String[tabsCount];
	    tabsLines = new String[tabsCount][];
	    tabsColors = new Color[tabsCount];
    	
    	for (int tabIdx = 0; tabIdx < tabsCount; tabIdx++) {
	    	tabsNames[tabIdx] = String.valueOf(properties.getProperty("tabName."+(tabIdx+1), EmptyString)).trim();
	        tabsLines[tabIdx] = String.valueOf(properties.getProperty("tabLines."+(tabIdx+1), EmptyString)).trim().split("[|]{2}");
	        tabsColors[tabIdx] = Color.decode("#"+properties.getProperty("tabColor."+(tabIdx+1), ColorWhiteString).trim());
	        
	    	if (debug)
	    		logInfo("tab added: tabName." + (tabIdx+1) + "='" + tabsNames[tabIdx] + "'");
		}
    }

    public void onPlayerLogin(Player player){
    	if (!enabled) 
    		return;

    	if (debug)
    		logInfo("Handling onPlayerLogin, player='" + player.getName() + "', tabsCount=" + tabsCount);

		for (int tabIdx = 0; tabIdx < tabsCount; tabIdx++) {
			if (isNullOrEmpty(tabsNames[tabIdx])) continue;
			
	    	if (debug)
	    		logInfo("Adding tab to player='" + player.getName() + "', tabName='" + tabsNames[tabIdx] + "', tabLines.length=" + tabsLines[tabIdx].length);
			
	        try {
	        	for(String tabLines : tabsLines[tabIdx]) {
					Color tabColor = tabsColors[tabIdx];
					player.getCommunicator().sendMessage(new Message(null, (byte) 0, tabsNames[tabIdx], tabLines, tabColor.getRed(), tabColor.getGreen(), tabColor.getBlue()));
				}
	        } catch (Throwable ex) {
	            logWarning(modName + ": error parsing tabIdx=" + tabIdx + ", disabling tab!");
	            tabsNames[tabIdx] = EmptyString;
	        }
		}
    }
    
    public String getVersion() {
        return "v2.0.0.0";
    }

	// -------------------------

	// TODO: need to move this to shared library mod. 
    public static void logSevere(String msg, Throwable e) {
        if(logger != null) 
            logger.log(Level.SEVERE, modName + ": " + msg, e);        
    }

    public static void logWarning(String msg) {
        if(logger != null) 
            logger.log(Level.WARNING, modName + ": " + msg);
    }

    public static void logInfo(String msg) {
        if(logger != null) 
            logger.log(Level.INFO, modName + ": " + msg);
    }

    public static boolean isNullOrEmpty(String str) {
        if(str == null || str.isEmpty())
            return true;
        return false;
    }
}
