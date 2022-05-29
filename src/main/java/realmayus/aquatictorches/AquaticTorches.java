package realmayus.aquatictorches;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AquaticTorches implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("aquatictorches");
	public static final AquaticTorchBlock AQUATIC_TORCH = new AquaticTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().lightLevel(15).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME);
	public static final AquaticWallTorchBlock AQUATIC_WALL_TORCH = new AquaticWallTorchBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().lightLevel(15).sounds(BlockSoundGroup.WOOD), ParticleTypes.FLAME);
	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier("aquatictorches", "aquatic_torch"), AQUATIC_TORCH);
		Registry.register(Registry.BLOCK, new Identifier("aquatictorches", "aquatic_wall_torch"), AQUATIC_WALL_TORCH);

		Registry.register(Registry.ITEM, new Identifier("aquatictorches", "aquatic_torch"), new WallStandingBlockItem(AQUATIC_TORCH, AQUATIC_WALL_TORCH, new Item.Settings().group(ItemGroup.DECORATIONS)));

	}
}
