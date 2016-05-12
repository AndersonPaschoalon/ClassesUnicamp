#!/bin/bash

#Global variables
ETH_INTERFACE=$1
TCPDUMP_PID=""
END_FLAG=""
#Constants
DEFAULT_ETH_INTERFACE="eth0"
FILE_NAME="tcpdump1.out.txt"

function tcpdump_exec() 
{
	sudo tcpdump -n -i $ETH_INTERFACE  & 
	export TCPDUMP_PID=$!;
} &> ./$FILE_NAME


function wait_finish_tcpdump()
{
	stdin_flag="";	
	while [[ $stdin_flag != "s"  ]]
	do
		printf "> ";
		read stdin_flag;

		if [[ $stdin_flag == "quit" ]]
		then
			stdin_flag="s";
			export END_FLAG="quit";
			break;
		fi
		
	done

}


#main function ######################
if [[ "$1" == "-h"* || "$1" = "--h"*  ]]
then
	echo "This script uses tcpdump to listen a given interface, and at the end of the   "
	echo "execution will display how much packets were received in this interface.      "
	echo "In the CLI, to stop listening, display the results, and start again, just type"
	echo " \"s\". To display the results and quit, type \"quit\". Any other command will"
	echo "be ignored. Tou may specify the interface that will be listened as argument.  "
	echo "Otherwise, this script will use the default interface.                        "
	echo "									            "
	echo "usage: ./listen-interface-term1.sh [-h|--help] [<param>]                      "
	echo "		-h|--help : hill display this help info.			    "
	echo "		<param> : host interface you want to listen			    "

else
	
	echo "- type \"quit \" to quit ";
	echo "- press \"s\" to stop listening the interface, and start again";
	if [[ $ETH_INTERFACE == "" ]]
	then
		export ETH_INTERFACE=$DEFAULT_ETH_INTERFACE;
	fi
	echo "Listening interface <" $ETH_INTERFACE ">";

	while [[ $END_FLAG != "quit" ]]
	do
		tcpdump_exec;
		wait_finish_tcpdump;

		kill $TCPDUMP_PID;
		printf "Number of packets @$ETH_INTERFACE : "
		tail ./$FILE_NAME -n  2 |grep "filter" | awk '{print $1}'
		echo ""

		if [[ $END_FLAG == "quit" ]]
		then
			rm ./$FILE_NAME
			exit
		fi
	done
fi


