package com.narylr.narylrmod.block.entity;

import com.narylr.narylrmod.block.SteelFurnaceBlock;
import com.narylr.narylrmod.recipe.ModRecipes;
import com.narylr.narylrmod.recipe.SteelFurnaceRecipe;
import com.narylr.narylrmod.recipe.SteelFurnaceRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class SteelFurnaceBlockEntity extends BlockEntity implements Container {
    // 当前已经烧了多久（单位：游戏刻）
    private int progress = 0;
    // 总共要烧多久才能完成当前的配方（单位：游戏刻）
    private int maxProgress = 0;
    // 当前燃料燃烧时间（单位：游戏刻）
    private int burnTime = 0;
    // 燃料总共可以燃烧的时间（单位：游戏刻）
    private int maxBurnTime = 0;
    // 模式常量 - 表示钢熔炉处于空闲状态
    private static final int MODE_IDLE = 0;
    // 模式常量 - 表示钢熔炉正在处理钢铁配方
    private static final int MODE_STEEL = 1;
    // 模式常量 - 表示钢熔炉正在处理熔炼配方
    private static final int MODE_SMELTING = 2;
    // 钢熔炉当前的工作模式
    private int mode = MODE_IDLE;
    // 当前机器已经积累但还没有发给玩家的经验值
    private float experienceStored = 0.0F;
    // 当前使用的钢铁配方
    private SteelFurnaceRecipe currentSteelRecipe;
    // 当前使用的熔炼配方
    private RecipeHolder<SmeltingRecipe> currentSmeltingRecipe;
    // 钢熔炉中的物品槽位列表，包含3个槽位
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    // 同步到客户端的数据容器，用于存储进度、最大进度、燃烧时间和最大燃烧时间
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> maxProgress;
                case 2 -> burnTime;
                case 3 -> maxBurnTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> maxProgress = value;
                case 2 -> burnTime = value;
                case 3 -> maxBurnTime = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public SteelFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.STEEL_FURNACE_BLOCK_ENTITY, pos, blockState);
    }

    public ContainerData getData() {
        return data;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SteelFurnaceBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }

        boolean working = blockEntity.tickWork();

        if (state.getValue(SteelFurnaceBlock.LIT) != working) {
            level.setBlock(pos, state.setValue(SteelFurnaceBlock.LIT, working), 3);
        }

        setChanged(level, pos, state);
    }

    // 根据当前工作模式处理机器逻辑，炼钢配方拥有最高优先级
    private boolean tickWork() {
        if (mode == MODE_SMELTING && canStartSteelRecipe()) {
            stopWorking();
            return tryStartSteelRecipe();
        }

        if (mode == MODE_IDLE) {
            return tryStartSteelRecipe() || tryStartSmeltingRecipe();
        }

        if (mode == MODE_STEEL) {
            return processSteelRecipe();
        }

        if (mode == MODE_SMELTING) {
            return processSmeltingRecipe();
        }

        stopWorking();
        return false;
    }

    private boolean tryStartSteelRecipe() {
        SteelFurnaceRecipe recipe = findSteelRecipeWithCoal();

        if (recipe == null || !canOutput(recipe.result())) {
            return false;
        }

        ItemStack coal = items.get(1);
        coal.shrink(recipe.coalCount());

        currentSteelRecipe = recipe;
        currentSmeltingRecipe = null;

        mode = MODE_STEEL;
        progress = 0;
        maxProgress = recipe.cookingTime();
        burnTime = recipe.cookingTime();
        maxBurnTime = recipe.cookingTime();

        playSteelStartSound();

        setChanged();
        return true;
    }

    // 播放钢熔炉开始炼钢时的重型点火声音
    private void playSteelStartSound() {
        if (level == null || level.isClientSide) {
            return;
        }

        level.playSound(
                null,
                worldPosition,
                SoundEvents.FIRECHARGE_USE,
                SoundSource.BLOCKS,
                0.8F,
                0.65F
        );
    }

    private SteelFurnaceRecipe findSteelRecipeWithCoal() {
        if (level == null) {
            return null;
        }

        SteelFurnaceRecipeInput recipeInput = new SteelFurnaceRecipeInput(items.get(0), items.get(1));

        Optional<RecipeHolder<SteelFurnaceRecipe>> recipeHolder =
                level.getRecipeManager().getRecipeFor(ModRecipes.STEEL_FURNACING_TYPE, recipeInput, level);

        return recipeHolder.map(RecipeHolder::value).orElse(null);
    }

    // 判断当前输入槽和燃料槽是否已经满足炼钢配方
    private boolean canStartSteelRecipe() {
        SteelFurnaceRecipe recipe = findSteelRecipeWithCoal();

        return recipe != null && canOutput(recipe.result());
    }

    private SteelFurnaceRecipe findSteelRecipeByInputOnly() {
        if (level == null) {
            return null;
        }

        ItemStack input = items.get(0);

        return level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.STEEL_FURNACING_TYPE)
                .stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.ingredient().test(input))
                .findFirst()
                .orElse(null);
    }

    private boolean processSteelRecipe() {
        if (burnTime <= 0) {
            stopWorking();
            return false;
        }

        SteelFurnaceRecipe recipe = currentSteelRecipe;

        if (recipe == null) {
            recipe = findSteelRecipeByInputOnly();
            currentSteelRecipe = recipe;
        }

        if (recipe == null || !canOutput(recipe.result())) {
            stopWorking();
            return false;
        }

        maxProgress = recipe.cookingTime();

        burnTime--;
        progress++;

        if (progress >= maxProgress) {
            finishSteelRecipe();
            stopWorking();
            return tryStartSteelRecipe();
        }

        setChanged();
        return true;
    }

    // 完成炼钢配方，消耗输入物并把产物放入输出槽
    private void finishSteelRecipe() {
        if (currentSteelRecipe == null) {
            return;
        }

        ItemStack input = items.get(0);
        ItemStack output = items.get(2);
        ItemStack result = currentSteelRecipe.result().copy();

        input.shrink(1);
        experienceStored += currentSteelRecipe.experience();

        if (output.isEmpty()) {
            items.set(2, result);
        } else {
            output.grow(result.getCount());
        }

        setChanged();
    }

    private RecipeHolder<SmeltingRecipe> findSmeltingRecipe() {
        if (level == null) {
            return null;
        }

        SingleRecipeInput recipeInput = new SingleRecipeInput(items.get(0));

        Optional<RecipeHolder<SmeltingRecipe>> recipeHolder =
                level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, recipeInput, level);

        return recipeHolder.orElse(null);
    }

    private boolean tryStartSmeltingRecipe() {
        RecipeHolder<SmeltingRecipe> recipe = findSmeltingRecipe();

        if (recipe == null) {
            return false;
        }

        ItemStack result = recipe.value().getResultItem(level.registryAccess());

        if (!canOutput(result)) {
            return false;
        }

        if (!consumeFuel()) {
            return false;
        }

        currentSteelRecipe = null;
        currentSmeltingRecipe = recipe;

        mode = MODE_SMELTING;
        progress = 0;
        maxProgress = Math.max(recipe.value().getCookingTime() / 2, 1);

        setChanged();
        return true;
    }

    // 处理普通熔炼模式：燃料会持续燃烧，没有可熔炼输入时只消耗火焰，不推进进度
    private boolean processSmeltingRecipe() {
        if (burnTime <= 0) {
            stopWorking();
            return false;
        }

        burnTime--;

        RecipeHolder<SmeltingRecipe> recipe = findSmeltingRecipe();

        if (recipe == null) {
            progress = 0;
            maxProgress = 0;

            if (burnTime <= 0) {
                stopWorking();
                return false;
            }

            setChanged();
            return true;
        }

        ItemStack result = recipe.value().getResultItem(level.registryAccess());

        if (!canOutput(result)) {
            progress = 0;
            maxProgress = Math.max(recipe.value().getCookingTime() / 2, 1);

            if (burnTime <= 0) {
                stopWorking();
                return false;
            }

            setChanged();
            return true;
        }

        currentSmeltingRecipe = recipe;
        maxProgress = Math.max(recipe.value().getCookingTime() / 2, 1);

        progress++;

        if (progress >= maxProgress) {
            finishSmeltingRecipe();
            progress = 0;
        }

        if (burnTime <= 0) {
            stopWorking();
            return tryStartSteelRecipe() || tryStartSmeltingRecipe();
        }

        setChanged();
        return true;
    }

    private boolean consumeFuel() {
        ItemStack fuel = items.get(1);

        if (fuel.isEmpty()) {
            return false;
        }

        int fuelTime = AbstractFurnaceBlockEntity.getFuel().getOrDefault(fuel.getItem(), 0);

        if (fuelTime <= 0) {
            return false;
        }

        burnTime = fuelTime;
        maxBurnTime = fuelTime;

        fuel.shrink(1);
        return true;
    }

    // 完成普通熔炼配方，消耗输入物并把产物放入输出槽
    private void finishSmeltingRecipe() {
        if (level == null || currentSmeltingRecipe == null) {
            return;
        }

        ItemStack input = items.get(0);
        ItemStack output = items.get(2);
        ItemStack result = currentSmeltingRecipe.value().getResultItem(level.registryAccess()).copy();

        input.shrink(1);
        experienceStored += currentSmeltingRecipe.value().getExperience();

        if (output.isEmpty()) {
            items.set(2, result);
        } else {
            output.grow(result.getCount());
        }

        setChanged();
    }

    private void stopWorking() {
        mode = MODE_IDLE;

        progress = 0;
        maxProgress = 0;
        burnTime = 0;
        maxBurnTime = 0;

        currentSteelRecipe = null;
        currentSmeltingRecipe = null;

        setChanged();
    }

    private boolean canOutput(ItemStack result) {
        ItemStack output = items.get(2);

        if (result.isEmpty()) {
            return false;
        }

        if (output.isEmpty()) {
            return true;
        }

        return ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    // 根据小数经验计算最终应该生成多少经验球
    private int getExperienceAmount() {
        int experience = Mth.floor(experienceStored);
        float fraction = experienceStored - experience;

        if (fraction > 0.0F && level != null && level.random.nextFloat() < fraction) {
            experience++;
        }

        return experience;
    }

    // 玩家取出输出槽产物时，把机器积累的经验发给玩家
    public void awardStoredExperience(Player player) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        int experience = getExperienceAmount();

        if (experience <= 0) {
            return;
        }

        ExperienceOrb.award(serverLevel, player.position(), experience);

        experienceStored = 0.0F;
        setChanged();
    }

    // 方块被破坏时，把机器积累的经验释放到方块位置
    public void dropStoredExperience() {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        int experience = getExperienceAmount();

        if (experience <= 0) {
            return;
        }

        ExperienceOrb.award(
                serverLevel,
                worldPosition.getCenter(),
                experience
        );

        experienceStored = 0.0F;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putInt("Progress", progress);
        tag.putInt("MaxProgress", maxProgress);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("MaxBurnTime", maxBurnTime);
        tag.putFloat("ExperienceStored", experienceStored);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        ContainerHelper.loadAllItems(tag, items, registries);
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        burnTime = tag.getInt("BurnTime");
        maxBurnTime = tag.getInt("MaxBurnTime");
        mode = tag.getInt("Mode");
        experienceStored = tag.getFloat("ExperienceStored");
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack item : items) {
            if (!item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack itemStack = ContainerHelper.removeItem(items, slot, amount);
        if (!itemStack.isEmpty()) {
            setChanged();
        }
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        items.set(slot, itemStack);

        if (itemStack.getCount() > getMaxStackSize()) {
            itemStack.setCount(getMaxStackSize());
        }

        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null) {
            return false;
        }

        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5
        ) <= 64;
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    // 获取钢熔炉当前工作模式
    public int getMode() {
        return mode;
    }

    // 判断钢熔炉当前是否处于炼钢模式
    public boolean isSteelMode() {
        return mode == MODE_STEEL;
    }

    // 判断钢熔炉当前是否处于原版熔炼模式
    public boolean isSmeltingMode() {
        return mode == MODE_SMELTING;
    }

    // 获取当前处理进度
    public int getProgress() {
        return progress;
    }

    // 获取当前处理最大进度
    public int getMaxProgress() {
        return maxProgress;
    }

    // 获取当前剩余燃烧时间
    public int getBurnTime() {
        return burnTime;
    }

    // 获取当前燃料最大燃烧时间
    public int getMaxBurnTime() {
        return maxBurnTime;
    }
}
