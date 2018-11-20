var corDaVez = 0, connection, cor;
function coordinates(cell) {
    var dc = cell.cellIndex;
    var dr = cell.parentNode.rowIndex;
    return [dr, dc];
}
function drag(ev) {
    ev.dataTransfer.effectAllowed = 'move';
    ev.dataTransfer.setData("text/plain", "[" + coordinates(this.parentNode) + "]");
}
function allowDrop(ev) {
    ev.preventDefault();
}
function drop(ev) {
    ev.preventDefault();
    connection.send(JSON.stringify({origem: JSON.parse(ev.dataTransfer.getData("text/plain")), destino: coordinates(this)}));
}
function setMessage(mensagem) {
    var p = document.getElementsByTagName("p")[0];
    p.innerHTML = mensagem;
}
function onMessage(evt) {
    var msg = JSON.parse(evt.data);
    switch (msg.type) {
        case 0:
            /* Informando cor da peça do usuário atual */
            cor = msg.color;
            break;
        case 1:
            /* Recebendo o tabuleiro modificado */
            corDaVez = msg.turn;
            setMessage((corDaVez === cor) ? "É a sua vez de jogar." : "É a vez do adversário de jogar.");
            montarTabela(msg.tabuleiro);
            break;
        case 2:
            /* Fim do jogo */
            setMessage(msg.message);
            connection.close();
            break;
    }
}
function montarTabela(tabuleiro) {
    var table = document.getElementsByTagName("table")[0];
    tabuleiro.forEach(function (row, rowIndex) {
        row.forEach(function (col, colIndex) {
            var cell = table.rows[rowIndex].cells[colIndex];
            cell.innerHTML = 
                    (col === 0 ? "" : 
                        (col === 1 ? '<img src="imagens/Peao-Branco.svg" alt="">' :
                            (col === 2 ? '<img src="imagens/Torre-Branca.svg" alt="">':
                                (col === 3 ? '<img src="imagens/Cavalo-Branco.svg" alt="">':
                                    (col === 4 ? '<img src="imagens/Bispo-Branco.svg" alt="">':
                                        (col === 5 ? '<img src="imagens/Rei-Branco.svg" alt="">':
                                            (col === 6 ? '<img src="imagens/Rainha-Branca.svg" alt="">':
                                                (col === 7 ? '<img src="imagens/Peao-Preto.svg" alt="">':
                                                    (col === 8 ? '<img src="imagens/Torre-Preta.svg" alt="">':
                                                        (col === 9 ? '<img src="imagens/Cavalo-Preto.svg" alt="">':
                                                            (col === 10 ? '<img src="imagens/Bispo-Preto.svg" alt="">':
                                                                (col === 11 ? '<img src="imagens/Rei-Preto.svg" alt="">':
                                                                    (col === 12 ? '<img src="imagens/Rainha-Preta.svg" alt="">' :"")))))))))))));
//                    '<img src="imagens/Peao-Preto.svg" alt="">'));;;
            var x = cell.firstChild;
            if (x) {
                x.draggable = true;
                x.ondragstart = drag;
            }
            cell.ondragover = allowDrop;
            cell.ondrop = drop;
        });
    });
}
window.onload = function () {
    connection = new WebSocket("ws://" + document.location.host + document.location.pathname + "chess");
    connection.onmessage = onMessage;
};