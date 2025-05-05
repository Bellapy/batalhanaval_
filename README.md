# Batalha Naval (Battleship) - Jogo de Console em Java 🚢💥

Este projeto implementa o clássico jogo de Batalha Naval em Java para ser jogado diretamente no console/terminal.

## Descrição

Uma implementação fiel do jogo Batalha Naval, permitindo que um jogador desafie o computador (com posicionamento e ataques aleatórios) ou jogue contra outro jogador humano no mesmo computador. O jogo segue as regras padrão, incluindo o posicionamento de uma frota de navios em um grid 10x10 e turnos alternados de ataques para tentar afundar a frota inimiga.

## Funcionalidades ✨

*   **Modos de Jogo:**
    *   Jogador vs Computador (PvC)
    *   Jogador vs Jogador (PvP - local)
*   **Grid:** Tabuleiro de 10x10.
*   **Frota Padrão:** Conjunto de navios com tamanhos {4, 3, 3, 2, 2, 2, 1, 1, 1, 1}.
*   **Alocação de Navios:**
    *   Opção de alocação **Manual** para jogadores humanos (definindo coordenada inicial e orientação H/V).
    *   Opção de alocação **Automática** para jogadores humanos.
    *   Alocação sempre **Automática** para o computador.
*   **Regra de Adjacência:** Navios não podem ser posicionados encostados uns nos outros (nem mesmo nas diagonais).
*   **Jogabilidade por Turnos:** Jogadores alternam turnos para atacar coordenadas.
*   **Jogar Novamente:** Um acerto (`X`) permite ao jogador atacar novamente no mesmo turno.
*   **Feedback Visual:** Exibição clara dos mapas (mapa de ataque e mapa próprio) e mensagens de status (Acerto!, Água!, Vencedor).
*   **Validação de Entrada:** Tratamento robusto de entradas do usuário (coordenadas, opções de menu) para evitar erros.

## Requisitos 📋

*   **Java Development Kit (JDK):** Versão 8 ou superior instalada.

## Como Compilar e Executar 🚀

1.  **Salve o Código:** Certifique-se de que o código-fonte está salvo em um arquivo chamado `batalhaNaval.java`.
2.  **Abra o Terminal:** Navegue até o diretório onde você salvou o arquivo usando o terminal ou prompt de comando.
3.  **Compile:** Execute o comando de compilação do Java:
    ```bash
    javac batalhaNaval.java
    ```
    Isso criará um arquivo `batalhaNaval.class`.
4.  **Execute:** Rode o jogo com o comando:
    ```bash
    java batalhaNaval
    ```
5.  **Siga as Instruções:** O jogo iniciará no console. Siga as instruções na tela para escolher o modo, inserir nomes e jogar.

## Como Jogar 🎮

1.  **Inicie o Jogo:** Execute o programa conforme as instruções acima.
2.  **Escolha o Modo:** Digite `1` para Jogador vs Computador ou `2` para Jogador vs Jogador.
3.  **Insira os Nomes:** Digite os nomes dos jogadores quando solicitado.
4.  **Alocação de Navios:**
    *   Se for um jogador humano, escolha `1` para alocar manualmente ou `2` para automaticamente.
    *   **Manual:** Para cada navio, informe a coordenada inicial (Ex: `A0`, `B5`, `J9`) e a orientação (`H` para horizontal, `V` para vertical). O jogo validará se a posição é permitida (dentro do mapa, sem sobrepor ou encostar em outros navios).
    *   **Automática:** O jogo posicionará seus navios aleatoriamente, respeitando as regras.
    *   O computador sempre aloca automaticamente.
5.  **Jogando os Turnos:**
    *   O jogo indicará de quem é a vez.
    *   Você verá seu **mapa de ataque** (onde você já atirou no inimigo) e seu **mapa de navios** (onde estão seus navios e os danos sofridos).
    *   Digite a coordenada que deseja atacar no mapa inimigo (Ex: `C7`, `H2`).
    *   O resultado será exibido: `ACERTO!` ou `ÁGUA!`.
    *   Se você acertar (`ACERTO!`), você joga novamente.
    *   Se você errar (`ÁGUA!`), o turno passa para o oponente.
6.  **Vencendo:** O primeiro jogador a acertar todas as células ocupadas pelos navios do oponente (`X` em todas as posições de `B`) vence o jogo! Os mapas finais são exibidos.

## Legenda dos Símbolos do Mapa 🗺️

*   `~` : Água (célula vazia ou não atacada no mapa de ataque)
*   `N` : Seu Navio (visível apenas no seu próprio mapa de navios)
*   `X` : Acerto (um tiro que atingiu um navio)
*   `O` : Erro (um tiro que atingiu a água)

---

Divirta-se jogando Batalha Naval!