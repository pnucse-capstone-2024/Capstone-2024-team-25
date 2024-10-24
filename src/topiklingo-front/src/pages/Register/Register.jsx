import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { usePostRegisterMutate } from '@api/authApi';
import { userAtom } from '@atom';
import PageHeader from '@components/PageHeader/PageHeader';
import COUNTRY_CODES from '@constants/ISO3166-1.alpha2';
import useGetUser from '@hooks/useGetUser';
import { useSetAtom } from 'jotai';

const defaultInfo = {
  nation: '',
  gender: '',
  birth: `${new Date().getFullYear()}-01-01`, // 기본값으로 현재 연도 1월 1일
  department: 'None',
};

const dummyCenter = [
  { id: 1, name: 'Inje University' },
  { id: 2, name: 'Pusan National University' },
];

function Register() {
  const navigate = useNavigate();
  const user = useGetUser();
  const setUser = useSetAtom(userAtom);
  const { mutateAsync: register } = usePostRegisterMutate();
  const [submitting, setSubmitting] = useState(false);
  const [info, setInfo] = useState(defaultInfo);

  const handleRegister = () => {
    if (submitting || !user) return;

    // Validate required fields
    if (!info.nation || info.nation === '' || info.birth === `${new Date().getFullYear()}-01-01`) {
      alert('Please fill in all required fields (Country and BirthDay).');
      return;
    }

    setSubmitting(true);
    register({
      memberId: user.memberId,
      nation: info.nation,
      gender: info.gender,
      birth: info.birth,
      department: info.department === 'Personal' ? 'None' : info.department, // 'Personal'일 때는 'None'으로 전송
    }).then(() => {
      setSubmitting(false);
      setUser({
        ...user,
        auth: 'STUDENT',
      });
      alert('Successfully registered!');
      navigate('/');
    });
  };

  return (
    <div className="flex h-screen md:grid md:grid-flow-col md:grid-cols-2">
      <div className="hidden h-full w-full bg-blue-500 md:flex" />
      <div className="flex h-full w-full flex-col place-content-center bg-gray-200 px-3 md:px-20">
        <div className="flex w-full flex-col rounded-xl bg-white shadow-2xl">
          <PageHeader title="VineEdu Register" />
          <div className="flex w-full flex-col px-10 py-5">
            {/* Country Input */}
            <p className="text-xl text-gray-500">Country <span className="text-red-500">* <span className='text-xs'>Required</span></span></p>
            <select
              value={info.nation}
              onChange={(e) => {
                setInfo({ ...info, nation: e.target.value });
              }}
              className="mt-2 rounded-md border border-gray-300 p-2 focus:outline-none"
            >
              <option value="">Select Your Country</option>
              {Object.keys(COUNTRY_CODES).map((key) => (
                <option key={key} value={key}>
                  {COUNTRY_CODES[key]}
                </option>
              ))}
            </select>
            {/* Gender Input */}
            <p className="mt-8 text-xl text-gray-500">Gender</p>
            <select
              value={info.gender}
              onChange={(e) => {
                setInfo({ ...info, gender: e.target.value });
              }}
              className="mt-2 rounded-md border border-gray-300 p-2 focus:outline-none"
            >
              <option value="">Select Your Gender</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
            </select>
            {/* BirthDay Input */}
            <p className="mt-8 text-xl text-gray-500">BirthDay <span className="text-red-500">* <span className='text-xs'>Required</span></span></p>
            <input
              type="date"
              value={info.birth}
              onChange={(e) => {
                setInfo({ ...info, birth: e.target.value });
              }}
              className="mt-2 rounded-md border border-gray-300 p-2 focus:outline-none"
            />
            {/* Center Input */}
            <p className="mt-8 text-xl text-gray-500">Department (Optional)</p>
            <select
              value={info.department === 'None' ? 'Personal' : info.department} // 'None'일 때 'Personal'로 보여줌
              onChange={(e) => {
                setInfo({ ...info, department: e.target.value === 'Personal' ? 'None' : e.target.value });
              }}
              className="mt-2 rounded-md border border-gray-300 p-2 focus:outline-none"
            >
              <option value="Personal">Personal</option> {/* Personal 옵션 추가 */}
              {dummyCenter.map((center) => (
                <option key={center.id} value={center.name}>
                  {center.name}
                </option>
              ))}
            </select>
            {/* Submit */}
            <button
              type="button"
              onClick={handleRegister}
              className="mb-4 mt-10 rounded-md bg-blue-500 p-2 text-white focus:outline-none"
            >
              Register
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Register;
