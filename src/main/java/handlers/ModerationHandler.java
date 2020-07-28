package handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class ModerationHandler {

	/**
	 * Used to create a HashMap to set permissions
	 * 
	 * @param ids is a 2D array that contains an array of IDs
	 * @param isRole is a boolean value that determines if the IDs provided are type Role, OTHERWISE it MUST be type User
	 * @return a HashMap of the key and IDs as designated
	 */
	public static HashMap<String, ArrayList<Long>> createPermissions(Long[][] ids, boolean isRole) {
		
		HashMap<String, ArrayList<Long>> returnThis = new HashMap<String, ArrayList<Long>>();
		
		ArrayList<Long> tempArr = new ArrayList<Long>();
		for(Long[] arr: ids) {
			for(Long id: arr) {
				Collections.addAll(tempArr, id);
			}
		}
		
		if(isRole)
			returnThis.put("ROLE", tempArr);
		else
			returnThis.put("USER", tempArr);
		
		return returnThis;
		
	}
	
	/**
	 * Used to obtain the "Punished" user from a message.
	 * 
	 * @param gld is the Guild in which the Message was sent.
	 * @param msg is the message sent for analysis.
	 * @return a User, presumed to be the "Punished," otherwise null.
	 */
	@SuppressWarnings("unused")
	public static User grabPunished(Guild gld, Message msg) {
		
		User punished = null;
		String decipher = msg.toString();
		String breakDownDigits = "";
		
		// Check if "<" and ">" exist, particularly, with them in order.
		if(decipher.contains("<") && decipher.contains(">")) {
			
			punished = gld.getMemberById(decipher.substring(decipher.indexOf("<") + 1, decipher.indexOf(">"))).getUser();
			
		}
		
		if(punished != null) {
			return punished;
		}
		
		// Check if there's a long ID that could turn up to be the user.
		for(int i = 0; i < breakDownDigits.length(); i++) {
			if(Character.isDigit(decipher.charAt(i))) { 
				breakDownDigits = breakDownDigits + decipher.charAt(i);
			} else {
				break;
			}
		}
		
		if(punished != null) {
			return punished;
		}
		
		// Check if it matches the Username#Discriminator
		punished = gld.getMemberByTag(decipher.substring(decipher.indexOf(" ") + 1, decipher.indexOf("#") + 4)).getUser();
		
		return punished;
		
	}
	
}
