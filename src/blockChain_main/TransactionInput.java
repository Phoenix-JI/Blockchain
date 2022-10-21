package blockChain_main;

import java.util.ArrayList;
import Encryption_Algorithm.SHA256;

public class TransactionInput {

	public TransactionOutput UTXO;

	public String transactionOutputId; // Refer to previous transactionId

	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}

}
