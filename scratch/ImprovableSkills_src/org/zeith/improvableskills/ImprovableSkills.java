/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.common.Tags$Items
 *  net.minecraftforge.event.RegisterCommandsEvent
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
 *  net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent
 *  net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
 *  net.minecraftforge.registries.IForgeRegistry
 *  net.minecraftforge.registries.NewRegistryEvent
 *  net.minecraftforge.registries.RegistryBuilder
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.zeith.api.registry.RegistryMapping
 *  org.zeith.hammerlib.api.items.CreativeTab
 *  org.zeith.hammerlib.api.items.CreativeTab$RegisterTab
 *  org.zeith.hammerlib.api.proxy.IProxy
 *  org.zeith.hammerlib.core.adapter.LanguageAdapter
 *  org.zeith.hammerlib.core.adapter.LootTableAdapter
 *  org.zeith.hammerlib.core.adapter.recipe.ShapedRecipeBuilder
 *  org.zeith.hammerlib.core.adapter.recipe.ShapelessRecipeBuilder
 *  org.zeith.hammerlib.event.fml.FMLFingerprintCheckEvent
 *  org.zeith.hammerlib.event.recipe.RegisterRecipesEvent
 *  org.zeith.hammerlib.proxy.HLConstants
 *  org.zeith.hammerlib.util.CommonMessages
 */
package org.zeith.improvableskills;

import com.mojang.brigadier.CommandDispatcher;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.api.proxy.IProxy;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;
import org.zeith.hammerlib.core.adapter.LootTableAdapter;
import org.zeith.hammerlib.core.adapter.recipe.ShapedRecipeBuilder;
import org.zeith.hammerlib.core.adapter.recipe.ShapelessRecipeBuilder;
import org.zeith.hammerlib.event.fml.FMLFingerprintCheckEvent;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.CommonMessages;
import org.zeith.improvableskills.api.recipe.Is3RecipeBuilderExtension;
import org.zeith.improvableskills.api.registry.PageletBase;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.cfg.ConfigsIS;
import org.zeith.improvableskills.command.CommandImprovableSkills;
import org.zeith.improvableskills.custom.LootTableLoader;
import org.zeith.improvableskills.custom.items.ItemSkillsBook;
import org.zeith.improvableskills.init.AbilitiesIS;
import org.zeith.improvableskills.init.ItemsIS;
import org.zeith.improvableskills.init.TreasuresIS;
import org.zeith.improvableskills.proxy.ISClient;
import org.zeith.improvableskills.proxy.ISServer;

@Mod(value="improvableskills")
public class ImprovableSkills {
    public static final Logger LOG = LogManager.getLogger((String)"ImprovableSkills");
    public static final String MOD_ID = "improvableskills";
    public static final String MOD_NAME = "Improvable Skills";
    public static final String NBT_DATA_TAG = "ImprovableSkillsData";
    public static final ISServer PROXY = (ISServer)IProxy.create(() -> ISClient::new, () -> ISServer::new);
    @CreativeTab.RegisterTab
    public static final CreativeTab TAB = new CreativeTab(new ResourceLocation("improvableskills", "root"), b -> b.m_257737_(() -> ((ItemSkillsBook)ItemsIS.SKILLS_BOOK).m_7968_()).m_257941_((Component)Component.m_237115_((String)"itemGroup.improvableskills")).withTabsBefore(new ResourceLocation[]{HLConstants.HL_TAB.id()}));
    private static Supplier<IForgeRegistry<PlayerSkillBase>> SKILLS;
    private static Supplier<IForgeRegistry<PlayerAbilityBase>> ABILITIES;
    private static Supplier<IForgeRegistry<PageletBase>> PAGELETS;

    public ImprovableSkills() {
        CommonMessages.printMessageOnIllegalRedistribution(ImprovableSkills.class, (Logger)LOG, (String)"ImprovableSkills", (String)"https://www.curseforge.com/minecraft/mc-mods/improvable-skills");
        LanguageAdapter.registerMod((String)MOD_ID);
        LootTableAdapter.addLoadHook(LootTableLoader::loadTable);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::newRegistries);
        modBus.addListener(this::setup);
        modBus.addListener(this::loadComplete);
        modBus.addListener(this::fingerprintCheck);
        modBus.addListener(this::addRecipes);
        PROXY.register(modBus);
        IEventBus mcfBus = MinecraftForge.EVENT_BUS;
        mcfBus.addListener(this::registerCommands);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    private void fingerprintCheck(FMLFingerprintCheckEvent e) {
        CommonMessages.printMessageOnFingerprintViolation((FMLFingerprintCheckEvent)e, (String)"97e852e9b3f01b83574e8315f7e77651c6605f2b455919a7319e9869564f013c", (Logger)LOG, (String)"ImprovableSkills", (String)"https://www.curseforge.com/minecraft/mc-mods/improvable-skills");
    }

    private void registerCommands(RegisterCommandsEvent e) {
        CommandImprovableSkills.register((CommandDispatcher<CommandSourceStack>)e.getDispatcher(), e.getBuildContext());
    }

    private void setup(FMLCommonSetupEvent e) {
        TreasuresIS.register();
    }

    private void loadComplete(FMLLoadCompleteEvent e) {
        ConfigsIS.reloadCosts();
        if (ConfigsIS.config.hasChanged()) {
            ConfigsIS.config.save();
        }
    }

    private void newRegistries(NewRegistryEvent e) {
        SKILLS = e.create(new RegistryBuilder().setName(new ResourceLocation(MOD_ID, "skills")).disableSync(), reg -> RegistryMapping.report(PlayerSkillBase.class, (IForgeRegistry)reg, (boolean)false));
        ABILITIES = e.create(new RegistryBuilder().setName(new ResourceLocation(MOD_ID, "abilities")).disableSync(), reg -> RegistryMapping.report(PlayerAbilityBase.class, (IForgeRegistry)reg, (boolean)false));
        PAGELETS = e.create(new RegistryBuilder().setName(new ResourceLocation(MOD_ID, "pagelets")).disableSync(), reg -> RegistryMapping.report(PageletBase.class, (IForgeRegistry)reg, (boolean)false));
    }

    private void addRecipes(RegisterRecipesEvent e) {
        ((ShapedRecipeBuilder)e.shaped().shape(new String[]{"lbl", "pgp", "lbl"}).map('l', (Object)Tags.Items.LEATHER).map('b', (Object)Items.f_42517_).map('p', (Object)Items.f_42516_).map('g', (Object)Tags.Items.INGOTS_GOLD).result((ItemLike)ItemsIS.SKILLS_BOOK)).register();
        ((ShapelessRecipeBuilder)e.shapeless().add((Object)ItemsIS.PARCHMENT_FRAGMENT).result(new ItemStack((ItemLike)Items.f_42516_, 7))).register();
        Is3RecipeBuilderExtension $ = (Is3RecipeBuilderExtension)e.extension(Is3RecipeBuilderExtension.class);
        $.parchment().abilityScroll(AbilitiesIS.ANVIL).addAll(Tags.Items.ENDER_PEARLS, Items.f_42146_, Tags.Items.GEMS_EMERALD).registerIf(AbilitiesIS.ANVIL::registered);
        $.parchment().abilityScroll(AbilitiesIS.CRAFTER).addAll(Tags.Items.ENDER_PEARLS, Items.f_41960_, Tags.Items.INGOTS_IRON).registerIf(AbilitiesIS.CRAFTER::registered);
        $.parchment().abilityScroll(AbilitiesIS.ENCHANTING).addAll(Tags.Items.ENDER_PEARLS, Items.f_42100_, Items.f_41997_).registerIf(AbilitiesIS.ENCHANTING::registered);
        $.parchment().abilityScroll(AbilitiesIS.MAGNETISM).addAll(Tags.Items.ENDER_PEARLS, Items.f_42545_, Items.f_42416_, Items.f_42026_).registerIf(AbilitiesIS.MAGNETISM::registered);
        $.parchment().abilityScroll(AbilitiesIS.AUTO_XP_BANK).addAll(Tags.Items.ENDER_PEARLS, Items.f_42612_, Items.f_42451_).registerIf(AbilitiesIS.AUTO_XP_BANK::registered);
        $.parchment().abilityScroll(AbilitiesIS.COWBOY).addAll(Tags.Items.ENDER_PEARLS, Items.f_42525_, Items.f_42686_, Items.f_42450_).registerIf(AbilitiesIS.COWBOY::registered);
    }

    public static IForgeRegistry<PlayerSkillBase> SKILLS() {
        return SKILLS.get();
    }

    public static IForgeRegistry<PlayerAbilityBase> ABILITIES() {
        return ABILITIES.get();
    }

    public static IForgeRegistry<PageletBase> PAGELETS() {
        return PAGELETS.get();
    }
}

