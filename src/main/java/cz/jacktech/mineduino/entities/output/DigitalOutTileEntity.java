package cz.jacktech.mineduino.entities.output;

import cz.jacktech.mineduino.core.MineduinoLogger;
import cz.jacktech.mineduino.entities.ETileEntity;
import cz.jacktech.mineduino.entities.OutputTileEntity;
import cz.jacktech.mineduino.gui.GuiHandler;
import cz.jacktech.mineduino.serialiface.SerialManager;
import cz.jacktech.mineduino.serialiface.arduino.ArduinoDigitalPin;
import cz.jacktech.mineduino.serialiface.arduino.ArduinoPin;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by toor on 17.11.14.
 */
public class DigitalOutTileEntity extends OutputTileEntity {

    public static final String ENTITY_NAME = DigitalOutTileEntity.class.getSimpleName();

    private static final String ARDUINO_PIN_NUMBER = "ArduinoPinNumber";

    private ArduinoDigitalPin arduinoPin;
    private int arduinoPinNumber = -1;
    private int countZ = 0;

    @Override
    public void writeToNBT(NBTTagCompound nbttag) {
        super.writeToNBT(nbttag);
        nbttag.setInteger(ARDUINO_PIN_NUMBER, arduinoPinNumber);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttag) {
        super.readFromNBT(nbttag);
        setArduinoPinNumber(nbttag.getInteger(ARDUINO_PIN_NUMBER));
    }

    public void setArduinoPin(int pinNumber) {
        if(!getWorldObj().isRemote) {
            arduinoPin = SerialManager.getInstance().getDigitalPin(pinNumber);
            if(arduinoPin != null)
                arduinoPin.updateMode(ArduinoPin.PinMode.OUTPUT);
        }
    }

    @Override
    public void updateEntity() {
        countZ++;
        if(countZ >= 1) {
            countZ = 0;
            boolean blockIsPowered = isPowered();
            if ((arduinoPin == null || arduinoPin.pinNumber != arduinoPinNumber) && arduinoPinNumber >= 0)
                setArduinoPin(arduinoPinNumber);

            if (arduinoPin != null && arduinoPin.read() != blockIsPowered) {
                //MineduinoLogger.info("updating arduino pin : " + blockIsPowered);
                arduinoPin.update(blockIsPowered);
            }
        }
    }

    @Override
    public void blockDestroyed() {
        if(arduinoPin != null)
            arduinoPin.reset();
    }

    @Override
    public int openGui() {
        return GuiHandler.GUI_DIGITAL;
    }

    @Override
    public void blockAdded() {
        if(arduinoPin != null)
            arduinoPin.updateMode(ArduinoPin.PinMode.OUTPUT);
    }

    @Override
    public int isProvidingPower() {
        return 0;
    }

    public ArduinoDigitalPin getArduinoPin() {
        return arduinoPin;
    }

    public void setArduinoPinNumber(int arduinoPinNumber) {
        this.arduinoPinNumber = arduinoPinNumber;
    }

    public int getArduinoPinNumber() {
        return arduinoPinNumber;
    }

    @Override
    public void sendOutput(Object object) {
        //unsupported, only redstone possible ??
    }
}