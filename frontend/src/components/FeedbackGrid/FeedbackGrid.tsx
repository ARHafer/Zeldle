import FeedbackGridRow from "./FeedbackGridRow";
import type { Feedback } from "../../types/Feedback";

import './FeedbackGrid.css';

export default function FeedbackGrid(props: { guessHistory: (Feedback | null)[] }) {

    const rows = Array.from({ length: 6 }, (_, i) => props.guessHistory[i] ?? null);

    return(
        <div className='feedback-grid-wrapper'>

            <div className='feedback-grid-container'>

                <table className='feedback-grid'>

                    <thead>

                        <tr>
                            <th className='feedback-grid-header'> <span>Item</span> </th>
                            <th className='feedback-grid-header'> <span>Game</span> </th>
                            <th className='feedback-grid-header'> <span>Purpose</span> </th>
                            <th className='feedback-grid-header'> <span>Consumption</span> </th>
                            <th className='feedback-grid-header'> <span>Acquisition</span> </th>
                            <th className='feedback-grid-header'> <span>Range</span> </th>
                            <th className='feedback-grid-header'> <span>Combat</span> </th>
                            <th className='feedback-grid-header'> <span>Control Mode</span> </th>
                        </tr>

                    </thead>

                    <tbody>
                        {rows.map((feedback, i) => (
                            <FeedbackGridRow key={i} feedback={feedback}/>
                        ))}
                    </tbody>
                    
                </table>

            </div>

        </div>
    )
}