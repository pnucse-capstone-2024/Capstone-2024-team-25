/* eslint-disable no-unsafe-optional-chaining */
import { Input } from '@mui/material';

function ListenOneOne({ config, setConfig }) {
  return (
    <div className="mb-4 w-full">
      <div>
        <span className="font-bold">문제 ID:</span> {config?.problemId ?? ''}
      </div>

      {/* 문제 내용 */}
      <div className="mb-2 flex items-center justify-center">
        <span className="mr-2">※</span>
        <Input
          className="w-full"
          value={config?.problem ?? ''}
          onChange={(e) => {
            setConfig({ ...config, problem: e.target.value });
          }}
        />
      </div>

      {/* 보기 */}
      <div className="mb-2 border-2 border-gray-400 p-4">
        <div className="font-bold">&lt;보기&gt;</div>
        <div>{config?.example?.conversation[0] ?? ''}</div>
        <div>{config?.example?.conversation[1] ?? ''}</div>
        <div>
          {config?.example?.answers.map((answer, idx) => (
            <div key={answer} className="mb-2">
              <span className="mr-2">{idx}:</span>
              <Input
                value={answer ?? ''}
                onChange={(e) => {
                  const answers = [...config?.example.answers];
                  answers[idx] = e.target.value;

                  setConfig({
                    ...config,
                    example: {
                      ...config?.example,
                      answers,
                    },
                  });
                }}
              />
            </div>
          ))}
        </div>
      </div>

      {/* 각 세부 문항 */}
      {config?.questions?.map(({ answers, questionNumber, score }, questionIdx) => (
        <div key={questionNumber}>
          <div>
            <span className="font-bold">
              {questionNumber}. ({score}점)
            </span>
            <div className="pb-2">
              {answers.map(({ answer }, answerIdx) => (
                <div key={answer} className="mr-4 flex">
                  <span className="mr-2">{answerIdx}:</span>
                  <Input
                    value={answer ?? ''}
                    onChange={(e) => {
                      const newAnswers = [...config?.questions[questionIdx].answers];
                      newAnswers[answerIdx].answer = e.target.value;

                      const newQuestions = [...config?.questions];
                      newQuestions[questionIdx].answers = newAnswers;

                      setConfig({
                        ...config,
                        questions: newQuestions,
                      });
                    }}
                  />
                </div>
              ))}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}

export default ListenOneOne;
