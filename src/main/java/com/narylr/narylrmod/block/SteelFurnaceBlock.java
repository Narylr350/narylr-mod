package com.narylr.narylrmod.block;

import com.mojang.serialization.MapCodec;
import com.narylr.narylrmod.block.entity.SteelFurnaceBlockEntity;
import com.narylr.narylrmod.screen.SteelFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

//钢熔炉方块
public class SteelFurnaceBlock extends BaseEntityBlock {
    //声明水平朝向属性
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final MapCodec<SteelFurnaceBlock> CODEC = simpleCodec(SteelFurnaceBlock::new);

    //构造方法里设置默认朝向
    public SteelFurnaceBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().
                setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    //告诉方块状态系统有FACING这个属性
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    //玩家放置时，让正面朝向玩家
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().
                setValue(FACING, blockPlaceContext.getHorizontalDirection()
                        .getOpposite());
    }

    //右键打开gui
    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof SteelFurnaceBlockEntity steelFurnaceBlockEntity) {
                player.sendSystemMessage(Component.literal("右键了钢熔炉方块实体"));
                player.openMenu(new SimpleMenuProvider(new MenuConstructor() {
                    @Override
                    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                        return new SteelFurnaceMenu(containerId, playerInventory, steelFurnaceBlockEntity);
                    }
                }, Component.translatable("container.narylr-mod.steel_furnace")));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SteelFurnaceBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean movedByPiston
    ) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof SteelFurnaceBlockEntity steelFurnaceBlockEntity) {
                Containers.dropContents(level, pos, steelFurnaceBlockEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
