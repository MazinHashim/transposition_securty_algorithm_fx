package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class designController {

    @FXML
    private AnchorPane homePane,encrPane,decrPane,shutPane;

    @FXML
    private JFXTextArea encryArea,decryArea;

    @FXML
    private JFXTextField encryText;
    
    private double x=0,y=0;
    private char mArr[][];
    
    @FXML
    private void dragged(MouseEvent event) {
    	Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		stage.setX(event.getScreenX()-x);
		stage.setY(event.getScreenY()-y);
    }
    @FXML
    private void pressed(MouseEvent event) {
    	x = event.getSceneX();
		y = event.getSceneY();
    }
    @FXML
    private void closeAction(ActionEvent event) {
    	Platform.exit();
    	System.exit(0);
    }
    
    @FXML
    private void encryptionAction(ActionEvent event) {
    	decryArea.setText("");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("C:\\Users\\Mazin\\Desktop"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT Files","*.txt"));
		File selected = fileChooser.showOpenDialog(null);
		
		String plaintext = readFromFile(selected);
		String output = encryption(plaintext, encryText.getText());
		encryArea.setText(output);
    	writeInToFile(output, selected);
    }
    @FXML
    private void decryptionAction(ActionEvent event) {
    	encryArea.setText("");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("C:\\Users\\Mazin\\Desktop"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT Files","*.txt"));
		File selected = fileChooser.showOpenDialog(null);
		
		String ciphertext = readFromFile(selected);
		String output = decryption(ciphertext);
		decryArea.setText(output);
    	writeInToFile(output, selected);
    }
	public String encryption(String mass,String k){
		int mlen = mass.length();
		char[] key = k.toCharArray();
		Arrays.sort(key);
		int klen = key.length;
		int mml = mlen % klen;
		String ciphertext="";
		
		if(mml == 0)
			mArr = new char[mlen/klen][klen];
		else
			mArr = new char[(mlen/klen)+1][klen];

		int z=0;
		for(int i=0; i<mArr.length; i++){
			for(int j=0; j<mArr[i].length; j++){
				if(z<mlen){
					mArr[i][j] = mass.charAt(z);
					System.out.print(mArr[i][j]);
					z++;
				}else break;
			}
		}

		int index=0;
		for(int i=0; i<mArr.length; i++){
			index = k.indexOf(key[i]);
			for(int j=0; j<mArr[i].length; j++){
				ciphertext += mArr[j][index];
			}
		}

		return ciphertext;
	}
	public String decryption(String mass){
		String plaintext="";
		for(int i=0; i<mArr.length; i++){
			for(int j=0; j<mArr[i].length; j++){
				plaintext += mArr[i][j];
			}
		}
		return plaintext;
	}
    private void visibility(AnchorPane needed, AnchorPane not1, AnchorPane not2, AnchorPane not3) {
		needed.setVisible(true);
		not1.setVisible(false);
		not2.setVisible(false);
		not3.setVisible(false);
	}
    @FXML
    private void HomePage(ActionEvent event) {
    	visibility(homePane, shutPane, encrPane, decrPane);
    }

    @FXML
    private void decryptionPage(ActionEvent event) {
    	visibility(decrPane, homePane, encrPane, shutPane);
    }

    @FXML
    private void encriptionPage(ActionEvent event) {
    	visibility(encrPane, homePane, decrPane,shutPane);
    }

    @FXML
    private void shutdownPage(ActionEvent event) {
    	visibility(shutPane, homePane, encrPane, decrPane);
    }
    public String readFromFile(File name){
		String readed = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(name));
			String line = null;
			StringBuilder strb = new StringBuilder();
			while((line = reader.readLine()) != null){
				strb.append(line);
			}
			readed = strb.toString();
			reader.close();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "The file "+name.getName()+" could not be found","Open File",JOptionPane.ERROR_MESSAGE);
		}
		return readed;
	}
	public void writeInToFile(String text,File name){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(name));
			writer.write(text);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}