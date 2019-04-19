package sample;

import com.sun.glass.events.KeyEvent;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import java.awt.*;
import java.awt.event.InputEvent;

import static com.sun.glass.events.KeyEvent.VK_E;

public class Controller implements NativeKeyListener, NativeMouseListener {



    private Robot robot;

    @FXML
    private CheckBox shift;
    @FXML
    private CheckBox ctrl;
    @FXML
    private CheckBox leftclick;
    @FXML
    private CheckBox rightclick;
    @FXML
    private CheckBox recordButton;
    @FXML
    public TextField textDelay;

    private int recordedKey = -1;
    private int delay = 500;

    public static boolean run = false;
    private static boolean recordingMacro = false;
    private static boolean recordingKey = false;
    private static boolean recordingMacroTrigger = false;

    public Controller(){
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void OnStartClicked(ActionEvent event) {
        try{
            int delayValue = Integer.parseInt(textDelay.getText());
            this.delay = delayValue;
            Service clickService = new ClickService();
            clickService.start();

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error:  That's not a number");
            alert.show();

            textDelay.setText("500");
            this.delay = 500;
        }

    }



    public void startProcess(){
        try {
            Thread.sleep(3000);
            run = true;

            while(run) {
                if (shift.isSelected()) {
                    robot.keyPress(KeyEvent.VK_SHIFT);
                }
                if (ctrl.isSelected()) {
                    robot.keyPress(KeyEvent.VK_CONTROL);
                }
                if (recordButton.isSelected()) {
                    robot.keyPress(VK_E);
                }

                Thread.sleep(this.delay);

                if (leftclick.isSelected()) {
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                }
                if (rightclick.isSelected()) {
                    robot.mousePress(InputEvent.BUTTON3_MASK);
                }

                Thread.sleep(this.delay);

                if(shift.isSelected()){
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                }
                if(ctrl.isSelected()){
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                }
                if (recordButton.isSelected()) {
                    robot.keyRelease(VK_E);
                }
                if(leftclick.isSelected()){
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                }
                if(rightclick.isSelected()){
                    robot.mouseRelease(InputEvent.BUTTON3_MASK);
                }

                Thread.sleep(this.delay);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onStopClicked(ActionEvent event) {
        run = false;
    }

    public void RecordButton(ActionEvent actionEvent) {
        recordingKey = true;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recording Button");
        alert.setHeaderText("Recording Button...");
        alert.setContentText("Press button to record macro to.");

        alert.show();

        try{
            GlobalScreen.registerNativeHook();

            GlobalScreen.addNativeKeyListener(this);
            GlobalScreen.addNativeMouseListener(this);



        }catch(NativeHookException e){

        }
    }

    private class ClickService extends Service<Void> {
        protected Task createTask() {
            return new Task<Void>() {
                protected Void call() throws Exception {

                    startProcess();

                    return null;
                }
            };
        }
    }

    public void OnRecordClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recording Macro");
        alert.setHeaderText("Recording Macro...");
        alert.setContentText("Press button to record macro to.");

        alert.show();

        try{
            GlobalScreen.registerNativeHook();

            GlobalScreen.addNativeKeyListener(this);
            GlobalScreen.addNativeMouseListener(this);



        }catch(NativeHookException e){

        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        if(recordingKey) {
            this.recordedKey = nativeKeyEvent.getKeyCode();
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
         if(recordingKey) {
            this.recordedKey = nativeKeyEvent.getKeyCode();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {

    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {

    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {

    }

}
