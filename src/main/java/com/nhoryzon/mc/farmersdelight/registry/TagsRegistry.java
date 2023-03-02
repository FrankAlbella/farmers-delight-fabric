package com.nhoryzon.mc.farmersdelight.registry;

import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import net.fabricmc.fabric.impl.tag.convention.TagRegistration;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

@SuppressWarnings("unused")
public class TagsRegistry {

    public static final TagKey<Item> KNIVES = TagRegistration.ITEM_TAG_REGISTRATION.registerCommon("tools/knives");

    public static final TagKey<Block> WILD_CROPS = create("wild_crops", Registries.BLOCK);
    public static final TagKey<Block> ROPES = create("ropes", Registries.BLOCK);

    public static final TagKey<Block> HEAT_SOURCES = create("heat_sources", Registries.BLOCK);
    public static final TagKey<Block> HEAT_CONDUCTORS = create("heat_conductors", Registries.BLOCK);
    public static final TagKey<Block> TRAY_HEAT_SOURCES = create("tray_heat_sources", Registries.BLOCK);
    public static final TagKey<Block> COMPOST_ACTIVATORS = create("compost_activators", Registries.BLOCK);
    public static final TagKey<Block> UNAFFECTED_BY_RICH_SOIL = create("unaffected_by_rich_soil", Registries.BLOCK);
    public static final TagKey<Block> MUSHROOM_COLONY_GROWABLE_ON = create("mushroom_colony_growable_on", Registries.BLOCK);
    public static final TagKey<Block> KNIVES_CUTTABLE = create("knives_cuttable", Registries.BLOCK);
    public static final TagKey<Block> DROPS_CAKE_SLICE = create("drops_cake_slice", Registries.BLOCK);

    public static final TagKey<Item> WILD_CROPS_ITEM = create("wild_crops", Registries.ITEM);
    public static final TagKey<Item> STRAW_HARVESTERS = create("straw_harvesters", Registries.ITEM);
    public static final TagKey<Item> COMFORT_FOODS = create("comfort_foods", Registries.ITEM);
    public static final TagKey<Item> WOLF_PREY = create("wolf_prey", Registries.ITEM);
    public static final TagKey<Item> CABBAGE_ROLL_INGREDIENTS = create("cabbage_roll_ingredients", Registries.ITEM);

    public static final TagKey<EntityType<?>> DOG_FOOD_USERS = create("dog_food_users", Registries.ENTITY_TYPE);
    public static final TagKey<EntityType<?>> HORSE_FEED_USERS = create("horse_feed_users", Registries.ENTITY_TYPE);

    private static <E> TagKey<E> create(String pathName, DefaultedRegistry<E> registry) {
        return TagKey.of(registry.getKey(), new Identifier(FarmersDelightMod.MOD_ID, pathName));
    }

    private TagsRegistry() throws InstantiationException {
        throw new InstantiationException("Constant class cannot be instantiate");
    }

}
