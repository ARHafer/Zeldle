import { getPropertyImage } from '../../util/Images';
import type { Result } from "../../types/Results";
import type { Feedback } from "../../types/Feedback"; 

import './FeedbackGrid.css';

/* TODO: 
 * Add animation for result reveal.
 * Add images for each property.
 * Add arrow assets.
 */

export default function FeedbackGridRow(props: { feedback: (Feedback | null) }) {

    if (props.feedback == null) {
        return (
            <tr>
                <td className='feedback-grid-cell'></td>
                <td className='feedback-grid-cell'></td>
                <td className='feedback-grid-cell'></td>
                <td className='feedback-grid-cell'></td>
                <td className='feedback-grid-cell'></td>
                <td className='feedback-grid-cell'></td>
                <td className='feedback-grid-cell'></td>
                <td className='feedback-grid-cell'></td>
            </tr>
        );
    }

    const { results, properties } = props.feedback;

    return (
        <tr>
            <td className={getResultStyle(results.name)}>
                <img src={`/item_images/${properties.id}.png`}/>
                <span>{properties.name}</span>
            </td>

            <td className={getResultStyle(results.game)}>
                <img src={getPropertyImage(properties.game)}/>
                <span>{properties.game} {getReleaseOrderArrow(results.gameReleaseDate)}</span>
            </td>

            <td className={getResultStyle(results.purpose)}>
                <img src={getPropertyImage(properties.purpose)}/>
                <span>{properties.purpose}</span>
            </td>

            <td className={getResultStyle(results.consumption)}>
                <img src={getPropertyImage(properties.consumption)}/>
                <span>{properties.consumption}</span>
            </td>

            <td className={getResultStyle(results.acquisition)}>
                <img src={getPropertyImage(properties.acquisition)}/>
                <span>{properties.acquisition}</span>
            </td>

            <td className={getResultStyle(results.range)}>
                <img src={getPropertyImage(properties.range)}/>
                <span>{properties.range}</span>
            </td>

            <td className={getResultStyle(results.enemyInteraction)}>
                <img src={getPropertyImage(properties.enemyInteraction)}/>
                <span>{properties.enemyInteraction}</span>
            </td>

            <td className={getResultStyle(results.controlMode)}>
                <img src={getPropertyImage(properties.controlMode)}/>
                <span>{properties.controlMode}</span>
            </td>
        </tr>
    )
}

function getResultStyle(result: Result): string {
    if (result === 'CORRECT') {
        return 'feedback-grid-cell correct';
    } else {
        return 'feedback-grid-cell incorrect';
    }
}

function getReleaseOrderArrow(result: Result): string {
    if (result === 'TARGET_HIGHER') {
        return '🠕';
    } else if (result === 'TARGET_LOWER') {
        return '🠗';
    } else {
        return '';
    }
}