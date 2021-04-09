
import java.io.Serializable;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class ClientGui extends Application {

    @FXML
    private TextField t1, t2;
    private String ipAddress; //"127.0.0.1"
    private int portNumber; //5555
    HashMap<String, Scene> sceneMap;
    @FXML
    private GridPane gridPane = new GridPane(); // creates a grid pane
    int wordSize;
    char[] displayWord;
    String displayString;
    TextField s1, s2, s3, s4;
    Button login, b, b1, b2, playBtn, howToPlayBtn, ExitBtn, backToMenuBtn, cat1, cat2, cat3, playAgainBtn;
    GridPane grid;
    HBox buttonBox;
    VBox clientBox;
    Scene startScene, clientMenuScene, howToPlayScene;
    BorderPane startPane;
    Client clientConnection;
    ListView<String> listItems, listItems2;
    private Button[][] arr = new Button[13][2];
    private EventHandler<ActionEvent> myHandler;
    int cat = 0;
    private Image bg;
    private ImageView bgView;
    readFromClient reader = new readFromClient();
    BorderPane bp;

    class readFromClient implements Serializable {

        private static final long serialVersionUID = 1L;
        String gamePhase = "";

        public void setGamePhase(String i) {
            gamePhase = i;
        }

        public String getGamePhase() {
            return gamePhase;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Client GUI");
        t1 = new TextField("Please Enter The IpAddress");
        t1.setText("127.0.0.1");
        t2 = new TextField("Please Enter The portNumber");
        t2.setText("8080");
        s1 = new TextField();
        bp = new BorderPane();
        howToPlayBtn = new Button("How To Play");
        playAgainBtn = new Button("Play Again");
        ExitBtn = new Button("Exit");
        cat1 = new Button("Food Catagory");
        cat2 = new Button("Countries Catagory");
        cat3 = new Button("Sport Catagory");
        howToPlayBtn.setStyle("-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: gray;-fx-font-size:18");
        playAgainBtn.setStyle("-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: gray;-fx-font-size:18");
        ExitBtn.setStyle("-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: #b91428;-fx-font-size:18");
        cat1.setStyle("-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: #fc6c85;-fx-font-size:20");
        cat2.setStyle("-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: #ffa343;-fx-font-size:20");
        cat3.setStyle("-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: #9f4347;-fx-font-size:20");

        myHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                b = (Button) e.getSource();
                char lowercase = Character.toLowerCase(b.getText().charAt(0));
                b.setDisable(true);
                clientConnection.send(lowercase);

                PauseTransition pause = new PauseTransition(Duration.seconds(.05));
                pause.play();
                pause.setOnFinished(t -> {
                    PauseTransition pause2 = new PauseTransition(Duration.seconds(4));
                    pause2.setOnFinished(z -> {
                        clientMenuScene = createCatGui();
                        sceneMap.put("client", clientMenuScene);
                        primaryStage.setScene(clientMenuScene);
                        listItems2.getItems().clear();
                    });
                    clientConnection.setUpdates(reader);
                    String s = reader.gamePhase;
                    if (s.equals("You Lost The Game")) {
                        disableAll();
                        pause2.setOnFinished(z -> {
                            clientMenuScene = createGameOverGui("You Lost!");
                            sceneMap.put("client", clientMenuScene);
                            primaryStage.setScene(clientMenuScene);
                            listItems2.getItems().clear();
                        });
                        pause2.play();

                    } else if (s.equals("You Won The Game!")) {
                        disableAll();
                        pause2.setOnFinished(z -> {
                            clientMenuScene = createGameOverGui("You Won!");
                            sceneMap.put("client", clientMenuScene);
                            primaryStage.setScene(clientMenuScene);
                            listItems2.getItems().clear();
                        });
                        pause2.play();
                    } else if (s.equals("You Beat Category: 1")) {
                        disableAll();
                        cat1.setDisable(true);
                        pause2.play();
                    } else if (s.equals("You Beat Category: 2")) {
                        disableAll();
                        cat2.setDisable(true);
                        pause2.play();
                    } else if (s.equals("You Beat Category: 3")) {
                        disableAll();
                        cat3.setDisable(true);
                        pause2.play();
                    } else if (s.substring(0, 8).equals("You Lost")) {
                        disableAll();
                        pause2.play();
                    }
                });
            }
        };
        playBtn = new Button("Play Game");
        playBtn.setOnAction(e -> {
            clientMenuScene = createPlayGui();
            sceneMap.put("client", clientMenuScene);
            primaryStage.setScene(clientMenuScene);
        });

        login = new Button("Login");
        login.setOnAction(e -> {
            try {
                portNumber = Integer.parseInt(t2.getText());
                ipAddress = t1.getText();
                System.out.println(ipAddress + " " + portNumber);
                clientConnection = new Client(data -> {
                    Platform.runLater(() -> {
                        listItems2.getItems().add(data.toString());
                    });
                }, ipAddress, portNumber);
                clientConnection.start();
                //naveen
                startScene = createCatGui();
                sceneMap.put("client", startScene);
                primaryStage.setScene(startScene);
            } catch (Exception t) {
                t1.setText("Please Enter An IP Address");
                t2.setText("Please Enter A Port Number");
            }
        });

        playAgainBtn = new Button("Play Again");
        backToMenuBtn = new Button("Back");
        ExitBtn = new Button("Exit");

        playAgainBtn.setOnAction(e -> {
            resetCat();
            clientMenuScene = createCatGui();
            sceneMap.put("client", clientMenuScene);
            primaryStage.setScene(clientMenuScene);
        });

        howToPlayBtn.setOnAction(e -> {
            clientMenuScene = createHowToPlayGui();
            sceneMap.put("client", clientMenuScene);
            primaryStage.setScene(clientMenuScene);
        });

        backToMenuBtn.setOnAction(e -> {
            clientMenuScene = createCatGui();
            sceneMap.put("client", clientMenuScene);
            primaryStage.setScene(clientMenuScene);
        });

        ExitBtn.setOnAction(e -> {
            System.exit(0);
        });

        startScene = createLoginGui();
        listItems = new ListView<String>();
        listItems2 = new ListView<String>();
        sceneMap = new HashMap<String, Scene>();
        sceneMap.put("client", startScene);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        cat1.setOnAction(e -> {
            cat = 1;
            clientConnection.send('1');
            clientMenuScene = createPlayGui();
            sceneMap.put("client", clientMenuScene);
            primaryStage.setScene(clientMenuScene);
        });
        cat2.setOnAction(e -> {
            cat = 2;
            clientConnection.send('2');
            clientMenuScene = createPlayGui();
            sceneMap.put("client", clientMenuScene);
            primaryStage.setScene(clientMenuScene);
        });
        cat3.setOnAction(e -> {
            cat = 3;
            clientConnection.send('3');
            clientMenuScene = createPlayGui();
            sceneMap.put("client", clientMenuScene);
            primaryStage.setScene(clientMenuScene);
        });
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        addGrid(gridPane);
        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    public void disableAll() {
        for (int x = 0; x < 13; x++) {
            for (int y = 0; y < 2; y++) {
                b2 = arr[x][y];
                b2.setDisable(true);
            }
        }
    }

     // client login GUI
    public Scene createLoginGui() {
        BorderPane bp = new BorderPane();
        bg = new Image("guesstheword.png");
        bgView = new ImageView(bg);
        login.setText("LOGIN");
        t1.setStyle("-fx-text-fill: green;-fx-font-size:20");
        t2.setStyle("-fx-text-fill: green;-fx-font-size:20");
        t1.setMaxWidth(150);
        t2.setMaxWidth(150);
        login.setStyle("-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: gray;-fx-font-size:20");
        VBox vb = new VBox(10, bgView, t1, t2, login);
        vb.setAlignment(Pos.CENTER);
        bp.setPadding(new Insets(20));
        bp.setCenter(vb);
        bp.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058)");
        return new Scene(bp, 700, 700);
    }
    
    // home menu GUI
    public Scene createCatGui() {
        BorderPane bp = new BorderPane();
        HBox hb = new HBox(10, cat1, cat2, cat3);
        hb.setAlignment(Pos.CENTER);
        bp.setPadding(new Insets(20));
        Label label1 = new Label("CHOOSE A CATEGORY");                
        bg = new Image("guesstheword.png");
        bgView = new ImageView(bg);
        Label label3 = new Label("DEVELOPED BY\n\nAnupreet Paulker\nAvi Bhatnagar\n\n\n");
        TextField t = new TextField("Pick a Catagory");
        t.setEditable(false);
        label1.setStyle("-fx-text-fill: green;-fx-font-size:30;-fx-font-weight: bold;");        
        label3.setStyle("-fx-text-fill: black;-fx-font-size:20");
        t.setStyle("-fx-text-fill: green; -fx-font-size:20");
        ExitBtn.setStyle("-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: red;-fx-font-size:20");
        VBox hbExit = new VBox(10, label1, hb, howToPlayBtn, bgView, label3, ExitBtn);
        hbExit.setAlignment(Pos.CENTER);
        bp.setCenter(hbExit);
        bp.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058)");
        return new Scene(bp, 700, 700);
    }

    public void resetCat() {
        cat1.setDisable(false);
        cat2.setDisable(false);
        cat3.setDisable(false);
    }

    // gameplay GUI
    public Scene createPlayGui() {
        resetGrid(gridPane);
        clientBox = new VBox(10, gridPane, listItems2);
        clientBox.setPadding(new Insets(10));
        clientBox.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058)");
        return new Scene(clientBox, 700, 500);
    }

    // how to play GUI
    public Scene createHowToPlayGui() {
        BorderPane bp = new BorderPane();
        bg = new Image("howTo.jpg");
        bgView = new ImageView(bg);
        bp.getChildren().addAll(bgView);
        backToMenuBtn.setStyle("-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: #b91428;-fx-font-size:18");
        bp.setTop(backToMenuBtn);
        return new Scene(bp, 900, 433);
    }

    // Game Over GUI
    public Scene createGameOverGui(String s) { //WRONG
        BorderPane bp = new BorderPane();
        bg = new Image("win.jpg");
        bgView = new ImageView(bg);
        bp.getChildren().addAll(bgView);
        clientBox.setPadding(new Insets(100));
        s1.setText(s);
        s1.setMaxWidth(150);
        s1.setAlignment(Pos.CENTER);
        s1.setStyle("-fx-text-fill: green; -fx-font-size:20");
        playAgainBtn.setStyle("-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: #b91428;-fx-font-size:18");
        ExitBtn.setStyle("-fx-text-fill: white;-fx-font-weight: bold;-fx-background-color: #b91428;-fx-font-size:18");
        clientBox = new VBox(10, playAgainBtn, ExitBtn, s1);
        clientBox.setAlignment(Pos.CENTER);
        bp.setCenter(clientBox);
        return new Scene(bp, 700, 700);
    }

    // adds an alphabet grid
    public void addGrid(GridPane grid) {
        int asciiAlphabet = 65;
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 13; x++) {
                b2 = new Button();
                b2.setMinSize(40, 40);
                b2.setStyle("-fx-background-color: snow;");
                b2.setText(String.valueOf((char) asciiAlphabet));
                b2.setOnAction(myHandler);
                arr[x][y] = b2;
                grid.add(arr[x][y], x, y);
                asciiAlphabet++;
            }
        }
    }

    public void resetGrid(GridPane grid) {
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 13; x++) {
                b2 = arr[x][y];
                b2.setDisable(false);
            }
        }
    }

    public void updatedMessage() {
        System.out.println(reader.getGamePhase());

    }

    public void setReader(String message) {
        reader.setGamePhase(message);
    }

}
