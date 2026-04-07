#!/bin/bash
KILLED=0
SURVIVED=0

# backup original
mkdir -p contracts_backup/
cp CodeCoverage/contracts/BuyTicket.sol contracts_backup/

for dir in gambit_out/mutants/*/; do
    # copy mutant files into CodeCoverage
    find "$dir" -name "*.sol" | while read mutant_file; do
        rel_path="${mutant_file#$dir}"
        cp "$mutant_file" "$rel_path"
    done
    
    # run tests from inside CodeCoverage
    output=$(cd CodeCoverage && npx hardhat test 2>&1)

    #echo $output 
    
    if echo "$output" | grep -q "failing"; then
        echo "KILLED: $dir"
        ((KILLED++))
    else
        echo "SURVIVED: $dir"
        ((SURVIVED++))
    fi
done

# restore original
cp contracts_backup/BuyTicket.sol CodeCoverage/contracts/

echo ""
echo "Killed:   $KILLED / $((KILLED + SURVIVED))"
echo "Survived: $SURVIVED / $((KILLED + SURVIVED))"
