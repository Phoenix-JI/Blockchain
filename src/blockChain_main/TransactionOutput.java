package blockChain_main;

import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;

import Encryption_Algorithm.SHA256;

import java.security.Key;
import java.security.PublicKey;

import Encryption_Algorithm.ECDSA;

public class TransactionOutput {

	public String outputId; // the id of the output for UTXO
	public PublicKey receiverAdd;
	public float amount;
	public String TXId; // corresponding transaction ID

	public TransactionOutput(PublicKey receiverAdd, float amount, String TXId) {
		this.receiverAdd = receiverAdd;
		this.amount = amount;
		this.TXId = TXId;
		this.outputId = SHA256.getHashForStr(getStringFromKey(receiverAdd) + Float.toString(amount) + TXId);
	}

	public static String getStringFromKey(Key key) {
		return DatatypeConverter.printHexBinary(key.getEncoded());
	}

	// Check the owner of coins
	
	public boolean coinBeMe(PublicKey publicKey) {
		return (publicKey == receiverAdd);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
