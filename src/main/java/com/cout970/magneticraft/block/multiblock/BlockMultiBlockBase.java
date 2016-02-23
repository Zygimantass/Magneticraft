package com.cout970.magneticraft.block.multiblock;

import com.cout970.magneticraft.block.BlockBase;
import com.cout970.magneticraft.util.multiblock.IMultiBlockData;
import com.cout970.magneticraft.util.multiblock.MB_Block;
import net.darkaqua.blacksmith.api.common.block.IBlock;
import net.darkaqua.blacksmith.api.common.block.blockdata.BlockDataFactory;
import net.darkaqua.blacksmith.api.common.block.blockdata.IBlockAttribute;
import net.darkaqua.blacksmith.api.common.util.WorldRef;

import java.util.List;

/**
 * Created by cout970 on 15/01/2016.
 */
public abstract class BlockMultiBlockBase extends BlockBase implements MB_Block {

    public static IBlockAttribute<Boolean> ACTIVATION = BlockDataFactory.createBlockAttribute("activation", Boolean.class, new Boolean[]{true, false});

    @Override
    public void mutates(WorldRef ref, IMultiBlockData data) {
        ref.setBlockData(getBlock().getDefaultBlockData().setValue(ACTIVATION, true));
    }

    @Override
    public void destroy(WorldRef ref, IMultiBlockData data) {
        ref.setBlockData(getBlock().getDefaultBlockData().setValue(ACTIVATION, false));
    }

    public boolean matchesAny(WorldRef worldRef, List<IBlock> blocks){
        return blocks.contains(getBlock());
    }
}
