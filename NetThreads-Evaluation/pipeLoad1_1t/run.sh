#!/bin/bash

if [[ "$1" == "" ]]
then
	echo "You must define a packet rate"
	exit;
fi
	
timeout 6 tcpreplay -l 100 -p $1 -i eth0 dataset/pipe1000.trace;


