package com.nhoryzon.mc.farmersdelight.block;

import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.util.BlockStateUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class FeastBlock extends Block {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty SERVINGS = IntProperty.of("servings", 0, 4);

    protected static final VoxelShape[] SHAPES = new VoxelShape[] {
            Block.createCuboidShape(2.d, 0.d, 2.d, 14.d, 1.d, 14.d),
            Block.createCuboidShape(2.d, 0.d, 2.d, 14.d, 3.d, 14.d),
            Block.createCuboidShape(2.d, 0.d, 2.d, 14.d, 6.d, 14.d),
            Block.createCuboidShape(2.d, 0.d, 2.d, 14.d, 8.d, 14.d),
            Block.createCuboidShape(2.d, 0.d, 2.d, 14.d, 10.d, 14.d),
    };

    public final Item servingItem;
    public final boolean hasLeftovers;

    public FeastBlock(Settings settings, Item servingItem, boolean hasLeftovers) {
        super(settings);
        this.servingItem = servingItem;
        this.hasLeftovers = hasLeftovers;
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(getServingsProperty(), getMaxServings()));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState().with(FACING, context.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, SERVINGS);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient() && takeServing(world, pos, state, player, hand).isAccepted()) {
            return ActionResult.SUCCESS;
        }

        return takeServing(world, pos, state, player, hand);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).getMaterial().isSolid();
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos,
            BlockPos posFrom) {
        return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState()
                : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(getServingsProperty());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[state.get(SERVINGS)];
    }

    public IntProperty getServingsProperty() {
        return SERVINGS;
    }

    public int getMaxServings() {
        return 4;
    }

    public ItemStack getServingStack(BlockState state) {
        return new ItemStack(servingItem);
    }

    @SuppressWarnings("ConstantConditions")
    private ActionResult takeServing(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand) {
        int servings = state.get(getServingsProperty());

        if (servings == 0) {
            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.PLAYERS, .8f, .8f);
            world.breakBlock(pos, true);

            return ActionResult.SUCCESS;
        }

        ItemStack serving = getServingStack(state);
        ItemStack heldItem = player.getStackInHand(hand);

        if (servings > 0) {
            // Since we can't ignore damange when comparing anymore
            // create a new object and make the damage the same as the held item
            ItemStack compare = new ItemStack(serving.getItem().getRecipeRemainder());
            compare.setDamage(heldItem.getDamage());
            if (!serving.getItem().hasRecipeRemainder() || heldItem.isItemEqual(compare)) {
                world.setBlockState(pos, state.with(getServingsProperty(), servings - 1), BlockStateUtils.DEFAULT);
                if (!player.getAbilities().creativeMode && serving.getItem().hasRecipeRemainder()) {
                    heldItem.decrement(1);
                }
                if (!player.getInventory().insertStack(serving)) {
                    player.dropItem(serving, false);
                }
                if (world.getBlockState(pos).get(getServingsProperty()) == 0 && !hasLeftovers) {
                    world.removeBlock(pos, false);
                }

                world.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.f, 1.f);

                return ActionResult.SUCCESS;
            } else {
                player.sendMessage(FarmersDelightMod.i18n("block.feast.use_container", serving.getItem().getRecipeRemainder().getName()), true);
            }
        }

        return ActionResult.PASS;
    }

}