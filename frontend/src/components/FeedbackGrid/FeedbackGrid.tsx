import FeedbackGridRow from "./FeedbackGridRow";
import type { Feedback } from "../../types/Feedback";

import './FeedbackGrid.css';

export default function FeedbackGrid(props: { guessHistory: (Feedback | null)[] }) {

    const rows = Array.from({ length: 6 }, (_, i) => props.guessHistory[i] ?? null);

    return(
        <div className='feedback-grid-container'>
            <table className='feedback-grid'>
                <thead>
                    <tr>
                        <th className='feedback-grid-header'>
                            <span>Item</span>
                        </th>

                        <th className='feedback-grid-header'>
                            <span>Game</span>
                        </th>

                        <th className='feedback-grid-header'>
                            <span className='full-name'>Purpose</span>
                            <span className='short-name'>Use</span>
                        </th>

                        <th className='feedback-grid-header'>
                            <span className='full-name'>Consumption</span>
                            <span className='short-name'>Ammo</span>
                        </th>

                        <th className='feedback-grid-header'>
                            <span className='full-name'>Acquisition</span>
                            <span className='short-name'>Get</span>
                        </th>

                        <th className='feedback-grid-header'>
                            <span>Range</span>
                        </th>

                        <th className='feedback-grid-header'>
                            <span className='full-name'>Enemy Interact</span>
                            <span className='short-name'>Combat</span>
                        </th>

                        <th className='feedback-grid-header'>
                            <span className='full-name'>Control Mode</span>
                            <span className='short-name'>Ctrl</span>
                        </th>
                    </tr>
                </thead>

                <tbody>
                    {rows.map((feedback, i) => (
                        <FeedbackGridRow key={i} feedback={feedback}/>
                    ))}
                </tbody>
            </table>
        </div>
    )
}