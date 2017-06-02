/* Copyright (c) 2016 SpaceToad and the BuildCraft team
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package buildcraft.energy;

import java.util.function.Consumer;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import buildcraft.lib.BCLib;
import buildcraft.lib.registry.MigrationManager;
import buildcraft.lib.registry.RegistryHelper;
import buildcraft.lib.registry.TagManager;
import buildcraft.lib.registry.TagManager.EnumTagType;
import buildcraft.lib.registry.TagManager.TagEntry;

import buildcraft.core.BCCore;
import buildcraft.energy.generation.BiomeInitializer;
import buildcraft.energy.generation.BiomeOilDesert;
import buildcraft.energy.generation.BiomeOilOcean;
import buildcraft.energy.generation.OilPopulate;

//@formatter:off
@Mod(modid = BCEnergy.MODID,
 name = "BuildCraft Energy",
 version = BCLib.VERSION,
 dependencies = "required-after:buildcraftcore@[" + BCLib.VERSION + "]")
//@formatter:on
public class BCEnergy {
    public static final String MODID = "buildcraftenergy";
    static {
        FluidRegistry.enableUniversalBucket(); // FIXME: not working
    }

    @Mod.Instance(MODID)
    public static BCEnergy INSTANCE;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent evt) {
        RegistryHelper.useOtherModConfigFor(MODID, BCCore.MODID);
        BCEnergyItems.preInit();
        BCEnergyFluids.preInit();
        BCEnergyBlocks.preInit();
        BCEnergyEntities.preInit();

        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, BCEnergyProxy.getProxy());
        GameRegistry.register(BiomeOilOcean.INSTANCE);
        GameRegistry.register(BiomeOilDesert.INSTANCE);
        BiomeDictionary.addTypes(
                BiomeOilOcean.INSTANCE,
                BiomeDictionary.Type.OCEAN
        );
        BiomeDictionary.addTypes(
                BiomeOilDesert.INSTANCE,
                BiomeDictionary.Type.HOT,
                BiomeDictionary.Type.DRY,
                BiomeDictionary.Type.SANDY
        );
        MinecraftForge.TERRAIN_GEN_BUS.register(new BiomeInitializer());

        BCEnergyProxy.getProxy().fmlPreInit();
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent evt) {
        BCEnergyRecipes.init();
        BCEnergyProxy.getProxy().fmlInit();
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent evt) {
        MinecraftForge.EVENT_BUS.register(OilPopulate.INSTANCE);
        BCEnergyProxy.getProxy().fmlPostInit();
        registerMigrations();
    }

    private static void registerMigrations() {
        /** 7.99.0 */
        // Fluid registration changed from "fluid_block_[FLUID]" to "fluid_block_heat_[HEAT]_[FLUID]"
        MigrationManager.INSTANCE.addBlockMigration(BCEnergyFluids.crudeOil[0].getBlock(), "fluid_block_oil");
        MigrationManager.INSTANCE.addBlockMigration(BCEnergyFluids.fuelLight[0].getBlock(), "fluid_block_fuel");
    }

    static {
        startBatch();
        // Items
        registerTag("item.glob.oil").reg("glob_oil").locale("globOil").model("glob_oil");

        // Item Blocks

        // Blocks

        // Tiles
        registerTag("tile.engine.stone").reg("engine.stone");
        registerTag("tile.engine.iron").reg("engine.iron");

        endBatch(TagManager.prependTags("buildcraftenergy:", EnumTagType.REGISTRY_NAME, EnumTagType.MODEL_LOCATION).andThen(TagManager.setTab("buildcraft.main")));
    }

    private static TagEntry registerTag(String id) {
        return TagManager.registerTag(id);
    }

    private static void startBatch() {
        TagManager.startBatch();
    }

    private static void endBatch(Consumer<TagEntry> consumer) {
        TagManager.endBatch(consumer);
    }
}
