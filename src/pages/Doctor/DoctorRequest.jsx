
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
  Chip,
  OutlinedInput
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { doctorAPI, departmentAPI } from '../../services/api';
import { toast } from 'react-toastify';

const DoctorRequest = () => {
  const navigate = useNavigate();
  
  const [departments, setDepartments] = useState([]);
  const [requestData, setRequestData] = useState({
    specialty: '',
    departmentId: '',
    daysOfWeek: [],
    startTime: '',
    endTime: '',
    fee: '',
    description: ''
  });
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const specialties = [
    'CARDIOLOGY', 'DERMATOLOGY', 'NEUROLOGY', 'ORTHOPEDICS', 
    'PEDIATRICS', 'PSYCHIATRY', 'RADIOLOGY', 'SURGERY'
  ];

  const daysOfWeek = [
    'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 
    'FRIDAY', 'SATURDAY', 'SUNDAY'
  ];

  useEffect(() => {
    fetchDepartments();
  }, []);

  const fetchDepartments = async () => {
    try {
      const response = await departmentAPI.getAllDepartments();
      setDepartments(response.data);
    } catch (error) {
      console.error('Error fetching departments:', error);
    }
  };

  const handleChange = (field, value) => {
    setRequestData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const formData = new FormData();
      Object.keys(requestData).forEach(key => {
        if (key === 'daysOfWeek') {
          requestData[key].forEach(day => formData.append('daysOfWeek', day));
        } else {
          formData.append(key, requestData[key]);
        }
      });
      
      if (file) {
        formData.append('file', file);
      }
      
      await doctorAPI.createDoctorRequest(formData);
      toast.success('Doctor request submitted successfully!');
      navigate('/');
    } catch (error) {
      setError(error.response?.data?.message || 'Error submitting request');
      toast.error('Failed to submit doctor request');
    }
    
    setLoading(false);
  };

  return (
    <Container maxWidth="md">
      <Paper elevation={3} sx={{ p: 4 }}>
        <Typography variant="h4" gutterBottom>
          Become a Doctor
        </Typography>
        
        <Typography variant="body1" sx={{ mb: 3 }}>
          Submit your application to become a verified doctor on our platform.
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth required>
                <InputLabel>Specialty</InputLabel>
                <Select
                  value={requestData.specialty}
                  label="Specialty"
                  onChange={(e) => handleChange('specialty', e.target.value)}
                >
                  {specialties.map((specialty) => (
                    <MenuItem key={specialty} value={specialty}>
                      {specialty.charAt(0) + specialty.slice(1).toLowerCase()}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            
            <Grid item xs={12} md={6}>
              <FormControl fullWidth required>
                <InputLabel>Department</InputLabel>
                <Select
                  value={requestData.departmentId}
                  label="Department"
                  onChange={(e) => handleChange('departmentId', e.target.value)}
                >
                  {departments.map((department) => (
                    <MenuItem key={department.id} value={department.id}>
                      {department.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            
            <Grid item xs={12}>
              <FormControl fullWidth required>
                <InputLabel>Available Days</InputLabel>
                <Select
                  multiple
                  value={requestData.daysOfWeek}
                  onChange={(e) => handleChange('daysOfWeek', e.target.value)}
                  input={<OutlinedInput label="Available Days" />}
                  renderValue={(selected) => (
                    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                      {selected.map((value) => (
                        <Chip key={value} label={value} size="small" />
                      ))}
                    </Box>
                  )}
                >
                  {daysOfWeek.map((day) => (
                    <MenuItem key={day} value={day}>
                      {day}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                type="time"
                label="Start Time"
                value={requestData.startTime}
                onChange={(e) => handleChange('startTime', e.target.value)}
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
            
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                type="time"
                label="End Time"
                value={requestData.endTime}
                onChange={(e) => handleChange('endTime', e.target.value)}
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
            
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                type="number"
                label="Consultation Fee"
                value={requestData.fee}
                onChange={(e) => handleChange('fee', e.target.value)}
                inputProps={{ min: 100000 }}
              />
            </Grid>
            
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                type="file"
                label="Profile Image"
                onChange={handleFileChange}
                InputLabelProps={{ shrink: true }}
                inputProps={{ accept: 'image/*' }}
              />
            </Grid>
            
            <Grid item xs={12}>
              <TextField
                fullWidth
                required
                multiline
                rows={4}
                label="Description"
                value={requestData.description}
                onChange={(e) => handleChange('description', e.target.value)}
                placeholder="Describe your experience, qualifications, and expertise"
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
              {loading ? 'Submitting...' : 'Submit Application'}
            </Button>
            <Button
              variant="outlined"
              size="large"
              onClick={() => navigate('/')}
            >
              Cancel
            </Button>
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default DoctorRequest;
