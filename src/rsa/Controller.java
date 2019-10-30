package rsa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;





public class Controller {
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger e;
    private BigInteger phiN;
    private BigInteger zero = new BigInteger(("0"));
    private BigInteger one = new BigInteger(("1"));
    private BigInteger two = new BigInteger(("2"));
    @FXML
    private TextField nField;

    @FXML
    private TextArea mArea;

    @FXML
    private Label outputLabel;

    @FXML
    private void startEncryption(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        Scene scene = new Scene((Parent) FXMLLoader.load(getClass().getResource("encryption.fxml")));
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    @FXML
    private void calculatePQ(ActionEvent event){
        List<BigInteger> pAndQ = new ArrayList<BigInteger>();

        n = new BigInteger(nField.getText());
        BigInteger new_n = n;
        BigInteger i = new BigInteger(("2"));
        long startTime = System.currentTimeMillis();
        while(new_n.compareTo(i) > 0) {
            while(new_n.mod(i).equals(zero)) {
                pAndQ.add(i);
                new_n = new_n.divide((i));
            }
            i = i.add(one);
        }
        if(new_n.compareTo(two) > 0) {
            pAndQ.add(new_n);
        }
        long endTime = System.currentTimeMillis();

        pAndQ.remove((n));
        StringBuilder sb = new StringBuilder();
        p = pAndQ.get(0);
        q = pAndQ.get(1);
        sb.append("p is " + p.toString() + "\n");
        sb.append("q is " + q.toString() + "\n");
        sb.append("Amount of time busy finding p and q:" + (endTime - startTime) + "milliseconds");
        outputLabel.setText(sb.toString());
    }

    @FXML
    private void calculateE(ActionEvent event){
        BigInteger pMinusOne = p.subtract(one);
        BigInteger qMinusOne = q.subtract(one);
        phiN = pMinusOne.multiply(qMinusOne);
        e = two;
        while(true){
            if ((e.gcd(phiN)).equals(one)){
                break;
            }
            else{
                e = e.add(one);
            }
        }
        outputLabel.setText("e is " + e.toString());
    }

    @FXML
    private void encryptMessage(ActionEvent event){
        String m = mArea.getText().toString();
        BigInteger mEncoded = new BigInteger(m);
        mEncoded = mEncoded.pow(e.intValue());
        BigInteger encryptedMessage = mEncoded.mod(n);
        outputLabel.setText("Message after encryption is: " + encryptedMessage.toString());




    }
    @FXML
    private void startDecryption(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        Scene scene = new Scene((Parent) FXMLLoader.load(getClass().getResource("encryption.fxml")));
        dialogStage.setScene(scene);
        dialogStage.show();
    }
}
