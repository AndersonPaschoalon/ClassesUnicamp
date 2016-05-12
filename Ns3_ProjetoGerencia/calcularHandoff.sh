#!/bin/bash

# Topologia:
# 
#                                <-(C)
#    AP2[Node3] (*)                    (*) AP1[Node2]
#                |                      |
#                |                      |
#                |     Router[Node0]    |
#                +---------(x)----------+
#                           | 
#                           |
#                           |
#                           |
#                          (S) Server[Node1]

###############################################################################
# Funcao retorna (imprime) o nome do arquivo desejado na rodada da simulacao  #
# a partir dos parametros fornecidos.                                         #
###############################################################################
function getFileName()
{
	#args
	local strSimName=$1;
	local strMobilidade=$2;
	local strAplicacao=$3;
	local nosMoveis=$4;
	local rodada=$5;
	local node=$6;

	local fileName=($strSimName"_Mobilidade-"$strMobilidade"_App-"$strAplicacao"_nMobile-"$nosMoveis"_Rodada-"$rodada"_-"$node"-0.pcap");
	
	echo $fileName

	return 0;
}

###############################################################################
# Calcula o tempo de handoff de uma simulacao a partir de dois arquivos pcaps.#
###############################################################################
function getHandoff()
{
	#args
	local fileName_ap1=$1;
	local fileName_ap2=$2;


	#Filtra o ultimo pacote recebido pelo AP1
	time_lastPacketAp1=`tcpdump -ttttnr  $fileName_ap1 |grep "> 10.1.1.1.10000: UDP" |sed "s/^1970-01-01\ -1:-59://g" |awk '{print $1}'|awk 'END{print}'`


	#Filtra o primeiro pacote recebido pelo AP2
	time_firstPacketAp2=`tcpdump -ttttnr  $fileName_ap2  |grep "> 10.1.1.1.10000: UDP" |sed "s/^1970-01-01\ -1:-59://g" |awk '{ if(NR == 1){ print $1} }'`


#debugF
#echo "files: " $fileName_ap1 " " $fileName_ap2
#echo "time_lastPacketAp1 = " $time_lastPacketAp1
#echo "tsme_firstPacketAp2 = " $time_firstPacketAp2 

	# calcula e imprime o tempo de Handoff
	echo $time_lastPacketAp1 " " $time_firstPacketAp2 |awk '{printf("%.8f", sqrt(($2 - $1)^2) ) }'

	return 0;
}

###############################################################################
# Configura os arquivos do GnuPlot para a criação das plotagens.              #
###############################################################################
function configGnuPlot()
{
	#args
	local gnuPlotScript=$1;
	local strAplicacao=$2;
	local strMobilidade=$3;

	echo "set terminal png" >> $gnuPlotScript;
	echo "set output \"Handoff$strAplicacao$strMobilidade\"" >> $gnuPlotScript;
	echo "set xlabel \"Número de dispositivos móveis\"" >> $gnuPlotScript;
	echo "set ylabel \"Tempo de Handoff - $strAplicacao - $strMobilidade \"" >> $gnuPlotScript;
	echo "" >> $gnuPlotScript;
	echo "set xrange [0:+50]" >> $gnuPlotScript;
	echo "plot \"-\"  title \"Tempo de Handoff\" with lines, \"-\"  title \"Desvio Padrão\" with yerrorbars" >> $gnuPlotScript;

	return 0;
}

###############################################################################
# Grava as entradas dos eixos x, y, e desvio padrão (tempo de handoff e       #
# numero de dispositivos moveis, e desvio padrão do tempo de handoff          #
# respectivamente) nos scripts do gnuplot.                                    #
###############################################################################
function outputData()
{
	#args
	local outputDataName=$1;
	declare -a xValue=("${!2}");
	declare -a yValue=("${!3}");
	declare -a dpValue=("${!4}");

	#tamanho dos vetores de entrada
	local inputSize=${#xValue[@]};
	local i=0;

	for i in `seq 0 $(($inputSize - 1))`
	do
		echo ${xValue[$i]} ${yValue[$i]} >> $outputDataName;

	done	
	
	echo "e" >> $outputDataName;

        for i in `seq 0 $(($inputSize - 1))`                                    
        do                                                                      
                echo ${xValue[$i]} ${yValue[$i]} ${dpValue[$i]} >> $outputDataName;            
                                                                                
        done 

	echo "e" >> $outputDataName;

	return 0;
}

###############################################################################
# Calcula a media de um vetor de valores passado como argumento, e imprime    #
# o resultado.                                                                #
###############################################################################
function media()
{
	#args
	declare -a vetor=("${!1}")

        #tamanho dos vetores de entrada                                         
        local inputSize=${#vetor[@]};
	local soma=0;
	local media=0;
        local i=0;  

        for i in `seq 0 $(($inputSize - 1))`                                    
        do                                                                      
		soma=`echo $soma ${vetor[$i]} |awk '{printf("%.8f",$1 + $2);}'`;
        done  

	media=`echo $soma $inputSize |awk '{printf("%.8f",$1/$2);}'`;

	echo $media;
	
	return 0;
}

###############################################################################
# Calcula o desvio padrao de um vetor de valores passado como argumento, e    #
# imprime o resultado.                                                        #
###############################################################################
function desvioPadrao()
{
	#args
	declare -a vetor=("${!1}");

        #tamanho dos vetores de entrada                                         
        local inputSize=${#vetor[@]};
	local soma=0;
	local media=0;
	local desvio=0;
	local desvioPadrao=0;                              
        local i=0;  

	#calcula a media
        for i in `seq 0 $(($inputSize - 1))`                                    
        do                                                                         
                soma=`echo $soma ${vetor[$i]} |awk '{printf("%.8f",$1 + $2);}'`;
        done                                                                    
        media=`echo $soma $inputSize |awk '{printf("%.8f",$1/$2);}'`;  

	#
        for i in `seq 0 $(($inputSize - 1))`                                    
        do      
		desvio=`echo $desvio $media ${vetor[$i]} |awk '{
			desvio = $1
			media = $2;
			valor = $3;
			desvio = desvio + (media - valor)^2;
			printf("%.8f",desvio );
		}' `; 

        done 

	desvioPadrao=`echo $(($inputSize - 1)) $desvio |awk '{
		dp = sqrt($2/$1);
		printf("%.8f",dp );
	}'`;
	
	echo $desvioPadrao;
	
	return 0;
}


###############################################################################
# Funcao main. Calcula os tempos de handoff e realiza a geracao dos scripts   #
# do GnuPlot.                                                                 #
###############################################################################
function main()
{
	local strSimName="simulacaoMo655";
	local strDiretorio="./results/";
	local strSimulacao=$strDiretorio$strSimName;
	local vetMobilidade=(estatica baixa media alta);
	local vetAplicacao=(cbr rajada);
	local maxRodadas=30;
	local maxMobiles=40;
	local nodeAp1=2;
	local nodeAp2=3;
	local tempoHandoff;

	#variaveis loop
	local i=0;
	local j=3;
	local k=0;
	local nMoveis=0;
	local nRodada;
	local strMobilidade;
	local vetAplicacao;
	local fileName_ap1;
	local fileName_ap2;
	local outFileName;
	local yValues;
	local dpValues;
	local xValues;	

	#loop aplicações
	for i in `seq 0 1`
	do
	
		#loop mobilidade
		for j in `seq 0 3`
		#for j in `seq 1 2`
		do

			strMobilidade=${vetMobilidade[$j]};
			strAplicacao=${vetAplicacao[$i]};

			#loop rodadas
			for nMoveis in `seq 1 $maxMobiles`;
			do
			#temp
			#for nMoveis in  "5" "10" "15" "20" "25" "30" "35" "40"
			#do
				
				for nRodada in `seq 0 $(($maxRodadas - 1))`
				do
					fileName_ap1=`getFileName $strSimulacao $strMobilidade $strAplicacao $nMoveis $nRodada $nodeAp1`;
					fileName_ap2=`getFileName $strSimulacao $strMobilidade $strAplicacao $nMoveis $nRodada $nodeAp2`;

					tempoHandoff[$nRodada]=`getHandoff $fileName_ap1 $fileName_ap2`;

				done
				# nesse ponto o vetor tempoHandoff[] possui
				# o valor do tempo para todas as rodadas
			
				#temp: por enquanto vai de 5 em 5
				k=$nMoveis
				#k=`echo $(( $nMoveis / 5 ))`;


				yValues[$k]=`media tempoHandoff[@]`;
				dpValues[$k]=`desvioPadrao tempoHandoff[@]`;
				xValues[$k]=$nMoveis;
				
			done

			outFileName=$strDiretorio"Handoff-"$strAplicacao"-"$strMobilidade".plt";

			echo "" > $outFileName;
				
			configGnuPlot $outFileName $strAplicacao $strMobilidade;
			outputData $outFileName  xValues[@] yValues[@] dpValues[@];
						
		done

	done


}


main;












