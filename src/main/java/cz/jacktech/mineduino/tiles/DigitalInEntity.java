package cz.jacktech.mineduino.tiles;

import cz.jacktech.mineduino.serialiface.SerialManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by toor on 11.11.14.
 */
public class DigitalInEntity extends DigitalPinEntity {

    public static final String ENTITY_NAME = DigitalInEntity.class.getSimpleName();
    public static final String PROVIDING_POWER = "Providing";
    public boolean isProvidingPower = false;

    @Override
    public void updateEntity() {
        if(!getWorldObj().isRemote){
            if(getArduinoPin() != -1) {
                boolean currentStatus = SerialManager.getInstance().getPinStatus(getArduinoPin());
                if(currentStatus != isProvidingPower) {
                    isProvidingPower = currentStatus;
                    notifySurroundingBlocks();
                }
                //System.out.println("Current status ("+getArduinoPin()+"): "+currentStatus);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttag) {
        super.writeToNBT(nbttag);
        nbttag.setBoolean(PROVIDING_POWER, isProvidingPower);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttag) {
        super.readFromNBT(nbttag);
        isProvidingPower = nbttag.getBoolean(PROVIDING_POWER);
    }

    private void notifySurroundingBlocks() {
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
        markForUpdate();
    }

    @Override
    public void setArduinoPin(int arduinoPin) {
        super.setArduinoPin(arduinoPin);
        SerialManager.getInstance().setupInput(arduinoPin);
    }
}
