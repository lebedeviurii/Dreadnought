package cz.cvut.fel.sit.battleship.GameInterface.model;

import cz.cvut.fel.sit.battleship.GameInterface.UIFactory.UIButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewManager {

    private static final int H = 768;
    private static final int W = 1024;
    private final VBox mainPane;
    private Scene mainScene;
    private Stage mainStage;

    // Better for Main Stage;
    public Stage getMainStage() {
        return mainStage;
    }

    public ViewManager() {
        mainPane = new VBox();
        mainScene = new Scene(mainPane, W, H);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        createLogo();
        createMenuButtons();
        createExit();
        mainPane.setBackground(UIConstants.BASE_BACKGROUND);
        mainPane.setPadding(new Insets(20));
        mainPane.setSpacing(70);
        mainPane.setAlignment(Pos.CENTER);
        mainStage.initStyle(StageStyle.DECORATED);
        mainStage.setScene(mainScene);
    }

    public ViewManager(Stage stage) {
        mainStage = stage;
        mainPane = new VBox();
        mainScene = new Scene(mainPane, W, H);
        createLogo();
        createMenuButtons();
        createExit();
        mainPane.setBackground(UIConstants.BASE_BACKGROUND);
        mainPane.setPadding(new Insets(20));
        mainPane.setSpacing(70);
        mainPane.setAlignment(Pos.CENTER);
        mainStage.setScene(mainScene);
    }

    private void createLogo() {
        ImageView logo = new ImageView("DreadnoughtLogo.png");
        mainPane.getChildren().add(logo);
    }

    private void createMenuButtons() {
        VBox buttonBox = new VBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        Button newGameManPC = UIButtonFactory.createButton("New game: man vs PC", Color.WHITE, UIConstants.LIGHT_BUTTON_BACKGROUND);
        newGameManPC.setOnMouseClicked(actionEvent -> {
            GameViewManager gameViewManager = new GameViewManager();
            GameModeViewManager gvm = new GameModeViewManager();
            gvm.initModeChooser(mainStage, gameViewManager);
            mainStage.show();
        });


        Button newOnlineGame = UIButtonFactory.createButton("New online game", Color.WHITE, UIConstants.LIGHT_BUTTON_BACKGROUND);
        newOnlineGame.setOnMouseClicked(actionEvent -> {
            GameViewManager gameViewManager = new GameViewManager();
            NetworkLogInViewManager nL = new NetworkLogInViewManager();
            nL.initLogIn(mainStage, gameViewManager);
            mainStage.show();
        });



        buttonBox.getChildren().add(newGameManPC);
        buttonBox.getChildren().add(newOnlineGame);
        mainPane.getChildren().add(buttonBox);
    }

    private void createExit() {
        Button quitButton = UIButtonFactory.createButton("EXIT", Color.WHITE, UIConstants.DARK_BUTTON_BACKGROUND);
        quitButton.setOnMouseClicked(mouseEvent -> mainStage.close());
        mainPane.getChildren().add(quitButton);
    }


}
