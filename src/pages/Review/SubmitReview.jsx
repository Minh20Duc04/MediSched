
import React, { useState } from 'react';
import {
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Box,
  Rating,
  Alert
} from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';
import { reviewAPI } from '../../services/api';
import { toast } from 'react-toastify';

const SubmitReview = () => {
  const { doctorId } = useParams();
  const navigate = useNavigate();
  
  const [reviewData, setReviewData] = useState({
    rating: 5,
    comment: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await reviewAPI.createReview({
        ...reviewData,
        doctorId: parseInt(doctorId)
      });
      toast.success('Review submitted successfully!');
      navigate(`/doctor/${doctorId}`);
    } catch (error) {
      setError(error.response?.data?.message || 'Error submitting review');
      toast.error('Failed to submit review');
    }
    
    setLoading(false);
  };

  return (
    <Container maxWidth="md">
      <Paper elevation={3} sx={{ p: 4 }}>
        <Typography variant="h4" gutterBottom>
          Submit Review
        </Typography>
        
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit}>
          <Box sx={{ mb: 3 }}>
            <Typography component="legend" gutterBottom>
              Rate your experience
            </Typography>
            <Rating
              size="large"
              value={reviewData.rating}
              onChange={(event, newValue) => {
                setReviewData(prev => ({
                  ...prev,
                  rating: newValue
                }));
              }}
            />
          </Box>
          
          <TextField
            fullWidth
            required
            multiline
            rows={6}
            label="Write your review"
            value={reviewData.comment}
            onChange={(e) => setReviewData(prev => ({
              ...prev,
              comment: e.target.value
            }))}
            placeholder="Share your experience with this doctor..."
            sx={{ mb: 3 }}
          />
          
          <Box sx={{ display: 'flex', gap: 2 }}>
            <Button
              type="submit"
              variant="contained"
              size="large"
              disabled={loading}
              sx={{ flexGrow: 1 }}
            >
              {loading ? 'Submitting...' : 'Submit Review'}
            </Button>
            <Button
              variant="outlined"
              size="large"
              onClick={() => navigate(`/doctor/${doctorId}`)}
            >
              Cancel
            </Button>
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default SubmitReview;
