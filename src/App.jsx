
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { AuthProvider } from './contexts/AuthContext';
import Layout from './components/Layout/Layout';
import Home from './pages/Home/Home';
import Login from './pages/Auth/Login';
import Register from './pages/Auth/Register';
import DoctorSearch from './pages/Doctor/DoctorSearch';
import DoctorProfile from './pages/Doctor/DoctorProfile';
import BookAppointment from './pages/Appointment/BookAppointment';
import MyAppointments from './pages/Appointment/MyAppointments';
import DoctorDashboard from './pages/Doctor/DoctorDashboard';
import DoctorRequest from './pages/Doctor/DoctorRequest';
import AdminDashboard from './pages/Admin/AdminDashboard';
import PatientProfile from './pages/Patient/PatientProfile';
import PatientList from './pages/Patient/PatientList';
import ForgotPassword from './pages/Auth/ForgotPassword';
import SubmitReview from './pages/Review/SubmitReview';
import DepartmentManagement from './pages/Department/DepartmentManagement';
import ChatbotPage from './pages/Chatbot/ChatbotPage';
import ProtectedRoute from './components/ProtectedRoute';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <Router>
          <Layout>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/forgot-password" element={<ForgotPassword />} />
              <Route path="/doctors" element={<DoctorSearch />} />
              <Route path="/chatbot" element={<ChatbotPage />} />
              <Route path="/doctor/:id" element={<DoctorProfile />} />
              <Route 
                path="/book-appointment/:doctorId" 
                element={
                  <ProtectedRoute>
                    <BookAppointment />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/my-appointments" 
                element={
                  <ProtectedRoute>
                    <MyAppointments />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/doctor-dashboard" 
                element={
                  <ProtectedRoute requiredRole="DOCTOR">
                    <DoctorDashboard />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/become-doctor" 
                element={
                  <ProtectedRoute>
                    <DoctorRequest />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/admin" 
                element={
                  <ProtectedRoute requiredRole="ADMIN">
                    <AdminDashboard />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/profile" 
                element={
                  <ProtectedRoute>
                    <PatientProfile />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/submit-review/:doctorId" 
                element={
                  <ProtectedRoute>
                    <SubmitReview />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/patients" 
                element={
                  <ProtectedRoute requiredRole="ADMIN">
                    <PatientList />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/departments" 
                element={
                  <ProtectedRoute requiredRole="ADMIN">
                    <DepartmentManagement />
                  </ProtectedRoute>
                } 
              />
            </Routes>
          </Layout>
        </Router>
        <ToastContainer position="top-right" autoClose={3000} />
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
