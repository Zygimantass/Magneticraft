package com.cout970.magneticraft.tileentity.electric

import coffee.cypher.mcextlib.extensions.inventories.get
import com.cout970.magneticraft.api.energy.IElectricNode
import com.cout970.magneticraft.api.internal.energy.ElectricNode
import com.cout970.magneticraft.block.PROPERTY_DIRECTION
import com.cout970.magneticraft.config.Config
import com.cout970.magneticraft.registry.ITEM_ENERGY_CONSUMER
import com.cout970.magneticraft.registry.ITEM_ENERGY_PROVIDER
import com.cout970.magneticraft.registry.fromItem
import com.cout970.magneticraft.util.get
import com.cout970.magneticraft.util.misc.ValueAverage
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.ItemStackHandler

/**
 * Created by cout970 on 11/07/2016.
 */
class TileBattery : TileElectricBase() {

    var mainNode = ElectricNode({ world }, { pos }, capacity = 1.25)
    override val electricNodes: List<IElectricNode>
        get() = listOf(mainNode)
    var storage: Int = 0
    val inventory = ItemStackHandler(2)
    val chargeRate = ValueAverage(20)
    val itemChargeRate = ValueAverage(20)

    override fun update() {
        if (!worldObj.isRemote) {
            if (mainNode.voltage > UPPER_LIMIT) {
                val speed = interpolate(mainNode.voltage, UPPER_LIMIT, 120.0) * MAX_CHARGE_SPEED
                val finalSpeed = Math.min(Math.floor(speed).toInt(), Config.blockBatteryCapacity - storage)
                mainNode.applyPower(-finalSpeed.toDouble(), false)
                storage += finalSpeed
                chargeRate += finalSpeed
            } else if (mainNode.voltage < LOWER_LIMIT) {
                val speed = (1 - interpolate(mainNode.voltage, 60.0, LOWER_LIMIT)) * MAX_CHARGE_SPEED
                val finalSpeed = Math.min(Math.floor(speed).toInt(), storage)
                mainNode.applyPower(finalSpeed.toDouble(), false)
                storage -= finalSpeed
                chargeRate -= finalSpeed
            }
            chargeRate.tick()

            val toCharge = inventory[0]
            if(toCharge != null){
                val cap = ITEM_ENERGY_CONSUMER!!.fromItem(toCharge)
                if(cap != null){
                    val amount = Math.min(storage, Config.blockBatteryTransferRate)
                    //simulated
                    val given = cap.giveEnergy(amount.toDouble(), true)
                    //this avoid energy deletion creation when the battery has decimals in the energy value\
                    val floored = Math.floor(given)
                    if(floored > 0){
                        cap.giveEnergy(floored, false)
                        storage -= floored.toInt()
                        itemChargeRate -= floored
                    }
                }
            }

            val toDischarge = inventory[1]
            if(toDischarge != null){
                val cap = ITEM_ENERGY_PROVIDER!!.fromItem(toDischarge)
                if(cap != null){
                    val amount = Math.min(Config.blockBatteryCapacity - storage, Config.blockBatteryTransferRate)
                    //simulated
                    val taken = cap.takeEnergy(amount.toDouble(), true)
                    //this avoid energy deletion creation when the battery has decimals in the energy value
                    val floored = Math.floor(taken)
                    if(floored > 0){
                        cap.takeEnergy(floored, false)
                        storage += floored.toInt()
                        itemChargeRate += floored
                    }
                }
            }
            itemChargeRate.tick()
        }
        super.update()
    }

    override fun save(): NBTTagCompound {
        val nbt = NBTTagCompound()
        nbt.setInteger("storage", storage)
        nbt.setTag("inventory", inventory.serializeNBT())
        return nbt
    }

    override fun load(nbt: NBTTagCompound) {
        storage = nbt.getInteger("storage")
        inventory.deserializeNBT(nbt.getCompoundTag("inventory"))
    }

    fun getFacing(): EnumFacing {
        val state = world.getBlockState(pos)
        return PROPERTY_DIRECTION[state]
    }

    override fun canConnectAtSide(facing: EnumFacing?): Boolean {
        return facing == null || facing == getFacing()
    }

    companion object {
        //this is only used with voltage to charge the block, not for charging items
        val MAX_CHARGE_SPEED = 400
        val UPPER_LIMIT = 100.0
        val LOWER_LIMIT = 90.0
    }
}