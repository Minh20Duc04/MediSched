import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Auth API
export const authAPI = {
  setAuthToken: (token) => {
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
      delete api.defaults.headers.common['Authorization'];
    }
  },

  login: (credentials) => api.post('/user/login', credentials),
  register: (userData) => api.post('/user/register', userData),
  forgotPassword: (email) => api.post('/user/forgot-password', { email }),
};

// Doctor API
export const doctorAPI = {
  searchDoctors: (params) => api.get('/doctor/search', { params }),
  getDoctorById: (id) => api.get(`/doctor/${id}`),
  getDoctorProfile: () => api.get('/doctor/profile'),
  updateDoctorProfile: (data) => api.put('/doctor/update', data),
  createDoctorRequest: (formData) => api.post('/doctor-request/create', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
};

// Appointment API
export const appointmentAPI = {
  bookAppointment: (appointmentData) => api.post('/appointment/book', appointmentData),
  getMyAppointments: () => api.get('/appointment/my-appointments'),
  getAvailableSlots: (doctorId, date) => api.get(`/appointment/available-slots`, {
    params: { doctorId, date }
  }),
  updateAppointmentStatus: (id, status) => api.put(`/appointment/${id}/status`, { status }),
  getDoctorAppointments: () => api.get('/appointment/doctor-appointments'),
};

// Patient API
export const patientAPI = {
  getProfile: () => api.get('/patient/profile'),
  updateProfile: (data) => api.put('/patient/update', data),
  getAllPatients: () => api.get('/patient/all'),
};

// Department API
export const departmentAPI = {
  getAllDepartments: () => api.get('/department/get-all'),
};

// Admin API
export const adminAPI = {
  getAllDoctorRequests: () => api.get('/admin/doctor-requests'),
  approveDoctorRequest: (id) => api.put(`/admin/doctor-request/${id}/approve`),
  rejectDoctorRequest: (id) => api.put(`/admin/doctor-request/${id}/reject`),
};

// Review API
export const reviewAPI = {
  getReviewsByDoctor: (doctorId) => api.get(`/review/get-all/${doctorId}`),
  createReview: (reviewData) => api.post('/review/evaluate', reviewData),
};

// Chatbot API
export const chatbotAPI = {
  sendMessage: (message) => api.post('/bot/chat', { message }),
};

// Department API (update with create method)
export const departmentAPI = {
  getAllDepartments: () => api.get('/department/get-all'),
  createDepartment: (data) => api.post('/department/createDepartment', data),
};

export default api;