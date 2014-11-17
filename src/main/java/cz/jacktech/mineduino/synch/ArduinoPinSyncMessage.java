package cz.jacktech.mineduino.synch;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cz.jacktech.mineduino.entities.ETileEntity;
import cz.jacktech.mineduino.entities.input.DigitalInTileEntity;
import cz.jacktech.mineduino.entities.output.DigitalOutTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by toor on 11.11.14.
 */
public class ArduinoPinSyncMessage implements IMessage {

    public int x,y,z,arduinoPin;

    public ArduinoPinSyncMessage() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        arduinoPin = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(arduinoPin);
    }

    public IMessage setup(ETileEntity tileEntity, int arduinoPin) {
        this.x = tileEntity.xCoord;
        this.y = tileEntity.yCoord;
        this.z = tileEntity.zCoord;
        this.arduinoPin = arduinoPin;
        return this;
    }

    public static class Handler implements IMessageHandler<ArduinoPinSyncMessage, IMessage> {

        @Override
        public IMessage onMessage(ArduinoPinSyncMessage m, MessageContext ctx) {
            TileEntity entity = ctx.getServerHandler().playerEntity.getEntityWorld().getTileEntity(m.x,m.y,m.z);
            if(entity instanceof DigitalInTileEntity){
                DigitalInTileEntity outputTileEntity = (DigitalInTileEntity) entity;
                outputTileEntity.setArduinoPinNumber(m.arduinoPin);
                outputTileEntity.markForUpdate();
            }else if(entity instanceof DigitalOutTileEntity){
                DigitalOutTileEntity inputTileEntity = (DigitalOutTileEntity) entity;
                inputTileEntity.setArduinoPinNumber(m.arduinoPin);
                inputTileEntity.markForUpdate();
            }
            return null;
        }
    }

}