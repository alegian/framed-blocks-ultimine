package me.alegian.framedblocksultimine;

import dev.ftb.mods.ftbultimine.api.rightclick.RegisterRightClickHandlerEvent;
import dev.ftb.mods.ftbultimine.api.rightclick.RightClickHandler;
import dev.ftb.mods.ftbultimine.shape.ShapeContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import xfacthd.framedblocks.api.block.IFramedBlock;

import java.util.Collection;

@Mod("framed_blocks_ultimine")
public class FramedBlocksUltimine {
  public FramedBlocksUltimine() {
    RegisterRightClickHandlerEvent.REGISTER.register(dispatcher ->
        dispatcher.registerHandler(FramedBlocksRightClickHandler.INSTANCE)
    );
  }
}


enum FramedBlocksRightClickHandler implements RightClickHandler {
  INSTANCE;

  @Override
  public int handleRightClickBlock(ShapeContext shapeContext, InteractionHand hand, Collection<BlockPos> positions) {
    var player = shapeContext.player();
    var hitResult = player.pick(player.getBlockReach(), 0f, false);
    if (!(hitResult instanceof BlockHitResult blockHitResult)) return 0;
    var relativeHitLocation = blockHitResult.getLocation().subtract(
        blockHitResult.getBlockPos().getCenter()
    );
    var hitFace = blockHitResult.getDirection();
    var originalFace = getBlockstateDirection(player.level().getBlockState(blockHitResult.getBlockPos()));

    return (int) positions.stream().filter(pos -> {
      var blockstate = shapeContext.block(pos);
      var block = blockstate.getBlock();
      if (!(block instanceof IFramedBlock framedBlock)) return false;

      var facing = getBlockstateDirection(blockstate);

      var rotatedHitLocation = new Vec3(relativeHitLocation.toVector3f().rotate(
          rotation(originalFace, facing)
      ));

      var rotatedFace = rotateDirection(hitFace, originalFace, facing);

      var interaction = framedBlock.handleUse(
          blockstate,
          player.level(),
          pos,
          player,
          hand,
          new BlockHitResult(pos.getCenter().add(rotatedHitLocation), rotatedFace, pos, false)
      );
      return interaction.consumesAction();
    }).count();
  }

  private static Quaternionf rotation(Direction from, Direction to) {
    return to.getRotation().mul(from.getRotation().invert(new Quaternionf()));
  }

  private static Direction rotateDirection(Direction dir, Direction from, Direction to) {
    Vector3f normal = dir.step();
    normal.rotate(rotation(from, to));

    return Direction.getNearest(normal.x(), normal.y(), normal.z());
  }

  private static Direction getBlockstateDirection(BlockState state) {
    if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
      return state.getValue(BlockStateProperties.HORIZONTAL_FACING);

    return Direction.NORTH;
  }
}
