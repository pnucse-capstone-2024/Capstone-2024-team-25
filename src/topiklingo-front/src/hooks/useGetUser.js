import { useEffect, useState } from 'react';

import { userAtom } from '@atom';
import { useAtomValue } from 'jotai';

const useGetUser = () => {
  const [isMounted, setIsMounted] = useState(false);
  const user = useAtomValue(userAtom);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  return isMounted ? user : null;
};

export default useGetUser;
