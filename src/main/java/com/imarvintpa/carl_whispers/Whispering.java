package com.imarvintpa.carl_whispers;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Whispering {

    public World world;
    private static int timeSinceLastMessage = 42;
    private final int TIME_BETWEEN_MESSAGES = 2 * 60 * 20;
    private final static Random rand = new Random();
    
    //Some of this code is shamelessly taken from Extra Utilities 2.
    
    //TODO: make the tool keyword an array set in the config file.
    //TODO: make the various text responses arrays in the config file.
    
	@SubscribeEvent
    public void onTickPlayerEvent(PlayerTickEvent event){

		timeSinceLastMessage--;

		if (timeSinceLastMessage < 0) {
			if(event.phase == Phase.START){
				//
				
				EntityPlayer player = (EntityPlayer) event.player;
				InventoryPlayer inv = player.inventory;
				
				ItemStack myCarl = findCarl(inv.getCurrentItem());				

				for (ItemStack anItem:  inv.offHandInventory) {
					if (!myCarl.isEmpty()) {
						break;
					}
					if (!anItem.isEmpty()) {
						//sendMessage(anItem, player, new TextComponentTranslation("off Kill!"));
						myCarl = findCarl(anItem);
					}
				}

				boolean isSelected = !myCarl.isEmpty();

				
				for (ItemStack anItem:  inv.mainInventory) {
					if (!myCarl.isEmpty()) {
						break;
					}
					
					if (!anItem.isEmpty()) {
						//sendMessage(anItem, player, new TextComponentTranslation("main Kill!"));
						myCarl = findCarl(anItem);
					}
				}
				for (ItemStack anItem:  inv.armorInventory) {
					if (!myCarl.isEmpty()) {
						break;
					}
					if (!anItem.isEmpty()) {
						//sendMessage(anItem, player, new TextComponentTranslation("armor Kill!"));
						myCarl = findCarl(anItem);
					}
				}
				//We have a Carl.
				if (!myCarl.isEmpty()) {					
					RayTraceResult objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
					boolean haveTarget = (objectMouseOver != null && objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY && objectMouseOver.entityHit instanceof EntityCreature); 
					if (isSelected && haveTarget) {
						ITextComponent killMessage = new TextComponentString("Kill ");
						killMessage.appendSibling(objectMouseOver.entityHit.getDisplayName());
						killMessage.appendText("!");
						sendMessage(myCarl, player, killMessage);
					} else if (isSelected) {
						sendMessage(myCarl, player, getRandomElementMulti(
								chat("Time for death and destruction?"),
								chat("I'm hungry, feed me."),
								chat("Hey you, let's go kill everything!"),
								chat("Murder! Death! Kill!"),
								chat("Hack n' slash! Hack n' slash! Hack n' slash! Hack n' slash! Hack n' slash!"),
								chat("Feast on their blood."),
								chat("I feel... sharp."),
								chat("I'm ready and willing."),
								chat("Stabby stabby stab!."),
								chat("Let the essence of life and death flow freely."),
								chat("This world is filled with such life and beauty. Let's go destroy it all.")
						));
					} else {
						//Idle chatter when not active, much less frequent.
						if (rand.nextInt(10) == 0) {
							if (isSelected) {
							sendMessage(myCarl, player, getRandomElementMulti(
								chat("I'm hungry for hands."),
								chat("I'm lonely and hungry, feed me."),
								chat("My stomach is making the rumblies that only hands can satisfy."),
								chat("I yearn for the sound of forgiveness."),
								chat("Hack n' slash! Hack n' slash! Hack n' slash! Hack n' slash! Hack n' slash!"),
								chat("I need to feast!."),
								chat("I feel... neglected."),
								chat("I'm ready and willing."),
								chat("Let's go stabbing!."),
								chat("It is dark in here, let me bathe in blood."),
								chat("It is hard to destroy things while away.")
							));
							} else {
								sendMessage(myCarl, player, chat("Urge to Kill rising!"));
							}
						} else {
							//Test message
							//sendMessage(myCarl, player, chat("Idle miss."));
							timeSinceLastMessage = TIME_BETWEEN_MESSAGES + player.world.rand.nextInt(TIME_BETWEEN_MESSAGES);
						}
					}
				}
			}
		}
    }
	
	public static <K> K getRandomElementArray(K[] ks) {
		int i = rand.nextInt(ks.length);
		return ks[i];
	}
	
	@SafeVarargs
	public static <K> K getRandomElementMulti(K a, K... ks) {
		int i = rand.nextInt(ks.length + 1);
		if (i == 0) return a;
		return ks[i - 1];
}
	
	private TextComponentTranslation chat(String message) {
		return new TextComponentTranslation(message);
	}
	
	private ItemStack findCarl(ItemStack anItem) {
		//If it is empty, just return the empty thing.
		if (anItem.isEmpty())
			return anItem;
		//If the name contains our key name, return it.
		if (anItem.getDisplayName().toLowerCase().indexOf("carl") >= 0) {
			return anItem;
		}
		//Don't return anything if it is not our guy.
		return ItemStack.EMPTY;
	}
	
	private void sendMessage(ItemStack tool, EntityPlayer player, ITextComponent message) {
		TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.message.display.incoming", tool.getDisplayName(), message);
		textcomponenttranslation.getStyle().setColor(TextFormatting.GRAY).setItalic(Boolean.TRUE);
		player.sendMessage(textcomponenttranslation);


		timeSinceLastMessage = TIME_BETWEEN_MESSAGES + player.world.rand.nextInt(TIME_BETWEEN_MESSAGES);
}

}
