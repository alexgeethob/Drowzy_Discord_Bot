package com.github.alexgeethob.DrowzyBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GetAPI {
	static String hypixel = "https://api.hypixel.net/player?";
	static String key = "a89c2ee9-18f6-4dab-b6e7-d141d32e9e96";
	static String mojang = "https://api.mojang.com/users/profiles/minecraft/";
	
	//retrieves information from hypixel API using UUID
	public static String getInfo(String name){
		try {
			String UUID = mojangUUID(name);
			if(UUID == null) {return null;}
			URL url = new URL(hypixel+"key="+key+"&uuid="+UUID);
        	BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            StringBuilder sb = new StringBuilder(); 
            while ((line = read.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
           
            return sb.toString();
        }
		catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}
	
	//retrives UUID from mojang using username
	private static String mojangUUID(String name) {
        try {
        	URL url = new URL(mojang+name);
        	BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = br.readLine();
            if(line == null) {
            	System.out.println("Error retrieving data.");
            	return null;
            }
            line = line.substring(line.indexOf("\"id\""));
            line = line.substring(6,line.length()-2);
            
            return line;
        }
        catch(IOException e) {
        	System.out.println(e);
        	return null;
        }
	}
}
