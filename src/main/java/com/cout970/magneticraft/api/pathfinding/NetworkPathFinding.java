package com.cout970.magneticraft.api.pathfinding;

import com.cout970.magneticraft.api.network.INetworkNode;
import com.cout970.magneticraft.api.network.Network;
import net.darkaqua.blacksmith.scanner.ObjectScanner;
import net.darkaqua.blacksmith.util.WorldRef;
import net.darkaqua.blacksmith.vectors.Vect3i;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;

public class NetworkPathFinding<T extends INetworkNode> extends PathFinding {
    Capability id;
    Network<T> network;

    /**
     * Initializes a new PathFinding instance that will work on given field, starting with a block at given position.
     *
     * @param net   {@link Network} to check nodes against
     * @param ba    {@link World} that will provide information about blocks for the algorithm
     * @param start {@link Vect3i} containing coordinates of a start block.
     */
    public NetworkPathFinding(Network<T> net, World ba, Vect3i start) {
        super(ba, start);
        network = net;
    }

    public NetworkPathFinding(Network<T> net, WorldRef w) {
        this(net, w.getWorld(), w.getPosition());
    }

    @Override
    protected boolean hasFailed() {
        return super.hasFailed() || (scanned.size() > 4000) || (toScan.size() > 10000);
    }

    @Override
    public boolean hasGoal() {
        return false;
    }

    @Override
    public boolean isGoal(PathNode node) {
        return false;
    }

    @Override
    public Result getResult() {
        if (!isDone()) {
            return null;
        }

        return new Result(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isPath(PathNode node) {
        TileEntity tile = field.getTileEntity(node.getPosition().toBlockPos());
        if (node.getBefore() == null) {
            T n = ObjectScanner.findInTileEntity(tile, network.getInterfaceIdentifier(), null);
            return network.canAddToNetwork(n);
        } else {
            Vect3i dir = node.getPosition().copy().sub(node.getBefore().getPosition());
            T n = ObjectScanner.findInTileEntity(tile, network.getInterfaceIdentifier(), dir.toDirection());
            TileEntity oldTile = field.getTileEntity(node.getBefore().getPosition().toBlockPos());
            T old = ObjectScanner.findInTileEntity(oldTile, network.getInterfaceIdentifier(), null);
            return old != null && n != null && network.canAddToNetwork(n) && n.isAbleToConnect(old, dir) && old.isAbleToConnect(n, dir.getOpposite());
        }
    }

    public class Result extends PathFinding.Result {
        private List<T> nodes;

        public Result(NetworkPathFinding<T> p) {
            super(p);
            nodes = new ArrayList<>();
            getAllScanned().stream()
                    .map(Vect3i::toBlockPos)
                    .map(p.field::getTileEntity)
                    .map(t -> ObjectScanner.findInTileEntity(t, p.network.getInterfaceIdentifier(), null))
                    .forEach(nodes::add);
        }

        public List<T> getNodes() {
            return nodes;
        }
    }
}