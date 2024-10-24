import AnswersGridLayout from '../AnswersGridLayout';
import Border from '../Border';
import TextArea from '../exam-span/TextArea';
import QuestionContent from '../QuestionContent';
import QuestionHeader from '../QuestionHeader';

function ReadingOneProblemTypeThree({ config, getter, setter, editmode, userSelected, setUserSelected }) {
  return (
    <>
      {config.questions.map(({ questionId, questionNumber, example, score, answers }, index) => (
        <div key={questionNumber}>
          {index === 0 && (
            <QuestionHeader
              subItem="※"
              title={<span className="whitespace-pre-wrap">{config.problem}</span>}
              size="lg"
            />
          )}
          <QuestionHeader
            subItem={`${questionNumber}.`}
            title={
              <>
                <div className="mb-2">({score}점)</div>
                <Border isThin>
                  <TextArea
                    className="whitespace-pre-wrap"
                    value={getter?.question(questionId).example[0][0]}
                    onChange={(e) => {
                      setter?.question(questionId, { example: [[e.target.value]] });
                    }}
                    editmode={editmode}
                  >
                    {example}
                  </TextArea>
                </Border>
              </>
            }
          />
          <QuestionContent>
            <AnswersGridLayout
              answers={answers}
              getter={getter}
              setter={setter}
              editmode={editmode}
              selected={userSelected[questionNumber]}
              setSelected={(number) => {
                const newSelected = [...userSelected];
                newSelected[questionNumber] = number;
                setUserSelected(() => newSelected);
              }}
            />
          </QuestionContent>
        </div>
      ))}
    </>
  );
}

export default ReadingOneProblemTypeThree;
