const { ethers } = require("ethers");
require("dotenv").config();


// TODO: Your name as you want it registered on the contract (hashed)
const YOUR_NAME = "";

// Contract details
const CONTRACT_ADDRESS = "0x229e039b1605bD1C01247bD5ee5714ba5F0Cc3a9";
const ABI = [];

async function main() {
  // TODO: Connect to Sepolia using your RPC URL from .env
  const provider = ;

  // TODO: Load your wallet from your private key in .env
  const wallet = ;

  // Check your balance before doing anything
  const balance = await provider.getBalance(wallet.address);
  console.log("Wallet balance:", ethers.formatEther(balance), "ETH");
  if (balance === 0n) {
    console.error("No Sepolia ETH! Visit one of the faucets in the README.");
    return;
  }

  // TODO: Connect to the NFT contract
  const contract = ;

  // TODO: Call enterAddressIntoBook() with your name and wait for it to be mined
  // then print the transaction hash


  // TODO: Call mintNFT() and wait for it to be mined
  // then print the transaction hash

}

main().catch(console.error);
