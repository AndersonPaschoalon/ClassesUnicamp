#include <iostream>
#include <string>
#include <sstream>
#include <stdlib.h>
#include <math.h>
#include "ns3/applications-module.h"
#include "ns3/bridge-helper.h"
#include "ns3/core-module.h"
#include "ns3/csma-module.h"
#include "ns3/flow-monitor-module.h"
#include "ns3/internet-module.h"
#include "ns3/mobility-module.h"
#include "ns3/network-module.h"
#include "ns3/point-to-point-module.h"
#include "ns3/wifi-module.h"

#include "ns3/olsr-helper.h"
#include "ns3/ipv4-static-routing-helper.h"
#include "ns3/ipv4-list-routing-helper.h"
#include "ns3/ipv4-global-routing-helper.h"
#include "ns3/global-router-interface.h"

#include "ns3/netanim-module.h"
#include "ns3/gnuplot.h"

using namespace ns3;
using namespace std;

/**
 * Converte um determinador valor em uma string
 * @param X & arg parametro a ser convertido
 * @return string parametro convertido em string
 */
template<class X> string toString(X & arg)
{
	string Result;
	ostringstream convert;

	convert << arg;
	Result = convert.str();

	return Result;
}

/**
 * Classe responsavel por executar a simulação do Projeto de MO655.
 * Ela constroi uma topologia contendo um servidor, representando uma
 * conecção com a internet, que liga-se a um roteador de borda de uma rede
 * local. Esta rede local possui dois pontos de acesso, responsável por
 * estabelecer a comunicação com n dispositivos móveis.
 * Estes dispositivos móveis podem conectar-se com a internet por meio
 * de conecções cliente-servidor, através de aplicações que enviam trafego
 * CBR (taxa de tranferência constante) ou Radada (trafego tranmitido por
 * fluxos, seguidos de pausa). O tamano dos quadros é de 512 e 1500 bytes
 * respectivamente. O número de dispositivos móveis conectados varia de 1 até
 * 40.
 */
class SimulacaoNs3Mo655
{
public:

	/**
	 * Construtor. Realiza o numero de repetições especificado como parâmetro
	 *  @param repeticoes Numero total de repeticoes para cada rodada da
	 *  simulação. A Simulação é repetida esta quantia de veses para cada
	 *  dispositivo wifi
	 *  @param nome Nome da simulacao. Será utilizado como prefixo para o nome
	 *  dos arquivos gerados
	 */
	SimulacaoNs3Mo655(string nome, unsigned int repeticoes);

	/**
	 * Destrutor
	 */
	virtual ~SimulacaoNs3Mo655();

	/**
	 * Seta o nivel de mobilidade a aplicação cliente-servidor utilizada na
	 * simulação, e o numero máximo de dispositivos móveis
	 *  @param mobilidade Mobilidade dos nós. É definida como sendo o número de
	 *  nós se movendo.Os valores possiveis são "estatica"(nenhum nó se move),
	 *  "baixa"(10% dos nós se movem), "media"(35% dos nós se movem) e "alta"
	 *  (80% dos nós se movem).
	 *  @param aplicacao Aplicação cliente-servidor. Cada nó estabelece uma
	 *  conecção com o servidor de internet por meio dessa aplicação. Há 3
	 *  aplicações possíveis "cbr",  "rajada" e "echo".
	 *  @param nDipositivosMoveis numero maximo de dispositivos moveis com os
	 *  quais a simulação é executada.
	 */
	int Exec(string mobilidade, string aplicacao,
			unsigned int nDipositivosMoveis);

	//int GetHandoof();

private:
	/**
	 * Seta os parametros de mobilidade, aplicação e o numero de dispositivos
	 * móveis. É chamada pelo método Exec()
	 * @param mobilidade Mobilidade da rede sem fio. Mobilidade "alta",
	 * "media", "baixa" e "estatica"
	 * @param string aplicacao Aplicação cliente-servidor "cbr", "rajada" ou
	 * "echo".
	 * @param nDipositivosMoveis Número máximo de dispositivos móveis da rede
	 * sem fio.
	 */
	int SetParam(string mobilidade, string aplicacao,
			unsigned int nDipositivosMoveis);

	/**
	 * Executa a simulação propriamente dita, com os parêtros definidos pela
	 * fução Exec() e pelo construtor.
	 * @param void
	 * @return Retorna zero em caso de sucesso
	 */
	int Run(void);

	/**
	 * Seta a velocidade do nó de handoff. Utilizada pela função privada Run()
	 * @param node ponteiro para o nó de handoff
	 * @param vel vetor contendo a velocidade (velocidade vetorial 3D)
	 * @return void
	 */
	static void SetVelocity(Ptr<Node> node, Vector vel);

	//--------------------------------------------------------------------------
	// variaveis: Simulação
	//--------------------------------------------------------------------------
	unsigned int tempoSim;
	double serverStartTime;
	double clientStartTime;
	double stopTime;
	unsigned int nRepeticoes;

	//--------------------------------------------------------------------------
	// variaveis: Topologia
	//--------------------------------------------------------------------------
	string strSimName;
	unsigned int maxMobile;
	unsigned int nAps;
	string strDataRateP2p;
	string strDelayP2p;
	string strDataRateCsma;
	unsigned int delayCsma;

	//--------------------------------------------------------------------------
	// variaveis: Mobilidade e posicionamento
	//--------------------------------------------------------------------------
	string strMobilidade;
	double mobilityStatic;
	double mobilityLow;
	double mobilityMedium;
	double mobilityHigh;
	double mobilityLevel;
	double maxXPos;
	double maxYPos;
	double minXPos;
	double minYPos;

	//--------------------------------------------------------------------------
	// variaveis: Aplicações
	//--------------------------------------------------------------------------
	unsigned int echoPort;
	unsigned int onOffport;
	unsigned int handOffport;
	string strAplicacao; // "echo" "rajada" "cbr"
	string tempoON;
	string tempoOFF;
	unsigned int pacote;
	unsigned int quadro;
	string strOnOffDataRate;

};

/*******************************************************************************
 * Função Main. Executa a simulação
 ******************************************************************************/
int main()
{

	Time::SetResolution(Time::NS);
	SimulacaoNs3Mo655 simulacao = SimulacaoNs3Mo655("simulacaoMo655", 30);
	simulacao.Exec("estatica", "cbr", 40);
	simulacao.Exec("estatica", "rajada", 40);
	simulacao.Exec("baixa", "cbr", 40);
	simulacao.Exec("baixa", "rajada", 40);
	simulacao.Exec("media", "cbr", 40);
	simulacao.Exec("media", "rajada", 40);
	simulacao.Exec("alta", "cbr", 40);
	simulacao.Exec("alta", "rajada", 40);

	return 0;
}

void SimulacaoNs3Mo655::SetVelocity(Ptr<Node> node, Vector vel)
{
	Ptr < ConstantVelocityMobilityModel > mobility = node->GetObject<
			ConstantVelocityMobilityModel>();
	mobility->SetVelocity(vel);
}

SimulacaoNs3Mo655::SimulacaoNs3Mo655(string nome, unsigned int repeticoes)
{
	//--------------------------------------------------------------------------
	// variaveis: Simulação
	//--------------------------------------------------------------------------
	// tempos de termino da simulação, inicio da aplicação no servidor, no
	// cliente, tempo no qual a simulação é encerrada e número de repetições
	// (máximo: 40)
	tempoSim = 32;
	serverStartTime = 1;
	clientStartTime = 2;
	stopTime = 45;
	if (repeticoes >= 40)
	{
		cout << "Repetições em ecesso (" << repeticoes
				<< "). Numero default setado para 40." << endl;
		nRepeticoes = 40;
	}
	else
	{
		nRepeticoes = repeticoes;
	}

	//--------------------------------------------------------------------------
	// variaveis: Topologia
	//--------------------------------------------------------------------------
	// define parâmetros da simulação: nome, numero máximo de dispositivos
	// móveis, números de APs, taxa de tranferência e delay nos canais csma e
	// p2p
	strSimName = nome;
	maxMobile = 40;
	nAps = 2;
	strDataRateP2p = "100Mbps";
	strDelayP2p = "35ms";
	strDataRateCsma = "100Mbps";
	delayCsma = 6560;
	//--------------------------------------------------------------------------
	// variaveis: Mobilidade e posicionamento
	//--------------------------------------------------------------------------
	// mobility*: constantes que definem a porcentagem dos dispositivos que
	// estará em movimento
	mobilityStatic = 0;
	mobilityLow = 0.2;
	mobilityMedium = 0.4;
	mobilityHigh = 0.75;
	mobilityLevel = 0;
	// definem as máximas e minimas posições no eixo x e y nos quais os dispo
	// sitivos míveis serão alocados
	maxXPos = 100;
	maxYPos = maxXPos;
	minXPos = 0;
	minYPos = minXPos;

	//--------------------------------------------------------------------------
	// variaveis: Aplicações
	//--------------------------------------------------------------------------
	echoPort = 4000;
	onOffport = 9;
	handOffport = 10000;
	// vazão de cada dispositivo móvel, individualmente
	// Valor escolhido: (limite + delta)/40 = (11 + 4)Mbps/40 = 375kbps
	strOnOffDataRate = "375kbps";

}

SimulacaoNs3Mo655::~SimulacaoNs3Mo655()
{
	// não faz nada
}

int SimulacaoNs3Mo655::Exec(string mobilidade, string aplicacao,
		unsigned int nDipositivosMoveis)
{
	int estado1;

	estado1 = SetParam(mobilidade, aplicacao, nDipositivosMoveis);

	if (estado1 >= 0)
	{
		return Run();
	}
	else
	{
		cout << "Não foi possível executar a simulação" << endl;
		cout << "parametros passados:" << mobilidade << ", " << aplicacao
				<< ", " << nDipositivosMoveis << endl;
		return -1;
	}
}

int SimulacaoNs3Mo655::SetParam(string mobilidade, string aplicacao,
		unsigned int nDipositivosMoveis)
{
	if ((mobilidade == "alta") || (mobilidade == "Alta")
			|| (mobilidade == "Media") || (mobilidade == "media")
			|| (mobilidade == "Baixa") || (mobilidade == "baixa")
			|| (mobilidade == "Estatica") || (mobilidade == "estatica"))
	{
		strMobilidade = mobilidade;
		// atribuição dos graus de mobilidade
		if ((strMobilidade == "estatica") || (strMobilidade == "Estatica"))
		{
			mobilityLevel = mobilityStatic;
		}
		else if ((strMobilidade == "baixa") || (strMobilidade == "Baixa"))
		{
			mobilityLevel = mobilityLow;
		}
		else if ((strMobilidade == "media") || (strMobilidade == "Media"))
		{
			mobilityLevel = mobilityMedium;
		}
		else if ((strMobilidade == "alta") || (strMobilidade == "Alta"))
		{
			mobilityLevel = mobilityHigh;
		}
		else // default strMobilidade = "estatica"
		{
			mobilityLevel = mobilityStatic;
		}
	}
	else
	{
		cout << "Parametro de mobilidade não reconhecido:" << mobilidade
				<< endl;
		cout << "Valores possíveis: alta, media, baixa, estatica" << endl;
		return -1;
	}
	if ((aplicacao == "cbr") || (aplicacao == "CBR"))
	{
		strAplicacao = "cbr";
	}
	else if ((aplicacao == "Rajada") || (aplicacao == "rajada"))
	{
		strAplicacao = "rajada";
	}
	else if ((aplicacao == "Echo") || (aplicacao == "echo"))
	{
		strAplicacao = "echo";
	}
	else
	{
		cout << "Parametro de mobilidade não reconhecido:" << aplicacao << endl;
		cout << "Valores possíveis: cbr, rajada, echo" << endl;
		return -2;
	}
	maxMobile = nDipositivosMoveis;

	return 1;
}

int SimulacaoNs3Mo655::Run(void)
{
	//--------------------------------------------------------------------------
	// variaveis: Simulação
	//--------------------------------------------------------------------------
	int semente[] =
	{ 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71,
			73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139 };
	unsigned int nRodada = 0;
	unsigned int i = 0;

	//--------------------------------------------------------------------------
	// variaveis: Topologia
	//--------------------------------------------------------------------------
	unsigned int nMobile = 0;

	//--------------------------------------------------------------------------
	// variaveis: Mobilidade e posicionamento
	//--------------------------------------------------------------------------
	unsigned int nMovingNodes = 0;
	unsigned int nStaticNodes = 0;
	Vector posServidor = Vector(maxXPos / 2, 0, 0);
	Vector posRoteador = Vector(maxXPos / 2, 0, 0);
	Vector posAp1 = Vector(0, maxYPos / 2, 0);
	Vector posAp2 = Vector(maxXPos, maxYPos / 2, 0);
	Rectangle retanguloTopologia = Rectangle(-maxXPos, maxXPos, -maxYPos,
			maxYPos);
	Vector posHandoffNode = Vector(0, maxYPos, 0);

	//--------------------------------------------------------------------------
	// variaveis: Mobilidade e posicionamento
	//--------------------------------------------------------------------------
	string strAlocadorAleatorioMobileX = "ns3::UniformRandomVariable[Min="
			+ toString(minXPos) + "|Max=" + toString(maxXPos) + "]";

	string strAlocadorAleatorioMobileY = "ns3::UniformRandomVariable[Min="
			+ toString(minYPos) + "|Max=" + toString(maxYPos) + "]";

	//--------------------------------------------------------------------------
	// variaveis: Aplicações
	//--------------------------------------------------------------------------
	// tamanho do header (verificado pelo wireshark): 30 bytes
	if (strAplicacao == "cbr")
	{
		pacote = 482;
		quadro = pacote + 30;
		tempoON = "ns3::ConstantRandomVariable[Constant=30]";
		tempoOFF = "ns3::ConstantRandomVariable[Constant=0]";
	}
	else if (strAplicacao == "rajada")
	{
		pacote = 1470;
		quadro = pacote + 30;
		tempoON = "ns3::ConstantRandomVariable[Constant=3]";
		tempoOFF = "ns3::ConstantRandomVariable[Constant=3]";
	}
	else // default = echo
	{
		strAplicacao = "echo";
	}

	//--------------------------------------------------------------------------
	// variaveis: Coleta de dados
	//--------------------------------------------------------------------------
	// constantes e rótulos
	string strDiretorio = "results/";
	string strVazao = "Vazao";
	string strPerda = "Perda";
	string strAtraso = "Atraso";
	string strDesvio = "Desvio Padrao";
	string strExtensao = ".plt";

	//--------------------------------------------------------------------------
	// Plotagem(variaveis e configuração)
	//--------------------------------------------------------------------------
	// parâmetro é o nome do arquivo png que será gerado
	Gnuplot gnuplotVazao(strVazao + "-" + strAplicacao + "-" + strMobilidade);
	string plotFileNameVazao = strDiretorio + strVazao + "-" + strAplicacao
			+ "-" + strMobilidade + strExtensao;
	gnuplotVazao.SetTerminal("png");
	gnuplotVazao.SetLegend("Quantidade de dispositivos móveis",
			strVazao + " - " + strAplicacao + " - " + strMobilidade);
	gnuplotVazao.AppendExtra("set xrange [0:+50]");

	Gnuplot2dDataset datasetVazao(strVazao);
	Gnuplot2dDataset datasetVazaoDP(strDesvio); //DATA SET COM DESVIO PADRÃO
	datasetVazaoDP.SetStyle(Gnuplot2dDataset::POINTS);
	datasetVazaoDP.SetErrorBars(Gnuplot2dDataset::Y);

	Gnuplot gnuplotAtraso(strAtraso + "-" + strAplicacao + "-" + strMobilidade);
	string plotFileNameAtraso = strDiretorio + strAtraso + "-" + strAplicacao
			+ "-" + strMobilidade + strExtensao;
	gnuplotAtraso.SetTerminal("png");
	gnuplotAtraso.SetLegend("Quantidade de dispositivos móveis",
			strAtraso + " - " + strAplicacao + " - " + strMobilidade);
	gnuplotAtraso.AppendExtra("set xrange [0:+50]");

	Gnuplot2dDataset datasetAtraso(strAtraso);
	Gnuplot2dDataset datasetAtrasoDP(strDesvio);
	datasetAtrasoDP.SetStyle(Gnuplot2dDataset::POINTS);
	datasetAtrasoDP.SetErrorBars(Gnuplot2dDataset::Y);

	Gnuplot gnuplotPerda(strPerda + "-" + strAplicacao + "-" + strMobilidade);
	string plotFileNamePerda = strDiretorio + strPerda + "-" + strAplicacao
			+ "-" + strMobilidade + strExtensao;
	gnuplotPerda.SetTerminal("png");
	gnuplotPerda.SetLegend("Quantidade de dispositivos móveis",
			strPerda + " - " + strAplicacao + " - " + strMobilidade);
	gnuplotPerda.AppendExtra("set xrange [0:+50]");

	Gnuplot2dDataset datasetPerda(strPerda);
	Gnuplot2dDataset datasetPerdaDP(strDesvio);
	datasetPerdaDP.SetStyle(Gnuplot2dDataset::POINTS);
	datasetPerdaDP.SetErrorBars(Gnuplot2dDataset::Y);

	//--------------------------------------------------------------------------
	// Simulação
	//--------------------------------------------------------------------------
	string strSimulacaoLabel = strSimName + "_Mobilidade-" + strMobilidade
			+ "_App-" + strAplicacao;
	//Time::SetResolution(Time::NS);

	for (nMobile = 1; nMobile <= maxMobile; nMobile++)
	{

		float vazaoArray[] =
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0 };
		float perdaArray[] =
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0 };
		float atrasoArray[] =
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0 };

		nMovingNodes = nMobile * mobilityLevel;
		nStaticNodes = nMobile - nMovingNodes;

		for (nRodada = 0; nRodada < nRepeticoes; nRodada++)
		{
			//------------------------------------------------------------------
			// Topologia
			//------------------------------------------------------------------

			// Inicialização das variáveis
			string strSimulacaoLabelRodada = strSimulacaoLabel + "_nMobile-"
					+ toString(nMobile) + "_Rodada-" + toString(nRodada) + "_";

			string strSimulacaoPcap = strDiretorio + strSimulacaoLabelRodada;

			unsigned int txPacketsum = 0;
			unsigned int rxPacketsum = 0;
			unsigned int DropPacketsum = 0;
			unsigned int LostPacketsum = 0;
			double Delaysum = 0;
			double Jittersum = 0;

			SeedManager::SetSeed(semente[nRodada]);

			//cria container para os nós da ligação p2p entre o
			//roteador e o servidor que simboliza a internet
			NodeContainer p2pNodes;
			p2pNodes.Create(2);

			// cria canal p2p
			PointToPointHelper pointToPoint;
			pointToPoint.SetDeviceAttribute("DataRate",
					StringValue(strDataRateP2p));
			pointToPoint.SetChannelAttribute("Delay", StringValue(strDelayP2p));

			// cria device para o nó, e isntala utilizando o canal p2p
			NetDeviceContainer p2pDevices = pointToPoint.Install(p2pNodes);

			// cria container para os nós ligados ao roteador, os seja os APs
			NodeContainer csmaNodes;

			csmaNodes.Add(p2pNodes.Get(1));
			csmaNodes.Create(nAps);

			// cria o canal CSMA
			CsmaHelper csma;
			csma.SetChannelAttribute("DataRate", StringValue(strDataRateCsma));
			csma.SetChannelAttribute("Delay",
					TimeValue(NanoSeconds(delayCsma)));

			// instala devices nos nós
			NetDeviceContainer csmaDevices;
			csmaDevices = csma.Install(csmaNodes);

			// cria nós para os APs
			NodeContainer wifiApNodes;
			for (i = 1; i <= nAps; i++)
			{
				wifiApNodes.Add(csmaNodes.Get(i));
			}

			// nó de handoff
			NodeContainer wifiHandoffNode;
			wifiHandoffNode.Create(1);

			// declara e cria os nMobile nós correspondente aos
			// dispositivos móveis
			NodeContainer wifiStaNodes;
			wifiStaNodes.Create(nMobile);

			// classifica nós em estaticos e moveis
			NodeContainer movingNodes;
			NodeContainer staticNodes;

			for (i = 0; i < nMovingNodes; i++)
			{
				movingNodes.Add(wifiStaNodes.Get(i));
			}
			for (i = nMovingNodes; i < nMobile; i++)
			{
				staticNodes.Add(wifiStaNodes.Get(i));
			}

			// Cria o canal wifi (receita de bolo)
			YansWifiChannelHelper channel;
			channel = YansWifiChannelHelper::Default();

			// cria a camada física do wifi (receita de bolo)
			YansWifiPhyHelper phy;
			phy = YansWifiPhyHelper::Default();

			// seta o canal na camada física do wifi
			phy.SetChannel(channel.Create());

			// cria o pilha do wifi, incluindo os protocolos, e o
			// algoríticmo de decaimento RF
			WifiHelper wifi;
			wifi = WifiHelper::Default();
			wifi.SetRemoteStationManager("ns3::AarfWifiManager");

			// Concluida a camada físcia, cria a camada MAC para o Wifi
			NqosWifiMacHelper mac;
			mac = NqosWifiMacHelper::Default();

			// Nome da rede criada
			Ssid ssid = Ssid(strSimName);

			mac.SetType("ns3::StaWifiMac", "Ssid", SsidValue(ssid),
					"ActiveProbing", BooleanValue(false));

			NetDeviceContainer staDevices;
			staDevices = wifi.Install(phy, mac, wifiStaNodes);
			NetDeviceContainer staDevicesHandoff;
			staDevicesHandoff = wifi.Install(phy, mac, wifiHandoffNode);

			mac.SetType("ns3::ApWifiMac", "Ssid", SsidValue(ssid),
					"BeaconGeneration", BooleanValue(true));
			NetDeviceContainer apDevices;
			apDevices = wifi.Install(phy, mac, wifiApNodes);

			BridgeHelper bridge;
			NetDeviceContainer bridgeDevices;
			for (i = 0; i < nAps; i++)
			{
				bridgeDevices.Add(
						bridge.Install(wifiApNodes.Get(i),
								NetDeviceContainer(apDevices.Get(i),
										csmaDevices.Get(i + 1))));
			}

			//------------------------------------------------------------------
			// Mobilidade e posicionamento
			//------------------------------------------------------------------

			// mobilidade: roteador e servidor(internet)
			Ptr < ListPositionAllocator > positionAlloc;
			positionAlloc = CreateObject<ListPositionAllocator>();
			positionAlloc->Add(posServidor);
			positionAlloc->Add(posRoteador);
			MobilityHelper mobility;
			mobility.SetPositionAllocator(positionAlloc);
			mobility.SetMobilityModel("ns3::ConstantPositionMobilityModel");
			mobility.Install(p2pNodes);

			// mobilidade: APs
			positionAlloc = CreateObject<ListPositionAllocator>();
			positionAlloc->Add(posAp1);
			positionAlloc->Add(posAp2);
			mobility.SetPositionAllocator(positionAlloc);
			mobility.SetMobilityModel("ns3::ConstantPositionMobilityModel");
			mobility.Install(wifiApNodes);

			// mobilidade: nó de handoff
			positionAlloc = CreateObject<ListPositionAllocator>();
			positionAlloc->Add(posHandoffNode);
			mobility.SetPositionAllocator(positionAlloc);
			mobility.SetMobilityModel("ns3::ConstantVelocityMobilityModel");
			mobility.Install(wifiHandoffNode);

			// mobilidade: nós moveis
			mobility.SetPositionAllocator(
					"ns3::RandomRectanglePositionAllocator", "X",
					StringValue(strAlocadorAleatorioMobileX), "Y",
					StringValue(strAlocadorAleatorioMobileY));
			mobility.SetMobilityModel("ns3::RandomWalk2dMobilityModel",
					"Bounds", RectangleValue(retanguloTopologia), "Speed",
					StringValue("ns3::ConstantRandomVariable[Constant=10.0]"));
			mobility.Install(movingNodes);

			mobility.SetMobilityModel("ns3::ConstantPositionMobilityModel");
			mobility.Install(staticNodes);

			//------------------------------------------------------------------
			// Pilha de rede
			//------------------------------------------------------------------
			InternetStackHelper stack;

			stack.Install(p2pNodes.Get(0));
			stack.Install(csmaNodes);
			stack.Install(wifiStaNodes);
			stack.Install(wifiHandoffNode);

			Ipv4AddressHelper address;

			// atribui endereço IP ao nó n1, para sua interface CSMA
			address.SetBase("192.168.0.0", "255.255.255.0");
			Ipv4InterfaceContainer apsInterfaces = address.Assign(
					csmaDevices.Get(0));

			Ipv4InterfaceContainer handoffInterface = address.Assign(
					staDevicesHandoff);
			Ipv4InterfaceContainer staInterfaces = address.Assign(staDevices);

			// atribui um endereço IP as interfaces do canal p2p
			address.SetBase("10.1.1.0", "255.255.255.0");
			Ipv4InterfaceContainer p2pInterfaces = address.Assign(p2pDevices);

			Ipv4GlobalRoutingHelper::PopulateRoutingTables();

			//------------------------------------------------------------------
			// Aplicações
			//------------------------------------------------------------------

			if (strAplicacao == "echo")
			{
				LogComponentEnable("UdpEchoClientApplication", LOG_LEVEL_INFO);
				LogComponentEnable("UdpEchoServerApplication", LOG_LEVEL_INFO);

				// Cria a aplicação UdpEcho servidor
				UdpEchoServerHelper echoServer(echoPort);

				// Instala a aplicação no no ultimo nó servidor, e define os
				// tempos de início e fim
				ApplicationContainer serverApps = echoServer.Install(
						p2pNodes.Get(0));
				serverApps.Start(Seconds(serverStartTime));
				serverApps.Stop(Seconds(tempoSim));

				// Instala a aplicação nos ultimo nos wifi
				for (i = 0; i < nMobile; i++)
				{
					// Cria UdpEcho cliente
					UdpEchoClientHelper echoClient(p2pInterfaces.GetAddress(0),
							echoPort);
					echoClient.SetAttribute("MaxPackets", UintegerValue(10));
					echoClient.SetAttribute("Interval",
							TimeValue(Seconds(1.0)));
					echoClient.SetAttribute("PacketSize", UintegerValue(1024));

					ApplicationContainer clientApps = echoClient.Install(
							wifiStaNodes.Get(i));
					clientApps.Start(Seconds(clientStartTime));
					clientApps.Stop(Seconds(tempoSim));
				}
			}
			else
			{

				// Configura o OnOffHelper Servidor para os nos nós moveis
				PacketSinkHelper packetSinkHelper("ns3::UdpSocketFactory",
						Address());
				packetSinkHelper.SetAttribute("Local",
						AddressValue(
								InetSocketAddress(p2pInterfaces.GetAddress(0),
										onOffport)));
				ApplicationContainer serverApps = packetSinkHelper.Install(
						p2pNodes.Get(0));
				serverApps.Start(Seconds(serverStartTime));
				serverApps.Stop(Seconds(tempoSim));

				// Configura o OnOffHelper Cliente nos nós moveis
				OnOffHelper onOffHelper("ns3::UdpSocketFactory", Address());
				onOffHelper.SetAttribute("DataRate",
						DataRateValue(DataRate(strOnOffDataRate)));
				onOffHelper.SetAttribute("PacketSize", UintegerValue(pacote));
				onOffHelper.SetAttribute("OnTime", StringValue(tempoON));
				onOffHelper.SetAttribute("OffTime", StringValue(tempoOFF));
				onOffHelper.SetAttribute("Remote",
						AddressValue(
								InetSocketAddress(p2pInterfaces.GetAddress(0),
										onOffport)));
				ApplicationContainer clientApps;
				for (unsigned int i = 0; i < wifiStaNodes.GetN(); ++i)
				{

					clientApps = onOffHelper.Install(wifiStaNodes.Get(i));
					clientApps.Start(Seconds(clientStartTime));
					clientApps.Stop(Seconds(tempoSim));
				}

				// Configura o OnOffHelper Servidor para o no de handoff
				PacketSinkHelper packetSinkHelperHandoff(
						"ns3::UdpSocketFactory", Address());
				packetSinkHelperHandoff.SetAttribute("Local",
						AddressValue(
								InetSocketAddress(p2pInterfaces.GetAddress(0),
										handOffport)));
				ApplicationContainer serverAppsHandoff =
						packetSinkHelperHandoff.Install(p2pNodes.Get(0));
				serverAppsHandoff.Start(Seconds(serverStartTime));
				serverAppsHandoff.Stop(Seconds(tempoSim));

				// Configura o OnOffHelper Cliente o nó handoff
				OnOffHelper onOffHelperHandoff("ns3::UdpSocketFactory",
						Address());
				onOffHelperHandoff.SetAttribute("DataRate",
						DataRateValue(DataRate(strOnOffDataRate)));
				onOffHelperHandoff.SetAttribute("PacketSize",
						UintegerValue(pacote));
				onOffHelperHandoff.SetAttribute("OnTime", StringValue(tempoON));
				onOffHelperHandoff.SetAttribute("OffTime",
						StringValue(tempoOFF));
				onOffHelperHandoff.SetAttribute("Remote",
						AddressValue(
								InetSocketAddress(p2pInterfaces.GetAddress(0),
										handOffport)));
				ApplicationContainer clientAppsHandoff;
				clientAppsHandoff = onOffHelperHandoff.Install(wifiHandoffNode);
				clientAppsHandoff.Start(Seconds(clientStartTime));
				clientAppsHandoff.Stop(Seconds(tempoSim));

			}

			Simulator::Schedule(Seconds(0.2), &SetVelocity,
					wifiHandoffNode.Get(0), Vector(10.0, 0.0, 0.0));

			Simulator::Stop(Seconds(stopTime));

			// Captura de pcaps
			bool tracing = true;
			if (tracing == true)
			{
				pointToPoint.EnablePcapAll(strSimulacaoPcap);
				csma.EnablePcapAll(strSimulacaoPcap);
			}

			FlowMonitorHelper flowHelper;
			Ptr < FlowMonitor > monitor;
			monitor = flowHelper.InstallAll();

			Simulator::Run();

			std::map < FlowId, FlowMonitor::FlowStats > stats =
					monitor->GetFlowStats();

			monitor->SerializeToXmlFile(
					strDiretorio + strSimulacaoLabel + ".xml", true, true);
			// classificador
			Ptr < Ipv4FlowClassifier > classifier = DynamicCast
					< Ipv4FlowClassifier > (flowHelper.GetClassifier());

			//Coleta de dados do flowmonitor
			for (std::map<FlowId, FlowMonitor::FlowStats>::const_iterator i =
					stats.begin(); i != stats.end(); ++i)
			{
				txPacketsum += i->second.txPackets;
				rxPacketsum += i->second.rxPackets;
				LostPacketsum += i->second.lostPackets;
				DropPacketsum += i->second.packetsDropped.size();
				Delaysum += i->second.delaySum.GetSeconds();
				Jittersum += i->second.jitterSum.GetSeconds();
			}

			vazaoArray[nRodada] = (txPacketsum - LostPacketsum) * quadro;
			atrasoArray[nRodada] = (Delaysum / (txPacketsum - LostPacketsum));
			perdaArray[nRodada] = LostPacketsum;

			Simulator::Destroy();

			//log
			std::cout << "Simulação " << strAplicacao << ", Mobilidade: "
					<< strMobilidade << ", Nós: " << nMobile << "(NósMoveis: "
					<< nMovingNodes << ", NósEstáticos: " << nStaticNodes << ")"
					<< ", Rodada: " << nRodada << endl;

		}

		float avgVazao = 0;
		float avgPerda = 0;
		float avgAtraso = 0;

		for (nRodada = 0; nRodada < nRepeticoes; nRodada++)
		{
			avgVazao += vazaoArray[nRodada];
			avgPerda += perdaArray[nRodada];
			avgAtraso += atrasoArray[nRodada];
		}

		avgVazao = avgVazao / nRepeticoes;
		avgPerda = avgPerda / nRepeticoes;
		avgAtraso = avgAtraso / nRepeticoes;

		float desvioVazao = 0;
		float desvioPerda = 0;
		float desvioAtraso = 0;

		for (nRodada = 0; nRodada < nRepeticoes; nRodada++)
		{
			desvioVazao += (vazaoArray[nRodada] - avgVazao)
					* (vazaoArray[nRodada] - avgVazao);
			desvioPerda += (perdaArray[nRodada] - avgPerda)
					* (perdaArray[nRodada] - avgPerda);
			desvioAtraso += (atrasoArray[nRodada] - avgAtraso)
					* (atrasoArray[nRodada] - avgAtraso);
		}

		datasetVazao.Add(nMobile, avgVazao);
		datasetVazaoDP.Add(nMobile, avgVazao, sqrt(desvioVazao / nRepeticoes));

		datasetAtraso.Add(nMobile, avgAtraso);
		datasetAtrasoDP.Add(nMobile, avgAtraso,
				std::sqrt(desvioAtraso / nRepeticoes));

		datasetPerda.Add(nMobile, avgPerda);
		datasetPerdaDP.Add(nMobile, avgPerda,
				std::sqrt(desvioPerda / nRepeticoes));

	}

	gnuplotVazao.AddDataset(datasetVazao);
	gnuplotVazao.AddDataset(datasetVazaoDP);
	std::ofstream plotFileVazao(plotFileNameVazao.c_str());
	gnuplotVazao.GenerateOutput(plotFileVazao);
	plotFileVazao.close();

	gnuplotAtraso.AddDataset(datasetAtraso);
	gnuplotAtraso.AddDataset(datasetAtrasoDP);
	std::ofstream plotFileAtraso(plotFileNameAtraso.c_str());
	gnuplotAtraso.GenerateOutput(plotFileAtraso);
	plotFileAtraso.close();

	gnuplotPerda.AddDataset(datasetPerda);
	gnuplotPerda.AddDataset(datasetPerdaDP);
	std::ofstream plotFilePerda(plotFileNamePerda.c_str());
	gnuplotPerda.GenerateOutput(plotFilePerda);
	plotFilePerda.close();

	return 0;

}

