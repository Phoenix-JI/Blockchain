package blockChain_main;

import java.util.ArrayList;

import java.util.List;

import javax.xml.bind.DatatypeConverter;

import Encryption_Algorithm.SHA256;
import Encryption_Algorithm.ECDSA;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Transaction {

	public String txId; // transaction ID, hash of the transaction content

	public List<TransactionInput> inputs = new ArrayList<TransactionInput>();; // refer to previous transaction output
	public List<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

	public PublicKey sender; // Sender address

	public PublicKey receiver; // receiver address

	public float amount;

	public String signature;

	private static int TXtimes = 0;

	
	public Transaction(PublicKey sender, PublicKey receiver, float amount, List<TransactionInput> inputs) {

		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
		this.inputs = inputs;
		
		String TXConetent = getStringFromKey(sender) + getStringFromKey(receiver) + Float.toString(amount)
		+ Integer.toString(TXtimes);

		this.txId = SHA256.getHashForStr(TXConetent);
		
		TXtimes++;

	}
	
	public void setTXID(String newtxId) {
		
		this.txId = newtxId;
	}

	public static String getStringFromKey(Key key) {
		return DatatypeConverter.printHexBinary(key.getEncoded());
	}

	// Get TXID by hash the TX content

	public String getTransactionId() {

		String TXConetent = getStringFromKey(sender) + getStringFromKey(receiver) + Float.toString(amount)
				+ Integer.toString(TXtimes);
		
		txId = SHA256.getHashForStr(TXConetent);
		
		return txId;

	}

	// Generate the signature by sign the Transaction with private key

	public String signTransaction(PrivateKey privateKey) {

		//String signMsg = getTransactionId();
		
		String signMsg = txId;

		signature = ECDSA.signECDSA(privateKey, signMsg);

		return signature;

	}

	public boolean verifySign() {

		//String signMsg = getTransactionId();
		String signMsg = txId;

		boolean bool = ECDSA.verifyECDSA(sender, signature, signMsg);
		
		return bool;

	}


	public float getInputsValue() {
		float value = 0;
		for (TransactionInput input : inputs) {
			if (input.UTXO == null)
				continue;
			value += input.UTXO.amount;
		}
		return value;
	}

	
	public float getOutputsValue() {
		float value = 0;
		for (TransactionOutput output : outputs) {
			value += output.amount;
		}
		return value;
	}

	public boolean takeTransaction() {

		if(verifySign() == false) {
			System.out.println("Signature not valid");
			
			return false;
		}
		
		// collect transaction inputs from the UTXO 
		for (TransactionInput i : inputs) {
			i.UTXO = Chain.UTXOs.get(i.transactionOutputId);
		}

		// collect transaction outputs
		float leftAmount = getInputsValue() - amount; // the left amount
		
		// must spend all coins , send back
		outputs.add(new TransactionOutput(this.receiver, amount, txId)); // send amount to receiver
		outputs.add(new TransactionOutput(this.sender, leftAmount, txId)); // send left amount back to sender

		// collect UTXO from outputs list for next transaction

		for (TransactionOutput output : outputs) {
			Chain.UTXOs.put(output.outputId, output);
		}

		// remove transaction inputs from UTXO if referred
		for (TransactionInput input : inputs) {
			if (input.UTXO == null)
				continue;
			Chain.UTXOs.remove(input.UTXO.outputId);
		}

		return true;
	}

	// Sign the transaction input

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
