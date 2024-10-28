import { useEffect, useRef, useState } from 'react';

import { getAnswerInfo, getExamInfo, usePostGenerateExamMutate } from '@api/examApi';
import Container from '@components/common/container';
import NeedAuth from '@components/common/NeedAuth';
import SectionTitle from '@components/common/sectionTitle';
import Topik from '@components/exam/exam-types/Topik';
import PrintLayout from '@components/layouts/PrintLayout';
import PrintTwoLayout from '@components/layouts/PrintTwoLayout';
import ExamLoading from '@components/Loading/ExamLoading';
import PageHeader from '@components/PageHeader/PageHeader';
import AUTH_LIST from '@constants/authList';
import topikProblemTypes, { reading1, reading2 } from '@constants/topik-problem-types';
import { faArrowRight, faArrowLeft, faArrowDown, faArrowUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import useGetUser from '@hooks/useGetUser';
import openExam from '@utils/openExam';
import createPDF from '@utils/pdf';

import MockExamHero from './MockExamHero';

const PROBLEM_CNT_RANGE = {
  MIN: 1,
  MAX: 5,
};

const TOPIK_CATEGORY = [
  // {
  //   id: 'listen1',
  //   title: '토픽1 듣기',
  //   types: listen1,
  // },
  {
    id: 'reading1',
    title: 'TOPIK1 Reading',
    types: reading1,
  },
  // {
  //   id: 'listen2',
  //   title: '토픽2 듣기',
  //   types: listen2,
  // },
  {
    id: 'reading2',
    title: 'TOPIK2 Reading',
    types: reading2,
  },
];

const defaultExam = {
  title: '',
  problems: [],
  includeCover: true,
  includeAnswer: true,
  printLayout: '2 Layer',
};

function ProblemTypeCard({ type, selected, onClick }) {
  return (
    <button
      type="button"
      onClick={onClick}
      className={`item-center flex h-16 min-h-16 w-full min-w-[30vw] place-items-center justify-center rounded-lg px-4 text-sm font-bold md:text-lg lg:min-w-full ${selected ? 'bg-indigo-600 text-white' : 'border-2 border-indigo-600'}`}
    >
      {type.title}
    </button>
  );
}

function Category() {
  const user = useGetUser();
  const ref = useRef(null);
  const [isLoading, setIsLoading] = useState(false);
  const [addTopikCategory, setAddTopikCategory] = useState(TOPIK_CATEGORY[0]);
  const [addSelectedList, setAddSelectedList] = useState([]);

  const [isCBTSelected, setCBTSelected] = useState(true);

  const [includedSelectedList, setIncludedSelectedList] = useState([]);

  const [exam, setExam] = useState(defaultExam);
  const [examConfig, setExamConfig] = useState(null);

  const { mutateAsync: generateExam } = usePostGenerateExamMutate();

  const handleCategoryAdd = (key) => {
    if (addSelectedList.includes(key)) {
      setAddSelectedList((prev) => prev.filter((item) => item !== key));
    } else {
      setAddSelectedList((prev) => [...prev, key]);
    }
  };

  const handleCategoryIncluded = (key) => {
    if (includedSelectedList.includes(key)) {
      setIncludedSelectedList((prev) => prev.filter((item) => item !== key));
    } else {
      setIncludedSelectedList((prev) => [...prev, key]);
    }
  };

  const handleAddButton = () => {
    setExam((prev) => ({
      ...prev,
      problems: [
        ...prev.problems,
        ...addSelectedList
          .filter((item) => !prev.problems.map((pb) => pb.type).includes(item))
          .map((item) => ({
            type: item,
            count: 1,
          })),
      ],
    }));
    setAddSelectedList([]);
  };

  const handleExcludeButton = () => {
    setExam((prev) => ({
      ...prev,
      problems: prev.problems.filter((item) => !includedSelectedList.includes(item.type)),
    }));
    setIncludedSelectedList([]);
  };

  const calcIncludedSelectedCount = () => {
    if (includedSelectedList.length === 0) return '';

    const { count } = exam.problems.find((pd) => pd.type === includedSelectedList[0]);

    for (let i = 1; i < includedSelectedList.length; i += 1) {
      const type = includedSelectedList[i];
      if (exam.problems.find((pd) => pd.type === type).count !== count) {
        return '';
      }
    }
    return count;
  };

  const handleProblemCountChange = (e) => {
    const value = parseInt(e.target.value, 10);
    if (value >= PROBLEM_CNT_RANGE.MIN && value <= PROBLEM_CNT_RANGE.MAX) {
      setExam((prev) => ({
        ...prev,
        problems: prev.problems.map((pb) => ({
          ...pb,
          count: includedSelectedList.includes(pb.type) ? value : pb.count,
        })),
      }));
    }
  };

  const handleExamTitleChange = (e) => {
    setExam((prev) => ({
      ...prev,
      title: e.target.value,
    }));
  };

  const handleExamCoverToggle = () => {
    setExam((prev) => ({
      ...prev,
      includeCover: !prev.includeCover,
    }));
  };

  const handleExamAnswerToggle = () => {
    setExam((prev) => ({
      ...prev,
      includeAnswer: !prev.includeAnswer,
    }));
  };

  const handleExamLayoutChange = (value) => {
    setExam((prev) => ({
      ...prev,
      printLayout: value,
    }));
  };

  const validateExam = () => {
    const studentIdx = AUTH_LIST.indexOf('STUDENT');
    const userAuthIdx = user?.auth ? AUTH_LIST.indexOf(user?.auth) : AUTH_LIST.length;
    if (studentIdx < userAuthIdx) {
      alert('You do not have permission. Please Login First.');
      return false;
    }

    if (exam.problems.length === 0) {
      alert('Please choose a range of types');
      return false;
    }

    return true;
  };

  const handleGenerateExam = async () => {
    if (validateExam()) {
      return generateExam({
        title: exam.title,
        problems: exam.problems.map((pb) => ({
          problemType: pb.type,
          problemCount: pb.count,
        })),
      });
    }
    return null;
  };

  const handlePBTButtonClick = async () => {
    setIsLoading(true);
    setCBTSelected(false);
    const id = await handleGenerateExam();
    const { examId, year, totalQuestions, type, config } = await getExamInfo(id, user?.token);
    const { answerJson } = await getAnswerInfo(examId, user?.token);
    setExamConfig(config);

    if (config) {
      if (exam.printLayout === '1 Layer') {
        setTimeout(async () => {
          await window.print();
          setIsLoading(false);
        }, 3000);
      } else {
        setTimeout(async () => {
          let coverProps = null;
          let answerProps = null;
          if (exam.includeAnswer) {
            answerProps = answerJson.map((answer) => {
              const { rightAnswer } = answer;
              return { rightAnswer };
            });
          }
          if (exam.includeCover) {
            coverProps = {
              id: examId,
              year,
              examType: 'MIXED',
              subject: type,
              title: exam.title,
              totalQuestions,
              timeLimit: 1.5 * totalQuestions,
            };
          }
          await createPDF(ref, id, exam.title, exam.includeCover, coverProps, exam.includeAnswer, answerProps);
          setIsLoading(false);
        }, 3000);
      }
    } else {
      setIsLoading(false);
    }
  };

  const handleCBTButtonClick = () => {
    setCBTSelected(true);
    setIsLoading(true);
    handleGenerateExam()?.then((res) => {
      if (res) {
        openExam({ id: res }, user?.auth);
      }
      setIsLoading(false);
    });
  };

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
      <div className="z-30 flex h-full w-full flex-col">
        <div>
          <MockExamHero
            title="Topik Mock Exam By Type"
            description="Select the type of test you want to take. You can freely practice the same TOPIK test as the actual one."
            applicant={116120 + 301881}
            candidate={95942 + 237347}
            passed={85448 + 156139}
          />
          <Container>
            <div className="flex h-full w-full flex-col">
              <PageHeader title="INTENSIVE EXAM GENERATOR" />
              <div className="grid-rows-auto grid h-full w-full gap-10 p-4 lg:grid-cols-[2fr_1fr] lg:grid-rows-[4fr_1fr] xl:grid-cols-[4fr_1fr]">
                <div className="grid w-full grid-cols-none rounded-lg border-2 border-black p-4 lg:h-full lg:grid-cols-[2fr_1fr_2fr]">
                  <div className="flex h-full w-full flex-col gap-4">
                    <div className="gird-cols-4 grid h-20 w-full grid-flow-col place-items-center text-sm font-bold md:text-lg">
                      {TOPIK_CATEGORY.map((category) => (
                        <button
                          key={category.id}
                          type="button"
                          disabled={addTopikCategory.id === category.id}
                          onClick={() => setAddTopikCategory(category)}
                          className="h-full w-full border-b-4 border-gray-400 pb-2 text-center text-sm text-gray-400 disabled:border-indigo-600 disabled:text-indigo-600 lg:text-lg"
                        >
                          {category.title}
                        </button>
                      ))}
                    </div>
                    <div className="mt-5 flex h-20 w-[70vw] gap-4 overflow-scroll md:scrollbar-hide lg:h-content lg:w-full lg:flex-col">
                      {Object.keys(addTopikCategory.types).map((key) => (
                        <ProblemTypeCard
                          key={key}
                          selected={addSelectedList.includes(key)}
                          onClick={() => handleCategoryAdd(key)}
                          type={addTopikCategory.types[key]}
                        />
                      ))}
                    </div>
                  </div>
                  <div className="flex h-20 w-full place-content-center place-items-center gap-10 lg:h-auto lg:flex-col">
                    <FontAwesomeIcon
                      icon={faArrowRight}
                      cursor="pointer"
                      onClick={handleAddButton}
                      className="hidden rounded-lg bg-indigo-600 p-2 text-sm text-white shadow-lg hover:shadow-2xl sm:p-4 sm:text-2xl lg:block"
                    />
                    <FontAwesomeIcon
                      icon={faArrowDown}
                      cursor="pointer"
                      onClick={handleAddButton}
                      className="rounded-lg bg-indigo-600 p-2 text-sm text-white shadow-lg hover:shadow-2xl sm:p-4 sm:text-2xl lg:hidden"
                    />
                    <FontAwesomeIcon
                      icon={faArrowLeft}
                      cursor="pointer"
                      onClick={handleExcludeButton}
                      className="hidden rounded-lg bg-indigo-600 p-2 text-sm text-white shadow-lg hover:shadow-2xl sm:p-4 sm:text-2xl lg:block"
                    />
                    <FontAwesomeIcon
                      icon={faArrowUp}
                      cursor="pointer"
                      onClick={handleExcludeButton}
                      className="rounded-lg bg-indigo-600 p-2 text-sm text-white shadow-lg hover:shadow-2xl sm:p-4 sm:text-2xl lg:hidden"
                    />
                  </div>
                  <div className="flex flex-col gap-4">
                    <div className="gird-cols-4 grid h-20 w-full grid-flow-col place-items-center border-b-4 border-gray-600 text-sm font-bold md:text-lg">
                      <div className="w-full pb-2 pl-4 text-gray-600">▶ Selected Type</div>
                    </div>
                    <div className="mt-5 flex h-96 w-full flex-col gap-4 overflow-scroll scrollbar-hide lg:h-content">
                      {TOPIK_CATEGORY.filter((category) =>
                        exam.problems
                          .map((pb) => pb.type)
                          .find((key) => topikProblemTypes[key].category === category.id),
                      ).map((category) => (
                        <div className="flex flex-col gap-4" key={category}>
                          <div className="gird-cols-4 grid h-20 w-full grid-flow-col place-items-center text-sm font-bold md:text-lg">
                            <div
                              key={category.id}
                              className="w-full border-b-4 border-dotted border-indigo-600 pb-2 pl-4 text-indigo-600"
                            >
                              {category.title}
                            </div>
                          </div>
                          <div className="flex h-20 w-[70vw] gap-4 overflow-scroll lg:h-auto lg:w-auto lg:flex-col">
                            {exam.problems
                              .map((pb) => pb.type)
                              .filter((key) => topikProblemTypes[key].category === category.id)
                              .map((key) => (
                                <ProblemTypeCard
                                  key={key}
                                  selected={includedSelectedList.includes(key)}
                                  onClick={() => handleCategoryIncluded(key)}
                                  type={topikProblemTypes[key]}
                                />
                              ))}
                          </div>
                        </div>
                      ))}
                      {exam.problems.length === 0 && (
                        <div className="flex h-full w-full flex-col place-content-center place-items-center gap-y-10">
                          <p className="text-lg font-bold text-gray-400">Please choose a range of types</p>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
                <div className="grid h-full w-full grid-rows-[1fr_3fr] gap-10">
                  <div className="flex h-full w-full flex-col rounded-lg border-2 border-black p-4">
                    <div className="gird-cols-4 text-xm grid h-20 w-full grid-flow-col place-items-center border-b-4 border-gray-600 font-bold md:text-lg">
                      <div className="w-full pb-2 pl-4 text-gray-600">Selected Type Num</div>
                    </div>
                    <div className="flex h-full w-full flex-col place-content-center gap-2">
                      <div className="flex place-content-center place-items-center text-lg md:text-xl">
                        <input
                          disabled={includedSelectedList.length === 0}
                          value={calcIncludedSelectedCount()}
                          onChange={handleProblemCountChange}
                          type="number"
                          className="my-auto h-12 w-full place-items-center border-2 text-center"
                        />
                        <p className="whitespace-nowrap px-2">Count</p>
                      </div>
                      <p className="text-gray-500">* Integer range for 1~5</p>
                    </div>
                  </div>
                  <div className="flex h-full w-full flex-col rounded-lg border-2 border-black p-4">
                    <div className="flex h-20 w-full place-items-center border-b-4 border-gray-600 text-sm font-bold md:text-lg">
                      <div className="w-full pb-2 pl-4 text-gray-600">Test Setting</div>
                    </div>
                    <div className="w-ful mt-10 flex flex-col place-content-center gap-4">
                      <div className="flex flex-col place-content-center gap-2 text-lg md:text-xl">
                        <p className="whitespace-nowrap">Total Selected Type Num</p>
                        <p className="my-auto h-12 w-full place-content-center place-items-center border-2 px-5">
                          {exam.problems.reduce((acc, pb) => acc + pb.count, 0)}
                        </p>
                      </div>
                      <div className="flex flex-col place-content-center gap-2 text-lg md:text-xl">
                        <p className="whitespace-nowrap">Exam Title</p>
                        <input
                          value={exam.title}
                          onChange={handleExamTitleChange}
                          className="my-auto h-12 w-full place-items-center border-2 px-5"
                        />
                      </div>
                      <div className="flex place-content-center text-lg md:text-xl">
                        <input
                          checked={exam.includeCover}
                          onChange={handleExamCoverToggle}
                          type="checkbox"
                          className="my-auto h-6 w-6 text-center"
                        />
                        <p className="w-full whitespace-nowrap px-2">Include Cover</p>
                      </div>
                      <div className="flex place-content-center text-lg md:text-xl">
                        <input
                          checked={exam.includeAnswer}
                          onChange={handleExamAnswerToggle}
                          type="checkbox"
                          className="my-auto h-6 w-6 text-center"
                        />
                        <p className="w-full whitespace-nowrap px-2">Include Answer</p>
                      </div>
                      <div className="flex flex-col place-content-center gap-2 text-xl md:text-lg">
                        <p className="whitespace-nowrap"> ∎ Print Layout</p>
                        <div className="flex text-sm md:text-lg">
                          <input
                            checked={exam.printLayout === '1 Layer'}
                            onChange={() => handleExamLayoutChange('1 Layer')}
                            type="radio"
                            className="my-auto h-6 w-6"
                          />
                          <p className="w-full whitespace-nowrap px-2">
                            1 Layer
                            <br />
                            (Experimental)
                          </p>
                          <input
                            checked={exam.printLayout === '2 Layer'}
                            onChange={() => handleExamLayoutChange('2 Layer')}
                            type="radio"
                            className="my-auto h-6 w-6"
                          />
                          <p className="w-full whitespace-nowrap px-2">
                            2 Layer
                            <br />
                            (Default)
                          </p>
                        </div>
                      </div>
                    </div>
                    <div className="mt-auto flex h-20 w-full place-items-center gap-4 text-lg text-white md:text-xl">
                      <NeedAuth auth="TEACHER">
                        <button
                          onClick={handlePBTButtonClick}
                          type="button"
                          className="w-full rounded-lg bg-orange-600 hover:shadow-xl"
                        >
                          PBT
                        </button>
                      </NeedAuth>
                      <button
                        onClick={handleCBTButtonClick}
                        type="button"
                        className="w-full rounded-lg bg-indigo-600 hover:shadow-xl"
                      >
                        CBT
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </Container>
        </div>
        <div id="printContent" ref={ref} className={isLoading ? '' : 'hidden'}>
          {exam.printLayout === '1 Layer' && ( // 1단 출력 선택 시
            <PrintLayout>
              <Topik config={examConfig} userSelected={[]} setUserSelected={() => {}} />
            </PrintLayout>
          )}
          {exam.printLayout === '2 Layer' && ( // 2단 출력 선택 시
            <PrintTwoLayout>
              <Topik config={examConfig} userSelected={[]} setUserSelected={() => {}} />
            </PrintTwoLayout>
          )}
        </div>
        <SectionTitle pretitle="" title="">
          We provide the same CBT and PBT as the AI Generation questions.
        </SectionTitle>
      </div>
    </>
  );
}

export default Category;
