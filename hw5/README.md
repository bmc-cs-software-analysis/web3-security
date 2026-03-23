# HW5 Exploiting Vulnerabilities

deadline: March 30th 11:59pm

In this assignment, you will act as a hacker to exploit vulnerabilities in smart contracts. 

This assignment will require **AT LEAST** .2 test ETH. Be proactive in collecting test ETH. Use the same MetaMask wallet and account that you setup in HW4. 

- [Google Cloud web3](https://cloud.google.com/application/web3/faucet/ethereum/sepolia)  
- [PoW Faucet](https://sepolia-faucet.pk910.de/)  


### Instructions

In this assignment, you will be completing capture the flag style challenges at [https://ethernaut.openzeppelin.com](https://ethernaut.openzeppelin.com//)


**As you complete the levels, take note of the calls you make. You will submit these sequences as part of the assignment.**

For each contract, there is a hidden vulnerability. Exploit the vulnerability to get points for that contract:

1. Study the contract manually
2. Find the vulnerability
3. Exploit the vulnerability using a series of transactions in the console or by constructing an attack contract and deploying it via hardhat
4. Record the series of calls you made for the exploit

For levels that require an attack contract, create a hardhat project using the instructions from HW4. 

**Levels Required:**

1. Level 0: Hello Ethernaut
2. Level 1: Fallback
3. Level 2: Fal1out
4. Level 4: Telephone
5. Level 5: Token
6. Level 9: King
7. Level 10: Re-entrancy

Complete level 0 first as it includes detailed instructions on how to interact with the ethernaut framework. 

For this assignment, submit a text file or pdf answering the following questions for each contract in the benchmark **except Level 0**:

1. Describe the vulnerability in 1-3 sentences
2. What series of calls did you make to exploit the contract? If you made an attack contract, also include that.
3. Is there another series of calls / a different exploit contract that would have also led to an exploit?
4. What could the programmer have done differently to avoid this vulnerability?
5. hash(es) of the transaction(s) you used to beat the level.

**Other questions:**

1. What was the password for Level 0 - Hello Ethernaut?
2. What is the Rubixi bug?

## Grading

You will be graded on the above questions as well as evidence on Etherscan that you successfully completed the level.

### Signing out:

Submit the following files on Gradescope
1. txt or markdown file containing answers to the above questions


