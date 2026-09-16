import type { Feedback } from "../../types/Feedback";
import FeedbackGridRow from "./FeedbackGridRow";

export default function FeedbackGrid(props: { guessHistory: (Feedback | null)[] }) {

    const rows = Array.from({ length: 6 }, (_, i) => props.guessHistory[i] ?? null);

    return(
        <table>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Game</th>
                    <th>Purpose</th>
                    <th>Consumption</th>
                    <th>Acquisition</th>
                    <th>Range</th>
                    <th>Enemy Interaction</th>
                    <th>Control Mode</th>
                </tr>
            </thead>

            <tbody>
                {rows.map((feedback, i) => (
                    <FeedbackGridRow key={i} feedback={feedback}/>
                ))}
            </tbody>
        </table>
    )
}