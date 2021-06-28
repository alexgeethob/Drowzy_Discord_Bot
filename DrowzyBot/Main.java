package com.github.alexgeethob.DrowzyBot;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class Main {

	public static void main(String[] args) {
		DiscordApi api = new DiscordApiBuilder().setToken("ODQ0NjM3NzM5ODkwNTA3Nzg2.YKVUMQ.qzI2XLEQWOhouDIUG73zojHJx9U").login().join();
		MessageListener msgListen = new MessageListener();
		api.addMessageCreateListener(msgListen);
	}


	//other file too messy
	public static void generateHelp(MessageCreateEvent event) {
		EmbedBuilder embed = new EmbedBuilder()
				.setAuthor("Command List")
				.addField("help", "Displays this list. Commands are not case-sensitive.")
				.addField("bedwars", "Displays a list of bedwars stats across all modes for the given user.\nAliases: bw")
				.addField("solos", "Displays a list of bedwars stats in the solos mode for the given user.\nAliases: ones")
				.addField("doubles", "Displays a list of bedwars stats in the doubles mode for the given user.\nAliases: twos")
				.addField("threes", "Displays a list of bedwars stats in the threes mode for the given user.")
				.addField("fours", "Displays a list of bedwars stats in the fours mode for the given user.")
				.addField("file", "Generates a json file of bedwars stats for the specified user.\n(Mostly for debug, do not expect actual information)");
		event.getChannel().sendMessage(embed);
		return;
	}
}
