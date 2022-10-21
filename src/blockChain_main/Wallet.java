package blockChain_main;

import java.security.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;
import java.security.spec.ECGenParameterSpec;

import Encryption_Algorithm.ECDSA;

public class Wallet {

	public PrivateKey privateKey; // To sign the TX
	public PublicKey publicKey; // Wallet Address to send or receive the coins

	public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

	public Wallet() {
		generateKeyPair();
	}

	public void generateKeyPair() {

		KeyPair keyPair;

		try {

			keyPair = ECDSA.getKeyPair();

			privateKey = keyPair.getPrivate();

			publicKey = keyPair.getPublic();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// get balance from UTXO <- inputs refer to this, the balance are UTXO not coins

	public float getBalance() {
		
		float value = 0;
		
		for (Map.Entry<String, TransactionOutput> item : Chain.UTXOs.entrySet()) { // Get wallet UTXO from chain UTXO
			TransactionOutput UTXO = item.getValue();
			if (UTXO.coinBeMe(publicKey)) { // Check whether the UTXO coins belong to the wallet
				UTXOs.put(UTXO.outputId, UTXO); // add it to wallet UTXO.
				value = value+ UTXO.amount;
			}
		}
		return value;
	}

	// Process the  new transaction from this wallet and update the UTXO
	
	public Transaction sendAmount(PublicKey receiver, float amount) {

		if (getBalance() < amount) { // gather balance and check funds.
			System.out.println("Coins are not enough");
			return null;
		}

		// Adding the UTXO until the amount is enough
		
		List<TransactionInput> inputs = new ArrayList<TransactionInput>();

		
		// Inputs refer to the UTXO
		float value = 0;
		for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) { 

			TransactionOutput UTXO = item.getValue();
			value = value + UTXO.amount;
			inputs.add(new TransactionInput(UTXO.outputId));

			if (value > amount)
				break;
		}

		// Create the new TX and sign it
		Transaction TX = new Transaction(publicKey, receiver, amount, inputs);
		System.out.println("Transaction ID:  "+TX.txId);
		TX.signTransaction(privateKey);
		System.out.println("Transaction Sig:  "+TX.signature);

		// UTXO remove the spent output
		for (TransactionInput input : inputs) {
			UTXOs.remove(input.transactionOutputId);
		}

		return TX;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub



	}

}
