# Protocols for Byzantine Agreement

Deadline: Friday 2/27/26 at 11:59pm

In this assignment, you will implement two protocols for byzantine assignment. 
Lecture 7 slides will be a useful reference in this assignment.

## Part 1: Two Round Signed Protocol 

First, you will implement the two round signed protocol with digital signatures. It functions as follows:

```
Round 1: Leader (party 1) sends message <v, σ_1> to all parties
Round 2: If party i receives <v, σ_1> from leader,
            then add v to V_i and send <v, σ_1> to all
End of round 2: 
         If party i receives <v, σ_1> from anyone
            then add v to V_i
Decision rule:
         If the set V_i contains only a single value v,
            then output v
         Otherwise output a default value 
```

We provide a `Message` class as a convenient structure for `<value, signature>` pairs. 
You will implement methods in the `Party` and `ByzantineAgreement` files.

Since the protocol involves digital signatures, copy over your `Wallet.java` (and `Keys.java`) from HW2.

### Party Class

Each party has a wallet for signing messages. In this part, only the general signs messages, but in Part 2 (Dolev-Strong), all parties will add their signature to the chain. 

A `Party` has a `msgs` member variable which corresponds to (`V_i`) in the psuedo code. 

Implement the following methods:
- `Message sign(int v)` should sign the input value with the party's wallet and return the value and signature as a message object
- `void send(Party receiver, Message msg, Map<Integer, Keys.PublicKey> PKI)` should function differently based on if the party is honest or dishonest / faulty. If the party is honest, it should simply relay the given message to the reciever. If the party is dishonest it can deviate from the protocol in any way. For dishonest behavior, do not choose one particular protocol deviation. Instead, your code should non-deterministically deviate in any way. 
- `void receive(Message msg, Map<Integer, Keys.PublicKey> PKI)` should validate that the message was signed by the general and add it to V_i if so. The `PKI` parameter is a mapping from the party number to its public key. This is used to verify signatures.
- `void decide()` If the party is honest, it should set the `output` member variable according to the protocol. If the party is dishonest, it can deviate from the protocol in any way. 


### Byzantine Agreement Class

Implement the following methods:
- `static boolean validity(int v, List<Party> parties)` should return true iff validity is satisfied
- `static boolean agreement(List<Party> parties)` should return true iff agreement is satisfied
- `static boolean protocol(List<Party> parties, Map<Integer, Keys.PublicKey> PKI)` should run both rounds of the protocol and return true iff both validity and agreement are satisfied. In round 1 of the protocol, the general sends the signed bit to all other parties. If the general is dishonest, the non-deterministic deviation should be handled in `send`. In round 2: Relay (send) any messages recieved in round 1 to all other parties. Honest / dishonest behavior of non-general parties should also be handled in `send`. Make sure to also consider dishonest deviation from the protocol external to the `send` primitive. For example, a dishonest non-general party could send a message in round 1. This nondeterministic deviation should be implemented in the `protocol` method. Lastly, there is a decision round.

In the main, test your protocol under various settings of n and t. Test if validity and agreement are satisfied. Since the dishonest send is non-deterministic, run your protocol **many** times under each setting. If you ever hit a case where validity and agreement are not met, you know that it is not satisfied for the protocol. However, seeing 1 run for which they ARE satisfied does not mean the protocol satisfies BG. Make sure to run a sufficient number of trials

Fill out the following table with a row for each value of n and t you tested. Indicate which Pis were dishonest and if validity and agreement were satisfied:

|     | dishonest parties |  valildity?  | agreement?  | 
| --- | ----------------- | ------------ |------------ |
| n,t |                   |              |             |


If you find cases where validity and agreement do not hold, explain in words the situation that occured. 


## Part 2: Dolev-Strong
In this part, you will implement the Dolev-Strong protocol.

```
Round 1: Leader (P1) sends message <v, σ₁> to all parties

Round k=2..|t|+1: If Pi receives <v, σ₁, σ₂, …, σk₋₁> 
			   with k-1 valid distinct signatures (including P1s) and v ∉ V_i
			   add v to V_i and send <v,σ₁,…, σₖ₋₁ Pi.sign(v)> to all parties

Decision rule:
         	If the set V_i contains only a single value v,
            		then output v
         	Otherwise output a default value 
```

This differs from the previous algorithm in two major ways:   
1. Number of rounds
2. Each message has a signature chain rather than a single signature by the general. This indicates the party which is sending it. It also indicates which round it was received by the length of the signature chain.


Again, we provide a `Message` class (`DSMessage.java`). The `Message` structure for Dolev Strong differs in the following ways:
1. Each message contains *list* of signatures rather than a single signature. 
2. The `Signature` object is now a class that contains both the raw `byte[]` and the party id. This is necessary for this component since we will need to verify that messages were signed by particular parties (not just verifying the general's signatures as in Part 1).
3. We also include a number of methods to expose access to the list of signatures (`getSignatures()`, `chainLength()`, `addSig(Signature sig)`.

### DSParty Class

Similarily, we provide a modified party class (`DSParty.java`) which is different from Part1's `Party.java` in the following ways:
1. Each party has a `partyId` so it can be identified in the PKI and in the signature chain.
2. The `sign` message now takes a `DSMessage` rather than just a value. 
3. `send` and `receive` take the round number (`roundNum`) as an argument. 
4. A new method `relay` is added. This is different from `send` which delivers a single specified message to a single specified receiver, and is used by the leader in round 1. `relay` captures the full forwarding behavior of a non-leader party. In `relay`, a `Party` should add its own signature to the message and broadcasts the extended message to all other parties. 

Implement the following methods:
- `DSMessage.Signature sign(DSMessage msg)` should sign the message by serializing the message value and signature chain as a `byte[]` and returning the signature. Your `Wallet` code from hw2 will also be used here.
- `void send(DSParty receiver, DSMessage msg, Map<Integer, Keys.PublicKey> PKI)` should again function differently based on if the party is honest or dishonest / faulty. If the party is honest, it should deliver the message to the receiver by calling receiver.receive(). If the party is dishonest, it can deviate from the protocol in any way.
- `void receive(DSMessage msg, int roundNum, Map<Integer, Keys.PublicKey> PKI)` should validate the incoming message and add it to `V_i` if it is valid. A message is valild iff all of the following hold:
1. The chain length equals roundNum (correct number of signatures for this round).
2. The first signature in the chain is a valid signature from the leader.
3. Every signature in the chain is a valid signature verified by the signer's public key.
- `void relay(List<DSParty> allParties, int roundNum, Map<Integer, Keys.PublicKey> PKI)` should function differently based on if the party is honest or dishonest / faulty. `relay` should be called in rounds 2+. Honest parties should add their signature and broadcast the message to all other parties. Use `send` as a helper. Dishonest parties can deviate from the protocol in any way. 
- `void decide()` If the party is honest, it should set the `output` member variable according to the protocol. If the party is dishonest, it can deviate from the protocol in any way. 

### DolevStrong class

The protocol file `DolevStrong.java` contains the same methods: `validity`, `agreement` and `protocol`.
Implement the Dolev Strong protocol we learned in class. 

In the main, test your protocol under various settings of n and t as in Part 1. Fill out the following table with a row for each value of n and t you tested:

|     | dishonest parties |  valildity?  | agreement?  | 
| --- | ----------------- | ------------ |------------ |
| n,t |                   |              |             |



If you find cases where validity and agreement do not hold, explain in words the situation that occured. 



## What to submit
1. README with table and descriptions of non BG situations.
2. Party.java
3. ByzantineAgreement.java
4. DSParty.java
5. DolevStrong.java
6. Wallet.java
