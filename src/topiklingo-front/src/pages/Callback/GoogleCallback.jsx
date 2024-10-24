import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

import { usePostLoginMutate } from '@api/authApi';
import { loginBackPathAtom, userAtom } from '@atom';
import { useAtomValue, useSetAtom } from 'jotai';

function GoogleCallback() {
  const [searchParams] = useSearchParams();

  const code = searchParams.get('code');
  const navigate = useNavigate();

  const setUser = useSetAtom(userAtom);
  const loginBackPath = useAtomValue(loginBackPathAtom);

  const { mutateAsync: login } = usePostLoginMutate();

  useEffect(() => {
    if (code) {
      login(code).then(async (res) => {
        const body = await res.json();
        setUser({
          memberId: body?.memberId,
          token: body?.token,
          isLogin: true,
          auth: body?.authType,
        });
        if (body?.authType === 'UNVERIFIED_USER') {
          navigate('/register');
        } else {
          navigate(loginBackPath);
        }
      });
    }
  }, [code, navigate, setUser, loginBackPath, login]);

  return <div />;
}

export default GoogleCallback;
