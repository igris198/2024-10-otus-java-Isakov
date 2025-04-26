let stompClient = null;

const chatLineElementId = "chatLine";
const roomIdElementId = "roomId";
const messageElementId = "message";


const setConnected = (connected) => {
    const connectBtn = document.getElementById("connect");
    const disconnectBtn = document.getElementById("disconnect");

    connectBtn.disabled = connected;
    disconnectBtn.disabled = !connected;
    const chatLine = document.getElementById(chatLineElementId);
    chatLine.hidden = !connected;
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        const userName = frame.headers['user-name'];
        const roomId = document.getElementById(roomIdElementId).value;
        console.log(`Connected to roomId: ${roomId} frame:${frame}`);
        const topicName = `/topic/response.${roomId}`;
        const topicNameUser = `/user/${userName}${topicName}`;
        stompClient.subscribe(topicName, (message) => showMessage(JSON.parse(message.body).messageStr));
        stompClient.subscribe(topicNameUser, (message) => showMessage(JSON.parse(message.body).messageStr));
        checkIsMagic(roomId);
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const sendMsg = () => {
    const roomId = document.getElementById(roomIdElementId).value;
    const message = document.getElementById(messageElementId).value;
    stompClient.send(`/app/message.${roomId}`, {}, JSON.stringify({'messageStr': message}))
}

const showMessage = (message) => {
    const chatLine = document.getElementById(chatLineElementId);
    let newRow = chatLine.insertRow(-1);
    let newCell = newRow.insertCell(0);
    let newText = document.createTextNode(message);
    newCell.appendChild(newText);
}

const checkIsMagic = (roomId) => {
    fetch(`/api/${roomId}/ismagic`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(isMagic => {
            document.getElementById("message").disabled = isMagic;
            document.getElementById("send").disabled = isMagic;
        })
        .catch(error => {
            console.error('Error checking magic:', error);
        });
}
