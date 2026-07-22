import type { Result } from "../../types/Feedback";
import type { GuessData } from "../../types/GuessData"; 

export default function FeedbackGridRow(props: { guessData: (GuessData | null) }) {

    if (props.guessData == null) {
        return (
            <tr>
                <td style={{ backgroundColor: 'gray' }}></td>
                <td style={{ backgroundColor: 'gray' }}></td>
                <td style={{ backgroundColor: 'gray' }}></td>
                <td style={{ backgroundColor: 'gray' }}></td>
                <td style={{ backgroundColor: 'gray' }}></td>
                <td style={{ backgroundColor: 'gray' }}></td>
                <td style={{ backgroundColor: 'gray' }}></td>
                <td style={{ backgroundColor: 'gray' }}></td>
            </tr>
        );
    }

    const { feedback, properties } = props.guessData;

    return (
        <tr>
            <td style={{ backgroundColor: getResultColor(feedback.name) }}>{properties.name}</td>
            <td style={{ backgroundColor: getResultColor(feedback.game) }}>{properties.game} {getReleaseOrderArrow(feedback.gameReleaseDate)}</td>
            <td style={{ backgroundColor: getResultColor(feedback.purpose) }}>{properties.purpose}</td>
            <td style={{ backgroundColor: getResultColor(feedback.consumption) }}>{properties.consumption}</td>
            <td style={{ backgroundColor: getResultColor(feedback.acquisition) }}>{properties.acquisition}</td>
            <td style={{ backgroundColor: getResultColor(feedback.range) }}>{properties.range}</td>
            <td style={{ backgroundColor: getResultColor(feedback.enemyInteraction) }}>{properties.enemyInteraction}</td>
            <td style={{ backgroundColor: getResultColor(feedback.controlMode) }}>{properties.controlMode}</td>
        </tr>
    )
}

function getResultColor(result: Result): string {
    if (result == 'CORRECT') {
        return 'green';
    } else {
        return 'red';
    }
}

function getReleaseOrderArrow(result: Result): string {
    if (result == 'HIGHER') {
        return '🠗';
    } else if (result == 'LOWER') {
        return '🠕';
    } else {
        return '';
    }
}