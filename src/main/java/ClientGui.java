//import javafx.application.Application;
//
//import javafx.scene.Scene;
//
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//public class JavaFXTemplate extends Application {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		launch(args);
//	}
//
//	//feel free to remove the starter code from this method
//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		// TODO Auto-generated method stub
//		primaryStage.setTitle("Welcome to JavaFX");
//		
//		
//		
//				
//		Scene scene = new Scene(new VBox(), 700,700);
//		primaryStage.setScene(scene);
//		primaryStage.show();
//	}
//
//}

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.FocusModel;
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

public class ClientGui extends Application{

	@FXML
	private TextField t1, t2;
	

	private String ipAddress; //"127.0.0.1"
	private int portNumber; //5555

    
	HashMap<String, Scene> sceneMap;
	
	@FXML
	private GridPane gridPane = new GridPane(); // creates a grid pane

	
//	HangmanLogic logic;
	int wordSize;
	char[] displayWord;
	String displayString;
	TextField s1,s2,s3,s4;
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
	private Image bg; //The image used for the background
	private ImageView bgView;
	readFromClient reader = new readFromClient();
	BorderPane bp;	// new for how to play
	
	
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
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Networked Client/Server GUI Example");
		
		t1 = new TextField("Please Enter IpAddress");
		t1.setText("127.0.0.1");
		t2 = new TextField("Please Enter portNumber");
		t2.setText("5555");
		s1 = new TextField();
		//b1 = new Button("Send");
		bp = new BorderPane();
		howToPlayBtn = new Button("How To Play");
		howToPlayBtn.setStyle("-fx-background-color:red;");
		playAgainBtn = new Button("Play Again");
		ExitBtn = new Button("Exit");
		ExitBtn.setStyle("-fx-background-color:red;");
		cat1 = new Button("Food Catagory");
		cat1.setStyle("-fx-background-color:gold;");
		cat2 = new Button("Countries Catagory");
		cat2.setStyle("-fx-background-color:aqua;");
		cat3 = new Button("Sport Catagory");
		cat3.setStyle("-fx-background-color:lawngreen;");

		
		myHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				
				b = (Button) e.getSource();

				char lowercase = Character.toLowerCase(b.getText().charAt(0));
				
				b.setDisable(true);
				clientConnection.send(lowercase);
				
				
				PauseTransition pause = new PauseTransition(Duration.seconds(.05));
				pause.play();
				pause.setOnFinished(t->{
					PauseTransition pause2 = new PauseTransition(Duration.seconds(4));
					pause2.setOnFinished(z->{
						clientMenuScene = createCatGui();
						sceneMap.put("client",  clientMenuScene);
						primaryStage.setScene(clientMenuScene);
						listItems2.getItems().clear();
						});
					clientConnection.setUpdates(reader);
					String s = reader.gamePhase;
					if (s.equals("You Lost The Game")) {
						disableAll();
						pause2.setOnFinished(z->{
							clientMenuScene = createGameOverGui("You Lost!");
							sceneMap.put("client",  clientMenuScene);
							primaryStage.setScene(clientMenuScene);
							listItems2.getItems().clear();
							});
						pause2.play();

						
						
					} else if (s.equals("You Won The Game!")) {
						disableAll();
						pause2.setOnFinished(z->{
							clientMenuScene = createGameOverGui("You Won!");
							sceneMap.put("client",  clientMenuScene);
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
					} else if (s.substring(0,8).equals("You Lost")) {
						disableAll();
						pause2.play();
					}
				});
				
			}
		};
		playBtn = new Button("Play Game");
		playBtn.setOnAction(e->{
			clientMenuScene = createPlayGui();
			sceneMap.put("client",  clientMenuScene);
			primaryStage.setScene(clientMenuScene);
		});
		
		login = new Button("Login");
		login.setOnAction(e->{
			try {
				portNumber = Integer.parseInt(t2.getText());
				ipAddress = t1.getText();
				System.out.println(ipAddress + " " + portNumber);
				
				clientConnection = new Client(data->{Platform.runLater(()->{listItems2.getItems().add(data.toString());});}, ipAddress, portNumber);
				clientConnection.start();
				startScene = createCatGui();
				sceneMap.put("client",  startScene);
				primaryStage.setScene(startScene);
			} catch (Exception t) {
				t1.setText("Please Enter An IP Address");
				t2.setText("Please Enter A Port Number");
			}
		});
		

		playAgainBtn = new Button("Play Again");
		backToMenuBtn = new Button("Back");
		ExitBtn = new Button("Exit");
		
		playAgainBtn.setOnAction(e->{
			resetCat();
			clientMenuScene = createCatGui();
			sceneMap.put("client",  clientMenuScene);
			primaryStage.setScene(clientMenuScene);
		});
		

		howToPlayBtn.setOnAction(e->{
			clientMenuScene = createHowToPlayGui();
			sceneMap.put("client",  clientMenuScene);
			primaryStage.setScene(clientMenuScene);
		});
		
		backToMenuBtn.setOnAction(e->{
			clientMenuScene = createCatGui();
			sceneMap.put("client",  clientMenuScene);
			primaryStage.setScene(clientMenuScene);
		});
		
		ExitBtn.setOnAction(e->{
			System.exit(0);
		});
		
		
		
		startScene = createLoginGui();
		
		listItems = new ListView<String>();
		listItems2 = new ListView<String>();
		
		
		
		sceneMap = new HashMap<String, Scene>();
		
		sceneMap.put("client",  startScene);
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		cat1.setOnAction(e->{
			cat = 1;
			clientConnection.send('1');
			clientMenuScene = createPlayGui();
			sceneMap.put("client",  clientMenuScene);
			primaryStage.setScene(clientMenuScene);
		});
		cat2.setOnAction(e->{			
			cat = 2;
			clientConnection.send('2');
			clientMenuScene = createPlayGui();
			sceneMap.put("client",  clientMenuScene);
			primaryStage.setScene(clientMenuScene);
		});
		cat3.setOnAction(e->{
			cat = 3;
			clientConnection.send('3');
			clientMenuScene = createPlayGui();
			sceneMap.put("client",  clientMenuScene);
			primaryStage.setScene(clientMenuScene);
		});
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		addGrid(gridPane);

		primaryStage.setScene(startScene);
		primaryStage.show();
		
	}
	
	
	public void disableAll(){
		for(int x = 0; x < 13; x++) {
			for(int y = 0; y < 2; y++) {
				b2 = arr[x][y];
				b2.setDisable(true);
			}
		}
	}
	
	// home menu GUI
	public Scene createCatGui() {
		BorderPane bp = new BorderPane();
		bg = new Image("welcome1.jpg");
		bgView = new ImageView(bg);
		bp.getChildren().addAll(bgView);
		HBox hb = new HBox(10, cat1, cat2, cat3);
		bp.setPadding(new Insets(140));
		TextField t = new TextField("Pick a Catagory");
		t.setEditable(false);
		VBox hbExit = new VBox(hb, ExitBtn);
		HBox hbHowToPlay = new HBox(howToPlayBtn);
		bp.setTop(t);
		bp.setCenter(hbExit);
//		bp.setBottom(vbExit);
		bp.setRight(hbHowToPlay);
		bp.setStyle("-fx-background-color: black");
		return new Scene(bp, 700, 700);
	}
	
	public void resetCat() {
		cat1.setDisable(false);
		cat2.setDisable(false);
		cat3.setDisable(false);
	}
	
	
	
	// client login GUI
	public Scene createLoginGui() {
			BorderPane bp = new BorderPane();
			bg = new Image("client1.jpg");
			bgView = new ImageView(bg);
			bp.getChildren().addAll(bgView);
			login.setText("Login");
			VBox vb = new VBox(10, t1, t2, login);
			bp.setPadding(new Insets(70));
			bp.setBottom(vb);
			bp.setStyle("-fx-background-color: mediumspringgreen");
			return new Scene(bp, 700, 700);
	}

	
	// gameplay GUI
	public Scene createPlayGui() {
		resetGrid(gridPane);
		clientBox = new VBox(10, gridPane,listItems2);
		clientBox.setPadding(new Insets(20));
		clientBox.setStyle("-fx-background-color: black");
		return new Scene(clientBox, 700, 700);
	}
	
	// how to play GUI
	public Scene createHowToPlayGui() {
		BorderPane bp = new BorderPane();
		bg = new Image("howToPlay.jpg");
		bgView = new ImageView(bg);
		bp.getChildren().addAll(bgView);
		bp.setBottom(backToMenuBtn);
		return new Scene(bp, 700, 700);
	}
	
	// Game Over GUI
	public Scene createGameOverGui(String s) { //WRONG
		BorderPane bp = new BorderPane();
		bg = new Image("win.jpg");
		bgView = new ImageView(bg);
		bp.getChildren().addAll(bgView);
		clientBox.setPadding(new Insets(100));
		s1.setText(s);
		clientBox = new VBox(10, playAgainBtn, ExitBtn, s1);
		bp.setCenter(clientBox);
//		bp.setStyle("-fx-background-color: black");
		return new Scene(bp, 700, 700);
	}
	
	// adds an alphabet grid
	public void addGrid(GridPane grid) {
		int asciiAlphabet = 65;
        for(int y = 0; y < 2; y++) {
        	for(int x = 0; x < 13; x++) {
	            b2 = new Button();
	            b2.setMinSize(40, 40);
	            b2.setStyle("-fx-background-color: snow;");
	            b2.setText(String.valueOf((char)asciiAlphabet));
	            b2.setOnAction(myHandler);
	            arr[x][y] = b2;
	            grid.add(arr[x][y], x, y);
	            asciiAlphabet++;
	        }
	    }
	}
	
	public void resetGrid(GridPane grid) {
        for(int y = 0; y < 2; y++) {
        	for(int x = 0; x < 13; x++) {
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

