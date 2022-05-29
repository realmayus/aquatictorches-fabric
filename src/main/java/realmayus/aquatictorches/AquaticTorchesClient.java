package realmayus.aquatictorches;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

public class AquaticTorchesClient implements ClientModInitializer {

    public static void registerCutout(Block block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
    }

    @Override
    public void onInitializeClient() {
        registerCutout(AquaticTorches.AQUATIC_TORCH);
        registerCutout(AquaticTorches.AQUATIC_WALL_TORCH);
    }
}
