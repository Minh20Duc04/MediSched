
import React from 'react';
import { 
  Box, 
  Typography, 
  Button, 
  Grid, 
  Card, 
  CardContent,
  Container
} from '@mui/material';
import { Link } from 'react-router-dom';
import LocalHospitalIcon from '@mui/icons-material/LocalHospital';
import SearchIcon from '@mui/icons-material/Search';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import PeopleIcon from '@mui/icons-material/People';

const Home = () => {
  const features = [
    {
      icon: <SearchIcon fontSize="large" color="primary" />,
      title: 'Find Doctors',
      description: 'Search for doctors by specialty and location'
    },
    {
      icon: <CalendarTodayIcon fontSize="large" color="primary" />,
      title: 'Book Appointments',
      description: 'Schedule appointments with your preferred doctors'
    },
    {
      icon: <PeopleIcon fontSize="large" color="primary" />,
      title: 'Manage Health',
      description: 'Keep track of your medical appointments and history'
    }
  ];

  return (
    <Container maxWidth="lg">
      {/* Hero Section */}
      <Box 
        sx={{ 
          textAlign: 'center', 
          py: 8,
          background: 'linear-gradient(45deg, #1976d2 30%, #42a5f5 90%)',
          borderRadius: 2,
          color: 'white',
          mb: 6
        }}
      >
        <LocalHospitalIcon sx={{ fontSize: 80, mb: 2 }} />
        <Typography variant="h2" component="h1" gutterBottom>
          Welcome to MediSched
        </Typography>
        <Typography variant="h5" component="p" gutterBottom sx={{ mb: 4 }}>
          Your trusted platform for medical appointment booking
        </Typography>
        <Button 
          variant="contained" 
          size="large" 
          component={Link} 
          to="/doctors"
          sx={{ 
            backgroundColor: 'white', 
            color: 'primary.main',
            '&:hover': {
              backgroundColor: '#f5f5f5'
            }
          }}
        >
          Find a Doctor
        </Button>
      </Box>

      {/* Features Section */}
      <Typography variant="h3" component="h2" textAlign="center" gutterBottom sx={{ mb: 4 }}>
        Why Choose MediSched?
      </Typography>
      
      <Grid container spacing={4} sx={{ mb: 6 }}>
        {features.map((feature, index) => (
          <Grid item xs={12} md={4} key={index}>
            <Card sx={{ height: '100%', textAlign: 'center', p: 2 }}>
              <CardContent>
                <Box sx={{ mb: 2 }}>
                  {feature.icon}
                </Box>
                <Typography variant="h5" component="h3" gutterBottom>
                  {feature.title}
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  {feature.description}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* CTA Section */}
      <Box 
        sx={{ 
          textAlign: 'center', 
          py: 6,
          backgroundColor: '#f5f5f5',
          borderRadius: 2
        }}
      >
        <Typography variant="h4" component="h2" gutterBottom>
          Ready to Get Started?
        </Typography>
        <Typography variant="body1" sx={{ mb: 3 }}>
          Join thousands of patients who trust MediSched for their healthcare needs
        </Typography>
        <Button 
          variant="contained" 
          size="large" 
          component={Link} 
          to="/register"
          sx={{ mr: 2 }}
        >
          Sign Up Now
        </Button>
        <Button 
          variant="outlined" 
          size="large" 
          component={Link} 
          to="/login"
        >
          Login
        </Button>
      </Box>
    </Container>
  );
};

export default Home;
