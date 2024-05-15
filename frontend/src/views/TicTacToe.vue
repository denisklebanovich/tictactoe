<template>
  <div class="game-content">
    <div class="card">
      <div class="game-info">
        <div class="player">Player 1: <span class="text-red">{{ game.player1 || 'Waiting for player 1...' }}</span>
        </div>
        <div class="player">Player 2: <span
            class="text-red">{{ game.player2 || (game.winner ? '-' : 'Waiting for player 2...') }}</span></div>
        <div class="turn">Turn: <span class="text-red">{{ game.turn }}</span></div>
        <div class="winner" v-if="game.winner">Winner: {{ game.winner }}</div>
      </div>
      <div class="board">
        <div v-for="(row, rowIndex) in game.board" :key="rowIndex" :class="'row row-' + rowIndex">
          <div v-for="(cell, cellIndex) in row" :key="cellIndex" :class="'cell cell-' + cellIndex">
            <button v-if="cell === ' '" @click="makeMove(rowIndex * 3 + cellIndex)"></button>
            <span v-else>{{ cell }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="join-game-btn" v-if="showJoinButton" @click="joinGame">Join Game</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import toastr from 'toastr';

const game = ref({
  gameId: '',
  board: Array(3).fill().map(() => Array(3).fill(' ')), // Proper initialization of nested arrays
  turn: '',
  player1: '',
  player2: '',
  gameState: '',
  winner: '',
  type: '',
});

const stompClient = ref(null);
const player = ref(null);
const showJoinButton = ref(true);
const connected = ref(false);

const updateGame = (message) => {
  Object.assign(game.value, message);
};

const makeMove = (index) => {
  if (game.value.gameState === 'gameOver') {
    toastr.warning('Game is over!');
    return;
  }
  sendMessage({
    type: 'game.move',
    move: index,
    sender: player.value,
    gameId: game.value.gameId
  });
};

const joinGame = async () => {
  if (!connected.value) {
    await connect();
  }
  player.value = jwtDecode(localStorage.getItem('token')).username;
  sendMessage({
    type: 'game.join',
    player: player.value
  });
};

const handleMessage = (message) => {
  switch (message.type) {
    case 'game.joined':
    case 'game.move':
    case 'game.left':
      updateGame(message);
      showJoinButton.value = message.type === 'game.left';
      break;
    case 'game.gameOver':
      updateGame(message);
      toastr[message.gameState === 'TIE' ? 'info' : 'success'](`Game Over! ${message.winner ? 'Winner: ' + message.winner : "It's a draw!"}`);
      showJoinButton.value = true;
      break;
    case 'error':
      toastr.error(message.content);
      break;
    default:
      console.warn('Unknown message type:', message.type);
  }
};

const connect = async () => {
  const token = localStorage.getItem('token');
  const socket = new SockJS('/api/ws');
  stompClient.value = Stomp.over(socket);
  stompClient.value.connect({ token }, () => {
    console.log('Connected to the server');
    connected.value = true;
    stompClient.value.subscribe('/topic/game.state', (m) => {
      handleMessage(JSON.parse(m.body));
    });
  }, (error) => {
    console.error('Failed to connect:', error);
    connected.value = false;
  });
};

onMounted(connect);
</script>


<style scoped lang="scss">
.game-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #282c34;
}

.card {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 500px;
  height: 500px;

  .game-info {
    margin-bottom: 20px;
    display: flex;
    flex-direction: column;
    font-size: 20px;

    .player {
      margin-bottom: 10px;
    }

    .turn {
      margin-bottom: 10px;
    }

    .winner {
      margin-top: 10px;
      font-size: 24px;
      font-weight: bold;
      color: #4caf50;
    }
  }

  .board {
    display: flex;
    flex-direction: column;


    .row {
      display: flex;
      justify-content: center;
    }

    .cell {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 80px;
      height: 80px;
      border: 1px solid #000;


      button {
        width: 100%;
        height: 100%;
        border: none;
        background-color: #fff;
        font-size: 20px;
        cursor: pointer;

        &:hover {
          background-color: #f0f0f0;
        }
      }
    }
  }
}

.join-game-btn {
  padding: 10px 20px;
  border: none;
  background-color: #4d8cbf;
  color: #fff;
  font-size: 16px;
  cursor: pointer;
  border-radius: 5px;

  &:hover {
    background-color: #3c7aa0;
  }
}
</style>
