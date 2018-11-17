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
            cell.innerHTML = (col === 0 ? "" : (col === 1 ? '<img src="imagens/Peao-Branco.svg" alt="">' : '<img src="imagens/Peao-Preto.svg" alt="">'));
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