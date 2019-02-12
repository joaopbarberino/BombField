package bombfield;

/* 
 * Gabriel Alves Freitas
 * João Pedro Barberino
 * Matheus Vicente
 * Jogos Digitais - 2º Período
 */
import java.util.Scanner;

public class BombField {

    public static void main(String[] args) {
        game();
    }

    public static void game() {
        int move, line, col, endGame, bombRest, casasRest, replay = 1;
        boolean abreTudo, valido = false;

        Scanner ler = new Scanner(System.in);

        // Game loop secundário que checa se o jogador quer jogar de novo após uma vitória ou derrota
        while (replay != 2) {
            System.out.println("O jogo só acaba quando todas as casas que não forem bombas forem abertas! Boa sorte!");
            char mask[][];
            int system[][];
            endGame = 0;
            mask = initialize(); // Inicializa a matriz do usuário
            system = initializeSystem(); // Inicializa a matriz do sistema
            bombRest = bombRest(system); // Conta as bombas restantes pra exibir ao usuário
            casasRest = casasRest(system); // Conta as casas que não têm bombas
            System.out.println();

            // Game loop principal
            while (endGame != 1) {
                print(mask, bombRest, casasRest);
                System.out.println("------------------------------------------");
                printTemp(system);

                System.out.println();
                System.out.println("Digite 1 para revelar uma posição, 2 para "
                        + "marcar uma posição e 3 para desmarcar uma posição: ");
                move = ler.nextInt();

                while (move != 1 && move != 2 && move != 3) {
                    System.out.println();
                    System.out.println("Escolha inválida!");
                    System.out.println("Digite 1 para revelar uma posição, 2 para "
                        + "marcar uma posição e 3 para desmarcar uma posição: ");
                    move = ler.nextInt();
                }
                
                System.out.println();
                System.out.println("Digite a posição que deseja fazer a jogada!");
                System.out.printf("Linha: ");
                line = ler.nextInt();
                System.out.printf("Coluna: ");
                col = ler.nextInt();
                
                //if (line > 4 || col > 4 || line < 1 || col < 1) {
                    
                while(line > 4 || col > 4 || line < 1 || col < 1) {
                    System.out.println("Essa posição não existe! Escolha uma nova posição.");
                    System.out.println();
                    System.out.println("Digite a posição que deseja jogar!");
                    System.out.printf("Linha: ");
                    line = ler.nextInt();
                    System.out.printf("Coluna: ");
                    col = ler.nextInt();
                }
                
                // Abrir posição 
                if (move == 1) {
                    // Derrota
                    if (step(system, mask, line, col, move) == 0) {
                        abreTudo = true;
                        abrePos(system, mask, abreTudo, line, col, casasRest);
                        bombRest = 0;
                        casasRest = 0;
                        System.out.println("Você perdeu!");
                        print(mask, bombRest, casasRest);
                        endGame = 1;
                    }

                    // Abrir com sucesso
                    if (step(system, mask, line, col, move) == -1) {
                        abreTudo = false;
                        casasRest = abrePos(system, mask, abreTudo, line, col, casasRest);
                    }

                    // Vitória
                    if (step(system, mask, line, col, move) == -1 && status(casasRest) == true) {
                        System.out.println("Parabéns! Você venceu!");
                        abreTudo = true;
                        abrePos(system, mask, abreTudo, line, col, casasRest);
                        bombRest = 0;
                        print(mask, bombRest, casasRest);
                        endGame = 1;
                    }
                } // Marcar posição
                else if (move == 2) {
                    if (step(system, mask, line, col, move) == 1) {
                        bombRest--;
                    } else if (step(system, mask, line, col, move) == 3) {
                        System.out.println("Erro! Essa casa já está marcada ou revelada!");
                    }
                } // Desmarcar posição
                else if (move == 3) {
                    if (step(system, mask, line, col, move) == 1) {
                        bombRest++;
                    } else if (step(system, mask, line, col, move) == 3) {
                        System.out.println("Erro! Essa casa não está marcada ou já foi revelada.");
                    }
                }
            }

            System.out.println("Deseja jogar novamente? Digite 1 para Sim e 2 para Não: ");
            replay = ler.nextInt();
            if (replay == 1) {
                System.out.println("Então lá vamos nós de novo!");
            } else if (replay == 2) {
                System.out.println("Obrigado por jogar!");
            }
        }
    }

    public static boolean status(int casasRest) {
        if (casasRest == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static int step(int system[][], char mask[][], int line, int col, int move) {
        // Aderimos ao 'state = 3' como um retorno de erro
        int tip = system[line][col], state = -1;
        if (move == 1) {
            if (tip < 0) {
                state = 0;
            }
            if (tip >= 0) {
                state = -1;
            }

        } else if (move == 2) {
            if (mask[line][col] == ' ') {
                mask[line][col] = '!';
                state = 1;
            } else if (mask[line][col] != ' ') {
                state = 3;
            }

        } else if (move == 3) {
            if (mask[line][col] == '!') {
                mask[line][col] = ' ';
                state = 1;
            } else {
                state = 3;
            }
        }
        return state;
    }

    // Abre posições, uma por uma ou todas de uma vez
    public static int abrePos(int system[][], char mask[][], boolean abreTudo, int line, int col, int casasRest) {
        // Se o usuário abriu uma bomba, 'abreTudo' chega como 'true' e todas as casas serão exibidas, sinalizando a derrota
        // Eu não sei explicar porquê, mas matrizes e vetores não precisam ser retornados para serem alterados em outros lugares
        if (abreTudo == true) {
            for (int j = 1; j < 5; j++) {
                for (int i = 1; i < 5; i++) {
                    if (system[j][i] == 0) {
                        mask[j][i] = '0';
                    }
                    if (system[j][i] == 1) {
                        mask[j][i] = '1';
                    }
                    if (system[j][i] == 2) {
                        mask[j][i] = '2';
                    }
                    if (system[j][i] == 3) {
                        mask[j][i] = '3';
                    }
                    if (system[j][i] == 4) {
                        mask[j][i] = '4';
                    }
                    if (system[j][i] == 5) {
                        mask[j][i] = '5';
                    }
                    if (system[j][i] == 6) {
                        mask[j][i] = '6';
                    }
                    if (system[j][i] == 7) {
                        mask[j][i] = '7';
                    }
                    if (system[j][i] == 8) {
                        mask[j][i] = '8';
                    }
                    if (system[j][i] == 9) {
                        mask[j][i] = '9';
                    }
                    if (system[j][i] == -1) {
                        mask[j][i] = '*';
                    }
                }
            }
        }
        // Se o usuario abriu uma casa marcada ou com uma dica, 'abreTudo' chega como 'false' e somente a casa[linha][coluna] será exibida
        // O número de casas restantes é decrementado e retornado
        // Não consegui pensar num jeito (que funciona) de printar isso fora da função
        if (abreTudo == false) {
            if (system[line][col] == 0) {
                if (mask[line][col] == ' ' || mask[line][col] == '!') {
                    mask[line][col] = '0';
                    casasRest--;
                } else {
                    System.out.println("Erro! A casa já estava aberta!");
                }
            }
            if (system[line][col] == 1) {
                if (mask[line][col] == ' ' || mask[line][col] == '!') {
                    mask[line][col] = '1';
                    casasRest--;
                } else {
                    System.out.println("Erro! A casa já estava aberta!");
                }
            }
            if (system[line][col] == 2) {
                if (mask[line][col] == ' ' || mask[line][col] == '!') {
                    mask[line][col] = '2';
                    casasRest--;
                } else {
                    System.out.println("Erro! A casa já estava aberta!");
                }
            }
            if (system[line][col] == 3) {
                if (mask[line][col] == ' ' || mask[line][col] == '!') {
                    mask[line][col] = '3';
                    casasRest--;
                } else {
                    System.out.println("Erro! A casa já estava aberta!");
                }
            }
            if (system[line][col] == 4) {
                if (mask[line][col] == ' ' || mask[line][col] == '!') {
                    mask[line][col] = '4';
                    casasRest--;
                } else {
                    System.out.println("Erro! A casa já estava aberta!");
                }
            }
            if (system[line][col] == 5) {
                if (mask[line][col] == ' ' || mask[line][col] == '!') {
                    mask[line][col] = '5';
                    casasRest--;
                } else {
                    System.out.println("Erro! A casa já estava aberta!");
                }
            }
            if (system[line][col] == 6) {
                if (mask[line][col] == ' ' || mask[line][col] == '!') {
                    mask[line][col] = '6';
                    casasRest--;
                } else {
                    System.out.println("Erro! A casa já estava aberta!");
                }
            }
            if (system[line][col] == 7) {
                if (mask[line][col] == ' ' || mask[line][col] == '!') {
                    mask[line][col] = '7';
                    casasRest--;
                } else {
                    System.out.println("Erro! A casa já estava aberta!");
                }
            }
            if (system[line][col] == 8) {
                if (mask[line][col] == ' ' || mask[line][col] == '!') {
                    mask[line][col] = '8';
                    casasRest--;
                } else {
                    System.out.println("Erro! A casa já estava aberta!");
                }
            }
            if (system[line][col] == 9) {
                if (mask[line][col] == ' ' || mask[line][col] == '!') {
                    mask[line][col] = '9';
                    casasRest--;
                } else {
                    System.out.println("Erro! A casa já estava aberta!");
                }
            }
            return casasRest;
        }
        return 0;
    }

    // Inicializa a matriz que o usuário vê
    public static char[][] initialize() {
        char[][] mask = new char[5][5];

        for (int line = 0; line < 5; line++) {
            for (int col = 0; col < 5; col++) {
                mask[line][col] = ' ';
            }
        }
        return mask;
    }

    // Inicializa a matriz do jogo
    public static int[][] initializeSystem() {
        int[][] system = new int[6][6];
        int bombLin = 0, bombCol = 0, i = 0;

        // Espalha as bombas
        while (i < 5) {
            bombLin = (int) (Math.random() * ((4 - 1) + 1)) + 1;
            bombCol = (int) (Math.random() * ((4 - 1) + 1)) + 1;

            if (system[bombLin][bombCol] == 0) {
                system[bombLin][bombCol] = -1;

                i += 1;
            }
        }

        // Preenche as outras posições
        for (int linha = 1; linha < 5; linha++) {
            for (int coluna = 1; coluna < 5; coluna++) {
                // Incrementa as dicas conforme encontra bombas
                // Olha pra esquerda
                if (system[linha][coluna] == -1 && system[linha][coluna - 1] != -1) {
                    system[linha][coluna - 1] += 1;
                }

                // Olha pra direita
                if (system[linha][coluna] == -1 && system[linha][coluna + 1] != -1) {
                    system[linha][coluna + 1] += 1;
                }

                // Olha pra cima
                if (system[linha][coluna] == -1 && system[linha - 1][coluna] != -1) {
                    system[linha - 1][coluna] += 1;
                }

                // Olha pra baixo
                if (system[linha][coluna] == -1 && system[linha + 1][coluna] != -1) {
                    system[linha + 1][coluna] += 1;
                }

                // Olha pra diagonal superior direita
                if (system[linha][coluna] == -1 && system[linha - 1][coluna + 1] != -1) {
                    system[linha - 1][coluna + 1] += 1;
                }

                // Olha pra diagonal superior esquerda
                if (system[linha][coluna] == -1 && system[linha - 1][coluna - 1] != -1) {
                    system[linha - 1][coluna - 1] += 1;
                }

                // Olha pra diagonal inferior direita
                if (system[linha][coluna] == -1 && system[linha + 1][coluna + 1] != -1) {
                    system[linha + 1][coluna + 1] += 1;
                }

                // Olha pra diagonal inferior esquerda
                if (system[linha][coluna] == -1 && system[linha + 1][coluna - 1] != -1) {
                    system[linha + 1][coluna - 1] += 1;
                }
            }
        }
        return system;
    }

    // Contador inicial de bombas restantes
    public static int bombRest(int system[][]) {
        int bomba = 0;

        for (int line = 1; line < 5; line++) {
            for (int col = 1; col < 5; col++) {
                if (system[line][col] == -1) {
                    bomba++;
                }
            }
        }
        return bomba;
    }

    // Contador inicial de casas a serem abertas para ganhar o jogo
    public static int casasRest(int system[][]) {
        int casasRest = 0;

        for (int line = 1; line < 5; line++) {
            for (int col = 1; col < 5; col++) {
                if (system[line][col] != -1) {
                    casasRest++;
                }
            }
        }
        return casasRest;
    }

    // Printa matriz do usuário
    public static void print(char matriz[][], int bombRest, int casasRest) {
        if (bombRest >= 0) {
            System.out.println("Bombas restantes: " + bombRest);
        }
        if (bombRest < 0) {
            System.out.println("Bombas restantes: " + bombRest + "(Acho que tem alguma coisa marcada errada aí, hein?)");
        }
        System.out.println("Casas restantes: " + casasRest);

        for (int line = 1; line < 5; line++) {
            for (int col = 1; col < 5; col++) {
                System.out.printf("[%2c ]", matriz[line][col]);
            }
            System.out.println("");
        }
    }

    // PRINT SISTEMA
    public static void printTemp(int matriz[][]) {
        for (int line = 1; line < 5; line++) {
            for (int col = 1; col < 5; col++) {
                System.out.printf("[%2d]", matriz[line][col]);
            }
            System.out.println("");
        }

    }
}
