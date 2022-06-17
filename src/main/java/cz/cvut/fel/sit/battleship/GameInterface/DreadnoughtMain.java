package cz.cvut.fel.sit.battleship.GameInterface;


import cz.cvut.fel.sit.battleship.GameInterface.model.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class DreadnoughtMain  extends Application {

    @Override
    public void start(Stage stage) {
        try {
            ViewManager vm = new ViewManager();
            stage = vm.getMainStage();
            stage.show();
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }



}
