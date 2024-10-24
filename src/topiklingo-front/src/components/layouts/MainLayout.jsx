// eslint-disable-next-line import/order
import { useState, useEffect } from 'react';
import { Outlet, useLocation } from 'react-router-dom';

import 'aos/dist/aos.css';

import Footer from '@components/common/footer';
import TopButton from '@components/common/topbutton';
import SkeletonLoader from '@components/Loading/skeletonLoading';
import AOS from 'aos';

import Header from './Header/Header';

function MainLayout() {
  const [isLoading, setIsLoading] = useState(false);
  const location = useLocation();

  useEffect(() => {
    AOS.init({
      once: true,
      duration: 700,
      easing: 'ease-out-cubic',
    });

    setIsLoading(true);

    setTimeout(() => {
      setIsLoading(false);
    }, 450);
  }, [location.pathname]);

  return (
    <div id="page_top" className="flex h-full min-h-screen flex-col overflow-clip">
      <Header />
      {isLoading ? (
        <><div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-30">
          <div className="animate-spin rounded-full bg-transparent p-4">
            <div className="gradient-border h-24 w-24 rounded-full" />
          </div>
        </div><SkeletonLoader /></>
      ) : (
        <div className="flex h-full w-full flex-col justify-center mt-28">
          <Outlet />
        </div>
      )}
      <Footer />
      <TopButton />
    </div>
  );
}

export default MainLayout;
