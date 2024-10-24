import PastExamRow from './components/PastExamRow';

function PastExam() {
  return (
    <div className="h-full w-full">
      <div className="flex h-48 w-full items-center justify-center">
        <h1 className="text-4xl font-bold">기출 문제</h1>
      </div>
      <PastExamRow title="2023년 기출 문제" />
      <PastExamRow title="2022년 기출 문제" />
      <PastExamRow title="2021년 기출 문제" />
    </div>
  );
}

export default PastExam;
