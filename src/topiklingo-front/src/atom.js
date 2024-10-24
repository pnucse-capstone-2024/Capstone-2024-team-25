/* eslint-disable import/prefer-default-export */
import { atomWithStorage } from 'jotai/utils';

export const userAtom = atomWithStorage('user', {
  token: '',
  memberId: '',
  auth: '',
  isLogin: false,
});

export const loginBackPathAtom = atomWithStorage('loginBackPath', '/');
