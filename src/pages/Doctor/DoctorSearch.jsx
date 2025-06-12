
import React, { useState, useEffect } from 'react';
import {
  Container,
  Grid,
  Card,
  CardContent,
  CardActions,
  Typography,
  Button,
  TextField,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Avatar,
  Chip,
  Pagination
} from '@mui/material';
import { Link } from 'react-router-dom';
import { doctorAPI, departmentAPI } from '../../services/api';

const DoctorSearch = () => {
  const [doctors, setDoctors] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [filters, setFilters] = useState({
    name: '',
    specialty: '',
    page: 0
  });
  const [loading, setLoading] = useState(false);
  const [totalPages, setTotalPages] = useState(0);

  const specialties = [
    'CARDIOLOGY', 'DERMATOLOGY', 'NEUROLOGY', 'ORTHOPEDICS', 
    'PEDIATRICS', 'PSYCHIATRY', 'RADIOLOGY', 'SURGERY'
  ];

  useEffect(() => {
    fetchDepartments();
    searchDoctors();
  }, [filters]);

  const fetchDepartments = async () => {
    try {
      const response = await departmentAPI.getAllDepartments();
      setDepartments(response.data);
    } catch (error) {
      console.error('Error fetching departments:', error);
    }
  };

  const searchDoctors = async () => {
    setLoading(true);
    try {
      const response = await doctorAPI.searchDoctors(filters);
      setDoctors(response.data);
      // You might need to handle pagination differently based on your backend response
      setTotalPages(Math.ceil(response.data.length / 10));
    } catch (error) {
      console.error('Error searching doctors:', error);
    }
    setLoading(false);
  };

  const handleFilterChange = (field, value) => {
    setFilters(prev => ({
      ...prev,
      [field]: value,
      page: field !== 'page' ? 0 : value
    }));
  };

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" gutterBottom>
        Find a Doctor
      </Typography>
      
      {/* Search Filters */}
      <Box sx={{ mb: 4 }}>
        <Grid container spacing={2}>
          <Grid item xs={12} md={4}>
            <TextField
              fullWidth
              label="Doctor Name"
              value={filters.name}
              onChange={(e) => handleFilterChange('name', e.target.value)}
              placeholder="Search by doctor name"
            />
          </Grid>
          <Grid item xs={12} md={4}>
            <FormControl fullWidth>
              <InputLabel>Specialty</InputLabel>
              <Select
                value={filters.specialty}
                label="Specialty"
                onChange={(e) => handleFilterChange('specialty', e.target.value)}
              >
                <MenuItem value="">All Specialties</MenuItem>
                {specialties.map((specialty) => (
                  <MenuItem key={specialty} value={specialty}>
                    {specialty.charAt(0) + specialty.slice(1).toLowerCase()}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} md={4}>
            <Button
              variant="contained"
              fullWidth
              onClick={searchDoctors}
              sx={{ height: '56px' }}
            >
              Search
            </Button>
          </Grid>
        </Grid>
      </Box>

      {/* Doctor Results */}
      {loading ? (
        <Typography>Loading...</Typography>
      ) : (
        <Grid container spacing={3}>
          {doctors.map((doctor) => (
            <Grid item xs={12} md={6} lg={4} key={doctor.id}>
              <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                <CardContent sx={{ flexGrow: 1 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Avatar
                      src={doctor.imageUrl}
                      sx={{ width: 60, height: 60, mr: 2 }}
                    >
                      {doctor.fullName?.charAt(0)}
                    </Avatar>
                    <Box>
                      <Typography variant="h6" component="h2">
                        Dr. {doctor.fullName}
                      </Typography>
                      <Chip 
                        label={doctor.specialty} 
                        size="small" 
                        color="primary" 
                      />
                    </Box>
                  </Box>
                  
                  <Typography variant="body2" color="text.secondary" gutterBottom>
                    {doctor.department?.name}
                  </Typography>
                  
                  <Typography variant="body2" sx={{ mb: 2 }}>
                    {doctor.description || 'Experienced medical professional'}
                  </Typography>
                  
                  <Typography variant="h6" color="primary">
                    ${doctor.fee}
                  </Typography>
                </CardContent>
                
                <CardActions>
                  <Button 
                    size="small" 
                    component={Link} 
                    to={`/doctor/${doctor.id}`}
                  >
                    View Profile
                  </Button>
                  <Button 
                    size="small" 
                    variant="contained"
                    component={Link} 
                    to={`/book-appointment/${doctor.id}`}
                  >
                    Book Appointment
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}

      {/* Pagination */}
      {totalPages > 1 && (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
          <Pagination
            count={totalPages}
            page={filters.page + 1}
            onChange={(e, page) => handleFilterChange('page', page - 1)}
            color="primary"
          />
        </Box>
      )}
    </Container>
  );
};

export default DoctorSearch;
