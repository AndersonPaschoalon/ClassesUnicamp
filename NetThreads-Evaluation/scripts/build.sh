#!/bin/bash


echo "################################################################"
echo "Removing older embedded instruction and data files"
echo "################################################################"
rm -f ../pipeLoad0_1t/pipe.data.mif    ../pipeLoad0_mt/pipe.instr.mif
rm -f ../pipeLoad1_1t/pipe.data.mif    ../pipeLoad1_mt/pipe.instr.mif
rm -f ../pipeLoad2_1t/pipe.data.mif    ../pipeLoad2_mt/pipe.instr.mif


echo ""
echo "################################################################"
make clean -C  ../pipeLoad0_1t/

echo ""
echo "################################################################"
make clean -C  ../pipeLoad0_mt/

echo ""
echo "################################################################"
make clean -C ../pipeLoad1_1t/

echo ""
echo "################################################################"
make clean -C ../pipeLoad1_mt/

echo ""
echo "################################################################"
make clean -C ../pipeLoad2_1t/    

echo ""
echo "################################################################"
make clean -C ../pipeLoad2_mt/

echo ""
echo "################################################################"
make embed -C  ../pipeLoad0_1t/

echo ""
echo "################################################################"
make embed -C  ../pipeLoad0_mt/

echo ""
echo "################################################################"
make embed -C ../pipeLoad1_1t/

echo ""
echo "################################################################"
make embed -C ../pipeLoad1_mt/

echo ""
echo "################################################################"
make embed -C ../pipeLoad2_1t/    

echo ""
echo "################################################################"
make embed -C ../pipeLoad2_mt/







