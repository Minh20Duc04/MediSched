
import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Grid,
  Alert,
  Chip
} from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';
import { appointmentAPI, doctorAPI } from '../../services/api';
import { toast } from 'react-toastify';

const BookAppointment = () => {
  const { doctorId } = useParams();
  const navigate = useNavigate();
  
  const [doctor, setDoctor] = useState(null);
  const [appointmentData, setAppointmentData] = useState({
    appointmentDate: '',
    appointmentTime: '',
    note: '',
    paymentMethod: 'CASH'
  });
  const [availableSlots, setAvailableSlots] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchDoctorDetails();
  }, [doctorId]);

  useEffect(() => {
    if (appointmentData.appointmentDate) {
      fetchAvailableSlots();
    }
  }, [appointmentData.appointmentDate]);

  const fetchDoctorDetails = async () => {
    try {
      const response = await doctorAPI.getDoctorById(doctorId);
      setDoctor(response.data);
    } catch (error) {
      setError('Error fetching doctor details');
    }
  };

  const fetchAvailableSlots = async () => {
    try {
      const response = await appointmentAPI.getAvailableSlots(
        doctorId, 
        appointmentData.appointmentDate
      );
      setAvailableSlots(response.data);
    } catch (error) {
      console.error('Error fetching available slots:', error);
    }
  };

  const handleChange = (field, value) => {
    setAppointmentData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const bookingData = {
        ...appointmentData,
        doctorId: parseInt(doctorId)
      };
      
      await appointmentAPI.bookAppointment(bookingData);
      toast.success('Appointment booked successfully!');
      navigate('/my-appointments');
    } catch (error) {
      setError(error.response?.data?.message || 'Error booking appointment');
      toast.error('Failed to book appointment');
    }
    
    setLoading(false);
  };

  if (!doctor) {
    return <Typography>Loading...</Typography>;
  }

  return (
    <Container maxWidth="md">
      <Paper elevation={3} sx={{ p: 4 }}>
        <Typography variant="h4" gutterBottom>
          Book Appointment
        </Typography>
        
        {/* Doctor Info */}
        <Box sx={{ mb: 4, p: 3, backgroundColor: '#f5f5f5', borderRadius: 2 }}>
          <Typography variant="h6" gutterBottom>
            Dr. {doctor.fullName}
          </Typography>
          <Chip label={doctor.specialty} color="primary" sx={{ mr: 1 }} />
          <Typography variant="body2" sx={{ mt: 1 }}>
            {doctor.department?.name}
          </Typography>
          <Typography variant="h6" color="primary" sx={{ mt: 1 }}>
            Fee: ${doctor.fee}
          </Typography>
        </Box>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                type="date"
                label="Appointment Date"
                value={appointmentData.appointmentDate}
                onChange={(e) => handleChange('appointmentDate', e.target.value)}
                InputLabelProps={{ shrink: true }}
                inputProps={{ min: new Date().toISOString().split('T')[0] }}
              />
            </Grid>
            
            <Grid item xs={12} md={6}>
              <FormControl fullWidth required>
                <InputLabel>Appointment Time</InputLabel>
                <Select
                  value={appointmentData.appointmentTime}
                  label="Appointment Time"
                  onChange={(e) => handleChange('appointmentTime', e.target.value)}
                >
                  {availableSlots.map((slot) => (
                    <MenuItem key={slot} value={slot}>
                      {slot}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            
            <Grid item xs={12}>
              <FormControl fullWidth required>
                <InputLabel>Payment Method</InputLabel>
                <Select
                  value={appointmentData.paymentMethod}
                  label="Payment Method"
                  onChange={(e) => handleChange('paymentMethod', e.target.value)}
                >
                  <MenuItem value="CASH">Cash</MenuItem>
                  <MenuItem value="ONLINE">Online</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            
            <Grid item xs={12}>
              <TextField
                fullWidth
                multiline
                rows={4}
                label="Note (Optional)"
                value={appointmentData.note}
                onChange={(e) => handleChange('note', e.target.value)}
                placeholder="Any specific concerns or notes for the doctor"
              />
            </Grid>
          </Grid>
          
          <Box sx={{ mt: 3, display: 'flex', gap: 2 }}>
            <Button
              type="submit"
              variant="contained"
              size="large"
              disabled={loading}
              sx={{ flexGrow: 1 }}
            >
              {loading ? 'Booking...' : 'Book Appointment'}
            </Button>
            <Button
              variant="outlined"
              size="large"
              onClick={() => navigate('/doctors')}
            >
              Cancel
            </Button>
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default BookAppointment;
