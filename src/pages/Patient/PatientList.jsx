
import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  Box,
  TextField,
  InputAdornment,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Chip
} from '@mui/material';
import { Search, Edit, Delete, Visibility } from '@mui/icons-material';
import { patientAPI } from '../../services/api';
import { toast } from 'react-toastify';
import { format } from 'date-fns';

const PatientList = () => {
  const [patients, setPatients] = useState([]);
  const [filteredPatients, setFilteredPatients] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [viewDialog, setViewDialog] = useState(false);
  const [editDialog, setEditDialog] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchPatients();
  }, []);

  useEffect(() => {
    const filtered = patients.filter(patient =>
      patient.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      patient.lastName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      patient.email?.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredPatients(filtered);
  }, [searchTerm, patients]);

  const fetchPatients = async () => {
    try {
      const response = await patientAPI.getAllPatients();
      setPatients(response.data);
      setFilteredPatients(response.data);
    } catch (error) {
      console.error('Error fetching patients:', error);
      toast.error('Failed to fetch patients');
    }
    setLoading(false);
  };

  const handleViewPatient = (patient) => {
    setSelectedPatient(patient);
    setViewDialog(true);
  };

  const handleEditPatient = (patient) => {
    setSelectedPatient({ ...patient });
    setEditDialog(true);
  };

  const handleUpdatePatient = async () => {
    try {
      await patientAPI.updateProfile(selectedPatient);
      toast.success('Patient updated successfully!');
      setEditDialog(false);
      fetchPatients();
    } catch (error) {
      toast.error('Failed to update patient');
    }
  };

  const formatDate = (dateString) => {
    try {
      return format(new Date(dateString), 'MMM dd, yyyy');
    } catch {
      return dateString;
    }
  };

  if (loading) {
    return <Typography>Loading patients...</Typography>;
  }

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" gutterBottom>
        Patient Management
      </Typography>

      <Box sx={{ mb: 3 }}>
        <TextField
          fullWidth
          placeholder="Search patients by name or email..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <Search />
              </InputAdornment>
            ),
          }}
        />
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Phone</TableCell>
              <TableCell>Date of Birth</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredPatients.map((patient) => (
              <TableRow key={patient.id}>
                <TableCell>
                  {patient.firstName} {patient.lastName}
                </TableCell>
                <TableCell>{patient.email}</TableCell>
                <TableCell>{patient.phoneNumber}</TableCell>
                <TableCell>{formatDate(patient.dob)}</TableCell>
                <TableCell>
                  <Chip
                    label={patient.status || 'Active'}
                    color="success"
                    size="small"
                  />
                </TableCell>
                <TableCell>
                  <IconButton
                    size="small"
                    onClick={() => handleViewPatient(patient)}
                  >
                    <Visibility />
                  </IconButton>
                  <IconButton
                    size="small"
                    onClick={() => handleEditPatient(patient)}
                  >
                    <Edit />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* View Patient Dialog */}
      <Dialog open={viewDialog} onClose={() => setViewDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Patient Details</DialogTitle>
        <DialogContent>
          {selectedPatient && (
            <Box>
              <Typography variant="h6" gutterBottom>
                {selectedPatient.firstName} {selectedPatient.lastName}
              </Typography>
              <Typography variant="body2" gutterBottom>
                Email: {selectedPatient.email}
              </Typography>
              <Typography variant="body2" gutterBottom>
                Phone: {selectedPatient.phoneNumber}
              </Typography>
              <Typography variant="body2" gutterBottom>
                Date of Birth: {formatDate(selectedPatient.dob)}
              </Typography>
              <Typography variant="body2" gutterBottom>
                Address: {selectedPatient.address}
              </Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setViewDialog(false)}>Close</Button>
        </DialogActions>
      </Dialog>

      {/* Edit Patient Dialog */}
      <Dialog open={editDialog} onClose={() => setEditDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Edit Patient</DialogTitle>
        <DialogContent>
          {selectedPatient && (
            <Box sx={{ mt: 2 }}>
              <TextField
                fullWidth
                margin="normal"
                label="First Name"
                value={selectedPatient.firstName || ''}
                onChange={(e) => setSelectedPatient({
                  ...selectedPatient,
                  firstName: e.target.value
                })}
              />
              <TextField
                fullWidth
                margin="normal"
                label="Last Name"
                value={selectedPatient.lastName || ''}
                onChange={(e) => setSelectedPatient({
                  ...selectedPatient,
                  lastName: e.target.value
                })}
              />
              <TextField
                fullWidth
                margin="normal"
                label="Email"
                value={selectedPatient.email || ''}
                onChange={(e) => setSelectedPatient({
                  ...selectedPatient,
                  email: e.target.value
                })}
              />
              <TextField
                fullWidth
                margin="normal"
                label="Phone"
                value={selectedPatient.phoneNumber || ''}
                onChange={(e) => setSelectedPatient({
                  ...selectedPatient,
                  phoneNumber: e.target.value
                })}
              />
              <TextField
                fullWidth
                margin="normal"
                label="Address"
                multiline
                rows={2}
                value={selectedPatient.address || ''}
                onChange={(e) => setSelectedPatient({
                  ...selectedPatient,
                  address: e.target.value
                })}
              />
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setEditDialog(false)}>Cancel</Button>
          <Button onClick={handleUpdatePatient} variant="contained">
            Update
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default PatientList;
