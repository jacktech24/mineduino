package cz.jacktech.mineduino.tiles;

import cz.jacktech.mineduino.serialiface.SerialManager;

/**
 * Created by toor on 11.11.14.
 */
public class DigitalOutEntity extends DigitalPinEntity {

    public static final String ENTITY_NAME = DigitalOutEntity.class.getSimpleName();

    @Override
    public void setArduinoPin(int arduinoPin) {
        super.setArduinoPin(arduinoPin);
        SerialManager.getInstance().setupOutput(arduinoPin);
    }
}
