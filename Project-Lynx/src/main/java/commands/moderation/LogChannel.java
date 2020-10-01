package commands.moderation;

import org.json.JSONObject;

import commands.Command;
import data.Data;
import handlers.MessageHandler;
import init.InitData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LogChannel extends Command {
	
	public LogChannel() {
		setName("logchannel");
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		if(msg.equalsIgnoreCase("logchannel")) {
			MessageHandler.sendMessage(chn, getDesc());
			return true;
		} else if(!verifyUse(((MessageReceivedEvent) misc).getAuthor(), ((MessageReceivedEvent) misc).getGuild(), chn)) {
			return false;
		}
		
		String modifyThis = new String(msg);
		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		JSONObject data = Data.rawJSON;
		final char[] illegalCharacters = new char[] {'@', '<', '>', '!', '#', ' '};
		
		// Obtain the ID
		modifyThis = modifyThis.substring(modifyThis.indexOf(" ") + 1);
		
		// Clean the ID
		for(char c: illegalCharacters) {
			modifyThis = modifyThis.replaceAll(Character.toString(c), "");
		}
		
		// Modify the data
		data.getJSONObject(gld.getId()).getJSONObject("srvr_config").put("logging_channel", modifyThis);
		
		// Write the new data
		if(Data.writeData(InitData.locationJSON, data.toString(), true, gld.getId())) {
			MessageHandler.sendMessage(chn, "Logging channel is now set to " + gld.getTextChannelById(modifyThis).getAsMention());
		} else {
			MessageHandler.sendMessage(chn, "Logging channel failed to be set. Debug parse: " + modifyThis);
			return false;
		}
		
		return true;
	}

}
