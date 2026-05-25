package me.alegian.framedblocksultimine;

import dev.ftb.mods.ftbultimine.api.rightclick.RegisterRightClickHandlerEvent;
import dev.ftb.mods.ftbultimine.api.rightclick.RightClickHandler;
import dev.ftb.mods.ftbultimine.shape.ShapeContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.common.Mod;
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

    return (int) positions.stream().filter(pos -> {
      var blockstate = shapeContext.block(pos);
      var block = blockstate.getBlock();
      if (!(block instanceof IFramedBlock framedBlock)) return false;

      var interaction = framedBlock.handleUse(
          blockstate,
          player.level(),
          pos,
          player,
          InteractionHand.MAIN_HAND,
          new BlockHitResult(pos.getCenter().add(relativeHitLocation), shapeContext.face(), pos, false)
      );
      return interaction.consumesAction();
    }).count();
  }
}
