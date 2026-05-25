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
    return (int) positions.stream().filter(pos -> {
      var blockstate = shapeContext.block(pos);
      var block = blockstate.getBlock();
      var player = shapeContext.player();
      if (!(block instanceof IFramedBlock framedBlock)) return false;

      var interaction = framedBlock.handleUse(
          blockstate,
          player.level(),
          pos,
          player,
          InteractionHand.MAIN_HAND,
          new BlockHitResult(pos.getCenter(), shapeContext.face(), pos, false)
      );
      return interaction.consumesAction();
    }).count();
  }
}
