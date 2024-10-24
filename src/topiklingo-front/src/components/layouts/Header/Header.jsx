import { Link, useNavigate } from 'react-router-dom';

import { useCheckToken } from '@api/authApi';
import GoogleButtonDesign from '@assets/google_sign_in.png';
import logo from '@assets/logo.png';
import { userAtom } from '@atom';
import GOOGLE_LOGIN_URL from '@constants/googleLoginUrl';
import { Disclosure } from '@headlessui/react';
import useGetUser from '@hooks/useGetUser';
import { Button } from '@mui/material';
import { useSetAtom } from 'jotai';

import HeaderTab from './HeaderTab';

const linkConfig = [
  // {
  //   title: 'Company',
  //   links: [
  //     {
  //       name: 'ABOUT',
  //       link: '/company/about',
  //     },
  //   ],
  // },
  {
    title: 'Topik I',
    links: [
      {
        name: 'Listening',
        link: '/mock/topik1/listening',
      },
      {
        name: 'Reading',
        link: '/mock/topik1/reading',
      },
    ],
  },
  {
    title: 'Topik II',
    links: [
      {
        name: 'Listening',
        link: '/mock/topik2/listening',
      },
      {
        name: 'Reading',
        link: '/mock/topik2/reading',
      },
      // {
      //   name: 'Writing',
      //   link: '/mock/topik2/writing',
      // },
    ],
  },
  {
    title: 'INTENSIVE',
    links: [
      {
        name: 'INTENSIVE',
        link: '/mock/combination-exam',
      },
    ],
  },
  // {
  //   title: 'Contact',
  //   links: [
  //     {
  //       name: 'Customer Service',
  //       link: '/contact/customer-service',
  //     },
  //     {
  //       name: 'Product Service',
  //       link: '/contact/product-service',
  //     },
  //   ],
  // },
];

function Header() {
  const user = useGetUser();
  const setUser = useSetAtom(userAtom);
  const navigate = useNavigate();
  const checkToken = useCheckToken();

  checkToken();

  const handleLogout = () => {
    setUser({
      token: '',
      memberId: '',
      auth: '',
      isLogin: false,
    });
    navigate('/');
  };

  return (
    <div className="header fixed top-0 z-40 flex w-screen justify-center shadow-[0_0_16px_rgba(0,0,0,.6)]">
      <div className="container relative mx-auto flex flex-wrap items-center justify-between p-8 lg:justify-between xl:px-0">
        {/* Logo  */}
        <Disclosure>
          {({ open }) => (
            <div className="flex w-full flex-wrap items-center justify-between lg:w-auto">
              <Link to="/">
                <span className="flex items-center space-x-2 text-2xl font-medium text-gray-500">
                  <span>
                    <img src={logo} alt="N" width="32" height="32" className="w-10" />
                  </span>
                  <div className="flex flex-col">
                    {' '}
                    {/* 세로 정렬 */}
                    <span className="text-2xl leading-tight sm:text-3xl font-serif font-bold">TopikLingo</span>{' '}
                    {/* 모바일에서는 text-2xl, 더 큰 화면에서는 text-3xl로 큰 글씨로 표시 */}
                    <span className="ml-2 text-xs sm:text-sm font-inter font-extrabold tracking-widest">Free Topik Exam</span>{' '}
                    {/* 모바일에서는 text-xs, 더 큰 화면에서는 text-sm으로 작은 글씨로 표시 */}
                  </div>
                </span>
              </Link>

              <Disclosure.Button
                aria-label="Toggle Menu"
                className="ml-auto rounded-md px-2 py-1 text-gray-500 hover:text-indigo-500 focus:bg-indigo-100 focus:text-indigo-500 focus:outline-none lg:hidden"
              >
                <svg className="h-6 w-6 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                  {open && (
                    <path
                      fillRule="evenodd"
                      clipRule="evenodd"
                      d="M18.278 16.864a1 1 0 0 1-1.414 1.414l-4.829-4.828-4.828 4.828a1 1 0 0 1-1.414-1.414l4.828-4.829-4.828-4.828a1 1 0 0 1 1.414-1.414l4.829 4.828 4.828-4.828a1 1 0 1 1 1.414 1.414l-4.828 4.829 4.828 4.828z"
                    />
                  )}
                  {!open && (
                    <path
                      fillRule="evenodd"
                      d="M4 5h16a1 1 0 0 1 0 2H4a1 1 0 1 1 0-2zm0 6h16a1 1 0 0 1 0 2H4a1 1 0 0 1 0-2zm0 6h16a1 1 0 0 1 0 2H4a1 1 0 0 1 0-2z"
                    />
                  )}
                </svg>
              </Disclosure.Button>

              <Disclosure.Panel className="z-40 my-5 flex w-full flex-col lg:hidden">
                <>
                  {linkConfig.map(({ title, links }) => {
                    // 링크가 하나인 경우 바로 이동하는 링크 렌더링
                    if (links.length === 1) {
                      return (
                        <Link
                          key={title}
                          to={links[0].link}
                          className="px-4 py-2 text-lg font-normal text-gray-700 hover:bg-gray-100"
                        >
                          {links[0].name}
                        </Link>
                      );
                    }
                    // 링크가 여러 개인 경우, 기존의 HeaderTab 컴포넌트 렌더링
                    return <HeaderTab key={title} title={title} links={links} className="hover:bg-gray-100" />;
                  })}
                  {user &&
                    (user.isLogin ? (
                      <div className="flex flex-col">
                        <Link to="/mypage" className="rounded-md bg-orange-600 px-6 py-2 text-white md:ml-2">
                          My Page
                        </Link>
                        <Button className="!flex !justify-end" onClick={handleLogout}>
                          Logout
                        </Button>
                      </div>
                    ) : (
                      <a href={GOOGLE_LOGIN_URL} className="h-12 w-32 md:ml-2">
                        <img
                          className="box-content h-full min-w-full rounded-md border-2 border-orange-600 bg-[#F2F2F2] object-contain"
                          src={GoogleButtonDesign}
                          alt="Google Login"
                        />
                      </a>
                    ))}
                </>
              </Disclosure.Panel>
            </div>
          )}
        </Disclosure>

        {/* menu  */}
        <div className="hidden text-center lg:flex lg:items-center">
          <ul className="z-40 flex-1 list-none items-center justify-end pt-6 lg:flex lg:pt-0">
            {linkConfig.map(({ title, links }) => {
              // 링크가 하나인 경우 바로 이동하는 링크 렌더링
              if (links.length === 1) {
                return (
                  <Link
                    key={title}
                    to={links[0].link}
                    className="px-4 py-2 text-lg font-normal text-gray-700 hover:bg-gray-100"
                  >
                    {links[0].name}
                  </Link>
                );
              }
              // 링크가 여러 개인 경우, 기존의 HeaderTab 컴포넌트 렌더링
              return <HeaderTab key={title} title={title} links={links} className="hover:bg-gray-100" />;
            })}
          </ul>
        </div>

        <div className="nav__item mr-3 hidden space-x-4 lg:flex">
          {user &&
            (user.isLogin ? (
              <div className="flex gap-5">
                <Button onClick={handleLogout}>Logout</Button>
                <Link to="/mypage" className="rounded-md bg-orange-600 px-6 py-2 text-white md:ml-2">
                  My Page
                </Link>
              </div>
            ) : (
              <a href={GOOGLE_LOGIN_URL} className="h-12 w-32 md:ml-2">
                <img
                  className="box-content h-full min-w-full rounded-md border-2 border-orange-600 bg-[#F2F2F2] object-contain"
                  src={GoogleButtonDesign}
                  alt="Google Login"
                />
              </a>
            ))}
        </div>
      </div>
    </div>
  );
}

export default Header;
