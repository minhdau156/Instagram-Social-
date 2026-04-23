import { createTheme, alpha } from '@mui/material/styles';

const brandPurple = '#8B5CF6';
const brandPink = '#EC4899';

const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: brandPurple,
      light: '#A78BFA',
      dark: '#6D28D9',
    },
    secondary: {
      main: brandPink,
      light: '#F472B6',
      dark: '#BE185D',
    },
    background: {
      default: '#0F0F14',
      paper: '#1A1A24',
    },
    text: {
      primary: '#F3F4F6',
      secondary: '#9CA3AF',
    },
    divider: alpha('#ffffff', 0.08),
    error: { main: '#F87171' },
    success: { main: '#34D399' },
    info: { main: '#60A5FA' },
    warning: { main: '#FBBF24' },
  },
  typography: {
    fontFamily: '"Inter", -apple-system, BlinkMacSystemFont, sans-serif',
    h1: { fontWeight: 700, letterSpacing: '-0.02em' },
    h2: { fontWeight: 700, letterSpacing: '-0.02em' },
    h3: { fontWeight: 600, letterSpacing: '-0.01em' },
    h4: { fontWeight: 600, letterSpacing: '-0.01em' },
    h5: { fontWeight: 600 },
    h6: { fontWeight: 600 },
    subtitle1: { fontWeight: 500 },
    button: { fontWeight: 600, letterSpacing: '0.02em' },
  },
  shape: { borderRadius: 12 },
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          scrollbarWidth: 'thin',
          scrollbarColor: `${alpha(brandPurple, 0.4)} transparent`,
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          borderRadius: 10,
          padding: '8px 20px',
          transition: 'all 0.2s ease',
        },
        containedPrimary: {
          background: `linear-gradient(135deg, ${brandPurple} 0%, ${brandPink} 100%)`,
          boxShadow: `0 4px 20px ${alpha(brandPurple, 0.4)}`,
          '&:hover': {
            boxShadow: `0 6px 28px ${alpha(brandPurple, 0.6)}`,
            transform: 'translateY(-1px)',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          backgroundImage: 'none',
          backgroundColor: '#1A1A24',
          border: `1px solid ${alpha('#ffffff', 0.06)}`,
          transition: 'box-shadow 0.2s ease, transform 0.2s ease',
          '&:hover': {
            boxShadow: `0 8px 32px ${alpha(brandPurple, 0.25)}`,
            transform: 'translateY(-2px)',
          },
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: { borderRadius: 8, fontWeight: 500 },
      },
    },
    MuiTextField: {
      defaultProps: { variant: 'outlined' },
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 10,
            '&:hover .MuiOutlinedInput-notchedOutline': {
              borderColor: alpha(brandPurple, 0.6),
            },
          },
        },
      },
    },
    MuiDialog: {
      styleOverrides: {
        paper: {
          backgroundImage: 'none',
          backgroundColor: '#1E1E2E',
          border: `1px solid ${alpha('#ffffff', 0.1)}`,
          borderRadius: 16,
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundImage: 'none',
          backgroundColor: alpha('#0F0F14', 0.85),
          backdropFilter: 'blur(20px)',
          borderBottom: `1px solid ${alpha('#ffffff', 0.06)}`,
          boxShadow: 'none',
        },
      },
    },
  },
});

export default theme;
