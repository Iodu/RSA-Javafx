package rsa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rsa.Main;

public class Controller {
    private int pAndQ;
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger e;
    private BigInteger d;
    private BigInteger phiN;
    private BigInteger zero = new BigInteger(("0"));
    private BigInteger one = new BigInteger(("1"));
    private BigInteger two = new BigInteger(("2"));


    @FXML
    private TextField nField;

    @FXML
    private TextField pField;

    @FXML
    private TextField qField;

    @FXML
    private TextArea mArea;

    @FXML
    private TextField nFieldDecrypt;

    @FXML
    private TextField eFieldDecrypt;

    @FXML
    private TextArea cArea;

    @FXML
    private Label outputLabel;

    @FXML
    private Button nextEButton;


    @FXML
    private void startOver(ActionEvent event) throws IOException {
        p = null;
        q = null;
        d = null;
        e = null;
        n = null;
        phiN = null;
        pAndQ = 0;
        Node source = (Node) event.getSource();
        Stage dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        Scene scene = new Scene((Parent) FXMLLoader.load(getClass().getResource("main.fxml")));
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    @FXML
    private void copyToClipboard(ActionEvent event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(outputLabel.getText());
        clipboard.setContent(content);
    }

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
    private void useN(ActionEvent event) {
        if (pAndQ == 2) {
            outputLabel.setText("You have already chosen to use P and Q, there is no way back now!");
        } else {
            pAndQ = 1;
            nField.setVisible(true);
        }
    }

    @FXML
    private void usePQ(ActionEvent event) {
        if (pAndQ == 1) {
            outputLabel.setText("You have already chosen to use N, there is no way back now!");
        } else {
            pAndQ = 2;
            pField.setVisible(true);
            qField.setVisible(true);
        }
    }

    @FXML
    private void calculatePQ(ActionEvent event) {
        if (pAndQ == 2) {
            p = new BigInteger(pField.getText());
            q = new BigInteger(qField.getText());
            n = p.multiply(q);
            outputLabel.setText("Value of N: " + n.toString());
        } else {
            List<BigInteger> pAndQ = new ArrayList<BigInteger>();
            n = new BigInteger(nField.getText());
            BigInteger new_n = n;
            BigInteger i = new BigInteger(("2"));
            long startTime = System.currentTimeMillis();
            while (new_n.compareTo(i) > 0) {
                while (new_n.mod(i).equals(zero)) {
                    pAndQ.add(i);
                    new_n = new_n.divide((i));
                }
                i = i.add(one);
            }
            if (new_n.compareTo(two) > 0) {
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
    }

    @FXML
    private void calculateE(ActionEvent event) {
        BigInteger pMinusOne = p.subtract(one);
        BigInteger qMinusOne = q.subtract(one);
        phiN = pMinusOne.multiply(qMinusOne);
        e = two;
        while (true) {
            if ((e.gcd(phiN)).equals(one)) {
                break;
            } else {
                e = e.add(one);
            }
        }
        nextEButton.setVisible(true);
        outputLabel.setText("e is " + e.toString());
    }

    @FXML
    private void calculateNextE(ActionEvent event) {
        e = e.add(one);
        while (true) {
            if ((e.gcd(phiN)).equals(one)) {
                break;
            } else {
                e = e.add(one);
            }
        }
        outputLabel.setText("e is " + e.toString());
    }

    @FXML
    private void encryptMessage(ActionEvent event) {
        String m = mArea.getText();
        StringBuilder sb = new StringBuilder();
        byte[] byteValues = m.getBytes();
        for (byte byteValue : byteValues) {
            BigInteger bigIntegerValue = new BigInteger(Byte.toString(byteValue));
            bigIntegerValue = bigIntegerValue.pow(e.intValue());
            bigIntegerValue = bigIntegerValue.mod(n);
            sb.append(bigIntegerValue.intValue() + ",");
        }
        outputLabel.setText(sb.toString().replaceFirst(".$", ""));
    }

    @FXML
    private void startDecryption(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        Scene scene = new Scene((Parent) FXMLLoader.load(getClass().getResource("decryption.fxml")));
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    @FXML
    private void calculateD(ActionEvent event) {
        n = new BigInteger(nFieldDecrypt.getText());
        e = new BigInteger(eFieldDecrypt.getText());
        List<BigInteger> pAndQ = new ArrayList<BigInteger>();
        BigInteger new_n = n;
        BigInteger i = new BigInteger(("2"));
        long startTime = System.currentTimeMillis();
        while (new_n.compareTo(i) > 0) {
            while (new_n.mod(i).equals(zero)) {
                pAndQ.add(i);
                new_n = new_n.divide((i));
            }
            i = i.add(one);
        }
        if (new_n.compareTo(two) > 0) {
            pAndQ.add(new_n);
        }
        pAndQ.remove((n));
        p = pAndQ.get(0);
        q = pAndQ.get(1);
        BigInteger pMinusOne = p.subtract(one);
        BigInteger qMinusOne = q.subtract(one);
        phiN = pMinusOne.multiply(qMinusOne);
        d = e.modInverse(phiN);
        outputLabel.setText("d is " + d.toString());
    }

    @FXML
    private void decryptMessage(ActionEvent event) throws UnsupportedEncodingException {
        String c = cArea.getText();
        String[] stringValuesArray = c.split(",");
        byte[] byteValuesArray = new byte[stringValuesArray.length];
        int i = 0;
        for (String value : stringValuesArray) {
            BigInteger bigIntegerValue = new BigInteger(value);
            bigIntegerValue = bigIntegerValue.pow(d.intValue());
            byteValuesArray[i] = bigIntegerValue.mod(n).byteValueExact();
            i++;
        }
        outputLabel.setText("Message after decryption is: " + new String(byteValuesArray));
    }
}
