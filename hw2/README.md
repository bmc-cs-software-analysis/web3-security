# Homework 2: Bitcoin and Cryptocurrencies

Deadline: Friday February 20th at 11:59pm (on Gradescope)

## Overview

In this assignment, you will combine your knowledge of hash functions, digital signatures, hash pointers, and merkle trees to make a basic prototype of a cryptocurrency. 

## Part 1 - Bitcoin Whitepaper
Read the abstract and sections 1-4, 7-9 of the [Bitcoin Whitepaper](https://bitcoin.org/bitcoin.pdf) and answer the following questions:

1. Section 2 describes transactions as a "chain of digital signatures." Explain why digital signatures alone are not sufficient to prevent someone from spending the same coin twice. What additional mechanism is needed?
2. Section 3 describes linking blocks together using hashes. If an attacker modifies a transaction in an old block, explain step by step why this modification is detectable.
3. Section 9 describes transactions with multiple inputs and outputs. Suppose Alice has two unspent outputs worth 3 coins and 4 coins, and she wants to pay Bob 5 coins. Describe the inputs and outputs of this transaction, including any change. 

## Part 2 - Digital Signatures
Implement a `Wallet` class that implements digital signatures with a simplified RSA that we discussed in class. 

Your digital signature schemes will need the following functions:

`Keys.Key[] generateKeys(int keySize)` which returns an object array of two elements: a `SecretKey` and a `PublicKey`

`publid byte[] sign(byte[] message)`

`static boolean verify(Keys.PublicKey pk, byte[] message, byte[] signature)` 

`public Keys.PublicKey getPublicKey()`

Your constructor `Wallet(int keysize)` should call generateKeys and save the public and private keys as private member variables.

The Java BigInteger library (imported in the template file) has many useful methods. I suggest using `probablePrime`, `modInverse`, `modPow`, `toByteArray` and `mod` in your implementation.

Use `e = 65537` (2¹⁶ + 1) as your public exponent. This is the standard choice in practice.

## Part 3 - Blockchain: An append-only tamper resistant log

Review the [Reverse Blockchain](https://github.com/bmc-cs-software-analysis/web3-lecture-code/tree/main/ReverseBlockchain) code we implemented in Lecture 5.

Your code will be similar to what we did in class, but it should have the following key differences:
1. Each block will point to the previous block rather than the next block. (recalculateAll should no longer be needed)
2. Blocks will hold multiple data elements rather than just one.
3. Rather than documents, each block will store Transactions as data.

First, we will need a `Transaction` class. In this assignment Transactions will be implemented as UTXO and will have inputs (coins being consumed) and outputs (coins being created). `Input`s and `Output`s are defined as sub-classes in `Transaction.java` Each `Input` must refer to a previous transaction and indicate which output is being referred to with an index. Each `Output` has a recipient and a value. Note that `Input` does not have a value as the entire input will be consumed. If there is change leftover, this will be sent as an `Output`. 

In the `Transaction` class implement the following methods:
- `void addInput(byte[] prevTxHash, int prevOutIndex)`
- `void addOutput(double value, PublicKey recipient)`
- `void signInput(int inputIndex, byte[] signature)`
- `byte[] getInputDataToSign(int inputIndex)`
- `void computeHash()`
- `byte[] getHash()`

Recall that each `Input` must be signed since inputs can come from different wallets (joint payment scenario).
Your `sign` method you implemented in `Wallet` (Part 2) takes a `byte[] message` parameter. In order to represent an input as a `byte[]`, implement a `byte[] serialize()` method in the `Input` subclass. This method should return the `prevTxHash` and `prevOutIndex` as a `byte[]`. Since `sign` is implemented in the `Wallet` class, implement an accessor `getInputDataToSign` which returns the serialized version of the specified input index.

After the transaction inputs and outputs are specified, `computeHash` will be executed. It should represent all inputs and outputs as a `byte[]` and be hashed with SHA-256.

### Blocks

Once you are satisfied that your `Transaction` class is functioning properly, move on to modifying the `Block` class we started in lecture to hold a collection of `Transaction`s rather than a single document. Ensure your class has the following methods:

- `Block(List<Transaction> transactions, Block previousBlock)`
- `List<Transaction> getTransactions()`
- `String getTimestamp()`
- `Block getPrevious()`
- `HashPointer getHashPointer()`

### Hash Pointers

Update the `HashPointer` class from lecture to work with the new `Block` structure. Recall that in lecture, `calculateHash()` computed a SHA-256 hash from the block's document, timestamp, and previous hash. Now that blocks no longer store a single document, you will need to change what gets hashed. `calculateHash()` should compute SHA-256 over the concatenation of:
- The hashes of all transactions in the block (concatenated in order)
- The block's timestamp
- The previous block's hash (or "null" for the genesis block)


### The Blockchain

Lastly, implement a `Blockchain` class with the following methods:
- `Blockchain()` - create a genesis block with an empty list of transactions
- `void append(List<Transaction> transactions)` — creates a new Block from the given transactions and appends it to the chain. The new block's previous pointer should reference the current head block, or null if this is the first block in the chain. 
- `Block getHead()` — returns the most recent block in the chain, or null if the chain is empty
- `int getSize()` — returns the number of blocks in the chain
- `boolean validateChain()` — Returns true if the entire chain is intact, false if any block has been tampered with.

At this point I recommend creating a `main` and testing your functionality. Create lists of transactions, and add them to blockchain. Test traversing through the blocks and ensure everything is functioning as expected.

Although they are not strictly autograder, I recommend creating a `display()` method to visualize your blockchain. To display your blockchain effectively, you also need to write `toString()` methods for `Transaction` and `Block`.

## Part 4 - Merkle Trees

Implement Merkle Trees to allow for concise proof of membership in a block. Before we integrate this into blocks, implement the `MerkleTree` class. It should have a `Node root` member variable that stores the root node. Include an accessor `public byte[] getRootHash()`

The constructor `public MerkleTree(List<byte[]> txHashes)` should build the tree and set the `root` member variable accordingly. If the number of transactions is not a power of two, duplicate the last node to ensure a proper structure.

`List<byte[]> getProofOfInclusion(byte[] txHash)` should return a O(logn) sized list of hashes that can be used to verify that the given `txHash` is indeed in the tree. 

`static boolean verifyProof(byte[] txHash, List<byte[]> proof, byte[] rootHash)` should return true if the `txHash` is in the tree with the given `rootHash`.

Once you are satisfied with your `MerkleTree` class and have tested and verified its sub-linear nature, integrate it into the `Block` class. Your `Block` class should have a member variable for its `MerkleTree`. It should also have the following methods for checking if a given transaction is present in the block.

- `List<byte[]> getProofOfInclusion(Transaction tx)`
- `boolean verifyProof(byte[] txHash, List<byte[]> proof)`


## Part 5 - Putting it all together...
Implement `CentralCoin` that we discussed in class. `theMint` is a `Wallet` type member variable. This wallet will be a centralized party that validates transactions. The Mint is responsible for the following operations:

- Minting: `Transaction createCoins(double amount, PublicKey recipient)`
- Validating Transactions: `boolean processTransaction(Transaction tx)`
- Mining a Block: `Block mineBlock()`
- Rebuilding the UTXOPool: `void rebuildUTXOPool()`

To validate transactions efficiently, we will need a `UTXOPool` class that will keep track of which transaction outputs have been created but not yet spent. The `UTXOPool` should be a wrapper around a data structure of your choosing which supports the following operations:
- `void addUTXO(byte[] txHash, int outIndex, Transaction.Output output)` 
- `void removeUTXO(byte[] txHash, int outIndex)` 
- `Transaction.Output getOutput(byte[] txHash, int outIndex)`
- `boolean contains(byte[] txHash, int outIndex)`
Ensure you have a default constructor that properly initializes the pool.

Test your cryptocurrency under the following workflow:
 * 1. Transactions accumulate in pending pool
 * 2. mineBlock() packages them into a block with merkle tree
 * 3. Block is appended to blockchain
 * 4. UTXO pool is updated to reflect new state

We've included a `Demo.java` to assist in understanding the difference components and workflow of your cryptocurrency.

## Signing Out
Submit the following files on Gradescope:
1. Wallet.java
2. Transaction.java
3. Block.java 
4. MerkleTree.java
5. HashPointer.java 
6. Blockchain.java 
7. UTXOPool.java 
8. CentralCoin.java 
9. A text/markdown file with answers to Part 1

