package com.cout970.magneticraft.block

import com.cout970.magneticraft.block.states.BlockLimestoneStates
import com.cout970.magneticraft.block.states.BlockProperties
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

/**
 * Created by cout970 on 11/06/2016.
 */
object BlockBurnLimestone : BlockMultiState(Material.ROCK, "burn_limestone") {

    override fun getMaxModels():Int = 3

    override fun getUnlocalizedName(stack: ItemStack?): String? {
        return unlocalizedName +"."+ deserializeState(stack?.metadata ?: 0)
                .getValue(BlockProperties.blockBurnLimestoneState).getName()
    }

    override fun shouldItemBeDisplayed(itemIn: Item, tab: CreativeTabs, i: Int): Boolean = i < 3

    override fun getProperties(): Array<IProperty<*>> = arrayOf(BlockProperties.blockBurnLimestoneState)

    override fun deserializeState(meta: Int): IBlockState {
        return blockState.baseState.withProperty(BlockProperties.blockBurnLimestoneState,
                BlockLimestoneStates.values()[meta])
    }

    override fun serializeState(state: IBlockState): Int {
        return state.getValue(BlockProperties.blockBurnLimestoneState).ordinal
    }
}