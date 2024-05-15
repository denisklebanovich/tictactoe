<template>
  <div class="board-container">
    <div v-for="(row, y) in board" :key="y" class="board-row">
      <button
          v-for="(cell, x) in row"
          :key="x"
          class="cell"
          :disabled="disabled || cell !== 'EMPTY'"
          @click="() => handleCellClick(x, y)"
      >
        {{ cell === 'EMPTY' ? '' : cell }}
      </button>
    </div>
  </div>
</template>

<script setup>
import {defineProps} from 'vue';


const props = defineProps({
  board: Array,
  onCellClick: Function,
  disabled: Boolean
});


const handleCellClick = (x, y) => {
  if (props.onCellClick) {
    props.onCellClick(x, y);
  }
};
</script>

<style scoped>
.cell {
  width: 100px; /* Increased size for better visibility */
  height: 100px; /* Uniform size for width and height */
  margin: 10px; /* Larger margin for a cleaner look */
  font-size: 36px; /* Larger font size for better readability */
  background-color: #fff; /* White background for cells */
  color: #333; /* Darker text for better contrast */
  border: 2px solid #ddd; /* Subtle border for each cell */
  display: flex; /* Flexbox for perfect center alignment */
  align-items: center; /* Center alignment vertically */
  justify-content: center; /* Center alignment horizontally */
  cursor: pointer; /* Cursor pointer to indicate clickable */
  transition: background-color 0.3s; /* Smooth transition for hover effect */
}

.cell:hover {
  background-color: #f0f0f0; /* Light grey background on hover for interactive feel */
}

.cell[disabled] {
  cursor: not-allowed; /* Cursor change to indicate non-interactivity */
  background-color: #eaeaea; /* Grey background for disabled cells */
  color: #bbb; /* Lighter text color for disabled cells */
}

.board-row {
  display: flex; /* Flex container for rows to hold cells in a row */
  justify-content: center; /* Center the rows in the middle of the board */
}

/* Additional styling for the overall board container for better alignment and presentation */
.board-container {
  display: flex; /* Using flex to center the board vertically and horizontally */
  flex-direction: column; /* Align children (rows) in a column */
  justify-content: center;
  align-items: center;
  padding: 20px; /* Padding around the board */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Subtle shadow for depth */
  background-color: #f7f7f7; /* Light background for the board area */
  border-radius: 10px; /* Rounded corners for the board */
}
</style>

