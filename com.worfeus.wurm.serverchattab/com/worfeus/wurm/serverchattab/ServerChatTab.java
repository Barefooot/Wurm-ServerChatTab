package com.worfeus.wurm.serverchattab;

import com.wurmonline.server.players.Player;
import com.wurmonline.server.Message;
import com.wurmonline.server.creatures.Communicator;

import java.awt.Color;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.MessagePolicy;
import org.gotti.wurmunlimited.modloader.interfaces.PlayerLoginListener;
import org.gotti.wurmunlimited.modloader.interfaces.PlayerMessageListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

public class ServerChatTab implements WurmServerMod, Configurable, PlayerLoginListener, PlayerMessageListener {
	private static final String modName = "ServerChatTab"; 
	private static final Logger logger = Logger.getLogger(modName);
	static final String EmptyString = "";
	static final String ColorWhiteString = "FFFFFF";

	
    boolean enabled;
    
    int tabsCount;

    String[] tabsNames;
    String[][] tabsLines;
    Color[] tabsColors;
    Boolean[] tabsTypables;
    
    
    public void configure(Properties properties) {
    	try {
    		enabled = Boolean.valueOf(properties.getProperty("enabled", "false"));
		} catch (Exception ex) {
			enabled = false;
		}
    	if (!enabled) return;
    	
    	try {
    		tabsCount = Math.max(0, Integer.valueOf(properties.getProperty("tabsCount", "0").trim()));
		} catch (Exception e) {
    		tabsCount = 0;
		}
    	if (tabsCount == 0) return;

	    tabsNames = new String[tabsCount];
	    tabsLines = new String[tabsCount][];
	    tabsColors = new Color[tabsCount];
	    tabsTypables = new Boolean[tabsCount];
    	
    	for (int tabIdx = 0; tabIdx < tabsCount; tabIdx++) {
	    	tabsNames[tabIdx] = String.valueOf(properties.getProperty("tabName."+tabIdx+1, EmptyString)).trim();
	        tabsLines[tabIdx] = String.valueOf(properties.getProperty("tabLines."+tabIdx+1, EmptyString)).trim().split("[|]{2}");
	        tabsColors[tabIdx] = Color.decode("#"+properties.getProperty("tabColor."+tabIdx+1, ColorWhiteString).trim());
	        tabsTypables[tabIdx] = Boolean.valueOf(properties.getProperty("tabTypable."+tabIdx+1, "false").trim());
		}
    }

    public void onPlayerLogin(Player player){
    	if (!enabled) return;

		for (int tabIdx = 0; tabIdx < tabsCount; tabIdx++) {
			if (tabsNames[tabIdx] == EmptyString) continue;
			
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

    @Deprecated
	@Override
	public boolean onPlayerMessage(Communicator communicator, String message) {
		return false;
	}

    @Override
    public MessagePolicy onPlayerMessage(Communicator communicator, String message, String title) {
    	if (!enabled) return MessagePolicy.PASS;

//		for (int tabIdx = 0; tabIdx < tabsCount; tabIdx++) {
////	        if (title.equals(tab1Name) && !tab1Typable && !message.startsWith("#") && !message.startsWith("/"))
////	            return MessagePolicy.DISCARD;
//		}
    
        return MessagePolicy.PASS;
    }

    public String getVersion() {
        return "v2.0.0.1-alpha";
    }

	// -------------------------

	// TODO: need to move this to shared library mod. 
    public static void logSevere(String msg, Throwable e) {
        if(logger != null) 
            logger.log(Level.SEVERE, msg, e);        
    }

    public static void logWarning(String msg) {
        if(logger != null) 
            logger.log(Level.WARNING, msg);
    }

    public static void logInfo(String msg) {
        if(logger != null) 
            logger.log(Level.INFO, msg);
    }

}
