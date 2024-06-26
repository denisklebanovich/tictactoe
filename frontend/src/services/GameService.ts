import {EventSourcePolyfill} from 'ng-event-source';
import {refreshToken} from "./AuthService";
import axios from "axios";

const API_URL = '/api';

export type GameStatus = "GAME_STARTED" | "GAME_UPDATED" | "GAME_ENDED"

export type Cell = "EMPTY" | "X" | "O"

export interface GameEvent {
    event: GameStatus;
    board: Cell[][];
    winner: string;
    currentPlayer: string;
    gameId: number;
}

interface EventSourceErrorEvent extends Event {
    errorCode: number;
    errorMessage: string;
}


export const subscribe = (onEvent: (event: GameEvent) => void, retries = 0) => {
    const eventSource = new EventSourcePolyfill(`${API_URL}/game/subscribe`, {
        headers: {
            Authorization: `Bearer ${getAuthToken()}`,
            Accept: '*'
        }
    });

    eventSource.onmessage = (event) => {
        const data = JSON.parse(event.data);
        onEvent(data);
    }

    eventSource.onerror = (error: EventSourceErrorEvent) => {
        if (error.errorCode === 401 && retries < 3) { // Limit to 3 retries
            refreshToken()
                .then(() => {
                    eventSource.close();
                    setTimeout(() => subscribe(onEvent, retries + 1), 1000 * retries);
                })
                .catch((error) => {
                    console.error('Failed to refresh token:', error);
                });
        } else {
            console.error('Non-retryable error or max retries reached', error);
            eventSource.close();
        }
    }
    return eventSource;
}


interface MoveRequest {
    x: number;
    y: number;
    playerName: string;
}

//@ts-ignore
export async function makeMove(gameId: number, move: MoveRequest): Promise<void> {
    const token = getAuthToken();
    console.log("token", token);
    const response = await fetch(`${API_URL}/game/move/${gameId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(move),
    });

    if (!response.ok) {
        if (response.status === 401) {
            try {
                await refreshToken();
                return makeMove(gameId, move);
            } catch (error) {
                throw new Error('Failed to refresh token');
            }
        } else {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
    }
}

export type GameResult = {
    gameId: number;
    winner: string;
    firstPlayer: string;
    secondPlayer: string;
    status: string;
}

//@ts-ignore
export async function getResults(): Promise<GameResult[]> {
    const token = getAuthToken();
    const response = await axios.get(`${API_URL}/game/results`, {
        headers: {
            'Authorization': `Bearer ${token}`,
        }
    });
    return response.data;
}


function getAuthToken() {
    return localStorage.getItem('accessToken');
}