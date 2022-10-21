package blockChain_main;

import java.security.Key;
import java.util.ArrayList;
import Encryption_Algorithm.SHA256;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import com.google.gson.*;


public class Chain {

	public static List<Block> blockChain = new ArrayList<Block>();

	// Block Integrity Validation
	public static Boolean isValidChain() {
		
		Block currentBlock; 
		Block previousBlock;

		for(int i=1; i < blockChain.size(); i++) {
			currentBlock = blockChain.get(i);
			previousBlock = blockChain.get(i-1);
		
			if(!currentBlock.currentHash.equals(currentBlock.getCurrentHash()) ){
				System.out.println("Current Hashes not equal: "+i);			
				return false;
			}
		
			if(!previousBlock.currentHash.equals(currentBlock.previousHash) ) {
				System.out.println("Previous Hashes not equal: "+i);
				return false;
			}
			
			if(previousBlock.index+1 != currentBlock.index)
			return false;
			
			
		}
		return true;
	}

	
	public static String getStringFromKey(Key key) {
		return DatatypeConverter.printHexBinary(key.getEncoded());
	}
	


	public static Wallet wA;
	public static Wallet wB;
	public static Wallet wC;
	
	public static Transaction firstTransaction;
	public static Transaction SecondTransaction;
	public static Transaction ThirdTransaction;
	public static Transaction FourthTransaction;
	
	public static int Reward = 50;
	
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); // blockchain UTXO 
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Demo the basic blockchain without transactions
		
		if (false) {
			
			Block genesisBlock = new Block(0," ",new Date().getTime()," ",0);
			blockChain.add(genesisBlock);
			System.out.println(genesisBlock.currentHash);
			
			//public Block(int index,String previousHash,long timeStamp,String data,int difficulty)
			
			Block block2 = new Block(blockChain.size(),blockChain.get(blockChain.size()-1).currentHash,new Date().getTime(),"Second block",2);
			block2.mineBlock();
			blockChain.add(block2);
			System.out.println(block2.currentHash);
			
			Block block3 = new Block(blockChain.size(),blockChain.get(blockChain.size()-1).currentHash,new Date().getTime(),"Third block",3);
			block3.mineBlock();
			blockChain.add(block3);
			System.out.println(block3.currentHash);
			
			
			System.out.println("BlockChain Size: " + blockChain.size());
			System.out.println("Valid Chain ? " + isValidChain());

			String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
			System.out.println("\nThe block chain: ");
			System.out.println(blockChainJson);

			
		}
		
		
		//Demo the blockchain with transactions
		
		if(true) {
			
	
			wA = new Wallet();
			wB = new Wallet();
			wC = new Wallet();
			
			Wallet sW = new Wallet();
			
		// Genesis block 
			
			firstTransaction = new Transaction(sW.publicKey, wA.publicKey, Reward, null);
			
			firstTransaction.txId = SHA256.getHashForStr("0"+firstTransaction.amount+firstTransaction.sender+firstTransaction.receiver); 
			firstTransaction.signTransaction(sW.privateKey);
			//firstTransaction.txId ="0";
			firstTransaction.outputs.add(new TransactionOutput(firstTransaction.receiver, firstTransaction.amount, firstTransaction.txId)); 
			UTXOs.put(firstTransaction.outputs.get(0).outputId, firstTransaction.outputs.get(0)); 
			
			System.out.println("Create the genesis block");
			
			Block genesisBlock = new Block(0,"0",new Date().getTime(),0);
			genesisBlock.addTransaction(firstTransaction);
			blockChain.add(genesisBlock);
			
			System.out.println("wA  balance : " + wA.getBalance());
			System.out.println("wB  balance : " + wB.getBalance());
			System.out.println("wC  balance : " + wC.getBalance());
	
			System.out.println("----------------------------------");	

		// Block 1
			
			// Coinbase TX for block 1
			
			SecondTransaction = new Transaction(sW.publicKey, wA.publicKey, Reward, null);
			
			SecondTransaction.txId = SHA256.getHashForStr("1"+SecondTransaction.amount+SecondTransaction.sender+SecondTransaction.receiver); 
			SecondTransaction.signTransaction(sW.privateKey);	 
			
			SecondTransaction.outputs.add(new TransactionOutput(SecondTransaction.receiver, SecondTransaction.amount, SecondTransaction.txId)); 
			UTXOs.put(SecondTransaction.outputs.get(0).outputId, SecondTransaction.outputs.get(0));
			
			
			System.out.println("wA  balance : " + wA.getBalance());
			System.out.println("wB  balance : " + wB.getBalance());
			System.out.println("wC  balance : " + wC.getBalance());
			
			
			
			Block block1 = new Block(1,genesisBlock.currentHash,new Date().getTime(),1);
			block1.addTransaction(SecondTransaction);

			
			// Regular TX for block 1
			
			System.out.println("wA sends 10 coins to wB: ");
			block1.addTransaction(wA.sendAmount(wB.publicKey, 10));
			
			
			System.out.println("wA  balance : " + wA.getBalance());
			System.out.println("wB  balance : " + wB.getBalance());
			System.out.println("wC  balance : " + wC.getBalance());
			
			System.out.println("wA sends 20 coins to wB: ");
			block1.addTransaction(wA.sendAmount(wB.publicKey, 20));
			
			System.out.println("wA  balance : " + wA.getBalance());
			System.out.println("wB  balance : " + wB.getBalance());
			System.out.println("wC  balance : " + wC.getBalance());
			
			
			
			// Mine the block 1
			block1.mineBlock();
			blockChain.add(block1);
			
			System.out.println("----------------------------------");

						
		// Block 2
			
			// Coinbase TX for block 2
			ThirdTransaction = new Transaction(sW.publicKey, wA.publicKey, Reward, null);
			
			ThirdTransaction.txId = SHA256.getHashForStr("2"+ThirdTransaction.amount+ThirdTransaction.sender+ThirdTransaction.receiver); 
			ThirdTransaction.signTransaction(sW.privateKey);	 
			
			ThirdTransaction.outputs.add(new TransactionOutput(ThirdTransaction.receiver, ThirdTransaction.amount, ThirdTransaction.txId)); //manually add the Transactions Output
			UTXOs.put(ThirdTransaction.outputs.get(0).outputId, ThirdTransaction.outputs.get(0));
			
			System.out.println("wA  balance : " + wA.getBalance());
			System.out.println("wB  balance : " + wB.getBalance());
			System.out.println("wC  balance : " + wC.getBalance());
			
			
			Block block2 = new Block(2,block1.currentHash,new Date().getTime(),2);
			
			block2.addTransaction(ThirdTransaction);
			
			System.out.println("wB sends 10 coins to wC");
			block2.addTransaction(wB.sendAmount(wC.publicKey, 10));
			

			
			System.out.println("wA  balance : " + wA.getBalance());
			System.out.println("wB  balance : " + wB.getBalance());
			System.out.println("wC  balance : " + wC.getBalance());

			
			// Mine the block 1
			block2.mineBlock();
			blockChain.add(block2);
			
			System.out.println("----------------------------------");
			
			
			
		// Block 3
			
			// Coinbase TX for block 3
			FourthTransaction = new Transaction(sW.publicKey, wC.publicKey, Reward, null);
			
			FourthTransaction.txId = SHA256.getHashForStr("3"+FourthTransaction.amount+FourthTransaction.sender+FourthTransaction.receiver); 
			FourthTransaction.signTransaction(sW.privateKey);	 
			
			FourthTransaction.outputs.add(new TransactionOutput(FourthTransaction.receiver, FourthTransaction.amount, FourthTransaction.txId)); //manually add the Transactions Output
			UTXOs.put(FourthTransaction.outputs.get(0).outputId, FourthTransaction.outputs.get(0));
			
			System.out.println("wA  balance : " + wA.getBalance());
			System.out.println("wB  balance : " + wB.getBalance());
			System.out.println("wC  balance : " + wC.getBalance());
			
			
			Block block3 = new Block(3,block2.currentHash,new Date().getTime(),3);
			
			block3.addTransaction(FourthTransaction);
			
			System.out.println("wC sends 30 coins to wA");
			block3.addTransaction(wC.sendAmount(wA.publicKey, 30));
			

			
			System.out.println("wA  balance : " + wA.getBalance());
			System.out.println("wB  balance : " + wB.getBalance());
			System.out.println("wC  balance : " + wC.getBalance());

			
			// Mine the block 1
			block3.mineBlock();
			blockChain.add(block3);
			
			System.out.println("----------------------------------");
			
		
			
			System.out.println("BlockChain length: " + blockChain.size());
			
			System.out.println("TX length in block1: " + block1.transactions.size());
			System.out.println("TX length in block2: " + block2.transactions.size());
			System.out.println("TX length in block3: " + block3.transactions.size());
			
			System.out.println("Block0 " + genesisBlock.currentHash);
			System.out.println("Block1 " + block1.previousHash);
			System.out.println("Block1 " + block1.currentHash);
			System.out.println("Block2 " + block2.previousHash);
			System.out.println("Block2 " + block2.currentHash);
			System.out.println("Block3 " + block3.previousHash);
			System.out.println("Block3 " + block3.currentHash);
			
			System.out.println("Valid Chain ? " + isValidChain());

			
			
			
		}


	}

}
