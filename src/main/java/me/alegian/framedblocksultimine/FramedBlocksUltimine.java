package me.alegian.framedblocksultimine;

import dev.ftb.mods.ftbultimine.api.rightclick.RegisterRightClickHandlerEvent;
import dev.ftb.mods.ftbultimine.api.rightclick.RightClickHandler;
import dev.ftb.mods.ftbultimine.shape.ShapeContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Collection;

@Mod(FramedBlocksUltimine.MODID)
public class FramedBlocksUltimine {
  public static final String MODID = "framed_blocks_ultimine";

  public FramedBlocksUltimine(FMLJavaModLoadingContext context) {
    RegisterRightClickHandlerEvent.REGISTER.register(dispatcher ->
        dispatcher.registerHandler(FramedBlocksRightClickHandler.INSTANCE)
    );
  }
}


enum FramedBlocksRightClickHandler implements RightClickHandler {
  INSTANCE;

  @Override
  public int handleRightClickBlock(ShapeContext shapeContext, InteractionHand hand, Collection<BlockPos> positions) {
    return 0;
  }
}
