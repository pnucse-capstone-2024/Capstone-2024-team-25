import AUTH_LIST from '@constants/authList';
import useGetUser from '@hooks/useGetUser';

function NeedAuth({ auth, children }) {
  const user = useGetUser();
  const authIdx = auth ? AUTH_LIST.indexOf(auth) : -1;
  const userAuthIdx = user?.auth ? AUTH_LIST.indexOf(user.auth) : AUTH_LIST.length;

  if (!user || authIdx < userAuthIdx) {
    return null;
  }
  return children;
}

export default NeedAuth;
