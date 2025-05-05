import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {

    // --- Constantes do Jogo (Configurações) ---
    static final int TAMANHO_MAPA = 10; // Tamanho do mapa (10x10)
    static final char AGUA = '~';      // Símbolo para água
    static final char BARCO = 'B';     // Símbolo para barco (no mapa do jogador)
    static final char ACERTO = 'X';    // Símbolo para tiro que acertou um barco
    static final char ERRO = 'O';      // Símbolo para tiro que caiu na água
    static final char BARCO_VISIVEL = 'N'; // Símbolo para mostrar barco no próprio mapa (N de Navio)

    // Define os tamanhos dos barcos que cada jogador terá
    static final int[] TAMANHOS_BARCOS = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    static int TOTAL_CELULAS_BARCOS = 0; // Será calculado no início do main

    // --- Variáveis Globais do Jogo (Estado do Jogo) ---
    static Scanner scanner = new Scanner(System.in); // Para ler a entrada do teclado
    static Random random = new Random();             // Para gerar números aleatórios (computador)

    // Mapas dos jogadores (onde ficam os barcos)
    static char[][] mapaJogador1;
    static char[][] mapaJogador2;

    // Mapas de ataque (onde os jogadores marcam os tiros)
    static char[][] mapaAtaqueJogador1; // O que J1 vê ao atacar J2
    static char[][] mapaAtaqueJogador2; // O que J2 vê ao atacar J1

    // Nomes e modo de jogo
    static String nomeJogador1 = "Jogador 1";
    static String nomeJogador2 = "Jogador 2";
    static boolean contraComputador = false;

    // Contagem de acertos para saber quem ganhou
    static int acertosJogador1 = 0;
    static int acertosJogador2 = 0;

    // --- Método Principal (Onde o programa começa) ---
    public static void main(String[] args) {

        // Calcula o total de casas que os barcos ocupam
        // Isso é usado para saber quando alguém venceu
        for (int tamanho : TAMANHOS_BARCOS) {
            TOTAL_CELULAS_BARCOS += tamanho;
        }

        exibirBoasVindas();
        int modoJogo = escolherModoJogo();

        if (modoJogo == 2) { // Modo Jogador vs Jogador
            contraComputador = false;
            System.out.print("Digite o nome do Jogador 1: ");
            nomeJogador1 = scanner.nextLine();
            System.out.print("Digite o nome do Jogador 2: ");
            nomeJogador2 = scanner.nextLine();
        } else { // Modo Jogador vs Computador
            contraComputador = true;
            System.out.print("Digite o seu nome: ");
            nomeJogador1 = scanner.nextLine();
            nomeJogador2 = "Computador"; // Nome padrão para o PC
        }

        // Cria os mapas vazios (cheios de água)
        mapaJogador1 = inicializarMapa();
        mapaJogador2 = inicializarMapa();
        mapaAtaqueJogador1 = inicializarMapa(); // Mapa para J1 atacar J2
        mapaAtaqueJogador2 = inicializarMapa(); // Mapa para J2 atacar J1

        // Hora de colocar os barcos nos mapas
        System.out.println("\n--- Preparação: Colocar Barcos ---");
        System.out.println("--> Vez de " + nomeJogador1);
        alocarBarcos(nomeJogador1, mapaJogador1, false); // Jogador 1 sempre aloca (manual ou auto)

        if (contraComputador) {
            System.out.println("\n--> " + nomeJogador2 + " está colocando os barcos...");
            alocarBarcos(nomeJogador2, mapaJogador2, true); // Computador sempre aloca automaticamente
            System.out.println(nomeJogador2 + " terminou de colocar os barcos.");
        } else {
            System.out.println("\n--> Vez de " + nomeJogador2);
            alocarBarcos(nomeJogador2, mapaJogador2, false); // Jogador 2 aloca (manual ou auto)
        }

        // Começa a batalha
        jogar();

        // Fim do jogo
        scanner.close(); // Fecha o leitor de teclado
        System.out.println("\nObrigado por jogar Batalha Naval!");
    }

    // --- Métodos Auxiliares (Funções que ajudam o `main`) ---

    // Mostra a mensagem inicial
    static void exibirBoasVindas() {
        System.out.println("========================================");
        System.out.println("        Bem-vindo à Batalha Naval!      ");
        System.out.println("========================================");
    }

    // Pergunta se quer jogar contra PC ou outra pessoa
    static int escolherModoJogo() {
        String escolhaTexto = "";
        // Continua perguntando até digitar 1 ou 2
        while (!escolhaTexto.equals("1") && !escolhaTexto.equals("2")) {
            System.out.println("\nEscolha o modo de jogo:");
            System.out.println("1. Jogador vs Computador");
            System.out.println("2. Jogador vs Jogador");
            System.out.print("Opção (1 ou 2): ");
            escolhaTexto = scanner.nextLine();

            if (!escolhaTexto.equals("1") && !escolhaTexto.equals("2")) {
                System.out.println("Opção inválida. Por favor, digite apenas 1 ou 2.");
            }
        }
        // Converte o texto "1" ou "2" para número
        return Integer.parseInt(escolhaTexto);
    }

    // Cria um mapa vazio (só com água)
    static char[][] inicializarMapa() {
        char[][] mapa = new char[TAMANHO_MAPA][TAMANHO_MAPA];
        // Passa por cada linha
        for (int i = 0; i < TAMANHO_MAPA; i++) {
            // Passa por cada coluna da linha atual
            for (int j = 0; j < TAMANHO_MAPA; j++) {
                mapa[i][j] = AGUA; // Coloca água na posição
            }
        }
        return mapa; // Retorna o mapa pronto
    }

    // Mostra um mapa na tela
    // `esconderBarcos` diz se os barcos devem aparecer como água (~) ou não (N)
    static void exibirMapa(char[][] mapa, boolean esconderBarcos) {
        // Imprime as letras das colunas (A, B, C...)
        System.out.print("   "); // Espaço para os números das linhas
        for (int i = 0; i < TAMANHO_MAPA; i++) {
            System.out.print((char) ('A' + i) + " "); // Converte 0 para A, 1 para B, etc.
        }
        System.out.println(); // Pula linha

        // Imprime os números das linhas e o conteúdo do mapa
        for (int i = 0; i < TAMANHO_MAPA; i++) {
            System.out.printf("%2d ", i); // Imprime o número da linha (0 a 9) com espaço
            for (int j = 0; j < TAMANHO_MAPA; j++) {
                char celula = mapa[i][j]; // Pega o caractere da posição atual
                // Decide o que mostrar
                if (esconderBarcos && celula == BARCO) {
                    System.out.print(AGUA + " "); // Esconde barco, mostra água
                } else if (!esconderBarcos && celula == BARCO) {
                    System.out.print(BARCO_VISIVEL + " "); // Mostra o barco como 'N'
                } else {
                    System.out.print(celula + " "); // Mostra água (~), acerto (X) ou erro (O)
                }
            }
            System.out.println(); // Pula para a próxima linha do mapa
        }
        System.out.println(); // Linha em branco depois do mapa
    }

    // Pergunta ao jogador como ele quer colocar os barcos (ou faz automaticamente pro PC)
    static void alocarBarcos(String nomeJogador, char[][] mapa, boolean ehComputador) {
        int escolha = 0; // 1 para manual, 2 para automático

        // Se não for o computador, pergunta ao jogador
        if (!ehComputador) {
            String inputUsuario;
            while (escolha != 1 && escolha != 2) {
                System.out.println(nomeJogador + ", como deseja colocar seus barcos?");
                System.out.println("1. Manualmente (escolher posição)");
                System.out.println("2. Automaticamente (sortear posição)");
                System.out.print("Opção (1 ou 2): ");
                inputUsuario = scanner.nextLine();

                if (inputUsuario.equals("1")) {
                    escolha = 1;
                } else if (inputUsuario.equals("2")) {
                    escolha = 2;
                } else {
                    System.out.println("Entrada inválida. Por favor, digite 1 ou 2.");
                }
            }
        }

        // Se for o computador OU se o jogador escolheu automático
        if (ehComputador || escolha == 2) {
            alocarBarcosAutomaticamente(mapa);
        } else { // Se o jogador escolheu manual
            alocarBarcosManualmente(nomeJogador, mapa);
        }
    }

    // Jogador escolhe onde colocar cada barco
    static void alocarBarcosManualmente(String nomeJogador, char[][] mapa) {
        System.out.println("\n" + nomeJogador + ", coloque seus barcos no mapa:");
        exibirMapa(mapa, false); // Mostra o mapa (sem esconder barcos)

        // Para cada tamanho de barco definido na lista TAMANHOS_BARCOS
        for (int tamanhoBarco : TAMANHOS_BARCOS) {
            boolean alocadoComSucesso = false;
            // Continua pedindo posição até conseguir colocar o barco
            while (!alocadoComSucesso) {
                System.out.println("Posicionando um barco de tamanho " + tamanhoBarco);

                // Pede a coordenada inicial (Ex: Linha 5, Coluna C)
                int[] coordenadas = pedirCoordenadas(nomeJogador, "Digite a coordenada inicial");

                // Pede a orientação (Horizontal ou Vertical)
                boolean ehHorizontal = pedirOrientacao(nomeJogador);

                // Tenta colocar o barco na posição escolhida
                if (tentarAlocarBarco(mapa, tamanhoBarco, coordenadas[0], coordenadas[1], ehHorizontal)) {
                    alocadoComSucesso = true; // Conseguiu!
                    System.out.println("Barco colocado com sucesso!");
                    exibirMapa(mapa, false); // Mostra o mapa atualizado
                } else {
                    // Não deu certo (fora do mapa, em cima de outro barco, ou colado)
                    System.out.println("Posição inválida. O barco não pode sair do mapa, ficar sobre outro barco ou encostar em outro. Tente novamente.");
                }
            }
        }
        System.out.println(nomeJogador + " terminou de colocar os barcos.");
    }

    // Pede ao jogador a linha e a coluna
    static int[] pedirCoordenadas(String nomeJogador, String mensagem) {
        int linha = -1;    // Valor inválido inicial para a linha
        int coluna = -1;   // Valor inválido inicial para a coluna
        String inputLinha;
        String inputColuna;

        // Pede a LINHA até ser um número válido (0 a TAMANHO_MAPA-1)
        while (linha < 0 || linha >= TAMANHO_MAPA) {
            System.out.print(nomeJogador + ", " + mensagem + " - LINHA (0-" + (TAMANHO_MAPA - 1) + "): ");
            inputLinha = scanner.nextLine();
            // Verifica se o que foi digitado é um número
            if (isNumero(inputLinha)) {
                linha = Integer.parseInt(inputLinha); // Converte texto para número
                // Verifica se o número está dentro do intervalo permitido
                if (linha < 0 || linha >= TAMANHO_MAPA) {
                    System.out.println("Linha inválida. Use um número entre 0 e " + (TAMANHO_MAPA - 1) + ".");
                    linha = -1; // Marca como inválido para pedir de novo
                }
            } else {
                System.out.println("Entrada inválida. Por favor, digite um NÚMERO para a linha.");
                linha = -1; // Marca como inválido
            }
        }

        // Pede a COLUNA até ser uma letra válida (A a J, se TAMANHO_MAPA for 10)
        char letraFinal = (char) ('A' + TAMANHO_MAPA - 1); // Calcula a última letra válida
        while (coluna < 0 || coluna >= TAMANHO_MAPA) {
            System.out.print(nomeJogador + ", " + mensagem + " - COLUNA (A-" + letraFinal + "): ");
            inputColuna = scanner.nextLine().toUpperCase(); // Lê e converte para maiúscula

            // Verifica se foi digitada apenas UMA letra
            if (inputColuna.length() == 1) {
                char letraColuna = inputColuna.charAt(0);
                // Verifica se a letra está no intervalo A-J (ou qual for o limite)
                if (letraColuna >= 'A' && letraColuna <= letraFinal) {
                    coluna = letraColuna - 'A'; // Converte a letra para número (A=0, B=1, ...)
                } else {
                    System.out.println("Coluna inválida. Use uma letra entre A e " + letraFinal + ".");
                    coluna = -1; // Marca como inválido
                }
            } else {
                System.out.println("Entrada inválida. Por favor, digite apenas UMA LETRA para a coluna.");
                coluna = -1; // Marca como inválido
            }
        }

        // Retorna a linha e coluna como um array de inteiros
        return new int[]{linha, coluna};
    }

    // Função simples para verificar se uma String contém apenas dígitos
    static boolean isNumero(String texto) {
        if (texto == null || texto.isEmpty()) {
            return false; // Nulo ou vazio não é número
        }
        for (int i = 0; i < texto.length(); i++) {
            // Se encontrar algum caractere que NÃO seja dígito
            if (!Character.isDigit(texto.charAt(i))) {
                return false; // Não é um número puro
            }
        }
        return true; // Se passou por todos os caracteres e são dígitos, é número
    }


    // Pergunta se o barco será Horizontal ou Vertical
    static boolean pedirOrientacao(String nomeJogador) {
        String orientacao = "";
        // Continua pedindo até digitar H ou V
        while (!orientacao.equals("H") && !orientacao.equals("V")) {
            System.out.print(nomeJogador + ", Orientação (H para horizontal, V para vertical): ");
            orientacao = scanner.nextLine().toUpperCase(); // Lê e converte para maiúscula
            if (!orientacao.equals("H") && !orientacao.equals("V")) {
                System.out.println("Opção inválida. Digite H ou V.");
            }
        }
        return orientacao.equals("H"); // Retorna true se for Horizontal, false se for Vertical
    }

    // Coloca os barcos aleatoriamente no mapa (para o computador ou escolha do jogador)
    static void alocarBarcosAutomaticamente(char[][] mapa) {
        // Para cada tamanho de barco
        for (int tamanhoBarco : TAMANHOS_BARCOS) {
            boolean alocadoComSucesso = false;
            // Tenta posições aleatórias até conseguir colocar
            while (!alocadoComSucesso) {
                int linha = random.nextInt(TAMANHO_MAPA);     // Sorteia linha (0 a 9)
                int coluna = random.nextInt(TAMANHO_MAPA);    // Sorteia coluna (0 a 9)
                boolean horizontal = random.nextBoolean(); // Sorteia orientação (true=H, false=V)

                // Tenta colocar o barco na posição sorteada
                if (tentarAlocarBarco(mapa, tamanhoBarco, linha, coluna, horizontal)) {
                    alocadoComSucesso = true; // Deu certo!
                }
                // Se não deu certo, o loop continua e tenta outra posição
            }
        }
    }

    // Verifica se é possível colocar um barco numa posição e orientação
    static boolean tentarAlocarBarco(char[][] mapa, int tamanho, int linha, int coluna, boolean horizontal) {
        // 1. Verifica se o barco cabe dentro do mapa
        if (horizontal) {
            // Se for horizontal, verifica se a coluna + tamanho não ultrapassa o limite
            if (coluna + tamanho > TAMANHO_MAPA) {
                return false; // Não cabe
            }
        } else {
            // Se for vertical, verifica se a linha + tamanho não ultrapassa o limite
            if (linha + tamanho > TAMANHO_MAPA) {
                return false; // Não cabe
            }
        }

        // 2. Verifica se a área (incluindo vizinhos) está livre (sem outros barcos)
        for (int i = 0; i < tamanho; i++) {
            // Calcula a posição da parte atual do barco
            int lAtual = linha + (horizontal ? 0 : i);
            int cAtual = coluna + (horizontal ? i : 0);

            // Verifica a célula atual e todas as 8 vizinhas (incluindo diagonais)
            for (int dx = -1; dx <= 1; dx++) { // Variação na linha (-1, 0, 1)
                for (int dy = -1; dy <= 1; dy++) { // Variação na coluna (-1, 0, 1)
                    int lVerificar = lAtual + dx;
                    int cVerificar = cAtual + dy;

                    // Verifica se a coordenada vizinha está DENTRO do mapa
                    if (lVerificar >= 0 && lVerificar < TAMANHO_MAPA && cVerificar >= 0 && cVerificar < TAMANHO_MAPA) {
                        // Se a célula vizinha (ou a própria célula) contém um barco, não pode alocar aqui
                        if (mapa[lVerificar][cVerificar] == BARCO) {
                            return false; // Posição ocupada ou adjacente a outro barco
                        }
                    }
                }
            }
        }

        // 3. Se passou por todas as verificações, o lugar é válido! Coloca o barco.
        for (int i = 0; i < tamanho; i++) {
            if (horizontal) {
                mapa[linha][coluna + i] = BARCO; // Coloca parte do barco na horizontal
            } else {
                mapa[linha + i][coluna] = BARCO; // Coloca parte do barco na vertical
            }
        }

        return true; // Barco alocado com sucesso!
    }


    // --- Métodos do Loop Principal do Jogo ---

    // Controla os turnos do jogo até alguém vencer
    static void jogar() {
        System.out.println("\n--- O Jogo Começou! Que vença o melhor! ---");

        boolean jogoAcabou = false;
        boolean turnoJogador1 = true; // Jogador 1 começa

        // Loop principal do jogo, continua enquanto ninguém ganhou
        while (!jogoAcabou) {
            char[][] mapaDefensorAtual; // Mapa de quem está sendo atacado
            char[][] mapaAtaqueAtual;   // Mapa de quem está atacando (para marcar os tiros)
            String nomeAtacante;
            boolean ehTurnoComputador;

            // Define de quem é a vez e quais mapas usar
            if (turnoJogador1) {
                nomeAtacante = nomeJogador1;
                mapaDefensorAtual = mapaJogador2; // J1 ataca o mapa de J2
                mapaAtaqueAtual = mapaAtaqueJogador1; // J1 marca os tiros no seu mapa de ataque
                ehTurnoComputador = false;
                System.out.println("\n--- Turno de " + nomeAtacante + " ---");
                System.out.println("Mapa onde você ataca (" + nomeJogador2 + "):");
                exibirMapa(mapaAtaqueAtual, true); // Mostra onde J1 já atirou
                System.out.println("Seu mapa com seus barcos:");
                exibirMapa(mapaJogador1, false);   // Mostra o mapa de J1 (com barcos visíveis)
            } else { // Turno do Jogador 2 (ou Computador)
                nomeAtacante = nomeJogador2;
                mapaDefensorAtual = mapaJogador1; // J2/PC ataca o mapa de J1
                mapaAtaqueAtual = mapaAtaqueJogador2; // J2/PC marca os tiros no seu mapa de ataque
                ehTurnoComputador = contraComputador; // É turno do PC se estivermos no modo vs PC

                System.out.println("\n--- Turno de " + nomeAtacante + " ---");
                // Se for humano, mostra os mapas dele
                if (!ehTurnoComputador) {
                    System.out.println("Mapa onde você ataca (" + nomeJogador1 + "):");
                    exibirMapa(mapaAtaqueAtual, true);
                    System.out.println("Seu mapa com seus barcos:");
                    exibirMapa(mapaJogador2, false);
                }
            }

            // Executa o turno do jogador atual (pede tiro ou sorteia, processa)
            boolean acertou = turnoJogador(nomeAtacante, mapaDefensorAtual, mapaAtaqueAtual, ehTurnoComputador);

            // Verifica se alguém ganhou depois do tiro
            if (acertosJogador1 == TOTAL_CELULAS_BARCOS) {
                jogoAcabou = true;
                exibirVencedor(nomeJogador1);
                exibirMapasFinais(); // Mostra como ficaram os mapas no final
            } else if (acertosJogador2 == TOTAL_CELULAS_BARCOS) {
                jogoAcabou = true;
                exibirVencedor(nomeJogador2);
                exibirMapasFinais();
            } else {
                // Se ninguém ganhou ainda...
                if (acertou) {
                    // Se acertou, joga de novo! Não passa o turno.
                    System.out.println("Você acertou! Jogue novamente.");
                } else {
                    // Se errou, passa a vez para o outro jogador
                    System.out.println("Você errou (água!). Passando a vez.");
                    turnoJogador1 = !turnoJogador1; // Inverte o turno (true vira false, false vira true)

                    // Pausa entre turnos (apenas se for Jogo entre Humanos)
                    if (!contraComputador && !jogoAcabou) { // Só pausa se for PvP e o jogo não acabou
                        System.out.println("\nPressione Enter para passar o turno para " + (turnoJogador1 ? nomeJogador1 : nomeJogador2) + "...");
                        scanner.nextLine(); // Espera o jogador pressionar Enter
                    }
                  
                }
            }
        } // Fim do loop while (!jogoAcabou)
    }

    // Executa as ações de um turno: pedir/sortear coordenada, verificar tiro, processar
    // Retorna true se acertou, false se errou ou atirou repetido
    static boolean turnoJogador(String nomeAtacante, char[][] mapaDefensor, char[][] mapaAtaque, boolean ehComputador) {
        int linha = -1, coluna = -1;
        boolean tiroValido = false; // Para controlar se a coordenada escolhida é nova

        // Loop para garantir que a coordenada escolhida ainda não foi atacada
        while (!tiroValido) {
            if (ehComputador) {
                // Computador sorteia uma coordenada
                linha = random.nextInt(TAMANHO_MAPA);
                coluna = random.nextInt(TAMANHO_MAPA);
                System.out.println(nomeAtacante + " atira em " + (char) ('A' + coluna) + linha + "...");
            } else {
                // Jogador humano escolhe a coordenada
                int[] coords = pedirCoordenadas(nomeAtacante, "Digite a coordenada para ATACAR");
                linha = coords[0];
                coluna = coords[1];
            }

            // Verifica se essa posição JÁ foi atacada antes (olhando no mapa de ataque)
            if (mapaAtaque[linha][coluna] == ACERTO || mapaAtaque[linha][coluna] == ERRO) {
                // Se já atirou ali...
                if (!ehComputador) {
                    // Avisa o jogador humano
                    System.out.println("Você já atirou nessa posição (" + (char) ('A' + coluna) + linha + "). Tente novamente.");
                }
                // Se for o computador, ele simplesmente vai sortear outra na próxima volta do loop
                tiroValido = false; // Força o loop a pedir/sortear de novo
            } else {
                // Se a posição é nova, o tiro é válido! Sai do loop de pedir coordenada.
                tiroValido = true;
            }
        } // Fim do loop while (!tiroValido)

        // Agora que temos uma coordenada válida (não repetida), processa o ataque
        char resultadoNoMapaDefensor = mapaDefensor[linha][coluna];
        boolean foiAcerto = false;

        if (resultadoNoMapaDefensor == BARCO) {
            // Acertou um barco!
            System.out.println("ACERTO!");
            mapaAtaque[linha][coluna] = ACERTO;   // Marca 'X' no mapa de ataque
            mapaDefensor[linha][coluna] = ACERTO; // Marca 'X' no mapa original do defensor também
            foiAcerto = true;

            // Contabiliza o acerto para o jogador correto
            if (nomeAtacante.equals(nomeJogador1)) {
                acertosJogador1++;
            } else {
                acertosJogador2++;
            }

        } else if (resultadoNoMapaDefensor == AGUA) {
            // Acertou a água
            System.out.println("ÁGUA!");
            mapaAtaque[linha][coluna] = ERRO; // Marca 'O' no mapa de ataque
            // Não precisa mudar o mapa do defensor se era água
            foiAcerto = false;
        }
        // Não deve acontecer de atirar onde já tem ACERTO ou ERRO,
        // pois a validação `tiroValido` acima impede isso.

        return foiAcerto; // Retorna true se acertou, false se errou
    }


    // Mostra a mensagem de quem ganhou
    static void exibirVencedor(String nomeVencedor) {
        System.out.println("\n========================================");
        System.out.println("           FIM DE JOGO!               ");
        System.out.println("      " + nomeVencedor.toUpperCase() + " VENCEU! PARABÉNS!          ");
        System.out.println("========================================");
    }

    // Mostra os mapas finais de ambos os jogadores
    static void exibirMapasFinais() {
        System.out.println("\n--- Mapa Final de " + nomeJogador1 + " ---");
        exibirMapa(mapaJogador1, false); // Mostra barcos/acertos de J1
        System.out.println("\n--- Mapa Final de " + nomeJogador2 + " ---");
        exibirMapa(mapaJogador2, false); // Mostra barcos/acertos de J2
    }
}