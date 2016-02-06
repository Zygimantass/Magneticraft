package com.cout970.magneticraft.client.tilerender;

import com.cout970.magneticraft.Magneticraft;
import com.cout970.magneticraft.client.model.ModelConstants;
import com.cout970.magneticraft.tileentity.kinetic.generators.TileHandCrank;
import net.darkaqua.blacksmith.api.render.model.IDynamicModel;
import net.darkaqua.blacksmith.api.render.techne.TechneDynamicModel;
import net.darkaqua.blacksmith.api.render.tileentity.ITileEntityRendererHelper;
import net.darkaqua.blacksmith.api.tileentity.ITileEntity;
import net.darkaqua.blacksmith.api.util.Vect3d;
import org.lwjgl.opengl.GL11;

/**
 * Created by cout970 on 19/01/2016.
 */
public class TileRenderHandCrank extends TileEntityRenderer<TileHandCrank> {

    private static IDynamicModel model;
    private static IDynamicModel.IPartSet base;
    private static IDynamicModel.IPartSet handle;


    @Override
    public void renderTileEntity(ITileEntity tile, TileHandCrank def, ITileEntityRendererHelper helper, Vect3d pos, float partialTick, int breakingProgress) {
        helper.disableStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, (float) pos.getZ() + 0.5F);
        float rotation = -def.getRotationAngle(partialTick);

        GL11.glPushMatrix();
        switch (def.getDirection()){
            case DOWN:
                GL11.glTranslatef(0, -0.5f, 0);
                GL11.glRotatef(rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, 0, -0.5f);
                break;
            case UP:
                GL11.glTranslatef(0, 0.5f-0.0625f, 0);
                GL11.glRotatef(rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, 0, -0.5f);
                break;
            case NORTH:
                GL11.glRotatef(90, 1, 0, 0);
                GL11.glRotatef(rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                break;
            case SOUTH:
                GL11.glRotatef(90, 1, 0, 0);
                GL11.glRotatef(rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, 0.5f-0.0625f, -0.5f);
                break;
            case WEST:
                GL11.glRotatef(-90, 0, 0, 1);
                GL11.glRotatef(rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                break;
            case EAST:
                GL11.glRotatef(-90, 0, 0, 1);
                GL11.glRotatef(rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, 0.5f-0.0625f, -0.5f);
                break;
        }

        model.renderPartSet(base);
        GL11.glPopMatrix();
        switch (def.getDirection()){
            case DOWN:
                GL11.glTranslatef(0, -0.5f, 0);
                GL11.glRotatef(rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, 0, -0.5f);
                break;
            case UP:
                GL11.glRotatef(180, 1, 0, 0);
                GL11.glTranslatef(0, 0.5f-0.0625f, 0);
                GL11.glRotatef(-rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, -1f, -0.5f);
                break;
            case NORTH:
                GL11.glRotatef(90, 1, 0, 0);
                GL11.glRotatef(rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                break;
            case SOUTH:
                GL11.glRotatef(-90, 1, 0, 0);
                GL11.glRotatef(-rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                break;
            case WEST:
                GL11.glRotatef(-90, 0, 0, 1);
                GL11.glRotatef(rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                break;
            case EAST:
                GL11.glRotatef(90, 0, 0, 1);
                GL11.glRotatef(-rotation, 0, 1, 0);
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                break;
        }
        model.renderPartSet(handle);
        GL11.glPopMatrix();
        helper.enableStandardItemLighting();
    }

    @Override
    public void initModels() {
        model = new TechneDynamicModel(Magneticraft.IDENTIFIER, ModelConstants.HAND_CRANK);
        base = model.createAllContains("base");
        handle = model.createAllContains("handle");
    }
}
