package com.narylr.narylrmod.block;

import com.narylr.narylrmod.screen.SteelFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

//钢熔炉方块
public class SteelFurnaceBlock extends Block {
    //声明水平朝向属性
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    //构造方法里设置默认朝向
    public SteelFurnaceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
    }

    //告诉方块状态系统有FACING这个属性
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    //玩家放置时，让正面朝向玩家
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    //右键打开gui
    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (!level.isClientSide()){
            player.sendSystemMessage(Component.literal( "右键了钢熔炉"));
            player.openMenu(new SimpleMenuProvider(new MenuConstructor() {
                @Override
                public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                    return new SteelFurnaceMenu(containerId, playerInventory);
                }
            },Component.translatable("container.narylr-mod.steel_furnace")));
        }
        return InteractionResult.SUCCESS;
    }
}
