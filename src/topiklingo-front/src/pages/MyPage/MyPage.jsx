/* eslint-disable no-nested-ternary */
import { useEffect, useState } from 'react';
import { FaTimesCircle } from 'react-icons/fa'; // Font Awesome X icon

import { useCheckToken } from '@api/authApi';
import { getUserAnalyze, getUserData, getUserRecord } from '@api/userApi';
import Container from '@components/common/container';
import COUNTRY_CODES from '@constants/ISO3166-1.alpha2';
import useGetUser from '@hooks/useGetUser';
import FilterableBarChart from '@pages/MyPage/FilterChart';
import typeMap from '@pages/MyPage/typeMapper';

function MyPage() {
  const user = useGetUser();
  const checkToken = useCheckToken();
  const [userData, setUserData] = useState(null);
  const [userRecord, setUserRecord] = useState([]);
  const [userAnalyze, setUserAnalyze] = useState([]);

  const getUserInfo = async () => {
    const pass = await checkToken();
    if (user && pass) {
      let fetchUserData = await getUserData(user?.memberId, user?.token);
      // add user auth data append to user data
      fetchUserData = { ...fetchUserData, role: user.auth };
      let fetchUserRecord = await getUserRecord(user?.memberId, user?.token);
      fetchUserRecord = fetchUserRecord.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      const fetchUserAnalyze = await getUserAnalyze(user?.memberId, user?.token);
      setUserData(fetchUserData);
      setUserRecord(fetchUserRecord);
      setUserAnalyze(fetchUserAnalyze);
    }
  };

  useEffect(() => {
    getUserInfo();
  }, [user]);

  return (
    <div className="-mb-72 flex-col">
      <div className="bg-gray-200 bg-opacity-50">
        <Container className="flex flex-wrap">
          <div className="flex flex-col w-full">
            <h1
              className="pr-10 text-4xl font-bold leading-snug tracking-tight text-gray-800 lg:text-4xl lg:leading-tight xl:text-4xl xl:leading-tight"
              data-aos="zoom-y-out"
              data-aos-delay="150"
            >
              My Page
            </h1>
            {userData && <UserProfile userData={userData} />}
            {/* <Link to="/admin/user-list">
              <h3
                className="text-2xl font-bold leading-snug tracking-tight text-gray-800 lg:leading-tight xl:leading-tight"
                data-aos="zoom-y-out"
                data-aos-delay="150"
              >
                User List
              </h3>
            </Link> */}
          </div>
        </Container>
      </div>
      <div className="flex flex-col">
        <Container className="-mb-10">
          {/* User Exam Records Section */}
          <div className="my-8">
            <h2 className="text-2xl md:text-3xl font-bold mb-4">User Exam Records</h2>
            {/* Set a fixed height and enable scrolling */}
            <div className="overflow-y-scroll max-h-96">
              {userRecord.length > 0 ? (
                userRecord.map((record) => (
                  <RecordItem key={record.id} record={record} />
                ))
              ) : (
                <div className="flex text-xl md:text-3xl items-center justify-center h-full text-gray-500">
                  <FaTimesCircle className="mr-2 text-gray-400" />
                  <span>No History</span>
                </div>
              )}
            </div>
          </div>
        </Container>
        <Container className="w-full md:mb-2">
          <div className="my-8">
            <h2 className="text-2xl md:text-3xl font-bold mb-6">Answer Rate In Graph</h2>
            {userAnalyze.length > 0 ? (
               <FilterableBarChart userAnalyze={userAnalyze} />
            ) : (
              <div className="flex flex-col items-center justify-center mt-20 text-xl md:text-2xl text-gray-500">
                <FaTimesCircle className="text-4xl mb-4 text-gray-400" />
                <span>No History</span>
              </div>
            )}
          </div>
        </Container>
        <Container className="w-full mb-20 md:mb-2">
          <div className="my-8 h-content">
            <h2 className="text-2xl md:text-3xl font-bold mb-6">Answer Rate In Statistics</h2>
            {userAnalyze.length > 0 ? (
              <div className="overflow-y-auto h-80 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 sm:gap-6 overflow-x-clip">
                  {userAnalyze.map((analyze) => (
                    <div
                      key={analyze.id}
                      className="bg-white p-4 sm:p-5 rounded-lg shadow-md transition-transform transform hover:scale-105 hover:shadow-lg"
                    >
                      {typeMap({ type: analyze.problemType })}
                      <p className="text-gray-700 mb-2 text-sm sm:text-base">
                        Correct: <span className="font-bold">{analyze.correctCount}</span> / Total: <span className="font-bold">{analyze.totalCount}</span>
                      </p>
                      <p className="text-gray-700 text-sm sm:text-base">
                        Accuracy: <span className="font-bold">{((analyze.correctCount / analyze.totalCount) * 100).toFixed(2)}%</span>
                      </p>
                    </div>
                  ))}
                </div>
            ) : (
              <div className="flex flex-col items-center justify-center mt-20 text-xl md:text-2xl text-gray-500">
                <FaTimesCircle className="text-4xl mb-4 text-gray-400" />
                <span>No History</span>
              </div>
            )}
          </div>
        </Container>
      </div>
    </div>
  );
}
function UserProfile({ userData }) {
  const getRoleBadge = (role) => {
    if (role === 'ADMIN') {
      return 'bg-red-100 text-red-600';
    } if (role === 'TEACHER') {
      return 'bg-green-100 text-green-600';
    } 
      return 'bg-blue-100 text-blue-600';
    
  };

  const getRoleLabel = (role) => {
    if (role === 'ADMIN') {
      return 'Admin';
    } if (role === 'TEACHER') {
      return 'Teacher';
    } 
      return 'Student';
    
  };

  return (
    <Container>
      <div className="bg-white p-6 rounded-lg shadow-lg mb-8">
        <div className="flex flex-col items-center md:flex-row md:items-start">
          <div className="flex flex-col items-center">
            <div className="w-32 h-32 bg-gray-200 rounded-full flex items-center justify-center text-gray-600 mb-2">
              {/* Placeholder for user avatar */}
              <span className="text-4xl font-bold">{userData.name[0]}</span>
            </div>
            {/* 소속 기관 또는 Personal user 표시 */}
            <p className="text-gray-500 text-sm mb-4">
              {userData.center !== 'None' ? userData.center : 'Personal User'}
            </p>
          </div>
          <div className="md:ml-8 text-center md:text-left">
            <div className="flex items-center justify-center md:justify-start">
              <h2 className="text-xl md:text-3xl font-bold mb-2">{userData.name}</h2>
              {/* 권한 배지 */}
              <span className={`ml-3 px-2 py-1 rounded-full text-sm font-semibold ${getRoleBadge(userData.role)}`}>
                {getRoleLabel(userData.role)}
              </span>
            </div>
            <p className="text-base md:text-lg text-gray-600 mb-1">{userData.email}</p>
            <p className="text-gray-600 mb-1">Nation: {COUNTRY_CODES[userData.nation]}</p>
            <p className="text-gray-600 mb-1">Gender: {userData.gender}</p>
            <p className="text-gray-600 mb-1">Birth: {userData.birth}</p>
            <p className="text-gray-600">Provider: {userData.provider}</p>
          </div>
        </div>
      </div>
    </Container>
  );
}

function RecordItem({ record }) {
  const [isOpen, setIsOpen] = useState(false);

  // 유저 답변과 실제 정답을 배열로 변환
  const userAnswers = record.memberAnswers.split(",");
  const correctAnswers = record.realAnswers.split(",");

  const openExamPage = (examId) => {
    const url = `http://www.vinedu.co.kr/exam?id=${examId}`;
    window.open(url, '_blank', 'width=1200,height=900,left=10,top=10,toolbar=no,titlebar=no,menubar=no,location=no,resizable=no');
  };

  const getBackgroundColor = (score) => {
    if (score < 50) return 'bg-red-500';   // 낮은 점수 (예: 0-49)
    if (score < 80) return 'bg-yellow-500'; // 중간 점수 (예: 50-79)
    return 'bg-green-500'; // 높은 점수 (예: 80+)
  };

  const scoreColor = getBackgroundColor(record.score);

  return (
    <div className="bg-white p-6 mb-6 rounded-lg shadow-lg transform transition duration-300 hover:shadow-2xl hover:-translate-y-1">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center">
        <div className="mb-4 sm:mb-0">
          <h3 className="text-base md:text-2xl font-bold text-gray-800 mb-2">{record.examName}</h3>
          <h4 className="text-sm text-gray-500 mb-2">Exam ID: {record.examId}</h4>
          <p className="text-sm text-gray-600">
            <button
              type="button"
              onClick={(e) => {
                e.preventDefault();
                openExamPage(record.examId);
              }}
              className="text-purple-500 hover:underline bg-transparent border-none p-0"
            >
              Go to Exam page
            </button>
          </p>
          <p className="text-sm text-gray-600">Exam Type: {record.examType}</p>
          <p className="text-sm text-gray-600">
            Submit Time: {new Date(record.createdAt).toLocaleString('ko-KR', {
              year: 'numeric',
              month: '2-digit',
              day: '2-digit',
              hour: '2-digit',
              minute: '2-digit',
              second: '2-digit',
              hour12: false,
            })}
          </p>
        </div>
        <div className={`flex flex-row sm:flex-col items-center justify-between md:mr-8 ${scoreColor} text-white px-4 md:px-8 py-2 md:py-4 rounded-lg shadow-md`}>
          <p className="text-sm font-medium">Score</p>
          <p className="ml-2 md:ml-0 text-xl md:text-3xl font-bold">{record.score}</p>
        </div>
      </div>
      {/* Toggle button to show/hide details */}
      <button
        type="button"
        onClick={() => setIsOpen(!isOpen)}
        className="mt-4 text-blue-600 font-semibold transition-colors duration-200 hover:text-blue-800"
      >
        {isOpen ? "Hide Details" : "Show Details"}
      </button>
      {isOpen && (
        <div className="mt-4 grid gap-2 grid-cols-2 sm:grid-cols-3 md:grid-cols-5 lg:grid-cols-10">
          {userAnswers.map((answer, index) => {
            const isCorrect = answer === correctAnswers[index];
            const displayAnswer = answer === "-1" ? "--" : answer;

            return (
              <div
                // eslint-disable-next-line react/no-array-index-key
                key={index}
                className="p-2 border rounded-lg shadow bg-gray-50 text-xs flex flex-col items-center"
              >
                <h4 className="text-sm font-semibold mb-1 border-b border-gray-300 pb-1">Q{index + 1}</h4>
                <p
                  className={`p-1 rounded font-medium ${
                    isCorrect ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700"
                  }`}
                >
                  <span className="font-bold">User:</span> {displayAnswer}
                </p>
                <p className="mt-1 text-xs">
                  <span className="font-bold">Correct:</span> {correctAnswers[index]}
                </p>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}


export default MyPage;