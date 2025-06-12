import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Card,
  CardContent,
  Avatar,
  Chip,
  Box,
  Button,
  Grid,
  Divider
} from '@mui/material';
import { useParams, Link } from 'react-router-dom';
import { doctorAPI, reviewAPI } from '../../services/api';

const DoctorProfile = () => {
  const { id } = useParams();
  const [doctor, setDoctor] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDoctorProfile();
    fetchReviews();
  }, [id]);

  const fetchDoctorProfile = async () => {
    try {
      const response = await doctorAPI.getDoctorById(id);
      setDoctor(response.data);
    } catch (error) {
      console.error('Error fetching doctor profile:', error);
    }
  };

  const fetchReviews = async () => {
    try {
      const response = await reviewAPI.getReviewsByDoctor(id);
      setReviews(response.data);
    } catch (error) {
      console.error('Error fetching reviews:', error);
    }
    setLoading(false);
  };

  if (loading) {
    return <Typography>Loading doctor profile...</Typography>;
  }

  if (!doctor) {
    return <Typography>Doctor not found</Typography>;
  }

  return (
    <Container maxWidth="lg">
      <Grid container spacing={4}>
        {/* Doctor Info */}
        <Grid item xs={12} md={8}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
                <Avatar
                  src={doctor.imageUrl}
                  sx={{ width: 120, height: 120, mr: 3 }}
                >
                  {doctor.fullName?.charAt(0)}
                </Avatar>
                <Box>
                  <Typography variant="h4" gutterBottom>
                    Dr. {doctor.fullName}
                  </Typography>
                  <Chip 
                    label={doctor.specialty} 
                    color="primary" 
                    sx={{ mb: 1 }}
                  />
                  <Typography variant="body1" color="text.secondary">
                    {doctor.department?.name}
                  </Typography>
                  <Typography variant="h6" color="primary" sx={{ mt: 1 }}>
                    Consultation Fee: ${doctor.fee}
                  </Typography>
                </Box>
              </Box>

              <Divider sx={{ my: 2 }} />

              <Typography variant="h6" gutterBottom>
                About
              </Typography>
              <Typography variant="body1" paragraph>
                {doctor.description || 'Experienced medical professional dedicated to providing quality healthcare.'}
              </Typography>

              <Typography variant="h6" gutterBottom>
                Contact Information
              </Typography>
              <Typography variant="body2">
                Email: {doctor.email}
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* Action Panel */}
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Book Appointment
              </Typography>
              <Typography variant="body2" sx={{ mb: 2 }}>
                Schedule a consultation with Dr. {doctor.fullName}
              </Typography>
              <Button
                variant="contained"
                size="large"
                component={Link}
                to={`/book-appointment/${doctor.id}`}
                fullWidth
                sx={{ mb: 2 }}
              >
                Book Appointment
              </Button>
              <Button
                variant="outlined"
                size="large"
                component={Link}
                to={`/submit-review/${doctor.id}`}
                fullWidth
              >
                Write a Review
              </Button>
            </CardContent>
          </Card>

          {/* Reviews */}
          <Card sx={{ mt: 2 }}>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Patient Reviews
              </Typography>
              {reviews.length === 0 ? (
                <Typography variant="body2" color="text.secondary">
                  No reviews yet
                </Typography>
              ) : (
                reviews.slice(0, 3).map((review, index) => (
                  <Box key={index} sx={{ mb: 2 }}>
                    <Typography variant="body2" gutterBottom>
                      "{review.comment}"
                    </Typography>
                    <Typography variant="caption" color="text.secondary">
                      - {review.patientName}
                    </Typography>
                    {index < reviews.length - 1 && <Divider sx={{ mt: 1 }} />}
                  </Box>
                ))
              )}
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Container>
  );
};

export default DoctorProfile;