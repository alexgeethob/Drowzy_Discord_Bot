package com.github.alexgeethob.DrowzyBot;

import java.awt.Color;
import java.io.File;

import com.github.cliftonlabs.json_simple.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.CompletableFuture;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class MessageListener implements MessageCreateListener{
	//static arrays/flags for keys
	static final String[] bw = {"kills_bedwars","deaths_bedwars","wins_bedwars","losses_bedwars","final_kills_bedwars","final_deaths_bedwars","beds_broken_bedwars","winstreak"};
	static final String solo = "eight_one_";
	static final String doubles = "eight_two_";
	static final String threes = "four_three_";
	static final String fours = "four_four_";
	
	public void onMessageCreate(MessageCreateEvent event) {
		//gets any message from discord, parses until fits command requirements
		if(event.getMessageAuthor().isBotUser()) {
			return;
		}
		String s = event.getMessageContent();
		if(!s.startsWith("%")) {
			return;
		}
		s = s.substring(1).toLowerCase();
		
		//commands
		if(s.toLowerCase().split(" ")[0].equals("bedwars") || s.toLowerCase().split(" ")[0].equals("bw")) {
			String name = checkName(event);if(name == null) {return;}
			JsonObject obj = generateData(name);if(obj == null) {event.getChannel().sendMessage(":x: Either the username you entered is wrong, or an unexpected error occurred. Please try again.");return;}
			String[] keyClone = bw.clone();
			String[] info = getInfo(obj,keyClone);
			EmbedBuilder emb = createEmbed(name, info,"");
			event.getChannel().sendMessage(emb);
		}
		else if(s.toLowerCase().split(" ")[0].equals("solo") || s.toLowerCase().split(" ")[0].equals("ones")){
			String name = checkName(event);if(name == null) {return;}
			JsonObject obj = generateData(name);if(obj == null) {event.getChannel().sendMessage(":x: Either the username you entered is wrong, or an unexpected error occurred. Please try again.");return;}
			String[] keyClone = bw.clone();
			for(int i=0;i<8;i++) {
				keyClone[i] = solo + keyClone[i];
			}
			String[] info = getInfo(obj,keyClone);
			EmbedBuilder emb = createEmbed(name, info,"Solo ");
			event.getChannel().sendMessage(emb);
		}
		else if(s.toLowerCase().split(" ")[0].equals("doubles") || s.toLowerCase().split(" ")[0].equals("twos")){
			String name = checkName(event);if(name == null) {return;}
			JsonObject obj = generateData(name);if(obj == null) {event.getChannel().sendMessage(":x: Either the username you entered is wrong, or an unexpected error occurred. Please try again.");return;}
			String[] keyClone = bw.clone();
			for(int i=0;i<8;i++) {
				keyClone[i] = doubles + keyClone[i];
			}
			String[] info = getInfo(obj,keyClone);
			EmbedBuilder emb = createEmbed(name, info,"Doubles ");
			event.getChannel().sendMessage(emb);
		}
		else if(s.toLowerCase().split(" ")[0].equals("threes")){
			String name = checkName(event);if(name == null) {return;}
			JsonObject obj = generateData(name);if(obj == null) {event.getChannel().sendMessage(":x: Either the username you entered is wrong, or an unexpected error occurred. Please try again.");return;}
			String[] keyClone = bw.clone();
			for(int i=0;i<8;i++) {
				keyClone[i] = threes + keyClone[i];
			}
			String[] info = getInfo(obj,keyClone);
			EmbedBuilder emb = createEmbed(name, info,"Threes ");
			event.getChannel().sendMessage(emb);
		}
		else if(s.toLowerCase().split(" ")[0].equals("fours")){
			String name = checkName(event);if(name == null) {return;}
			JsonObject obj = generateData(name);if(obj == null) {event.getChannel().sendMessage(":x: Either the username you entered is wrong, or an unexpected error occurred. Please try again.");return;}
			String[] keyClone = bw.clone();
			for(int i=0;i<8;i++) {
				keyClone[i] = fours + keyClone[i];
			}
			String[] info = getInfo(obj,keyClone);
			EmbedBuilder emb = createEmbed(name, info,"Fours ");
			event.getChannel().sendMessage(emb);
		}
		else if(s.toLowerCase().split(" ")[0].equals("file")){
			String name = checkName(event); if(name == null) {return;}
			file(event,name);
		}
		else if(s.toLowerCase().split(" ")[0].equals("help")) {
			Main.generateHelp(event);
		}
		else {
			event.getMessage().getChannel().sendMessage(":no_entry_sign: Improper command. Try again. ");
		}
	}
	
	
	//checks to see if name is present (NOT IF IT WORKS)
	private String checkName(MessageCreateEvent event) {
		//System.out.println(event.getMessageContent());
		String[] line = event.getMessageContent().split(" ");
		if(line.length < 2) {
			event.getChannel().sendMessage(":warning: Error! Please enter any username.");
			return null;
		}

		String name = line[1].toLowerCase();
		return name;
	}
	//generates file (required because completable futures are messy)
	private void file(MessageCreateEvent event, String name) {
		//idk if this will work...
		Object check = generateData(name);
		if(check == null) {event.getChannel().sendMessage(":x: Either the username you entered is wrong, or an unexpected error occurred. Please try again."); return;}
		CompletableFuture<Message> msg = new MessageBuilder()
				.append("Full data file: ")
				.addAttachment(new File(name+".json"))
				.send(event.getChannel());
		return;
	}
	
	//creates embed with information about stats
	private EmbedBuilder createEmbed(String name, String[] info, String flag) {
		EmbedBuilder embed = new EmbedBuilder()
			    .setAuthor(flag+"Bedwars Statistics", null, "https://vignette3.wikia.nocookie.net/minecraftpocketedition/images/c/c5/Bed.png/revision/latest?cb=20130227185101")
			    .addInlineField(flag + "Kills:", info[0])
			    .addInlineField(flag + "Wins:", info[2])
			    .addInlineField(flag + "Final Kills:", info[4])
			    .addInlineField(flag + "Deaths:", info[1])
			    .addInlineField(flag + "Losses:", info[3])
			    .addInlineField(flag + "Final Deaths:", info[5])
			    .addInlineField(flag + "Beds Broken:", info[6])
			    .addInlineField(flag + "Current Winstreak:", info[7]);
		if(flag.equals("")){
			embed.setColor(Color.LIGHT_GRAY);
		}
		else if(flag.equals("Solo ") || flag.equals("Doubles ")){
			embed.setColor(Color.YELLOW);
		}
		else {
			embed.setColor(Color.GREEN);
		}
		return embed;
	}
	
	//generates JsonObject(map) with correct data
	private JsonObject generateData(String name){
		String json = GetAPI.getInfo(name);
		if(json == null) {
			return null;
		}
		json = Jsoner.prettyPrint(json);
		JsonObject obj = new JsonObject();
		try {
			obj = (JsonObject)Jsoner.deserialize(json);
		} 
		catch (JsonException e) {
			e.printStackTrace();
		}
		obj = (JsonObject) ((JsonObject)((JsonObject)obj.get("player")).get("stats")).get("Bedwars");
		json = Jsoner.prettyPrint(obj.toJson());
		
		updateFile(name, json);
		return obj;
	}

	//updates file
	private void updateFile(String name, String json) {
		try {
			File file = new File(name+".json");
			file.createNewFile();
			FileWriter myWriter = new FileWriter(name+".json");
			myWriter.write(json);
			myWriter.close();
		}
		catch (IOException e) {
			System.out.println(e);
			return;
		}
	}
	
	//retrieves information from JsonObject using keys in keyClone
	private String[] getInfo(JsonObject obj, String[] keyClone) {
		String info = "";
		for(String s:keyClone) {
			info += obj.get(s)+" ";
		}
		return info.split(" ");
	}
	

}
