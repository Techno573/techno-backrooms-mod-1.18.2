package net.techno573.backrooms.blocks.advanced;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.techno573.backrooms.sounds.ModTravelSound;
import net.techno573.backrooms.world.ModWorld;

public class Lvl0PortalBlock extends Block {
    public Lvl0PortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world instanceof ServerWorld && !entity.hasVehicle() && entity.isPlayer() && !entity.hasPassengers() && entity.canUsePortals() && VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity.getBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ())), state.getOutlineShape(world, pos), BooleanBiFunction.AND)) {
            RegistryKey<World> registryKey = world.getRegistryKey() == ModWorld.LEVEL_1_WORLD.worldWorldRegistryKey ? ModWorld.LEVEL_0_WORLD.worldWorldRegistryKey : ModWorld.LEVEL_1_WORLD.worldWorldRegistryKey;
            ServerWorld serverWorld = ((ServerWorld)world).getServer().getWorld(registryKey);
            if (serverWorld == null) {
                return;
            }

            ModTravelSound.isTraveling = true;
            FabricDimensions.teleport(
                    entity,
                    serverWorld,
                    new TeleportTarget(
                            new Vec3d(entity.getPos().x + 1,5,entity.getPos().z + 1),
                            new Vec3d(0,0,0),
                            entity.getYaw(),
                            entity.getPitch()
                    )
            );
        }
    }
}
