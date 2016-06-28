/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab3_1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Ada
 */
public class Lab3_1 extends Application {
 

        private File plik;
        private TreeView<File> widok;
        private String nowaNazwa;
        private TextField pole;
        private TextField serwer;
        private TextField port;
        private Label komunikaty;
        private Label komunikaty2;
        private ProgressBar postep;
        private ProgressBar calkowityPostep;
        
   @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Menadzer plikow");
        Parent wzor = FXMLLoader.load(getClass().getResource("lab3.fxml"));
        
        widok=(TreeView)wzor.lookup("#widok");
        widok.setCellFactory(new FileCellFactory());
        widok.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dodajMenu();
                
        Button korzen=(Button)wzor.lookup("#korzen");
        
        korzen.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    wybierzFolder(stage);
                    TreeItem<File> nowy= new TreeItem<File>(plik);
                    nowy.setExpanded(true);
                    widok.setRoot(nowy);
                    drzewko(nowy, plik);
                   
                }
            });
        
       
        serwer=(TextField)wzor.lookup("#serwer");          
        port=(TextField)wzor.lookup("#port");   
        komunikaty=(Label)wzor.lookup("#komunikaty");        
        postep=(ProgressBar)wzor.lookup("#postep");
        komunikaty2=(Label)wzor.lookup("#komunikaty2");  
        //calkowityPostep=(ProgressBar)wzor.lookup("#postepCalkowity");
        
        Scene scene = new Scene(wzor, 400, 500);
        stage.setScene(scene);
        stage.show();
        
        
}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    private void nazwij() throws IOException{        
        nowaNazwa=serwer.getText();      
    }
    
    private void dodajMenu(){
          final ContextMenu menu = new ContextMenu();
        
        MenuItem wyslijPlik= new MenuItem("Wyslij plik");
       wyslijPlik.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try{
                    nazwij();
                    wysPlik();
                }
                catch(IOException ex) {
                    System.out.println("Nie udalo sie wyslac pliku");
                }
            }
        });
       
        menu.getItems().add(wyslijPlik);
       
        widok.setContextMenu(menu);
        widok.setCellFactory(new FileCellFactory());
    
    }
    
    private void wysPlik() throws IOException{
        try{
             //moim workerem jest Wysylacz
             // treeView.getSelectionModel().getSelectedItems() 
            ObservableList<TreeItem<File>> temp= widok.getSelectionModel().getSelectedItems();
            ArrayList workers= new ArrayList<>(temp.size());
            int rozmiar=0;
            int ilosc=0;
            for(TreeItem<File> zaznaczony:temp){
                String ser=serwer.textProperty().get();
                int por= Integer.parseInt(port.textProperty().get());

                if(!ser.isEmpty()&&por!=0){
                    File wydobyty=zaznaczony.getValue();
                    Path sciezka=wydobyty.toPath();
                    rozmiar+=wydobyty.length();
                    ilosc+=1;
                    //Worker nowy= new Worker(wydobyty);
                }
                   
                }
           
            Worker poprzedni=null;
            int wyslane=0;
            for(TreeItem<File> zaznaczony:temp){
              
                String ser=serwer.textProperty().get();
                int por= Integer.parseInt(port.textProperty().get());
                
                if(!ser.isEmpty()&&por!=0){
                    
                    File wydobyty=zaznaczony.getValue();                  
                
                    Wysylacz nowyPlik= new Wysylacz(wydobyty,ser,por);
                    komunikaty.textProperty().bind(nowyPlik.messageProperty());
                    postep.progressProperty().bind(nowyPlik.progressProperty());
                    Thread watek=new Thread(nowyPlik);
                    watek.start();
                    
                    //calkowityPostep.add(postep.pressedProperty(),rozmiar);
                    komunikaty2.setText("Wyslano "+ wyslane+ "/" +ilosc+ "plikow");
                    wyslane=wyslane+1;
                }
            
            
            
            
            }
            komunikaty2.setText("Wyslano "+ wyslane+ "/" +ilosc+ "plikow");
        }
        catch(Exception ex){
                   System.err.println("Nalezy wypelnic port i serwer");
                    }
        
    }
    
    
    
 
    
    private void wybierzFolder(Stage stage) {
      DirectoryChooser nowy =new DirectoryChooser();
          plik=nowy.showDialog(null);    
    }
    
    private void drzewko(TreeItem<File> rodzic, File plik2){
        File[] tablica=plik2.listFiles();
        for(File temp:tablica){
            TreeItem<File> nowy= new TreeItem<File>(temp);
            nowy.setExpanded(true);
            rodzic.getChildren().add(nowy);
            if(temp.isDirectory()){
                drzewko(nowy, temp);
            }
        }   
    }
    
    private class FileCell extends TreeCell<File> {
        @Override
        protected void updateItem(File file, boolean empty) {
            super.updateItem(file, empty);
            if (file != null) {
                String nazwa=file.getName();
                Path sciezka=file.toPath();
                try {
                    DosFileAttributes atrybuty = Files.readAttributes(sciezka, DosFileAttributes.class);
                    if(atrybuty.isReadOnly()){
                    nazwa+=" r";
                    }
                    else{
                        nazwa+=" -";
                    }
                    if(atrybuty.isArchive()){
                        nazwa+="a";
                    }
                    else{
                        nazwa+="-";
                    }
                    if(atrybuty.isSystem()){
                        nazwa+="s";
                    }
                    else{
                        nazwa+="-";
                    }
                    if(atrybuty.isHidden()){
                        nazwa+="h";
                    }
                    else{
                        nazwa+="-";
                    } 
                } catch (IOException ex) {
                    System.out.print("Nie udalo sie kreslic wlasciwosci pliku");
                }
                
                setText(nazwa);
            } else {
                setText(null);
            }
        }
    }
    
//    public void initialize(URL url, ResourceBundle rb){
//        widok.setCellFactory(new FileCellFactory());
//    
//    }
    private class FileCellFactory implements Callback<TreeView<File>, TreeCell<File>> {
        @Override
        public TreeCell<File> call(TreeView<File> p) {
            return new FileCell();
        }
    }

}
