
import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Card,
  CardContent,
  Grid,
  Chip,
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions
} from '@mui/material';
import { appointmentAPI } from '../../services/api';
import { format } from 'date-fns';

const MyAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedAppointment, setSelectedAppointment] = useState(null);

  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    try {
      const response = await appointmentAPI.getMyAppointments();
      setAppointments(response.data);
    } catch (error) {
      console.error('Error fetching appointments:', error);
    }
    setLoading(false);
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'APPROVED': return 'success';
      case 'PENDING': return 'warning';
      case 'REJECTED': return 'error';
      case 'COMPLETED': return 'info';
      default: return 'default';
    }
  };

  const formatDate = (dateString) => {
    try {
      return format(new Date(dateString), 'PPP');
    } catch {
      return dateString;
    }
  };

  if (loading) {
    return <Typography>Loading appointments...</Typography>;
  }

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" gutterBottom>
        My Appointments
      </Typography>

      {appointments.length === 0 ? (
        <Typography variant="body1" sx={{ textAlign: 'center', mt: 4 }}>
          You have no appointments yet.
        </Typography>
      ) : (
        <Grid container spacing={3}>
          {appointments.map((appointment) => (
            <Grid item xs={12} md={6} lg={4} key={appointment.id}>
              <Card>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Dr. {appointment.doctorName}
                  </Typography>
                  
                  <Typography variant="body2" color="text.secondary" gutterBottom>
                    {appointment.specialty}
                  </Typography>
                  
                  <Box sx={{ my: 2 }}>
                    <Typography variant="body2">
                      <strong>Date:</strong> {formatDate(appointment.appointmentDate)}
                    </Typography>
                    <Typography variant="body2">
                      <strong>Time:</strong> {appointment.appointmentTime}
                    </Typography>
                    <Typography variant="body2">
                      <strong>Payment:</strong> {appointment.paymentMethod}
                    </Typography>
                  </Box>
                  
                  <Chip 
                    label={appointment.status}
                    color={getStatusColor(appointment.status)}
                    size="small"
                  />
                  
                  <Box sx={{ mt: 2 }}>
                    <Button 
                      size="small" 
                      onClick={() => setSelectedAppointment(appointment)}
                    >
                      View Details
                    </Button>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}

      {/* Appointment Details Dialog */}
      <Dialog 
        open={!!selectedAppointment} 
        onClose={() => setSelectedAppointment(null)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Appointment Details</DialogTitle>
        <DialogContent>
          {selectedAppointment && (
            <Box>
              <Typography variant="h6" gutterBottom>
                Dr. {selectedAppointment.doctorName}
              </Typography>
              <Typography variant="body2" gutterBottom>
                Specialty: {selectedAppointment.specialty}
              </Typography>
              <Typography variant="body2" gutterBottom>
                Date: {formatDate(selectedAppointment.appointmentDate)}
              </Typography>
              <Typography variant="body2" gutterBottom>
                Time: {selectedAppointment.appointmentTime}
              </Typography>
              <Typography variant="body2" gutterBottom>
                Payment Method: {selectedAppointment.paymentMethod}
              </Typography>
              <Typography variant="body2" gutterBottom>
                Status: {selectedAppointment.status}
              </Typography>
              {selectedAppointment.note && (
                <Typography variant="body2" gutterBottom>
                  Note: {selectedAppointment.note}
                </Typography>
              )}
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setSelectedAppointment(null)}>Close</Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default MyAppointments;
