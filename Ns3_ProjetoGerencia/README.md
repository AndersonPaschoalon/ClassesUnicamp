(in Portuguese!)
# Ns3_ProjetoGerencia
Meu projeto de Gerencia de Redes, usando o simulador Ns-3.
Foi realizada uma simulação de redes sem fio, com handoff.

# Requisitos

Neste projeto foi utilizada a versão 3.24 do simulador Ns-3, instalado 
conforme as recomendações encontradas no site oficial do projeto
https://www.nsnam.org/

# Arquivos

Além do arquivo README, há 3 outros arquivos deste projeto:

* Projeto de Gerência de Redes de Computadores.pdf: relatorio, com a descrição
  detalhada do projeto

* calcularHandoff.sh: script bash utilizado para o calculo do Handoff, via
  leitura de arquivos pcaps.

* SimulacaoNs3Mo655.cc: programa em C++ utilizado para realização da simulação

# Simulação

Para a realização da simulação, o script "calcularHandoff.sh" deve ser gravado
no diretorio relativo do Ns-3:
ns-allinone-3.24/ns-3.24/

Já o programa "SimulacaoNs3Mo655.cc" deve ser gravado no diretorio:
ns-allinone-3.24/ns-3.24/scratch

Deve também ser criado um diretorio chamado "results" em:
ns-allinone-3.24/ns-3.24/

A simulação pode ser compilada no diretorio
ns-allinone-3.24/ns-3.24/
com o comando:
$ ./waf
E executada com o comando:
$ ./waf --run scratch/SimulacaoNs3Mo655

 Mais detalhes podem ser consultados no arquivo 
"Projeto de Gerência de Redes de Computadores.pdf"


