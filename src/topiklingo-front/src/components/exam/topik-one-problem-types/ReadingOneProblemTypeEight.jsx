import AnswersGridLayout from '../AnswersGridLayout';
import Border from '../Border';
import TextArea from '../exam-span/TextArea';
import QuestionContent from '../QuestionContent';
import QuestionHeader from '../QuestionHeader';

function ReadingOneProblemTypeEight({ config, getter, setter, editmode, userSelected, setUserSelected }) {
  return (
    <>
      {config.questions.map(({ questionId, questionNumber, question, score, answers, example }, index) => (
        <div key={questionNumber}>
          {index === 0 && (
            <>
              <QuestionHeader subItem="※" title={config.problem} size="lg" />
              <QuestionContent>
                <Border isThin>
                  <TextArea
                    className="whitespace-pre-wrap font-semibold"
                    value={getter?.problem(config.problemId).example.conversation}
                    onChange={(e) => {
                      setter?.problem(config.problemId, { conversation: e.target.value });
                    }}
                    editmode={editmode}
                  >
                    {config.example.conversation}
                  </TextArea>
                </Border>
              </QuestionContent>
              <div className="mb-4" />
            </>
          )}
          <QuestionHeader
            subItem={`${questionNumber}.`}
            title={
              <>
                <span className="whitespace-pre-wrap">{question}</span>
                <span className="ml-1">({score}점)</span>
              </>
            }
          />
          <QuestionContent>
            {example && (
              <Border isThin>
                <TextArea
                  className="font-semibold"
                  value={getter?.question(questionId).example[0][0]}
                  onChange={(e) => {
                    setter?.question(questionId, { example: [[e.target.value]] });
                  }}
                  editmode={editmode}
                >
                  {example}
                </TextArea>
              </Border>
            )}
          </QuestionContent>
          <QuestionContent>
            <AnswersGridLayout
              answers={answers}
              editmode={editmode}
              getter={getter}
              setter={setter}
              selected={userSelected[questionNumber]}
              setSelected={(number) => {
                const newSelected = [...userSelected];
                newSelected[questionNumber] = number;
                setUserSelected(() => newSelected);
              }}
              mode="fullWidth"
            />
          </QuestionContent>
        </div>
      ))}
    </>
  );
}

export default ReadingOneProblemTypeEight;
