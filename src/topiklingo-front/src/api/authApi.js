import { useNavigate } from 'react-router-dom';

import { userAtom } from '@atom';
import useGetUser from '@hooks/useGetUser';
import { useMutation } from '@tanstack/react-query';
import { useSetAtom } from 'jotai';

const usePostLoginMutate = () => {
  const user = useGetUser();
  const fetcher = (code) =>
    fetch(`${import.meta.env.VITE_API_URL}/member/google`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${user?.token}`,
      },
      body: JSON.stringify({ code }),
    });
  return useMutation({
    mutationFn: fetcher,
  });
};

const usePostRegisterMutate = () => {
  const user = useGetUser();
  const fetcher = ({ memberId, nation, gender, birth, department }) => {
    fetch(`${import.meta.env.VITE_API_URL}/member/${memberId}/detail`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${user?.token}`,
      },
      body: JSON.stringify({ nation, gender, birth, department }),
    });
  };
  return useMutation({
    mutationFn: fetcher,
  });
};

const useCheckToken = () => {
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const user = useGetUser();
  const navigate = useNavigate();
  const setUser = useSetAtom(userAtom);

  const onTokenError = () => {
    alert('Please Login Again.');
    setUser({
      token: '',
      memberId: '',
      auth: '',
      isLogin: false,
    });
    navigate('/');
  };

  const checkToken = async () => {
    if (user?.token) {
      try {
        const res = await fetch(`${import.meta.env.VITE_API_URL}/member/validate`, {
          headers: {
            Authorization: `Bearer ${user.token}`,
          },
        });
        const json = await res.text();
        if (json) {
          onTokenError();
          return false;
        }
      } catch (error) {
        alert('Auth Check Error. Please Login Again.');
        return false;
      }
    }

    return true;
  };
  return checkToken;
};

export { usePostLoginMutate, usePostRegisterMutate, useCheckToken };
