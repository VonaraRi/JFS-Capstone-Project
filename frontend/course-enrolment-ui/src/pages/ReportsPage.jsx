import { useEffect, useState } from 'react';
import ErrorMessage from '../components/ErrorMessage.jsx';
import LoadingMessage from '../components/LoadingMessage.jsx';
import ReportCard from '../components/ReportCard.jsx';
import EnrolmentsPerCourseCard from '../components/EnrolmentsPerCourseCard.jsx';
import PopularCoursesCard from '../components/PopularCoursesCard.jsx';
import EnrolmentsOverviewTable from '../components/EnrolmentsOverviewTable.jsx';
import { useAuth } from '../context/AuthContext.jsx';
import { fetchCourseReports } from '../services/api.js';
import MonthlyEnrolmentTable from '../components/MonthlyEnrolmentTable.jsx';

export default function ReportsPage() {
  const { token } = useAuth();
  const [reports, setReports] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let ignore = false;

    async function loadReports() {
      try {
        setLoading(true);
        setError('');
        const data = await fetchCourseReports(token);

        if (!ignore) {
          setReports(data);
        }
      } catch (err) {
        if (!ignore) {
          setError(err.message || 'Could not load protected reports.');
          console.error(err);
        }
      } finally {
        if (!ignore) {
          setLoading(false);
        }
      }
    }

    loadReports();

    return () => {
      ignore = true;
    };
  }, [token]);

  if (loading) {
    return <LoadingMessage message="Loading protected reports..." />;
  }

  if (error) {
    return <ErrorMessage message={error} />;
  }

  return (
    <section className="report-grid">
      <ReportCard title="Courses by Status" items={reports?.byStatus || []} />
      <ReportCard title="Courses by Level" items={reports?.byLevel || []} />
      <ReportCard title="Courses by Category" items={reports?.byCategory || []} />
      <ReportCard title="Courses by Capacity" items={reports?.byCapacity || []} />

      <EnrolmentsPerCourseCard title="Enrolments per Course" items={reports?.enrolmentsPerCourse || []} />
      <PopularCoursesCard title="Most Popular Courses" items={reports?.popularCourses || []} />
      <EnrolmentsOverviewTable overview={reports?.overview} />
      <MonthlyEnrolmentTable title="Monthly Enrolment Totals" items={reports?.monthlyEnrolments || []} />
    </section>
  );
}