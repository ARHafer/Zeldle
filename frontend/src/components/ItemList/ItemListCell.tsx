import type { Item } from '../../types/Item';

export default function ItemListCell(props: { item: Item; guessedIds: number[]; isLoading: boolean; onCellClick: (itemId: number) => void  }) {

    const isGuessed = props.guessedIds.includes(props.item.id);

    return (
        <button style={{ backgroundColor: isGuessed ? 'gray' : 'white' }}
        onClick={() => props.onCellClick(props.item.id)}
        disabled={props.isLoading || isGuessed}>
            {props.item.name}</button>
    )
}