package com.nhoryzon.mc.farmersdelight.client.render.block;

import com.nhoryzon.mc.farmersdelight.block.StoveBlock;
import com.nhoryzon.mc.farmersdelight.entity.block.StoveBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;

import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(value= EnvType.CLIENT)
public class StoveBlockEntityRenderer implements BlockEntityRenderer<StoveBlockEntity> {

    public StoveBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(StoveBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            int overlay) {
        Direction direction = entity.getCachedState().get(StoveBlock.FACING).getOpposite();
        DefaultedList<ItemStack> inventory = entity.getInventory();
        int intPos = (int)entity.getPos().asLong();

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack itemStack = inventory.get(i);
            if (!itemStack.isEmpty()) {
                matrices.push();
                matrices.translate(.5d, 1.02d, .5d);

                float angle = -direction.asRotation();
                matrices.multiply(new Quaternionf(0, 1, 0, 0).rotateY((float)Math.toDegrees(angle)));
                matrices.multiply(new Quaternionf(1, 0, 0, 0).rotateX((float)Math.toDegrees(90.f)));

                Vec2f itemOffset = entity.getStoveItemOffset(i);
                matrices.translate(itemOffset.x, itemOffset.y, .0d);
                matrices.scale(.375f, .375f, .375f);

                if (entity.getWorld() != null) {
                    int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
                    MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.FIXED, lightAbove,
                            overlay, matrices, vertexConsumers,intPos + i);
                }
                matrices.pop();
            }
        }
    }

}