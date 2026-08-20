import { lazy, Suspense } from 'react';
import { Navigate, Route, Routes } from 'react-router';
import ProtectedRoute from './components/ProtectedRoute.jsx';
import AppShell from './components/AppShell.jsx';
import LoadingMessage from './components/LoadingMessage.jsx';
import DashboardPage from './pages/DashboardPage.jsx';
import LoginPage from './pages/LoginPage.jsx';
import CoursesPage from './pages/CoursesPage.jsx';
import { CourseDataProvider } from './context/CourseDataContext.jsx';

const CourseFormPage = lazy(() => import('./pages/CourseFormPage.jsx'));
const ReportsPage = lazy(() => import('./pages/ReportsPage.jsx'));
const DocsPage = lazy(() => import('./pages/DocsPage.jsx'));

// Student Pages
const StudentDashboardPage = lazy(() => import('./pages/StudentDashboardPage.jsx'));
const StudentProfilePage = lazy(() => import('./pages/StudentProfilePage.jsx'));
const StudentRegisterCoursePage = lazy(() => import('./pages/StudentRegisterCoursePage.jsx'));

const NotFoundPage = lazy(() => import('./pages/NotFoundPage.jsx'));

function withFallback(element) {
  return <Suspense fallback={<LoadingMessage message="Loading page..." />}>{element}</Suspense>;
}

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/app/dashboard" replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/docs" element={withFallback(<DocsPage />)} />

      <Route
        path="/app"
        element={
          <ProtectedRoute>
            <CourseDataProvider>
              <AppShell />
            </CourseDataProvider>
          </ProtectedRoute>
        }
      >
        <Route index element={<Navigate to="dashboard" replace />} />
        <Route path="dashboard" element={<DashboardPage />} />
        
        {/* Student Routes */}
        <Route path="student-dashboard" element={withFallback(<StudentDashboardPage />)} />
        <Route path="student-profile" element={withFallback(<StudentProfilePage />)} />
        <Route path="register-course" element={withFallback(<StudentRegisterCoursePage />)} />

        {/* Admin Routes */}
        <Route path="courses" element={<CoursesPage />} />
        <Route path="courses/new" element={withFallback(<CourseFormPage />)} />
        <Route path="courses/:courseId/edit" element={withFallback(<CourseFormPage />)} />
        <Route path="reports" element={withFallback(<ReportsPage />)} />
      </Route>

      <Route path="*" element={withFallback(<NotFoundPage />)} />
    </Routes>
  );
}