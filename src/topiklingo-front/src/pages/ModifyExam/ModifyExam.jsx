import { useState } from 'react';
import { useLocation } from 'react-router-dom';

import { useEditExamMutate, useGetExamAnswerQuery, useGetExamQuery } from '@api/examApi';
import TopikEditor from '@components/exam/exam-types/TopikEditor';
import ExamLayout from '@components/layouts/ExamLayout';
import NotFound from '@pages/NotFound/NotFound';

function ModifyExam() {
  const location = useLocation();
  const urlSearchParams = new URLSearchParams(location.search);
  const examId = urlSearchParams.get('id');

  const { data, isLoading } = useGetExamQuery(examId);
  // eslint-disable-next-line no-unused-vars
  const { data: examAnswerData } = useGetExamAnswerQuery(examId);
  const { mutateAsync: editExam } = useEditExamMutate();

  const [examConfig, setExamConfig] = useState();

  const handleSubmit = async () => {
    if (window.confirm('저장하시겠습니까?')) {
      editExam({
        examId,
        problems: examConfig,
      }).then(() => {
        window.close();
      });
    }
  };

  if (!examId) {
    return <NotFound />;
  }

  if (isLoading) {
    return <div>데이터 받아오는 중...</div>;
  }

  return (
    <div className="p-4">
      <ExamLayout>
        <TopikEditor
          originConfig={data.config}
          setConfig={setExamConfig}
          answers={[-1, ...examAnswerData.map((ea) => ea.rightAnswer)]}
        />
      </ExamLayout>
      <button
        className="rounded bg-blue-400 px-6 py-2 text-white hover:bg-blue-300"
        type="button"
        onClick={handleSubmit}
      >
        저장
      </button>
    </div>
  );
}

export default ModifyExam;
