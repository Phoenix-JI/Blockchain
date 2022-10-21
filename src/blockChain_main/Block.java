package blockChain_main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Encryption_Algorithm.SHA256;

public class Block {

	public int index;
	public String currentHash;
	public String previousHash;
	public long timeStamp;
	public String data;

	public int difficulty;
	public int nonce = 0;

	public List<Transaction> transactions = new ArrayList<Transaction>();



	 // for TX
	public Block(int index, String previousHash, long timeStamp, int difficulty) {

		this.index = index;
		this.previousHash = previousHash;
		this.timeStamp = timeStamp;
		this.difficulty = difficulty;

		this.currentHash = getCurrentHash();
		

	}

	 // for blockchain witout TX
	public Block(int index, String previousHash, long timeStamp, String data, int difficulty) {

		this.index = index;
		this.previousHash = previousHash;
		this.timeStamp = timeStamp;
		this.data = data;
		this.difficulty = difficulty;

		this.transactions = null;
		
		this.currentHash = getCurrentHash();

	}

	public String getCurrentHash() {

		if (data == null) {

			String data = "";

			for (Transaction tx : transactions) {
				data = data + tx.txId;

			}
		}
		String strHash = Integer.toString(index) + previousHash + timeStamp

				+ data + Integer.toString(difficulty) + Integer.toString(nonce);

		String getHash = SHA256.getHashForStr(strHash);

		return getHash;
	}



	// Proof of Work
	public void mineBlock() {

		String prefix0 = getPrefix0(difficulty);
		System.out.println("prefix0: " + prefix0);

		if (data == null) {

			String data = "";

			for (Transaction tx : transactions) {
				data = data + tx.txId;

			}
		}

		while (true) {

			currentHash = SHA256
					.getHashForStr(Integer.toString(index) + previousHash + timeStamp + data + difficulty + nonce);

			if (currentHash.startsWith(prefix0)) {
				System.out.println("Mined the block !");
				System.out.println("hash: " + currentHash);
				System.out.println("nonce: " + nonce);
				return;

			} else {

				nonce++;
			}
		}
	}

	// get prefix 000......
	private static String getPrefix0(int diff) {
		if (diff <= 0) {
			return null;
		}
		return String.format("%0" + diff + "d", 0);
	}

	public boolean addTransaction(Transaction transaction) {

		if (transaction == null)
			return false;

		if (transaction.inputs != null) {  // if null -> coinbase TX

			if ((transaction.takeTransaction() == false)) {
				System.out.println("Transaction failed ");
				return false;
			}
		}

		transactions.add(transaction);
		System.out.println("Block Get the Transaction ");
		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
