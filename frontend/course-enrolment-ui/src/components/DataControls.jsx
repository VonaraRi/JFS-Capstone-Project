export default function DataControls({
  pageInfo,
  cacheMessage,
  loading,
  onRefresh,
  onPageSizeChange,
  onSortChange
}) {
  const currentSortBy = pageInfo?.sortBy || 'courseCode';
  const currentDirection = pageInfo?.direction || 'asc';

  return (
    <section className="card">
      <div className="section-heading">
        <p className="eyebrow">Data layer</p>
        <h2>Server pagination and cache controls</h2>
        <p>{cacheMessage}</p>
      </div>

      <div className="action-row">
        <label>
          Page size
          <select
            value={pageInfo?.size || 5}
            onChange={(event) => onPageSizeChange(Number(event.target.value))}
          >
            <option value="3">3</option>
            <option value="5">5</option>
            <option value="10">10</option>
          </select>
        </label>

        <label>
          Sort by
          <select
            value={currentSortBy}
            onChange={(event) => onSortChange(event.target.value, currentDirection)}
          >
            <option value="courseCode">Course Code</option>
            <option value="title">Course Title</option>
            <option value="category">Category</option>
            <option value="level">Level</option>
            <option value="status">Status</option>
            <option value="capacity">Capacity</option>
            <option value="createdAt">Created At</option>
          </select>
        </label>

        <label>
          Direction
          <select
            value={currentDirection}
            onChange={(event) => onSortChange(currentSortBy, event.target.value)}
          >
            <option value="asc">Ascending</option>
            <option value="desc">Descending</option>
          </select>
        </label>
      </div>

      <div className="action-row form-actions">
        <button className="button-link" type="button" onClick={onRefresh} disabled={loading}>
          {loading ? 'Refreshing...' : 'Refresh from backend'}
        </button>
      </div>
    </section>
  );
}