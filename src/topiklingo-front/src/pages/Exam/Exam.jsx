import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import { useGetExamAnswerQuery, useGetExamQuery, usePostExamAnswerMutate } from '@api/examApi';
// import { userAtom } from '@atom';
import Topik from '@components/exam/exam-types/Topik';
import ExamLayout from '@components/layouts/ExamLayout';
import NotFound from '@pages/NotFound/NotFound';
// import { useSetAtom } from 'jotai';

import CustomAudioPlayer from './components/AudioPlayer';
import ExamCover from './components/ExamCover';
import ExamLoading from './components/ExamLoading';
import ExamTimer from './components/ExamTimer';
import OMRCard from './components/OMRCard';
import SubmitLoading from './components/SubmitLoading';

const calculateScore = (questions) => questions.reduce((acc, cur) => acc + cur.score, 0);

function Exam() {
  const [isExamInProgress, setIsExamInProgress] = useState(false);
  const [userSelected, setUserSelected] = useState([]);
  const [questionIds, setQuestionIds] = useState([]);
  const [resultModalIsOpen, setResultModalIsOpen] = useState(false);
  const [wrongQuestions, setWrongQuestions] = useState([]);
  const [correctQuestions, setCorrectQuestions] = useState([]);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [isSubmitLoading, setIsSubmitLoading] = useState(false);
  const [isTimeUp, setIsTimeUp] = useState(false);

  const location = useLocation();
  const urlSearchParams = new URLSearchParams(location.search);
  const examId = urlSearchParams.get('id');
  // const setUser = useSetAtom(userAtom);

  const { data, isLoading } = useGetExamQuery(examId?.toString());

  if (data?.code){
    console.log(data);
    alert(data.message);
    window.close();
  }

  const { data: examAnswerData } = useGetExamAnswerQuery(examId?.toString());
  const { mutate: postExamAnswer } = usePostExamAnswerMutate();

  const examTime = {
    TOPIK_1_LISTENING: { time: 40 * 60, label: 'topik1-listening (40분)' },
    TOPIK_1_READING: { time: 60 * 60, label: 'topik1-reading (60분)' },
    TOPIK_2_LISTENING: { time: 60 * 60, label: 'topik2-listening (60분)' },
    TOPIK_2_READING: { time: 70 * 60, label: 'topik2-reading (70분)' },
    GENERATED: { time: 90 * questionIds.length, label: `generated (${(90 * questionIds.length) / 60}분)` },
  };

  const handleSubmitExam = async () => {
    setIsSubmitLoading(true); // 로딩 시작

    // 로딩 화면을 3초간 유지하기 위해 setTimeout 사용
    setTimeout(async () => {
      setResultModalIsOpen(true);
      const userAnswers = [];
      for (let i = 0; i < data.totalQuestions; i += 1) {
        userAnswers.push({
          questionId: questionIds[i].id,
          questionNumber: questionIds[i].num,
          answer: userSelected[i + 1],
        });
      }

      const newCorrectQuestions = [];
      const newWrongQuestions = [];

      let idx = 0;

      examAnswerData.forEach((question) => {
        const correctAnswerId = `answer-${question.rightAnswer}`;
        const correctAnswerElement = document.getElementsByName(correctAnswerId).item(idx);
        idx += 1;
        if (correctAnswerElement) {
          correctAnswerElement.style.backgroundColor = 'lightcoral';
          correctAnswerElement.style.color = 'white';
          correctAnswerElement.style.textDecoration = 'underline';
        }
      });

      const isRightAnswer = (questionId) => {
        const userAnswer = userAnswers.find((answer) => answer.questionId === questionId);
        const question = examAnswerData.find((answer) => answer.questionId === questionId);
        return userAnswer.answer === question.rightAnswer;
      };

      const memberAnalyze = Object.keys(data.config).map((key) => ({
        type: key,
        total: data.config[key].reduce((acc, cur) => acc + cur.questions.length, 0),
        correct: data.config[key]
          .map((p) => p.questions.filter((q) => isRightAnswer(q.questionId)).length)
          .reduce((acc, cur) => acc + cur, 0),
      }));

      idx = 0;
      userAnswers.forEach((userAnswer) => {
        const question = examAnswerData.find((answer) => answer.questionId === userAnswer.questionId);
        if (question.rightAnswer === userAnswer.answer) {
          const correctAnswerId = `answer-${question.rightAnswer}`;
          const correctAnswerElement = document.getElementsByName(correctAnswerId).item(idx);
          if (correctAnswerElement) {
            correctAnswerElement.style.backgroundColor = 'midnightblue';
          }
          newCorrectQuestions.push({
            questionId: userAnswer.questionId,
            userAnswer: userAnswer.answer,
            rightAnswer: question.rightAnswer,
            score: question.score,
            questionNumber: userAnswer.questionNumber,
          });
        } else {
          newWrongQuestions.push({
            questionId: userAnswer.questionId,
            userAnswer: userAnswer.answer,
            rightAnswer: question.rightAnswer,
            score: question.score,
            questionNumber: userAnswer.questionNumber,
          });
        }
        idx += 1;
      });

      const examAnswerBody = {
        examId,
        memberAnswers: userAnswers.map((answer) => answer.answer).join(','),
        realAnswers: examAnswerData.map((answer) => answer.rightAnswer).join(','),
        score: calculateScore(newCorrectQuestions),
        memberAnalyzeRequest: {
          MemberAnalyze: memberAnalyze.map((v) => `${v.type.toUpperCase()}/${v.total}/${v.correct}`),
        },
      };

      postExamAnswer(examAnswerBody);

      setCorrectQuestions(newCorrectQuestions);
      setWrongQuestions(newWrongQuestions);
      setIsSubmitLoading(false); // 로딩 종료
      setIsSubmitted(true);
    }, 4000); // 4초 후 로직 실행
  };

  const handleTimeUp = (timeUp) => {
    if (!isTimeUp) {  // 이미 타임업된 경우 무시
      setIsTimeUp(timeUp);
      handleSubmitExam();
    }
  };

  const totalScore = () => calculateScore(correctQuestions) + calculateScore(wrongQuestions);

  const userScore = () => calculateScore(correctQuestions);

  useEffect(() => {
    if (data?.config) {
      const values = Object.values(data.config);
      const problems = [];
      values.forEach((value) => {
        problems.push(...value.map((v) => v.questions));
      });
      const questions = [];
      problems.forEach((problem) => {
        questions.push(...problem);
      });
      setQuestionIds(questions.map((q) => ({ id: q.questionId, num: q.questionNumber })));
      setUserSelected(() => Array(data.totalQuestions + 1).fill(-1));
    }
  }, [data]);

  if (isLoading) {
    return <ExamLoading />;
  }

  if (!examId) {
    return (
      <div>
        <NotFound />
      </div>
    );
  }

  if (data?.config) {
    if (!isExamInProgress) {
      const year = data?.year;
      const part = data?.type;
      if (part !== 'GENERATED') {
        const parts = part.split('_');
        const examTypeMap = { 1: 'I', 2: 'II' };
        const examType = examTypeMap[parts[1]];
        const subject = parts[2];
        let limitTime = examTime[data?.type]?.time;
        limitTime /= 60;
        return (
          <ExamCover
            year={year}
            examType={examType}
            subject={subject}
            timeLimit={limitTime}
            setIsExamInProgress={setIsExamInProgress}
          />
        );
      }
      let limitTime = examTime[data?.type]?.time;
      limitTime /= 60;
      return (
        <ExamCover
          year={year}
          examType="MIXED"
          subject={part}
          timeLimit={limitTime}
          setIsExamInProgress={setIsExamInProgress}
        />
      );
    }
  } else {
    <ExamCover
      year={2024}
      examType="error"
      subject="error"
      timeLimit={10 * 60}
      setIsExamInProgress={setIsExamInProgress}
    />;
  }

  return (
    <>
      {isSubmitLoading && <SubmitLoading />}
      <div
        className={`fixed left-0 top-0 z-10 flex h-screen w-screen items-center justify-center bg-black bg-opacity-50 ${resultModalIsOpen ? 'visible' : 'hidden'}`}
      >
        <div className="flex h-[300px] w-[400px] flex-col items-center justify-center rounded-lg bg-white p-6 shadow-lg">
          <div className="mb-4 w-full border-b pb-2 text-center text-2xl font-semibold text-gray-800">EXAM RESULT</div>
          <div className="mb-4 text-xl font-bold text-gray-800">
            User Score: <span className="text-red-500">{userScore()}</span> <br /> Total Score : {totalScore()}
          </div>
          <button
            onClick={() => setResultModalIsOpen(false)}
            type="button"
            className="h-10 w-32 rounded-full bg-midnightBlue text-white shadow-md transition duration-200 hover:bg-opacity-80"
          >
            Close
          </button>
        </div>
      </div>
      <div className="relative flex w-full justify-between">
        <div className="flex w-[900px] flex-col items-center px-8 pt-8">
          <div className="flex w-[800px] border-2">
            <ExamLayout
              wrongQuestions={wrongQuestions.map((q) => q.questionNumber)}
              correctQuestions={correctQuestions.map((q) => q.questionNumber)}
            >
              <Topik config={data?.config} userSelected={userSelected} setUserSelected={setUserSelected} />
            </ExamLayout>
          </div>
        </div>
        <div className="flex w-full flex-col">
          <div className="sticky top-8">
            {data?.type === 'TOPIK_1_LISTENING' || data?.type === 'TOPIK_2_LISTENING' ? (
              <div className="mb-4 w-[300px] rounded-lg border border-gray-300 p-4">
                <CustomAudioPlayer src={data?.listenUrl} isSubmit={isSubmitted} />
              </div>
            ) : null}
            <div className="mb-4 w-[300px] rounded-lg border border-gray-300 p-4">
              <span className="text-lg font-bold text-gray-700">Exam Timer</span>
              <div className="mt-2">
                <span className="mb-1 block">{examTime[data?.type]?.label}</span>
                <ExamTimer initialTime={examTime[data?.type]?.time} isSubmitted={isSubmitted} onTimeUp={handleTimeUp}/>
              </div>
            </div>
            <OMRCard
              wrongQuestions={wrongQuestions.map((q) => q.questionNumber)}
              correctQuestions={correctQuestions.map((q) => q.questionNumber)}
              userSelected={userSelected}
              setUserSelected={setUserSelected}
            />
            <button
              disabled={isSubmitted}
              onClick={handleSubmitExam}
              className="mt-4 h-16 w-[300px] bg-midnightBlue text-white hover:bg-opacity-70 disabled:cursor-not-allowed disabled:bg-gray-300 disabled:text-red-600"
              type="button"
            >
              {isSubmitted ? `Result Score : ${userScore()}/${totalScore()} \n` : 'Submit'}
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

export default Exam;
