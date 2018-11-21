package servlets;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Tabuleiro {

    private int turn = 0;
    private int[][] tabuleiro = {
        {8, 9, 10, 12, 11, 10, 9, 8},
        {7, 7, 7, 7, 7, 7, 7, 7},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {1, 1, 1, 1, 1, 1, 1, 1},
        {2, 3, 4, 5, 6, 4, 3, 2},};
//    PEÇAS BRANCAS
    public static int BRANCA = 1;
    public static int TORRE_BRANCA = 2;
    public static int CAVALO_BRANCA = 3;
    public static int BISPO_BRANCA = 4;
    public static int REI_BRANCA = 5;
    public static int RAINHA_BRANCA = 6;
//    PEÇAS PRETAS
    public static int PRETA = 7;
    public static int TORRE_PRETA = 8;
    public static int CAVALO_PRETA = 9;
    public static int BISPO_PRETA = 10;
    public static int REI_PRETA = 11;
    public static int RAINHA_PRETA = 12;

    public int getTurn() {
        return turn;
    }

    private boolean pecaEhBranca(int x, int y) {
        return tabuleiro[x][y] == BRANCA;
    }

    private boolean pecaEhTorreBranca(int x, int y) {
        return tabuleiro[x][y] == TORRE_BRANCA;
    }

    private boolean pecaEhBispoBranco(int x, int y) {
        return tabuleiro[x][y] == BISPO_BRANCA;
    }

    private boolean pecaEhCavaloBranco(int x, int y) {
        return tabuleiro[x][y] == CAVALO_BRANCA;
    }

    private boolean pecaEhReiBranco(int x, int y) {
        return tabuleiro[x][y] == REI_BRANCA;
    }

    private boolean pecaEhRainhaBranco(int x, int y) {
        return tabuleiro[x][y] == RAINHA_BRANCA;
    }

    public int qtdDePecas(int peca) {
        IntStream stream = Arrays.stream(tabuleiro).flatMapToInt(x -> Arrays.stream(x));
        return stream.reduce(0, (a, b) -> a + (b == peca ? 1 : 0));
    }

    public boolean podeJogarNovamente() {
        if (turn == 0) {

        } else {

        }
        return true;
    }

    public boolean mover(int player, int or, int oc, int dr, int dc) {
        /* É a sua vez de jogar? */
        if ((player == PRETA && (pecaEhBranca(or, oc) || turn == 0)) || (player == BRANCA && (!pecaEhBranca(or, oc) || turn == 1))) {
            return false;
        }

        if (pecaEhBranca(or, oc)) {
            System.out.println("Peão branco");
        } else if (!pecaEhBranca(or, oc)) {
            System.out.println("Peão Preto");
        } else if (pecaEhCavaloBranco(or, oc)) {
            System.out.println("Cavalo branco");
            moverCavalo(player, or, oc, dr, dc);
        } else if (!pecaEhCavaloBranco(or, oc)) {
            System.out.println("Cavalo preto");
            moverCavalo(player, or, oc, dr, dc);
        }
//        else if(pecaEhBispoBranco(or, oc)){
//            System.out.println("Bispo branco");
//        }
        /* Origem e destino devem ser diferentes */
        if (or == dr && oc == dc) {
            return false;
        }
        /* Origem e destino estão no tabuleiro */
        if ((or < 0 || or >= 8) || (oc < 0 || oc >= 8) || (dr < 0 || dr >= 8) || (dc < 0 || dc >= 8)) {
            return false;
        }
        /* Origem possui uma peça? */
        if (tabuleiro[or][oc] == 0) {
            return false;
        }

        boolean ok = false;
//        /* Jogada peão */
        if ((pecaEhBranca(or, oc) && or == dr + 1 && (oc == dc + 0 || oc == dc - 0)) || (!pecaEhBranca(or, oc) && or == dr - 1 && (oc == dc - 0 || oc == dc + 0))) {
            tabuleiro[dr][dc] = tabuleiro[or][oc];
            tabuleiro[or][oc] = 0;
            ok = true;
        }
//        if ((tabuleiro[or + 1][oc + 1] != 0 && tabuleiro[or + 1][or - 1] != 0) || (tabuleiro[or - 1][oc + 1] != 0 && tabuleiro[or - 1][or - 1] != 0)) {
        if (tabuleiro[dr][dc] != 0) {
            if ((pecaEhBranca(or, oc) && or == dr + 1 && (oc == dc + 1 || oc == dc - 1)) || (!pecaEhBranca(or, oc) && or == dr - 1 && (oc == dc - 1 || oc == dc + 1))) {
                tabuleiro[dr][dc] = tabuleiro[or][oc];
                tabuleiro[or][oc] = 0;
                ok = true;
            }
        }
//        /* Capturar com peão peça branca adversaria */
        if (dr == or + 1 && (dc == oc - 1 || dc == oc + 1) && tabuleiro[dr][dc] != 0 && tabuleiro[dr][dc] < 6) {
            boolean corPeca = pecaEhBranca(or, oc);
            boolean corOutra = pecaEhBranca(dr, dc);
            if ((corPeca && !corOutra) || (!corPeca && corOutra)) {
                tabuleiro[dr][dc] = tabuleiro[or][oc];
                tabuleiro[or][oc] = 0;
                ok = true;
            }
        }
//        /* Capturar com peão peça preta adversaria */
        if (dr == or + 1 && (dc == oc - 1 || dc == oc + 1) && tabuleiro[dr][dc] != 0 && tabuleiro[dr][dc] < 7) {
            boolean corPeca = pecaEhBranca(or, oc);
            boolean corOutra = pecaEhBranca(dr, dc);
            if ((corPeca && !corOutra) || (!corPeca && corOutra)) {
                tabuleiro[dr][dc] = tabuleiro[or][oc];
                tabuleiro[or][oc] = 0;
                ok = true;
            }
        }

        if (ok) {
            turn = (turn + 1) % 2;
        }
        return ok;
    }

    public boolean moverCavalo(int player, int or, int oc, int dr, int dc) {
        /* É a sua vez de jogar? */
        if ((player == PRETA && (pecaEhBranca(or, oc) || turn == 0)) || (player == BRANCA && (!pecaEhBranca(or, oc) || turn == 1))) {
            return false;
        }
        /* Origem e destino devem ser diferentes */
        if (or == dr && oc == dc) {
            return false;
        }
        /* Origem e destino estão no tabuleiro */
        if ((or < 0 || or >= 8) || (oc < 0 || oc >= 8) || (dr < 0 || dr >= 8) || (dc < 0 || dc >= 8)) {
            return false;
        }
        /* Origem possui uma peça? */
        if (tabuleiro[or][oc] == 0) {
            return false;
        }

        boolean ok = false;

        if ((pecaEhCavaloBranco(or, oc) && or == dr + 3 && (oc == dc + 0 || oc == dc - 0)) || (!pecaEhCavaloBranco(or, oc) && or == dr - 3 && (oc == dc - 0 || oc == dc + 0))) {
            tabuleiro[dr][dc] = tabuleiro[or][oc];
            tabuleiro[or][oc] = 0;
            ok = true;
        }

        if ((pecaEhCavaloBranco(or, oc) && or == dr + 0 && (oc == dc + 3 || oc == dc - 3)) || (!pecaEhCavaloBranco(or, oc) && or == dr - 0 && (oc == dc - 3 || oc == dc + 3))) {
            tabuleiro[dr][dc] = tabuleiro[or][oc];
            tabuleiro[or][oc] = 0;
            ok = true;
        }

        return ok;
    }

    public void printTabuleiro() {
        for (int[] linha : tabuleiro) {
            System.out.println(Arrays.toString(linha));
        }
    }

    @Override
    public String toString() {
        return Arrays.deepToString(tabuleiro);
    }

    public static void main(String[] args) {
        Tabuleiro tab = new Tabuleiro();

        tab.mover(BRANCA, 5, 0, 4, 1);
        tab.mover(PRETA, 2, 3, 3, 2);
        tab.mover(PRETA, 2, 7, 3, 6);
//        tab.jogadaValida(BRANCA, 4, 1, 2, 3);
//        tab.jogadaValida(PRETA, 1, 2, 3, 4);
//        tab.jogadaValida(BRANCA, 5, 2, 4, 1);
//        tab.jogadaValida(PRETA, 5, 4, 4, 3);

        tab.printTabuleiro();
    }
}
