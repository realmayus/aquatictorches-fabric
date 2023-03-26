package realmayus.aquatictorches;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class AquaticTorches implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("aquatictorches");
	public static final AquaticTorchBlock AQUATIC_TORCH = new AquaticTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(15).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME);
	public static final AquaticWallTorchBlock AQUATIC_WALL_TORCH = new AquaticWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(15).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME);

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, new Identifier("aquatictorches", "aquatic_torch"), AQUATIC_TORCH);
		Registry.register(Registries.BLOCK, new Identifier("aquatictorches", "aquatic_wall_torch"), AQUATIC_WALL_TORCH);
		Registry.register(Registries.ITEM, new Identifier("aquatictorches", "aquatic_torch"), new VerticallyAttachableBlockItem(AQUATIC_TORCH, AQUATIC_WALL_TORCH, new Item.Settings(), Direction.DOWN));

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.addAfter(Items.REDSTONE_TORCH, new ItemStack(AQUATIC_WALL_TORCH)));
	}
}