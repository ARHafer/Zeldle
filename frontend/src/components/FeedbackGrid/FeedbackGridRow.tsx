import type { Result } from "../../types/Results";
import type { Feedback } from "../../types/Feedback"; 

export default function FeedbackGridRow(props: { feedback: (Feedback | null) }) {

    if (props.feedback == null) {
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

    const { results, properties } = props.feedback;

    return (
        <tr>
            <td style={{ backgroundColor: getResultColor(results.name) }}>{properties.name}</td>
            <td style={{ backgroundColor: getResultColor(results.game) }}>{properties.game} {getReleaseOrderArrow(results.gameReleaseDate)}</td>
            <td style={{ backgroundColor: getResultColor(results.purpose) }}>{properties.purpose}</td>
            <td style={{ backgroundColor: getResultColor(results.consumption) }}>{properties.consumption}</td>
            <td style={{ backgroundColor: getResultColor(results.acquisition) }}>{properties.acquisition}</td>
            <td style={{ backgroundColor: getResultColor(results.range) }}>{properties.range}</td>
            <td style={{ backgroundColor: getResultColor(results.enemyInteraction) }}>{properties.enemyInteraction}</td>
            <td style={{ backgroundColor: getResultColor(results.controlMode) }}>{properties.controlMode}</td>
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
    if (result == 'TARGET_HIGHER') {
        return '🠕';
    } else if (result == 'TARGET_LOWER') {
        return '🠗';
    } else {
        return '';
    }
}