
import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Box,
  Grid,
  Alert,
  Avatar
} from '@mui/material';
import { patientAPI } from '../../services/api';
import { toast } from 'react-toastify';

const PatientProfile = () => {
  const [profile, setProfile] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    address: '',
    dob: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      const response = await patientAPI.getProfile();
      setProfile(response.data);
    } catch (error) {
      console.error('Error fetching profile:', error);
    }
  };

  const handleChange = (field, value) => {
    setProfile(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await patientAPI.updateProfile(profile);
      toast.success('Profile updated successfully!');
    } catch (error) {
      setError(error.response?.data?.message || 'Error updating profile');
      toast.error('Failed to update profile');
    }
    
    setLoading(false);
  };

  return (
    <Container maxWidth="md">
      <Paper elevation={3} sx={{ p: 4 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 4 }}>
          <Avatar sx={{ width: 80, height: 80, mr: 3 }}>
            {profile.firstName?.charAt(0)}
          </Avatar>
          <Box>
            <Typography variant="h4" gutterBottom>
              My Profile
            </Typography>
            <Typography variant="body1" color="text.secondary">
              Manage your personal information
            </Typography>
          </Box>
        </Box>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                label="First Name"
                value={profile.firstName}
                onChange={(e) => handleChange('firstName', e.target.value)}
              />
            </Grid>
            
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                label="Last Name"
                value={profile.lastName}
                onChange={(e) => handleChange('lastName', e.target.value)}
              />
            </Grid>
            
            <Grid item xs={12}>
              <TextField
                fullWidth
                required
                type="email"
                label="Email"
                value={profile.email}
                onChange={(e) => handleChange('email', e.target.value)}
              />
            </Grid>
            
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                label="Phone Number"
                value={profile.phoneNumber}
                onChange={(e) => handleChange('phoneNumber', e.target.value)}
              />
            </Grid>
            
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                type="date"
                label="Date of Birth"
                value={profile.dob}
                onChange={(e) => handleChange('dob', e.target.value)}
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
            
            <Grid item xs={12}>
              <TextField
                fullWidth
                required
                multiline
                rows={3}
                label="Address"
                value={profile.address}
                onChange={(e) => handleChange('address', e.target.value)}
              />
            </Grid>
          </Grid>
          
          <Box sx={{ mt: 3 }}>
            <Button
              type="submit"
              variant="contained"
              size="large"
              disabled={loading}
            >
              {loading ? 'Updating...' : 'Update Profile'}
            </Button>
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default PatientProfile;
