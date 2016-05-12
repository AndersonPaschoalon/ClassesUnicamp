#!/bin/bash

if [[ "$1" == "" ]]
then
	echo "You must define a packet rate as argument"
	exit;
fi


	
timeout 6 tcpreplay  -p $1 -i eth0 --stats=5  ./udp-load-test.trace


