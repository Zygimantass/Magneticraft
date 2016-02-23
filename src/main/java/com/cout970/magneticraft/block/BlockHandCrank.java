package com.cout970.magneticraft.block;

import com.cout970.magneticraft.Magneticraft;
import com.cout970.magneticraft.client.model.ModelConstants;
import com.cout970.magneticraft.tileentity.kinetic.generators.TileHandCrank;
import com.google.common.collect.Lists;
import net.darkaqua.blacksmith.api.common.block.IBlockContainerDefinition;
import net.darkaqua.blacksmith.api.common.block.blockdata.BlockDataFactory;
import net.darkaqua.blacksmith.api.common.block.blockdata.IBlockData;
import net.darkaqua.blacksmith.api.common.block.blockdata.IBlockDataHandler;
import net.darkaqua.blacksmith.api.common.block.methods.BlockMethod;
import net.darkaqua.blacksmith.api.common.entity.IEntity;
import net.darkaqua.blacksmith.api.common.entity.ILivingEntity;
import net.darkaqua.blacksmith.api.common.entity.IPlayer;
import net.darkaqua.blacksmith.api.client.render.block.IBlockModelProvider;
import net.darkaqua.blacksmith.api.client.render.block.defaults.SimpleBlockModelProvider;
import net.darkaqua.blacksmith.api.client.render.item.defaults.SimpleItemBlockModelProvider;
import net.darkaqua.blacksmith.api.common.tileentity.ITileEntity;
import net.darkaqua.blacksmith.api.common.tileentity.ITileEntityDefinition;
import net.darkaqua.blacksmith.api.common.util.raytrace.Cube;
import net.darkaqua.blacksmith.api.common.util.Direction;
import net.darkaqua.blacksmith.api.common.util.vectors.Vect3d;
import net.darkaqua.blacksmith.api.common.util.WorldRef;
import net.darkaqua.blacksmith.api.common.world.IWorld;

import java.util.List;

/**
 * Created by cout970 on 03/01/2016.
 */
public class BlockHandCrank extends BlockModeled implements IBlockContainerDefinition, BlockMethod.OnPlaced, BlockMethod.OnActivated {

    @Override
    public String getBlockName() {
        return "hand_crank";
    }

    @Override
    public ITileEntityDefinition createTileEntity(IWorld world, IBlockData state) {
        return new TileHandCrank();
    }

    public IBlockModelProvider getModelProvider() {
        return new SimpleItemBlockModelProvider(iModelRegistry -> new SimpleBlockModelProvider.BlockModel(
                iModelRegistry.registerModelPart(Magneticraft.IDENTIFIER, ModelConstants.HAND_CRANK)));
    }

    @Override
    public IBlockDataHandler getBlockDataGenerator() {
        return BlockDataFactory.createBlockDataHandler(parent, BlockDataFactory.ATTRIBUTE_ALL_DIRECTIONS);
    }

    @Override
    public IBlockData translateMetadataToVariant(int meta) {
        return parent.getDefaultBlockData().getBlockDataHandler().setValue(parent.getDefaultBlockData(), BlockDataFactory.ATTRIBUTE_ALL_DIRECTIONS, Direction.getDirection(meta));
    }

    @Override
    public int translateVariantToMetadata(IBlockData variant) {
        return variant.getValue(BlockDataFactory.ATTRIBUTE_ALL_DIRECTIONS).ordinal();
    }

    @Override
    public IBlockData onPlaced(WorldRef ref, Direction side, ILivingEntity entity, Vect3d hit, int metadata) {
        IBlockData state = parent.getDefaultBlockData();
        state = state.getBlockDataHandler().setValue(state, BlockDataFactory.ATTRIBUTE_ALL_DIRECTIONS, side.opposite());
        return state;
    }

    @Override
    public boolean onActivated(WorldRef ref, IBlockData state, IPlayer player, Direction side, Vect3d vector3d) {
        ITileEntity tile = ref.getTileEntity();
        if(tile.getTileEntityDefinition() instanceof TileHandCrank){
            ((TileHandCrank) tile.getTileEntityDefinition()).resetCounter();
            return true;
        }
        return false;
    }

    public Cube getSelectionCube(WorldRef ref){
        return getBounds(ref);
    }

    public List<Cube> getCollisionCubes(WorldRef ref, IEntity entity) {
        return Lists.newArrayList(getBounds(ref));
    }

    public Cube getBounds(WorldRef ref){
        ITileEntity tile = ref.getTileEntity();
        if(tile.getTileEntityDefinition() instanceof TileHandCrank){
            Direction dir = ((TileHandCrank) tile.getTileEntityDefinition()).getDirection();
            double size = 2/16d;
            Vect3d min = new Vect3d(0.5-size, 0.5-size, 0.5-size);
            Vect3d max = new Vect3d(0.5+size, 0.5+size, 0.5+size);
            Cube base = new Cube(min, max);

            return base.extend(dir, 0.5-size);
        }
        return Cube.fullBlock();
    }
}
