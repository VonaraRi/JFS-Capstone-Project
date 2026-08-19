export default function PaginationControls({ pageInfo, loading, onPageChange }) {
  // Defensive fallbacks to prevent undefined property errors
  const currentPage = pageInfo?.page ?? 0;
  const totalPages = pageInfo?.totalPages ?? 0;
  const totalElements = pageInfo?.totalElements ?? 0;

  const isFirstPage = currentPage <= 0;
  const isLastPage = totalPages === 0 || currentPage >= totalPages - 1;

  return (
    <section className="card welcome-card">
      <div>
        <p className="eyebrow">Pagination</p>
        <h2>Page {totalPages === 0 ? 0 : currentPage + 1} of {totalPages}</h2>
        <p>{totalElements} total course record(s) available from the backend.</p>
      </div>
      <div className="action-row">
        <button
          className="button-link secondary"
          type="button"
          disabled={loading || isFirstPage}
          onClick={() => onPageChange(currentPage - 1)}
        >
          Previous
        </button>
        <button
          className="button-link"
          type="button"
          disabled={loading || isLastPage}
          onClick={() => onPageChange(currentPage + 1)}
        >
          Next
        </button>
      </div>
    </section>
  );
}