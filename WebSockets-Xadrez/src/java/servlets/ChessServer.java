package servlets;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ServerEndpoint("/chess")
public class ChessServer {

    private static Session s1;
    private static Session s2;
    private static Tabuleiro tabuleiro;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        if (s1 == null) {
            s1 = session;
            s1.getBasicRemote().sendText("{\"type\": 0, \"color\": 0}");
        } else if (s2 == null) {
            tabuleiro = new Tabuleiro();
            s2 = session;
            s2.getBasicRemote().sendText("{\"type\": 0, \"color\": 1}");
            sendMessage("{\"type\": 1, \"tabuleiro\": " + tabuleiro + ", \"turn\": 0}");
        } else {
            session.close();
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws JSONException, IOException {
        JSONObject obj = new JSONObject(message);
        JSONArray origem = obj.getJSONArray("origem");
        JSONArray destino = obj.getJSONArray("destino");
        tabuleiro.mover(session == s1 ? Tabuleiro.BRANCA : Tabuleiro.PRETA, origem.getInt(0), origem.getInt(1), destino.getInt(0), destino.getInt(1));
        sendMessage("{\"type\": 1, \"tabuleiro\": " + tabuleiro + ", \"turn\": " + tabuleiro.getTurn() + "}");
        if (tabuleiro.qtdDePecas(Tabuleiro.BRANCA) == 0) {
            sendMessage("{\"type\": 2, \"message\": \"Fim de Jogo. As pretas ganharam.\"}");
            s1.close();
            s2.close();
        } else if (tabuleiro.qtdDePecas(Tabuleiro.PRETA) == 0) {
            sendMessage("{\"type\": 2, \"message\": \"Fim de Jogo. As brancas ganharam.\"}");
            s1.close();
            s2.close();
        }
    }

    private void sendMessage(String msg) throws IOException {
        s1.getBasicRemote().sendText(msg);
        s2.getBasicRemote().sendText(msg);
    }
}
