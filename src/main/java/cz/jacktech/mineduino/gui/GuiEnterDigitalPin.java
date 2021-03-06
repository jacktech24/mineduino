package cz.jacktech.mineduino.gui;

import cz.jacktech.mineduino.MineDuinoMod;
import cz.jacktech.mineduino.synch.DigitalPinSyncMessage;
import cz.jacktech.mineduino.tiles.DigitalPinEntity;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

/**
 * Created by toor on 11.11.14.
 */
public class GuiEnterDigitalPin extends GuiScreen {

    //private GuiTextField commandTextField;

    private final DigitalPinEntity tileEntity;

    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiButton leftArrowBtn;
    private GuiButton rightArrowBtn;

    private int arduinoPin = 0;

    public GuiEnterDigitalPin(DigitalPinEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui() {
        super.initGui();
        //Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(this.leftArrowBtn = new GuiButton(2, this.width/2 - 4 - 30, this.height / 4 + 60, 20, 20, "<"));
        this.buttonList.add(this.rightArrowBtn = new GuiButton(3, this.width/2 + 4 + 10, this.height / 4 + 60, 20, 20, ">"));
        /*this.commandTextField = new GuiTextField(this.fontRendererObj, this.width / 2 - 50, 100, 100, 20);
        this.commandTextField.setMaxStringLength(2);
        this.commandTextField.setFocused(true);
        if(tileEntity.getArduinoPin() != -1)
            this.commandTextField.setText(String.valueOf(tileEntity.getArduinoPin()));*/
        arduinoPin = tileEntity.getArduinoPin();

        //this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.enabled){
            switch (button.id){
                case 0: //done button
                    try{
                        tileEntity.setArduinoPin(arduinoPin);
                        MineDuinoMod.INSTANCE.sendToServer(new DigitalPinSyncMessage().setup(tileEntity));
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    mc.displayGuiScreen((GuiScreen) null);
                    break;
                case 1: //cancel button
                    mc.displayGuiScreen((GuiScreen)null);
                    break;
                case 2: //leftButton
                    if(arduinoPin > 2)
                        arduinoPin --;
                    break;
                case 3: //right button
                    if(arduinoPin < 50)
                        arduinoPin++;
                    break;
            }
        }
    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_){
        //this.commandTextField.textboxKeyTyped(p_73869_1_, p_73869_2_);
        //this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;

        if (p_73869_2_ != 28 && p_73869_2_ != 156){
            if (p_73869_2_ == 1){
                this.actionPerformed(this.cancelBtn);
            }
        }else{
            this.actionPerformed(this.doneBtn);
        }
    }

    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_){
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        //this.commandTextField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }


    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_){
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("mineduino.selectpin.digital", new Object[0]), this.width / 2, 80, 16777215);
        this.drawCenteredString(this.fontRendererObj, String.valueOf(arduinoPin), this.width/2, this.height / 4 + 60 + 7, 16777215);

        //this.commandTextField.drawTextBox();

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
