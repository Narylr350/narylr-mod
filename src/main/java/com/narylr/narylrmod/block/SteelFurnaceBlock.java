package com.narylr.narylrmod.block;

import com.mojang.serialization.MapCodec;
import com.narylr.narylrmod.block.entity.ModBlockEntities;
import com.narylr.narylrmod.block.entity.SteelFurnaceBlockEntity;
import com.narylr.narylrmod.screen.SteelFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

//钢熔炉方块
public class SteelFurnaceBlock extends BaseEntityBlock {
    //声明水平朝向属性
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final MapCodec<SteelFurnaceBlock> CODEC = simpleCodec(SteelFurnaceBlock::new);

    //熔炉状态
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    //构造方法里设置默认朝向,LIT
    public SteelFurnaceBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any()
                .setValue(LIT,false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    //告诉方块状态系统有FACING,LIT这个属性
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING,LIT);
    }

    //玩家放置时，让正面朝向玩家
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
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
                player.openMenu(new SimpleMenuProvider((containerId, playerInventory, player1) -> new SteelFurnaceMenu(containerId, playerInventory, steelFurnaceBlockEntity), Component.translatable("container.narylr-mod.steel_furnace")));
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
                steelFurnaceBlockEntity.dropStoredExperience();
                Containers.dropContents(level, pos, steelFurnaceBlockEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> blockEntityType
    ) {
        if (level.isClientSide){
            return null;
        }

        return createTickerHelper(
                blockEntityType,
                ModBlockEntities.STEEL_FURNACE_BLOCK_ENTITY,
                SteelFurnaceBlockEntity::serverTick
        );
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);

        if (!level.isClientSide && state.getValue(LIT) && entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().hotFloor(),1.0F);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;

        if (random.nextDouble() < 0.1) {
            level.playLocalSound(
                    x,
                    y,
                    z,
                    SoundEvents.FURNACE_FIRE_CRACKLE,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F,
                    false
            );
        }

        Direction direction = state.getValue(FACING);
        Direction.Axis axis = direction.getAxis();

        double frontOffset = 0.52;
        double sideRandom = random.nextDouble() * 0.6 - 0.3;

        double particleX = x + direction.getStepX() * frontOffset;
        double particleY = y + random.nextDouble() * 0.375 + 0.4;
        double particleZ = z + direction.getStepZ() * frontOffset;

        if (axis == Direction.Axis.X) {
            particleZ += sideRandom;
        } else {
            particleX += sideRandom;
        }

        level.addParticle(
                ParticleTypes.SMOKE,
                particleX,
                particleY,
                particleZ,
                0.0,
                0.0,
                0.0
        );

        level.addParticle(
                ParticleTypes.FLAME,
                particleX,
                particleY,
                particleZ,
                0.0,
                0.0,
                0.0
        );

        double topSmokeX = x + random.nextDouble() * 0.4 - 0.2;
        double topSmokeY = pos.getY() + 1.05;
        double topSmokeZ = z + random.nextDouble() * 0.4 - 0.2;

        level.addParticle(
                ParticleTypes.SMOKE,
                topSmokeX,
                topSmokeY,
                topSmokeZ,
                0.0,
                0.03,
                0.0
        );
    }
}
