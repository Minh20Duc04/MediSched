
import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Card,
  CardContent,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  Box,
  Chip,
  Avatar,
  Grid
} from '@mui/material';
import { adminAPI } from '../../services/api';
import { toast } from 'react-toastify';

const AdminDashboard = () => {
  const [doctorRequests, setDoctorRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDoctorRequests();
  }, []);

  const fetchDoctorRequests = async () => {
    try {
      const response = await adminAPI.getAllDoctorRequests();
      setDoctorRequests(response.data);
    } catch (error) {
      console.error('Error fetching doctor requests:', error);
    }
    setLoading(false);
  };

  const handleApprove = async (requestId) => {
    try {
      await adminAPI.approveDoctorRequest(requestId);
      toast.success('Doctor request approved successfully');
      fetchDoctorRequests();
    } catch (error) {
      toast.error('Error approving request');
    }
  };

  const handleReject = async (requestId) => {
    try {
      await adminAPI.rejectDoctorRequest(requestId);
      toast.success('Doctor request rejected');
      fetchDoctorRequests();
    } catch (error) {
      toast.error('Error rejecting request');
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'APPROVED': return 'success';
      case 'PENDING': return 'warning';
      case 'REJECTED': return 'error';
      default: return 'default';
    }
  };

  if (loading) {
    return <Typography>Loading admin dashboard...</Typography>;
  }

  const pendingRequests = doctorRequests.filter(req => req.status === 'PENDING');
  const approvedRequests = doctorRequests.filter(req => req.status === 'APPROVED');
  const rejectedRequests = doctorRequests.filter(req => req.status === 'REJECTED');

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" gutterBottom>
        Admin Dashboard
      </Typography>

      {/* Stats Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={4}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
                Pending Requests
              </Typography>
              <Typography variant="h4" color="warning.main">
                {pendingRequests.length}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={4}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
                Approved Doctors
              </Typography>
              <Typography variant="h4" color="success.main">
                {approvedRequests.length}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={4}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
                Rejected Requests
              </Typography>
              <Typography variant="h4" color="error.main">
                {rejectedRequests.length}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Doctor Requests Table */}
      <Typography variant="h5" gutterBottom>
        Doctor Requests
      </Typography>
      
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Doctor</TableCell>
              <TableCell>Specialty</TableCell>
              <TableCell>Department</TableCell>
              <TableCell>Fee</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {doctorRequests.map((request) => (
              <TableRow key={request.id}>
                <TableCell>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <Avatar sx={{ mr: 2 }}>
                      {request.fullName?.charAt(0)}
                    </Avatar>
                    <Box>
                      <Typography variant="body2">
                        {request.fullName}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        {request.email}
                      </Typography>
                    </Box>
                  </Box>
                </TableCell>
                <TableCell>{request.specialty}</TableCell>
                <TableCell>{request.departmentName}</TableCell>
                <TableCell>${request.fee}</TableCell>
                <TableCell>
                  <Chip 
                    label={request.status}
                    color={getStatusColor(request.status)}
                    size="small"
                  />
                </TableCell>
                <TableCell>
                  {request.status === 'PENDING' && (
                    <Box sx={{ display: 'flex', gap: 1 }}>
                      <Button
                        size="small"
                        variant="contained"
                        color="success"
                        onClick={() => handleApprove(request.id)}
                      >
                        Approve
                      </Button>
                      <Button
                        size="small"
                        variant="contained"
                        color="error"
                        onClick={() => handleReject(request.id)}
                      >
                        Reject
                      </Button>
                    </Box>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
};

export default AdminDashboard;
