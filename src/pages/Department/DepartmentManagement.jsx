
import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Card,
  CardContent,
  Grid,
  Button,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  IconButton,
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction
} from '@mui/material';
import { Add, Edit, Delete } from '@mui/icons-material';
import { departmentAPI } from '../../services/api';
import { toast } from 'react-toastify';

const DepartmentManagement = () => {
  const [departments, setDepartments] = useState([]);
  const [createDialog, setCreateDialog] = useState(false);
  const [newDepartment, setNewDepartment] = useState({
    name: '',
    description: ''
  });
  const [loading, setLoading] = useState(false);

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

  const handleCreateDepartment = async () => {
    setLoading(true);
    try {
      await departmentAPI.createDepartment(newDepartment);
      toast.success('Department created successfully!');
      setCreateDialog(false);
      setNewDepartment({ name: '', description: '' });
      fetchDepartments();
    } catch (error) {
      toast.error('Failed to create department');
    }
    setLoading(false);
  };

  return (
    <Container maxWidth="lg">
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Typography variant="h4">
          Department Management
        </Typography>
        <Button
          variant="contained"
          startIcon={<Add />}
          onClick={() => setCreateDialog(true)}
        >
          Add Department
        </Button>
      </Box>

      <Grid container spacing={3}>
        {departments.map((department) => (
          <Grid item xs={12} md={6} lg={4} key={department.id}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  {department.name}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {department.description || 'No description available'}
                </Typography>
                <Box sx={{ mt: 2, display: 'flex', gap: 1 }}>
                  <IconButton size="small">
                    <Edit />
                  </IconButton>
                  <IconButton size="small" color="error">
                    <Delete />
                  </IconButton>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Create Department Dialog */}
      <Dialog open={createDialog} onClose={() => setCreateDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Create New Department</DialogTitle>
        <DialogContent>
          <TextField
            fullWidth
            margin="normal"
            label="Department Name"
            value={newDepartment.name}
            onChange={(e) => setNewDepartment({
              ...newDepartment,
              name: e.target.value
            })}
            required
          />
          <TextField
            fullWidth
            margin="normal"
            label="Description"
            multiline
            rows={3}
            value={newDepartment.description}
            onChange={(e) => setNewDepartment({
              ...newDepartment,
              description: e.target.value
            })}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setCreateDialog(false)}>Cancel</Button>
          <Button
            onClick={handleCreateDepartment}
            variant="contained"
            disabled={loading || !newDepartment.name}
          >
            {loading ? 'Creating...' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default DepartmentManagement;
