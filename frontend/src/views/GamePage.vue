<template>
  <div class="app" :class="`step-${step}`">
    <section v-if="step === 'START'">
      <h1>Welcome, {{ username }}! Ready to challenge a random opponent?</h1>
      <button class="action-button" @click="handleFindGame">Find a Game</button>
    </section>

    <section v-else-if="step === 'WAITING'" class="status-display">
      <span>Waiting for an opponent...</span>
    </section>

    <section v-else-if="step === 'GAME'">
      <h1 :class="{'your-turn': myTurn}">{{ myTurn ? 'Your move...' : 'Waiting for opponent...' }}</h1>
      <Board :disabled="!myTurn" :board="board" @cell-click="handleCellClick"/>
    </section>

    <section v-else-if="step === 'END'" class="game-over-display">
      <h1>Game Over!</h1>
      <h2>{{ endGameMessage }}</h2>
      <Board :disabled="true" :board="board"/>
      <button class="action-button" @click="handleGameEnd">Play Again</button>
    </section>
  </div>
</template>


<script setup>
import {computed, onUnmounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import Board from '@/components/Board.vue';
import {makeMove, subscribe} from "@/services/GameService.ts";

const route = useRoute();
const username = route.params.username || localStorage.getItem('username');
const eventSource = ref(null);
const step = ref('START');
const gameId = ref(undefined);
const currentPlayer = ref(undefined);
const board = ref(Array(3).fill().map(() => Array(3).fill('EMPTY')));
const winner = ref(undefined);

const myTurn = computed(() => currentPlayer.value === username);

const handleGameEvent = (event) => {
  if (event.event === 'GAME_STARTED') {
    step.value = 'GAME';
    gameId.value = event.gameId;
  } else if (event.event === 'GAME_UPDATED') {
    console.log('Game updated');
  } else if (event.event === 'GAME_ENDED') {
    console.log('Game ended');
    step.value = 'END';
    winner.value = event.winner;
  }
  currentPlayer.value = event.currentPlayer;
  board.value = event.board;
};

const handleFindGame = () => {
  eventSource.value = subscribe(handleGameEvent);
  step.value = 'WAITING';
};

const handleGameEnd = () => {
  step.value = 'START';
  winner.value = undefined;
  gameId.value = undefined;
  board.value = Array(3).fill().map(() => Array(3).fill('EMPTY'));
  if (eventSource.value) {
    eventSource.value.close();
    eventSource.value = null;
  }
};

const handleCellClick = (x, y) => {
  console.log(`Making move: x: ${x}, y: ${y}`);
  if (gameId.value) {
    makeMove(gameId.value, {x, y, playerName: username})
        .then(() => console.log("Move made"))
        .catch((error) => console.log("Error making move:", error));
  }
};

onUnmounted(() => {
  if (eventSource.value) {
    eventSource.value.close();
  }
});

const endGameMessage = computed(() => {
  if (!winner.value) return "It's a draw";
  return winner.value === username ? "You won!" : "Your opponent won!";
});
</script>

<style scoped>
.app {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: #f5f5f5; /* Light grey background for a soft, minimal look */
  color: #333; /* Dark grey text for readability */
  font-family: 'Roboto', sans-serif;
  padding: 20px;
  text-align: center;
}

h1, h2 {
  color: #333; /* Consistent text color */
}

.action-button {
  background-color: #ffffff; /* White buttons for a clean, subtle look */
  border: 2px solid #dddddd; /* Slightly visible border to define the button edges */
  color: #333; /* Matching the text color for consistency */
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 8px; /* Rounded corners but not too pronounced */
  cursor: pointer;
  margin-top: 20px;
}

.action-button:hover, .action-button:focus {
  background-color: #e0e0e0;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.status-display {
  font-size: 24px;
  margin-top: 20px;
}

.game-over-display {
  margin-top: 20px;
}

</style>



