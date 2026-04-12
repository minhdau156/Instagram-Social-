import { AppBar, Box, Container, Toolbar, Typography, alpha } from '@mui/material';
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome';
import { Link, Outlet } from 'react-router-dom';

export default function AppShell() {
  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'background.default' }}>
      {/* ── Top Navigation Bar ── */}
      <AppBar position="sticky" elevation={0}>
        <Toolbar>
          <Box
            component={Link}
            to="/posts"
            sx={{
              display: 'flex',
              alignItems: 'center',
              gap: 1,
              textDecoration: 'none',
              color: 'inherit',
            }}
          >
            <Box
              sx={{
                width: 32,
                height: 32,
                borderRadius: 2,
                background: 'linear-gradient(135deg, #8B5CF6 0%, #EC4899 100%)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                boxShadow: (t) => `0 4px 12px ${alpha(t.palette.primary.main, 0.5)}`,
              }}
            >
              <AutoAwesomeIcon sx={{ fontSize: 18, color: '#fff' }} />
            </Box>
            <Typography
              variant="h6"
              fontWeight={700}
              sx={{
                background: 'linear-gradient(135deg, #A78BFA 0%, #F472B6 100%)',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
              }}
            >
              SocialMedia
            </Typography>
          </Box>
        </Toolbar>
      </AppBar>

      {/* ── Page Content ── */}
      <Container maxWidth="xl" disableGutters>
        <Outlet />
      </Container>
    </Box>
  );
}
