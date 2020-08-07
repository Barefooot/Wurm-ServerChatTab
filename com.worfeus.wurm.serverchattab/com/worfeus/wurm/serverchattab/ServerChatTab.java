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
	private static final Logger logger = Logger.getLogger("ServerChatTab");
	static String modName = "ServerChatTab"; 
	static final String EmptyString = "";
	static final String ColorWhiteString = "FFFFFF";
	

	
    boolean enabled;

    String chatTab1Name;
    String chatTab1Lines;
    Color chatTab1Color = Color.white;
    Boolean chatTab1Typable;
    
    
    public void configure(Properties properties) {
    	enabled = Boolean.valueOf(properties.getProperty("enabled", "false"));
    	if (!enabled) return;
    	
    	chatTab1Name = String.valueOf(properties.getProperty("chatTab1Name", EmptyString)).trim();
        chatTab1Lines = String.valueOf(properties.getProperty("chatTab1Lines", EmptyString));
        chatTab1Typable = Boolean.valueOf(properties.getProperty("chatTab1Typable", "false"));
        chatTab1Color = Color.decode("#"+properties.getProperty("chatTab1Color", ColorWhiteString));
    	chatTab1Name = String.valueOf(properties.getProperty("chatTab1Name", EmptyString)).trim();
    }

    public void onPlayerLogin(Player player){
    	if (!enabled) return;
    	
        try {
            player.getCommunicator().sendMessage(new Message(null, (byte) 0, chatTab1Name, chatTab1Lines, chatTab1Color.getRed(), chatTab1Color.getGreen(), chatTab1Color.getBlue()));
        } catch (Throwable ex) {
            logSevere("Error loading " + modName + " mod", ex);
            throw new RuntimeException(ex);
        }
    }

    @Deprecated
	@Override
	public boolean onPlayerMessage(Communicator communicator, String message) {
		return false;
	}

    @Override
    public MessagePolicy onPlayerMessage(Communicator communicator, String message, String title) {
//        if (title.equals(chatTab1Name) && !chatTab1Typable && !message.startsWith("#") && !message.startsWith("/"))
//            return MessagePolicy.DISCARD;
        return MessagePolicy.PASS;
    }


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

    public String getVersion() {
        return "v2.0.0.1-alpha";
    }
}
