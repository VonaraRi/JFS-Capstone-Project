import ErrorMessage from "./ErrorMessage";
import LoadingMessage from "./LoadingMessage";

export default function ApiInfoCard ({loading, error, apiInfo, apiDocs}) {
    return (
        <section className="card api-card">
            <div className="section-heading">
                <h2>Backend connection</h2>
                <p>Fetched using useEffect from the public backend endpoints.</p>
            </div>

            {loading && <LoadingMessage message="Loading API Information..." />}
            {error && <ErrorMessage message={error} />}

            {!loading && !error && apiInfo && (
                <div className="api-info-grid">
                    <InfoItem label="Application" value={apiInfo.application} />
                    <InfoItem label="Version" value={apiInfo.version} />
                    <InfoItem label="Documented Endpoints" value={apiDocs?.endpoints?.length ?? 0} />
                </div>
            )}
        </section>
    );
}

function InfoItem({ label, value}) {
    return (
        <div className="info-item">
            <span>{label}</span>
            <strong>{value}</strong>
        </div>
    );
}