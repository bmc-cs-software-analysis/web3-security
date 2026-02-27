# HW4 Ethereum and Solidity

deadline: March 15th 11:59pm

In this assignment you will get hands-on experience with Ethereum development. You will create a wallet, interact with a deployed smart contract, write and deploy your own contract, and investigate an incident on chain.

## Part 1: Mint a Course NFT
In this part you will create your own Ethereum account and wallet, and interact with a smart contract deployed on Ethereum's Sepolia test network to mint a Web3 Security Course NFT.

### 1. Setting up your Ethereum wallet

A wallet is an interface that allows you to interact with your Ethereum account. The first step in this assignment is to create a [MetaMask](https://metamask.io/) wallet and account. Install MetaMask as a browswer extension.

> **Security warning**: During setup, MetaMask will give you a Secret Recovery Phrase (12 words) and a private key. Never share these with anyone, never commit them to GitHub, and store them somewhere safe. Anyone with access to these can take full control of your account and any funds in it. In this assignment you will be putting your private key in a .env file. Make sure this file is in your .gitignore

Since we'll be working on the testnet, follow [these instructions](https://support.metamask.io/configure/networks/how-to-view-testnets-in-metamask/) to enable testnets in MetaMask. 

### 2. Get some Test ETH 

You'll need Sepolia ETH to pay for gas on transactions. Visit one or more of the following faucets. Each has a daily limit so you may need to use multiple:

- [Google Cloud web3](https://cloud.google.com/application/web3/faucet/ethereum/sepolia)  
- [Chainlink](https://faucets.chain.link/sepolia)  
- [Infura Faucet](https://www.infura.io/faucet/sepolia)  
- [PoW Faucet](https://sepolia-faucet.pk910.de/)  

In future assignments you will need more test ETH. We recommend visiting the faucets daily to build up a balance.

### 3. Set up an RPC Endpoint

To send transactions to the Sepolia network, you need a way to communicate with an Ethereum node. We don't have an Ethereum node at Bryn Mawr (yet!) so you will need to use Infura, a service that hosts Ethereum nodes and exposes them via an RPC URL. RPC (Remote Procedure Call) is a protocol that lets you call functions on a remote server. In this case, the remote server is an Ethereum node, connected to the Seoplia network, hosted by Infura.

Create an account and API Key on the [Infura website](https://www.infura.io/). 

### 4. Send a sequence of transactions to mint a course NFT

You will write a Node.js script that sends two transactions to our course NFT contract deployed at: 0x229e039b1605bD1C01247bD5ee5714ba5F0Cc3a9.

You can view the contract and its functions on [Sepolia Etherscan](https://sepolia.etherscan.io/address/0x229e039b1605bD1C01247bD5ee5714ba5F0Cc3a9).

File 1 of 15: Web3CourseNFT.sol is the code you will be interacting with. The other 14 files are included library files.

#### Setup:

1. Make sure you have Node.js installed (`node --version`). If not, download the latest version.
2. Install dependencies with `npm install ethers dotenv` in your hw4 directory.
3. Create a `.env` file:
```
SEPOLIA_RPC_URL=https://sepolia.infura.io/v3/YOUR_INFURA_KEY
PRIVATE_KEY=your_wallet_private_key
```

You can later read from it using `process.env.VAR_NAME`

#### Your Task: mint.js

Write a `mint.js` script that does the following:

1. Connects to Sepolia using your RPC URL
2. Loads your wallet from your private key
3. Connects to the NFT contract using its address and ABI
4. Calls `enterAddressIntoBook(string name)` with your name and waits for it to be mined
5. Calls `mintNFT()` and waits for it to be mined

Documentation (ethers.js):
1. [JsonRPCProvider](https://docs.ethers.org/v6/api/providers/jsonrpc/#JsonRpcProvider) - connecting to a network via an RPC URL
2. [Wallet](https://docs.ethers.org/v6/api/wallet/#Wallet) - loading a wallet from a private key
3. [Contract](https://docs.ethers.org/v6/api/contract/#Contract) - connecting to a deployed contract
4. [ContractTransactionResponse](https://docs.ethers.org/v6/api/contract/#ContractTransactionResponse) - the object returned when you call a contract function, and how to use `.wait()`

Run your code with `node mint.js`

#### Viewing your transactions
Head over to Etherscan to inspect your transactions. After a minute or so, you should see your transactions to the [contract](https://sepolia.etherscan.io/address/0x229e039b1605bD1C01247bD5ee5714ba5F0Cc3a9).

#### Viewing your NFT
You can view your NFT in metamask by selecting "import NFT" and pasting the address and token ID. You can view your token ID in the etherscan overview for your `mintNFT` transaction.

For this part, you will only need to submit a README with:
1. Your wallet address (public key)
2. The plaintext of the name you registered in the address book

## Part 2: Programming in Solidity 

In this part, you will learn:
1. The basic syntax and semantics of the Solidity language
2. How to create an application which accepts, holds, and transfers Ethereum
3. How fork an Ethereum network
4. Navigating Hardhat framework

In this part you will write a smart contract, in Solidity, for an NFT auction. The auction will be for the course NFT you minted in Part 1. 

### Hardhat Setup
[Hardhat](https://hardhat.org/hardhat-runner/docs/getting-started#overview) is a development environment for Ethereum software.

1. Create an npm project by going to an empty folder, running `npm init`, and following its instructions. 
2. Install hardhat with `npm install --save-dev hardhat@hh2`
3. To create the sample project, run `npx hardhat init` in your project folder:

```
$ npx hardhat init
888    888                      888 888               888
888    888                      888 888               888
888    888                      888 888               888
8888888888  8888b.  888d888 .d88888 88888b.   8888b.  888888
888    888     "88b 888P"  d88" 888 888 "88b     "88b 888
888    888 .d888888 888    888  888 888  888 .d888888 888
888    888 888  888 888    Y88b 888 888  888 888  888 Y88b.
888    888 "Y888888 888     "Y88888 888  888 "Y888888  "Y888

👷 Welcome to Hardhat v2.26.5 👷‍

? What do you want to do? …
❯ Create a JavaScript project
  Create a TypeScript project
  Create a TypeScript project (with Viem)
  Create an empty hardhat.config.js
  Quit
```

and create the JavaScript project. 

Once you’re done, you should have a placeholder `Lock` project with the following directory structure:

```
HW4/
├── contracts/
│   └── Lock.sol
├── test/
│   └── Lock.js
└── hardhat.config.js
```


Replace the `Lock` placeholder files with our hw4 files: `Auction.sol` and `Auction.js`.

### Implement your NFT Auction

The auction has two roles:
- **Owner** - The owner is the account that deployed the smart contract. The owner, and only hte owner, should be able to do the following: start the auction, end the auction, payout the winner.
- **Bidder** - The bidders are competing to win the NFT. They should be able to do the following: make a bid, up a bid if someone outbids them, get their funds returned if they lost the auction.

The contract has three states:
- **Idle** — the contract has been deployed but the auction has not started
- **Active** — the auction is open and accepting bids
- **Closed** — the auction has ended. The winner can be paid out and losers can be refunded

A typical sequence of events might be as follows:
1. **Owner** opens the auction
2. **Bidder1** makes a bid
3. **Bidder2** makes a higher bid, becoming the new highest bidder
4. **Bidder1** ups their bid to reclaim the lead
5. The auction continues until the **Owner** closes it
6. **Owner** calls `payoutWinner` to transfer the NFT to the highest bidder
7. Losing bidders each call `refund` to reclaim their ETH

Open `Auction.sol` and implement each function following the inline comments. A summary of what each function should do:

- `startAuction()` — set the startTime to the current block timestamp. Only the owner can call this.
- `endAuction()` — set the endTime to the current block timestamp. Only the owner can call this, and only while the auction is active.
- `makeBid()` — accept a bid only if the auction is active, the sender has not yet bid, and the bid is higher than the current highest bid. Update highestBidder, highestBid, and fundsPerBidder.
- `upBid()` — allow an existing bidder to increase their bid. Only valid if the auction is active, the sender has already bid, and their new total exceeds the current highest bid. Update highestBidder, highestBid, and fundsPerBidder.
- `refund()` — allow a losing bidder to reclaim their funds after the auction closes. Only non-winners can be refunded. Update fundsPerBidder and transfer the ETH back to the caller.

- `payoutWinner()`  is already implemented for you — study it to understand how the NFT transfer works.

Hint: For sending ETH, you only need the recipient's address. Use Solidity's transfer function. For refunds, design the function so each loser calls it individually rather than refunding all losers in a batch. You will see in Part 4 why batch refunds are a dangerous pattern.

### Testing your Auction

Run `npx hardhat test` to test your Auction. All tests should pass except one:

```
Auction
     Payout Winner
     The winner's balance should go to 0:
     Error: Transaction reverted: function call to a non-contract account
```


This error is coming from a test of our `payoutWinner` function. This function, which we’ve implemented, transfers the NFT to the winner.


```sol
function payoutWinner() public {
        fundsPerBidder[highestBidder] = 0;
        nft.enterAddressIntoBook("auction");
        nft.mintNFT();
        nft.transferFrom(address(this), highestBidder, 2);
}
```

The hardhat test is failing due to the calls on the `nft` object. 

This error means that the NFT contract address we passed in the constructor doesn’t exist… Let’s see the address we sent to the constructor in line 17 of our hardhat test:

```js
const auction = await Auction.deploy("0x345565c62EFB2859769b6Ee887577123C550a6Ff", 1);
```

If you check that address in the Etherscan Sepolia test network, you’ll see it is our hw4 NFT contract. So, why does Hardhat complain that the contract doesn’t exist?

Hardhat has no knowledge of the Ethereum networks. The tests start with a blank slate blockchain with no contracts and only a small set of initial accounts.

## Part 3: Forking the Ethereum Network 

In this part you will learn how to fork the test net so Hardhat has knowledge of our hw4 NFT contract.

**What is a fork?** Forking an Ethereum network means creating a local copy of its state at a specific point in time. After forking, your local Hardhat blockchain will start with all the same contracts, balances, and storage as the real network, but any transactions you run stay local and don't affect the real chain.

**A quick example: reading Vitalik's balance**
Vitalik Buterin's Ethereum address is publicly known: 0xd8dA6BF26964aF9D7eEd9e03E53415D37aA96045. On mainnet, he holds a significant amount of ETH. Without forking, Hardhat has no idea this address exists. With forking, we can impersonate it, read its balance, and even spend its funds in our local tests.
In a different directory, init a hardhat project and add this to your hardhat.config.js:

```
require("@nomicfoundation/hardhat-toolbox");

module.exports = {
  solidity: "0.8.28",
  networks: {
    hardhat: {
      forking: {
        url: "https://mainnet.infura.io/v3/YOUR_INFURA_KEY"
      }
    }
  }
};
```

Then in a script (`vitalik.js`):
```
const { ethers } = require("hardhat");

async function main() {
  const vitalik = "0xd8dA6BF26964aF9D7eEd9e03E53415D37aA96045";
  const balance = await ethers.provider.getBalance(vitalik);
  console.log("Vitalik's balance:", ethers.formatEther(balance), "ETH");
}

main().catch(console.error);
```

Run it with npx hardhat run `scripts/vitalik.js` and you'll see his real mainnet balance.

Make sure it matches what you see on Etherscan. 

**Forking Sepolia for the hw4 NFT contract**:
The same technique fixes our `payoutWinner` test. Instead of forking mainnet, we fork Sepolia so Hardhat knows about our deployed NFT contract.

Update your `hardhat.config.js` in your auction directory to fork the mainnnet and run `npx hardhat test` again. All tests including `payoutWinner` should pass, because Hardhat now has a local copy of the Sepolia state including our NFT contract at 0x229e039b1605bD1C01247bD5ee5714ba5F0Cc3a9.

When working on projects that require interaction with contracts that are already deployed (and the state that they have) we have two options:
1. Create a mock which implements a fake version of the necessary functions of the already deployed contract or
2. Fork the Ethereum network

Creating a mock is not the best option beause creating a realistic mock environment is very difficult. 

## Part 4: On Chain Investigations

In this part, you will learn what can go wrong if your auction code is not secure. 

In April 2022, [AkuDreams](https://hoo.be/aku.dreams) launched an NFT auction. Due to a bug, over 15,000 ETH (45Million USD) were locked in the smart contract. The owners and bidders could not retrieve their funds. 

Read this report on the bug and vulnerability that caused the lock: 

https://kyrianalex.substack.com/p/akudreams-exploit?r=755lh&s=w&utm_campaign=post&utm_medium=web&utm_source=direct

AkuAuction Contract: 

https://etherscan.io/address/0xf42c318dbfbaab0eee040279c6a2588fa01a961d

Answer the following questions:

1. Describe both bugs in a few sentences. 
2. Inspect all of the transactions made from the attacker’s account to the Akutar: Deployer [https://etherscan.io/address/0x4c6731d49a8667fa5e853ef2f586e9c7f73c3d72](https://etherscan.io/address/0x4c6731d49a8667fa5e853ef2f586e9c7f73c3d72). Read them in chronological order. Hint: Decode the “Input Data” by hitting View Input As UTF-8. Describe with the hacker locked the funds and what happened after the article you read was published.
3. Your auction has a major bug that will lock funds. What is it? 
4. There is also a centralization that allows the owner to manipulate the outcome of the auction. What is it?


Lastly, study the following transactions:

https://etherscan.io/tx/0x183be142c6593e985039c9b9630ab600e2ab42428f93f27e420f5a52d421a3f2

https://etherscan.io/tx/0x7b8ce252d1810ac8dd3c94b48dee03d39a52b0b3e5f7d5ad4c50790f8d8dfd56

Answer the following questions
1. How much money was sent in these transactions? In both ether and USD.
2. What address was the eth sent to? What is the signifiance on this address?
3. Briefly describe the message included.

### Signing out:
Submit the following files on Gradescope
1. README with your wallet address and name you used in the address book (part 1) and answers to the questions in Part 4
2. `Auction.sol` 

