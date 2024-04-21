package thaumicbees.main;

import java.io.File;

import thaumicbees.block.BlockPlanks;
import thaumicbees.block.BlockWoodSlab;
import thaumicbees.item.ItemCapsule;
import thaumicbees.item.ItemComb;
import thaumicbees.item.ItemDrop;
import thaumicbees.item.ItemMagicHiveFrame;
import thaumicbees.item.ItemMiscResources;
import thaumicbees.item.ItemNugget;
import thaumicbees.item.ItemPollen;
import thaumicbees.item.ItemPropolis;
import thaumicbees.item.ItemThaumiumGrafter;
import thaumicbees.item.ItemThaumiumScoop;
import thaumicbees.item.ItemSolidFlux;
import thaumicbees.item.ItemWax;
import thaumicbees.item.types.CapsuleType;
import thaumicbees.item.types.HiveFrameType;
import thaumicbees.item.types.NuggetType;
import thaumicbees.item.types.PlankType;
import thaumicbees.item.types.ResourceType;
import thaumicbees.main.utils.CompatabilityManager;
import thaumicbees.main.utils.LocalizationManager;
import thaumicbees.main.utils.compat.EquivalentExchangeHelper;
import thaumicbees.main.utils.compat.ForestryHelper;
import thaumicbees.main.utils.compat.ThaumcraftHelper;
import thaumicbees.storage.BackpackDefinition;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import forestry.api.apiculture.BeeManager;
import forestry.api.storage.BackpackManager;
import forestry.api.storage.EnumBackpackType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * A class to hold some data related to mod state & functions.
 * @author MysteriousAges
 *
 */
public class Config
{
	public boolean DrawParticleEffects;	
	public boolean BeeInfusionsAdded;
	public boolean AddThaumcraftItemsToBackpacks;
	public boolean SkipUpdateCheck;
	public boolean AreMagicPlanksFlammable;
	public boolean UseImpregnatedStickInTools;
	public String ThaumaturgeExtraItems;
	public int CapsuleStackSizeMax;

	public static BlockPlanks planksWood;
	public static BlockWoodSlab slabWoodHalf;
	public static BlockWoodSlab slabWoodFull;
	
	public static ItemComb combs;
	public static ItemWax wax;
	public static ItemPropolis propolis;
	public static ItemDrop drops;
	public static ItemPollen pollen;
	public static ItemSolidFlux solidFlux;
	public static ItemMiscResources miscResources;
	public static ItemFood jellyBaby;
	public static ItemThaumiumScoop thaumiumScoop;
	public static ItemThaumiumGrafter thaumiumGrafter;
	public static ItemNugget nuggets;
	
	//----- Liquid Capsules --------------------
	public static ItemCapsule magicCapsule;
	public static ItemCapsule voidCapsule;
	
	//----- Apiary Frames ----------------------
	public static ItemMagicHiveFrame hiveFrameMagic;
	public static ItemMagicHiveFrame hiveFrameResilient;
	public static ItemMagicHiveFrame hiveFrameGentle;
	public static ItemMagicHiveFrame hiveFrameMetabolic;
	public static ItemMagicHiveFrame hiveFrameNecrotic;
	public static ItemMagicHiveFrame hiveFrameTemporal;
	public static ItemMagicHiveFrame hiveFrameOblivion;
	
	//----- Backpacks ------------------------------------------
	public static Item thaumaturgeBackpackT1;
	public static Item thaumaturgeBackpackT2;
	public static BackpackDefinition thaumaturgeBackpackDef;

	//----- Forestry Blocks ------------------------------------
	//----- Forestry Items -------------------------------------
	public static Item fBeeComb;
	public static Item fPollen;
	public static Item fCraftingResource;
	//----- Thaumcraft Blocks ----------------------------------
	public static Block tcPlant;
	public static Block tcCandle;
	public static Block tcCrystal;
	public static Block tcMarker;
	public static Block tcJar;
	public static Block tcLog;
	public static Block tcLeaf;
	public static Block tcWarded;
	//----- Thaumcraft Items -----------------------------------
	public static Item tcFilledJar;
	public static Item tcMiscResource;
	public static Item tcEssentiaBottle;
	public static Item tcShard;
	public static Item tcGolem;
	public static Item tcWispEssence;
	public static Item tcNuggets;
	public static Item tcNuggetChicken;
	public static Item tcNuggetBeef;
	public static Item tcNuggetPork;
	//----- Equivalent Exchange Items --------------------------
	public static Item eeMinuimShard;
	

	//----- Config State info ----------------------------------
	private Configuration tbConfig;
	
	public Config(File configFile)
	{
		this.tbConfig = new Configuration(configFile);
		
		this.tbConfig.load();
		this.doMiscConfig();
	}
	
	public void saveConfigs()
	{
		this.tbConfig.save();
	}

	public void setupBlocks()
	{
		ThaumcraftHelper.getBlocks();
		ForestryHelper.getBlocks();
		EquivalentExchangeHelper.getBlocks();
		
		int blockIdBase = 1750;
		
		{
			int plankId = tbConfig.getBlock("planksTC", blockIdBase++).getInt();
			int slabFullId = tbConfig.getBlock("slabFull", blockIdBase++).getInt();
			int slabHalfId = tbConfig.getBlock("slabHalf", blockIdBase++).getInt();
			
			if (ThaumcraftHelper.isActive())
			{
				planksWood = new BlockPlanks(plankId);
		
				Item item = new ItemMultiTextureTile(planksWood.blockID - 256, planksWood, PlankType.getAllNames())
					.setItemName(planksWood.getBlockName()).setTextureFile(CommonProxy.TCBEES_ITEMS_IMAGE);
		        Item.itemsList[planksWood.blockID] = item;
		        
		        OreDictionary.registerOre("plankWood", new ItemStack(planksWood, 1, -1));
				FMLInterModComms.sendMessage("BuildCraft|Transport", "add-facade", ThaumicBees.getConfig().planksWood.blockID + "@" + PlankType.GREATWOOD.ordinal());
				FMLInterModComms.sendMessage("BuildCraft|Transport", "add-facade", ThaumicBees.getConfig().planksWood.blockID + "@" + PlankType.SILVERWOOD.ordinal());
		        
		        slabWoodFull = new BlockWoodSlab(slabFullId, true);
		        slabWoodHalf = new BlockWoodSlab(slabHalfId, false);
		        
		        item = new ItemSlab(slabWoodHalf.blockID - 256, slabWoodHalf, slabWoodFull, false)
		    		.setItemName(slabWoodHalf.getBlockName()).setTextureFile(CommonProxy.TCBEES_ITEMS_IMAGE);
			    Item.itemsList[slabWoodHalf.blockID] = item;
			    item = new ItemSlab(slabWoodFull.blockID - 256, slabWoodHalf, slabWoodFull, true)
					.setItemName(slabWoodFull.getBlockName()).setTextureFile(CommonProxy.TCBEES_ITEMS_IMAGE);
			    Item.itemsList[slabWoodFull.blockID] = item;
			    
			    OreDictionary.registerOre("slabWood", new ItemStack(slabWoodHalf, 1, -1));
			}
		}
		
	}
	
	public void registerTileEntities()
	{

	}
	
	public void setupItems()
	{
		ThaumcraftHelper.getItems();
		ForestryHelper.getItems();
		EquivalentExchangeHelper.getItems();
		
		int itemIDBase = 26090;
		
		Property p = tbConfig.getItem("combs", itemIDBase++);
		combs= new ItemComb(p.getInt());
		OreDictionary.registerOre("beeComb", new ItemStack(combs, 1, -1));
		
		wax = new ItemWax(tbConfig.getItem("wax", itemIDBase++).getInt());
		propolis = new ItemPropolis(tbConfig.getItem("propolis", itemIDBase++).getInt());
		drops = new ItemDrop(tbConfig.getItem("drop", itemIDBase++).getInt());
		miscResources = new ItemMiscResources(tbConfig.getItem("miscResources", itemIDBase++).getInt());
		

		// Make Aromatic Lumps a swarmer inducer. Chance is /1000.
		BeeManager.inducers.put(miscResources.getStackForType(ResourceType.AROMATIC_LUMP), 80);
		
		{
			int tier1 = tbConfig.getItem("thaumaturgePack1", itemIDBase++).getInt();
			int tier2 = tbConfig.getItem("thaumaturgePack2", itemIDBase++).getInt();
			
			if (ThaumcraftHelper.isActive())
			{
				try
				{
					// 0x8700C6 = purpleish.
					String backpackName = LocalizationManager.getLocalizedString("tb.backpack.thaumaturge");
					BackpackDefinition def = new BackpackDefinition("thaumaturge", backpackName, 0x8700C6);
					thaumaturgeBackpackT1 = BackpackManager.backpackInterface.addBackpack(tier1, def, EnumBackpackType.T1);
					thaumaturgeBackpackT2 = BackpackManager.backpackInterface.addBackpack(tier2, def, EnumBackpackType.T2);
					// Add additional items from configs to backpack.
					if (ThaumicBees.getConfig().ThaumaturgeExtraItems.length() > 0)
					{
						FMLLog.info("Attempting to add extra items to Thaumaturge's backpack! If you get an error, check your ThaumicBees.conf.");
						FMLInterModComms.sendMessage("Forestry", "add-backpack-items", "thaumaturge@" + ThaumicBees.getConfig().ThaumaturgeExtraItems);
					}
				}
				catch (Exception e)
				{
					FMLLog.severe("ThaumicBees encountered a problem during loading!");
					FMLLog.severe("Could not register backpacks via Forestry. Check your FML Client log and see if Forestry crashed silently.");
				}
			}
		}
		
		magicCapsule = new ItemCapsule(CapsuleType.MAGIC, tbConfig.getItem("magicCapsule", itemIDBase++).getInt(), this.CapsuleStackSizeMax);
		
		pollen = new ItemPollen(tbConfig.getItem("pollen", itemIDBase++).getInt());
		
		{
			int crystalId = tbConfig.getItem("fluxCrystal", itemIDBase++).getInt();
			if (ThaumcraftHelper.isActive())
			{
				solidFlux = new ItemSolidFlux(crystalId);
			}
		}
		
		hiveFrameMagic = new ItemMagicHiveFrame(tbConfig.getItem("frameMagic", itemIDBase++).getInt(), HiveFrameType.MAGIC);
		hiveFrameResilient = new ItemMagicHiveFrame(tbConfig.getItem("frameResilient", itemIDBase++).getInt(), HiveFrameType.RESILIENT);
		hiveFrameGentle = new ItemMagicHiveFrame(tbConfig.getItem("frameGentle", itemIDBase++).getInt(), HiveFrameType.GENTLE);
		hiveFrameMetabolic = new ItemMagicHiveFrame(tbConfig.getItem("frameMetabolic", itemIDBase++).getInt(), HiveFrameType.METABOLIC);
		hiveFrameNecrotic = new ItemMagicHiveFrame(tbConfig.getItem("frameNecrotic", itemIDBase++).getInt(), HiveFrameType.NECROTIC);
		hiveFrameTemporal = new ItemMagicHiveFrame(tbConfig.getItem("frameTemporal", itemIDBase++).getInt(), HiveFrameType.TEMPORAL);
		hiveFrameOblivion = new ItemMagicHiveFrame(tbConfig.getItem("frameOblivion", itemIDBase++).getInt(), HiveFrameType.OBLIVION);
		// Future frames, so they all are clumped together.
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(new ItemStack(hiveFrameOblivion), 1, 1, 18));
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(new ItemStack(hiveFrameOblivion), 1, 3, 23));

		jellyBaby = new ItemFood(tbConfig.getItem("jellyBabies", itemIDBase++).getInt(), 1, false).setAlwaysEdible()
				.setPotionEffect(Potion.moveSpeed.id, 5, 1, 1f);
		jellyBaby.setTextureFile(CommonProxy.TCBEES_ITEMS_IMAGE).setIconIndex(19).setItemName("jellyBabies");
		
		voidCapsule = new ItemCapsule(CapsuleType.VOID, tbConfig.getItem("voidCapsule", itemIDBase++).getInt(), this.CapsuleStackSizeMax);

		{
			int scoopID = tbConfig.getItem("thaumiumScoop", itemIDBase++).getInt();
			int grafterID = tbConfig.getItem("thaumiumGrafter", itemIDBase++).getInt();
			if (ThaumcraftHelper.isActive())
			{
				thaumiumScoop = new ItemThaumiumScoop(scoopID);
				MinecraftForge.setToolClass(thaumiumScoop, "scoop", 3);
				
				thaumiumGrafter = new ItemThaumiumGrafter(grafterID);
				MinecraftForge.setToolClass(thaumiumGrafter, "grafter", 3);
			}
		}
		
		// Other tools I might need in the future
		itemIDBase++;
		itemIDBase++;
		
		nuggets = new ItemNugget(tbConfig.getItem("beeNuggets", itemIDBase++).getInt());
		
		OreDictionary.registerOre("nuggetIron", nuggets.getStackForType(NuggetType.IRON));
		OreDictionary.registerOre("nuggetCopper", nuggets.getStackForType(NuggetType.COPPER));
		OreDictionary.registerOre("nuggetTin", nuggets.getStackForType(NuggetType.TIN));
		OreDictionary.registerOre("nuggetSilver", nuggets.getStackForType(NuggetType.SILVER));
		OreDictionary.registerOre("nuggetLead", nuggets.getStackForType(NuggetType.LEAD));
		OreDictionary.registerOre("shardDiamond", nuggets.getStackForType(NuggetType.DIAMOND));
		OreDictionary.registerOre("shardEmerald", nuggets.getStackForType(NuggetType.EMERALD));
	}
	
	private void doMiscConfig()
	{
		Property p;
		
		p = tbConfig.get("general", "backpack.thaumaturge.additionalItems", "");
		p.comment = "Add additional items to the Thaumaturge's Backpack." +
				"\n Format is the same as Forestry's: id:meta;id;id:meta (etc)";
		this.ThaumaturgeExtraItems = p.value;
		
		p = tbConfig.get("general", "backpack.forestry.addThaumcraftItems", true);
		p.comment = "Set to true if you want ThaumicBees to add several Thaumcraft blocks & items to Forestry backpacks." +
				"\n Set to false to disable.";
		this.AddThaumcraftItemsToBackpacks = p.getBoolean(true);
		
		p = tbConfig.get("general", "capsuleStackSize", 64);
		p.comment = "Allows you to edit the stack size of the capsules in ThaumicBees if using GregTech, \n" +
				"or the reduced capsule size in Forestry & Railcraft. Default: 64";
		this.CapsuleStackSizeMax = p.getInt();
		
		p = tbConfig.get("general", "disableVersionCheck", false);
		p.comment = "Set to true to stop ThaumicBees from checking for updates.";
		this.SkipUpdateCheck = p.getBoolean(false);
		
		p = tbConfig.get("general", "areMagicPlanksFlammable", false);
		p.comment = "Set to true to allow Greatwood & Silverwood planks to burn in a fire.";
		this.AreMagicPlanksFlammable = p.getBoolean(false);
		
		p = tbConfig.get("general", "useImpregnatedStickInTools", false);
		p.comment = "Set to true to make Thaumium Grafter & Scoop require impregnated sticks in the recipe.";
		this.UseImpregnatedStickInTools = p.getBoolean(false);
	}

}
