const {
  loadFixture,
} = require("@nomicfoundation/hardhat-network-helpers");
const { expect } = require("chai");
const { ethers } = require("hardhat");

describe("Auction", function () {
  // We define a fixture to reuse the same setup in every test.
  // We use loadFixture to run this setup once, snapshot that state,
  // and reset Hardhat Network to that snapshot in every test.
  async function deployAuction() {
    const [owner, otherAccount1, otherAccount2] = await ethers.getSigners();
    const Auction = await ethers.getContractFactory("Auction");
    const auction = await Auction.deploy("0x229e039b1605bD1C01247bD5ee5714ba5F0Cc3a9", 100);
    return { auction, owner, otherAccount1, otherAccount2 };
  }

  describe("Deployment", function () {
    it("Should start with auction closed", async function () {
      const { auction } = await loadFixture(deployAuction);
      await expect(auction.makeBid({ value: 1 })).to.be.revertedWith("Auction not yet active");
    });

    it("Should set the right owner", async function () {
      const { auction, owner } = await loadFixture(deployAuction);
      expect(await auction.owner()).to.equal(owner.address);
    });
  });

  describe("Opening the auction", function () {
    it("Should only allow the owner to start the auction", async function () {
      const { auction, otherAccount1 } = await loadFixture(deployAuction);
      await expect(auction.connect(otherAccount1).startAuction()).to.be.reverted;
    });

    it("Should allow bidding to start once it is open", async function () {
      const { auction, otherAccount1 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await expect(auction.connect(otherAccount1).makeBid({ value: 1 })).not.to.be.reverted;
    });
  });

  describe("Bidding", function () {
    it("Should set the highest bidder accordingly", async function () {
      const { auction, otherAccount1 } = await loadFixture(deployAuction);
      const bidAmount = 1;
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: bidAmount });
      expect(await auction.highestBidder()).to.equal(otherAccount1.address);
      expect(await auction.highestBid()).to.equal(bidAmount);
      expect(await auction.fundsPerBidder(otherAccount1.address)).to.equal(bidAmount);
    });

    it("Should not allow a bid lower than the highest", async function () {
      const { auction, otherAccount1, otherAccount2 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: 2 });
      await expect(auction.connect(otherAccount2).makeBid({ value: 1 })).to.be.reverted;
    });

    it("Should not allow more than one bid per sender", async function () {
      const { auction, otherAccount1 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: 1 });
      await expect(auction.connect(otherAccount1).makeBid({ value: 2 })).to.be.reverted;
    });
  });

  describe("Up Bidding", function () {
    it("Should set the highest bidder accordingly", async function () {
      const { auction, otherAccount1, otherAccount2 } = await loadFixture(deployAuction);
      const acc1_bidAmount = 1;
      const acc2_bidAmount = 2;
      const acc1_upAmount = 2;
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: acc1_bidAmount });
      await auction.connect(otherAccount2).makeBid({ value: acc2_bidAmount });
      await auction.connect(otherAccount1).upBid({ value: acc1_upAmount });
      expect(await auction.highestBidder()).to.equal(otherAccount1.address);
      expect(await auction.fundsPerBidder(otherAccount1.address)).to.equal(acc1_bidAmount + acc1_upAmount);
    });

    it("Should not allow senders who have not yet bid", async function () {
      const { auction, otherAccount1 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await expect(auction.connect(otherAccount1).upBid({ value: 1 })).to.be.reverted;
    });

    it("Should not allow a bid lower than the highest", async function () {
      const { auction, otherAccount1, otherAccount2 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: 1 });
      await auction.connect(otherAccount2).makeBid({ value: 3 });
      // otherAccount1 total would be 1 + 1 = 2, still below 3
      await expect(auction.connect(otherAccount1).upBid({ value: 1 })).to.be.reverted;
    });
  });

  describe("Close auction", function () {
    it("Only the owner can close the auction", async function () {
      const { auction, otherAccount1 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await expect(auction.connect(otherAccount1).endAuction()).to.be.reverted;
    });

    it("Should not allow bidding once the auction is closed", async function () {
      const { auction, otherAccount1 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await auction.endAuction();
      await expect(auction.connect(otherAccount1).makeBid({ value: 1 })).to.be.reverted;
    });

    it("Should not allow up bidding once the auction is closed", async function () {
      const { auction, otherAccount1 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: 1 });
      await auction.endAuction();
      await expect(auction.connect(otherAccount1).upBid({ value: 1 })).to.be.reverted;
    });

    it("Should not close if the auction has not been started", async function () {
      const { auction } = await loadFixture(deployAuction);
      await expect(auction.endAuction()).to.be.reverted;
    });
  });

  describe("Refund", function () {
    it("Should only refund if the auction is closed", async function () {
      const { auction, otherAccount1, otherAccount2 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: 1 });
      await auction.connect(otherAccount2).makeBid({ value: 2 });
      await expect(auction.connect(otherAccount1).refund()).to.be.reverted;
    });

    it("Should not refund the highest bidder", async function () {
      const { auction, otherAccount1 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: 1 });
      await auction.endAuction();
      await expect(auction.connect(otherAccount1).refund()).to.be.reverted;
    });

    it("Should set fundsPerBidder to 0 after refund", async function () {
      const { auction, otherAccount1, otherAccount2 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: 1 });
      await auction.connect(otherAccount2).makeBid({ value: 2 });
      await auction.endAuction();
      await auction.connect(otherAccount1).refund();
      expect(await auction.fundsPerBidder(otherAccount1.address)).to.equal(0);
    });

    it("Should only allow refund once per sender", async function () {
      const { auction, otherAccount1, otherAccount2 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: 1 });
      await auction.connect(otherAccount2).makeBid({ value: 2 });
      await auction.endAuction();
      await auction.connect(otherAccount1).refund();
      await expect(auction.connect(otherAccount1).refund()).to.be.reverted;
    });
  });

  describe("Payout Winner", function () {
    it("Only the owner can payout the winner", async function () {
      const { auction, otherAccount1 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: 1 });
      await auction.endAuction();
      await expect(auction.connect(otherAccount1).payoutWinner()).to.be.reverted;
    });

    it("The winner's fundsPerBidder should go to 0", async function () {
      const { auction, otherAccount1 } = await loadFixture(deployAuction);
      await auction.startAuction();
      await auction.connect(otherAccount1).makeBid({ value: 1 });
      await auction.endAuction();
      await auction.payoutWinner();
      expect(await auction.fundsPerBidder(otherAccount1.address)).to.equal(0);
    });
  });
});
