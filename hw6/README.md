# HW6 Testing

deadline: April 17th 11:59pm

## Objectives:

The main goals for this assignment are:

1. Run a mutation tester to evaluate and improve your test suite.
2. Run a fuzzer (Echidna) to find bugs in smart contracts.

Code coverage is the established standard metric for assessing test suite quality. In the Code Coverage exercise, we saw that even with a test suite that achieves the most rigorous measure of coverage (100% feasible path coverage), bugs may still go undetected. In this assignment, we’ll continue the discussion on how to measure and improve test suite quality. Coverage alone is not enough. Test oracles are also important to high quality tests that can detect faults. A test oracle describes the correct output based on an input and is often written as an assertion.

Consider a test suite with full coverage, but without any assertions. Would it be able to find any bugs? 

Yes, it would only be able to find violations in safety properties (things that should be true across all solidity programs), but it would not be able to find violations in functional properties. In our BuyTicket code from Code Coverage exercise, both bugs could be detected without assertions since they trigger EVM panics.

Mutation testing involves modifying the program in small ways to evaluate the quality of a test suite. Each mutated version is called a mutant and tests detect and reject mutants by causing the behavior of the original version to differ from the mutant. This is called killing the mutant. Test suites are measured by their mutation score which is a percentage of mutants that they kill. In this lab you will get familiar with mutation testing through running a mutation tester and designing, implementing, and evaluating your own mutation operator.  

## Setup:

**Pre-requisites**
1. Docker
2. VSCode and dev containers extension 

Once the above software is installed, you should:
1. Start docker 
2. Open the assignment directory in VS Code

A popup should open in the bottom right saying **"Reopen in Container"** - click it. If you don't see it: `Ctrl+Shift+P` (or `Cmd+Shift+P` on Mac) -> search **Reopen in Container** -> Enter. Docker will build the environment and run setup. When it finishes, a terminal will open inside the container with all tools installed.

### Verify Your Install
 
Run these in the container terminal:
 
```bash
solc --version
slither --version
echidna-test --version
```

### Setup Your Files

1. Copy your `CodeCoverage` hardhat project into the hw6 directory
2. Change the solidity pragma version in `BuyTicket.sol` to be `^0.8.0`
3. Fix the bugs in `BuyTicket.sol`. Mutation testing requires the test suite to pass on the current version.

Your directory structure should be as follows:  
hw6/  
├── CodeCoverage/  
│   ├── contracts/  
│   │   └── BuyTicket.sol   
│   ├── test/  
│   │   └── TestBuyTicket.js  
│   ├── hardhat.config.js  
├── run_mutation_testing.sh  
└── README.md  

## Part 1a: Running Gambit

Evaluate the quality of your test suite by generating mutants and ensuring that your test suite fails on them.

Generate mutations using: `gambit mutate --filename CodeCoverage/contracts/BuyTicket.sol`

Inspect some of the mutations its generated. You should see 40+ mutations generated. Useful information about each mutation type is given in the [Gambit documentation](https://docs.certora.com/en/latest/docs/gambit/gambit.html)


## Part 1b: Evaluating and improving your test suite 

run `bash run_mutation_testing.sh` to calcualte the number of killed and surviving mutations.

**Exercise:** Add tests to your `TestBuyTicket.js` which kill the surviving mutants. 


## Part 2: Fuzzing with Echidna

In this part, you will run the fuzzer Echidna on a bug benchmark of 8 programs. 6 of these were the ethernaut programs from HW5. 

1. Level 1: Fallback
2. Level 2: Fal1out
3. Level 4: Telephone
4. Level 5: Token
5. Level 9: King
6. Level 10: Re-entrancy
7. Real world contract 1: [Ragnarok Online Invasion](https://www.bscscan.com/address/0xe48b75dc1b131fd3a8364b0580f76efd04cf6e9c#code)
8. **Extra Credit**: Real world contract 2: Brave3d

## Running Echidna 

You will run Echidna under the *external testing* approach discussed in lecture. 

For the first program, I’ve included some files to help you get started. First, is the `Test.sol`. This is a “harness” which tells Echidna what properties to check for and which functions on `Fallback.sol` we should call. 

Fill in the missing function bodies and run Echidna to try to find a sequence of transactions that violates the assertion in `test_hacked`.

I’ve also included a `config.yaml` file. This is to set various flags for Echidna.  Use this format of Test harness and config for inspiration on the rest of the programs.

Run echidna using the following command in your vscode terminal:

`echidna-test Test.sol --contract Test --config config.yaml --corpus-dir out/`

Feel free to check the `out` directory for coverage information. A * symbol indicates the line has been covered while an 'r' or 'e' symbol indicates that a *reverted* or an *error* (assertion failing) transaction executed that line.

For all of the programs in this benchmark, you knew what the bug was prior to running the fuzzer. For a more realistic setting, I’ve included a program with one injected bug. This one is left as an extra credit problem. Attempt to use Echidna in combination with manual inspection to find the bug.. Enjoy!

**Questions:**

1. For each of the contracts, answer the following questions (no more than 2 sentences each).
    1. Did Echidna find the bug: Y/N
    2. If Y, # of iterations / time taken? (If you didn’t time it just say an estimate.. Was it seconds, minutes, hours?).
    3. If Y, What manual effort was required?
    4. If N, why not? Is this a limitation of fuzzing, Echidna, or something else?
    5. If Y, list the transactions needed to hit that bug.
    6. If Y, Was this the same way you did manually in HW2?
        1. For ROIToken: is this the same way the hacker exploited the vulnerability?
    
2. Answer the following questions: (Only 2-3 sentences each)
    1. List some appealing properties of fuzzing
    2. What are the limitations of fuzzing?
    3. How are bug finding traces minimized?
    4. How do you know if multiple tests find the same bug?
    5. What are used as bug oracles?
    6. How are seeds selected?
    7. How does the fuzzer select which inputs to pick next? (how is the fuzzing guided?)

### Submission 
1. `TestBuyTicket.sol` with the bugs fixed
2. `TestBuyTicket.js` with the added test cases that kill all mutants
3. markdown / text file with answers to the questions in part 2
4. Your `Test.sol` harnesses
