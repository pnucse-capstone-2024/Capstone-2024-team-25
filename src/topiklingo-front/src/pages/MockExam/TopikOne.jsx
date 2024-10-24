import { useEffect, useRef, useState } from 'react';

import { useCheckToken } from '@api/authApi';
import { getAnswerInfo, getExamInfo, usePostGenerateExamMutate } from '@api/examApi';
import Container from '@components/common/container';
import NeedAuth from '@components/common/NeedAuth';
import SectionTitle from '@components/common/sectionTitle';
import Topik from '@components/exam/exam-types/Topik';
import PrintOneLayout from '@components/layouts/PrintLayout';
import PrintTwoLayout from '@components/layouts/PrintTwoLayout';
import ExamLoading from '@components/Loading/ExamLoading';
import PageHeader from '@components/PageHeader/PageHeader';
import { reading1 } from '@constants/topik-problem-types';
import useGetUser from '@hooks/useGetUser';
import openExam from '@utils/openExam';
import openModifyExam from '@utils/openModifyExam';
import createPDF from '@utils/pdf';

import MockExamHero from './MockExamHero';
import PBTModal from './PBTModal';

const shuffleCustomCount = {
  reading_1_problem_type_1: 2,
  reading_1_problem_type_5: 4,
  reading_1_problem_type_8: 5,
};

function TopikOne({ defaultType }) {
  const user = useGetUser();
  const ref = useRef(null);
  const scrollRef = useRef(null);

  const [isLoading, setIsLoading] = useState(false);
  const [exams, setExams] = useState(null);
  const [examConfig, setExamConfig] = useState(null);
  const [years, setYears] = useState([]);
  const [selectedYear, setSelectedYear] = useState(0);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedExamId, setSelectedExamId] = useState(null);
  const [printLayout, setLayout] = useState('double');
  const [isCBTSelected, setCBTSelected] = useState(true);
  const { mutateAsync: generateExam } = usePostGenerateExamMutate();

  const [isInSpecial, setIsInSpecial] = useState(false);
  const [specialExams, setSpecialExams] = useState([]);
  const [specialExamsYears, setSpecialExamsYears] = useState([]);
  const [specialSelectedYear, setSpecialSelectedYear] = useState(0);

  const checkToken = useCheckToken();

  // 모달을 열기 위한 함수
  const openModal = (id) => {
    // 여기서 id를 인자로 받습니다.
    setSelectedExamId(id); // 선택된 시험의 ID를 상태로 저장
    setIsModalOpen(true);
  };

  // 모달을 닫기 위한 함수
  const closeModal = () => setIsModalOpen(false);

  const cbtExam = async (id) => {
    setIsLoading(true);
    setCBTSelected(true);
    setTimeout(async () => {
      const pass = await checkToken();
      if (pass) await openExam(id, user?.auth);
      setIsLoading(false);
    }, 1500);
  };

  const openModifyExamPage = (id) => {
    openModifyExam(id);
  };

  const examTime = {
    TOPIK_1_LISTENING: { time: 40 },
    TOPIK_1_READING: { time: 60 },
    GENERATED: { time: 60 },
  };

  const printExam = async (id, title, isIncludeCover, isIncludeAnswers, selectedLayout) => {
    setIsLoading(true);
    setCBTSelected(false);
    if (!(await checkToken())) {
      setIsLoading(false);
      return;
    }
    const { examId, year, totalQuestions, type, config } = await getExamInfo(id, user?.token);
    const { answerJson } = await getAnswerInfo(examId, user?.token);
    setExamConfig(config);
    setLayout(selectedLayout);

    if (type && config) {
      if (selectedLayout === 'single') {
        setTimeout(async () => {
          await window.print();
          setIsLoading(false);
        }, 3000);
      } else {
        setTimeout(async () => {
          let coverProps = null;
          let answerProps = null;
          if (isIncludeAnswers) {
            // extract answer info
            answerProps = answerJson.map((answer) => {
              const { rightAnswer } = answer;
              return { rightAnswer };
            });
          }
          if (isIncludeCover) {
            if (type === 'GENERATED') {
              coverProps = {
                id: examId,
                year,
                examType: 'I',
                subject: 'Reading Shuffled',
                title,
                timeLimit: examTime[type].time,
                totalQuestions,
              };
            } else {
              const parts = type.split('_');
              const examType = 'I';
              const subject = parts[2] || 'READING';
              coverProps = {
                id: examId,
                year,
                examType,
                subject,
                title,
                timeLimit: examTime[type].time,
                totalQuestions,
              };
            }
          }
          await createPDF(ref, id, title, isIncludeCover, coverProps, isIncludeAnswers, answerProps);
          setIsLoading(false);
        }, 2000);
      }
    } else {
      setIsLoading(false);
    }
  };

  const handleSubmit = (formData) => {
    printExam(formData.examId, formData.title, formData.includeCover, formData.includeAnswers, formData.layout);
  };

  const getExams = async () => {
    try {
      const res = await fetch(`${import.meta.env.VITE_API_URL}/exam/topik1-${defaultType}`);

      if (res.ok) {
        const json = await res.json();

        const specialWord = "Special";

        let examsJson = json.sort((a, b) => b.year - a.year);
        const specialExamsYearsSet = new Set();

        examsJson.forEach((exam) => {
          if (exam.title.includes(specialWord)) {
            specialExams.push(exam);
            specialExamsYearsSet.add(exam.year); // Add unique years to the set
            setIsInSpecial(true);
          }
        });
  
        setSpecialExams(specialExams);
        setSpecialExamsYears(Array.from(specialExamsYearsSet));
        setSpecialSelectedYear(Array.from(specialExamsYearsSet)[0]);

        examsJson = examsJson.filter(exam => !exam.title.includes(specialWord));
        const yrs = examsJson.map(({ year }) => year).filter((el, index, arr) => arr.indexOf(el) === index);

        setExams(examsJson);
        setYears(() => yrs);
        setSelectedYear(yrs[0]);
      }
    } catch (err) {
      alert('Data loading failed. Please try again.');
    }
  };

  let heroProps;
  switch (defaultType) {
    case 'listening':
      heroProps = {
        title: 'Topik I Listening Actual Test',
        description:
          'The TOPIK I is designed for beginners, covering levels 1 and 2. The Listening tested your ability to understand spoken Korean.',
        applicant: 116120,
        candidate: 95942,
        passed: 85448,
      };
      break;
    case 'reading':
      heroProps = {
        title: 'Topik I Reading Actual Test',
        description:
          'The TOPIK I is designed for beginners, covering levels 1 and 2. The Reading tested your ability to understand written Korean.',
        applicant: 116120,
        candidate: 95942,
        passed: 85448,
      };
      break;
    default:
      heroProps = {
        title: 'Unknown Exam',
        description: 'Please select a valid exam type.',
        applicant: 0,
        candidate: 0,
        passed: 0,
      };
  }

  const handleGenerateShuffledExam = async () =>
    generateExam({
      title: `TOPIK 1 ${defaultType} shuffled test generate`,
      problems: Object.values(reading1).map((type) => ({
        problemType: type.id,
        problemCount: shuffleCustomCount[type.id] || 1,
      })),
    });

  const handleCBTShuffledExam = async () => {
    setIsLoading(true);
    setCBTSelected(true);
    const pass = await checkToken();
    if (pass) {
      handleGenerateShuffledExam().then((res) => {
        openExam({ id: res }, user?.auth);
        setIsLoading(false);
      });
    } else {
      setIsLoading(false);
    }
  };

  const handlePBTShuffledExam = () => {
    setIsLoading(true);
    handleGenerateShuffledExam().then((res) => {
      if (res) {
        openModal(res);
      }
      setIsLoading(false);
    });
  };

  useEffect(() => {
    getExams();
  }, [defaultType]);

  useEffect(() => {
    if (isLoading) {
      // 로딩 중일 때 스크롤 비활성화
      document.body.style.overflow = 'hidden';
    } else {
      // 로딩이 끝나면 스크롤 활성화
      document.body.style.overflow = 'auto';
    }
    return () => {
      document.body.style.overflow = 'auto';
    };
  }, [isLoading]);

  return (
    <>
      <ExamLoading isLoading={isLoading} isCBTSelected={isCBTSelected} />
      <div className="-mb-72 flex w-full flex-col">
        <MockExamHero
          // eslint-disable-next-line react/jsx-props-no-spreading
          {...heroProps}
        />
        {isInSpecial && (
          <Container>
          <PageHeader title="Special Exam" />
          <div className="flex -mb-20 w-full flex-col justify-start p-4 md:mb-0 md:flex-row md:justify-between">
            <div
              ref={scrollRef}
              onWheel={(e) => {
                scrollRef.current.scrollLeft += e.deltaY;
              }}
              className="mb-4 flex w-full justify-start overflow-y-scroll scrollbar-hide md:mb-0 md:w-1/6 md:flex-col"
            >
              {specialExamsYears &&
                specialExamsYears.map((specialYear) => (
                  <button
                    type="button"
                    key={specialYear}
                    className={`mr-2 flex h-16 min-h-16 w-48 items-center justify-center rounded-xl px-10 font-semibold hover:cursor-pointer hover:bg-gray-200 hover:text-midnightBlue md:mb-2 md:w-11/12 md:px-4 ${setSpecialSelectedYear === specialYear ? 'bg-gray-200 text-midnightBlue' : 'text-gray-500'}`}
                    onClick={() => setSpecialSelectedYear(specialYear)}
                  >
                    {specialYear}년
                  </button>
                ))}
            </div>
            <div className="flex w-full flex-col justify-start overflow-y-scroll scrollbar-hide md:w-3/4">
              {specialExams &&
                specialExams
                  .filter((exam) => exam.year === specialSelectedYear)
                  .map(({ id, title }) => (
                    <div
                      key={id}
                      className="mb-4 flex min-h-24 w-full flex-col justify-start rounded-lg border shadow-lg md:w-11/12 md:flex-row md:border-none bg-orange-100 bg-opacity-70"
                    >
                      <div className="flex h-full w-full items-center justify-start mt-4 md:mt-0 pl-4 text-sm font-semibold text-midnightBlue md:w-3/4 md:text-lg">
                        {title}
                      </div>
                      <div className="flex w-full flex-col items-center justify-end p-4 md:h-full md:w-1/2 md:flex-row md:justify-end md:pr-4">
                        <NeedAuth auth="TEACHER">
                          <button
                            className="mb-2 mr-2 flex h-2/3 w-full items-center justify-center rounded-xl bg-red-100 py-2 text-xs text-red-400 hover:bg-red-400 hover:text-white md:mb-0 md:mr-2 md:w-1/3 md:text-lg"
                            type="button"
                            onClick={() => openModal(id)}
                          >
                            PBT
                          </button>
                        </NeedAuth>
                        <button
                          className="mb-2 mr-2 flex h-2/3 w-full items-center justify-center rounded-xl bg-gray-300 py-2 text-xs hover:bg-midnightBlue hover:text-white md:mb-0 md:mr-2 md:w-1/3 md:text-lg"
                          type="button"
                          onClick={() => cbtExam({ id })}
                        >
                          CBT
                        </button>
                      </div>
                      {isModalOpen && <PBTModal onClose={closeModal} onSubmit={handleSubmit} examId={selectedExamId} />}
                    </div>
                  ))}
            </div>
          </div>
        </Container>
        )}
        {defaultType !== 'listening' && (
          <Container>
            <PageHeader title="Shuffle Exam" />
            <div className="mb-4 mt-10 flex min-h-24 w-full flex-col rounded-lg shadow-lg first-letter:border md:w-11/12 md:flex-row md:border-none">
              <div className="mb-6 flex items-center justify-center text-sm font-semibold text-midnightBlue md:w-3/4 md:text-xl">
                TOPIK 1 {defaultType} shuffled test generate
              </div>
              <div className="flex w-full flex-col items-center justify-end p-4 md:h-full md:w-1/2 md:flex-row md:justify-end md:pr-4">
                <NeedAuth auth="TEACHER">
                  <button
                    onClick={handlePBTShuffledExam}
                    className="mb-2 mr-2 flex h-2/3 w-full items-center justify-center rounded-xl bg-red-100 py-2 text-xs text-red-400 hover:bg-red-400 hover:text-white md:mb-0 md:mr-2 md:w-1/3 md:text-lg"
                    type="button"
                  >
                    PBT
                  </button>
                </NeedAuth>
                <button
                  onClick={handleCBTShuffledExam}
                  className="mb-2 mr-2 flex h-2/3 w-full items-center justify-center rounded-xl bg-gray-300 py-2 text-xs hover:bg-midnightBlue hover:text-white md:mb-0 md:mr-2 md:w-1/3 md:text-lg"
                  type="button"
                >
                  CBT
                </button>
              </div>
              {isModalOpen && <PBTModal onClose={closeModal} onSubmit={handleSubmit} examId={selectedExamId} />}
            </div>
          </Container>
        )}
        <Container>
          <PageHeader title="Searching Exam Bank" />
          <div className="mb-32 flex h-full min-h-screen w-full flex-col justify-start p-4 md:mb-0 md:flex-row md:justify-between">
            <div
              ref={scrollRef}
              onWheel={(e) => {
                scrollRef.current.scrollLeft += e.deltaY;
              }}
              className="mb-4 flex w-full justify-start overflow-y-scroll scrollbar-hide md:mb-0 md:h-content md:w-1/6 md:flex-col"
            >
              {years &&
                years.map((year) => (
                  <button
                    type="button"
                    key={year}
                    className={`mr-2 flex h-16 min-h-16 w-48 items-center justify-center rounded-xl px-10 font-semibold hover:cursor-pointer hover:bg-gray-200 hover:text-midnightBlue md:mb-2 md:w-11/12 md:px-4 ${selectedYear === year ? 'bg-gray-200 text-midnightBlue' : 'text-gray-400'}`}
                    onClick={() => setSelectedYear(year)}
                  >
                    {year}년
                  </button>
                ))}
            </div>
            <div className="flex h-content w-full flex-col justify-start overflow-y-scroll scrollbar-hide md:w-3/4">
              {exams &&
                exams
                  .filter((exam) => exam.year === selectedYear)
                  .map(({ id, title }) => (
                    <div
                      key={id}
                      className="mb-4 flex min-h-24 w-full flex-col justify-start rounded-lg border shadow-lg md:w-11/12 md:flex-row md:border-none"
                    >
                      <div className="flex h-full w-full items-center justify-start pl-4 mt-4 md:mt-0 text-sm font-semibold text-midnightBlue md:w-3/4 md:text-lg">
                        {title}
                      </div>
                      <div className="flex w-full flex-col items-center justify-end p-4 md:h-full md:w-1/2 md:flex-row md:justify-end md:pr-4">
                        <NeedAuth auth="ADMIN">
                          {defaultType !== 'listening' && (
                            <button
                              className="mb-2 mr-2 flex h-2/3 w-full items-center justify-center rounded-xl bg-blue-200 py-2 text-xs text-blue-400 hover:bg-blue-400 hover:text-white md:mb-0 md:mr-2 md:w-1/3 md:text-lg"
                              type="button"
                              onClick={() => openModifyExamPage({ id })}
                            >
                              MODIFY
                            </button>
                          )}
                        </NeedAuth>
                        <NeedAuth auth="TEACHER">
                          <button
                            className="mb-2 mr-2 flex h-2/3 w-full items-center justify-center rounded-xl bg-red-100 py-2 text-xs text-red-400 hover:bg-red-400 hover:text-white md:mb-0 md:mr-2 md:w-1/3 md:text-lg"
                            type="button"
                            onClick={() => openModal(id)}
                          >
                            PBT
                          </button>
                        </NeedAuth>
                        <button
                          className="mb-2 mr-2 flex h-2/3 w-full items-center justify-center rounded-xl bg-gray-300 py-2 text-xs hover:bg-midnightBlue hover:text-white md:mb-0 md:mr-2 md:w-1/3 md:text-lg"
                          type="button"
                          onClick={() => cbtExam({ id })}
                        >
                          CBT
                        </button>
                      </div>
                      {isModalOpen && <PBTModal onClose={closeModal} onSubmit={handleSubmit} examId={selectedExamId} />}
                    </div>
                  ))}
            </div>
          </div>
        </Container>
      </div>
      <div id="printContent" ref={ref} className={isLoading ? '' : 'hidden'}>
        {printLayout === 'single' && ( // 1단 출력 선택 시
          <PrintOneLayout>
            <Topik config={examConfig} userSelected={[]} setUserSelected={() => {}} />
          </PrintOneLayout>
        )}
        {printLayout === 'double' && ( // 2단 출력 선택 시
          <PrintTwoLayout>
            <Topik config={examConfig} userSelected={[]} setUserSelected={() => {}} />
          </PrintTwoLayout>
        )}
      </div>
      <SectionTitle pretitle="" title="">
        We provide the same CBT and PBT as the actual past questions.
      </SectionTitle>
    </>
  );
}

export default TopikOne;
