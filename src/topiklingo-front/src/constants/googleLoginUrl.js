const GOOGLE_LOGIN_URL =
  'https://accounts.google.com/o/oauth2/v2/auth?' +
  `client_id=${import.meta.env.VITE_GOOGLE_CLIENT_ID}&` +
  `redirect_uri=${import.meta.env.VITE_GOOGLE_REDIRECT_URI}&` +
  `response_type=code&` +
  `scope=${import.meta.env.VITE_GOOGLE_SCOPE}`;

export default GOOGLE_LOGIN_URL;
