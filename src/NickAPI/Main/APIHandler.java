package NickAPI.Main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class APIHandler {

	
	private static APIHandler instance;
	private ArrayList<String> _names = new ArrayList<>();
	
	public APIHandler(){
		instance = this;
		_names.add("ImDyl");
		_names.add("zych");
		_names.add("8xO");
		_names.add("xNyu");
		_names.add("Awlo");
		_names.add("Naosuki");
		_names.add("breqking");
		_names.add("100moral");
		_names.add("KingAngelo");
		_names.add("ewdie");
		_names.add("BackInTheDay");
	}
	
	public static APIHandler getInstance(){
		return instance;
	}
	
	private HashMap<String, String> __userNames = new HashMap<>();
	
	public void removeNick(Player p){
		if(p.getMetadata("NICKED") != null){
			if(p.getMetadata("NICKED").get(0).asBoolean() != true){
				return;
			}
			
			String realName = __userNames.get(p.getPlayerListName());
			String nickName = p.getPlayerListName();
			
			String value = null;
	        String signature = null;
	        String name = (realName);
	        __userNames.put(name, null);
	        _names.add(nickName);
	        p.removeMetadata("NICKED", NickAPI.getInstance());
	        p.setMetadata("NICKED", new FixedMetadataValue(NickAPI.getInstance(), false));
	        try {
	            JSONParser parser = new JSONParser();
	            Object obj = parser.parse(getResponse("https://api.mojang.com/users/profiles/minecraft/" + name));
	            JSONObject json = (JSONObject)obj;
	            String uuid = (String)json.get("id");
	            Object obj2 = parser.parse(getResponse("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false"));
	            JSONObject json2 = (JSONObject)obj2;
	            Object props = ((JSONArray)json2.get("properties")).get(0);
	            JSONObject propsObj = (JSONObject)props;
	            value = (String)propsObj.get("value");
	            signature = (String)propsObj.get("signature");
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        for(Player pl : Bukkit.getServer().getOnlinePlayers()){
	            if(pl == p) continue;
	            //REMOVES THE PLAYER
	            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)p).getHandle()));
	            //CHANGES THE PLAYER'S GAME PROFILE
	            GameProfile gp = ((CraftPlayer)p).getProfile();
	            gp.getProperties().removeAll("textures");
	            gp.getProperties().put("textures", new Property("textures", value, signature));
	            //ADDS THE PLAYER
	            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer)p).getHandle()));
	            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(p.getEntityId()));
	            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)p).getHandle()));
	        }
			
			
			
		}
	}
	
	
	 public void changeSkin(Player p){
	        String value = null;
	        String signature = null;
	        Random r = new Random();
	        String name = _names.get(r.nextInt(_names.size()));
	        __userNames.put(name, p.getPlayerListName());
	        _names.remove(name);
	        p.setMetadata("NICKED", new FixedMetadataValue(NickAPI.getInstance(), true));
	        try {
	            JSONParser parser = new JSONParser();
	            Object obj = parser.parse(getResponse("https://api.mojang.com/users/profiles/minecraft/" + name));
	            JSONObject json = (JSONObject)obj;
	            String uuid = (String)json.get("id");
	            Object obj2 = parser.parse(getResponse("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false"));
	            JSONObject json2 = (JSONObject)obj2;
	            Object props = ((JSONArray)json2.get("properties")).get(0);
	            JSONObject propsObj = (JSONObject)props;
	            value = (String)propsObj.get("value");
	            signature = (String)propsObj.get("signature");
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        for(Player pl : Bukkit.getServer().getOnlinePlayers()){
	            if(pl == p) continue;
	            //REMOVES THE PLAYER
	            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)p).getHandle()));
	            //CHANGES THE PLAYER'S GAME PROFILE
	            GameProfile gp = ((CraftPlayer)p).getProfile();
	            gp.getProperties().removeAll("textures");
	            gp.getProperties().put("textures", new Property("textures", value, signature));
	            //ADDS THE PLAYER
	            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer)p).getHandle()));
	            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(p.getEntityId()));
	            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)p).getHandle()));
	        }
	    }
	 
	 public String getResponse(String _url){
	        try {
	            URL url = new URL(_url);
	            URLConnection con = url.openConnection();
	            InputStream in = con.getInputStream();
	            String encoding = con.getContentEncoding();
	            encoding = encoding == null ? "UTF-8" : encoding;
	            String body = IOUtils.toString(in, encoding);
	            return body;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
}
