import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box, Container } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import LocalHospitalIcon from '@mui/icons-material/LocalHospital';

const Layout = ({ children }) => {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <LocalHospitalIcon sx={{ mr: 2 }} />
          <Typography 
            variant="h6" 
            component={Link} 
            to="/" 
            sx={{ 
              flexGrow: 1, 
              textDecoration: 'none', 
              color: 'inherit' 
            }}
          >
            MediSched
          </Typography>

          <Button color="inherit" component={Link} to="/doctors">
              Find Doctors
            </Button>
            <Button color="inherit" component={Link} to="/chatbot">
              Medical Assistant
            </Button>

          {isAuthenticated ? (
            <>
              <Button color="inherit" component={Link} to="/my-appointments">
                My Appointments
              </Button>
              <Button color="inherit" component={Link} to="/profile">
                Profile
              </Button>
              {user?.role === 'DOCTOR' && (
                <Button color="inherit" component={Link} to="/doctor-dashboard">
                  Dashboard
                </Button>
              )}
              {user?.role === 'ADMIN' && (
                <>
                  <Button color="inherit" component={Link} to="/admin">
                    Admin Dashboard
                  </Button>
                  <Button color="inherit" component={Link} to="/patients">
                    Patients
                  </Button>
                  <Button color="inherit" component={Link} to="/departments">
                    Departments
                  </Button>
                </>
              )}
              <Button color="inherit" onClick={handleLogout}>
                Logout
              </Button>
            </>
          ) : (
            <>
              <Button color="inherit" component={Link} to="/login">
                Login
              </Button>
              <Button color="inherit" component={Link} to="/register">
                Register
              </Button>
            </>
          )}
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        {children}
      </Container>
    </Box>
  );
};

export default Layout;