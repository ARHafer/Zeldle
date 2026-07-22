import type { GuessData } from "../../types/GuessData";
import FeedbackGridRow from "./FeedbackGridRow";

export default function FeedbackGrid(props: { guessHistory: (GuessData | null)[] }) {

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
                {rows.map((guessData, i) => (
                    <FeedbackGridRow key={i} guessData={guessData}/>
                ))}
            </tbody>
        </table>
    )
}