export default function LevelBadge({ level }) {
    return <span className={`level-badge level-${level.toLowerCase()}`}>{level}</span>;
}